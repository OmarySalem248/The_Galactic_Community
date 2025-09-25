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
    private JLabel gameUpdate;

    private JComboBox<Colonist> colonistDropdown;
    private JLabel energyLabel;
    private JLabel hpLabel;
    private JLabel ageLabel;
    private JLabel colonistUpdate;
    private JLabel occupationLabel;
    private JButton feedButton;
    private JButton reduceFeedButton;

    private JComboBox<String> buildingDropdown;
    private JButton nextTurnBtn;
    private JButton buildBtn;
    private boolean updatingDropdown = false;

    public GameWindow(Game game) {
        this.game = game;

        JFrame frame = new JFrame("Colony Builder - Micromanagement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLayout(new BorderLayout(10, 10));

        // ----- Top Panel -----
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        turnLabel = new JLabel("Turn: " + game.getTurn(), SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resLabel = new JLabel(game.getColony().getResources().toString(), SwingConstants.CENTER);
        resLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameUpdate = new JLabel(game.getStatus(), SwingConstants.CENTER);
        gameUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(turnLabel);
        infoPanel.add(resLabel);
        infoPanel.add(gameUpdate);
        infoPanel.add(Box.createVerticalStrut(5));

        frame.add(infoPanel, BorderLayout.NORTH);

        // ----- Center Panel -----
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        int row = 0;

        colonistDropdown = new JComboBox<>();
        for (Colonist c : game.getColony().getColonists()) {
            colonistDropdown.addItem(c);
        }
        colonistDropdown.addActionListener(e -> updateColonistStats());

        occupationLabel = new JLabel();
        energyLabel = new JLabel();
        hpLabel = new JLabel();
        ageLabel = new JLabel();
        colonistUpdate = new JLabel();

        Font statFont = new Font("Monospaced", Font.PLAIN, 14);
        occupationLabel.setFont(statFont);
        energyLabel.setFont(statFont);
        hpLabel.setFont(statFont);
        ageLabel.setFont(statFont);
        colonistUpdate.setFont(statFont);

        // Feed buttons
        feedButton = new JButton("Feed 1 Extra Food");
        reduceFeedButton = new JButton("Deallocate 1 Food");
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
        reduceFeedButton.addActionListener(e -> {
            Colonist selected = (Colonist) colonistDropdown.getSelectedItem();
            if (selected != null ) {
                if (selected.getEnergy() > 1) {
                    boolean success = game.getColony().feedColonist(selected, -1);
                    if (success) {
                        updateColonistStats();
                        updateGameStats();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Can't reduce feed any further for " + selected.getName(),
                            "MINIMUM FEED REACHED", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Building assignment dropdown
        buildingDropdown = new JComboBox<>();
        buildingDropdown.addActionListener(e -> assignColonistToBuilding());

        // ----- Adding components in two-column layout -----
        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("Select Colonist:"), gbc);
        gbc.gridx = 1; centerPanel.add(colonistDropdown, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; centerPanel.add(ageLabel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("Occupation:"), gbc);
        gbc.gridx = 1; centerPanel.add(occupationLabel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("Energy:"), gbc);
        gbc.gridx = 1; centerPanel.add(energyLabel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("HP:"), gbc);
        gbc.gridx = 1; centerPanel.add(hpLabel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(new JLabel("Assigned Building:"), gbc);
        gbc.gridx = 1; centerPanel.add(buildingDropdown, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        centerPanel.add(colonistUpdate, gbc);
        row++;

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row; centerPanel.add(feedButton, gbc);
        gbc.gridx = 1; centerPanel.add(reduceFeedButton, gbc);
        row++;

        frame.add(centerPanel, BorderLayout.CENTER);

        // ----- Bottom Panel -----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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
            hpLabel.setText(String.valueOf(selected.getHealth()));
            ageLabel.setText(selected.getAge()+" Years "+selected.getAgeMonths()+" Months");
            colonistUpdate.setText(selected.getStatus());

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
    public static void startGame(Game game) { SwingUtilities.invokeLater(() -> new GameWindow(game)); }
}




