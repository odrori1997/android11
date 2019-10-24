package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 *
 * <h1>Bishop</h1>
 */
public class Bishop extends piece {

	/**
	 * @param name piece type
	 * @param color piece color
	 */
	public Bishop(char name, char color)
	{
		super(name, color);
	}

	// Bishops move diagonally -- so change in x == change in y.
	public boolean move(int startX, int startY, int endX, int endY, piece[][] board)
	{
		if(!checkValidCordinates(startX, startY, endX, endY)) {
			return false;
		}

		if( Math.abs(endX - startX) == Math.abs(endY - startY) )	// make sure we're not moving vertically or horizontally
		{
			int distance = Math.abs(endX - startX);
			for(int i = 1; i < distance; i++) {
				if(endX < startX && endY < startY) { // top left
					if(board[startY - i][startX - i] != null) {
						return false;
					}
				}else if(endX > startX && endY < startY) { // top right
					if(board[startY - i][startX + i] != null) {
						return false;
					}
				}else if(endX > startX && endY > startY) { // bottom right
					if(board[startY + i][startX + i] != null) {
						return false;
					}
				}else { // bottom left
					if(board[startY + i][startX - i] != null) {
						return false;
					}
				}
			}


			if (board[endY][endX] == null || board[endY][endX].color != this.color)
			{
				board[endY][endX] = board[startY][startX];
				board[startY][startX] = null;
				return true;
			}

		}

		return false;
	}


	/**
	 * @param startX start x coord
	 * @param endX end x coord
	 * @param startY start y coord
	 * @param endY end y coord
	 * @param board board we operate on
	 * @return true if (startx, startY) to (endX, endY) is empty along the diagonal
	 */
	public boolean checkEmpty(int startX, int endX, int startY, int endY, piece board[][]) {

		if (checkValidCordinates(endX, endY, startX, startY)) {

			int x = startX;
			int deltaX = (startX > endX) ? -1 : 1; // if start < end, then we have to increment delta; otherwise decrement.

			int y = startY;
			int deltaY = (startY > endY) ? -1 : 1;

			while (x != endX && y != endY) {
				x += deltaX;
				y += deltaY;
				if (board[y][x] != null)
					return false;
			}
		}
		else {
			return false;
		}

		return true;
	}

	public boolean canMove(int x, int y, piece[][] board) {

		int i = 1;

		if (checkValidCordinates(x, y, x - 1, y - 1))
			if(board[y-i][x-i] == null)
				return true;

		if (checkValidCordinates(x, y, x+1, y+1))
			if(board[y+i][x+i] == null)
				return true;

		if (checkValidCordinates(x, y, x+1, y-1))
			if(board[y-i][x+i] == null)
				return true;

		if (checkValidCordinates(x,y, x-1, y+1))
			if (board[y+i][x-i] == null)
				return true;


		if (checkValidCordinates(x,y,x-1, y-1))
			if(board[y-i][x-i] != null && board[y-i][x-i].color != this.color)
				return true;

		if (checkValidCordinates(x, y, x+1, y+1))
			if(board[y+i][x+i] != null && board[y+i][x+i].color != this.color)
				return true;

		if (checkValidCordinates(x, y, x-1, y+1))
			if(board[y+i][x-i] != null && board[y+i][x-i].color != this.color)
				return true;

		if (checkValidCordinates(x, y, x+1, y-1))
			if(board[y-i][x+i] != null && board[y-i][x+i].color != this.color)
				return true;


		return false;
	}

	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		LinkedList possibleMoves = new LinkedList();
		// top left
		for(int i=1; x-i>=0 && y-i>=0; i++) {
			if(board[y-i][x-i] != null) {
				if(board[y-i][x-i].color != this.color) {
					possibleMoves.add(board[y-i][x-i], x-i, y-i);
				}
				break;
			}
			possibleMoves.add(board[y-i][x-i], x-i, y-i);
		}
		//top right
		for(int i=1; x+i<8 && y-i>=0; i++) {
			if(board[y-i][x+i] != null) {
				if(board[y-i][x+i].color != this.color) {
					possibleMoves.add(board[y-i][x+i], x+i, y-i);
				}
				break;
			}
			possibleMoves.add(board[y-i][x+i], x+i, y-i);
		}
		//bottom right
		for(int i=1; x+i<8 && y+i<8; i++) {
			if(board[y+i][x+i] != null) {
				if(board[y+i][x+i].color != this.color) {
					possibleMoves.add(board[y+i][x+i], x+i, y+i);
				}
				break;
			}
			possibleMoves.add(board[y+i][x+i], x+i, y+i);
		}
		//bottom left
		for(int i=1; x-i>=0 && y+i<8; i++) {
			if(board[y+i][x-1] != null) {
				if(board[y+i][x-1].color != this.color) {
					possibleMoves.add(board[y+i][x-1], x-i, y+i);
				}
				break;
			}
			possibleMoves.add(board[y+i][x-1], x-i, y+i);
		}
		return possibleMoves;
	}

	public LinkedList isAttacking(int x, int y, piece[][] board) {
		LinkedList attackingCoordinates = new LinkedList();

		//bottom right
		for(int attackingX = x + 1, attackingY = y + 1; attackingX < 8 && attackingY < 8; attackingX++, attackingY++) {
			attackingCoordinates.add(board[attackingY][attackingX], attackingX, attackingY);
			if(board[attackingY][attackingX] != null) {
				break;
			}
		}
		//top left
		for(int attackingX = x - 1, attackingY = y - 1; attackingX >= 0 && attackingY >= 0; attackingX--, attackingY--) {
			attackingCoordinates.add(board[attackingY][attackingX], attackingX, attackingY);
			if(board[attackingY][attackingX] != null) {
				break;
			}
		}
		//bottom left
		for(int attackingX = x - 1, attackingY = y + 1; attackingX >= 0 && attackingY < 8; attackingX--, attackingY++) {
			attackingCoordinates.add(board[attackingY][attackingX], attackingX, attackingY);
			if(board[attackingY][attackingX] != null) {
				break;
			}
		}
		//top right
		for(int attackingX = x + 1, attackingY = y - 1; attackingX < 8 && attackingY >= 0; attackingX++, attackingY--) {
			attackingCoordinates.add(board[attackingY][attackingX], attackingX, attackingY);
			if(board[attackingY][attackingX] != null) {
				break;
			}
		}

		return attackingCoordinates;
	}


}