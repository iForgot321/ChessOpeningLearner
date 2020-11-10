package ui.gui;

import model.Move;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

// Code adapted from GenealogyExample from Oracle java tutorials
public class MoveTree extends JTree {

    // EFFECTS: creates and initializes new MoveTree with root move
    public MoveTree(Move parentNode) {
        super(new MoveModel(parentNode));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        setCellRenderer(renderer);
    }
}
