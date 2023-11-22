package com.deckerben.utilities.msgpics.component;

import com.deckerben.component.SimpleImagePainter;
import com.deckerben.utilities.Utility;
import com.deckerben.utilities.msgpics.MessagePicturator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MessagePicturatorPanel extends JPanel implements Utility {

    //Felder
    private final MessagePicturator converter = new MessagePicturator();

    private final JTextArea msgIO = new JTextArea();
    private final SimpleImagePainter imgIO = new SimpleImagePainter(getMsgToPicture(), false);

    private final SpinnerNumberModel widthModel = new SpinnerNumberModel(1,1,null,1);
    private final SpinnerNumberModel heightModel = new SpinnerNumberModel(1,1,null,1);

    //Konstruktoren
    public MessagePicturatorPanel(){
        //Super
        super(new GridLayout(1,1));

        //Component-Deklaration
        JButton importTextButton = new JButton("Text aus Datei importieren (.txt)");
        JButton importImageButton = new JButton("Bild aus Datei importieren (.png)");
        JButton pasteImageButton = new JButton("Bild aus Zwischenablage einfügen");

        JButton convertMsgButton = new JButton("Nachricht --> Bild");
        JButton convertImgButton = new JButton("Bild --> Nachricht");

        JButton exportTextButton = new JButton("Text zu Datei exportieren (.txt)");
        JButton exportImageButton = new JButton("Bild zu Datei exportieren (.png)");
        JButton copyImageButton = new JButton("Bild in Zwischenablage kopieren");

        JSpinner widthSpinner = new JSpinner(widthModel);
        JSpinner heightSpinner = new JSpinner(heightModel);

        JButton resetWidthButton = new JButton("↺");
        JButton resetHeightButton = new JButton("↺");

        //Listener-Deklaration
        ActionListener importTextListener = e -> importText();
        ActionListener importImageListener = e -> importImage();
        ActionListener pasteImageListener = e -> importImageFromClipboard();

        ActionListener convertMsgListener = e -> updateImgIO();
        ActionListener convertImgListener = e -> updateMsgIO();

        ChangeListener ratioListener = e -> updateImgIO();

        ActionListener resetWidthListener = e -> widthModel.setValue(1);
        ActionListener resetHeightListener = e -> heightModel.setValue(1);

        ActionListener exportTextListener = e -> exportText();
        ActionListener exportImageListener = e -> exportImage();
        ActionListener copyImagelistener = e -> exportImageToClipboard();

        CaretListener imgIOTextUpdater = e -> updateImgIO();
        PropertyChangeListener imgIOResizeUpdater = evt -> updateImgIO();

        //Reset
        reset();

        //Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
            JPanel textPanel = new JPanel(new BorderLayout());
                textPanel.add(importTextButton,BorderLayout.NORTH);
                textPanel.add(new JScrollPane(msgIO),BorderLayout.CENTER);
                JPanel genExMsgPanel = new JPanel(new GridLayout(2,1));
                    genExMsgPanel.add(convertMsgButton);
                    genExMsgPanel.add(exportTextButton);
                textPanel.add(genExMsgPanel,BorderLayout.SOUTH);
            splitPane.setLeftComponent(textPanel);
            JPanel imgPanel = new JPanel(new BorderLayout());
                JPanel imRatPanel = new JPanel(new GridLayout(2,2));
                    imRatPanel.add(importImageButton);
                    imRatPanel.add(pasteImageButton);
                    JPanel widthPanel = new JPanel(new BorderLayout());
                        widthPanel.add(widthSpinner,BorderLayout.CENTER);
                        widthPanel.add(resetWidthButton,BorderLayout.WEST);
                    imRatPanel.add(widthPanel);
                    JPanel heightPanel = new JPanel(new BorderLayout());
                        heightPanel.add(heightSpinner,BorderLayout.CENTER);
                        heightPanel.add(resetHeightButton,BorderLayout.EAST);
                    imRatPanel.add(heightPanel);
                imgPanel.add(imRatPanel,BorderLayout.NORTH);
                imgPanel.add(imgIO,BorderLayout.CENTER);
                JPanel genExImgPanel = new JPanel(new GridLayout(2,1));
                    genExImgPanel.add(convertImgButton);
                    JPanel exCopyImgPanel = new JPanel(new GridLayout(1,2));
                        exCopyImgPanel.add(exportImageButton);
                        exCopyImgPanel.add(copyImageButton);
                    genExImgPanel.add(exCopyImgPanel);
                imgPanel.add(genExImgPanel,BorderLayout.SOUTH);
            splitPane.setRightComponent(imgPanel);
        add(splitPane);

        //Einstellungen vornehmen
        msgIO.setLineWrap(true);
        msgIO.setWrapStyleWord(true);
        widthSpinner.setToolTipText("Relative Breite des Bildes im Bezug zu seiner Höhe.");
        heightSpinner.setToolTipText("Relative Höhe des Bildes im Bezug zu seiner Breite.");
        resetWidthButton.setToolTipText("Relative Breite zurücksetzen");
        resetHeightButton.setToolTipText("Relative Höhe zurücksetzen");
        splitPane.setOneTouchExpandable(true);

        //Listenerzuweisung
        msgIO.addCaretListener(imgIOTextUpdater);
        splitPane.addPropertyChangeListener(imgIOResizeUpdater);
        importTextButton.addActionListener(importTextListener);
        importImageButton.addActionListener(importImageListener);
        pasteImageButton.addActionListener(pasteImageListener);
        convertMsgButton.addActionListener(convertMsgListener);
        convertImgButton.addActionListener(convertImgListener);
        widthSpinner.addChangeListener(ratioListener);
        heightSpinner.addChangeListener(ratioListener);
        resetWidthButton.addActionListener(resetWidthListener);
        resetHeightButton.addActionListener(resetHeightListener);
        exportTextButton.addActionListener(exportTextListener);
        exportImageButton.addActionListener(exportImageListener);
        copyImageButton.addActionListener(copyImagelistener);
    }

    //Methoden
    ////Update-Management
    private void updateImgIO(){
        applyRatio();
        imgIO.setImage(getMsgToPicture(), false);
        updateUI();
    }

    private void updateMsgIO() {
        msgIO.setText(getPictureToMsg());
    }

    ////Importieren
    private void importImageFromClipboard() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            imgIO.setImage((BufferedImage)c.getContents(this).getTransferData(DataFlavor.imageFlavor),false);
            updateUI();
            updateMsgIO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void importText(){
        importFile(new FileNameExtensionFilter("Text (.txt)","txt"), selectedFile -> {
            String readContent = "";
            StringBuilder contentBuilder = new StringBuilder(readContent);
            try (FileReader fileReader = new FileReader(selectedFile)) {
                int readChar;
                do {
                    readChar = fileReader.read();
                    if (readChar >=0) contentBuilder.append((char) readChar);
                } while (readChar >=0);
            } catch (Exception e) {
                contentBuilder.replace(0,contentBuilder.length(),"Fehler beim Lesen der Datei.");
                throw new RuntimeException(e);
            }   finally {
                msgIO.setText(contentBuilder.toString());
            }
        });
    }

    private void importImage(){
        importFile(new FileNameExtensionFilter("Bild (.png)","png"), selectedFile -> {
            try {
                imgIO.setImage(ImageIO.read(selectedFile));
                updateUI();
                updateMsgIO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    ////Import-Kern
    private void importFile(FileNameExtensionFilter filter , Consumer<File> loadActions){
        JFileChooser loadChooser = new JFileChooser();
        loadChooser.setAcceptAllFileFilterUsed(filter == null);
        if (filter != null) loadChooser.addChoosableFileFilter(filter);
        if (loadChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File selectedFile = loadChooser.getSelectedFile();
            loadActions.accept(selectedFile);
        }
    }

    ////Exportieren
    private void exportImageToClipboard() {
        TransferableImage trans = new TransferableImage(getMsgToPicture());
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, null);
    }

    private void exportText() {
        exportFile(new FileNameExtensionFilter("Text (.txt)","txt"),(selectedFile, format) -> {
            try {
                FileWriter textExporter = new FileWriter(selectedFile);
                textExporter.write(msgIO.getText());
                textExporter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void exportImage() {
        exportFile(new FileNameExtensionFilter("Bild (.png)","png"),(selectedFile, format) -> {
            applyRatio();
            try {
                ImageIO.write(getMsgToPicture(), format, selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    ////Export-Kern
    private void exportFile(FileNameExtensionFilter filter , BiConsumer<File, String> saveActions) {
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setAcceptAllFileFilterUsed(filter == null);
        if (filter != null) saveChooser.addChoosableFileFilter(filter);
        File selectedFile;
        if (saveChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            selectedFile = saveChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            FileNameExtensionFilter extension = (FileNameExtensionFilter) saveChooser.getFileFilter();
            String fileType = extension.getExtensions()[0];
            if(!path.endsWith(fileType)){
                selectedFile = new File(path +"."+ fileType);
            }

            String[] formats = selectedFile.getAbsolutePath().split("\\.");
            String format = formats[formats.length-1];

            try {
                boolean writingAllowed = selectedFile.createNewFile();
                if (!writingAllowed) {
                    int result = JOptionPane.showConfirmDialog(this,"Die Datei existiert bereits. Überschreiben?","Überschreiben?",JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        writingAllowed = true;
                    }
                }
                if (writingAllowed) {
                    saveActions.accept(selectedFile,format); //HAHA! BiConsumer! Wie Geil ist das denn?
                }
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this,"Fehler beim Überschreiben der Datei.");
                e.printStackTrace();
            }
        }
    }

    private void applyRatio() {
        converter.setPreferredRatio(new Dimension(widthModel.getNumber().intValue(),heightModel.getNumber().intValue()));
    }

    //Getter
    private BufferedImage getMsgToPicture() {
        return converter.msgToPicture(msgIO.getText());
    }

    private String getPictureToMsg() {
        return MessagePicturator.pictureToMsg(imgIO.getImage());
    }

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode(){
        converter.reset();
        widthModel.setValue(1);
        heightModel.setValue(1);
        msgIO.setText("Einfach Text eingeben und direkt sehen, wie sich das Bild verändert!");
        updateImgIO();
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "MsgPics";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new MessagePicturatorPanel();
    }

    //Interne Klassen
    ////TransferableImage
    ////Speicherform eines Bilds in der Zwischenablage
    private record TransferableImage(Image i) implements Transferable {

        //Getter
         public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
                return i;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (DataFlavor dataFlavor : flavors) {
                if (flavor.equals(dataFlavor)) {
                    return true;
                }
            }
            return false;
        }
    }
}
