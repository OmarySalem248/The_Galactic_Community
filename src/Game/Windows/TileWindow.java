package Game.Windows;

import Game.Engine.Buildings.*;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Game;
import Game.Engine.Map.Tile;
import Game.Modes.BuildMode;
import Game.Windows.BuildingPanels.BuildingStatPanel;
import Game.Windows.BuildingPanels.DefaultBuildingStatPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TileWindow {

    public TileWindow(Game game, Tile tile, GameWindow parentWindow) {
        JFrame frame = new JFrame("Tile [" + tile.col + ", " + tile.row + "]");
        frame.setSize(400, 450);
        frame.setLayout(new BorderLayout(8, 8));

        // Escape cancels build mode
        frame.getRootPane().registerKeyboardAction(
                e -> {
                    game.getBuildMode().exit();
                    parentWindow.updateBuildModeIndicator();
                    frame.dispose();
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        JLabel coordLabel = new JLabel("Location: " + tile.col + ", " + tile.row);
        coordLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        infoPanel.add(coordLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        frame.add(infoPanel, BorderLayout.NORTH);

        BuildMode buildMode = game.getBuildMode();

        if (buildMode.isActive() && !tile.hasBuilding()) {
            // --- Build mode: show building picker ---
            frame.add(buildBuildingPicker(game, tile, frame, parentWindow), BorderLayout.CENTER);

        } else if (tile.hasBuilding()) {
            Building building = tile.getBuilding();

            // Clicking an EngineeringHub enters build mode
            if (building instanceof EngineeringHub) {
                game.getBuildMode().enter((EngineeringHub) building);
                parentWindow.updateBuildModeIndicator();
            }

            // Show WIP label if tile has an active build project
            if (tile.getBuildProject() != null && !tile.getBuildProject().isCompleted()) {
                JLabel wipLabel = new JLabel("🔨 WIP: " + tile.getBuildProject().getName());
                wipLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
                wipLabel.setForeground(new Color(0xFFA500));
                wipLabel.setHorizontalAlignment(SwingConstants.CENTER);
                infoPanel.add(wipLabel);
            }

            // Show building stat panel
            BuildingStatPanel statPanel = resolveStatPanel(building);
            frame.add(new JScrollPane(statPanel), BorderLayout.CENTER);

        } else {
            // Empty tile, not in build mode
            JLabel emptyLabel = new JLabel("Empty tile", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            frame.add(emptyLabel, BorderLayout.CENTER);
        }

        frame.setVisible(true);
    }

    // -------------------------------------------------------------------------
    // Building picker panel shown in build mode
    // -------------------------------------------------------------------------
    private JPanel buildBuildingPicker(Game game, Tile tile, JFrame frame, GameWindow parentWindow) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Select Building"));

        JLabel hubLabel = new JLabel("Hub: " + game.getBuildMode().getActiveHub().getName(),
                SwingConstants.CENTER);
        hubLabel.setFont(new Font("Monospaced", Font.ITALIC, 11));
        panel.add(hubLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // Add a button for each buildable building type
        for (Building prototype : getBuildableBuildings()) {
            float totalCost = prototype.getNeededResourcesWeight();
            String label = prototype.getType()
                    + "  (Resources: " + totalCost + ")";
            JButton btn = new JButton(label);
            btn.setFont(new Font("Monospaced", Font.PLAIN, 11));

            btn.addActionListener(e -> {
                // Always create the project — let the hub/colony handle affordability messaging
                BuildingProject project = new BuildingProject(prototype.getType(), prototype, tile);
                game.getBuildMode().getActiveHub().addProject(project);

                JOptionPane.showMessageDialog(frame,
                        prototype.getType() + " project created and assigned to "
                                + game.getBuildMode().getActiveHub().getName() + ".\n"
                                + "Builders will begin collecting resources.",
                        "Project Created",
                        JOptionPane.INFORMATION_MESSAGE);
                game.getBuildMode().exit();
                parentWindow.updateBuildModeIndicator();
                frame.dispose();

                parentWindow.repaintMap();
                frame.dispose();
            });

            btnPanel.add(btn);
        }

        JScrollPane scroll = new JScrollPane(btnPanel);
        panel.add(scroll, BorderLayout.CENTER);

        JButton cancelBtn = new JButton("Cancel Build Mode [Esc]");
        cancelBtn.addActionListener(e -> {
            game.getBuildMode().exit();
            parentWindow.updateBuildModeIndicator();
            frame.dispose();
        });
        panel.add(cancelBtn, BorderLayout.SOUTH);

        return panel;
    }

    // -------------------------------------------------------------------------
    // Resolve which stat panel to show for a given building
    // -------------------------------------------------------------------------
    private BuildingStatPanel resolveStatPanel(Building building) {
        // Add specific panel types here as you build them
        // e.g. if (building instanceof Farm f) return new FarmStatPanel(f);
        return new DefaultBuildingStatPanel(building);
    }

    // -------------------------------------------------------------------------
    // List of buildings available to build — add new types here
    // -------------------------------------------------------------------------
    private Building[] getBuildableBuildings() {
        return new Building[]{
                new Farm(),
                new LumberMill(),
                new Mine(),
                new House(),
                new EngineeringHub(),
                new Storage()
        };
    }
}