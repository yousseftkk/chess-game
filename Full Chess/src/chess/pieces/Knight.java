package chess.pieces;

public class Knight extends Piece {
	public Knight(Color color, PieceType pieceType){
		super(color,pieceType);
	}

	@Override
	public boolean isValidMove(int sr, int sc, int er, int ec) {
		return (Math.abs(er - sr) == 2 && Math.abs(ec - sc) == 1) || (Math.abs(er - sr) == 1 && Math.abs(ec - sc) == 2);
	}
}
