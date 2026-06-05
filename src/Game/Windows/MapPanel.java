package Game.Windows;

import Game.Engine.Map.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MapPanel extends JPanel {

    private static final int CELL    = 48;
    private static final int PADDING = 10;

    private final Map map;
    private Tile selected = null;

    private int offsetX = 0, offsetY = 0;
    private Point dragStart;
    private int dragOriginX, dragOriginY;

    public MapPanel(Map map) {
        this.map = map;
        setBackground(new Color(0x1C1C2E));
        setBorder(BorderFactory.createTitledBorder("Map"));

        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragStart   = e.getPoint();
                dragOriginX = offsetX;
                dragOriginY = offsetY;
            }
            @Override public void mouseDragged(MouseEvent e) {
                offsetX = dragOriginX + (e.getX() - dragStart.x);
                offsetY = dragOriginY + (e.getY() - dragStart.y);
                repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (dragStart == null) return;
                boolean wasDrag = Math.abs(e.getX() - dragStart.x) > 4
                        || Math.abs(e.getY() - dragStart.y) > 4;
                if (!wasDrag) {
                    int col = (e.getX() - PADDING - offsetX) / CELL;
                    int row = (e.getY() - PADDING - offsetY) / CELL;
                    selected = map.getTile(col, row);
                    repaint();
                }
                dragStart = null;
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (int row = 0; row < map.rows; row++) {
            for (int col = 0; col < map.cols; col++) {
                Tile tile = map.getTile(col, row);
                if (tile == null) continue;
                int x = PADDING + offsetX + col * CELL;
                int y = PADDING + offsetY + row * CELL;

                // Cell background
                g2.setColor(new Color(0x2A2A45));
                g2.fillRect(x, y, CELL, CELL);

                // Building
                if (tile.hasBuilding()) {
                    Image img;
                    img = tile.building.getImage();
                    if (img != null) {
                        g2.drawImage(img, x + 4, y + 4, CELL - 8, CELL - 8, null);
                    } else {
                        // Fallback: coloured rectangle + name
                        g2.setColor(new Color(0x7B3F00));
                        g2.fillRoundRect(x + 5, y + 5, CELL - 10, CELL - 10, 6, 6);
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Monospaced", Font.BOLD, 9));
                        FontMetrics fm = g2.getFontMetrics();
                        String name = tile.building.getName();
                        while (name.length() > 1 && fm.stringWidth(name) > CELL - 6)
                            name = name.substring(0, name.length() - 1);
                        g2.drawString(name, x + (CELL - fm.stringWidth(name)) / 2, y + CELL / 2 + fm.getAscent() / 2 - 2);
                    }
                }

                // Grid lines
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawRect(x, y, CELL, CELL);

                // Selection
                if (tile == selected) {
                    g2.setColor(new Color(255, 220, 50, 200));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRect(x + 1, y + 1, CELL - 2, CELL - 2);
                    g2.setStroke(new BasicStroke(1f));
                }
            }
        }
    }
}