package ui.gui;

import model.Move;
import model.board.Board;
import model.board.Position;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static model.board.Board.*;

// Implementation of a graphic interface of the opening app
public class GraphicOpeningApp extends JFrame implements ActionListener, WindowListener {
    private static final String OPENING_FILE = "./data/openingList.json";
    private static final String BOARD_PATH = "./images/chessboard.png";
    private static String[] PIECE_PATH = {"bk", "bq", "br", "bb", "bn", "bp", "", "wp", "wn", "wb", "wr", "wq", "wk"};
    ImageIcon icon = new ImageIcon("./images/check.png");
    private Position currSelect;
    private Move root;
    private Move currentMove;
    private JPanel boardPanel;
    private JPanel movePanel;
    private JLabel boardLabel;
    private MoveTree tree;
    private boolean whiteSide = true;
    private boolean requiresSave = false;
    private JsonReader reader;
    private JsonWriter writer;

    // EFFECTS: creates new graphic application
    public GraphicOpeningApp() {
        super("Chess Opening List");
        init();

        add(boardPanel, BorderLayout.EAST);
        add(movePanel, BorderLayout.CENTER);

        finishFrame();
    }

    // MODIFIES: this and frame
    // EFFECTS: calls all initializing methods
    private void init() {
        initMove();
        initFrame();
        initBoard();
        initMovePanel();
        refreshBoard();
        reader = new JsonReader(OPENING_FILE);
        writer = new JsonWriter(OPENING_FILE);
    }

