package chess.player;

import chess.pieces.Color;

public class Player {
	Color color;
	
	public Player(Color color){
		this.color=color;
	}
	
	public Color getColor() {
		return color;
	}
}
