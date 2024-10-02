package com.github.crafterchen2.toolbox.utilities.eqsystrainer;

import com.github.crafterchen2.toolbox.Resettable;
import com.github.crafterchen2.toolbox.Tool;
import com.github.crafterchen2.toolbox.Utility;
import com.github.crafterchen2.toolbox.component.layout.MonoLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Objects;

//Classes {
@Tool
public class EqSysTrainer extends JPanel implements Utility {
	
	//Fields {
	public static final byte DEFAULT_N_VARS = 3;
	public static final byte DEFAULT_RNG_START = -5; //Default for rngStart.
	public static final byte DEFAULT_RNG_RANGE = 10; //Default for rngRange.
	private final SpinnerNumberModel nVarModel = new SpinnerNumberModel(DEFAULT_N_VARS, 1, Byte.MAX_VALUE, 1);
	private final SpinnerNumberModel rngStartModel = new SpinnerNumberModel(DEFAULT_RNG_START, Byte.MIN_VALUE, Byte.MAX_VALUE, 1);
	private final SpinnerNumberModel rngRangeModel = new SpinnerNumberModel(DEFAULT_RNG_RANGE, 1, Byte.MAX_VALUE, 1);
	private final JSpinner nVarSpinner = new JSpinner(nVarModel);
	private final JSpinner rngStartSpinner = new JSpinner(rngStartModel);
	private final JSpinner rngRangeSpinner = new JSpinner(rngRangeModel);
	private final EqSysTableModel tableModel = new EqSysTableModel();
	private final JButton checkButton = new JButton("Überprüfen");
	private byte nVars;
	private byte rngStart;
	private byte rngRange;
	private int[][] a;
	private int[] x;
	private long[] b;
	private State[] states;
	private boolean showSolution = false;
	//} Fields
	
	//Constructor {
	public EqSysTrainer() {
		super(new BorderLayout(2,2));
		reset();
		Insets zero = new Insets(0, 0, 0, 0);
		setOpaque(false);
		JPanel top = new JPanel(new GridLayout(1,6,2,0));
		top.setOpaque(false);
		nVarSpinner.setToolTipText("Wie viele Variablen?");
		top.add(nVarSpinner);
		rngStartSpinner.setToolTipText("Kleinste Zahl?");
		top.add(rngStartSpinner);
		rngRangeSpinner.setToolTipText("Wie viele Zahlen?");
		top.add(rngRangeSpinner);
		JButton calcButton = new JButton("Neue Aufgabe");
		calcButton.setToolTipText("Stellt eine neue Aufgabe.");
		calcButton.setMargin(zero);
		calcButton.addActionListener(_ -> calculate());
		top.add(calcButton);
		JButton revealButton = new JButton("Auflösen");
		revealButton.setToolTipText("Präsentiert die Lösung der Aufgabe.");
		revealButton.setMargin(zero);
		revealButton.addActionListener(_ -> reveal());
		top.add(revealButton);
		checkButton.setToolTipText("Überprüft die Eingabe, ob sie korrekt ist.");
		checkButton.setMargin(zero);
		checkButton.addActionListener(_ -> {
			if (!showSolution) check();
		});
		top.add(checkButton);
		add(top, BorderLayout.NORTH);
		JPanel bottom = new JPanel(new MonoLayout());
		bottom.setBorder(BorderFactory.createRaisedBevelBorder());
		JTable table = new JTable(tableModel);
		updateRenderer(table);
		table.setCellSelectionEnabled(false);
		table.setAutoCreateColumnsFromModel(true);
		bottom.add(new JScrollPane(table));
		add(bottom, BorderLayout.CENTER);
		tableModel.addTableModelListener(_ -> {
			table.createDefaultColumnsFromModel();
			updateRenderer(table);
		});
	}
	//} Constructor
	
	//Methods {
	private void updateRenderer(JTable table) {
		table.getColumnModel().getColumn(nVars).setCellRenderer(new EqSysRenderer(table));
	}
	
	private void calculate() {
		nVars = nVarModel.getNumber().byteValue();
		rngStart = rngStartModel.getNumber().byteValue();
		rngRange = rngRangeModel.getNumber().byteValue();
		if (nVars < 1) throw new IllegalArgumentException("nVars must be at least 1.");
		a = new int[nVars][nVars];
		x = new int[nVars];
		b = new long[nVars]; //Outer array of a
		states = new State[nVars];
		for (int i = 0; i < nVars; i++) {
			x[i] = rng();
			for (int j = 0; j < nVars; j++) {
				a[i][j] = rng();
			}
		}
		for (int i = 0; i < nVars; i++) {
			for (int j = 0; j < nVars; j++) {
				b[i] += (long) a[j][i] * x[j];
			}
		}
		tableModel.reset();
		reveal(false);
	}
	
