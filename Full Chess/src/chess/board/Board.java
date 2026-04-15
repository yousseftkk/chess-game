package chess.board;

import java.util.ArrayList;
import chess.exceptions.InvalidMoveException;
import chess.pieces.*;


public class Board {
	private final Piece[][] board = new Piece[8][8];
	
	public int[] findKing(Color color) {
		int[] pos = new int[2];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Piece piece = board[i][j];
				if (piece != null)
					if (piece.getColor() == color
							&& piece.getPieceType() == PieceType.KING) {
						pos[0] = i;
						pos[1] = j;
					}
			}
		}
		return pos;
	}

	public boolean inCheck(Color kingColor){
		int[] kingPos = findKing(kingColor);
		int kr = kingPos[0];
	    int kc = kingPos[1];
	    
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				Piece Checker = board[i][j];
				
				if (Checker != null && Checker.getColor() != kingColor) {
					
					int sr = i;
					int sc = j;
					if (Checker.isValidMove(sr,sc,kr,kc)) {
						
					}
						if (Checker.isValidMove(sr, sc, kr, kc)) {
						 
						    if (Checker.getPieceType() == PieceType.PAWN && sc == kc) {
						        continue;
						    }
						if ((Checker.getPieceType() == PieceType.BISHOP || 
								Checker.getPieceType() == PieceType.ROOK || 
								Checker.getPieceType() == PieceType.QUEEN)) {
							int dx = Integer.signum(kr - sr);
							int dy = Integer.signum(kc - sc);
							int r = sr + dx;
							int c = sc + dy;
							boolean blocked = false;
							while (r != kr || c != kc) {
								if (board[r][c] != null){
									blocked=true;
									break;
								}
								r += dx;
								c += dy;
							}
							if (blocked) continue;
						}
						return true;
					}
				}
				
			}
		}

		return false;
	}
	
	public Piece getPiece(int row, int col) {
		return board[row][col];

	}
	
	public boolean isPathBlocked(int sr, int sc, int er, int ec, Piece piece) {
	    PieceType type = piece.getPieceType();

	    // 1. Sliding Pieces (Bishop, Rook, Queen)
	    if (type == PieceType.BISHOP || type == PieceType.ROOK || type == PieceType.QUEEN) {
	        int dx = Integer.signum(er - sr);
	        int dy = Integer.signum(ec - sc);
	        
	        int r = sr + dx;
	        int c = sc + dy;

	        while (r != er || c != ec) {
	            if (board[r][c] != null) {
	                return true; 
	            }
	            r += dx;
	            c += dy;
	        }
	    }

	    // 2. Pawns (Forward moves only)
	    if (type == PieceType.PAWN) {
	        // If moving straight forward
	        if (sc == ec) {
	            // Blocked if destination is occupied
	            if (board[er][ec] != null) return true;
	            
	            // If moving 2 squares, check the middle square
	            if (Math.abs(er - sr) == 2) {
	                int middleRow = (sr + er) / 2;
	                if (board[middleRow][sc] != null) return true;
	            }
	        }
	    }

	    // 3. Knights & Kings never have "blocked paths" because they don't slide
	    return false;
	}
	
	public ArrayList<int[]> checkAllLegalMoves(Color color) {
	    ArrayList<int[]> legalMoves = new ArrayList<>();

	    for (int sr = 0; sr < 8; sr++) {
	        for (int sc = 0; sc < 8; sc++) {
	            Piece piece = board[sr][sc];

	            
	            if (piece != null && piece.getColor() == color) {

	                for (int er = 0; er < 8; er++) {
	                    for (int ec = 0; ec < 8; ec++) {
	                        Piece target = board[er][ec];

	                       
	                        if (piece.isValidMove(sr, sc, er, ec) && !isPathBlocked(sr, sc, er, ec, piece)) {
	                            
	                            
	                            if (piece.getPieceType() == PieceType.PAWN) {
	                                boolean isDiagonal = (sc != ec);
	                                
	                            
	                                if (isDiagonal && target == null) continue;
	                                
	                       
	                                if (!isDiagonal && target != null) continue;
	                                
	                               
	                                if (Math.abs(er - sr) == 2) {
	                                    int middleRow = (sr + er) / 2;
	                                    if (board[middleRow][sc] != null) continue;
	                                }
	                            }
	                    

	                     
	                            if (target == null || target.getColor() != color) {
	                                
	
	                                Piece temp = board[er][ec];
	                                board[er][ec] = piece;
	                                board[sr][sc] = null;

	                                if (!inCheck(color)) {
	                                    legalMoves.add(new int[] { sr, sc, er, ec });
	                                    System.out.println("Found a safe move: " + piece.getPieceType() + " from " + 
	                                                        getPositionL(sr, sc) + " to " + getPositionL(er, ec));
	                                }

	                                board[sr][sc] = piece;
	                                board[er][ec] = temp;
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return legalMoves;
	}
	
	@SuppressWarnings("unused")
	public void move(int sr, int sc, int er, int ec)throws InvalidMoveException {
		System.out.println("New move: ");
		Piece piece = board[sr][sc];
		System.out.println(piece.getPieceType() + " on " + getPositionL(sr, sc) + " requesting move to " + getPositionL(er, ec) + "\nChecking...");
		Color search;

		if (piece.getColor() == Color.WHITE)
			search = Color.WHITE;
		else
			search = Color.BLACK;

		boolean castled = false;
		int[] myKingPos = findKing(search);
		int kr = myKingPos[0];
		int kc = myKingPos[1];
		Piece myKing = board[kr][kc];
		
		Piece friendly = board[er][ec];
		
		
		if (sr < 0 || sr >= 8 || sc < 0 || sc >= 8 || er < 0 || er >= 8
				|| ec < 0 || ec >= 8) {
			System.out.println("Move out of bounds");
			throw new InvalidMoveException("Move out of bounds");
		}
		if (piece == null) {
			System.out.println("No piece in starting square");
			throw new InvalidMoveException("No piece in starting square");
		}
		
		// CASTLING
		if (piece instanceof King) {
			if ((ec == 6 || ec == 2) &&((King) piece).isFirstMove() == true && ((King) piece).isCastled() == false) {
				if (inCheck(search)) {
				    System.out.println("Cannot castle while in check");
				    throw new InvalidMoveException("Cannot castle while in check");
				}
				else {
					if (piece.getColor() == Color.WHITE) {
					Piece rookShort = board[7][7];
					Piece rookLong = board[7][0];

					if (ec == 6 && er==7) { // white short castles
						System.out.println("White requests short castle");
						if (rookShort instanceof Rook) {
							if (((Rook) rookShort).isFirstMove() == true) { // if rook hasn't moved
								if (!isPathBlocked(7, 4, 7, 6, piece)) {// if there are no pieces between king and short rook
									board[7][6] = piece;
									board[7][5] = rookShort;
									board[sr][sc]=null;
									board[7][7]=null;
									piece.setPosition(new int[] {7, 6});
									rookShort.setPosition(new int[] {7, 5});

									System.out.println("White short castles\nWaiting for check threat..");

									if (inCheck(search)) {
										board[7][4] = piece;
								        board[7][7] = rookShort;
								        board[7][6] = null;
								        board[7][5] = null;
								        piece.setPosition(new int[] { sr, sc });
								        rookShort.setPosition(new int[] { 7, 7 });
										System.out.println("King is checked");
										throw new InvalidMoveException("King is checked");
									}
									((King) piece).setCastled(true);
									((Rook) rookShort).setFirstMove(false);
									castled = true;

								} else {
									System.out.println("Piece between h1 Rook and King");
									throw new InvalidMoveException("Piece between h1 Rook and King");
								}
							} else {
								System.out.println("h1 Rook already moved");
								throw new InvalidMoveException("h1 Rook already moved");
							}
						} else {
							System.out.println("h1 is not a Rook");
							throw new InvalidMoveException("h1 is not a Rook");
						}
					} else if (ec == 2 && er==7) { // white long castles
						System.out.println("White requests long castle");
						if (rookLong instanceof Rook) {
							if (((Rook) rookLong).isFirstMove() == true) { // if rook hasn't moved
								if (!isPathBlocked(7, 4, 7, 1, piece)) {	 // if there are no pieces between king and short rook
									board[7][2] = piece;
									board[7][3] = rookLong;
									board[sr][sc]=null;
									board[7][0]=null;
									piece.setPosition(new int[] { 7, 2 });
									rookLong.setPosition(new int[] { 7, 3 });

									System.out.println("White long castles\nWaiting for check threat..");

									if (inCheck(search)) {
										board[7][4] = piece;
								        board[7][0] = rookLong;
								        board[7][2] = null;
								        board[7][3] = null;
								        piece.setPosition(new int[] { sr, sc });
								        rookLong.setPosition(new int[] { 7, 0 });
										System.out.println("King is checked");
										throw new InvalidMoveException("King is checked");
									}
									((King) piece).setCastled(true);
									((Rook) rookLong).setFirstMove(false);
									castled = true;
								} else {
									System.out.println("Piece between a1 Rook and King");
									throw new InvalidMoveException("Piece between a1 Rook and King");
								}
							} else {
								System.out.println("a1 Rook already moved");
								throw new InvalidMoveException("a1 Rook already moved");
							}
						} else {
							System.out.println("a1 is not a Rook");
							throw new InvalidMoveException("a1 is not a Rook");
						}
					}
				} 
				else if (piece.getColor() == Color.BLACK) {
					Piece rookShort = board[0][7];
					Piece rookLong = board[0][0];

					if (ec == 6 && er==0) { // black short castles
						if (rookShort instanceof Rook) {
							if (((Rook) rookShort).isFirstMove() == true) { // if rook hasn't moved
								if (!isPathBlocked(0, 4, 0, 6, piece)) {		 // if there are no pieces between king and short rook
									board[0][6] = piece;
									board[0][5] = rookShort;
									board[sr][sc]=null;
									board[0][7]=null;
									piece.setPosition(new int[] { 0, 6 });
									rookShort.setPosition(new int[] { 0, 5 });

									System.out.println("Black short castles\nWaiting for check threat..");

									if (inCheck(search)) {
										board[0][4] = piece;
								        board[0][7] = rookShort;
								        board[0][6] = null;
								        board[0][5] = null;
								        piece.setPosition(new int[] { sr, sc });
								        rookShort.setPosition(new int[] { 0, 7 });
										System.out.println("King is checked");
										throw new InvalidMoveException("King is checked");
									}
									((King) piece).setCastled(true);
									((Rook) rookShort).setFirstMove(false);
									castled = true;

								} else {
									System.out
											.println("Piece between a8 Rook and King");
									throw new InvalidMoveException("Piece between a8 Rook and King");
								}
							} else {
								System.out.println("a8 Rook already moved");
								throw new InvalidMoveException("a8 Rook already moved");
							}
						} else {
							System.out.println("a8 is not a Rook");
							throw new InvalidMoveException("a8 is not a Rook");
						}
					} else if (ec == 2 && er==0 ) { // black long castles
						System.out.println("Black requests long castle\n Waiting for check threat.. ");
						if (rookLong instanceof Rook) {
							if (((Rook) rookLong).isFirstMove() == true) { // if rook hasn't moved
								if (!isPathBlocked(0, 4, 0, 1, piece)) {	 // if there are no pieces between king and short rook
									board[0][2] = piece;
									board[0][3] = rookLong;
									board[sr][sc]=null;
									board[0][0]=null;
									piece.setPosition(new int[] { 0, 2 });
									rookLong.setPosition(new int[] { 0, 3 });

									System.out.println("Black long castles\nWaiting for check threat..");

									if (inCheck(search)) {
										board[0][4] = piece;
								        board[0][0] = rookLong;
								        board[0][2] = null;
								        board[0][3] = null;
								        piece.setPosition(new int[] { sr, sc });
								        rookLong.setPosition(new int[] { 0, 0 });
										System.out.println("King is checked");
										throw new InvalidMoveException("King is checked");
									}
									((King) piece).setCastled(true);
									((Rook) rookLong).setFirstMove(false);
									castled = true;
								} else {
									System.out.println("a8 Rook already moved");
									throw new InvalidMoveException("a8 Rook already moved");
								}
							} else {
								System.out.println("a8 is not a Rook");
								throw new InvalidMoveException("a8 is not a Rook");
								}
							}
						}
					}
				}
			}
		} // end of castling
		
		
		if (!piece.isValidMove(sr, sc, er, ec) && !castled) {
			System.out.println("Invalid move");
			throw new InvalidMoveException("Invalid move");
		}
		
		if (friendly != null && friendly.getColor() == piece.getColor() && !castled ) {
			System.out.println("Cannot capture your own piece");
			throw new InvalidMoveException("Cannot capture your own piece");
		}
		
		if (piece.getPieceType() == PieceType.PAWN && piece.getColor() == Color.WHITE) {
			if ((er == sr - 1 && (ec == sc - 1 || ec == sc + 1)) && board[er][ec] == null) {
				System.out.println("Pawn can't move diagonally unless capturing");
				throw new InvalidMoveException(
					"Pawn can't move diagonally unless capturing");
			}
		}
		
		if (piece.getPieceType() == PieceType.PAWN && piece.getColor() == Color.BLACK) {
			if ((er == sr + 1 && (ec == sc - 1 || ec == sc + 1)) && board[er][ec] == null) {
				System.out.println("Pawn can't move diagonally unless capturing");
				throw new InvalidMoveException(
					"Pawn can't move diagonally unless capturing");
			}
		}
		
		if (piece.getPieceType() == PieceType.PAWN && piece.isValidMove(sr, sc, er, ec)) {
			if ((board[er][ec] != null) && (((sr == er + 1 && sc == ec) || (sr == er - 1 && sc == ec)) || ((sr==er+2 && sc==ec || sr==er-2 && sc==ec)))){
				System.out.println("Piece in the way");
				throw new InvalidMoveException("Piece in the way");
			}
		}

		if ((piece.getPieceType() == PieceType.BISHOP
				|| piece.getPieceType() == PieceType.ROOK || piece
				.getPieceType() == PieceType.QUEEN)
				&& piece.isValidMove(sr, sc, er, ec)) 
		{
			int dx = Integer.signum(er - sr);
			int dy = Integer.signum(ec - sc);
			int r = sr + dx;
			int c = sc + dy;
			while (r != er || c != ec) {
			    if (board[r][c] != null){
			    	System.out.println("Piece in the way");
			        throw new InvalidMoveException("Piece in the way");
			    }
			    if (r != er) r += dx;
			    if (c != ec) c += dy;
			}
		}
		
		
		
		
		
		
		if (piece instanceof Pawn) {
			((Pawn) piece).setFirstMove(false);
		}
		
		if (piece instanceof King) {
			((King) piece).setFirstMove(false);
		}
		
		if (piece instanceof Rook) {
			((Rook) piece).setFirstMove(false);
		}
		
		if(piece instanceof King){
			((King)piece).setFirstMove(false);
		}
		

		Piece tempTarget = board[er][ec];
		int[] originalPos = {sr, sc};
		int[] newPos = {er, ec};


		board[er][ec] = piece;
		board[sr][sc] = null;
		piece.setPosition(newPos);

		
		int[] currentKingPos = findKing(search);
		myKing = board[currentKingPos[0]][currentKingPos[1]];


		if (inCheck(search) && !castled) {
		    board[sr][sc] = piece;
		    board[er][ec] = tempTarget;
		    piece.setPosition(originalPos);
		    System.out.println("King is checked");
		    throw new InvalidMoveException("King is checked");
		}
		
		System.out.println("Move accepted.");
		
		if(piece instanceof Pawn && ((er == 7 && piece.getColor()==Color.BLACK)||(er == 0 && piece.getColor()==Color.WHITE))){
			int[] pos = piece.getPosition();
			board[pos[0]][pos[1]] = new Queen(search,PieceType.QUEEN);
			System.out.println(piece.getColor() + " " + ((Pawn)piece).getLetter() + " Pawn promoted to Queen");
		}
		
		
		System.out.println();
	}

	public String getPositionL(int r, int c){
		String res = "";
		switch(c){
			case 0:
				res="a"+res;
				break;
			case 1:
				res="b"+res;
				break;
			case 2:
				res="c"+res;
				break;
			case 3:
				res="d"+res;
				break;
			case 4:
				res="e"+res;
				break;
			case 5:
				res="f"+res;
				break;
			case 6:
				res="g"+res;
				break;
			case 7:
				res="h"+res;
				break;
		}
		switch(r){
		case 0:
			r=8;
			break;
		case 1:
			r=7;
			break;
		case 2:
			r=6;
			break;
		case 3:
			r=5;
			break;
		case 4:
			r=4;
			break;
		case 5:
			r=3;
			break;
		case 6:
			r=2;
			break;
		case 7:
			r=1;
			break;
	}
		return res + r;
		
	}
	
	public void place(Piece piece, int row, int col) {
		board[row][col] = piece;
		int[] newPos = { row, col };
		piece.setPosition(newPos);
	}

	public void initializeBoard() {
		Piece blackRook1 = new Rook(Color.BLACK, PieceType.ROOK);
		Piece blackRook2 = new Rook(Color.BLACK, PieceType.ROOK);
		Piece blackBishop1 = new Bishop(Color.BLACK, PieceType.BISHOP);
		Piece blackBishop2 = new Bishop(Color.BLACK, PieceType.BISHOP);
		Piece blackKnight1 = new Knight(Color.BLACK, PieceType.KNIGHT);
		Piece blackKnight2 = new Knight(Color.BLACK, PieceType.KNIGHT);
		Piece blackQueen = new Queen(Color.BLACK, PieceType.QUEEN);
		Piece blackKing = new King(Color.BLACK, PieceType.KING);

		Piece blackPawn1 = new Pawn(Color.BLACK, PieceType.PAWN,'a');
		Piece blackPawn2 = new Pawn(Color.BLACK, PieceType.PAWN,'b');
		Piece blackPawn3 = new Pawn(Color.BLACK, PieceType.PAWN,'c');
		Piece blackPawn4 = new Pawn(Color.BLACK, PieceType.PAWN,'d');
		Piece blackPawn5 = new Pawn(Color.BLACK, PieceType.PAWN,'e');
		Piece blackPawn6 = new Pawn(Color.BLACK, PieceType.PAWN,'f');
		Piece blackPawn7 = new Pawn(Color.BLACK, PieceType.PAWN,'g');
		Piece blackPawn8 = new Pawn(Color.BLACK, PieceType.PAWN,'h');

		Piece whiteRook1 = new Rook(Color.WHITE, PieceType.ROOK);
		Piece whiteRook2 = new Rook(Color.WHITE, PieceType.ROOK);
		Piece whiteBishop1 = new Bishop(Color.WHITE, PieceType.BISHOP);
		Piece whiteBishop2 = new Bishop(Color.WHITE, PieceType.BISHOP);
		Piece whiteKnight1 = new Knight(Color.WHITE, PieceType.KNIGHT);
		Piece whiteKnight2 = new Knight(Color.WHITE, PieceType.KNIGHT);
		Piece whiteQueen = new Queen(Color.WHITE, PieceType.QUEEN);
		Piece whiteKing = new King(Color.WHITE, PieceType.KING);

		Piece whitePawn1 = new Pawn(Color.WHITE, PieceType.PAWN,'a');
		Piece whitePawn2 = new Pawn(Color.WHITE, PieceType.PAWN,'b');
		Piece whitePawn3 = new Pawn(Color.WHITE, PieceType.PAWN,'c');
		Piece whitePawn4 = new Pawn(Color.WHITE, PieceType.PAWN,'d');
		Piece whitePawn5 = new Pawn(Color.WHITE, PieceType.PAWN,'e');
		Piece whitePawn6 = new Pawn(Color.WHITE, PieceType.PAWN,'f');
		Piece whitePawn7 = new Pawn(Color.WHITE, PieceType.PAWN,'g');
		Piece whitePawn8 = new Pawn(Color.WHITE, PieceType.PAWN,'h');

		place(blackRook1, 0, 0);
		place(blackKnight1, 0, 1);
		place(blackBishop1, 0, 2);
		place(blackQueen, 0, 3);
		place(blackKing, 0, 4);
		place(blackBishop2, 0, 5);
		place(blackKnight2, 0, 6);
		place(blackRook2, 0, 7);

		place(blackPawn1, 1, 0);
		place(blackPawn2, 1, 1);
		place(blackPawn3, 1, 2);
		place(blackPawn4, 1, 3);
		place(blackPawn5, 1, 4);
		place(blackPawn6, 1, 5);
		place(blackPawn7, 1, 6);
		place(blackPawn8, 1, 7);

		place(whiteRook1, 7, 0);
		place(whiteKnight1, 7, 1);
		place(whiteBishop1, 7, 2);
		place(whiteQueen, 7, 3);
		place(whiteKing, 7, 4);
		place(whiteBishop2, 7, 5);
		place(whiteKnight2, 7, 6);
		place(whiteRook2, 7, 7);

		place(whitePawn1, 6, 0);
		place(whitePawn2, 6, 1);
		place(whitePawn3, 6, 2);
		place(whitePawn4, 6, 3);
		place(whitePawn5, 6, 4);
		place(whitePawn6, 6, 5);
		place(whitePawn7, 6, 6);
		place(whitePawn8, 6, 7);

	}

	public void displayBoard() {

		;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null)
					System.out.print("-");
				else if (board[i][j].getColor() == Color.BLACK) {
					if (board[i][j].getPieceType() == PieceType.ROOK)
						System.out.print("R");
					else if (board[i][j].getPieceType() == PieceType.BISHOP)
						System.out.print("B");
					else if (board[i][j].getPieceType() == PieceType.KING)
						System.out.print("K");
					else if (board[i][j].getPieceType() == PieceType.KNIGHT)
						System.out.print("N");
					else if (board[i][j].getPieceType() == PieceType.QUEEN)
						System.out.print("Q");
					else
						System.out.print("P");
				}

				else if (board[i][j].getColor() == Color.WHITE) {
					if (board[i][j].getPieceType() == PieceType.ROOK)
						System.out.print("r");
					else if (board[i][j].getPieceType() == PieceType.BISHOP)
						System.out.print("b");
					else if (board[i][j].getPieceType() == PieceType.KING)
						System.out.print("k");
					else if (board[i][j].getPieceType() == PieceType.KNIGHT)
						System.out.print("n");
					else if (board[i][j].getPieceType() == PieceType.QUEEN)
						System.out.print("q");
					else
						System.out.print("p");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public Piece[][] getBoard() {
		return board;
	}

	public boolean isCheckmate(Color playerColor) {
		if (!inCheck(playerColor)) {
	        return false;
	    }
	  
	    return checkAllLegalMoves(playerColor).isEmpty();
	}
	
	public boolean isStaleMate(Color playerColor){
		return checkAllLegalMoves(playerColor).isEmpty() && !inCheck(playerColor);
	}
}
