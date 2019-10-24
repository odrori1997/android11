package com.ashton.tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.graphics.drawable.*;
import android.graphics.Bitmap;
import android.widget.Toast;
import android.content.Intent;
import java.lang.String.*;

import com.ashton.tictactoe.piece;
import com.ashton.tictactoe.King;
import com.ashton.tictactoe.Kingv2;
import com.ashton.tictactoe.Bishop;
import com.ashton.tictactoe.Knight;
import com.ashton.tictactoe.Queen;
import com.ashton.tictactoe.Rook;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonResign = findViewById(R.id.start_game_button);
        buttonResign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Game.class);

                startActivity(i);
            }
        });

        final Button recordedGames = findViewById(R.id.recorded_games);
        recordedGames.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, recordGamesSortPopUp.class);

                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){

                Intent i = new Intent(MainActivity.this, recordedGames.class);

                String sort_by = data.getStringExtra("sort_by");
                Bundle bundle = new Bundle();
                bundle.putString("sort_by", sort_by);

                i.putExtras(bundle);

                startActivity(i);
            }
        }

    }

}

