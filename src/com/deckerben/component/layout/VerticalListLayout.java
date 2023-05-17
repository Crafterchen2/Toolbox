package com.deckerben.component.layout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class VerticalListLayout implements LayoutManager{

    public static final int MAXIMUM = 0;
    public static final int PREFERRED = 1;
    public static final int MINIMUM = 2;

    private final int vgap;
    private final int widthSize;
    private final int heightSize;


    public VerticalListLayout() {
        this(0);
    }


    public VerticalListLayout(int vgap) {
        this(vgap, MAXIMUM, PREFERRED);
    }

    public VerticalListLayout(int vgap, int widthSize, int heightSize){
        this.vgap = vgap;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
    }

    public void addLayoutComponent(String name, Component comp) {

    }

    public void removeLayoutComponent(Component comp) {

    }

    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            Dimension rv = findMaxPreferredSize(parent);
            return new Dimension((insets.left+insets.right)+(rv.width+vgap)*ncomponents,(insets.top+insets.bottom)+(rv.height+vgap)*ncomponents);
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            Dimension rv = findMaxMinimumSize(parent);
            return new Dimension((insets.left+insets.right)+(rv.width+vgap)*ncomponents,(insets.top+insets.bottom)+(rv.height+vgap)*ncomponents);
        }
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            if (ncomponents == 0) {
                return;
            }
            Component[] coms = parent.getComponents();
            Dimension w = switch (widthSize) {
                case MAXIMUM -> findMaxMaximumSize(parent);
                case PREFERRED -> findMaxPreferredSize(parent);
                case MINIMUM -> findMaxMinimumSize(parent);
                default -> new Dimension(10, 0);
            };
            Dimension h = switch (heightSize) {
                case MAXIMUM -> findMaxMaximumSize(parent);
                case PREFERRED -> findMaxPreferredSize(parent);
                case MINIMUM -> findMaxMinimumSize(parent);
                default -> new Dimension(0, 10);
            };
            Dimension dim = new Dimension(w.width,h.height);
            for (int i = 0; i<ncomponents; i++) {
                coms[i].setBounds(insets.left,insets.top+(vgap+dim.height)*i,dim.width-insets.right,dim.height-insets.bottom);
            } // end of for
        }
    }

    private Dimension findMaxPreferredSize(Container parent){
        return findMaxPreferredSize(parent.getComponents());
    }

    private Dimension findMaxMinimumSize(Container parent){
        return findMaxMinimumSize(parent.getComponents());
    }

    private Dimension findMaxMaximumSize(Container parent){
        Insets ins = parent.getInsets();
        Dimension size = parent.getSize();
        size.height = size.height-ins.bottom-ins.top;
        size.width = size.width-ins.left-ins.right;
        return size;
    }

    private Dimension findMaxPreferredSize(Component[] coms){
        Dimension rv = new Dimension(0,0);
        for (Component com : coms) {
            rv.height = Math.max(rv.height, com.getPreferredSize().height);
            rv.width = Math.max(rv.width, com.getPreferredSize().width);
        } // end of for
        return rv;
    }

    private Dimension findMaxMinimumSize(Component[] coms){
        Dimension rv = new Dimension(0,0);
        for (Component com : coms) {
            rv.height = Math.max(rv.height, com.getMinimumSize().height);
            rv.width = Math.max(rv.width, com.getMinimumSize().width);
        } // end of for
        return rv;
    }

}