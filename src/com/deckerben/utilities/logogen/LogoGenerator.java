package com.deckerben.utilities.logogen;

import com.deckerben.component.SimpleImagePainter;
import com.deckerben.utilities.DualColor;
import com.deckerben.utilities.Utility;

import javax.swing.*;
import java.awt.*;

//Classes {
public class LogoGenerator extends JPanel implements Utility {
	
	private final SimpleImagePainter zoomedDisplay;
	private final SimpleImagePainter actualDisplay;
	private DualColor color;
	
	public LogoGenerator(){
		reset();
	}
	
	//Overrides {
	@Override
	public boolean canReset() {
		return true;
	}
	
	@Override
	public void resetCode() {
	
	}
	
	@Override
	public String getUtilitiyName() {
		return null;
	}
	
	@Override
	public Component getComponent() {
		return null;
	}
	
	@Override
	public Component createNewInstance() {
		return null;
	}
	//} Overrides
}
//} Classes
