package com.github.crafterchen2.toolbox;

import com.github.crafterchen2.toolbox.component.MenuTools;
import com.github.crafterchen2.toolbox.component.layout.MonoLayout;
import com.github.crafterchen2.toolbox.component.tab.TabButton;
import com.github.crafterchen2.toolbox.component.tab.TabLabel;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

//Classes {
public class ToolboxPanel extends JPanel implements Utility {
	
	//Fields {
	private final ArrayList<Utility> utilityList = createInitialUtilityList();
	
	private final JTabbedPane tabs = new JTabbedPane(); //siehe kon: JTabbedPane(int tabPlacement, int tabLayoutPolicy) (tabLayoutPolicy sagt ob gescrollt wird oder nicht)
	private final ArrayList<JFrame> framedUtils = new ArrayList<>();
	//} Fields
	
	//Constructor {
	ToolboxPanel() {
		this(true);
	}
	
	public ToolboxPanel(boolean showBar) {
		reset();
		setLayout(new BorderLayout());
		if (showBar) {
			add(makeUtilityBar(), BorderLayout.NORTH);
		}
		add(tabs, BorderLayout.CENTER);
	}
	//} Constructor
	
	//Methods {
	public static Dimension getMaxDimension(Dimension a, Dimension b) {
		return new Dimension(Math.max(a.width, b.width), Math.max(a.height, b.height));
	}
	
