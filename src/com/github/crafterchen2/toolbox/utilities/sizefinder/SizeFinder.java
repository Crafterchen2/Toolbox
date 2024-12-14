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

//Classes {
@Tool
public class SizeFinder extends JPanel implements Utility {
	
	//Fields {
	private final FilePickerField pathField = new FilePickerField();
	private final JButton startButton = new JButton("Start");
	private final JButton stopButton = new JButton("Stop");
	private final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("No Data", true));
	private final ComboBoxModel<FileSizes> sizeGate = new DefaultComboBoxModel<>(FileSizes.values());
	private final JPanel buttons;
	private final JPanel loadingLayout;
	private final JProgressBar progressBar;
	private final JLabel progressLabel;
	private boolean busy = false;
	private int progress = 0;
	private long currentSize = 0;
	private SizeNode currentData;
	private Thread thread;
	private SizeNode nextData;
	//} Fields
	
	//Constructor {
	public SizeFinder() {
		super(new BorderLayout());
		reset();
		
		JButton outCsv = new JButton("Export CSV");
		JButton outData = new JButton("Export Data");
		JButton inData = new JButton("Import Data");
		JTree tree = new JTree(model);
		
		pathField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		startButton.addActionListener(_ -> reassembleModel());
		stopButton.addActionListener(_ -> stopReassemble());
		
		add(pathField, BorderLayout.NORTH);
		add(new JScrollPane(tree), BorderLayout.CENTER);
		buttons = new JPanel(new GridLayout(1, 5));
		buttons.add(startButton);
		buttons.add(new JComboBox<>(sizeGate));
		buttons.add(outCsv);
		buttons.add(outData);
		buttons.add(inData);
		add(buttons, BorderLayout.SOUTH);
		loadingLayout = new JPanel(new BorderLayout());
		loadingLayout.add(stopButton, BorderLayout.WEST);
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		loadingLayout.add(progressBar, BorderLayout.CENTER);
		progressLabel = new JLabel("Size: 0");
		loadingLayout.add(progressLabel, BorderLayout.EAST);
	}
	//} Constructor
	
	//Methods {
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
		
		if (files == null) return;
		
		for (File file : files) {
			if (Thread.currentThread().isInterrupted()) {
				return; // Exit if the thread is interrupted
			}
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

	public static String getSmallRepresentation(long lsize, FileSizes gate) {
		double size = (double) lsize / gate.sizeInBytes;
		StringBuilder rv = new StringBuilder();
		if (gate.isAtomic) {
			rv.append((long) size);
		} else {
			rv.append(String.format("%.2f", size));
		}
		rv.append(gate.name);
		return rv.toString();
	}

	public void rootGetFolderSize(File folder, long gate) {
		currentSize = 0;
		if (folder == null) {
			SizeNode errNode = new SizeNode(new ArrayList<>(), 0, "Fehler beim Erstellen: Kein Ordner angegeben.");
			nextData.add(errNode);
			return;
		}
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		int max = countFilesInRootDirectory(folder);
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
		progressBar.setMaximum(max);
		progressBar.setStringPainted(true);
		updateUI();

		SizeNode me = new SizeNode(new ArrayList<>(), 0, folder);
		File[] files = folder.listFiles();

		if (files == null) return;

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (Thread.currentThread().isInterrupted()) {
				return; // Exit if the thread is interrupted
			}
			progressBar.setString(file.getName());
			updateUI();
			if (file.isFile()) {
				long fileLength = file.length();
				if (fileLength / gate > 0) {
					me.addChild(new SizeNode(new ArrayList<>(), fileLength, file));
				}
				me.addSize(fileLength);
				currentSize += fileLength;
				progressLabel.setText("Size: " + getSmallRepresentation(currentSize,getGate()));
				updateUI();
			} else {
				internalGetFolderSize(file, me, gate);
			}
			progressBar.setValue(i+1);
			updateUI();
		}
		nextData.add(me);
	}

	public void internalGetFolderSize(File folder, SizeNode parent, long gate) {
		if (folder == null) {
			SizeNode errNode = new SizeNode(new ArrayList<>(), 0, "Fehler beim Erstellen: Kein Ordner angegeben.");
			parent.add(errNode);
			return;
		}
		SizeNode me = new SizeNode(new ArrayList<>(), 0, folder);
		File[] files = folder.listFiles();

		if (files == null) return;

		for (File file : files) {
			if (Thread.currentThread().isInterrupted()) {
				return; // Exit if the thread is interrupted
			}
			progressBar.setString(file.getName());
			updateUI();
			if (file.isFile()) {
				long fileLength = file.length();
				if (fileLength / gate > 0) {
					me.addChild(new SizeNode(new ArrayList<>(), fileLength, file));
				}
				me.addSize(fileLength);
				currentSize += fileLength;
				progressLabel.setText("Size: " + getSmallRepresentation(currentSize,getGate()));
				updateUI();
			} else {
				internalGetFolderSize(file, me, gate);
			}
		}
		parent.add(me);
	}
	
	public static SizeNode getFolderSize(File folder, FileSizes gate) {
		SizeNode wrapper = new SizeNode(new ArrayList<>(), 0, folder);
		getFolderSize(folder, wrapper, gate.sizeInBytes);
		return wrapper.getNodes().getFirst();
	}

	private int countFilesInRootDirectory(File root) {
		if (root == null || !root.isDirectory()) {
			return 0;
		}
		File[] files = root.listFiles();
		if (files == null) {
			return 0;
		}
		int fileCount = 0;
		return files.length;
	}
	
	private void reassembleModel() {
		thread = new Thread(() -> {
			busy = true;
			this.remove(buttons);
			this.add(loadingLayout, BorderLayout.SOUTH);
			this.updateUI();
			currentData = new SizeNode(new ArrayList<>(), 0, makeRootName());
			FileSizes gate = getGate();
			if (gate == null) gate = FileSizes.B;
			nextData = currentData;
			rootGetFolderSize(pathField.getSelected(), gate.sizeInBytes);
			if (thread.isInterrupted()) {
				return;
			}
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(currentData.getRepresentation(getGate()), true);
			buildTree(root, currentData);
			model.setRoot(root);
			this.remove(loadingLayout);
			this.add(buttons, BorderLayout.SOUTH);
			this.updateUI();
		});
		thread.start();
	}

	private void stopReassemble() {
		if (thread != null) {
			thread.interrupt();
		}
		busy = false;
		this.remove(loadingLayout);
		this.add(buttons, BorderLayout.SOUTH);
		this.updateUI();
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
	//} Methods
	
	//Getter {
	private FileSizes getGate() {
		return (FileSizes) sizeGate.getSelectedItem();
	}
	//} Getter
	
	//Overrides {
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
		currentData = new SizeNode(new ArrayList<>(), 0, "No Data");
		model.setRoot(new DefaultMutableTreeNode(currentData.getRepresentation(getGate()), true));
		sizeGate.setSelectedItem(FileSizes.values()[0]);
	}
	//} Overrides
}

//} Classes
