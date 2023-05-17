package com.deckerben.utilities;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ToolboxFrame extends JFrame {

    public static final ToolboxPanel CONTENT_PANE = new ToolboxPanel();

    public ToolboxFrame(){
        this(new String[0]);
    }

    public ToolboxFrame(String[] args){
        this(args,0);
    }

    public ToolboxFrame(String[] args, int iterator){
        super();
        setSize(750,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (args.length == 0){
            setTitle("Toolbox");
            setContentPane(CONTENT_PANE);
            setMinimumSize(CONTENT_PANE.getMinimumSize());
        }   else {
            Utility util = CONTENT_PANE.getUtility(args[iterator]);
            try{
                setTitle(util.getUtilitiyName());
                Container con = (Container) util.getComponent();
                setContentPane(con);
                setMinimumSize(con.getMinimumSize());
            }   catch (Exception e){
                setTitle("Toolbox");
                setContentPane(CONTENT_PANE);
                setMinimumSize(CONTENT_PANE.getMinimumSize());
            }
            if (iterator < args.length-1) new ToolboxFrame(args,iterator+1);
        }
        setVisible(true);
    }

    public static void main(String[] args){
        new ToolboxFrame(args);
    }

    @Override
    public void dispose() {
        CONTENT_PANE.saveConfigs();
        super.dispose();
    }
}