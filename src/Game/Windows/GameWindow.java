package Game.Windows;

import Game.Actions.AssignAction;
import Game.Buildings.Building;
import Game.Colonist.Colonist;
import Game.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private Game game;
    private JLabel turnLabel;
    private JLabel resLabel;

    private JComboBox<Colonist> colonistDropdown;
    private JLabel energyLabel;
    private JLabel productivityLabel;
    private JLabel occupationLabel;
    private JButton feedButton;

    private JComboBox<String> buildingDropdown; // NEW
    private JButton nextTurnBtn;
    private JButton buildBtn;
    private boolean updatingDropdown = false;
    public GameWindow(Game game) {
        this.game = game;

        JFrame frame = new JFrame("Colony Builder - Micromanagement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Top panel: Turn and resources
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        turnLabel = new JLabel("Turn: " + game.getTurn(), SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resLabel = new JLabel(game.getColony().getResources().toString(), SwingConstants.CENTER);
        resLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        infoPanel.add(turnLabel);
        infoPanel.add(resLabel);
        frame.add(infoPanel, BorderLayout.NORTH);

        // Center panel: Colonist selection and stats
        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        colonistDropdown = new JComboBox<>();
        for (Colonist c : game.getColony().getColonists()) {
            colonistDropdown.addItem(c);
        }
        colonistDropdown.addActionListener(e -> updateColonistStats());

        occupationLabel = new JLabel();
        energyLabel = new JLabel();
        productivityLabel = new JLabel();

        feedButton = new JButton("Feed 1 Extra Food");
        feedButton.addActionListener(e -> {
            Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
            if (selected != null) {
                boolean success = game.getColony().feedColonist(selected, 1);
                if (success) {
                    updateColonistStats();
                    updateGameStats();
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough food to feed " + selected.getName(),
                            "Insufficient Food", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // NEW: Building assignment dropdown
        buildingDropdown = new JComboBox<>();
        buildingDropdown.addActionListener(e -> assignColonistToBuilding());

        centerPanel.add(new JLabel("Select Colonist:"));
        centerPanel.add(colonistDropdown);
        centerPanel.add(new JLabel("Occupation:"));
        centerPanel.add(occupationLabel);
        centerPanel.add(new JLabel("Energy:"));
        centerPanel.add(energyLabel);
        centerPanel.add(new JLabel("Productivity:"));
        centerPanel.add(productivityLabel);
        centerPanel.add(new JLabel("Assigned Building:"));
        centerPanel.add(buildingDropdown);
        centerPanel.add(new JLabel("")); // placeholder
        centerPanel.add(feedButton);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel: End Turn and Build buttons
        JPanel bottomPanel = new JPanel();
        nextTurnBtn = new JButton("End Turn");
        nextTurnBtn.addActionListener(e -> nextTurn());
        buildBtn = new JButton("Build Structures");
        buildBtn.addActionListener(e -> new BuildWindow(this.game, this));

        bottomPanel.add(nextTurnBtn);
        bottomPanel.add(buildBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Initial UI update
        updateColonistStats();
        updateGameStats();

        frame.setVisible(true);
    }

    public void updateColonistStats() {
        updatingDropdown = true;
        Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
        if (selected != null) {
            occupationLabel.setText(selected.getOccupation());
            energyLabel.setText(String.valueOf(selected.getEnergy()));
            productivityLabel.setText(String.valueOf(selected.getProductivity()));

            // Update building dropdown
            buildingDropdown.removeAllItems();
            buildingDropdown.addItem("Unassigned");
            for (Building b : game.getColony().getBuildings()) {
                buildingDropdown.addItem(b.getName());
            }

            if (selected.getAssignedBuilding() != null) {
                buildingDropdown.setSelectedItem(selected.getAssignedBuilding().getName());
            } else {
                buildingDropdown.setSelectedItem("Unassigned");
            }
        }
        updatingDropdown = false;
    }

    private void assignColonistToBuilding() {
        if (updatingDropdown) return;
        Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
        if (selected == null) return;

        String chosen = (String) buildingDropdown.getSelectedItem();
        Building building = null;

        if (!"Unassigned".equals(chosen)) {
            for (Building b : game.getColony().getBuildings()) {
                if (b.getName().equals(chosen)) {
                    building = b;
                    break;
                }
            }
        }

        AssignAction action = new AssignAction(selected, building);
        if (!game.getColony().performAction(action)) {
            JOptionPane.showMessageDialog(null, "Invalid assignment!", "Error", JOptionPane.ERROR_MESSAGE);
            buildingDropdown.setSelectedItem("Unassigned");
        }

        updateColonistStats();
        updateGameStats();
    }

    public void updateGameStats() {
        turnLabel.setText("Turn: " + game.getTurn());
        resLabel.setText(game.getColony().getResources().toString());
    }

    private void nextTurn() {
        game.nextTurn();
        updateGameStats();
        updateColonistStats();

        if (game.getColony().getPopulation() == 0) {
            JOptionPane.showMessageDialog(null,
                    "All your colonists have perished! Game Over.",
                    "Game Over",
                    JOptionPane.WARNING_MESSAGE);
            nextTurnBtn.setEnabled(false);
            buildBtn.setEnabled(false);
            feedButton.setEnabled(false);
            colonistDropdown.setEnabled(false);
            buildingDropdown.setEnabled(false);
        }
    }

    public static void startGame(Game game) {
        SwingUtilities.invokeLater(() -> new GameWindow(game));
    }
}



