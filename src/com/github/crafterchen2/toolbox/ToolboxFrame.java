package com.github.crafterchen2.toolbox;

import com.github.crafterchen2.toolbox.component.MenuTools;

import javax.swing.*;
import java.awt.*;

//Classes {
public class ToolboxFrame extends JFrame {
	
	//Fields {
	private static final ToolboxPanel CONTENT_PANE = new ToolboxPanel();
	//} Fields
	
	//Constructor {
	public ToolboxFrame() {
		this(new String[0]);
	}
	
	public ToolboxFrame(String[] args) {
		this(args, 0);
	}
	
	private ToolboxFrame(String[] args, int iterator) {
		super();
		setSize(750, 500);
		if (args.length == 0) {
			setLocationRelativeTo(null);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			setTitle("Toolbox");
			setContentPane(CONTENT_PANE);
			setMinimumSize(CONTENT_PANE.getMinimumSize());
		} else {
			if (iterator == 0) {
				setLocationRelativeTo(null);
			} else {
				setLocationByPlatform(true);
			}
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			Utility util = CONTENT_PANE.getUtility(args[iterator]);
			try {
				setTitle(util.getUtilitiyName());
				Container con = (Container) util.getComponent();
				JMenuBar bar = new JMenuBar();
				JMenu toolMenu = new JMenu("Tool");
				toolMenu.add(MenuTools.item("Zurücksetzen", _ -> util.reset()));
				toolMenu.add(MenuTools.item("Entfernen", _ -> dispose()));
				toolMenu.addSeparator();
				toolMenu.add(MenuTools.item("Toolbox öffnen", _ -> new ToolboxFrame()));
				bar.add(toolMenu);
				setJMenuBar(bar);
				setContentPane(con);
				setMinimumSize(con.getMinimumSize());
			} catch (Exception e) {
				setTitle("Toolbox");
				setContentPane(CONTENT_PANE);
				setMinimumSize(CONTENT_PANE.getMinimumSize());
			}
			if (iterator < args.length - 1) new ToolboxFrame(args, iterator + 1);
		}
		setVisible(true);
	}
	//} Constructor
}
//} Classes