package chess.pieces;

public class Rook extends Piece {
	private boolean firstMove;
	public Rook(Color color, PieceType pieceType){
		super(color,pieceType);
		this.setFirstMove(true);
	}

	@Override
	public boolean isValidMove(int sr, int sc, int er, int ec) {
		return (sr == er) || (sc == ec);
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}
	
}
