package com.github.crafterchen2.toolbox.utilities.clicker;


import com.github.crafterchen2.toolbox.Tool;
import com.github.crafterchen2.toolbox.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Tool
public class ClickerPanel extends JPanel implements Utility {
  
  //Felder
  private int num; 
  
  private final JButton clickerButton = new JButton();
  private final JButton resetButton = new JButton("Reset");
  
  private final SpinnerNumberModel factorStepSizeModel = new SpinnerNumberModel();
  private final SpinnerNumberModel factorModel = new SpinnerNumberModel();

  //Konstruktoren
  public ClickerPanel() {    
    reset();
    //Layout
    setLayout(new BorderLayout());
    add(clickerButton,BorderLayout.CENTER);
    JPanel configPanel = new JPanel(new GridLayout(1,3));
    JSpinner factorSpinner = new JSpinner(factorModel);
    configPanel.add(factorSpinner);
    JSpinner factorStepSizeSpinner = new JSpinner(factorStepSizeModel);
    configPanel.add(factorStepSizeSpinner);
    configPanel.add(resetButton);
    add(configPanel,BorderLayout.SOUTH);
    //Listener zuweisen
    MouseAdapter clickerLs = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        int isRightClick = 1;
        if (SwingUtilities.isRightMouseButton(e)) isRightClick = -1;
        num = num + (factorModel.getNumber().intValue() * isRightClick);
        updateClickerButtonText();
        setResetTooltip();
      }
    };
    clickerButton.addMouseListener(clickerLs);
    resetButton.addActionListener(e -> {
      if (num == 0) {
        reset();
      } else {
        setResetTooltipAll();
        num = 0;
        updateClickerButtonText();
      }
    });
    factorStepSizeSpinner.addChangeListener(e -> factorModel.setStepSize(factorStepSizeModel.getNumber().intValue()));
    //Einstellungen tätigen
    factorStepSizeSpinner.setToolTipText("Um wie viel der Faktor erhöht/verringert werden soll beim anklicken eines Pfeiles.");
    factorSpinner.setToolTipText("Um wie viel der Wert erhöht/verringert werden soll beim anklicken des Knopfes.");
    
  }

  //Methoden
  private void updateClickerButtonText() {
    clickerButton.setText(String.valueOf(num));
  }

  //Getter
  @SuppressWarnings("unused")
  public int getNum(){
    return num;
  }

  //Setter
  private void setResetTooltip(){
    if (num == 0) {
      setResetTooltipAll();
    } else {
      setResetTooltipValue();
    }
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

  //Overrides aus
  ////Resettable
  @Override
  public boolean canReset() {
    return true;
  }

  @Override
  public void resetCode(){
    setNum(0);

    clickerButton.setText("Klick mich!");

    setResetTooltipAll();

    factorStepSizeModel.setValue(1);
    factorStepSizeModel.setStepSize(1);

    factorModel.setValue(1);
    factorModel.setStepSize(1);
  }

  ////Utilities
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

}
