package Game.Windows;

import Game.Engine.Actions.AssignAction;
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
    private final JLabel energyLabel;
    private final JLabel hpLabel;
    private final JLabel ageLabel;
    private final JLabel colonistUpdate;
    private final JButton feedButton;
    private final JButton reduceFeedButton;

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

        // Feed buttons
        feedButton = new JButton("Feed 1 Extra Food");
        reduceFeedButton = new JButton("Deallocate 1 Food");
        feedButton.addActionListener(e -> {
            Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
            if (selected != null) {
                boolean success = game.getColony().feedColonist(selected, 1);
                if (success) updateColonistStats();
                else JOptionPane.showMessageDialog(this, "Not enough food to feed " + selected.getName(), "Insufficient Food", JOptionPane.WARNING_MESSAGE);
            }
        });
        reduceFeedButton.addActionListener(e -> {
            Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
            if (selected != null) {
                if (selected.getEnergy() > 1) {
                    boolean success = game.getColony().feedColonist(selected, -1);
                    if (success) updateColonistStats();
                } else {
                    JOptionPane.showMessageDialog(this, "Can't reduce feed any further for " + selected.getName(), "MINIMUM FEED REACHED", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

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

        gbc.gridx = 0; gbc.gridy = row; add(feedButton, gbc);
        gbc.gridx = 1; add(reduceFeedButton, gbc);

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

        selected.setProfession(ProfessionRegistry.create(newProfessionName));
        if (selected.getAssignedBuilding() != null && !selected.getAssignedBuilding().isCompatible(selected))
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
    public record ColonistControls(
            JButton feedButton,
            JButton reduceFeedButton,
            JComboBox<Colonist> colonistDropdown,
            JComboBox<String> buildingDropdown
    ) {}

    public ColonistControls getControls() {
        return new ColonistControls(feedButton, reduceFeedButton, colonistDropdown, buildingDropdown);
    }
}
