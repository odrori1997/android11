package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 * <h1>Rook</h1>
 *
 */
public class Rook extends piece {


	/**
	 * @param name piece type
	 * @param color piece color
	 */
	public Rook(char name, char color)
	{
		super(name, color);
		beenMoved = false;
	}

	public boolean move(int startX, int startY, int endX, int endY, piece[][] board)
	{
		if(!checkValidCordinates(startX, startY, endX, endY))
			return false;

		if( (endX - startX == 0) && startY == endY || (endY - startY == 0) && startX == endX )	// make sure we're moving vertically or horizontally
		{
			//check to see if there are any pieces in the way
			if(startY == endY) { // rook is moving horizontally
				int distance = Math.abs(startY - endY);
				for(int i = 1; i < distance - 1; i++) {
					if(startY > endY) { //rook is moving to the left
						if(board[startY][startX - i] != null) {
							return false;
						}
					}else if (startY < endY) { // rook is moving to the right
						if(board[startY][startX + i] != null) {
							return false;
						}
					}
				}
			}else { // rook is moving vertically
				int distance = Math.abs(startX - endX);
				for(int i = 1; i < distance - 1; i++) {
					if(startX > endX) { // moving down the board
						if(board[startY + i][startX] != null) {
							return false;
						}
					}else if (startX > endX) { //moving up the board
						if(board[startY - i][startX] != null) {
							return false;
						}
					}
				}
			}

			// friendly piece check
			if (board[endY][endX] == null || board[endY][endX].color != this.color)
			{
				board[endY][endX] = board[startY][startX];
				board[startY][startX] = null;

				this.beenMoved = true;

				return true;
			}

		}

		return false;
	}

	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		LinkedList possibleMoves = new LinkedList();

		//add horizontal moves
		for(int i = 1; x + i < 8; i++) {
			if(board[y][x+i] != null) {
				if(board[y][x+i].color != this.color) {
					possibleMoves.add(board[y][x+i], x+i, y);
				}
				break;
			}
			possibleMoves.add(board[y][x+i], x+i, y);
		}
		for(int i = 1; x - i >= 0; i++) {
			if(board[y][x-i] != null) {
				if(board[y][x-i].color != this.color) {
					possibleMoves.add(board[y][x-i], x-i, y);
				}
				break;
			}
			possibleMoves.add(board[y][x-i], x-i, y);
		}

		//add vertical moves
		for(int i = 1; y - i >= 0; i++) {
			if(board[y-i][x] != null) {
				if(board[y-i][x].color != this.color) {
					possibleMoves.add(board[y-i][x], x, y-i);
				}
				break;
			}
			possibleMoves.add(board[y-i][x], x, y-i);
		}
		for(int i = 1; y + i < 8; i++) {
			if(board[y+i][x] != null) {
				if(board[y+i][x].color != this.color) {
					possibleMoves.add(board[y+i][x], x, y+i);
				}
				break;
			}
			possibleMoves.add(board[y+i][x], x, y+i);
		}

		return possibleMoves;
	}

	public boolean canMove(int x, int y, piece[][] board) {

		int i = 1;


		if(board[y-i][x] == null || board[y][x-i] == null)
			return true;

		if(board[y+i][x] == null || board[y][x+i] == null)
			return true;

		if(board[y][x-i].color != this.color)
			return true;

		if(board[y-i][x].color != this.color)
			return true;

		if(board[y+i][x].color != this.color)
			return true;

		if(board[y][x+i].color != this.color)
			return true;


		return false;
	}

	public LinkedList isAttacking(int x, int y, piece[][] board) {
		LinkedList attackingCoordinates = new LinkedList();

		// positions to the right
		for(int attackingX = x + 1; attackingX < 8; attackingX++) {
			attackingCoordinates.add(board[y][attackingX], attackingX, y);
			if(board[y][attackingX] != null) {
				break;
			}
		}
		// positions to the right
		for(int attackingX = x - 1; attackingX >= 0; attackingX--) {
			attackingCoordinates.add(board[y][attackingX], attackingX, y);
			if(board[y][attackingX] != null) {
				break;
			}
		}
		// positions "upwards"
		for(int attackingY = y - 1; attackingY >= 0; attackingY--) {
			attackingCoordinates.add(board[attackingY][x], x, attackingY);
			if(board[attackingY][x] != null) {
				break;
			}
		}
		// positions "downwards"
		for(int attackingY = y + 1; attackingY < 8; attackingY++) {
			attackingCoordinates.add(board[attackingY][x], x, attackingY);
			if(board[attackingY][x] != null) {
				break;
			}
		}

		return attackingCoordinates;
	}


	// check whether all spaces are empty in a given dimension

	/**
	 * @param startPos starting coord
	 * @param endPos ending coord
	 * @param fixed number of dimension on which we fix (i.e. 1,2,...)
	 * @param dimension dimension type (i.e. x,y)
	 * @param board board we operate on
	 * @return check if a given dimension (row or column) is empty
	 */
	public boolean checkEmpty(int startPos, int endPos, int fixed, char dimension, piece board[][]) {

		if (checkValidCordinates(endPos, 0, startPos, 0)) {

			int x = startPos;
			int delta = (startPos > endPos) ? -1 : 1; // if start < end, then we have to increment delta; otherwise decrement.

			while (x != endPos) {
				x += delta;
				if (dimension == 'x') {
					if (board[fixed][x] != null)
						return false;
				}
				if (dimension == 'y') {
					if (board[x][fixed] != null)
						return false;
				}


			}
		}
		else { // if coordinates aren't valid, just return false
			return false;
		}

		return true; // if false still hasn't been returned, the row/col is empty, so return true
	}

}


