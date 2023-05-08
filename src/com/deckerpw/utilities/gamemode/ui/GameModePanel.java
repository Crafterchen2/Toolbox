package com.deckerpw.utilities.gamemode.ui;


import com.deckerpw.utilities.gamemode.GameMode;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameModePanel extends JPanel {

    final GameMode gameMode;
    Boolean[] data;
    String[] names;
    JPanel mainButtonGroup = new JPanel(new GridLayout(3,1));
        JButton killButton = new JButton();
        JPanel addProgramGroup = new JPanel(new BorderLayout(1,2));
            JButton addButton = new JButton();
            JTextField addField = new JTextField();

        JButton delButton = new JButton();
    JTable table = new JTable();

    class Model implements TableModel{

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnIndex == 0 ? "Name" : "Enabled";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? String.class : Boolean.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex == 0 ? names[rowIndex]:data[rowIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            gameMode.setProgramValue(rowIndex,(Boolean) aValue);
            data = gameMode.getValues();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    public GameModePanel(GameMode mode){
        super(new BorderLayout());
        this.gameMode = mode;
        names = gameMode.getNames();
        data = gameMode.getValues();

        mainButtonGroup.add(killButton);
        addProgramGroup.add(addButton,BorderLayout.EAST);
        addProgramGroup.add(addField,BorderLayout.CENTER);
        mainButtonGroup.add(addProgramGroup);
        mainButtonGroup.add(delButton);
        this.add(mainButtonGroup,BorderLayout.NORTH);
        table.setModel(new Model());
        this.add(table,BorderLayout.CENTER);
        killButton.setAction(new AbstractAction("Kill!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode.KillPrograms();
            }
        });
        addButton.setAction(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode.addProgram(new GameMode.Program(addField.getText()));
                names = gameMode.getNames();
                data = gameMode.getValues();
                table.updateUI();
                addField.setText("");
            }
        });
        delButton.setAction(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode.delProgram( table.getSelectedRow());
                names = gameMode.getNames();
                data = gameMode.getValues();
                table.updateUI();
            }
        });
    }

}
