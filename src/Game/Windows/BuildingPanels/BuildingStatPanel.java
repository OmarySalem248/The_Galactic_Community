package Game.Windows.BuildingPanels;

import Game.Engine.Buildings.Building;
import javax.swing.*;
import java.awt.*;

/**
 * Abstract base for all building stat panels shown in TileWindow.
 * Subclasses add their own building-specific sections below the common header.
 */
public abstract class BuildingStatPanel extends JPanel {

    protected final Building building;

    public BuildingStatPanel(Building building) {
        this.building = building;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buildCommonHeader();
        buildContent();
    }

    private void buildCommonHeader() {
        JLabel nameLabel = new JLabel(building.getName());
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel("Type: " + building.getBType().name());
        typeLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        typeLabel.setAlignmentX(LEFT_ALIGNMENT);

        add(nameLabel);
        add(Box.createVerticalStrut(4));
        add(typeLabel);
        add(Box.createVerticalStrut(8));
        add(makeSeparator());
        add(Box.createVerticalStrut(8));
    }

    protected JSeparator makeSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    protected JLabel makeHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    protected JLabel makeRow(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Subclasses implement this to add their own building-specific content
     * below the common header.
     */
    protected abstract void buildContent();

    /**
     * Called by TileWindow to refresh displayed data — subclasses override
     * if they need live updates (e.g. farm incubator status, project progress).
     */
    public void refresh() {}
}
