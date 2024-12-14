package com.github.crafterchen2.toolbox.utilities.sizefinder;

//Enums {
public enum FileSizes {
	
	B(1, true),
	KB(B.sizeInBytes * 1000),
	MB(KB.sizeInBytes * 1000),
	GB(MB.sizeInBytes * 1000),
	TB(GB.sizeInBytes * 1000),
	KIB(B.sizeInBytes * 1024, "KiB"),
	MIB(KIB.sizeInBytes * 1024, "MiB"),
	GIB(MIB.sizeInBytes * 1024, "GiB"),
	TIB(GIB.sizeInBytes * 1024, "TiB"),
	;
	
	//Fields {
	final long sizeInBytes;
	final String name;
	final boolean isAtomic;
	//} Fields
	
	//Constructor {
	FileSizes(long sizeInBytes) {
		this(sizeInBytes, null, false);
	}
	
	FileSizes(long sizeInBytes, boolean isAtomic) {
		this(sizeInBytes, null, isAtomic);
	}
	
	FileSizes(long sizeInBytes, String name) {
		this(sizeInBytes, name, false);
	}
	
	FileSizes(long sizeInBytes, String name, boolean isAtomic) {
		if (sizeInBytes < 1) throw new IllegalArgumentException("sizeInBytes must be at least 1.");
		this.sizeInBytes = sizeInBytes;
		this.name = (name != null) ? name : super.toString();
		this.isAtomic = isAtomic;
	}
	//} Constructor
	
	
	//Overrides {
	@Override
	public String toString() {
		return name + ": " + sizeInBytes + " Byte" + ((sizeInBytes > 1) ? "s" : "");
	}
	//} Overrides
}
//} Enums
