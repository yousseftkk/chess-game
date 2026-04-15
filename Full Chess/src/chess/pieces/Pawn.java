package chess.pieces;

public class Pawn extends Piece {
	private boolean firstMove;
	private char letter;

	public char getLetter() {
		return letter;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean isFirstMove) {
		this.firstMove = isFirstMove;
	}

	public Pawn(Color color, PieceType pieceType, char letter) {
		super(color, pieceType);
		firstMove=true;
		this.letter=letter;
	}

	public boolean isValidMove(int sr, int sc, int er, int ec) { 
		if (firstMove == true) {
			if (getColor() == Color.WHITE){
				return (sr == er + 2 && sc == ec || (sr == er + 1 && sc == ec)|| (er == sr - 1 && (ec == sc - 1 || ec == sc + 1)));
			}
			else{
				return (sr == er - 2 && sc == ec || (sr == er - 1 && sc == ec) || (er == sr + 1 && (ec == sc - 1 || ec == sc + 1)));
			}
			
		}
		if (getColor() == Color.WHITE) {
			return (sr == er + 1 && sc == ec)
					|| (er == sr - 1 && (ec == sc - 1 || ec == sc + 1));
		} else
			return (sr == er - 1 && sc == ec)
					|| (er == sr + 1 && (ec == sc - 1 || ec == sc + 1));
	}


}
