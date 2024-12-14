package com.github.crafterchen2.toolbox.utilities.sizefinder;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class SizeNode {
	private final List<SizeNode> nodes;
	private long size;
	private final String name;
	private final String path;
	private final boolean isFolder;
	
	public SizeNode(List<SizeNode> nodes, long size, String name) {
		this.nodes = nodes;
		this.size = size;
		this.name = name;
		path = name;
		isFolder = false;
	}
	
	public SizeNode(List<SizeNode> nodes, long size, File file) {
		this.nodes = nodes;
		this.size = size;
		name = file.getName();
		path = file.getPath();
		isFolder = file.isDirectory();
	}
	
	public boolean isFolder(){
		return isFolder;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getName(){
	    return name;
	}
	
	public boolean hasChildren() {
		return !getNodes().isEmpty();
	}
	
	public List<SizeNode> getNodes() {
		return nodes;
	}
	
	public void add(SizeNode child) {
		addChild(child);
		addSize(child.getSize());
	}
	
	public void addChild(SizeNode child) {
		nodes.add(child);
	}
	
	public long getSize() {
		return size;
	}
	
	public void addSize(long toAdd) {
		size += toAdd;
	}
	
	public String getRepresentation(FileSizes gate) {
		double size = (double) this.size / gate.sizeInBytes;
		StringBuilder rv = new StringBuilder();
		if (gate.isAtomic) {
			rv.append((long) size);
		} else {
			rv.append(String.format("%.2f", size));
		}
		rv.append(gate.name);
		rv.append(": ");
		rv.append(getName());
		return rv.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (SizeNode) obj;
		return Objects.equals(this.nodes, that.nodes) &&
			   this.size == that.size;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nodes, size);
	}
}
