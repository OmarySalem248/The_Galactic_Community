package Game.Windows;

import Game.Engine.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {

    private final Game game;
    private final JLabel turnLabel;
    private final JLabel resLabel;
    private final JLabel gameUpdate;
    private final ColonistsWindow colonistWindow;
    private final JButton nextTurnBtn;
    private final JButton buildBtn;
    private final JButton feedButton;
    private final JButton reduceFeedButton;
    private final JComboBox colonistDropdown;
    private final JComboBox buildingDropdown;
    private boolean autoRunning = false;
    private Timer autoTurnTimer;
    private final JButton autoRunButton;

    public GameWindow(Game game) {
        this.game = game;

        JFrame frame = new JFrame("Colony Builder - Micromanagement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 550);
        frame.setLayout(new BorderLayout(10, 10));

        // ----- Top Panel -----
        JPanel infoPanel = new JPanel(new BorderLayout());

        JPanel statsRow = new JPanel();
        statsRow.setLayout(new BoxLayout(statsRow, BoxLayout.Y_AXIS));
        turnLabel = new JLabel("Turn: " + game.getTurn(), SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resLabel = new JLabel(game.getColony().getResources().toString(), SwingConstants.CENTER);
        resLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameUpdate = new JLabel(game.getStatus(), SwingConstants.CENTER);
        gameUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsRow.add(Box.createVerticalStrut(5));
        statsRow.add(turnLabel);
        statsRow.add(resLabel);
        statsRow.add(gameUpdate);
        statsRow.add(Box.createVerticalStrut(5));

        // View dropdown (top-right)
        JComboBox<String> viewDropdown = new JComboBox<>(new String[]{"Map", "Relationships"});
        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        viewRow.add(new JLabel("View:"));
        viewRow.add(viewDropdown);

        infoPanel.add(statsRow,  BorderLayout.CENTER);
        infoPanel.add(viewRow,   BorderLayout.EAST);
        frame.add(infoPanel, BorderLayout.NORTH);

        // ----- Center Panel -----
        RelationshipPanel relPanel = new RelationshipPanel(game);
        colonistWindow = new ColonistsWindow(game, relPanel);

        // Card panel — add new views here as your game grows
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(new MapPanel(game.getMap()), "Map");
        cardPanel.add(relPanel,                    "Relationships");

        viewDropdown.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, (String) viewDropdown.getSelectedItem());
        });

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(colonistWindow, BorderLayout.WEST);
        centerPanel.add(cardPanel,      BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // ----- Bottom Panel -----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        nextTurnBtn   = new JButton("End Turn");
        buildBtn      = new JButton("Build Structures");
        autoRunButton = new JButton("Auto Run");
        ColonistsWindow.ColonistControls controls = colonistWindow.getControls();
        feedButton       = controls.feedButton();
        reduceFeedButton = controls.reduceFeedButton();
        colonistDropdown = controls.colonistDropdown();
        buildingDropdown = controls.buildingDropdown();

        nextTurnBtn.addActionListener(e -> nextTurn());
        buildBtn.addActionListener(e -> new BuildWindow(this.game, this));
        autoRunButton.addActionListener(e -> toggleAutoRun());

        bottomPanel.add(nextTurnBtn);
        bottomPanel.add(buildBtn);
        bottomPanel.add(autoRunButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        updateGameStats();
        frame.setVisible(true);
    }

    private void toggleAutoRun() {
        if (autoRunning) {
            autoTurnTimer.stop();
            autoRunning = false;
            autoRunButton.setText("Auto Run");
        } else {
            autoRunning = true;
            autoRunButton.setText("Stop Auto Run");
            autoTurnTimer = new Timer(500, e -> {
                game.nextTurn();
                updateGameStats();
                if (game.getColony().getPopulation() == 0) {
                    autoTurnTimer.stop();
                    autoRunning = false;
                    autoRunButton.setText("Auto Run");
                    JOptionPane.showMessageDialog(null, "All colonists have perished. Auto-run stopped.", "Game Over", JOptionPane.WARNING_MESSAGE);
                }
            });
            autoTurnTimer.start();
        }
    }

    public void updateGameStats() {
        turnLabel.setText("Turn: " + game.getTurn());
        resLabel.setText(game.getColony().getResources().toString());
        gameUpdate.setText(game.getStatus());
        colonistWindow.updateColonistDropdown();
        colonistWindow.updateColonistStats();
    }

    private void nextTurn() {
        game.nextTurn();
        updateGameStats();
        if (game.getColony().getPopulation() == 0) {
            JOptionPane.showMessageDialog(null, "All your colonists have perished! Game Over.", "Game Over", JOptionPane.WARNING_MESSAGE);
            nextTurnBtn.setEnabled(false);
            buildBtn.setEnabled(false);
            feedButton.setEnabled(false);
            reduceFeedButton.setEnabled(false);
            colonistDropdown.setEnabled(false);
            buildingDropdown.setEnabled(false);
        }
    }
    public ColonistsWindow getColonistWindow(){
        return colonistWindow;
    }

    public static void startGame(Game game) { SwingUtilities.invokeLater(() -> new GameWindow(game)); }
}

