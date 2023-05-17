package com.deckerben.utilities.selector;

import com.deckerben.Resettable;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ChanceModule extends JComponent implements Resettable {

    //Felder
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final SpinnerNumberModel amountModel = new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1);
    private final JSpinner amountSpinner = new JSpinner(amountModel);

    private final JTextField nameField = new JTextField();

    private final JPanel parent;

    private ChanceModule me = null;

    private int lowerChance = 0;

    //Listener

    //Konstruktoren
    public ChanceModule(JPanel parent){
        //Parameter
        this.parent = parent;
        //Reset
        reset();
        //Layout
        setLayout(new BorderLayout());
        add(amountSpinner,BorderLayout.WEST);
        add(nameField,BorderLayout.CENTER);
        JButton deleteButton = new JButton("X");
        add(deleteButton,BorderLayout.EAST);
        //Listener Zuweisen
        deleteButton.addActionListener(ae -> {
            parent.remove(me);
            parent.updateUI();
        });
        amountSpinner.addChangeListener(ce -> parent.updateUI());
        //Einstellungen
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        deleteButton.setToolTipText("Lösche diesen Eintrag.");
        //Selbstreferenz
        me = this;
    }

    //Methoden
    public void resetColor(){
        nameField.setBackground(DEFAULT_COLOR);
    }

    //Getter
    public int getLowerChance() {
        return lowerChance;
    }

    public int getUpperChance() {
        return lowerChance+getAmount();
    }

    public int getAmount(){
        return amountModel.getNumber().intValue();
    }

    public String getName(){
        return nameField.getText();
    }

    //Setter
    public void select(){
        nameField.setBackground(SELECTED_COLOR);
    }

    public void setLowerChance(int chance) {
        lowerChance = chance;
    }

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        amountSpinner.setValue(1);
        resetColor();
        nameField.setText("Element "+(parent.getComponentCount()+1));
        lowerChance = 0;
    }
}