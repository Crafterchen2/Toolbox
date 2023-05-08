package com.deckerben.component.tab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseTabButton extends TabButton implements ActionListener {

    private final JTabbedPane pane;

    public CloseTabButton(JTabbedPane pane) {
        super("X");
        this.pane = pane;
        setup();
    }

    public CloseTabButton(String text, JTabbedPane pane) {
        super(text);
        this.pane = pane;
        setup();
    }

    public CloseTabButton(Icon icon, JTabbedPane pane) {
        super("X", icon);
        this.pane = pane;
        setup();
    }

    public CloseTabButton(Action a, JTabbedPane pane) {
        super(a);
        this.pane = pane;
        setup();
    }

    public CloseTabButton(String text, Icon icon, JTabbedPane pane) {
        super(text, icon);
        this.pane = pane;
        setup();
    }

    private void setup(){
        setToolTipText("Schließe diesen Tab");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i = pane.indexOfTabComponent(tabComponent);
        if (i != -1) {
            pane.remove(i);
        }
    }

    public JTabbedPane getTabbedPane() {
        return pane;
    }
}
