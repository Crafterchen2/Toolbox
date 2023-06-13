package com.deckerben.utilities.randombit;

import com.deckerben.utilities.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class RandomBitPanel extends JPanel implements Utility {

    //Felder
    private final SpinnerNumberModel amountModel = new SpinnerNumberModel();

    private final JTextArea outputArea = new JTextArea();
    private final JButton copyButton = new JButton("⎘");

    //Konstruktoren
    public RandomBitPanel(){
        //Super
        super(new BorderLayout());
        //Layout
        JPanel settingPanel = new JPanel(new BorderLayout());
        JSpinner amountSpinner = new JSpinner(amountModel);
        settingPanel.add(amountSpinner,BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset");
        settingPanel.add(resetButton,BorderLayout.EAST);
        JButton generateButton = new JButton();
        add(settingPanel,BorderLayout.NORTH);
        add(outputArea,BorderLayout.CENTER);
        JPanel copyPanel = new JPanel(new BorderLayout());
        copyPanel.add(copyButton,BorderLayout.EAST);
        copyPanel.add(generateButton,BorderLayout.CENTER);
        settingPanel.add(copyPanel, BorderLayout.SOUTH);
        //Einstellungen
        generateButton.setText("Generieren");
        copyButton.setFont(copyPanel.getFont().deriveFont(20f));
        copyButton.setPreferredSize(new Dimension(resetButton.getPreferredSize().width,copyButton.getPreferredSize().height));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLoweredBevelBorder());
        amountModel.setMinimum(1);
        amountModel.setStepSize(1);
        generateButton.addActionListener(ae -> {
            outputArea.setText(generateBits(amountModel.getNumber().intValue()));
            copyButton.setEnabled(true);
        });
        copyButton.addActionListener(ae -> copyToClipboard(outputArea.getText()));
        resetButton.addActionListener(ae -> reset());
        generateButton.setMargin(new Insets(1,1,1,1));
        //Reset
        reset();
    }

    //Methoden
    private void copyToClipboard(String toBeCopied){
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection sel = new StringSelection(toBeCopied);
        clip.setContents(sel, sel);
    }

    private String generateBits(int length){
        StringBuilder rv = new StringBuilder();
        long bit;
        for (int i = Math.abs(4-length%4); i<(length+Math.abs(4-length%4)); i++) {
            if (i%4 == 0) {
                rv.append(" ");
            }
            bit = (long)(Math.random()*2);
            rv.append(bit);
        }
        return rv.toString().trim();
    }

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        outputArea.setText("");
        amountModel.setValue(8);
        copyButton.setEnabled(false);
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "Bitfolgengenerator";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new RandomBitPanel();
    }
}