package com.github.crafterchen2.toolbox.utilities.animshift;

import com.github.crafterchen2.toolbox.Resettable;
import com.github.crafterchen2.toolbox.Utility;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AnimationShifter extends JPanel implements Utility {

    //Fields
    private File srcPath;
    private File targetPath;
    private boolean notWorking;

    //Constructor
    public AnimationShifter(){
        reset();
        setLayout(new BorderLayout());
    }

    //Methods

    //Getter

    //Setter

    //Overrides from
    ////Resettable
    @Override
    public boolean canReset() {
        return notWorking;
    }

    @Override
    public void resetCode() {
        srcPath = null;
        targetPath = null;
        notWorking = false;
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "Animation Shifter";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return null;
    }

}
