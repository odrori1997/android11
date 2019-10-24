package com.ashton.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

public class Game_Over extends Activity {

    String result;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_over_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int)(height * 0.8));

        TextView game_result_text_view = findViewById(R.id.gameResult);

        Bundle bundle = getIntent().getExtras();

        String state = bundle.getString("state");

        System.out.println(state);

        result = "";

        if(state.contains("White")){
            result += "Black Wins!";
        }else if(state.contains("Black")){
            result += "White Wins!";
        }else{
            result += "It is a Draw!";
        }

        game_result_text_view.setText(result);

        Button yesButton = findViewById(R.id.Yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText gameName_EditText = findViewById(R.id.game_name);
                String gameName = gameName_EditText.getText().toString();

                if(gameName.compareTo("") == 0){
                    Toast.makeText(getApplicationContext(), "Please Enter a Game Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("save", "yes");
                resultIntent.putExtra("gameName", gameName);
                resultIntent.putExtra("result", result);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("save", "no");

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}