	private void reveal() {
		reveal(!showSolution);
	}
	
	private void reveal(boolean show) {
		showSolution = show;
		checkButton.setEnabled(!showSolution);
		tableModel.fireTableDataChanged();
	}
	
	private void check() {
		if (!showSolution) {
			for (int i = 0; i < nVars; i++) {
				String replaced = tableModel.getValueAt(i, nVars).replaceAll("[^0-9-]", "");
				int parsed = x[i] + 1;
				if (!replaced.isEmpty()) parsed = Integer.parseInt(replaced);
				states[i] = (x[i] != parsed) ? State.INCORRECT : State.CORRECT;
				tableModel.fireTableDataChanged();
			}
		}
	}
	
	private int rng() {
		return (int) (Math.random() * rngRange) + rngStart;
	}
	//} Methods
	
	//Overrides {
	@Override
	public String getUtilitiyName() {
		return "LGS Trainer";
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public Component createNewInstance() {
		return new EqSysTrainer();
	}
	
	@Override
	public boolean canReset() {
		return true;
	}
	
	@Override
	public void resetCode() {
		nVarSpinner.setValue((byte) 3);
		rngStartSpinner.setValue((byte) -5);
		rngRangeSpinner.setValue((byte) 10);
		showSolution = false;
		checkButton.setEnabled(true);
		calculate();
	}
	
	@Override
	public int getListPriority() {
		return 10;
	}
	//} Overrides
	
	//Classes {
	private class EqSysRenderer extends DefaultTableCellRenderer {
		//Fields {
		private final JTable table;
		//} Fields
		
		//Constructor {
		public EqSysRenderer(JTable table) {
			this.table = table;
		}
		//} Constructor
		
		//Overrides {
		@Override
		public Component getTableCellRendererComponent(JTable table1, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column == nVars) {
				if (states[row] != null) {
					l.setBackground(switch (states[row]) {
						case State.CORRECT -> new Color(0x42FF42);
						case State.INCORRECT -> new Color(0xFF4242);
					});
				} else {
					l.setBackground(new Color(0x41B4FF));
				}
			}
			return l;
		}
		//} Overrides
	}
	
	private class EqSysTableModel extends AbstractTableModel implements Resettable {
		
		//Fields {
		private String[] edited;
		//} Fields
		
		//Constructor {
		public EqSysTableModel() {
			reset();
		}
		//} Constructor
		
		//Methods {
		private void adjustEdited(String value, int index) {
			String[] ned = (edited.length != nVars) ? new String[nVars] : edited;
			for (int i = 0; i < nVars; i++) {
				if (i == index) {
					ned[i] = value;
				} else {
					if (i <= edited.length && ned[i] != null && edited[i] != null && !Objects.equals(edited[i], "")) {
						ned[i] = edited[i];
					} else {
						ned[i] = "x" + i;
					}
				}
			}
			if (edited.length != nVars) edited = ned;
		}
		//} Methods
		
		//Overrides {
		@Override
		public int getRowCount() {
			return nVars;
		}
		
		@Override
		public int getColumnCount() {
			return nVars + 2;
		}
		
		@Override
		public String getValueAt(int rowIndex, int columnIndex) {
			if (edited.length != nVars) reset();
			if (columnIndex == nVars) return edited[rowIndex] + ((showSolution) ? "= " + x[rowIndex] : "");
			if (columnIndex > nVars) return "= " + b[rowIndex];
			return a[columnIndex][rowIndex] + "";
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex != nVars) return;
			if (aValue instanceof String s) adjustEdited(s, rowIndex);
		}
		
		@Override
		public String getColumnName(int column) {
			if (column == nVars) return (showSolution) ? "Gefunden:" : "Gesucht:";
			if (column > nVars) return "Ergebnis";
			return (column + 1) + "";
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == nVars;
		}
		
		@Override
		public boolean canReset() {
			return true;
		}
		
		@Override
		public void resetCode() {
			edited = new String[nVars];
			for (int i = 0; i < nVars; i++) {
				edited[i] = "x" + i;
			}
		}
		
		//} Overrides
	}
	
	//} Classes
	//Enums {
	private enum State {
		CORRECT,
		INCORRECT
	}
	//} Enums
}
//} Classes