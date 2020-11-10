package ui.gui;

import model.Move;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

// Implementation of TreeModel to fit Move.java
// Code adapted from GenealogyExample from Oracle java tutorials
public class MoveModel implements TreeModel {
    private List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private Move rootMove;

    // EFFECTS: create new model around Move root
    public MoveModel(Move root) {
        rootMove = root;
    }

    // MODIFIES: this
    // EFFECTS: adds listener to list of listeners
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    // EFFECTS: returns child in parent at given index
    public Object getChild(Object parent, int index) {
        Move m = (Move)parent;
        return m.getChildMove(index);
    }

    // EFFECTS: returns number of children in parent
    public int getChildCount(Object parent) {
        Move m = (Move)parent;
        return m.childCount();
    }

    // EFFECTS: returns the index of child in parent
    public int getIndexOfChild(Object parent, Object child) {
        Move m = (Move)parent;
        return m.getIndexOfChild((Move)child);
    }

    // EFFECTS: returns the root of the tree
    public Object getRoot() {
        return rootMove;
    }

    // EFFECTS: sets new root for the tree
    public void setRoot(Move move) {
        rootMove = move;
    }

    // EFFECTS: returns true if node is a leaf.
    public boolean isLeaf(Object node) {
        Move m = (Move)node;
        return m.childCount() == 0;
    }

    // MODIFIES: this
    // EFFECTS: removes a listener previously added
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    // EFFECTS: called when the user has altered the value for the item identified by path to newValue
    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}
