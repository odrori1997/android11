package com.ashton.tictactoe;


/**
 * @author Omer, Ashton
 * 
 * <h1>King</h1>
 * 
 *
 */
public class Kingv2 extends piece {
	
	boolean inCheck;
	
	/**
	 * @param name type of piece
	 * @param color piece color
	 */
	public Kingv2 (char name, char color) {
		super(name, color);
		inCheck = false;
	}
	
	/* (non-Javadoc)
	 * @see chess.piece#move(int, int, int, int, chess.piece[][])
	 */
	public boolean move(int startX, int startY, int endX, int endY, piece[][] board)
	{
		
		// 2 possible moves:
				// 1. move 1 space in any direction --> make sure not moving into a space that's under attack
				// castle with rook --> make sure spaces between rook and king are empty, and that rook is unmoved
		
		
		
		// ERROR CHECK
		
		System.out.println(startX + ", " + startY + "-->" + endX + ", " + endY + "; " + inCheck + ", " + beenMoved); 
		System.out.println("rook: " + (board[0][7].name == 'R') + ", moved: " + board[0][7].beenMoved);
		System.out.println(checkEmpty(startX, endX, endY, board));
		
		// ERROR CHECK
		
		if(!checkValidCordinates(startX, startY, endX, endY))
			return false;
		
		// a King can't move to a spot where it would be in Check
		
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(board[y][x] != null && board[y][x].color != this.color) {						
					LinkedList spacesUnderAttact = board[y][x].isAttacking(x, y, board);
					while(spacesUnderAttact.root != null) {
						LinkedList.Node squareUnderAttack = spacesUnderAttact.remove();
						if(squareUnderAttack.xPos == endX && squareUnderAttack.yPos == endY) {
							return false; // king would be moving into check
						}
					}
				}
			}
		}
			
		
		if( (Math.abs(endX - startX) <= 1) && (Math.abs(endY - startY) <= 1) )	// make sure we're moving only 1 space
		{
			
			// friendly piece check
			if (board[endY][endX] == null || board[endY][endX].color != this.color)
			{
				board[endY][endX] = board[startY][startX];
				board[startY][startX] = null;
				
				beenMoved = true;
				
				return true;
			}
				
			
			
		}
		
		// also check for castling
		if ( !this.beenMoved && !this.inCheck) {
			
			
			
			
			if(this.color == 'w') {
				
				if (endY == 7 && endX == 6) {
					
					if ( (board[7][7].name == 'R') && !board[7][7].beenMoved) {
						if (checkEmpty(startX, endX, endY, board)) {
							
							// board[7][7].move(7, 7, 5, 7, board);
							board[endY][endX] = board[startY][startX];
							board[startY][startX] = null;
							
							beenMoved = true;
							
							board[7][5] = board[7][7];
							board[7][7] = null;
							
							
							return true;
						}
							
					
					}
				}
				
				if (endY == 7 && endX == 1) {
					if ( (board[7][0].name == 'R') && !board[7][0].beenMoved) {
						if (checkEmpty(startX, endX, endY, board)) {
							
							// board[7][0].move(0, 7, 2, 7, board);
							board[endY][endX] = board[startY][startX];
							board[startY][startX] = null;
							board[7][2] = board[7][0];
							board[7][0] = null;
							beenMoved = true;
							// use move function on rook?
							return true;
						}
					}
				}
			}

			else { // if this.color == black
				
				
					if (endY == 0 && endX == 6) {
					
						if ( (board[0][7].name == 'R') && !board[0][7].beenMoved) {
							if (checkEmpty(startX, endX, endY, board)) {
							
								// board[0][7].move(7, 0, 5, 0, board);
								board[endY][endX] = board[startY][startX];
								board[startY][startX] = null;
							
								beenMoved = true;
							
								board[0][5] = board[0][7];
								board[0][7] = null;
							
							
								return true;
						}
							
					
					}
				}
				
					
					if (endY == 0 && endX == 1) {
						if ( (board[0][0].name == 'R') && !board[0][0].beenMoved) {
							if (checkEmpty(startX, endX, endY, board)) {
								
								// board[0][0].move(0, 0, 2, 0, board);
								board[endY][endX] = board[startY][startX];
								board[startY][startX] = null;
								board[0][2] = board[0][0];
								board[0][0] = null;
								beenMoved = true;
								// use move function on rook so beenMoved is updated
								return true;
							}
						}
					}
				}
		}
		return false;
	}
	
	public LinkedList possibleMoves(int x, int y, piece[][] board) {
		LinkedList possibleMoves = new LinkedList();
		
		//right
		if(checkValidCordinates(x, y, x+1, y)) {
			if(board[y][x+1] == null || board[y][x+1].color != this.color) {
				possibleMoves.add(board[y][x+1], x+1, y);
			}
		}
		//bottom right
		if(checkValidCordinates(x, y, x+1, y+1)) {
			if(board[y+1][x+1] == null || board[y+1][x+1].color != this.color) {
				possibleMoves.add(board[y+1][x+1], x+1, y+1);
			}
		}
		//down
		if(checkValidCordinates(x, y, x, y+1)) {
			if(board[y+1][x] == null || board[y+1][x].color != this.color) {
				possibleMoves.add(board[y+1][x], x, y+1);
			}
		}
		//bottom left
		if(checkValidCordinates(x, y, x-1, y+1)) {
			if(board[y+1][x-1] == null || board[y+1][x-1].color != this.color) {
				possibleMoves.add(board[y+1][x-1], x-1, y+1);
			}
		}
		//left
		if(checkValidCordinates(x, y, x-1, y)) {
			if(board[y][x-1] == null || board[y][x-1].color != this.color) {
				possibleMoves.add(board[y][x-1], x-1, y);
			}
		}
		//top right
		if(checkValidCordinates(x, y, x+1, y-1)) {
			if(board[y-1][x+1] == null || board[y-1][x+1].color != this.color) {
				possibleMoves.add(board[y-1][x+1], x+1, y-1);
			}
		}
		//top left
		if(checkValidCordinates(x, y, x-1, y-1)) {
			if(board[y-1][x-1] == null || board[y-1][x-1].color != this.color) {
				possibleMoves.add(board[y-1][x-1], x-1, y-1);
			}
		}
		//up
		if(checkValidCordinates(x, y, x, y-1)) {
			if(board[y-1][x] == null || board[y-1][x].color != this.color) {
				possibleMoves.add(board[y-1][x], x, y-1);
			}
		}
		
		return possibleMoves;
	}
	
	/**
	 * @param startX start x coord
	 * @param endX end x coord
	 * @param y y coord -- a constant
	 * @param board board we're operating on
	 * @return true if (startX, y) to (endX, y) is empty
	 */
	public boolean checkEmpty(int startX, int endX, int y, piece board[][]) {
		
		if (!checkValidCordinates(endX, 0, startX, y)) {
			
			return false;
			
		}
		
		else {
		
			int x = startX;
			int delta = (startX > endX) ? -1 : 1; // if start < end, then we have to increment delta; otherwise decrement.
		
			while (x != endX) {
				x += delta;
				
				 System.out.println(x + ", " + y + "checking Empty"); // ERROR CHECK
				
				if (board[y][x] != null)
					return false;
			}
		}
		
		return true;
	}
	
	/*	public boolean canMove(int x, int y, piece[][] board) {
		
		// if a space is empty or has an enemy piece in it, AND isn't threatened by an enemy piece, then King can move
				
				if(board[y-1][x] == null && !(Check(x,y-1,board)) )
					return true;
				
				if(board[y][x-1] == null && !(Check(x-1,y,board)) )
					return true;
				
				if(board[y+1][x] == null && !(Check(x,y+1,board)) )
					return true;
				
				if(board[y][x+1] == null && !(Check(x+1,y,board)) )
					return true;
				
				if(board[y+1][x+1] == null && !(Check(x+1,y+1,board)) )
					return true;
				
				if(board[y-1][x-1] == null && !(Check(x-1,y-1,board)) )
					return true;
				
				if(board[y][x-1].color != this.color && !(Check(x-1,y,board)) )
					return true;
				
				if(board[y-1][x].color != this.color && !(Check(x,y-1,board)) )
					return true;
				
				if(board[y+1][x].color != this.color && !(Check(x,y+1,board)) )
					return true;
				
				if(board[y][x+1].color != this.color && !(Check(x+1,y,board)) )
					return true;
				
				if(board[y+1][x+1].color != this.color && !(Check(x+1,y+1,board)) )
					return true;
				
				if(board[y-1][x-1].color != this.color && !(Check(x-1,y-1,board)) )
					return true;
				
				
		return false;
	}
	*/
	
	// A method to see if any piece on the board threatens a given (x,y). 
	// This is meant to see if King can move to a given space without being instantly put in check. 
