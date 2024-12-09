package com.github.crafterchen2.toolbox.utilities.sizefinder;

import com.github.crafterchen2.toolbox.Tool;
import com.github.crafterchen2.toolbox.Utility;
import com.github.crafterchen2.toolbox.component.FilePickerField;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

@Tool
public class SizeFinder extends JPanel implements Utility {
	
	private boolean busy = false;
	private SizeNode currentData;
	
	private final FilePickerField pathField = new FilePickerField();
	private final JButton startStop = new JButton();
	private final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("No Data", true));
	private final ComboBoxModel<FileSizes> sizeGate = new DefaultComboBoxModel<>(FileSizes.values());
	
	public SizeFinder() {
		super(new BorderLayout());
		reset();
		
		JButton outCsv = new JButton("Export CSV");
		JButton outData = new JButton("Export Data");
		JButton inData = new JButton("Import Data");
		JTree tree = new JTree(model);
		
		pathField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		startStop.addActionListener(_ -> reassembleModel());
		
		add(pathField, BorderLayout.NORTH);
		add(new JScrollPane(tree), BorderLayout.CENTER);
		JPanel buttons = new JPanel(new GridLayout(1,5));
			buttons.add(startStop);
			buttons.add(new JComboBox<>(sizeGate));
			buttons.add(outCsv);
			buttons.add(outData);
			buttons.add(inData);
		add(buttons, BorderLayout.SOUTH);
	}
	
	private FileSizes getGate() {
		return (FileSizes) sizeGate.getSelectedItem();
	}
	
	private void reassembleModel(){
		currentData = new SizeNode(new ArrayList<>(), 0, makeRootName());
		FileSizes gate = getGate();
		if (gate == null) gate = FileSizes.B;
		getFolderSize(pathField.getSelected(), currentData, gate.sizeInBytes);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(currentData.getRepresentation(getGate()), true);
		buildTree(root, currentData);
		model.setRoot(root);
	}
	
	private String makeRootName() {
		return "File sizes of " + LocalDate.now();
	}
	
	private void buildTree(DefaultMutableTreeNode node, SizeNode data) {
		if (data.hasChildren()) {
			for (SizeNode dataNode : data.getNodes()) {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(dataNode.getRepresentation(getGate()), dataNode.isFolder());
				node.add(child);
				buildTree(child, dataNode);
			}
		} else {
			if (data.isFolder()) {
				if (data.getSize() <= 0) {
					node.add(new DefaultMutableTreeNode("<Empty directory>", false));
				} else {
					node.add(new DefaultMutableTreeNode("<Content hidden>", false));
				}
			}
		}
	}
	
	//Original version created and developed by https://github.com/Apfeltasche20
	//Used with permission.
	public static void getFolderSize(File folder, SizeNode parent, long gate) {
		if (folder == null) {
			SizeNode errNode = new SizeNode(new ArrayList<>(), 0, "Fehler beim Erstellen: Kein Ordner angegeben.");
			parent.add(errNode);
			return;
		}
		SizeNode me = new SizeNode(new ArrayList<>(), 0, folder);
		File[] files = folder.listFiles();
		
		if(files == null) return;
		
		for (File file : files) {
			if (file.isFile()) {
				long fileLength = file.length();
				if (fileLength / gate > 0) {
					me.addChild(new SizeNode(new ArrayList<>(), fileLength, file));
				}
				me.addSize(fileLength);
			} else {
				getFolderSize(file, me, gate);
			}
		}
		parent.add(me);
	}
	
	public static SizeNode getFolderSize(File folder, FileSizes gate) {
		SizeNode wrapper = new SizeNode(new ArrayList<>(), 0, folder);
		getFolderSize(folder, wrapper, gate.sizeInBytes);
		return wrapper.getNodes().getFirst();
	}
	
	@Override
	public String getUtilitiyName() {
		return "Size Finder";
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public Component createNewInstance() {
		return new SizeFinder();
	}
	
	@Override
	public boolean canReset() {
		return !busy;
	}
	
	@Override
	public void resetCode() {
		busy = false;
		startStop.setText("Start");
		currentData = new SizeNode(new ArrayList<>(), 0, "No Data");
		model.setRoot(new DefaultMutableTreeNode(currentData.getRepresentation(getGate()), true));
		sizeGate.setSelectedItem(FileSizes.values()[0]);
	}
}

