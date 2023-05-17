package com.deckerben.utilities;

import com.deckerben.Resettable;
import com.deckerben.component.tab.TabButton;
import com.deckerben.component.tab.TabLabel;
import com.deckerben.utilities.comtest.ComponentTester;
import com.deckerpw.utilities.gamemode.GameMode;
import com.deckerben.utilities.msgpics.component.MessagePicturatorPanel;
import com.deckerpw.utilities.profiletransfer.ProfileTransferUtility;
import com.deckerben.utilities.clicker.ClickerPanel;
import com.deckerpw.utilities.ConfigurableUtility;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ToolboxPanel extends JPanel implements Utility {

    //Felder
    ////Utilityliste
    private ArrayList<Utility> utilityList = createInitialUtilityList();

    ////Components
    private final JTabbedPane tabs = new JTabbedPane(); //siehe kon: JTabbedPane(int tabPlacement, int tabLayoutPolicy) (tabLayoutPolicy sagt ob gescrollt wird oder nicht)
    private final ArrayList<JFrame> framedUtils = new ArrayList<>();

    ////JSON
    private static final File CONFIG_FILE = new File(System.getProperty("user.home")+"/toolbox-config.json");
    private final JSONObject CONFIGS;

    //Listener

    //Konstruktoren
    ToolboxPanel(){
        this(true);
    }

    public ToolboxPanel(boolean showBar){
        reset();
        CONFIGS = loadConfig();
        setLayout(new BorderLayout());
        if (showBar) {
            JMenuBar utilityBar = makeUtilityBar();
            add(utilityBar,BorderLayout.NORTH);
        }
        add(tabs,BorderLayout.CENTER);
    }

    //Methoden
    ////Code von Paul START
    //////Speichern und Laden der JSON Datei
    private JSONObject loadConfig() {
        try {
            return new JSONObject(readFile(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private String readFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    public void saveConfig(String utilityName,JSONObject config){
        CONFIGS.put(utilityName,config);
    }

    public void saveConfigs(){
        try {
            FileWriter myWriter = new FileWriter(CONFIG_FILE);
            myWriter.write(CONFIGS.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ////Code von Paul ENDE

    ////Hinzufügen von Utilities
    public void addUtility(Utility util){
        //utility zu Array "utilities" hinzufügen; "utilities" evtl. -> ArrayList
        utilityList.add(util); //Noch unsicher
        updateUtilityList();
    }

    private void updateUtilityList(){

    }

    private ArrayList<Utility> createInitialUtilityList(){
        ArrayList<Utility> rv = new ArrayList<>(5);
        rv.add(new MessagePicturatorPanel());
        rv.add(new ComponentTester());
        rv.add(new ProfileTransferUtility());
        rv.add(new ClickerPanel());
        rv.add(new GameMode());
        return rv;
    }

    private void resetTabs(){
        synchronized (tabs.getTreeLock()){
            for (int tab = 0; tab < tabs.getTabCount(); tab++) {
                ((Utility) tabs.getComponentAt(tab)).reset();
            }
        }
    }

    private void resetFramedUtils(){
        framedUtils.forEach(jFrame -> ((Utility) jFrame.getContentPane()).reset());
    }

    private void removeFramedUtils(){
        for (int frame = framedUtils.size()-1; (frame >= 0) && (!framedUtils.isEmpty()); frame--) {
            framedUtils.get(frame).dispose();
        }
    }

    //Getter
    ////Code von Paul START
    private JSONObject getConfig(String utilityName){
        if (CONFIGS.has(utilityName))
            return CONFIGS.getJSONObject(utilityName);
        else
            return new JSONObject();
    }
    ////Code von Paul ENDE

    public Utility getUtility(int id){
        return utilityList.get(id);
    }

    public Utility getUtility(String name){
        for (Utility value : utilityList) {
            if (value.getUtilitiyName().equals(name)) return value;
        }
        return null;
    }

    public static Dimension getMaxDimension(Dimension a, Dimension b){
        return new Dimension(Math.max(a.width,b.width),Math.max(a.height,b.height));
    }

    //Setter
    public void setUtilityList(ArrayList<Utility> utilityList) {
        this.utilityList = utilityList;
    }

    //Maker
    ////Modifiziert von Paul
    private JMenuBar makeUtilityBar(){
        JMenuBar bar = new JMenuBar();
        JMenu addMenu = new JMenu("Hinzufügen");
        UtilityMenuItem[] items = new UtilityMenuItem[utilityList.size()];
        for (int i = 0; i<utilityList.size(); i++) {
            items[i] = new UtilityMenuItem(i);
            addMenu.add(items[i]);
            items[i].addActionListener(e -> {
                UtilityMenuItem item = (UtilityMenuItem) e.getSource();
                if (getUtility(item.getUtilityID()) instanceof ConfigurableUtility configUtility) {
                    tabs.addTab(item.getText(),configUtility.createNewInstance(getConfig(getUtility(item.getUtilityID()).getUtilitiyName())));
                } else {
                    tabs.addTab(item.getText(),getUtility(item.getUtilityID()).createNewInstance());
                }
                TabLabel tabLabel = new TabLabel(getUtility(item.getUtilityID()).getUtilitiyName(),tabs);
                TabButton[] tabButtons = new TabButton[2];
                tabButtons[0] = new ResetTabButton(item.getUtilityID());
                tabButtons[1] = new EjectTabButton();
                tabs.setTabComponentAt(tabs.getTabCount()-1,tabLabel.generateRecommendedPanel(true,tabButtons));
                tabs.updateUI();
            });
        }
        bar.add(addMenu);
        JMenu resetMenu = new JMenu("Zurücksetzen");
        JMenuItem resetToolbox = new JMenuItem("Toolbox zurücksetzen");
        resetToolbox.addActionListener(e -> reset());
        resetMenu.add(resetToolbox);
        resetMenu.addSeparator();
        JMenuItem resetAll = new JMenuItem("Alles zurücksetzen");
        resetAll.addActionListener(e -> {
            resetTabs();
            removeFramedUtils();
        });
        resetMenu.add(resetAll);
        resetMenu.addSeparator();
        JMenuItem resetTabs = new JMenuItem("Tabs zurücksetzen");
        resetTabs.addActionListener(e -> resetTabs());
        resetMenu.add(resetTabs);
        JMenuItem resetWindows = new JMenuItem("Externe Fenster zurücksetzen");
        resetWindows.addActionListener(e -> resetFramedUtils());
        resetMenu.add(resetWindows);
        bar.add(resetMenu);
        JMenu removeMenu = new JMenu("Entfernen");
        JMenuItem removeAll = new JMenuItem("Alles entfernen");
        removeAll.addActionListener(e -> reset());
        removeMenu.add(removeAll);
        removeMenu.addSeparator();
        JMenuItem removeTabs = new JMenuItem("Tabs entfernen");
        removeTabs.addActionListener(e -> tabs.removeAll());
        removeMenu.add(removeTabs);
        JMenuItem removeWindows = new JMenuItem("Fenster Entfernen");
        removeWindows.addActionListener(e -> removeFramedUtils());
        removeMenu.add(removeWindows);
        bar.add(removeMenu);
        return bar;
    }

    //Overrides aus
    ////JComponent
    @Override
    public Dimension getMinimumSize() {
        Dimension rv = new Dimension();
        for (int i = 0; i < utilityList.size(); i++) {
            try {
                rv = getMaxDimension(rv,getUtility(i).getComponent().getMinimumSize());
            }   catch (Exception ignored) {

            }
        }
        return getMaxDimension(rv,super.getMinimumSize());
    }

    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        tabs.removeAll();
        removeFramedUtils();
        utilityList.forEach(Resettable::reset);
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "Utilities";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new ToolboxPanel();
    }

    //Interne Klassen
    ////Klasse "UtilityMenuItem"
    private class UtilityMenuItem extends JMenuItem{

        //Felder
        private final int UTILNUMBER;

        //Konstruktoren
        UtilityMenuItem(int utilNumber) {
            this.UTILNUMBER = utilNumber;
            setText(getUtility(UTILNUMBER).getUtilitiyName());
        }

        //Getter
        public int getUtilityID() {
            return UTILNUMBER;
        }
    }

    ////Klasse "ResetTabButton"
    private class ResetTabButton extends TabButton{

        //Konstruktoren
        public ResetTabButton(final int utilityID){
            super("↺");
            addActionListener(e -> {
                int i = tabs.indexOfTabComponent(tabComponent);
                if (i > -1) {
                    getUtility(utilityID).reset();
                    ((Utility)tabs.getComponent(i)).reset();
                }
            });
            setToolTipText("Setze diesen Tab zurück");
        }

    }

    ////Klasse "EjectTabButton"
    private class EjectTabButton extends TabButton{

        //Konstruktoren
        public EjectTabButton(){
            super("⏏");
            setToolTipText("In seperatem Fenster öffnen");
            addActionListener(e -> {
                synchronized (tabs.getTreeLock()){
                    int tabIndex = tabs.indexOfTabComponent(tabComponent);
                    if (tabIndex != -1) {
                        JFrame frame = new JFrame(tabs.getTitleAt(tabIndex)){
                            @Override
                            public void dispose() {
                                framedUtils.remove(this);
                                super.dispose();
                            }
                        };
                        frame.setSize(tabs.getSize());
                        frame.setLocationByPlatform(true);
                        frame.setContentPane((Container) tabs.getComponent(tabIndex));
                        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        framedUtils.add(frame);
                    }
                }
            });
        }
    }
}