/*	public boolean Check(int x, int y, piece[][] board) {
		for (piece row[] : board) {
			for (piece p : row) {
				System.out.println(p.toString() + " "); // ERROR CHECK
				LinkedList spacesAtRisk = p.isAttacking(x, y, board);
				
				if (spacesAtRisk == null) {
					return false;
				}
				
				
				LinkedList.Node curr = spacesAtRisk.root; 
				
				if (curr == null) {
					return false;
				}
				
				if (curr != null) {
					
					while (curr != null) {
						// ERROR CHECKING
						System.out.println("is attacking: " + curr.xPos + ", " + curr.yPos + "\n");
						// ERROR CHECKING
						
						if (curr.xPos == x && curr.yPos == y)
						{
							return true;
						}
					
						curr = curr.next;
					}
				}
			}
		}
		return false;
	}
	*/
	
	
	// is a King attacking a space that puts it in Check? --> in this implementation, yes
	public LinkedList isAttacking(int x, int y, piece[][] board) {
		LinkedList attackingCoordinates = new LinkedList();
		
			
		
		if(checkValidCordinates(x,y, x, y-1))
			attackingCoordinates.add(board[y-1][x], x, y-1);
		
		if(checkValidCordinates(x,y, x-1, y))
			attackingCoordinates.add(board[y][x-1], x-1, y);
		
		if(checkValidCordinates(x,y, x, y+11))
			attackingCoordinates.add(board[y+1][x], x, y+1);
		
		if(checkValidCordinates(x,y, x+1, y))
			attackingCoordinates.add(board[y][x+1], x+1, y);
		
		if(checkValidCordinates(x,y, x+1, y+1))
			attackingCoordinates.add(board[y+1][x+1], x+1, y+1);
		
		if(checkValidCordinates(x,y, x-1, y-1))
			attackingCoordinates.add(board[y-1][x-1], x-1, y-1);
				
		
		return attackingCoordinates;
	}
	
/*	// see if King is in CheckMate
	public boolean checkMate(int x, int y, piece[][] board) {
		
		return (this.inCheck) && !(this.canMove(x, y, board));
		
	}*/
	
}
