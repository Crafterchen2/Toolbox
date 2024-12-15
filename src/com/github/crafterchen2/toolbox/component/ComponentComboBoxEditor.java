package com.github.crafterchen2.toolbox.component;

import com.github.crafterchen2.toolbox.component.layout.MonoLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/// Enables any Component to be selected in the Combobox and displayed as the Editor.
/// To achieve this effect, this needs to be set as the Editor of the ComboBox
/// and setEditable needs to be enabled.
public class ComponentComboBoxEditor implements ComboBoxEditor {
	
	/// The JPanel that displays our Component.
	final JPanel display = new JPanel(new MonoLayout());
	
	/// The Object given by the ComboBox.
	Object object = null;
	
	@Override
	public Component getEditorComponent() {
		return display;
	}
	
	@Override
	public void setItem(Object anObject) {
		if (anObject instanceof Wrapper wrapper) {
			updateComponent(wrapper.component());
		} else if (anObject instanceof Component comp) {
			updateComponent(comp);
		}
		object = anObject;
	}
	
	/// Updates display to display the given comp
	/// @param comp The Component we want to be displayed.
	private void updateComponent(Component comp) {
		display.add(comp, 0);
		display.updateUI();
	}
	
	@Override
	public Object getItem() {
		return object;
	}
	
	@Override
	public void selectAll() {
		
	}
	
	/// This method is ignored.
	/// @param l the ActionListener to be ignored
	@Override
	public void addActionListener(ActionListener l) {
		
	}
	
	/// This method is ignored.
	/// @param l the ActionListener to be ignored
	@Override
	public void removeActionListener(ActionListener l) {
		
	}
	
	/// Wraps a Component paired with a text to be displayed in the ComboBox.
	/// @param component The Component to be displayed.
	/// @param displayText The text to be displayed in the ComboBox. This is the return value of the overridden toString() method.
	public record Wrapper(Component component, String displayText) {
		/// Returns displayText.
		@Override
		public String toString() {
			return displayText;
		}
	}
}
