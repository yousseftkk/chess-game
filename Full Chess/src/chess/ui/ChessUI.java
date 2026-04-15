package chess.ui;

import chess.game.Game;
import chess.exceptions.InvalidMoveException;
import chess.pieces.Color;
import chess.pieces.Piece;
import chess.pieces.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessUI {

    private final Game game;
    private final JButton[][] buttons = new JButton[8][8];
    private final JPanel boardPanel = new JPanel(new GridLayout(8, 8));
    private Piece selectedPiece = null;
    private int selectedRow = -1, selectedCol = -1;

    public ChessUI(Game game) {
        this.game = game;
        game.startGame();
        createAndShowGUI();
        refreshBoard();
    }
    
    private void addLabels(JFrame frame) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Labels for Files (a-h)
        JPanel filePanel = new JPanel(new GridLayout(1, 8));
        // Labels for Ranks (1-8)
        JPanel rankPanel = new JPanel(new GridLayout(8, 1));

        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (String file : files) {
            JLabel label = new JLabel(file, SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(80, 20));
            filePanel.add(label);
        }

        for (int i = 0; i < 8; i++) {
            // Chess ranks are usually 8 at the top to 1 at the bottom
            JLabel label = new JLabel(String.valueOf(8 - i), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(20, 80));
            rankPanel.add(label);
        }

        mainPanel.add(filePanel, BorderLayout.SOUTH); // Files at bottom
        mainPanel.add(rankPanel, BorderLayout.WEST);  // Ranks on left
        mainPanel.add(boardPanel, BorderLayout.CENTER); // Board in middle

        frame.add(mainPanel);
    }
    
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Chess Engine - Java");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(80, 80)); // Standard square size
                btn.setFocusPainted(false);
                btn.setBorder(null);

                final int row = i;
                final int col = j;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleClick(row, col);
                    }
                });
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        frame.add(boardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        
        addLabels(frame); 

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleClick(int row, int col) {
        if (game.isGameOver()) return;

        Piece clicked = game.getBoard().getBoard()[row][col];

        if (selectedPiece == null) {
            // STEP 1: Selecting a piece (must be your turn)
            if (clicked != null && clicked.getColor() == game.getCurrentTurn()) {
                selectedPiece = clicked;
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            // STEP 2: Attempting a move
            try {
                // If the user clicks their own piece again, switch selection
                if (clicked != null && clicked.getColor() == game.getCurrentTurn()) {
                    selectedPiece = clicked;
                    selectedRow = row;
                    selectedCol = col;
                } else {
                    // Try to execute the move logic
                    game.playMove(selectedRow, selectedCol, row, col);
                    
                    // Move successful: Clear selection
                    selectedPiece = null;
                    selectedRow = -1;
                    selectedCol = -1;
                }
            } catch (InvalidMoveException ex) {
                // Flash the error to the user
                JOptionPane.showMessageDialog(boardPanel, ex.getMessage(), "Invalid Move", JOptionPane.WARNING_MESSAGE);
                // We don't reset selection here so they can try a different move with the same piece
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        // Find if either King is currently in check
        boolean whiteInCheck = game.getBoard().inCheck(Color.WHITE);
        boolean blackInCheck = game.getBoard().inCheck(Color.BLACK);
        boolean gameOver = game.isGameOver();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = buttons[i][j];
                Piece p = game.getBoard().getBoard()[i][j];

                // 1. Update Icons
                btn.setIcon(getIcon(p));

                // 2. Set Square Colors (Default Alternating)
                java.awt.Color squareColor = (i + j) % 2 == 0 ? new java.awt.Color(235, 235, 208) : new java.awt.Color(119, 148, 85);
                btn.setBackground(squareColor);

                // 3. Highlight Selection (Yellow)
                if (i == selectedRow && j == selectedCol) {
                    btn.setBackground(java.awt.Color.YELLOW);
                }

                // 4. Highlight King States
                if (p != null && p.getPieceType() == PieceType.KING) {
                    boolean kingIsCheck = (p.getColor() == Color.WHITE && whiteInCheck) || 
                                          (p.getColor() == Color.BLACK && blackInCheck);
                    
                    if (kingIsCheck) {
                        if (gameOver) {
                            btn.setBackground(java.awt.Color.MAGENTA); // Checkmate!
                        } else {
                            btn.setBackground(new java.awt.Color(255, 100, 100)); // Red for Check
                        }
                    }
                }
            }
        }
        boardPanel.repaint();
    }

    private ImageIcon getIcon(Piece p) {
        if (p == null) return null;

        String colorPrefix = (p.getColor() == Color.WHITE) ? "white_" : "black_";
        String typeSuffix = p.getPieceType().name().toLowerCase();
        String filename = colorPrefix + typeSuffix + ".png";

        java.net.URL url = getClass().getClassLoader().getResource("images/" + filename);
        if (url == null) {
            // Fallback to text if image is missing
            return null; 
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}