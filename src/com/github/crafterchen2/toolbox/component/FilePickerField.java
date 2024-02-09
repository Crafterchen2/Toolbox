package com.github.crafterchen2.toolbox.component;

import com.github.crafterchen2.toolbox.Resettable;

import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.*;
import java.util.List;

public class FilePickerField extends JComponent implements Resettable {

    //Fields
    private final boolean initHistorical;
    private final FileFilter[] initFileFilter;
    private final JFileChooser chooser = new JFileChooser();
    private final Vector<File> history = new Vector<>();
    private final CardLayout displayLayout = new CardLayout();
    private final JPanel displayPanel = new JPanel(displayLayout);
    private final JComboBox<File> box = new JComboBox<>(history);
    private final JTextField field = new JTextField();

    private HashSet<FileFilter> fileFilter;
    private boolean historical;
    private File selected;
    private boolean pupupOpen = false;

    //Constructor
    public FilePickerField(){
        this(true);
    }

    public FilePickerField(boolean historical) {
        this(historical, (FileFilter) null);
    }

    public FilePickerField(FileFilter... fileFilters){
        this(true,fileFilters);
    }

    public FilePickerField(boolean historical, FileFilter... fileFilters) {
        this.initHistorical = historical;
        this.initFileFilter = fileFilters;
        reset();
        box.setEditable(true);
        box.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxEdited")) {
                Object item = box.getEditor().getItem();
                File edited = (item instanceof String string) ? new File(string) : ((item instanceof File file) ? file : new File("C:\\"));
                if (accepted(edited)) {
                    addToHistory(edited);
                    setSelected(edited);
                }
            }   else {
                if (box.getSelectedItem() instanceof File file) setSelected(file);
            }
        });
        field.setFont(field.getFont().deriveFont(Font.BOLD));
        field.addActionListener(e -> setSelected(new File(field.getText())));
        //chooser.setAcceptAllFileFilterUsed(false);
        //chooser.addChoosableFileFilter(new FileNameFilter("Resource pack (pack.mcmeta)", "pack.mcmeta"));
        setLayout(new BorderLayout());
        JButton picker = new JButton("Pick File");
        picker.addActionListener(e -> {
            pupupOpen = true;
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setSelected(chooser.getSelectedFile());
            pupupOpen = false;
        });
        add(picker, BorderLayout.EAST);
        displayLayout.addLayoutComponent(field,"field");
        displayLayout.addLayoutComponent(box,"box");
        displayPanel.add(box);
        displayPanel.add(field);
        setDisplayComp();
        add(displayPanel,BorderLayout.CENTER);
    }

    //Methods
    private void updateDisplay(){
        if (isHistorical()) {
            updateBox();
        } else {
            updateField();
        }
    }

    private void updateChooser(){
        chooser.setCurrentDirectory(getSelected());
    }

    private void updateBox() {
        if (hasSelection()) {
            box.setSelectedItem(getSelected());
        }   else {
            box.setSelectedItem("");
        }
    }

    private void updateField() {
        if (hasSelection()) {
            field.setText(getSelected().toString());
        }   else {
            field.setText("");
        }
    }

    private void updateFilter(){
        chooser.resetChoosableFileFilters();
        if (hasFileFilter()) {
            chooser.setAcceptAllFileFilterUsed(false);
            for (FileFilter filter : fileFilter) {
                chooser.addChoosableFileFilter(filter);
            }
        } else {
            chooser.setAcceptAllFileFilterUsed(true);
        }
    }

    public boolean accepted(File file) {
        if (!hasFileFilter()) return true;
        for (FileFilter filter : fileFilter) {
            if (filter.accept(file)) return true;
        }
        return false;
    }

    public void clearHistory(){
        history.clear();
    }

    public void addToHistory(File toAdd){
        if (!history.contains(toAdd)) history.add(toAdd);
    }

    public void addFileFilter(FileFilter toAdd) {
        if (existsFileFilter()) {
            fileFilter.add(toAdd);
        } else {
            setFileFilter(toAdd);
        }
    }

    public void removeFileFilter(FileFilter toRemove) {
        if (existsFileFilter()) fileFilter.remove(toRemove);
    }

    public boolean existsFileFilter(){
        return fileFilter != null;
    }

    public boolean hasFileFilter(){
        return existsFileFilter() || !fileFilter.isEmpty();
    }

    public boolean hasSelection(){
        return selected != null;
    }

    public boolean isHistorical() {
        return historical;
    }

    public boolean isPupupOpen(){
        return pupupOpen;
    }

    //Getter
    public File getSelected() {
        return selected;
    }

    public Vector<File> getHistory() {
        return history;
    }

    public HashSet<FileFilter> getFileFilter(){
        return fileFilter;
    }

    //Setter
    public void setHistorical(boolean historical) {
        this.historical = historical;
        setSelected(getSelected());
        setDisplayComp();
    }

    public void setSelected(File selected) {
        this.selected = selected;
        if (isHistorical()) {
            addToHistory(selected);
            updateBox();
        } else {
            updateField();
        }
        updateChooser();
    }

    public void setFileFilter(FileFilter... fileFilters){
        fileFilter = new HashSet<>(Arrays.asList(fileFilters));
        fileFilter.remove(null);
        updateFilter();
    }

    private void setDisplayComp(){
        if (isHistorical()) {
            displayLayout.show(displayPanel,"box");
            updateBox();
        }   else {
            displayLayout.show(displayPanel,"field");
            updateField();
        }
    }

    //Overrides from
    ////Resettable
    @Override
    public boolean canReset() {
        return !pupupOpen;
    }

    @Override
    public void resetCode() {
        historical = initHistorical;
        setFileFilter(initFileFilter);
        selected = null;
        clearHistory();
        updateDisplay();
        updateChooser();
    }

}