    // MODIFIES: frame
    // EFFECTS: initializes the frame of the app
    private void initFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1300, 808));
        setLayout(new BorderLayout());
        addWindowListener(this);
    }

    // MODIFIES: frame
    // EFFECTS: makes the frame visible and adds finishing settings
    private void finishFrame() {
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the board panel
    private void initBoard() {
        boardPanel = new JPanel();
        boardLabel = new JLabel();
        boardPanel.add(boardLabel);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds listener to boardPanel
    private void addListener() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = (e.getY() - 40) / 87;
                int col = (e.getX() - 40) / 87;
                if (!whiteSide) {
                    row = 7 - row;
                    col = 7 - col;
                }
                if (Position.isValid(row, col) || row != currSelect.getRow() && col != currSelect.getCol()) {
                    checkMove(row, col);
                    refreshBoard();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: calls makeMove if move is possible
    private void checkMove(int row, int col) {
        if (currSelect.isValid()) {
            int res = makeMove(new Position(row, col));
            if (res == -1) {
                currSelect = new Position(row, col);
            } else {
                currSelect = new Position(-1, -1);
                if (res != -3) {
                    requiresSave = true;
                }
                tree.updateUI();
                openMovePath();
            }
        } else {
            currSelect = new Position(row, col);
        }
    }

    // MODIFIES: this
    // EFFECTS:  open all nodes that lead up to current move
    private void openMovePath() {
        if (currentMove.equals(root)) {
            tree.setSelectionPath(new TreePath(root));
        } else {
            TreePath tp = new TreePath(currentMove.getPath());
            tree.setSelectionPath(tp);
            tree.expandPath(tp.getParentPath());
        }
    }

    // MODIFIES: this
    // EFFECTS: tries to create new move from given coordinates
    private int makeMove(Position end) {
        Board currB = currentMove.getBoard();
        int newNum = currentMove.getMoveNum() + 1;
        if ((int) Math.signum(currB.get(currSelect)) == (newNum % 2 == 0 ? -1 : 1)) {
            int index = currentMove.getIndexOfChild(currSelect, end);
            if (index != -1) {
                currentMove = currentMove.getChildMove(index);
                return -3;
            }
            Move m = new Move(newNum, currB.get(currSelect), false, false, currSelect, end, currentMove, currB);
            int res = m.isLegal();
            Board b;
            if (res == -1) {
                return -1;
            } else {
                b = currB.move(currSelect, end, res, Q * (int) Math.signum(currB.get(currSelect)));
            }

            boolean isCheck = b.isInCheck(currB.get(currSelect) < 0);
            m = new Move(newNum, currB.get(currSelect), currB.get(end) != 0, isCheck, currSelect, end, currentMove, b);
            currentMove.addChildMove(m);
            currentMove = m;

            return res;
        }
        return -1;
    }

    // MODIFIES: this
    // EFFECTS: initializes move panel
    private void initMovePanel() {
        movePanel = new JPanel(new BorderLayout());
        movePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel();
        createButtons(buttonPanel);

        tree = new MoveTree(root);
        tree.setScrollsOnExpand(true);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickOnMove(e);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);

        movePanel.add(buttonPanel, BorderLayout.PAGE_END);
        movePanel.add(scrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: p
    // EFFECTS: creates and initializes ActionListeners for all buttons, then adds to panel
    private void createButtons(JPanel p) {
        JButton firstMove = new JButton("<<<");
        JButton prevMove = new JButton("<");
        JButton nextMove = new JButton(">");
        JButton lastMove = new JButton(">>>");
        JButton save = new JButton("Save");
        JButton delete = new JButton("Delete");
        JButton flip = new JButton("Flip");

        firstMove.addActionListener(this);
        prevMove.addActionListener(this);
        nextMove.addActionListener(this);
        lastMove.addActionListener(this);
        save.addActionListener(this);
        delete.addActionListener(this);
        flip.addActionListener(this);

        p.add(firstMove);
        p.add(prevMove);
        p.add(nextMove);
        p.add(lastMove);
        p.add(Box.createRigidArea(new Dimension(24, 0)));
        p.add(flip);
        p.add(delete);
        p.add(save);
    }

    // MODIFIES: this
    // EFFECTS: if mouse cursor is on a move node, set currentMove to that node
    private void clickOnMove(MouseEvent e) {
        TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
        if (tp != null) {
            currentMove = (Move) tp.getPath()[tp.getPathCount() - 1];
            refreshBoard();
        }
    }

    // EFFECTS: create dialog box that asks whether or not user wants to load moves
    private void askIfLoad() {
        if (JOptionPane.showConfirmDialog(this, "Do you want to load moves?", "Load Moves",
                JOptionPane.YES_NO_OPTION) == 0) {
            loadMoves();
        }
    }

    // EFFECTS: create dialog box that asks whether or not user wants to save moves
    private boolean askIfSave() {
        Object[] options = {"Save", "Don't save", "Cancel"};
        int n = JOptionPane.showOptionDialog(this, "Do you want to save changes made?", "Save Moves",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if (n == 0) {
            saveMoves();
            return true;
        }
        return n == 1;
    }

    // MODIFIES: this
    // EFFECTS: initializes beginning move
    private void initMove() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, P, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        boolean[] moved = new boolean[6];
        Board b = new Board(moved, board);

        root = new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b);
        currentMove = root;

        currSelect = new Position(-1, -1);
    }

    // MODIFIES: this
    // EFFECTS: redraw the board for current move
    private void refreshBoard() {
        try {
            BufferedImage board = ImageIO.read(new File(BOARD_PATH));
            BufferedImage com = new BufferedImage(board.getWidth(), board.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = (Graphics2D) com.getGraphics();
            g.drawImage(board, 0, 0, null);
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.BOLD, 18));
            g.drawString(currentMove.lineToString(), 30, 20);
            for (int i = 0, i2 = 7; i < 8; i++, i2--) {
                for (int j = 0, j2 = 7; j < 8; j++, j2--) {
                    drawAtPosition(g,whiteSide ? i : i2, whiteSide ? j : j2, i, j);
                }
            }
            boardLabel.setIcon(new ImageIcon(com));
            g.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Image not found, IOException caught", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // MODIFIES: g, this
    // EFFECTS: draws given piece and other details at position (i, j)
    private void drawAtPosition(Graphics2D g, int i, int j, int ti, int tj) throws IOException {
        if (currSelect.getRow() == ti && currSelect.getCol() == tj) {
            g.setColor(new Color(143, 188, 143, 150));
            g.fillRect(36 + j * 87, 36 + i * 87, 87, 87);
        } else if (currentMove.getStart().getRow() == ti && currentMove.getStart().getCol() == tj
                    || currentMove.getEnd().getRow() == ti && currentMove.getEnd().getCol() == tj) {
            g.setColor(new Color(255, 255, 102, 150));
            g.fillRect(36 + j * 87, 36 + i * 87, 87, 87);
        }

        if (currentMove.getBoard().get(ti, tj) != E) {
            int shiftRow = j == 0 ? 36 : 37;
            String path = "./images/" + PIECE_PATH[currentMove.getBoard().get(ti, tj) + 6] + ".png";
            BufferedImage piece = ImageIO.read(new File(path));
            g.drawImage(piece, shiftRow + 87 * j, 37 + 87 * i, null);
        }
    }

    // EFFECTS: directs ActionEvent to corresponding function
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if ("<<<".equals(actionCommand)) {
            setFirstMove();
        } else if ("<".equals(actionCommand)) {
            setPrevMove();
        } else if (">".equals(actionCommand)) {
            setNextMove();
        } else if (">>>".equals(actionCommand)) {
            setLastMove();
        } else if ("Delete".equals(actionCommand)) {
            deleteMove();
        } else if ("Flip".equals(actionCommand)) {
            flipBoard();
        } else if ("Save".equals(actionCommand)) {
            saveMoves();
        }
        refreshBoard();
    }

    // MODIFIES: this
    // EFFECTS: set current move to root
    private void setFirstMove() {
        if (!currentMove.equals(root)) {
            currentMove = root;
            tree.setSelectionPath(new TreePath(root));
        }
    }

    // MODIFIES: this
    // EFFECTS: set current move to parent move
    private void setPrevMove() {
        if (!currentMove.equals(root)) {
            currentMove = currentMove.getParentMove();
            openMovePath();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // MODIFIES: this
    // EFFECTS: set current move to first child move
    private void setNextMove() {
        if (currentMove.childCount() != 0) {
            currentMove = currentMove.getChildMove(0);
            openMovePath();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // MODIFIES: this
    // EFFECTS: recursively sets move to first child until leaf node is reached
    private void setLastMove() {
        while (currentMove.childCount() != 0) {
            currentMove = currentMove.getChildMove(0);
        }
        openMovePath();
    }

    // MODIFIES: this
    // EFFECTS: deletes current move and its children from tree
    private void deleteMove() {
        if (!currentMove.equals(root)) {
            Move removedMove = currentMove;
            currentMove = currentMove.getParentMove();
            currentMove.removeChildMove(removedMove);
            requiresSave = true;
            tree.updateUI();
            openMovePath();
        }
    }

    // MODIFIES: this
    // EFFECTS: toggles boolean whiteSide
    private void flipBoard() {
        whiteSide = !whiteSide;
    }

    // MODIFIES: this
    // EFFECTS: saves current move tree to OPENING_FILE
    private void saveMoves() {
        try {
            writer.open();
            writer.write(root);
            writer.close();
            requiresSave = false;
            JOptionPane.showMessageDialog(this, "Successfully saved moves.", "Success",
                    JOptionPane.PLAIN_MESSAGE, icon);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error saving moves. Check " + OPENING_FILE + " for issues", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads move tree from OPENING_FILE
    private void loadMoves() {
        try {
            root = reader.read();
            currentMove = root;
            ((MoveModel)tree.getModel()).setRoot(root);
            tree.updateUI();
            expandAll(root);
            refreshBoard();
            JOptionPane.showMessageDialog(this, "Successfully loaded moves.", "Success",
                    JOptionPane.PLAIN_MESSAGE, icon);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading moves. Check " + OPENING_FILE + " for issues", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: recursively expands all nodes in tree
    private void expandAll(Move m) {
        if (m.childCount() >= 0) {
            for (int i = 0; i < m.childCount(); i++) {
                expandAll(m.getChildMove(i));
            }
        }
        if (!m.equals(root)) {
            tree.expandPath(new TreePath(m.getPath()));
        }
    }

    // MODIFIES: this
    // EFFECTS: asks user to save if application is about to close and user hasn't saved changes yet
    public void windowClosing(WindowEvent e) {
        if (requiresSave) {
            if (askIfSave()) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    // EFFECTS: nothing
    public void windowClosed(WindowEvent e) {
    }

    // MODIFIES: this
    // EFFECTS: asks user to load move tree when application opens
    public void windowOpened(WindowEvent e) {
        askIfLoad();
    }

    // EFFECTS: nothing
    public void windowIconified(WindowEvent e) {
    }

    // EFFECTS: nothing
    public void windowDeiconified(WindowEvent e) {
    }

    // EFFECTS: nothing
    public void windowActivated(WindowEvent e) {
    }

    // EFFECTS: nothing
    public void windowDeactivated(WindowEvent e) {
    }
}
