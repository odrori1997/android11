package com.ashton.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class recordGamesSortPopUp extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recorded_games_sort_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int)(height * 0.5));

        final Button sort_by_name_button = findViewById(R.id.sort_by_name_button);
        sort_by_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("sort_by", "name");

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        final Button sort_by_date_button = findViewById(R.id.sort_by_date_button);
        sort_by_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("sort_by", "date");

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}
