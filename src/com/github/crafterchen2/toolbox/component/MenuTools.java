package com.github.crafterchen2.toolbox.component;

import javax.swing.*;
import java.awt.event.ActionListener;

//Classes {
public class MenuTools {
	
	//Methods {
	public static JMenuItem item(String title, ActionListener action) {
		return item(title, null, action);
	}
	
	public static JMenuItem item(String title, String tooltip, ActionListener action) {
		JMenuItem rv = new JMenuItem();
		if (title != null) rv.setText(title);
		if (tooltip != null) rv.setToolTipText(tooltip);
		if (action != null) rv.addActionListener(action);
		return rv;
	}
	
	public static JMenu menu(String title) {
		return menu(title, null);
	}
	
	public static JMenu menu(String title, String tooltip) {
		JMenu rv = new JMenu();
		if (title != null) rv.setText(title);
		if (tooltip != null) rv.setToolTipText(tooltip);
		return rv;
	}
	//} Methods
	
}
//} Classes
