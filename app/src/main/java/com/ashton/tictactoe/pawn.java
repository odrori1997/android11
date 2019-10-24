package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 * <h1>Pawn</h1>
 */
public class pawn extends piece {

	boolean firstMove;
	int allowEnpassant = -1;
	
	/**
	 * @param name piece type
	 * @param color piece color
	 */
	public pawn(char name, char color) {
		super(name, color);
		firstMove = true;
	}
	
	/**
	 * @param startX start x coord
	 * @param startY start y coord
	 * @param endX end x coord
	 * @param endY end y coord
	 * @param board board we operate on
	 * @param promoteTo type of piece to which we promote the pawn
	 * @return true if valid promotion
	 */
	public boolean pawnPromote(int startX, int startY, int endX, int endY, piece[][] board, char promoteTo) {
		boolean result = move(startX, startY, endX, endY, board);
		
		if(result) { //if the move was valid then we do the promotion
			piece newPiece = null;
			if(promoteTo == 'N') {
				newPiece = new Knight('N', this.color);
			}
			else if(promoteTo == 'B') {
				newPiece = new Bishop('B', this.color);
			}
			else if(promoteTo == 'R') {
				newPiece = new Rook('R', this.color);
			}else {
				newPiece = new Queen('Q', this.color);
			}
			
			board[endY][endX] = newPiece;
		}		
		return result;
	}

	public boolean move(int startX, int startY, int endX, int endY, piece[][] board) {
		if(!checkValidCordinates(startX, startY, endX, endY)) {
			return false;
		}
				
		if(color == 'w') { // color is white so the pawn needs to move "up the board" - towards lower Y values

			if(endY > startY) {
				return false;
			}else { // check to make sure there are no pieces in the way
				if(endX == startX) { // in this case we know that it is not an attack
					if(board[startY - 1][startX] != null) {
						return false;
					}
					if(startY - endY == 2 && board[startY - 2][startX] != null) {
						return false;
					}
				}
			}
		}else { // color is black so the pawn needs to move "down the board" - towards higher Y values
			if(endY < startY) {
				return false;
			}else { // check to make sure there are no pieces in the way
				if(endX == startX) { // in this case we know that it is not an attack
					if(board[startY + 1][startX] != null) {
						return false;
					}
					if(endY - startY == 2 && board[startY + 2][startX] != null) {
						return false;
					}
				}
			}
		}
		
		// only allow two moves on the first turn
		if(startY - endY == 2 && !firstMove || startY - endY == -2 && !firstMove || startY - endY > 2 || startY - endY < -2) {
			return false;
		}else {
			//check to see if there is a pawn of the other color that can now do an Enpassant
			if(startX + 1 < 8 && board[endY][startX + 1] != null && board[endY][startX + 1].color != this.color && board[endY][startX + 1].name == 'p') {
				((pawn)board[endY][startX + 1]).allowEnpassant = startX;
			}
			if(startX - 1 > 0 && board[endY][startX - 1] != null && board[endY][startX - 1].color != this.color && board[endY][startX - 1].name == 'p') {
				((pawn)board[endY][startX - 1]).allowEnpassant = startX;
			}
		}
		
		//check for Enpassant
		if(endX == allowEnpassant && Math.abs(endY - startY) == 1) {
			//move the pawn
			board[endY][endX] = board[startY][startX];
			board[startY][startX] = null;
			
			//get rid of the other pawn
			if(this.color == 'w') { //if we are white then we know the pawn will be at a higher y value
				board[endY+1][endX] = null;
			}else {
				board[endY-1][endX] = null;
			}
			
			return true;
		}
		
		// only allow diagonal moves if it is an attack and only allow attack if there is another piece in that square
		if((endX == startX + 1 || endX == startX - 1) && (endY == startY - 1 || endY == startY + 1) && (board[endY][endX] != null && board[endY][endX].color != this.color)) {
			board[endY][endX] = board[startY][startX];
			board[startY][startX] = null;
			return true;
		}
		
		if(endX != startX) {
			return false;
		}
		
		//check for pawnPromote
		if(this.color == 'w' && endY == 0 || this.color == 'b' && endY == 8) {
			board[endY][endX] = new Queen('Q', this.color);
			board[startY][startX] = null;
			return true;
		}
		
		if(endY == startY + 2 || endY == startY + 1 || endY == startY - 2 || endY == startY - 1) {
			board[endY][endX] = board[startY][startX];
			board[startY][startX] = null;
			return true;
		}
		
		return false;
	}
	
	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		LinkedList possibleMoves = new LinkedList();
		
