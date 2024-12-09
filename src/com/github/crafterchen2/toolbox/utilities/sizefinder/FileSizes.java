package com.github.crafterchen2.toolbox.utilities.sizefinder;

public enum FileSizes {
	
	B(1, "B"),
	KB(B.sizeInBytes * 1000),
	MB(KB.sizeInBytes * 1000),
	GB(MB.sizeInBytes * 1000),
	TB(GB.sizeInBytes * 1000),
	KIB(B.sizeInBytes * 1024, "KiB"),
	MIB(KIB.sizeInBytes * 1024, "MiB"),
	GIB(MIB.sizeInBytes * 1024, "GiB"),
	TIB(GIB.sizeInBytes * 1024, "TiB"),
	;
	
	final long sizeInBytes;
	final String name;
	
	FileSizes(long sizeInBytes) {
		this(sizeInBytes, null);
	}
	
	FileSizes(long sizeInBytes, String name) {
		if (sizeInBytes < 1) throw new IllegalArgumentException("sizeInBytes must be at least 1.");
		this.sizeInBytes = sizeInBytes;
		this.name = (name != null) ? name : super.toString();
	}
	
	
	@Override
	public String toString() {
		return name + ": " + sizeInBytes + " Byte" + ((sizeInBytes > 1) ? "s" : "");
	}
}
