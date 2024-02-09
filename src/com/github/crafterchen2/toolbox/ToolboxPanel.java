package com.github.crafterchen2.toolbox;

import com.github.crafterchen2.toolbox.component.tab.TabButton;
import com.github.crafterchen2.toolbox.component.tab.TabLabel;
import com.github.crafterchen2.toolbox.utilities.animshift.AnimationShifter;
import com.github.crafterchen2.toolbox.utilities.clicker.ClickerPanel;
import com.github.crafterchen2.toolbox.utilities.comtest.ComponentTester;
import com.github.crafterchen2.toolbox.utilities.msgpics.component.MessagePicturatorPanel;
import com.github.crafterchen2.toolbox.utilities.randombit.RandomBitPanel;
import com.github.crafterchen2.toolbox.utilities.selector.ChancePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToolboxPanel extends JPanel implements Utility {

    //Felder
    ////Utilityliste
    private final ArrayList<Utility> utilityList = createInitialUtilityList();

    ////Components
    private final JTabbedPane tabs = new JTabbedPane(); //siehe kon: JTabbedPane(int tabPlacement, int tabLayoutPolicy) (tabLayoutPolicy sagt ob gescrollt wird oder nicht)
    private final ArrayList<JFrame> framedUtils = new ArrayList<>();

    //Listener

    //Konstruktoren
    ToolboxPanel(){
        this(true);
    }

    public ToolboxPanel(boolean showBar){
        reset();
        setLayout(new BorderLayout());
        if (showBar) {
            add(makeUtilityBar(),BorderLayout.NORTH);
        }
        add(tabs,BorderLayout.CENTER);
    }

    //Methoden
    ////Erstellen der Utility-liste
    private ArrayList<Utility> createInitialUtilityList(){
        ArrayList<Utility> rv = new ArrayList<>(6);
        rv.add(new MessagePicturatorPanel());
        rv.add(new AnimationShifter());
        rv.add(new ComponentTester());
        rv.add(new ClickerPanel());
        rv.add(new ChancePanel());
        rv.add(new RandomBitPanel());
        return rv;
    }

    ////Resetting
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

    private void resetAll() {
        resetTabs();
        resetFramedUtils();
    }

    ////Removing
    private void removeFramedUtils(){
        for (int frame = framedUtils.size()-1; (frame >= 0) && (!framedUtils.isEmpty()); frame--) {
            framedUtils.get(frame).dispose();
        }
    }

    //Getter
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

    //Maker
    private JMenuBar makeUtilityBar(){
        JMenuBar bar = new JMenuBar();
        JMenu addMenu = new JMenu("Hinzufügen");
        UtilityMenuItem[] items = new UtilityMenuItem[utilityList.size()];
        for (int i = 0; i<utilityList.size(); i++) {
            items[i] = new UtilityMenuItem(i);
            addMenu.add(items[i]);
            items[i].addActionListener(e -> {
                synchronized (tabs.getTreeLock()){
                    UtilityMenuItem item = (UtilityMenuItem) e.getSource();
                    tabs.addTab(item.getText(),getUtility(item.getUtilityID()).createNewInstance());
                    TabLabel tabLabel = new TabLabel(getUtility(item.getUtilityID()).getUtilitiyName(),tabs);
                    TabButton[] tabButtons = new TabButton[2];
                    tabButtons[0] = new ResetTabButton(item.getUtilityID());
                    tabButtons[1] = new EjectTabButton();
                    tabs.setTabComponentAt(tabs.getTabCount()-1,tabLabel.generateRecommendedPanel(true,tabButtons));
                    tabs.updateUI();
                }
            });
        }
        bar.add(addMenu);
        JMenu resetMenu = new JMenu("Zurücksetzen");
        JMenuItem resetToolbox = new JMenuItem("Toolbox zurücksetzen");
        resetToolbox.setToolTipText("Setzt die gesamte Toolbox auf den Ursprungszustand zurück.");
        resetToolbox.addActionListener(e -> reset());
        resetMenu.add(resetToolbox);
        resetMenu.addSeparator();
        JMenuItem resetAll = new JMenuItem("Alles zurücksetzen");
        resetAll.setToolTipText("Setzt jeden Tab und jedes externe Fenster zurück, ohne sie zu schließen.");
        resetAll.addActionListener(e -> resetAll());
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
        JMenuItem removeWindows = new JMenuItem("Externe Fenster Entfernen");
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