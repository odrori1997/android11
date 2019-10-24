package com.ashton.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;


public class offer_draw extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.offer_draw_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int)(height * 0.5));

        TextView header = findViewById(R.id.header);

        Bundle bundle = getIntent().getExtras();

        String state = bundle.getString("state");

        System.out.println(state);

        String result = "";

        if(state.contains("White")){
            result += "Black offers you a draw!";
        }else{
            result += "White offers you a draw!";
        }
        header.setText(result);

        Button yesButton = findViewById(R.id.Yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(offer_draw.this, Game_Over.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("state", "It is a draw.");

                i.putExtras(bundle);

                startActivityForResult(i, 2);
            }
        });

        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("save", "keep playing");

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String save = data.getStringExtra("save");

                if(save.compareTo("yes") == 0){
                    String gameName = data.getStringExtra("gameName");
                    String result = data.getStringExtra("result");

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("save", "yes");
                    resultIntent.putExtra("gameName", gameName);
                    resultIntent.putExtra("result", result);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("save", "no");

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                finish();
            }
            else if(resultCode == RESULT_CANCELED){
                Intent i = new Intent(offer_draw.this, Game_Over.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("state", "It is a draw.");

                i.putExtras(bundle);

                startActivityForResult(i, 2);
            }
        }
    }
}
