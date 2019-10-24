package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 * <h1>Piece</h1>
 * <p>The board is constructed of pieces. This is a wrapper class for piece types -- i.e. King, Bishop, Rook, etc all inherit from piece.
 * Each piece has a few necessary components: </p>
 * <ul>
 * 	<li>color</li>
 * 	<li>name</li>
 * 	<li>piecesAndSpacesUnderAttack -- This is a LL of the pieces and spaces that this particular piece is attacking.</li>
 * 	<li>beenMoved -- This is used for castling functionality.</li>
 * </ul>
 * 
 */
public class piece {
	public char color;
	public char name;
	public LinkedList piecesAndSpacesUnderAttack;
	public boolean beenMoved;
	
	/**
	 * @param name type of piece
	 * @param color piece color
	 */
	public piece(char name, char color) {
		this.name = name;
		this.color = color;
		beenMoved = false;
		piecesAndSpacesUnderAttack = new LinkedList();
	}
	
	/**
	 * @param startX start x coord
	 * @param startY start y coord
	 * @param endX end x coord
	 * @param endY end y coord
	 * @return true if coordinates are valid on a standard Chess board
	 */
	public boolean checkValidCordinates(int startX, int startY, int endX, int endY) {
		if(startX < 0 || startX > 7 || startY < 0 || startY > 7 || endX < 0 || endX > 7 || endY < 0 || endY > 7) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param startX start x coord
	 * @param startY start y coord
	 * @param endX end x coord
	 * @param endY end y coord
	 * @param board board we operate on
	 * @return true if the move is valid and is completed
	 */
	public boolean move(int startX, int startY, int endX, int endY, piece[][] board) {
		return true;
	}
	
	
	/**
	 * @param x x coord of piece
	 * @param y y coord of piece
	 * @param board board we operate on
	 * @return a LL of pieces and spaces under attack from the given piece
	 */
	public LinkedList isAttacking(int x, int y, piece[][] board){
		return null;
	}
	
	/**
	 * @param x x coord of piece
	 * @param y y coord of piece
	 * @param board board we operate on
	 * @return true if piece has available moves
	 */
	public boolean canMove(int x, int y, piece[][] board) {
		return true;
	}
	
	/**
	 * @param posUnderAttack LL of Nodes containing pieces and spaces under attack
	 * @param attackingColor color of opposition
	 * @return true if enemy King is in check
	 */
	public static boolean enemyIsInCheck(LinkedList posUnderAttack, char attackingColor) {
		if (posUnderAttack == null) {
			return false;
		}
		LinkedList.Node ptr = posUnderAttack.remove();
		while(ptr != null) {
			if(ptr.chessPiece != null && ptr.chessPiece.color != attackingColor && ptr.chessPiece.name == 'K') {
				System.out.println("Check");
				return true;
			}
			ptr = posUnderAttack.remove();
		}
		return false;
	}
	
	/**
	 * @param x x coord of piece
	 * @param y y coord of piece
	 * @param board board we operate on
	 * @return LL of possible spaces a given piece can move to 
	 */
	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		return null;
	}
	
	public String toString() {
		return Character.toString(color) + Character.toString(name);
	}
}