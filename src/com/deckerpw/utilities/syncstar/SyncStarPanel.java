package com.deckerpw.utilities.syncstar;

import com.deckerpw.utilities.ConfigurableUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SyncStarPanel extends JPanel implements ConfigurableUtility {


    private JPanel topGroup = new JPanel(new BorderLayout());
        private JPanel groupGroup = new JPanel(new BorderLayout(5,0));
            private JPanel labelGroup = new JPanel(new GridLayout(2,1));
                private JLabel remoteLabel = new JLabel("Remote Location:");
                private JLabel localLabel = new JLabel("Local Location:");

            private JPanel fieldGroup = new JPanel(new GridLayout(2,1));
                private JTextField remoteField = new JTextField();
                private JTextField localField = new JTextField();
        private JButton addButton = new JButton("Add");
    private static ArrayList<Sync> syncs;


    class Model implements TableModel {

        @Override
        public int getRowCount() {
            return syncs.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnIndex == 0 ? "Name" : "Enabled";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 2 ? true : false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex){
                case 0:
                    return syncs.get(rowIndex).getLocalPath();
                case 1:
                    return syncs.get(rowIndex).getRemotePath();
                default:
                    return "Sync";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    public SyncStarPanel(){
        super(new BorderLayout());
        labelGroup.add(remoteLabel);
        labelGroup.add(localLabel);
        groupGroup.add(labelGroup,BorderLayout.WEST);
        fieldGroup.add(remoteField);
        fieldGroup.add(localField);
        groupGroup.add(fieldGroup,BorderLayout.CENTER);
        topGroup.add(groupGroup,BorderLayout.CENTER);
        topGroup.add(addButton,BorderLayout.EAST);
        this.add(topGroup,BorderLayout.NORTH);
        Model model= new Model();
        JTable table = new JTable( model );
        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                ((DefaultTableModel)table.getModel()).removeRow(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(table, delete, 2);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        this.add(table);
    }

    @Override
    public boolean canReset() {
        return false;
    }

    @Override
    public JSONObject getConfigObject() {

        return null;
    }

    @Override
    public Component createNewInstance(JSONObject config) {
        if (!config.isEmpty()){
            JSONArray list =  config.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                //syncs.add(new Sync(list.getJSONObject(i).))
            }
        }else{

        }
        return new SyncStarPanel();
    }

    @Override
    public String getUtilitiyName() {
        return "Sync Star";
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
