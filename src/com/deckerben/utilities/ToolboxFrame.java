package com.deckerben.utilities;

import javax.swing.*;
import java.awt.*;

public class ToolboxFrame extends JFrame {

    //Felder
    private static final ToolboxPanel CONTENT_PANE = new ToolboxPanel();

    //Listener

    //Konstruktoren
    @SuppressWarnings("unused")
    public ToolboxFrame(){
        this(new String[0]);
    }

    public ToolboxFrame(String[] args){
        this(args,0);
    }

    public ToolboxFrame(String[] args, int iterator){
        super();
        setSize(750,500);
        if (args.length == 0){
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle("Toolbox");
            setContentPane(CONTENT_PANE);
            setMinimumSize(CONTENT_PANE.getMinimumSize());
        }   else {
            if (iterator == 0) {
                setLocationRelativeTo(null);
            } else {
                setLocationByPlatform(true);
            }
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

    //Methoden
    public static void main(String[] args){
        new ToolboxFrame(args);
    }

    //Getter

    //Setter

    //Maker

    //Overrides aus
    ////<Oberklasse>

    //Interne Klassen
    ////Klasse "<Klassenname>"
}