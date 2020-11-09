package ui.gui;

import model.Move;
import model.board.Board;
import model.board.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static model.board.Board.*;
import static model.board.Board.R;

// implementation of a graphic interface of the opening app
public class GraphicOpeningApp {
    private static final String BOARD_PATH = "./images/chessboard.png";
    private static String[] PIECE_PATH = {"bk", "bq", "br", "bb", "bn", "bp", "", "wp", "wn", "wb", "wr", "wq", "wk"};
    private Position currSelect;
    private Move root;
    private Move currentMove;
    private JPanel boardPanel;
    private JPanel movePanel;
    private JLabel boardLabel;
    private MoveTree tree;
    private boolean whiteSide = true;

    // EFFECTS: creates new graphic application
    public GraphicOpeningApp() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Opening List");
            init(frame);

            frame.add(boardPanel, BorderLayout.EAST);
            frame.add(movePanel, BorderLayout.CENTER);

            finishFrame(frame);
        });
    }

    // MODIFIES: this and frame
    // EFFECTS: calls all initializing methods
    private void init(JFrame frame) {
        initMove();
        initFrame(frame);
        initBoard();
        initMovePanel();
        refreshBoard();
    }

    // MODIFIES: frame
    // EFFECTS: initializes the frame of the app
    private void initFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 808));
        frame.setLayout(new BorderLayout());
    }

    // MODIFIES: frame
    // EFFECTS: makes the frame visible and adds finishing settings
    private void finishFrame(JFrame frame) {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the board panel
    private void initBoard() {
        boardPanel = new JPanel();
        boardLabel = new JLabel();
        boardPanel.add(boardLabel);
        addListener();
    }

    private void addListener() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = (e.getY() - 40) / 87;
                int col = (e.getX() - 40) / 87;
                if (Position.isValid(row, col) || row != currSelect.getRow() && col != currSelect.getCol()) {
                    checkMove(row, col);
                    refreshBoard();
                }
            }
        });
    }

    private void checkMove(int row, int col) {
        if (currSelect.isValid()) {
            int res = makeMove(new Position(row, col));
            if (res == -1) {
                currSelect = new Position(row, col);
            } else {
                currSelect = new Position(-1, -1);
                tree.updateUI();
                tree.expandPath(new TreePath(currentMove.getPath()));
            }
        } else {
            currSelect = new Position(row, col);
        }
    }

    // MODIFIES: this
    // EFFECTS: tries to create new move from given coordinates
    private int makeMove(Position end) {
        Board currB = currentMove.getBoard();
        int newNum = currentMove.getMoveNum() + 1;
        if ((int) Math.signum(currB.get(currSelect)) == (newNum % 2 == 0 ? -1 : 1)) {
            int piece = currB.get(currSelect);
            Move m = new Move(newNum, piece, false, false, currSelect, end, currentMove, currB);
            int res = m.isLegal();
            Board b;
            if (res == -1) {
                return -1;
            } else {
                b = currB.move(currSelect, end, res, Q * (int) Math.signum(piece));
            }
            boolean isCap = currB.get(end) != 0;
            boolean isCheck = b.isInCheck(piece < 0);
            m = new Move(newNum, piece, isCap, isCheck, currSelect, end, currentMove, b);
            currentMove.addChildMove(m);
            currentMove = m;

            return res;
        }
        return -1;
    }

    // MODIFIES: this
    // EFFECTS: initializes move panel
    private void initMovePanel() {
        movePanel = new JPanel();
        movePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        scrollPane.setPreferredSize(new Dimension(360, 730));
        movePanel.add(scrollPane);
    }

    void clickOnMove(MouseEvent e) {
        TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
        if (tp != null) {
            currentMove = (Move) tp.getPath()[tp.getPathCount() - 1];
            refreshBoard();
        }
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
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    drawAtPosition(g, i, j);
                }
            }
            boardLabel.setIcon(new ImageIcon(com));
            g.dispose();
        } catch (IOException e) {
            System.out.println("Image not found, IOException caught");
            System.exit(0);
        }
    }

    // MODIFIES: g, this
    // EFFECTS: draws given piece and other details at position (i, j)
    private void drawAtPosition(Graphics2D g, int i, int j) throws IOException {
        if (currSelect.getRow() == i && currSelect.getCol() == j) {
            g.setColor(new Color(143, 188, 143, 150));
            g.fillRect(36 + j * 87, 36 + i * 87, 87, 87);
        } else if (currentMove.getStart().getRow() == i && currentMove.getStart().getCol() == j) {
            g.setColor(new Color(255, 255, 102, 150));
            g.fillRect(36 + j * 87, 36 + i * 87, 87, 87);
        } else if (currentMove.getEnd().getRow() == i && currentMove.getEnd().getCol() == j) {
            g.setColor(new Color(255, 255, 102, 150));
            g.fillRect(36 + j * 87, 36 + i * 87, 87, 87);
        }

        if (currentMove.getBoard().get(i, j) != Board.E) {
            int shiftRow = j == 0 ? 36 : 37;
            String path = "./images/" + PIECE_PATH[currentMove.getBoard().get(i, j) + 6] + ".png";
            BufferedImage piece = ImageIO.read(new File(path));
            g.drawImage(piece, shiftRow + 87 * j, 37 + 87 * i, null);
        }
    }
}
