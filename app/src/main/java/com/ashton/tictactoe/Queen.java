package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 * <h1>Queen</h1>
 *
 */
public class Queen extends piece{

	/**
	 * @param name piece type
	 * @param color piece color
	 */
	public Queen(char name, char color) {
		super(name, color);
	}

	public boolean move(int startX, int startY, int endX, int endY, piece[][] board) {
		if(!checkValidCordinates(startX, startY, endX, endY)) {
			return false;
		}

		// make sure that queen actually moves
		if(startX == endX && startY == endY) {
			return false;
		}

		// check to make sure the queen is not attacking a piece of her own color
		if(board[endY][endX] != null && board[endY][endX].color == this.color) {
			return false;
		}

		if(Math.abs(startX - endX) == Math.abs(startY - endY)) { // Queen is moving diagonally
			int distance = Math.abs(startY - endY);
			for(int i = 1; i < distance; i++) {
				if (startY > endY) { // Queen is moving "up" the board - towards lower Y values
					if(endX > startX) { //Queen is moving "right" towards higher X values - moving towards top right
						if(board[startY - i][startX + i] != null) {
							System.out.println("Error 1");
							return false;
						}
					}else{ //Queen is moving "right" towards higher X values  - moving toward top left
						if(board[startY - i][startX - i] != null) {
							System.out.println("Error 2");
							return false;
						}
					}
				}else { // Queen is moving "down" the board - towards higher Y values
					if(endX > startX) { //Queen is moving "left" towards lower X values 
						if(board[startY + i][startX + i] != null) {
							System.out.println("Error 3");
							return false;
						}
					}else{ //Queen is moving "right" towards higher X values 
						if(board[startY + i][startX - i] != null) {
							System.out.println("Error 4");
							return false;
						}
					}
				}
			}
		}

		if (startX - endX == 0 && startY != endY) { // Queen is moving vertically
			int verticleMovement = Math.abs(startY - endY);
			for(int i = 1; i < verticleMovement; i++) {
				if (startY > endY) { // Queen is moving "up" the board - towards lower Y values
					if(board[startY - i][startX] != null) {
						System.out.println("Error 5");
						return false;
					}
				}else { // Queen is moving "down" the board - towards higher Y values
					if(board[startY + i][startX] != null) {
						System.out.println("Error 6");
						return false;
					}
				}
			}
		}

		if(startY - endY == 0 && startX != endX) { // Queen is moving horizontally
			int horizontalMovement = Math.abs(startX - endX);
			for(int i = 0; i < horizontalMovement; i++) {
				if (startX < endX) { // Queen is moving "right" on the board - towards higher X values
					if(board[startY][startX + i] != null) {
						System.out.println("Error 7");
						return false;
					}
				}else { // Queen is moving "left" on the board - towards lower X values
					if(board[startY][startX - i] != null) {
						System.out.println("Error 8");
						return false;
					}
				}
			}
		}

		if(startY - endY == 0 && startX != endX || startX - endX == 0 && startY != endY || Math.abs(startX - endX) == Math.abs(startY - endY)) {
			board[endY][endX] = board[startY][startX];
			board[startY][startX] = null;
			return true;
		}
		System.out.println("Error 9");
		return false;
	}

	public LinkedList isAttacking(int x, int y, piece[][] board){
		LinkedList attackingCoordinates = new LinkedList();

		// horizontal coordinates to the right of the Queen 
		for(int i = x+1; i < 8; i++) {
			if(board[y][i] == null) {
				attackingCoordinates.add(board[y][i], i, y);
			}else {
				attackingCoordinates.add(board[y][i], i, y);
				break;
			}
		}
		// horizontal coordinates to the left of the Queen 
		for(int i = x-1; i >= 0; i--) {
			if(board[y][i] == null) {
				attackingCoordinates.add(board[y][i], i, y);
			}else {
				attackingCoordinates.add(board[y][i], i, y);
				break;
			}
		}
		//vertical coordinates above the Queen
		for(int i = y - 1; i >= 0; i--) {
			if(board[i][x] == null) {
				attackingCoordinates.add(board[i][x], x, i);
			}else {
				attackingCoordinates.add(board[i][x], x, i);
				break;
			}
		}
		//vertical coordinates below the Queen
		for(int i = y + 1; i < 8; i++) {
			if(board[i][x] == null) {
				attackingCoordinates.add(board[i][x], x, i);
			}else {
				attackingCoordinates.add(board[i][x], x, i);
				break;
			}
		}
		//diagonal positions - bottom right
		for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
			if(board[i][j] == null) {
				attackingCoordinates.add(board[i][j], j, i);
			}else {
				attackingCoordinates.add(board[i][j], j, i);
				break;
			}
		}
		//diagonal positions - bottom left
		for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
			if(board[i][j] == null) {
				attackingCoordinates.add(board[i][j], j, i);
			}else {
				attackingCoordinates.add(board[i][j], j, i);
				break;
			}
		}
		//diagonal positions - top left
		for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
			if(board[i][j] == null) {
				attackingCoordinates.add(board[i][j], j, i);
			}else {
				attackingCoordinates.add(board[i][j], j, i);
				break;
			}
		}
		//diagonal positions - top right
		for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
			if(board[i][j] == null) {
				attackingCoordinates.add(board[i][j], j, i);
			}else {
				attackingCoordinates.add(board[i][j], j, i);
				break;
			}
		}

		return attackingCoordinates;
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
		for(int i = 1; x + i < 8; i++) {
			if(board[y][x+i] != null) {
				if(board[y][x+i].color != this.color) {
					possibleMoves.add(board[y][x+i], x+i, y);
				}
				break;
			}
			possibleMoves.add(board[y][x+i], x+i, y);
		}

		return possibleMoves;
	}

	public boolean canMove(int x, int y, piece[][] board) {
		if(checkValidCordinates(x, y, x+1, y+1)) {
			if(board[y+1][x+1] == null || board[y+1][x+1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x, y+1)) {
			if(board[y+1][x] == null || board[y+1][x].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x+1, y)) {
			if(board[y][x+1] == null || board[y][x+1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x-1, y)) {
			if(board[y][x-1] == null || board[y][x-1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y-1, x, y)) {
			if(board[y-1][x] == null || board[y-1][x].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y-1, x-1, y)) {
			if(board[y-1][x-1] == null || board[y-1][x-1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y-1, x+1, y)) {
			if(board[y-1][x+1] == null || board[y-1][x+1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y+1, x-1, y)) {
			if(board[y+1][x-1] == null || board[y+1][x-1].color != this.color) {
				return true;
			}
		}
		return false;
	}

}
