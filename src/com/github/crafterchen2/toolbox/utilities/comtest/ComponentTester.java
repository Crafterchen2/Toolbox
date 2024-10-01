package com.github.crafterchen2.toolbox.utilities.comtest;

import com.github.crafterchen2.toolbox.Tool;
import com.github.crafterchen2.toolbox.component.FileNameFilter;
import com.github.crafterchen2.toolbox.component.FilePickerField;
import com.github.crafterchen2.toolbox.component.layout.VerticalListLayout;
import com.github.crafterchen2.toolbox.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

@Tool
public class ComponentTester extends JPanel implements Utility {

    //Felder
    FilePickerField fpf = new FilePickerField(false);
    FilePickerField fpt = new FilePickerField(true, new FileNameFilter("Resource Pack (pack.mcmeta)","pack.mcmeta"));

    //Konstruktoren
    public ComponentTester(){
        setLayout(new GridLayout(5,1));
        add(fpf);
        add(fpt);
        JButton resetButton = new JButton("resetHistory");
        resetButton.addActionListener(e -> {
            fpf.clearHistory();
            fpt.clearHistory();
        });
        add(resetButton);
    }

    //Methoden
    private void test(BiConsumer<Integer, Integer> code){
        code.accept(3,4);
    }

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        fpf.reset();
        fpt.reset();
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
