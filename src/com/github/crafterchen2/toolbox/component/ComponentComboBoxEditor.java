package com.github.crafterchen2.toolbox.component;

import com.github.crafterchen2.toolbox.component.layout.MonoLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ComponentComboBoxEditor implements ComboBoxEditor {
	
	final JPanel display = new JPanel(new MonoLayout());
	
	Object object = null;
	
	@Override
	public Component getEditorComponent() {
		return display;
	}
	
	@Override
	public void setItem(Object anObject) {
		if (anObject instanceof Wrapper wrapper) {
			display.add(wrapper.component(), 0);
			display.updateUI();
		}
		object = anObject;
	}
	
	@Override
	public Object getItem() {
		return object;
	}
	
	@Override
	public void selectAll() {
		
	}
	
	@Override
	public void addActionListener(ActionListener l) {
		
	}
	
	@Override
	public void removeActionListener(ActionListener l) {
		
	}
	
	public record Wrapper(JComponent component, String displayText) {
		@Override
		public String toString() {
			return displayText;
		}
	}
}
