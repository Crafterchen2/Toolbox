package com.deckerben.component.tab;

import javax.swing.*;
import java.awt.*;

public class TabLabel extends JLabel {

    private final JTabbedPane pane;
    private String text;

    @SuppressWarnings("unused")
    public TabLabel(String text, Icon icon, int horizontalAlignment, JTabbedPane pane) {
        super(text, icon, horizontalAlignment);
        this.pane = pane;
    }

    @SuppressWarnings("unused")
    public TabLabel(String text, int horizontalAlignment, JTabbedPane pane) {
        super(text, horizontalAlignment);
        this.pane = pane;
    }

    public TabLabel(String text, JTabbedPane pane) {
        super(text);
        this.pane = pane;
    }

    @SuppressWarnings("unused")
    public TabLabel(Icon image, int horizontalAlignment, JTabbedPane pane) {
        super(image, horizontalAlignment);
        this.pane = pane;
    }

    @SuppressWarnings("unused")
    public TabLabel(Icon image, JTabbedPane pane) {
        super(image);
        this.pane = pane;
    }

    @SuppressWarnings("unused")
    public TabLabel(JTabbedPane pane) {
        this.pane = pane;
    }

    @Override
    public String getText() {
        if (text != null) return text;
        int i = pane.indexOfTabComponent(this);
        if (i > -1) {
            return pane.getTitleAt(i);
        }
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        super.setText(text);
    }

    public JPanel generateRecommendedPanel(){
        JPanel rv = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rv.setOpaque(false);
        rv.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        rv.add(this);
        return rv;
    }

    /**
    * Beispielhafte Nutzung:
    * TabLabel tabLabel = new TabLabel(aTabbedPane);
    * TabButton aTabButton = new TabButton("Knopf Nr.1");
    * TabButton anotherTabButton = new TabButton("Knopf Nr.2");
    * JPanel tabPanel = tabLabel.generateRecommendedPanel(true,aTabButton,anotherTabButton);
    * aTabbedPane.setTabComponentAt(tabIndexInt, tabPanel);
    * */
    public JPanel generateRecommendedPanel(boolean makeCloseButton, Component... otherComponents){
        JPanel rv = generateRecommendedPanel();
        rv.add(this);
        for (Component value : otherComponents) {
            try {
                TabButton tb = (TabButton) value;
                tb.setTabComponent(rv);
                rv.add(tb);
            } catch (Exception e) {
                rv.add(value);
            }
        }
        if (makeCloseButton) {
            CloseTabButton ctb = new CloseTabButton(pane);
            ctb.setTabComponent(rv);
            rv.add(ctb);
        }
        return rv;
    }

}
