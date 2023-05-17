package com.deckerben.utilities.randombit;

import com.deckerben.utilities.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HexFormat;

public class RandomBitPanel extends JPanel implements Utility {

    private final SpinnerNumberModel amountModel = new SpinnerNumberModel();
    private final JSpinner amountSpinner = new JSpinner(amountModel);

    private final JButton generateButton = new JButton();

    private final JButton copyButton = new JButton("In Zwischenablage kopieren");

    private long decValue = 0;
    private String hexValue = "";
    private final HexFormat hexFormat = HexFormat.of().withUpperCase();

    public RandomBitPanel(){
        //Super
        super(new BorderLayout());
        //Layout
        JPanel settingPanel = new JPanel(new BorderLayout());
        settingPanel.add(amountSpinner,BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset");
        settingPanel.add(resetButton,BorderLayout.EAST);
        add(settingPanel,BorderLayout.NORTH);
        add(generateButton, BorderLayout.CENTER);
        JPanel copyPanel = new JPanel(new BorderLayout());
        copyPanel.add(copyButton,BorderLayout.CENTER);
        JComboBox<String> formatSelector = new JComboBox<>();
        copyPanel.add(formatSelector,BorderLayout.WEST);     //TODO Kopierfunktion von Vorberereitung realisieren
        //Einstellungen

        amountModel.setMinimum(make1Integer());
        amountModel.setStepSize(make1Integer());
        generateButton.addActionListener(ae -> {
            generateButton.setText(generateBits(amountModel.getNumber().intValue()));
            copyButton.setEnabled(true);
        });
        copyButton.addActionListener(ae -> copyToClipboard(generateButton.getText()));
        resetButton.addActionListener(ae -> reset());
        generateButton.setMargin(new Insets(1,1,1,1));
        //Reset
        reset();
    }

    private void copyToClipboard(String toBeCopied){
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection sel = new StringSelection(toBeCopied);
        clip.setContents(sel, sel);
    }

    private String generateBits(int length){
        StringBuilder rv = new StringBuilder();
        long bit;
        long powerBit;
        decValue = 0;
        for (int i = Math.abs(4-length%4); i<(length+Math.abs(4-length%4)); i++) {
            if (i%4 == 0) {
                rv.append(" ");
            }
            bit = (long)(Math.random()*2);
            powerBit = bit * (long) Math.pow(2,i-Math.abs(4-length%4));
            decValue = decValue + powerBit;
            rv.append(bit);
        }
        hexValue = hexFormat.toHexDigits(decValue);
        return rv.toString().trim();
    }

    private Integer make1Integer(){
        return makeInteger(1);
    }

    private Integer makeInteger(int value){
        return value;
    }

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        amountSpinner.setValue(makeInteger(8));
        generateButton.setText("Generieren");
        copyButton.setEnabled(false);
        decValue = 0;
        hexValue = "";
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