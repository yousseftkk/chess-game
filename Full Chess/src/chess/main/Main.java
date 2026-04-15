package chess.main;

import chess.game.Game;
import chess.ui.*;
import chess.board.Board;

import java.util.Scanner;

import chess.exceptions.InvalidMoveException;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board);
        game.startGame();
        new ChessUI(game);

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        while (!game.isGameOver()) {
            game.getBoard().displayBoard();
            System.out.println(game.getCurrentTurn() + "'s turn.");
            System.out.print("Enter move (sr sc er ec): ");
            int sr = scanner.nextInt();
            int sc = scanner.nextInt();
            int er = scanner.nextInt();
            int ec = scanner.nextInt();

            try {
                game.playMove(sr, sc, er, ec);
            } catch (InvalidMoveException e) {
                System.out.println("Invalid move: " + e.getMessage());
            }
        }

        System.out.println("Game over!");
    }
}