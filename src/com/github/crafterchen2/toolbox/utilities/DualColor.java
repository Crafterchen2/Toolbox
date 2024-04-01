package com.github.crafterchen2.toolbox.utilities;

import java.awt.*;
import java.awt.color.ColorSpace;

//Classes {
public class DualColor extends Color {
	
	//Fields {
	private final Color secondary;
	//} Fields
	
	//Constructor {
	//Section 1
	public DualColor() {
		this(255,255,255,0,0,0);
	}
	
	public DualColor(Color primary, Color secondary) {
		this(primary.getRGB(), secondary);
	}
	
	public DualColor(int r, int g, int b) {
		this(r, g, b, invertColorChannel(r), invertColorChannel(g), invertColorChannel(b));
	}
	
	public DualColor(int r, int g, int b, int a) {
		this(r, g, b, a, invertColorChannel(r), invertColorChannel(g), invertColorChannel(b), invertColorChannel(a));
	}
	
	public DualColor(int rgb) {
		this(rgb, invertColor(new Color(rgb)));
	}
	
	public DualColor(int rgba, boolean hasalpha) {
		this(rgba, hasalpha, invertColor(new Color(rgba, hasalpha)));
	}
	
	public DualColor(float r, float g, float b) {
		this(r, g, b, invertColorChannel(r), invertColorChannel(g), invertColorChannel(b));
	}
	
	public DualColor(float r, float g, float b, float a) {
		this(r, g, b, a, invertColorChannel(r), invertColorChannel(g), invertColorChannel(b), invertColorChannel(a));
	}
	
	public DualColor(ColorSpace cspace, float[] components, float alpha) {
		this(cspace, components, alpha, invertColor(new Color(cspace, components, alpha)));
	}
	
	//Section 2
	
	public DualColor(int pr, int pg, int pb, int sr, int sg, int sb) {
		this(pr, pg, pb, new Color(sr, sg, sb));
	}
	
	public DualColor(int pr, int pg, int pb, int pa, int sr, int sg, int sb, int sa) {
		this(pr, pg, pb, pa, new Color(sr, sg, sb, sa));
	}
	
	public DualColor(int prgb, int srgb) {
		this(prgb, new Color(srgb));
	}
	
	public DualColor(int prgba, boolean phasalpha, int srgba, boolean shasalpha) {
		this(prgba, phasalpha, new Color(srgba, shasalpha));
	}
	
	public DualColor(float pr, float pg, float pb, float sr, float sg, float sb) {
		this(pr, pg, pb, new Color(sr, sg, sb));
	}
	
	public DualColor(float pr, float pg, float pb, float pa, float sr, float sg, float sb, float sa) {
		this(pr, pg, pb, pa, new Color(sr, sg, sb, sa));
	}
	
	public DualColor(ColorSpace pcspace, float[] pcomponents, float palpha, ColorSpace scspace, float[] scomponents, float salpha) {
		this(pcspace, pcomponents, palpha, new Color(scspace, scomponents, salpha));
	}
	
	//Section 3
	
	public DualColor(int r, int g, int b, Color secondary) {
		super(r, g, b);
		this.secondary = secondary;
	}
	
	public DualColor(int r, int g, int b, int a, Color secondary) {
		super(r, g, b, a);
		this.secondary = secondary;
	}
	
	public DualColor(int rgb, Color secondary) {
		super(rgb);
		this.secondary = secondary;
	}
	
	public DualColor(int rgba, boolean hasalpha, Color secondary) {
		super(rgba, hasalpha);
		this.secondary = secondary;
	}
	
	public DualColor(float r, float g, float b, Color secondary) {
		super(r, g, b);
		this.secondary = secondary;
	}
	
	public DualColor(float r, float g, float b, float a, Color secondary) {
		super(r, g, b, a);
		this.secondary = secondary;
	}
	
	public DualColor(ColorSpace cspace, float[] components, float alpha, Color secondary) {
		super(cspace, components, alpha);
		this.secondary = secondary;
	}
	//} Constructor
	
	//Methods {
	public static Color invertColor(Color input) {
		return invertColor(input, true);
	}
	
	public static Color invertColor(Color input, int alpha) {
		return new Color(invertColorChannel(input.getRed()), invertColorChannel(input.getGreen()), invertColorChannel(input.getBlue()), alpha);
	}
	
	public static Color invertColor(Color input, boolean copyAlpha) {
		return invertColor(input, copyAlpha ? input.getAlpha() : invertColorChannel(input.getAlpha()));
	}
	
	public static int invertColorChannel(int channel) {
		return 255 - channel;
	}
	
	public static float invertColorChannel(float channel) {
		return 1.0f - channel;
	}
	
	public static DualColor switchColors(DualColor dualColor){
		return new DualColor(dualColor.getSecondary(), dualColor.getPrimary());
	}
	
	public DualColor switchColors(){
		return switchColors(this);
	}
	//} Methods
	
	//Getter {
	public Color getSecondary() {
		return secondary;
	}
	
	public Color getPrimary() {
		return this;
	}
	//} Getter
	
}
//} Classes
