package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.Farm;
import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Inventory.Items.ItemStack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FarmPanel extends BuildingStatPanel {

    private static final int SLOTS = 10;

    // NOTE: no initializers on these fields. buildContent() runs inside super(),
    // and field initializers run *after* super() returns, so an initializer
    // (even "= null") would wipe out what buildContent() just created.
    private final Farm farm;
    private final List<IncPanel> incPanels;
    private JPanel invList;
    private JLabel incHeader;
    private JLabel freeSlots;

    public FarmPanel(Farm farm) {
        this.farm = farm;
        this.incPanels = new ArrayList<>();
        super(farm);
        refresh(); // populate everything once with live data
    }

    @Override
    protected void buildContent() {
        // Inventory section — rows are rebuilt in refresh()
        add(makeHeader("Inventory"));
        add(Box.createVerticalStrut(4));

        invList = new JPanel();
        invList.setLayout(new BoxLayout(invList, BoxLayout.Y_AXIS));
        invList.setOpaque(false);
        invList.setAlignmentX(LEFT_ALIGNMENT);
        add(invList);

        add(Box.createVerticalStrut(8));
        add(makeSeparator());
        add(Box.createVerticalStrut(8));

        // Incubator section — each slot is bound to one fixed incubator for good,
        // so its dropdown never has to change under the player
        incHeader = makeHeader("Incubators");
        add(incHeader);
        add(Box.createVerticalStrut(6));

        JPanel incGrid = new JPanel(new GridLayout(2, 5, 4, 4));
        incGrid.setOpaque(false);
        incGrid.setAlignmentX(LEFT_ALIGNMENT);
        List<PlantIncubater> all = farm.getAllInc(); // every incubator, fixed order
        for (int i = 0; i < Math.min(SLOTS, all.size()); i++) {
            IncPanel panel = new IncPanel(all.get(i), i + 1, farm.getColonists());
            incPanels.add(panel);
            incGrid.add(panel);
        }
        add(incGrid);

        add(Box.createVerticalStrut(6));
        freeSlots = makeRow("Free slots: -");
        add(freeSlots);
    }

    @Override
    public void refresh() {
        // Snapshot live lists — the clock threads mutate them while Swing reads
        List<PlantIncubater> active = new ArrayList<>(farm.getActiveInc());
        List<ItemStack> stacks = new ArrayList<>(farm.getInv().getStacks());

        // Inventory
        invList.removeAll();
        if (stacks.isEmpty()) {
            invList.add(makeRow("  (empty)"));
        } else {
            for (ItemStack s : stacks) invList.add(makeRow("  " + s));
        }

        // Header + free slots
        incHeader.setText("Incubators (" + active.size() + " active)");
        freeSlots.setText("Free slots: " + farm.getFreeInc().size());

        // Labels/colours only — dropdowns are left to the player
        for (IncPanel panel : incPanels) panel.refresh();

        revalidate(); // inventory row count can change
        repaint();
    }
}