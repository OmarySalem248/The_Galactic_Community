package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.Building;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Windows.BuildingPanels.BuildingStatPanel;

/**
 * Generic fallback stat panel for buildings without a dedicated subclass.
 * Shows inventory contents and assigned colonists.
 */
public class DefaultBuildingStatPanel extends BuildingStatPanel {

    public DefaultBuildingStatPanel(Building building) {
        super(building);
    }

    @Override
    protected void buildContent() {
        add(makeHeader("Inventory"));
        add(javax.swing.Box.createVerticalStrut(4));
        if (building.getInv().isEmpty()) {
            add(makeRow("  (empty)"));
        } else {
            for (ItemStack stack : building.getInv().getStacks()) {
                add(makeRow("  " + stack.toString()));
            }
        }

        add(javax.swing.Box.createVerticalStrut(8));
        add(makeSeparator());
        add(javax.swing.Box.createVerticalStrut(8));

        add(makeHeader("Assigned Colonists"));
        add(javax.swing.Box.createVerticalStrut(4));
        if (building.getColonists().isEmpty()) {
            add(makeRow("  (none)"));
        } else {
            building.getColonists().forEach(c -> add(makeRow("  " + c.getName())));
        }
    }
}
