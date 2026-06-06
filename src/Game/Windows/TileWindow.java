package Game.Windows;

import Game.Engine.Actions.ColonyActions.BuildAction;
import Game.Engine.Buildings.*;
import Game.Engine.Game;
import Game.Engine.Map.Tile;

import javax.swing.*;
import java.awt.*;

public class TileWindow {

    public TileWindow(Game game, Tile tile, GameWindow parentWindow) {
        JFrame frame = new JFrame("Tile [" + tile.col + ", " + tile.row + "]");
        frame.setSize(350, 350);
        frame.setLayout(new BorderLayout(10, 10));

        // ----- Tile info -----
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Tile Info"));

        JLabel coordLabel = new JLabel("Location: " + tile.col + ", " + tile.row);
        JLabel buildingLabel = new JLabel("Building: " + (tile.hasBuilding() ? tile.building.getName() : "None"));
        coordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buildingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(coordLabel);
        infoPanel.add(buildingLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        frame.add(infoPanel, BorderLayout.NORTH);

        // ----- Build options (only if tile is empty) -----
        if (!tile.hasBuilding()) {
            JPanel buildPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            buildPanel.setBorder(BorderFactory.createTitledBorder("Build"));

            JLabel resLabel = new JLabel(game.getColony().getResources().toString(), SwingConstants.CENTER);
            buildPanel.add(resLabel);

            for (Building building : new Building[]{
                    new Farm(), new LumberMill(), new Mine(),
                    new TribeCentre(), new EngineeringHub(), new House()
            }) {
                JButton btn = new JButton(building.getType()
                        + "  (Wood: " + building.getWoodCost()
                        + "  Stone: " + building.getStoneCost() + ")");
                btn.addActionListener(e -> {
                    BuildAction action = new BuildAction(building, tile);
                    if (game.getColony().performAction(action)) {
                        JOptionPane.showMessageDialog(frame, building.getName() + " built successfully!");
                        resLabel.setText(game.getColony().getResources().toString());
                        buildingLabel.setText("Building: " + tile.building.getName());
                        parentWindow.updateGameStats();
                        parentWindow.getColonistWindow().updateColonistStats();
                        parentWindow.repaintMap();
                        // Disable all build buttons once tile is occupied
                        for (Component c : buildPanel.getComponents())
                            if (c instanceof JButton) c.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Not enough resources to build " + building.getName(),
                                "Insufficient Resources", JOptionPane.WARNING_MESSAGE);
                    }
                });
                buildPanel.add(btn);
            }
            frame.add(buildPanel, BorderLayout.CENTER);
        }

        frame.setVisible(true);
    }
}
