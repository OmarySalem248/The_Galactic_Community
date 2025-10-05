package Game.Windows;

import Game.Colonist.Colonist;
import Game.*;
import Game.Relationships.Relationship;
import Game.Relationships.RelationshipType;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class RelationshipPanel extends JPanel {
    private JComboBox<Colonist> colonistDropdown;
    private JTable relationshipTable;
    private DefaultTableModel tableModel;
    private Game game;

    public RelationshipPanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout(5,8));

        String[] columns = {"Colonist", "Platonic", "Familial", "Romantic", "Sexual","Admiration","Proximity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        relationshipTable = new JTable(tableModel);
        relationshipTable.setFillsViewportHeight(true);
        add(new JScrollPane(relationshipTable), BorderLayout.CENTER);


    }

    public void updateTable(Colonist selectedcol) {
        tableModel.setRowCount(0);
        Colonist selected = selectedcol;
        if (selected == null) return;

        for (String otherName : selected.getRelationships().getKeys()) {
            Relationship rel = selected.getRelationships().get(otherName);
            Object[] row = {
                    otherName + "("+rel.getType()+")",
                    rel.getValue(RelationshipType.PLATONIC),
                    rel.getValue(RelationshipType.FAMILIAL),
                    rel.getValue(RelationshipType.ROMANTIC),
                    rel.getValue(RelationshipType.SEXUAL),
                    rel.getValue(RelationshipType.ADMIRATION),
                    rel.getValue(RelationshipType.PROXIMITY)
            };
            tableModel.addRow(row);
        }
    }
}
