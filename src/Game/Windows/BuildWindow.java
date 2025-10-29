package Game.Windows;

import Game.Engine.Actions.BuildAction;
import Game.Engine.Buildings.*;
import Game.Engine.Colony;
import Game.Engine.Game;

import javax.swing.*;
import java.awt.*;

public class BuildWindow {
    private Colony colony;
    private GameWindow parentWindow;
    private JLabel resLabel; // NEW

    public BuildWindow(Game game, GameWindow parentWindow) {
        this.colony = game.getColony();
        this.parentWindow = parentWindow;

        JFrame frame = new JFrame("Build Menu");
        frame.setSize(350, 250);
        frame.setLayout(new GridLayout(6, 1, 5, 5));

        // Resource label for this window
        resLabel = new JLabel(colony.getResources().toString(), SwingConstants.CENTER);
        frame.add(resLabel);

        // Example building buttons
        JButton farmBtn = new JButton("Build Farm");
        farmBtn.addActionListener(e -> build(new Farm()));
        frame.add(farmBtn);

        JButton lumberBtn = new JButton("Build Lumber Mill");
        lumberBtn.addActionListener(e -> build(new LumberMill()));
        frame.add(lumberBtn);

        JButton mineBtn = new JButton("Build Mine");
        mineBtn.addActionListener(e -> build(new Mine()));
        frame.add(mineBtn);

        JButton tcentreBtn = new JButton("Build Tribe Centre");
        tcentreBtn.addActionListener(e -> build(new TribeCentre()));
        frame.add(tcentreBtn);

        frame.setVisible(true);
    }


    private void build(Building building) {
        BuildAction action = new BuildAction(building);
        if (colony.performAction(action)) {
            JOptionPane.showMessageDialog(null, building.getName() + " built successfully!");

            resLabel.setText(colony.getResources().toString());
            parentWindow.updateGameStats();          // parent window
            parentWindow.updateColonistStats();      // refresh dropdowns
        } else {
            JOptionPane.showMessageDialog(null, "Not enough resources to build " + building.getName(),
                    "Insufficient Resources", JOptionPane.WARNING_MESSAGE);
        }
    }
}





