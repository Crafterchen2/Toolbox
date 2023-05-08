package com.deckerben.utilities.clicker;


import com.deckerben.utilities.Utility;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;

public class ClickerPanel extends JPanel implements Utility {
  
  //Attribute  
  
  private int num; 
  
  private JButton clickerButton = new JButton();
  private JButton resetButton = new JButton("Reset");
  
  private SpinnerNumberModel factorStepSizeModel = new SpinnerNumberModel();
  private JSpinner factorStepSizeSpinner = new JSpinner(factorStepSizeModel);
  
  private SpinnerNumberModel factorModel = new SpinnerNumberModel();
  private JSpinner factorSpinner = new JSpinner(factorModel);
  
  private ChangeListener stepSizeLs = new ChangeListener(){
    public void stateChanged(ChangeEvent ce){
      factorModel.setStepSize(factorStepSizeModel.getNumber().intValue());
    }
  };
  
  private ActionListener resetLs = new ActionListener(){
    public void actionPerformed(ActionEvent ae){
      if (num == 0) {
        reset();
      } else {
        setResetTooltipAll();
        num = 0;
        updateClickerButtonText();
      } // end of if-else
    }
  }; 
  
  private MouseAdapter clickerLs = new MouseAdapter(){
    public void mousePressed(MouseEvent e){
      int isRightClick = 1;
      if (SwingUtilities.isRightMouseButton(e)) isRightClick = -1;
      num = num+(factorModel.getNumber().intValue()*isRightClick);
      updateClickerButtonText();
      setResetTooltip();
    }
  }; 
 
  // Ende Attribute
  
  public ClickerPanel() {    
    reset();
    //Layout
    setLayout(new BorderLayout());
    add(clickerButton,BorderLayout.CENTER);
    JPanel configPanel = new JPanel(new GridLayout(1,3));
    configPanel.add(factorSpinner);
    configPanel.add(factorStepSizeSpinner);
    configPanel.add(resetButton);
    add(configPanel,BorderLayout.SOUTH);
    //Listener zuweisen
    clickerButton.addMouseListener(clickerLs);
    resetButton.addActionListener(resetLs);
    factorStepSizeSpinner.addChangeListener(stepSizeLs);
    //Einstellungen tätigen
    factorStepSizeSpinner.setToolTipText("Um wie viel der Faktor erhöht/verringert werden soll beim anklicken eines Pfeiles.");
    factorSpinner.setToolTipText("Um wie viel der Wert erhöht/verringert werden soll beim anklicken des Knopfes.");
    
  } // end of public ClickerPanel
  
  // Anfang Methoden

  @Override
  public boolean canReset() {
    return true;
  }

  public void resetCode(){
    setNum(0);

    clickerButton.setText("Klick mich!");

    setResetTooltipAll();

    factorStepSizeSpinner.setValue(make1Integer());
    factorStepSizeModel.setStepSize(make1Integer());

    factorSpinner.setValue(make1Integer());
    factorModel.setStepSize(make1Integer());
  }
  
  private void setResetTooltip(){
    if (num == 0) {
      setResetTooltipAll();
    } else {
      setResetTooltipValue();
    } // end of if-else
  }                  
  
  private void setResetTooltipAll(){
    resetButton.setToolTipText("Setzt alles auf Standardwerte zurück.");
  }
  
  private void setResetTooltipValue(){
    resetButton.setToolTipText("Setzt den Wert auf 0.");
  }
  
  public void setNum(int value){
    num = value;
  }
  
  public int getNum(){
    return num;
  }
  
  private Integer make1Integer(){
    return makeInteger(1);
  }
  
  private Integer makeInteger(int value){
    return Integer.valueOf(value);
  }
  
  private void updateClickerButtonText() {
    clickerButton.setText(""+num);
  }

  @Override
  public String getUtilitiyName() {
    return "Clicker";
  }

  @Override
  public Component getComponent() {
    return this;
  }

  @Override
  public Component createNewInstance() {
    return new ClickerPanel();
  }

  // Ende Methoden
} // end of class ClickerPanel