		if(this.color == 'w') {
			if(checkValidCordinates(x, y, x, y - 1)) {
				if(board[y-1][x] == null) {
					possibleMoves.add(board[y-1][x], x, y-1);
					if(firstMove && board[y-2][x] == null) {
						possibleMoves.add(board[y-2][x], x, y-2);
					}
				}
			}
			if(checkValidCordinates(x, y, x + 1, y - 1)) {
				if(board[y-1][x+1] != null && board[y-1][x+1].color != this.color) {
					possibleMoves.add(board[y-1][x+1], x+1, y-1);
				}
			}
			if(checkValidCordinates(x, y, x - 1, y - 1)) {
				if(board[y-1][x-1] != null && board[y-1][x-1].color != this.color) {
					possibleMoves.add(board[y-1][x-1], x-1, y-1);
				}
			}
			if(allowEnpassant != -1) {
				possibleMoves.add(board[y-1][allowEnpassant], allowEnpassant, y-1);
			}
		}else {
			if(checkValidCordinates(x, y, x, y + 1)) {
				if(board[y+1][x] == null) {
					possibleMoves.add(board[y+1][x], x, y+1);
					if(firstMove && board[y+2][x] == null) {
						possibleMoves.add(board[y+2][x], x, y+2);
					}
				}
			}
			if(checkValidCordinates(x, y, x + 1, y + 1)) {
				if(board[y+1][x+1] != null && board[y+1][x+1].color != this.color) {
					possibleMoves.add(board[y+1][x+1], x+1, y+1);
				}
			}
			if(checkValidCordinates(x, y, x - 1, y + 1)) {
				if(board[y+1][x-1] != null && board[y+1][x-1].color != this.color) {
					possibleMoves.add(board[y+1][x-1], x-1, y+1);
				}
			}
			if(allowEnpassant != -1) {
				possibleMoves.add(board[y+1][allowEnpassant], allowEnpassant, y+1);
			}
		}
		
		return possibleMoves;
	}
	
	public LinkedList isAttacking(int x, int y, piece[][] board){
		LinkedList attackingCordinates = new LinkedList();
		if(this.color == 'w') {
			if(checkValidCordinates(x, y, x + 1, y - 1)) {
				attackingCordinates.add(board[y-1][x+1], x+1, y-1);
			}
			if(checkValidCordinates(x, y, x - 1, y - 1)) {
				attackingCordinates.add(board[y-1][x-1], x-1, y-1);
			}
		}else {
			if(checkValidCordinates(x, y, x + 1, y + 1)) {
				attackingCordinates.add(board[y+1][x+1], x+1, y+1);
			}
			if(checkValidCordinates(x, y, x - 1, y + 1)) {
				attackingCordinates.add(board[y+1][x-1], x-1, y+1);
			}
		}
		return attackingCordinates;
	}
	
	public boolean canMove(int x, int y, piece[][] board) {
		if(this.color == 'w') {
			if(checkValidCordinates(x, y, x, y - 1) && board[y-1][x] == null) {
				return true;
			}
			if(checkValidCordinates(x, y, x+1, y - 1) && board[y-1][x+1] != null && board[y-1][x+1].color != this.color) {
				return true;
			}
			if(checkValidCordinates(x, y, x-1, y - 1) && board[y-1][x+1] != null && board[y-1][x-1].color != this.color) {
				return true;
			}
		}else {
			if(checkValidCordinates(x, y, x, y + 1) && board[y-1][x] == null) {
				return true;
			}
			if(checkValidCordinates(x, y, x+1, y + 1) && board[y-1][x+1] != null && board[y+1][x+1].color != this.color) {
				return true;
			}
			if(checkValidCordinates(x, y, x-1, y + 1) && board[y-1][x+1] != null && board[y+1][x-1].color != this.color) {
				return true;
			}			
		}
		
		return false;
	}

	/*
	public void move(int x, int y, piece[][] board) {
		firstMove = false;
		
		piecesAndSpacesUnderAttack.root = null;
		
		if(y != 0 && y != 7) { // this is the case of a regular pawn move - i.e. not a promotion
			if(color == 'w') { // color is white so the pawn needs to move "up the board" - towards lower Y values
				if(x + 1 < 8) {
					piecesAndSpacesUnderAttack.add(board[y+1][x+1], x+1, y+1);
				}
				if(x - 1 > 0) {
					piecesAndSpacesUnderAttack.add(board[y+1][x-1], x-1, y+1);
				}
			}else { // color is black so the pawn needs to move "down the board" - towards higher Y values
				if(x + 1 < 8) {
					piecesAndSpacesUnderAttack.add(board[y-1][x+1], x+1, y+1);
				}
				if(x - 1 > 0) {
					piecesAndSpacesUnderAttack.add(board[y-1][x-1], x-1, y+1);
				}
			}
		}else { // this is the case of a promotion --------- NOT CODED YET, STILL NEEDS TO BE DONE!!!!!!!
			
		}
	}
	*/
}