package Game.Windows;

import Game.Engine.Game;
import Game.Engine.Time.GameClock;

import javax.swing.*;
import java.awt.*;

public class GameWindow {

    private final Game game;

    private final JLabel resLabel;
    private final JLabel gameUpdate;
    private final ColonistsWindow colonistWindow;
    private final JButton nextTurnBtn;

    private boolean autoRunning = false;
    private Timer autoTurnTimer;
    private final JButton autoRunButton;
    private MapPanel mapPanel;

    public GameWindow(Game game) {
        this.game = game;
        game.getClock().addTickListener((time) -> SwingUtilities.invokeLater(this::updateGameStats));

        JFrame frame = new JFrame("Utopia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 550);
        frame.setLayout(new BorderLayout(10, 10));

        // ----- Top Panel -----
        JPanel infoPanel = new JPanel(new BorderLayout());
        JPanel statsRow = new JPanel();
        statsRow.setLayout(new BoxLayout(statsRow, BoxLayout.Y_AXIS));
        resLabel = new JLabel(game.getColony().getResources().toString(), SwingConstants.CENTER);
        resLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameUpdate = new JLabel(game.getStatus(), SwingConstants.CENTER);
        gameUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsRow.add(Box.createVerticalStrut(5));
        statsRow.add(resLabel);
        statsRow.add(gameUpdate);
        statsRow.add(Box.createVerticalStrut(5));

        JComboBox<String> viewDropdown = new JComboBox<>(new String[]{"Map", "Relationships"});
        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        viewRow.add(new JLabel("View:"));
        viewRow.add(viewDropdown);
        infoPanel.add(statsRow, BorderLayout.CENTER);
        infoPanel.add(viewRow,  BorderLayout.EAST);
        frame.add(infoPanel, BorderLayout.NORTH);

        // ----- Center Panel -----
        RelationshipPanel relPanel = new RelationshipPanel(game);
        game.getClock().addTickListener(relPanel);
        colonistWindow = new ColonistsWindow(game, relPanel);
        mapPanel = new MapPanel(game, this, game.getClock());

        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(mapPanel, "Map");
        cardPanel.add(relPanel, "Relationships");

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
        autoRunButton = new JButton("Auto Run");



        nextTurnBtn.addActionListener(e -> {
            game.getClock().tickOnce();
            updateGameStats();
        });
        autoRunButton.addActionListener(e -> toggleAutoRun());
        bottomPanel.add(nextTurnBtn);
        bottomPanel.add(autoRunButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        updateGameStats();
        frame.setVisible(true);
    }

    public ColonistsWindow getColonistWindow() { return colonistWindow; }

    public void repaintMap() { mapPanel.repaint(); }

    private void toggleAutoRun() {
        GameClock clock = game.getClock();
        if (clock.isRunning()) {
            clock.stop();
            autoRunButton.setText("Auto Run");
        } else {
            clock.start();
            autoRunButton.setText("Stop");
        }
    }

    public void updateGameStats() {

        resLabel.setText(game.getColony().getResources().toString());
        gameUpdate.setText(game.getStatus());
        colonistWindow.updateColonistDropdown();
        colonistWindow.updateColonistStats();
    }



    public static void startGame(Game game) { SwingUtilities.invokeLater(() -> new GameWindow(game)); }
}