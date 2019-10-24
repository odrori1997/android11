package com.ashton.tictactoe;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.graphics.drawable.*;
import android.graphics.Bitmap;
import android.widget.Toast;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.String.*;
import java.util.ArrayList;
import java.util.Random;

import com.ashton.tictactoe.piece;
import com.ashton.tictactoe.King;
import com.ashton.tictactoe.Kingv2;
import com.ashton.tictactoe.Bishop;
import com.ashton.tictactoe.Knight;
import com.ashton.tictactoe.Queen;
import com.ashton.tictactoe.Rook;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private ImageButton[][] buttons = new ImageButton[8][8];

    private TextView turn_text_view;

    private Drawable last_clicked_button_background;
    private ImageButton last_clicked_button;
    private String last_clicked_string;
    private int last_clicked_image_alpha;
    private Bitmap bitmap;

    private boolean first_click;
    private boolean offerDraw;
    ArrayList<String> moveHistory;

    private static final String FILE_NAME = "gameHistory.txt";

    //chess stuff
    private piece[][] board;
    private piece[][] board_last_itteration;
    private boolean canUndoMove;
    public boolean whitesTurn;

    private Random randomGenerator;
    int numberOfMoves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        turn_text_view = findViewById(R.id.text_view_turn);
        randomGenerator = new Random();
        moveHistory = new ArrayList<>();
        numberOfMoves = 0;

        canUndoMove = false;
        offerDraw = false;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                String buttonID = "button_" + String.valueOf((char)(j+97)) + (8-i);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        // black starting pieces
        board = new piece[8][8];
        board[0][0] = new Rook('R', 'b');
        board[0][1] = new Knight('N', 'b');
        board[0][2] = new Bishop('B', 'b');
        board[0][3] = new Queen('Q', 'b');
        board[0][4] = new Kingv2('K', 'b');
        board[0][5] = new Bishop('B', 'b');
        board[0][6] = new Knight('N', 'b');
        board[0][7] = new Rook('R', 'b');
        board[1][0] = new pawn('p', 'b');
        board[1][1] = new pawn('p', 'b');
        board[1][2] = new pawn('p', 'b');
        board[1][3] = new pawn('p', 'b');
        board[1][4] = new pawn('p', 'b');
        board[1][5] = new pawn('p', 'b');
        board[1][6] = new pawn('p', 'b');
        board[1][7] = new pawn('p', 'b');

        // white starting pieces
        board[7][0] = new Rook('R', 'w');
        board[7][1] = new Knight('N', 'w');
        board[7][2] = new Bishop('B', 'w');
        board[7][3] = new Queen('Q', 'w');
        board[7][4] = new Kingv2('K', 'w');
        board[7][5] = new Bishop('B', 'w');
        board[7][6] = new Knight('N', 'w');
        board[7][7] = new Rook('R', 'w');
        board[6][0] = new pawn('p', 'w');
        board[6][1] = new pawn('p', 'w');
        board[6][2] = new pawn('p', 'w');
        board[6][3] = new pawn('p', 'w');
        board[6][4] = new pawn('p', 'w');
        board[6][5] = new pawn('p', 'w');
        board[6][6] = new pawn('p', 'w');
        board[6][7] = new pawn('p', 'w');

        whitesTurn = true;

        first_click = true;

        Button buttonResign = findViewById(R.id.button_resign);
        buttonResign.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                TextView turn_text_view = findViewById(R.id.text_view_turn);
                String turn_text = turn_text_view.getText().toString();

                Intent i = new Intent(Game.this, Game_Over.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("state", turn_text);

                i.putExtras(bundle);

                startActivityForResult(i, 2);
            }
        });

        Button buttonUndo = findViewById(R.id.button_undo);
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canUndoMove){
                    board = board_last_itteration;

                    canUndoMove = false;
                    whitesTurn = !whitesTurn;
                    numberOfMoves--;
                    moveHistory.remove(moveHistory.size()-1);
                    drawBoard();
                    setText();
                    Toast.makeText(getApplicationContext(), "Undo Successful!", Toast.LENGTH_SHORT).show();

                    if(offerDraw){
                        offerDraw = false;
                        String turn_text = turn_text_view.getText().toString();

                        Intent i = new Intent(Game.this, offer_draw.class);

                        //Create the bundle
                        Bundle bundle = new Bundle();
                        //Add your data to bundle
                        bundle.putString("state", turn_text);

                        i.putExtras(bundle);

                        startActivityForResult(i, 1);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Can't Undo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonAI = findViewById(R.id.button_ai);
        buttonAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> possibleMoves = new ArrayList<>();
                if (whitesTurn) {
                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {
                            if(board[y][x] != null && board[y][x].toString().charAt(0) == 'w') {
                                System.out.println(board[y][x]);
                                try{
                                    LinkedList moves = board[y][x].possibleMoves(x, y, board);
                                    if(moves.root == null){
                                        continue;
                                    }

                                    LinkedList.Node move = moves.remove();
                                    while(move != null){

                                        String input = Character.toString(((char)('a' + x))) + (8 - y) + " " + Character.toString(((char)('a' + move.xPos))) + (8 - move.yPos);

                                        if(validMove(input, board, whitesTurn)) {
                                            possibleMoves.add(input);
                                        }

                                        move = moves.remove();

                                    }

                                }catch (Exception e){
                                    continue;
                                }
                            }
                        }
                    }
                }else{
                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {
                            if(board[y][x] != null && board[y][x].toString().charAt(0) == 'b') {
                                System.out.println(board[y][x]);
                                try{
                                    LinkedList moves = board[y][x].possibleMoves(x, y, board);
                                    if(moves.root == null){
                                        continue;
                                    }

                                    LinkedList.Node move = moves.remove();
                                    while(move != null){

                                        String input = Character.toString(((char)('a' + x))) + (8 - y) + " " + Character.toString(((char)('a' + move.xPos))) + (8 - move.yPos);

                                        if(validMove(input, board, whitesTurn)) {
                                            possibleMoves.add(input);
                                        }

                                        move = moves.remove();

                                    }

                                }catch (Exception e){
                                    continue;
                                }
                            }
                            System.out.println("x=" + x + " y=" + y);
                        }
                    }
                }

                int index = randomGenerator.nextInt(possibleMoves.size());
                String randomMove = possibleMoves.get(index);

                //input chess comand
                if(!parseInput(randomMove, board, whitesTurn)){
                    return;
                }

                if(!first_click){
                    first_click = true;
                    // fix the board
                    int y = 8 - Character.getNumericValue(last_clicked_string.charAt(1));
                    int x = Character.getNumericValue(last_clicked_string.charAt(0)) - 10;

                    if(y % 2 == 0 && x % 2 == 1 || y % 2 == 1 && x % 2 == 0){
                        buttons[y][x].setBackgroundColor(0xFFC66B3C);
                    }else{
                        buttons[y][x].setBackgroundColor(0xFFEAD3AA);
                    }
                }

                moveHistory.add(randomMove);
                numberOfMoves++;
                drawBoard();
                whitesTurn = !whitesTurn;
                setText();
                canUndoMove = true;

                if(offerDraw){
                    offerDraw = false;
                    String turn_text = turn_text_view.getText().toString();

                    Intent i = new Intent(Game.this, offer_draw.class);

                    //Create the bundle
                    Bundle bundle = new Bundle();
                    //Add your data to bundle
                    bundle.putString("state", turn_text);

                    i.putExtras(bundle);

                    startActivityForResult(i, 1);
                }
            }
        });

        Button buttonOfferDraw = findViewById(R.id.button_draw);
        buttonOfferDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offerDraw){
                    Toast.makeText(getApplicationContext(), "Draw  is already scheduled to be offered at the start of your opponent's next turn.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(whitesTurn){
                    Toast.makeText(getApplicationContext(), "Draw will be offered at the start of Black's next turn.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Draw will be offered at the start of White's next turn.", Toast.LENGTH_LONG).show();
                }
                offerDraw = true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(first_click){
            first_click= false;

            last_clicked_button_background =  ((ImageButton) v).getBackground();
            last_clicked_button = ((ImageButton) v);

            ((ImageButton) v).setBackgroundColor(0xFF77abff);
            last_clicked_string = ((ImageButton) v).getTag().toString();
        }else{
            first_click = true;

            String this_clicked_string = ((ImageButton) v).getTag().toString();

            String input = last_clicked_string + " " + this_clicked_string;

            System.out.println(input);

            // fix the board
            int y = 8 - Character.getNumericValue(last_clicked_string.charAt(1));
            int x = Character.getNumericValue(last_clicked_string.charAt(0)) - 10;

            if(y % 2 == 0 && x % 2 == 1 || y % 2 == 1 && x % 2 == 0){
                buttons[y][x].setBackgroundColor(0xFFC66B3C);
            }else{
                buttons[y][x].setBackgroundColor(0xFFEAD3AA);
            }

            //input chess comand
            if(!parseInput(input, board, whitesTurn)){
                return;
            }

            drawBoard();
            whitesTurn = !whitesTurn;
            setText();
            canUndoMove = true;

            moveHistory.add(input);
            numberOfMoves++;

            if(offerDraw){
                offerDraw = false;
                String turn_text = turn_text_view.getText().toString();

                Intent i = new Intent(Game.this, offer_draw.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("state", turn_text);

                i.putExtras(bundle);

                startActivityForResult(i, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 || requestCode==2){

            if(resultCode == RESULT_OK){
                String save = data.getStringExtra("save");
                String result = data.getStringExtra("result");

                if(save.compareTo("yes") == 0){
                    String gameName = data.getStringExtra("gameName");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    String output = "[" + gameName + "," + dateFormat.format(date) + "," + numberOfMoves;

                    for(int i = 0; i < moveHistory.size(); i++){
                        output += "," + moveHistory.get(i);
                    }

                    output += "," + result + "]";

                    FileOutputStream fos = null;
                    FileInputStream fis = null;

                    String existingContent = "";

                    try{
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String data1;

                        while((data1 = br.readLine()) != null){
                            existingContent += data1 + "\n";
                        }
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }finally {
                        if(fis != null){
                            try{
                                fis.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }
                    }

                    existingContent += output;

                    try{
                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                        fos.write(existingContent.getBytes());
                        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        if(fos != null){
                            try{
                                fos.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if(save.compareTo("keep playing") == 0) {
                    return;
                }else{
                    System.out.println("---------------I don't need to save the game");
                }

                finish();
            }
            else if(resultCode == RESULT_CANCELED){
                TextView turn_text_view = findViewById(R.id.text_view_turn);
                String turn_text = turn_text_view.getText().toString();

                Intent i = new Intent(Game.this, Game_Over.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("state", turn_text);

                i.putExtras(bundle);

                startActivityForResult(i, 2);
            }
        }
    }

    public void setText(){

        String output = "";

        if(whitesTurn){
            output += "White's Turn";
            if(whiteIsInCheck){
                output += " - you are in Check";
                Toast.makeText(this, "Check!", Toast.LENGTH_SHORT).show();
            }
        }else{
            output += "Black's Turn";
            if(blackIsInCheck){
                output += " - you are in Check";
                Toast.makeText(this, "Check!", Toast.LENGTH_SHORT).show();
            }
        }

        turn_text_view.setText(output);
    }

    public void drawBoard(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(board[y][x] == null){
                    buttons[y][x].setImageResource(R.drawable.blank);
                    continue;
                }

                if(board[y][x].toString().compareTo("wp") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_pawn);
                }
                else if(board[y][x].toString().compareTo("") == 0){
                    buttons[y][x].setImageResource(R.drawable.blank);
                }
                else if(board[y][x].toString().compareTo("wR") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_rook);
                }
                else if(board[y][x].toString().compareTo("wN") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_knight);
                }
                else if(board[y][x].toString().compareTo("wB") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_bishop);
                }
                else if(board[y][x].toString().compareTo("wK") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_king);
                }
                else if(board[y][x].toString().compareTo("wQ") == 0){
                    buttons[y][x].setImageResource(R.drawable.white_queen);
                }
                else if(board[y][x].toString().compareTo("bp") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_pawn);
                }
                else if(board[y][x].toString().compareTo("bR") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_rook);
                }
                else if(board[y][x].toString().compareTo("bN") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_knight);
                }
                else if(board[y][x].toString().compareTo("bB") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_bishop);
                }
                else if(board[y][x].toString().compareTo("bK") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_king);
                }
                else if(board[y][x].toString().compareTo("bQ") == 0){
                    buttons[y][x].setImageResource(R.drawable.black_queen);
                }
            }
        }
    }


    static boolean blackIsInCheck = false;
    static boolean whiteIsInCheck = false;

    /**
     * @param board board that's being operated on
     * @param whitesTurn determine which player for enpassant
     */
    public static void resetEnpassant(piece board[][], boolean whitesTurn) {
        char lastColor = 'b';
        if(whitesTurn) {
            lastColor = 'w';
        }
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(board[y][x] != null && board[y][x].color == lastColor && board[y][x].name == 'p') {
                    ((pawn) board[y][x]).allowEnpassant = -1;
                }
            }
        }
    }

    // used for check & checkmate functionality
	/* public static void checkMate(piece[][] board) {
		boolean inCheck = false;
		piece k = null;
		int kX = -1, kY = -1;

		// first see if king in check
		for(int iy = 0; iy < 8; iy++) {
			for(int ix = 0; ix < 8; ix++) {
				if(board[iy][ix] != null) {
					LinkedList spacesUnderAttack = board[iy][ix].isAttacking(ix, iy, board);


					while(spacesUnderAttack.root != null) {


						LinkedList.Node squareUnderAttack = spacesUnderAttack.remove();


						// ERROR CHECK

						LinkedList.Node curr = spacesUnderAttack.root;
						while (curr != null) {
							System.out.print(curr.chessPiece.toString() + " -> ");
							curr = curr.next;
						}
						System.out.println("/");

						System.out.println(board[iy][ix].toString() + " board, square " + squareUnderAttack.chessPiece.toString());

						// System.out.println("root: " + spacesUnderAttack.root.chessPiece.toString());
						// ERROR CHECK

						if(squareUnderAttack.chessPiece.name == 'K' && squareUnderAttack.chessPiece.color != board[iy][ix].color) {
							inCheck = true;
							k = squareUnderAttack.chessPiece;
							kX = ix;
							kY = iy;
						}


					}
				}
			}
		}


		if (k == null)
			return;

		// if king in check and can't move, then checkmate
		if ((inCheck) && (!k.canMove(kX, kY, board))) {
			System.out.println("Checkmate");
			if (k.color == 'b') {
				System.out.println("White wins");
			}
			else System.out.println("Black wins");
		}

		else if (inCheck)
			System.out.println("Check");

	} */

    /**
     * @param board board we're operating on
     * @return a dummy board for testing check
     */
    public static piece[][] createDummyBoard(piece[][] board){
        piece[][] dummyBoard = new piece[8][8];

        for(int y=0; y<8; y++) {
            for(int x=0; x<8; x++) {
                if(board[y][x] != null) {
                    if(board[y][x].name == 'p') {
                        dummyBoard[y][x] = new pawn('p', board[y][x].color);
                        ((pawn)dummyBoard[y][x]).firstMove = ((pawn)board[y][x]).firstMove;
                        ((pawn)dummyBoard[y][x]).allowEnpassant = ((pawn)board[y][x]).allowEnpassant;
                    }
                    else if(board[y][x].name == 'Q') {
                        dummyBoard[y][x] = new Queen('Q', board[y][x].color);
                    }
                    else if(board[y][x].name == 'N') {
                        dummyBoard[y][x] = new Knight('N', board[y][x].color);
                    }
                    else if(board[y][x].name == 'B') {
                        dummyBoard[y][x] = new Bishop('B', board[y][x].color);
                    }
                    else if(board[y][x].name == 'R') {
                        dummyBoard[y][x] = new Rook('R', board[y][x].color);
                        ((Rook)dummyBoard[y][x]).beenMoved = ((Rook)board[y][x]).beenMoved;
                    }
                    else if(board[y][x].name == 'K') { // ------------------I think that we need to add the "beenMoved" think to king for castling --------------
                        dummyBoard[y][x] = new Kingv2('K', board[y][x].color);
                    }
                }
            }
        }

        return dummyBoard;
    }

    /**
     * @param board board we're operating on
     * @param color determine which player is in check
     * @return true if king is in check; false otherwise
     */
    public static boolean checkForCheck(piece[][] board, char color) {
        //find if any enemy piece is attacking the king's square
        for(int y = 0; y<8; y++) {
            for(int x = 0; x<8; x++) {
                if(board[y][x] != null && board[y][x].color != color) {
                    LinkedList spotsUnderAttack = board[y][x].isAttacking(x, y, board);
                    while(spotsUnderAttack.root != null){
                        LinkedList.Node space = spotsUnderAttack.remove();
                        if(board[space.yPos][space.xPos] != null && board[space.yPos][space.xPos].name == 'K' && board[space.yPos][space.xPos].color == color) {
                            //this is what happens when it is discovered the enemy's king is in check
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param board board we're operating on
     * @param color color of player
     * @return true if checkmate; false otherwise
     */
    public static boolean checkForChekmate(piece[][] board, char color) {
        for(int y = 0; y<8; y++) {
            for(int x = 0; x<8; x++) {
                if(board[y][x] != null && board[y][x].color == color) {
                    LinkedList possibleMoves = board[y][x].possibleMoves(x, y, board);
                    while(possibleMoves.root != null) {
                        piece[][] dummyBoard = createDummyBoard(board);
                        LinkedList.Node move = possibleMoves.remove();
                        if(dummyBoard[y][x].move(x, y, move.xPos, move.yPos, dummyBoard)) {
                            if(!checkForCheck(dummyBoard, color)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("There is a checkmate");
        return true;
    }

    /**
     * @param board board we're operating on
     * @param color player color
     * @return true if stalemate; false otherwise
     */
    public static boolean checkForStalemate(piece[][] board, char color) {
        for(int y = 1; y<8; y++) {
            for(int x = 1; x<8; x++) {
                if(board[y][x] != null && board[y][x].color == color) {
                    LinkedList possibleMoves = board[y][x].possibleMoves(x, y, board);
                    while(possibleMoves.root != null) {
                        piece[][] dummyBoard = createDummyBoard(board);
                        LinkedList.Node move = possibleMoves.remove();
                        if(dummyBoard[y][x].move(x, y, move.xPos, move.yPos, dummyBoard)) {
                            if(!checkForCheck(dummyBoard, color)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param board board to be printed
     */
    public static void printBoard(piece[][] board) {
        int y;
        for(y=0;y<board.length; y++) {
            int x;
            for(x=0;x<board[y].length; x++) {
                if(board[y][x] == null) {
                    if(y % 2 == 0 && x % 2 == 1 || y % 2 == 1 && x % 2 == 0) {
                        System.out.print("##");
                    }else {
                        System.out.print("  ");
                    }
                }else {
                    System.out.print(board[y][x]);
                }
                System.out.print(" ");
            }
            System.out.println(Integer.toString(8 - y));
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }

    public boolean validMove(String input, piece[][] board, boolean whitesTurn){
        String endPosString = "";
        String startPosString = "";
        String pawnPromote = "";

        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;

        if(input.contains("resign")) {
            if(whitesTurn) {
             //   System.out.println("Black wins");
            }else {
             //   System.out.println("White wins");
            }
            return true;
        }

        if(input.contains("draw") && !input.contains("draw?")) {
            return true;
        }

        if(input.contains(" ")) {
            startPosString = input.substring(0, input.indexOf(" "));
            String restOfInput = input.substring(input.indexOf(" "), input.length());
            if(restOfInput.length() < 2) {
                return false;
            }else {
                restOfInput = restOfInput.substring(1);
                if(restOfInput.contains(" ")) {
                    endPosString = restOfInput.substring(0, input.indexOf(" "));
                    restOfInput = restOfInput.substring(input.indexOf(" "), restOfInput.length());
                    pawnPromote = restOfInput.substring(1);
                }else {
                    endPosString = restOfInput;
                }
            }
        }

        if(startPosString.length() == 2) {
            startX = Character.toUpperCase(startPosString.charAt(0)) - 'A';
            startY = 7 - (startPosString.charAt(1) - '1');
        }

        if(endPosString.length() == 2) {
            endX = Character.toUpperCase(endPosString.charAt(0)) - 'A';
            endY = 7 - (endPosString.charAt(1) - '1');
        }

        //check to make sure there is a piece that the player is trying to move
        if(board[startY][startX] == null) {
            //System.out.println("Illegal move, try again");
            return false;
        }

        // make sure we're moving the right color's piece
        if (whitesTurn) {
            if (board[startY][startX].color != 'w') {
                //System.out.println("Illegal move, try again");
                return false;
            }
        }
        else {
            if (board[startY][startX].color != 'b') {
                //System.out.println("Illegal move, try again");
                return false;
            }
        }

        piece[][] dummyBoard = createDummyBoard(board);

        //System.out.println("-----------------------");
        //printBoard(dummyBoard);
        //System.out.println("-----------------------");

        if(pawnPromote.length() == 1) { // check if it is a pawn promote move
            if(!((pawn)dummyBoard[startY][startX]).pawnPromote(startX, startY, endX, endY, dummyBoard, pawnPromote.charAt(0))) {
                //System.out.println("Illegal move, try again");
                return false;
            }else {
                ((pawn)dummyBoard[startY][startX]).pawnPromote(startX, startY, endX, endY, dummyBoard, pawnPromote.charAt(0));
            }
        }else if(!dummyBoard[startY][startX].move(startX, startY, endX, endY, dummyBoard)) { //regular move
            //System.out.println("Illegal move, try again");
            return false;
        }else {
            //dummy board move worked - check to see if the move put the player in check
            if(whitesTurn) {
                if(checkForCheck(dummyBoard, 'w')) {
                //    System.out.println("Illegal move, try again");
                    return false;
                }
            }else {
                if(checkForCheck(dummyBoard, 'b')) {
                 //   System.out.println("Illegal move, try again");
                    return false;
                }
            }

            //if the player was in check make sure that they are no longer in check
            if(!whitesTurn && blackIsInCheck) {
                if(checkForCheck(dummyBoard, 'b')) {
                 //   System.out.println("Illegal move, try again");
                    return false;
                }
            }
            if(whitesTurn && whiteIsInCheck) {
                if(checkForCheck(dummyBoard, 'w')) {
                //    System.out.println("Illegal move, try again");
                    return false;
                }
            }

            //copy over into dummy board
            board_last_itteration = createDummyBoard(board);

            //now make the move on the actual board
            //board[startY][startX].move(startX, startY, endX, endY, board);
        }

        // check to see if the move put the other player in check or checkmate
        if(whitesTurn) {
            if(checkForCheck(board, 'b')) {
                //black is in check
                if(checkForChekmate(board, 'b')) {
               //     System.out.println("Checkmate");
               //     System.out.println("White wins");
                    return true;
                }
                System.out.println("Check");
                blackIsInCheck = true;
            }else {
                blackIsInCheck = false;
                if(checkForStalemate(board, 'b')) {
                //    System.out.println("Stalemate");
                //    System.out.println("draw");
                    return true;
                }
            }
        }else {
            if(checkForCheck(board, 'w')) {
                //white is in check
                if(checkForChekmate(board, 'w')) {
                //    System.out.println("Checkmate");
                //    System.out.println("Black wins");
                    return true;
                }

               // System.out.println("Check");
                whiteIsInCheck = true;
            }else {
                whiteIsInCheck = false;
                if(checkForStalemate(board, 'b')) {
                    //System.out.println("Stalemate");
                    //System.out.println("draw");
                    return true;
                }
            }
        }

        return true;
    }

    /**
     * @param input commands given by user
     * @param board board to operate on
     * @param whitesTurn player whose turn it is
     * @return true if valid input; false otherwise
     */
    public boolean parseInput(String input, piece[][] board, boolean whitesTurn) {

        String endPosString = "";
        String startPosString = "";
        String pawnPromote = "";

        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;

        if(input.contains("resign")) {
            if(whitesTurn) {
                System.out.println("Black wins");
                Toast.makeText(getApplicationContext(), "Black wins!", Toast.LENGTH_SHORT).show();
            }else {
                System.out.println("White wins");
                Toast.makeText(getApplicationContext(), "White wins!", Toast.LENGTH_SHORT).show();
            }
            System.exit(0);
        }

        if(input.contains("draw") && !input.contains("draw?")) {
            System.exit(0);
        }

        if(input.contains(" ")) {
            startPosString = input.substring(0, input.indexOf(" "));
            String restOfInput = input.substring(input.indexOf(" "), input.length());
            if(restOfInput.length() < 2) {
                return false;
            }else {
                restOfInput = restOfInput.substring(1);
                if(restOfInput.contains(" ")) {
                    endPosString = restOfInput.substring(0, input.indexOf(" "));
                    restOfInput = restOfInput.substring(input.indexOf(" "), restOfInput.length());
                    pawnPromote = restOfInput.substring(1);
                }else {
                    endPosString = restOfInput;
                }
            }
        }

        if(startPosString.length() == 2) {
            startX = Character.toUpperCase(startPosString.charAt(0)) - 'A';
            startY = 7 - (startPosString.charAt(1) - '1');
        }

        if(endPosString.length() == 2) {
            endX = Character.toUpperCase(endPosString.charAt(0)) - 'A';
            endY = 7 - (endPosString.charAt(1) - '1');
        }

        //check to make sure there is a piece that the player is trying to move
        if(board[startY][startX] == null) {
            System.out.println("Illegal move, try again");
            Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // make sure we're moving the right color's piece
        if (whitesTurn) {
            if (board[startY][startX].color != 'w') {
                System.out.println("Illegal move, try again");
                Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            if (board[startY][startX].color != 'b') {
                System.out.println("Illegal move, try again");
                Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        piece[][] dummyBoard = createDummyBoard(board);

        //System.out.println("-----------------------");
        //printBoard(dummyBoard);
        //System.out.println("-----------------------");

        if(pawnPromote.length() == 1) { // check if it is a pawn promote move
            if(!((pawn)dummyBoard[startY][startX]).pawnPromote(startX, startY, endX, endY, dummyBoard, pawnPromote.charAt(0))) {
                System.out.println("Illegal move, try again");
                Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                ((pawn)dummyBoard[startY][startX]).pawnPromote(startX, startY, endX, endY, dummyBoard, pawnPromote.charAt(0));
            }
        }else if(!dummyBoard[startY][startX].move(startX, startY, endX, endY, dummyBoard)) { //regular move
            System.out.println("Illegal move, try again");
            Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            //dummy board move worked - check to see if the move put the player in check
            if(whitesTurn) {
                if(checkForCheck(dummyBoard, 'w')) {
                    System.out.println("Illegal move, try again");
                    Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                if(checkForCheck(dummyBoard, 'b')) {
                    System.out.println("Illegal move, try again");
                    Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            //if the player was in check make sure that they are no longer in check
            if(!whitesTurn && blackIsInCheck) {
                if(checkForCheck(dummyBoard, 'b')) {
                    System.out.println("Illegal move, try again");
                    Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(whitesTurn && whiteIsInCheck) {
                if(checkForCheck(dummyBoard, 'w')) {
                    System.out.println("Illegal move, try again");
                    Toast.makeText(getApplicationContext(), "Illegal move, try again.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            //copy over into dummy board
            board_last_itteration = createDummyBoard(board);

            //now make the move on the actual board
            board[startY][startX].move(startX, startY, endX, endY, board);
        }

        // check to see if the move put the other player in check or checkmate
        if(whitesTurn) {
            if(checkForCheck(board, 'b')) {
                //black is in check
                if(checkForChekmate(board, 'b')) {
                    System.out.println("Checkmate");
                    System.out.println("White wins");
                    Toast.makeText(getApplicationContext(), "Checkmate! White Wins!", Toast.LENGTH_SHORT).show();

                    String text = "Black";

                    Intent i = new Intent(Game.this, Game_Over.class);

                    //Create the bundle
                    Bundle bundle = new Bundle();
                    //Add your data to bundle
                    bundle.putString("state", text);

                    i.putExtras(bundle);

                    startActivityForResult(i, 2);
                }
                System.out.println("Check");
                Toast.makeText(getApplicationContext(), "Check!", Toast.LENGTH_SHORT).show();
                blackIsInCheck = true;
            }else {
                blackIsInCheck = false;
                if(checkForStalemate(board, 'b')) {
                    System.out.println("Stalemate");
                    System.out.println("draw");
                    System.exit(0);
                }
            }
        }else {
            if(checkForCheck(board, 'w')) {
                //white is in check
                if(checkForChekmate(board, 'w')) {
                    Toast.makeText(getApplicationContext(), "Checkmate! Black Wins!.", Toast.LENGTH_SHORT).show();
                    System.out.println("Checkmate");
                    System.out.println("Black wins");
                    String text = "White";

                    Intent i2 = new Intent(Game.this, Game_Over.class);

                    //Create the bundle
                    Bundle bundle = new Bundle();
                    //Add your data to bundle
                    bundle.putString("state", text);

                    i2.putExtras(bundle);

                    startActivityForResult(i2, 2);
                }

                System.out.println("Check");
                Toast.makeText(getApplicationContext(), "Check!", Toast.LENGTH_SHORT).show();
                whiteIsInCheck = true;
            }else {
                whiteIsInCheck = false;
                if(checkForStalemate(board, 'b')) {
                    System.out.println("Stalemate");
                    System.out.println("draw");
                    System.exit(0);
                }
            }
        }

        return true;
    }

}
