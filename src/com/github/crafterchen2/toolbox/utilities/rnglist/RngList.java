package com.github.crafterchen2.toolbox.utilities.rnglist;

import com.github.crafterchen2.toolbox.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RngList extends JComponent implements Utility {
	
	private final SpinnerNumberModel lengthModel = new SpinnerNumberModel(0,0, Byte.MAX_VALUE, 1);
	private final SpinnerNumberModel baseModel = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	private final SpinnerNumberModel rangeModel = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	
	private final JTextArea display = new JTextArea();
	
	public RngList() {
		reset();
		setLayout(new BorderLayout());
		
		JButton gen = new JButton("Würfeln");
		gen.addActionListener(_ -> refresh());
		display.setLineWrap(true);
		
		JPanel settings = new JPanel(new FlowLayout());
		settings.add(new JLabel("Menge:"));
		settings.add(new JSpinner(lengthModel));
		settings.add(new JLabel("Basis:"));
		settings.add(new JSpinner(baseModel));
		settings.add(new JLabel("Reichweite:"));
		settings.add(new JSpinner(rangeModel));
		settings.add(gen);
		add(settings, BorderLayout.NORTH);
		add(display, BorderLayout.CENTER);
	}
	
	public void refresh() {
		display.setText(Arrays.toString(rngMakeInt(lengthModel.getNumber().intValue(), baseModel.getNumber().intValue(), rangeModel.getNumber().intValue())));
	}
	
	static void rngFillArray(int[] arr, int base, int range) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * range) + base;
		}
	}
	
	static int[] rngMakeInt(int length, int base, int range) {
		int[] rv = new int[length];
		rngFillArray(rv, base, range);
		return rv;
	}
	
	@Override
	public String getUtilitiyName() {
		return "Zufallszahlen";
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public Component createNewInstance() {
		return new RngList();
	}
	
	@Override
	public boolean canReset() {
		return true;
	}
	
	@Override
	public void resetCode() {
		display.setText("");
		lengthModel.setValue(10);
		baseModel.setValue(0);
		rangeModel.setValue(100);
	}
}
