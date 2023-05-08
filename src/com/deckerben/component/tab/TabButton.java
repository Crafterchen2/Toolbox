package com.deckerben.component.tab;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TabButton extends JButton {

    private JLabel sizeTester = new JLabel();

    protected Component tabComponent = this;

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    public TabButton() {
        setup();
    }

    public TabButton(String text){
        super(text);
        setup();
    }

    public TabButton(Icon icon) {
        super(icon);
        setup();
    }

    public TabButton(Action a) {
        super(a);
        setup();
    }

    public TabButton(String text, Icon icon) {
        super(text, icon);
        setup();
    }

    private void setup(){
        int size = 17;
        sizeTester.setText(getText());
        updateSize();
        setUI(new BasicButtonUI());
        setContentAreaFilled(false);
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        addMouseListener(buttonMouseListener);
        setRolloverEnabled(true);
    }

     @Override
     public void setText(String text){
        try {
            sizeTester.setText(text);
        }   catch (Exception e){

        }
        super.setText(text);
     }

     public void updateSize(){
        setPreferredSize(new Dimension(Math.max(sizeTester.getMinimumSize().width+5,sizeTester.getMinimumSize().height), sizeTester.getMinimumSize().height));
     }

    public Component getTabComponent() {
        return tabComponent;
    }

    public void setTabComponent(Component tabComponent) {
        this.tabComponent = tabComponent;
    }
}
