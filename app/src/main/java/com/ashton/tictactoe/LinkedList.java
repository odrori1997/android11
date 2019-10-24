package com.ashton.tictactoe;

/**
 * @author Omer, Ashton
 *
 */
public class LinkedList {
	
	/**
	 * <h1>Node</h1>
	 * <p>Inner class of LinkedList; creates Nodes with
	 * <ul>
	 * 	<li>chessPiece -- type of piece</li>
	 * 	<li>xPos</li>
	 * 	<li>yPos</li>
	 * 	<li>next -- pointer to next Node in list</li>
	 * </ul>
	 *
	 */
	public static class Node{
		public piece chessPiece;
		public int xPos;
		public int yPos;
		public Node next;
		
		/**
		 * @param chessPiece type of piece
		 * @param xPos x position of piece
		 * @param yPos y position of piece
		 */
		public Node(piece chessPiece, int xPos, int yPos) {
			this.chessPiece = chessPiece;
			this.xPos = xPos;
			this.yPos = yPos;
			next = null;
		}
	}
	
	public Node root = null;
	
	/**
	 * @param chessPiece type of piece
	 * @param xPos x position
	 * @param yPos y position
	 */
	public void add(piece chessPiece, int xPos, int yPos) {
		Node newNode = new Node(chessPiece, xPos, yPos);
		newNode.next = root;
		root = newNode;
	}
	
	/**
	 * @return most recently added Node
	 */
	public LinkedList.Node remove() {
		if(root == null) {
			return null;
		}
		Node ptr = root;
		root = root.next;
		return ptr;
	}
}