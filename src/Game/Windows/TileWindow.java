package Game.Windows;

import Game.Engine.Actions.ColonyActions.BuildAction;
import Game.Engine.Buildings.*;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Colonist.ColonistAvatar;
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
                    new TribeCentre(), new EngineeringHub(), new House(), new Park(), new Cafe(), new Storage()
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
        else{
            Building building = tile.getBuilding();
            JPanel buildstatPanel = new JPanel();
            buildstatPanel.setLayout(new BoxLayout(buildstatPanel, BoxLayout.Y_AXIS));
            buildstatPanel.setBorder(BorderFactory.createTitledBorder("Building"));

            JLabel nameLabel = new JLabel(building.getName());
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buildstatPanel.add(nameLabel);
            buildstatPanel.add(Box.createVerticalStrut(8));

            JLabel invHeader = new JLabel("Inventory:");
            invHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            buildstatPanel.add(invHeader);




            if (building.getInv().isEmpty()) {
                JLabel emptyLabel = new JLabel("(empty)");
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                buildstatPanel.add(emptyLabel);
            } else {
                for (var stack : building.getInv().getStacks()) {
                    JLabel stackLabel = new JLabel(stack.toString());
                    stackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    buildstatPanel.add(stackLabel);
                }
            }

            JLabel weightLabel = new JLabel(String.format("Weight: %.1f / %.1f",
                    building.getInv().getCurrentWeight(),
                    building.getInv().getMaxWeight()));
            weightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buildstatPanel.add(Box.createVerticalStrut(8));
            buildstatPanel.add(weightLabel);

            frame.add(buildstatPanel, BorderLayout.CENTER);
        }
        JPanel colonistpanel = new JPanel();
        if (tile.getColonists().isEmpty()) {
            JLabel emptyLabel = new JLabel("(no colonist)");
            colonistpanel.add(emptyLabel);
        } else {
            for (ColonistAvatar colonist : tile.getColonists()) {
                Colonist c = colonist.getColonist();
                JLabel stackLabel = new JLabel(c.toString()+colonist.getActionManager().getDestination());
                colonistpanel.add(stackLabel);
            }
        }
        frame.add(colonistpanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
