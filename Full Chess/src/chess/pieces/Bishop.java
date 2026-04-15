package chess.pieces;

public class Bishop extends Piece {
	public Bishop(Color color, PieceType pieceType){
		super(color,pieceType);
	}

	@Override
	public boolean isValidMove(int sr, int sc, int er, int ec) {
		return Math.abs(er-sr) == Math.abs(ec - sc);
	}
}
