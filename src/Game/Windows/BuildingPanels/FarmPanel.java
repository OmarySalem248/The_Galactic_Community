package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FarmPanel extends BuildingStatPanel {

    private final Farm farm;
    private List<IncPanel> incPanels;

    public FarmPanel(Farm farm) {
        this.farm = farm;
        incPanels = new ArrayList<>();
        super(farm);

    }

    @Override
    protected void buildContent() {
        // Inventory section
        add(makeHeader("Inventory"));
        add(Box.createVerticalStrut(4));
        if (building.getInv().isEmpty()) {
            add(makeRow("  (empty)"));
        } else {
            building.getInv().getStacks().forEach(s -> add(makeRow("  " + s)));
        }
        add(Box.createVerticalStrut(8));
        add(makeSeparator());
        add(Box.createVerticalStrut(8));

        // Incubator section — show first 10 active incubators
        add(makeHeader("Incubators (" + farm.getActiveInc().size() + " active)"));
        add(Box.createVerticalStrut(6));

        JPanel incGrid = new JPanel(new GridLayout(2, 5, 4, 4));
        incGrid.setOpaque(false);
        incGrid.setAlignmentX(LEFT_ALIGNMENT);

        List<PlantIncubater> active = farm.getActiveInc();
        for (int i = 0; i < 10; i++) {
            PlantIncubater inc = i < active.size() ? active.get(i) : new PlantIncubater();
            IncPanel panel = new IncPanel(inc, i + 1, farm.getColonists());
            incPanels.add(panel);
            incGrid.add(panel);
        }

        add(incGrid);
        add(Box.createVerticalStrut(6));
        add(makeRow("Free slots: " + farm.getFreeInc().size()));
    }

    @Override
    public void refresh() {
        // Refresh all incubator panels
        List<PlantIncubater> active = farm.getActiveInc();
        for (int i = 0; i < incPanels.size(); i++) {
            incPanels.get(i).refresh();
        }
        repaint();
    }
}