package chess.pieces;

public class King extends Piece {
	private boolean firstMove;
	private boolean castled;
	
	public King(Color color, PieceType pieceType){
		super(color,pieceType);
		this.firstMove=true;
		this.setCastled(false);
	}

	@Override
	public boolean isValidMove(int sr, int sc, int er, int ec) {
		return (sr == er && sc-ec == 1) || (sr == er && ec-sc == 1) ||
				(sc == ec&& sr-er == 1) || (sc == ec && er-sr == 1) ||
				(ec == sc-1 && er == sr+1) || (ec == sc-1 && er == sr-1) || 
				(ec == sc+1 && er == sr-1) || (ec == sc+1 && er == sr+1) ;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public boolean isCastled() {
		return castled;
	}

	public void setCastled(boolean castled) {
		this.castled = castled;
	}
}
