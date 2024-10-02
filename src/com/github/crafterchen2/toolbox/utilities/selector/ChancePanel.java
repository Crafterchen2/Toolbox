package com.github.crafterchen2.toolbox.utilities.selector;

import com.github.crafterchen2.toolbox.Tool;
import com.github.crafterchen2.toolbox.component.layout.VerticalListLayout;
import com.github.crafterchen2.toolbox.Utility;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Objects;

@Tool
public class ChancePanel extends JPanel implements Utility {

    //Felder
    private final ModuleViewPanel chanceModulePanel = new ModuleViewPanel(new VerticalListLayout(4));

    private final SpinnerNumberModel addAmountModel = new SpinnerNumberModel(1,1,50,1);
    private final JSpinner addAmountSpinner = new JSpinner(addAmountModel);

    private final JButton chooseButton = new JButton("Auswählen");
    private final JButton addChance = new JButton("+");

    private final JPanel addPanel = new JPanel(new BorderLayout());

    private int lastSelectedNr = -1;

    //Listener

    //Konstruktoren
    public ChancePanel() {
        setLayout(new BorderLayout());
        reset();
        JPanel managerPanel = new JPanel(new BorderLayout());
        addAmountSpinner.addChangeListener(ce -> {
            setAddChanceTooltip();
            addPanel.updateUI();
        });
        addAmountSpinner.setToolTipText("Wie viele Einträge hinzugefügt werden sollen (maximal "+addAmountModel.getMaximum()+" Einträge pro Knopfdruck)");
        addPanel.add(addAmountSpinner, BorderLayout.CENTER);
        addChance.addActionListener(ae -> {
            for (int i = 0; i < getSpinnerValue(); i++) {
                chanceModulePanel.add(new ChanceModule(chanceModulePanel));
            }
            chanceModulePanel.updateUI();
        });
        addPanel.add(addChance, BorderLayout.WEST);
        managerPanel.add(addPanel, BorderLayout.WEST);
        chooseButton.addActionListener(ae -> selectRandomElement());
        chooseButton.setToolTipText("Wähle einen zufälligen Eintrag aus.");
        managerPanel.add(chooseButton, BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset");
        resetButton.setToolTipText("Setzt die Liste auf ihre Anfangswerte zurück.");
        resetButton.addActionListener(ae -> reset());
        managerPanel.add(resetButton, BorderLayout.EAST);
        chanceModulePanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        JScrollPane chanceScroller = new JScrollPane(chanceModulePanel);
        chanceScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chanceScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chanceScroller.setColumnHeaderView(managerPanel);
        chanceScroller.setBorder(null);
        chanceScroller.setViewportBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(chanceScroller, BorderLayout.CENTER);
    }

    //Methoden
    private void selectRandomElement() {
        synchronized (chanceModulePanel.getTreeLock()) {
            int nComponents = chanceModulePanel.getComponentCount();
            if (nComponents == 0) {
                return;
            }
            Component[] coms = chanceModulePanel.getComponents();
            ChanceModule[] modules = new ChanceModule[nComponents];
            int sumAmount = 0;
            for (int i = 0; i < nComponents; i++) {
                try {
                    modules[i] = (ChanceModule) coms[i];
                    modules[i].resetColor();
                    modules[i].setLowerChance(sumAmount);
                    sumAmount = sumAmount + modules[i].getAmount();
                } catch (Exception e) {
                    modules[i] = null;
                }
            }
            int lowerChance;
            int rng;
            int selectedNr = -1;
            boolean run;
            do {
                rng = (int) (Math.random() * sumAmount) + 1;
                run = true;
                for (int i = 0; i < nComponents && run; i++) {
                    try {
                        if (modules[i] != null) {
                            if (i == 0) {
                                lowerChance = 0;
                            } else {
                                lowerChance = modules[i].getLowerChance();
                            }
                            if (lowerChance < rng && rng <= Objects.requireNonNull(modules[i]).getUpperChance()) {
                                selectedNr = i;
                                run = false;
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            } while (selectedNr == lastSelectedNr);
            lastSelectedNr = selectedNr;
            modules[selectedNr].select();
            chooseButton.setText(modules[selectedNr].getName() + "!");
            chanceModulePanel.scrollRectToVisible(modules[selectedNr].getBounds());
        }
    }

    //Getter
    private int getSpinnerValue() {
        return addAmountModel.getNumber().intValue();
    }

    //Setter
    private void setAddChanceTooltip() {
        if (getSpinnerValue() == 1) {
            addChance.setToolTipText("Fügt einen Eintrag hinzu.");
        } else {
            addChance.setToolTipText("Fügt " + getSpinnerValue() + " Einträge hinzu.");
        }
    }

    //Maker

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        lastSelectedNr = -1;
        addAmountSpinner.setValue(1);
        chanceModulePanel.removeAll();
        for (int i = 0; i < 4; i++) {
            chanceModulePanel.add(new ChanceModule(chanceModulePanel));
        }
        setAddChanceTooltip();
        chooseButton.setText("Auswählen");
        updateUI();
    }

    ////Utility
    @Override
    public String getUtilitiyName() {
        return "Auswahlhilfe";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new ChancePanel();
    }
    
    @Override
    public int getListPriority() {
        return 2;
    }
    
    //Interne Klassen
    ////Klasse "ModuleViewPanel"
    static class ModuleViewPanel extends JPanel implements Scrollable {

        public ModuleViewPanel(LayoutManager layout) {
            super(layout);
        }

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }
    }
}