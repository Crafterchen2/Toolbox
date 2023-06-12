package com.deckerben.utilities.comtest;

import com.deckerben.component.SimpleImagePainter;
import com.deckerben.component.layout.VerticalListLayout;
import com.deckerben.utilities.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ComponentTester extends JPanel implements Utility {

    private final JPanel pane;
    //Felder
    private VerticalListLayout layout = new VerticalListLayout();

    //Konstruktoren
    public ComponentTester(){
        pane = new JPanel();
        pane.setLayout(layout);
        setLayout(new GridLayout(1,1));
        JButton[] buttons = new JButton[4];
        buttons[0] = new JButton("/\\");
        buttons[0].addActionListener(e -> {
            layout.setVgap(layout.getVgap() + 1);
            pane.updateUI();
        });
        buttons[1] = new JButton("\\/");
        buttons[1].addActionListener(e -> {
            layout.setVgap(layout.getVgap() - 1);
            pane.updateUI();
        });
        buttons[2] = new JButton("wc");
        buttons[2].addActionListener(e -> {
            layout.setWidthMode((layout.getWidthMode() + 1)%3);
            pane.updateUI();
        });
        buttons[3] = new JButton("hc");
        buttons[3].addActionListener(e -> {
            layout.setHeightMode((layout.getHeightMode() + 1)%3);
            pane.updateUI();
        });
        for (JButton value : buttons) pane.add(value);
        add(new JScrollPane(pane));
    }

    //Methoden

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        layout.setVgap(0);
        layout.setWidthMode(2);
        layout.setHeightMode(2);
        pane.updateUI();
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "ComponentTester";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new ComponentTester();
    }
}
