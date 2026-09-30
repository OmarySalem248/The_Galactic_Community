package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.PlantIncubater;
import Game.Engine.Colonist.Colonist;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class IncPanel extends JPanel {

    private final PlantIncubater inc;
    private final JLabel nameLabel;
    private final JLabel progressLabel;
    private final JLabel statusLabel;
    private final JComboBox<String> farmerDropdown;
    private ArrayList<Colonist> farmers;
    private boolean updatingDropdown = false;

    public IncPanel(PlantIncubater inc, int index, ArrayList<Colonist> farmers) {
        this.inc     = inc;
        this.farmers = farmers;
        setLayout(new GridLayout(4, 1, 2, 2));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Inc " + index,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Monospaced", Font.PLAIN, 9)));
        setPreferredSize(new Dimension(90, 75));

        nameLabel     = new JLabel("", SwingConstants.CENTER);
        progressLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel   = new JLabel("", SwingConstants.CENTER);
        farmerDropdown = new JComboBox<>();

        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 9));
        progressLabel.setFont(new Font("Monospaced", Font.PLAIN, 8));
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 8));
        farmerDropdown.setFont(new Font("Monospaced", Font.PLAIN, 8));

        // Populate dropdown
        farmerDropdown.addItem("Unassigned");
        for (Colonist c : farmers) farmerDropdown.addItem(c.getName());

        farmerDropdown.addActionListener(e -> {
            if (updatingDropdown) return;
            int selected = farmerDropdown.getSelectedIndex();
            if (selected == 0) {
                inc.unassign();
            } else {
                inc.assign(farmers.get(selected - 1));
            }
        });

        add(nameLabel);
        add(progressLabel);
        add(statusLabel);
        add(farmerDropdown);

        refresh();
    }

    public void refresh() {
        if (inc.isEmpty()) {
            nameLabel.setText("Empty");
            progressLabel.setText("");
            statusLabel.setText("");
            setBackground(new Color(0x2A2A45));
        } else if (inc.isDead()) {
            nameLabel.setText("Dead");
            progressLabel.setText("");
            statusLabel.setText("");
            setBackground(new Color(0x5A1A1A));
        } else {
            nameLabel.setText(inc.getPlant().getName());
            progressLabel.setText("Progress: " + inc.getPlant().getProgress());
            statusLabel.setText(inc.needsTending() ? "⚠ Needs tend" :
                    inc.canHarvest()   ? "✓ Ready"      : "Growing");
            setBackground(inc.canHarvest()   ? new Color(0x1A5A1A) :
                    inc.needsTending() ? new Color(0x5A4A1A) :
                            new Color(0x1A2A3A));
        }

        // Sync dropdown to current assigned farmer without triggering listener
        updatingDropdown = true;
        Colonist assigned = inc.getAssignedFarmer();
        if (assigned == null) {
            farmerDropdown.setSelectedIndex(0);
        } else {
            farmerDropdown.setSelectedItem(assigned.getName());
        }
        updatingDropdown = false;
    }
}