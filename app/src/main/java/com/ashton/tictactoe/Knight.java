package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 *
 */
public class Knight extends piece {

	public Knight(char name, char color) {
		super(name, color);
	}
	
	public boolean move(int startX, int startY, int endX, int endY, piece[][] board) {
		if(!checkValidCordinates(startX, startY, endX, endY)) {
			return false;
		}
		
		// check to make sure that the move is the correct number of spaces
		if(Math.abs(startY - endY) == 2 && Math.abs(startX - endX) == 1 || Math.abs(startY - endY) == 1 && Math.abs(startX - endX) == 2) {
			// check to make sure that there is not a friendly piece in the end square
			if(board[endY][endX] == null || board[endY][endX].color != this.color) {
				board[endY][endX] = board[startY][startX];
				board[startY][startX] = null;
				return true;
			}
		}
		
		return false;
	}
	
	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		LinkedList possibleMoves = new LinkedList();
	
		if(checkValidCordinates(x, y, x + 2, y + 1)) {
			if(board[y+1][x+2] == null || board[y+1][x+2].color != this.color) {
				possibleMoves.add(board[y+1][x+2], x+2, y+1);
			}
		}
		if(checkValidCordinates(x, y, x + 2, y - 1)) {
			if(board[y-1][x+2] != null && board[y-1][x+2].color != this.color) {
				possibleMoves.add(board[y-1][x+2], x+2, y-1);
			}
		}
		if(checkValidCordinates(x, y, x - 2, y - 1)) {
			if(board[y-1][x-2] == null || board[y-1][x-2].color != this.color) {
				possibleMoves.add(board[y-1][x-2], x-2, y-1);
			}
		}
		if(checkValidCordinates(x, y, x - 2, y + 1)) {
			if(board[y+1][x-2] == null || board[y+1][x-2].color != this.color) {
				possibleMoves.add(board[y+1][x-2], x-2, y+1);
			}
		}
		if(checkValidCordinates(x, y, x - 1, y + 2)) {
			if(board[y+2][x-1] == null || board[y+2][x-1].color != this.color) {
				possibleMoves.add(board[y+2][x-1], x-1, y+2);
			}
		}
		if(checkValidCordinates(x, y, x + 1, y + 2)) {
			if(board[y+2][x+1] == null || board[y+2][x+1].color != this.color) {
				possibleMoves.add(board[y+2][x+1], x+1, y+2);
			}
		}
		if(checkValidCordinates(x, y, x + 1, y - 2)) {
			if(board[y-2][x+1] == null || board[y-2][x+1].color != this.color) {
				possibleMoves.add(board[y-2][x+1], x+1, y-2);
			}
		}
		if(checkValidCordinates(x, y, x - 1, y - 2)) {
			if(board[y-2][x-1] == null || board[y-2][x-1].color != this.color) {
				possibleMoves.add(board[y-2][x-1], x-1, y-2);
			}
		}

		return possibleMoves;
	}
	
	public boolean canMove(int x, int y, piece[][] board) {
		if(checkValidCordinates(x, y, x + 2, y + 1)) {
			if(board[y+1][x+2] != null && board[y+1][x+2].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x + 2, y - 1)) {
			if(board[y-1][x+2] != null && board[y-1][x+2].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x - 2, y - 1)) {
			if(board[y-1][x-2] != null && board[y-1][x-2].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x - 2, y + 1)) {
			if(board[y+1][x-2] != null && board[y+1][x-2].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x - 1, y + 2)) {
			if(board[y+2][x-1] != null && board[y+2][x-1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x + 1, y + 2)) {
			if(board[y+2][x+1] != null && board[y+2][x+1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x + 1, y - 2)) {
			if(board[y-2][x+1] != null && board[y-2][x+1].color != this.color) {
				return true;
			}
		}
		if(checkValidCordinates(x, y, x - 1, y - 2)) {
			if(board[y-2][x-1] != null && board[y-2][x-1].color != this.color) {
				return true;
			}
		}
		return false;
	}
	
	public LinkedList isAttacking(int x, int y, piece[][] board){
		LinkedList attackingCordinates = new LinkedList();
		
		if(checkValidCordinates(x, y, x + 2, y + 1)) {
			attackingCordinates.add(board[y+1][x+2], x+2, y+1);
		}
		if(checkValidCordinates(x, y, x + 2, y - 1)) {
			attackingCordinates.add(board[y-1][x+2], x+2, y-1);
		}
		if(checkValidCordinates(x, y, x - 2, y - 1)) {
			attackingCordinates.add(board[y-1][x-2], x-2, y-1);
		}
		if(checkValidCordinates(x, y, x - 2, y + 1)) {
			attackingCordinates.add(board[y+1][x-2], x-2, y+1);
		}
		if(checkValidCordinates(x, y, x + 1, y + 2)) {
			attackingCordinates.add(board[y+2][x+1], x+1, y+2);
		}
		if(checkValidCordinates(x, y, x - 1, y + 2)) {
			attackingCordinates.add(board[y+2][x-1], x-1, y+2);
		}
		if(checkValidCordinates(x, y, x - 1, y - 2)) {
			attackingCordinates.add(board[y-2][x-1], x-1, y-2);
		}
		if(checkValidCordinates(x, y, x + 1, y - 2)) {
			attackingCordinates.add(board[y-2][x+1], x+1, y-2);
		}
		
		return attackingCordinates;
	}
}
