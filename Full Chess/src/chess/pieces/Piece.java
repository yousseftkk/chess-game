package chess.pieces;

public abstract class Piece {
	private Color color;
	private PieceType pieceType;
	private int[] position;

	public Piece(Color color, PieceType pieceType) {
		this.color = color;
		this.pieceType = pieceType;
	}

	public Color getColor() {
		return color;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public abstract boolean isValidMove(int sr, int sc, int er, int ec);

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}
	
	public boolean equals(Piece piece){
		return color == piece.color && pieceType == piece.pieceType && position==piece.position;
	}

}
