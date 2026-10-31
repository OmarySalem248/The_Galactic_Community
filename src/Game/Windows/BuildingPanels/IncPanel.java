package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Items.Plant.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * One incubator slot on the farm panel, bound to one incubator for its whole life.
 * refresh() only updates the labels and colour — the farmer dropdown is never
 * touched by code after construction, it belongs to the player.
 */
public class IncPanel extends JPanel {

    private static final String UNASSIGNED = "Unassigned";

    private static final Color READY   = new Color(0x2E7D32); // green
    private static final Color TEND    = new Color(0xB7791F); // amber
    private static final Color GROWING = new Color(0x1F3A5F); // dark blue
    private static final Color DEAD    = new Color(0x7F1D1D); // dark red
    private static final Color EMPTY   = new Color(0x2A2A45);

    private final PlantIncubater inc;

    private final JLabel nameLabel;
    private final JLabel progressLabel;
    private final JLabel statusLabel;
    private final JComboBox<Object> farmerBox;

    public IncPanel(PlantIncubater inc, int index, List<Colonist> farmers) {
        this.inc = inc;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("#" + index));
        setOpaque(true);

        nameLabel     = new JLabel("Empty");
        progressLabel = new JLabel(" ");
        statusLabel   = new JLabel(" ");
        for (JLabel l : new JLabel[]{nameLabel, progressLabel, statusLabel}) {
            l.setForeground(Color.WHITE);
            l.setAlignmentX(CENTER_ALIGNMENT);
            add(l);
        }

        // Dropdown: filled once, starts on the current assignment, then left alone
        farmerBox = new JComboBox<>();
        farmerBox.addItem(UNASSIGNED);
        for (Colonist c : farmers) farmerBox.addItem(c);
        farmerBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int i,
                                                          boolean sel, boolean focus) {
                Object shown = (value instanceof Colonist c) ? c.getName() : value;
                return super.getListCellRendererComponent(list, shown, i, sel, focus);
            }
        });
        Colonist assigned = inc.getAssignedFarmer();
        farmerBox.setSelectedItem(assigned != null ? assigned : UNASSIGNED);
        farmerBox.addActionListener(e -> onFarmerChosen()); // added after setup so setup doesn't fire it
        farmerBox.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(4));
        add(farmerBox);

        refresh();
    }

    /** Called every tick by FarmPanel. Updates labels and colour only. */
    public void refresh() {
        Plant plant = inc.getPlant();
        if (plant == null) {
            setBackground(EMPTY);
            nameLabel.setText("Empty");
            progressLabel.setText(" ");
            statusLabel.setText(" ");
            return;
        }

        nameLabel.setText(plant.getName());
        progressLabel.setText(String.valueOf(plant.getProgress()));

        if (plant.isDead()) {
            setBackground(DEAD);
            statusLabel.setText("Dead");
        } else if (plant.isMatured()) {
            setBackground(READY);
            statusLabel.setText("Ready");
        } else if (inc.needsTending()) {
            setBackground(TEND);
            statusLabel.setText("Needs tending");
        } else {
            setBackground(GROWING);
            statusLabel.setText("Growing");
        }
    }

    private void onFarmerChosen() {
        Object chosen = farmerBox.getSelectedItem();
        if (chosen instanceof Colonist farmer) {
            inc.assign(farmer);
        } else {
            inc.unassign();
        }
    }
}