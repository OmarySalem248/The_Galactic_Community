package Game.Windows;

import Game.Engine.Actions.ColonyActions.AssignAction;
import Game.Engine.Buildings.Building;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.Profession.ProfessionRegistry;
import Game.Engine.Game;
import Game.Engine.Government.ColonyLeadership;

import javax.swing.*;
import java.awt.*;

public class ColonistsWindow extends JPanel {

    private final Game game;
    private final ColonyLeadership leadership;
    private final RelationshipPanel relPanel;

    private final JComboBox<Colonist> colonistDropdown;
    private final JComboBox<String>   buildingDropdown;
    private final JComboBox<String>   professionDropdown;

    private final JLabel occupationLabel;
    private JTextArea inventoryArea;
    private JTextArea deliveryArea;
    private final JLabel energyLabel;
    private final JLabel hpLabel;
    private final JLabel ageLabel;
    private final JLabel colonistUpdate;


    private final java.util.Set<Colonist> knownColonists = new java.util.HashSet<>();
    private boolean updatingDropdown           = false;
    private boolean updatingProfessionDropdown = false;

    public ColonistsWindow(Game game, RelationshipPanel relPanel) {
        this.game       = game;
        this.leadership = game.getColony().getLeadership();
        this.relPanel   = relPanel;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(320, 0));
        setBorder(BorderFactory.createTitledBorder("Colonist Stats"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        int row = 0;

        // Colonist dropdown
        colonistDropdown = new JComboBox<>();
        for (Colonist c : game.getColony().getColonists()) {
            colonistDropdown.addItem(c);
            knownColonists.add(c);
        }
        colonistDropdown.addActionListener(e -> updateColonistStats());

        // Stat labels
        Font statFont = new Font("Monospaced", Font.PLAIN, 14);
        occupationLabel = new JLabel(); occupationLabel.setFont(statFont);
        energyLabel     = new JLabel(); energyLabel.setFont(statFont);
        hpLabel         = new JLabel(); hpLabel.setFont(statFont);
        ageLabel        = new JLabel(); ageLabel.setFont(statFont);
        colonistUpdate  = new JLabel(); colonistUpdate.setFont(statFont);



        // Building + profession dropdowns
        buildingDropdown = new JComboBox<>();
        buildingDropdown.addActionListener(e -> assignColonistToBuilding());

        professionDropdown = new JComboBox<>();
        for (String profName : ProfessionRegistry.getAllNames()) professionDropdown.addItem(profName);
        professionDropdown.addActionListener(e -> { if (!updatingProfessionDropdown) changeColonistProfession(); });

        // Layout
        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Select Colonist:"), gbc);
        gbc.gridx = 1; add(colonistDropdown, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; add(ageLabel, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Occupation:"), gbc);
        gbc.gridx = 1; add(occupationLabel, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Energy:"), gbc);
        gbc.gridx = 1; add(energyLabel, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("HP:"), gbc);
        gbc.gridx = 1; add(hpLabel, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Assigned Building:"), gbc);
        gbc.gridx = 1; add(buildingDropdown, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; add(colonistUpdate, gbc); row++;

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Profession:"), gbc);
        gbc.gridx = 1; add(professionDropdown, gbc); row++;
        // Inventory debug
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        add(new JLabel("── Inventory ──"), gbc); row++;

        inventoryArea = new JTextArea(6, 20);
        inventoryArea.setEditable(false);
        inventoryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        add(new JScrollPane(inventoryArea), gbc); row++;

        // Deliveries debug
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        add(new JLabel("── Deliveries ──"), gbc); row++;

        deliveryArea = new JTextArea(4, 20);
        deliveryArea.setEditable(false);
        deliveryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        add(new JScrollPane(deliveryArea), gbc);



        updateColonistStats();
    }

    public void updateColonistDropdown() {
        for (Colonist c : knownColonists)
            if (!c.isAlive()) colonistDropdown.removeItem(c);
        for (Colonist c : game.getColony().getColonists()) {
            if (!knownColonists.contains(c)) {
                colonistDropdown.addItem(c);
                knownColonists.add(c);
            }
        }
    }


    public void updateColonistStats() {
        updatingDropdown = true;
        Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
        if (selected != null) {
            if (leadership.getCurrentLeader() == selected) {
                occupationLabel.setText("Tribe Leader");
                professionDropdown.setEnabled(false);
            } else {
                occupationLabel.setText(selected.getOccupation());
                professionDropdown.setEnabled(true);
                updatingProfessionDropdown = true;
                professionDropdown.setSelectedItem(selected.getProfession().getName());
                updatingProfessionDropdown = false;
            }
            energyLabel.setText(String.valueOf(selected.getEnergy()));
            hpLabel.setText(String.valueOf(selected.getHealth()));
            ageLabel.setText(selected.getAge() + " Years " + selected.getAgeMonths() + " Months");
            colonistUpdate.setText(selected.getStatus());
            relPanel.updateTable(selected);
            // Inventory
            StringBuilder inv = new StringBuilder();
            if (selected.getInventory().isEmpty()) {
                inv.append("(empty)");
            } else {
                for (var stack : selected.getInventory().getStacks()) {
                    inv.append(stack.toString()).append("\n");
                }
            }
            inventoryArea.setText(inv.toString());

            // Deliveries
            StringBuilder del = new StringBuilder();
            if (!selected.getInventory().hasDeliveries()) {
                del.append("(none)");
            } else {
                for (var delivery : selected.getInventory().getDeliveries()) {
                    del.append("→ ").append(delivery.getDestination() != null ? "assigned" : "unassigned").append("\n");
                    for (var stack : delivery.getItems()) {
                        del.append("  ").append(stack.toString()).append("\n");
                    }
                }
            }
            deliveryArea.setText(del.toString());

            buildingDropdown.removeAllItems();
            buildingDropdown.addItem("Unassigned");
            for (Building b : game.getColony().getBuildings()) buildingDropdown.addItem(b.getName());
            buildingDropdown.setSelectedItem(
                    selected.getAssignedBuilding() != null ? selected.getAssignedBuilding().getName() : "Unassigned"
            );
        }
        updatingDropdown = false;
    }

    private void changeColonistProfession() {
        Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
        if (selected == null) return;
        if (leadership.getCurrentLeader() == selected) {
            JOptionPane.showMessageDialog(this, "The Tribe Leader's role cannot be changed!", "Leader Protected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selected.getAge() < 16) {
            JOptionPane.showMessageDialog(this, "Child Labour Laws bro", "Too young", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newProfessionName = (String) professionDropdown.getSelectedItem();
        if (newProfessionName == null || newProfessionName.equals(selected.getProfession().getName())) return;

        selected.setProfession(ProfessionRegistry.get(newProfessionName));
        if (selected.getAssignedBuilding() != null && !selected.getAssignedBuilding().isJobCompatible(selected))
            selected.unassignBuilding();

        updateColonistStats();
    }

    private void assignColonistToBuilding() {
        if (updatingDropdown) return;
        Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
        if (selected == null) return;

        String chosen = (String) buildingDropdown.getSelectedItem();
        Building building = null;
        if (!"Unassigned".equals(chosen)) {
            for (Building b : game.getColony().getBuildings()) {
                if (b.getName().equals(chosen)) { building = b; break; }
            }
        }

        AssignAction action = new AssignAction(selected, building);
        if (!game.getColony().performAction(action)) {
            JOptionPane.showMessageDialog(this, "Invalid assignment!", "Error", JOptionPane.ERROR_MESSAGE);
            buildingDropdown.setSelectedItem("Unassigned");
        }
        updateColonistStats();
    }



}
