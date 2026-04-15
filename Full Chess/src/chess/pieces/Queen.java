package chess.pieces;

public class Queen extends Piece {
	public Queen(Color color, PieceType pieceType){
		super(color,pieceType);
	}

	@Override
	public boolean isValidMove(int sr, int sc, int er, int ec) {
		return (Math.abs(er-sr) == Math.abs(ec - sc)) || ((sr == er) || (sc == ec));
	}
	
}
