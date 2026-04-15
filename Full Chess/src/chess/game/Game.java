package chess.game;


import javax.swing.JOptionPane;

import chess.board.Board;
import chess.exceptions.InvalidMoveException;
import chess.pieces.Color;
import chess.pieces.Piece;
import chess.player.Player;
import chess.ui.ChessUI;


public class Game {
	private Board board;
	private final Player white = new Player(Color.WHITE);
	private final Player black = new Player(Color.BLACK);
	private Color currentTurn;
	private boolean gameOver;

	public Game(Board board) {
		this.board = board;
		setCurrentTurn(Color.WHITE);
		this.gameOver = false;
	}

	public void switchTurn() {
		if (currentTurn == Color.WHITE)
			currentTurn = Color.BLACK;

		else
			currentTurn = Color.WHITE;

	}

	public void playMove(int sr, int sc, int er, int ec)
			throws InvalidMoveException {
		Piece piece = board.getBoard()[sr][sc];
		if (piece.getColor() != currentTurn) {
			throw new InvalidMoveException("Not your turn");
		}
		board.move(sr, sc, er, ec);
		switchTurn();
		
		boolean stalemateFound = board.isStaleMate(currentTurn);
		boolean checkmateFound = board.isCheckmate(currentTurn);
		System.out.println("Checkmate? " + checkmateFound);

		if (stalemateFound) {
			this.gameOver = true;
			System.out.println("!!! DRAW !!!");
			JOptionPane.showMessageDialog(null, "Stalemate");
		}
		
		if (checkmateFound) {
			this.gameOver = true;
			String winner = currentTurn == Color.WHITE?"Black":"White";
			System.out.println("!!! CHECKMATE !!!\n" + winner + " WINS!");
			JOptionPane.showMessageDialog(null, "Checkmate!\n" + winner + " wins!");
		}
	}

	public void startGame() {
		System.out.println("UPPER CASE = BLACK \nLOWER CASE = WHITE\n");
		currentTurn = Color.WHITE;
		gameOver = false;

		board.initializeBoard();
	}

	public Color getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Color currentTurn) {
		this.currentTurn = currentTurn;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public Board getBoard() {
		return board;
	}

	public Player getWhite() {
		return white;
	}

	public Player getBlack() {
		return black;
	}
	
	public static void main(String[] args) {
		Board board = new Board();
		board.initializeBoard();
		Game game = new Game(board);
		new ChessUI(game);
	}

}
