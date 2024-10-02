package com.github.crafterchen2.toolbox.component.layout;

import java.awt.*;

//Classes {
public class MonoLayout implements LayoutManager {
	
	//Fields {
	private int index;
	private int xGap;
	private int yGap;
	//} Fields
	
	//Constructor {
	public MonoLayout() {
		this(0);
	}
	
	public MonoLayout(int index) {
		this(index, 0, 0);
	}
	
	public MonoLayout(int xGap, int yGap) {
		this(0, xGap, yGap);
	}
	
	public MonoLayout(int index, int xGap, int yGap) {
		if (index < 0) throw new IllegalArgumentException("index must be >= 0.");
		this.index = index;
		this.xGap = xGap;
		this.yGap = yGap;
	}
	//} Constructor
	
	//Methods {
	private Dimension layoutSize(Container parent, Dimension comSize) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			return new Dimension((insets.left + insets.right) + (comSize.width + xGap), (insets.top + insets.bottom) + (comSize.height + yGap));
		}
	}
	
	private Component getIndexedComponent(Container parent) {
		return parent.getComponents()[index % parent.getComponentCount()];
	}
	//} Methods
	
	//Getter {
	public int getIndex() {
		return index;
	}
	
	public int getxGap() {
		return xGap;
	}
	
	public int getyGap() {
		return yGap;
	}
	//} Getter
	
	//Setter {
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setxGap(int xGap) {
		this.xGap = xGap;
	}
	
	public void setyGap(int yGap) {
		this.yGap = yGap;
	}
	//} Setter
	
	//Overrides {
	@Override
	public void addLayoutComponent(String name, Component comp) {
	
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
	
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return layoutSize(parent, getIndexedComponent(parent).getPreferredSize());
		}
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return layoutSize(parent, getIndexedComponent(parent).getMinimumSize());
		}
	}
	
	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			Dimension pSize = parent.getSize();
			getIndexedComponent(parent).setBounds(insets.left + xGap, insets.top + yGap, pSize.width - insets.right - insets.left - (xGap * 2), pSize.height - insets.bottom - insets.top - (yGap * 2));
		}
	}
	//} Overrides
}
//} Classes