	private ArrayList<Utility> createInitialUtilityList() {
		ArrayList<Utility> rv = new ArrayList<>();
		Reflections reflections = new Reflections(new ConfigurationBuilder()
														  .forPackages("")  // Empty string means scan the entire classpath
														  .addScanners(Scanners.TypesAnnotated));
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Tool.class);
		for (Class<?> clazz : annotatedClasses) {
			if (Utility.class.isAssignableFrom(clazz)) {
				try {
					Utility util = (Utility) clazz.getDeclaredConstructor().newInstance();
					if (util.getUtilitiyName() != null && !util.getUtilitiyName().isEmpty()) {
						if (util.getComponent() != null && util.createNewInstance() != null) {
							rv.add(util);
						} else {
							rv.add(new ErrorUtil(util.getClass().getName(), "No Component provided."));
						}
					} else {
						rv.add(new ErrorUtil(util.getClass().getName(), "No name provided."));
					}
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					rv.add(new ErrorUtil(clazz.getName(), "Could not create object."));
				}
			}
		}
		rv.sort((u1, u2) -> {
			int prioSort = -Integer.compare(u1.getListPriority(), u2.getListPriority());
			if (prioSort == 0) return String.CASE_INSENSITIVE_ORDER.compare(u1.getUtilitiyName(), u2.getUtilitiyName());
			return prioSort;
		});
		return rv;
	}
	
	private void resetTabs() {
		synchronized (tabs.getTreeLock()) {
			for (int tab = 0; tab < tabs.getTabCount(); tab++) {
				((Utility) tabs.getComponentAt(tab)).reset();
			}
		}
	}
	
	private void resetFramedUtils() {
		framedUtils.forEach(jFrame -> ((Utility) jFrame.getContentPane()).reset());
	}
	
	private void resetAll() {
		resetTabs();
		resetFramedUtils();
	}
	
	private void removeFramedUtils() {
		for (int frame = framedUtils.size() - 1; (frame >= 0) && (!framedUtils.isEmpty()); frame--) {
			framedUtils.get(frame).dispose();
		}
	}
	
	public Utility getUtility(int id) {
		return utilityList.get(id);
	}
	
	public Utility getUtility(String name) {
		for (Utility value : utilityList) {
			if (value.getUtilitiyName().equals(name)) return value;
		}
		return null;
	}
	
	private void addUtilToTabs(Component util, final int id, String name){
		tabs.addTab(name, util);
		TabLabel tabLabel = new TabLabel(name, tabs);
		TabButton[] tabButtons = new TabButton[2];
		tabButtons[0] = new ResetTabButton(id);
		tabButtons[1] = new EjectTabButton(id);
		tabs.setTabComponentAt(tabs.getTabCount() - 1, tabLabel.generateRecommendedPanel(true, tabButtons));
		tabs.updateUI();
	}
	
	private JMenuBar makeUtilityBar() {
		JMenuBar bar = new JMenuBar();
		JMenu addMenu = new JMenu("Hinzufügen");
		UtilityMenuItem[] items = new UtilityMenuItem[utilityList.size()];
		for (int i = 0; i < utilityList.size(); i++) {
			items[i] = new UtilityMenuItem(i);
			addMenu.add(items[i]);
			items[i].addActionListener(e -> {
				synchronized (tabs.getTreeLock()) {
					UtilityMenuItem item = (UtilityMenuItem) e.getSource();
					addUtilToTabs(getUtility(item.getUtilityID()).createNewInstance(), item.getUtilityID(), item.getText());
				}
			});
		}
		bar.add(addMenu);
		JMenu resetMenu = new JMenu("Zurücksetzen");
		resetMenu.add(MenuTools.item("Toolbox zurücksetzen", "Setzt die gesamte Toolbox auf den Ursprungszustand zurück.", _ -> reset()));
		resetMenu.addSeparator();
		resetMenu.add(MenuTools.item("Alles zurücksetzen", "Setzt jeden Tab und jedes externe Fenster zurück, ohne sie zu schließen.", _ -> resetAll()));
		resetMenu.addSeparator();
		resetMenu.add(MenuTools.item("Tabs zurücksetzen", _ -> resetTabs()));
		resetMenu.add(MenuTools.item("Externe Fenster zurücksetzen", _ -> resetFramedUtils()));
		bar.add(resetMenu);
		JMenu removeMenu = new JMenu("Entfernen");
		removeMenu.add(MenuTools.item("Toolbox entfernen", _ -> System.exit(0)));
		removeMenu.addSeparator();
		removeMenu.add(MenuTools.item("Alles entfernen", _ -> reset()));
		removeMenu.addSeparator();
		removeMenu.add(MenuTools.item("Tabs entfernen", _ -> tabs.removeAll()));
		removeMenu.add(MenuTools.item("Externe Fenster entfernen", _ -> removeFramedUtils()));
		bar.add(removeMenu);
		return bar;
	}
	//} Methods
	
	//Overrides {
	@Override
	public Dimension getMinimumSize() {
		Dimension rv = new Dimension();
		for (int i = 0; i < utilityList.size(); i++) {
			try {
				rv = getMaxDimension(rv, getUtility(i).getComponent().getMinimumSize());
			} catch (Exception ignored) {
			
			}
		}
		return getMaxDimension(rv, super.getMinimumSize());
	}
	
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
	//} Overrides
	
	//Classes {
	private static class ErrorUtil implements Utility {
		
		private final String name;
		private final String reason;
		
		public ErrorUtil() {
			this(null, null);
		}
		
		public ErrorUtil(String name) {
			this(name, null);
		}
		
		public ErrorUtil(String name, String reason) {
			if (name == null) name = "unknown";
			if (reason == null) reason = "No reason provided.";
			this.name = name;
			this.reason = reason;
		}
		
		@Override
		public String getUtilitiyName() {
			return name;
		}
		
		@Override
		public Component getComponent() {
			JPanel c = new JPanel(new MonoLayout(10,10));
			c.add(new JLabel(getUtilitiyName() + " failed to load. Reason: " + reason));
			return c;
		}
		
		@Override
		public Component createNewInstance() {
			return getComponent();
		}
		
		@Override
		public boolean canReset() {
			return false;
		}
		
		@Override
		public void resetCode() {
		
		}
		
		@Override
		public int getListPriority() {
			return Integer.MIN_VALUE;
		}
	}
	
	private class UtilityMenuItem extends JMenuItem {
		
		//Fields {
		private final int UTILNUMBER;
		//} Fields
		
		//Constructor {
		UtilityMenuItem(int utilNumber) {
			this.UTILNUMBER = utilNumber;
			setText(getUtility(UTILNUMBER).getUtilitiyName());
		}
		//} Constructor
		
		//Getter {
		public int getUtilityID() {
			return UTILNUMBER;
		}
		//} Getter
	}
	
	private class ResetTabButton extends TabButton {
		
		//Constructor {
		public ResetTabButton(final int utilityID) {
			super("↺");
			addActionListener(_ -> {
				int i = tabs.indexOfTabComponent(tabComponent);
				if (i > -1) {
					getUtility(utilityID).reset();
					((Utility) tabs.getComponent(i)).reset();
				}
			});
			setToolTipText("Setze diesen Tab zurück");
		}
		//} Constructor
		
	}
	
	private class EjectTabButton extends TabButton {
		
		//Constructor {
		public EjectTabButton(final int utilityID) {
			super("⏏");
			setToolTipText("In seperatem Fenster öffnen");
			addActionListener(_ -> {
				synchronized (tabs.getTreeLock()) {
					int tabIndex = tabs.indexOfTabComponent(tabComponent);
					if (tabIndex != -1) {
						JFrame frame = new JFrame(tabs.getTitleAt(tabIndex)) {
							//Overrides {
							@Override
							public void dispose() {
								framedUtils.remove(this);
								super.dispose();
							}
							//} Overrides
						};
						frame.setSize(tabs.getSize());
						frame.setLocationByPlatform(true);
						JMenuBar bar = new JMenuBar();
						JMenu toolMenu = new JMenu("Tool");
						toolMenu.add(MenuTools.item("Zurücksetzen", _ -> ((Utility) frame.getContentPane()).reset()));
						toolMenu.add(MenuTools.item("Entfernen", _ -> frame.dispose()));
						toolMenu.add(MenuTools.item("Zu Tab", "Schließt das Fenster und reiht sich wieder zu den Tabs ein.", _ -> {
							addUtilToTabs(frame.getContentPane(), utilityID, frame.getTitle());
							frame.dispose();
						}));
						bar.add(toolMenu);
						frame.setJMenuBar(bar);
						frame.setContentPane((Container) tabs.getComponent(tabIndex));
						frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						frame.setVisible(true);
						framedUtils.add(frame);
					}
				}
			});
		}
		//} Constructor
		
	}
	//} Classes
}
//} Classes