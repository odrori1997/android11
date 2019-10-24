package com.ashton.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class recordedGames extends Activity {

    private static final String FILE_NAME = "gameHistory.txt";

    private ArrayList<String> fileData = new ArrayList<>();
    private LinearLayout scrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recorded_games);

        FileInputStream fis = null;
        scrollView = (LinearLayout) findViewById(R.id.layout);

        Bundle bundle = getIntent().getExtras();
        String sortBy = bundle.getString("sort_by");

        if (sortBy.compareTo("date") == 0) {
            try {
                fis = openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String data;

                while ((data = br.readLine()) != null) {

                    Button newButton = new Button(this);

                    fileData.add(data);

                    String label = "";

                    int firstComma = data.indexOf(',');
                    String name = data.substring(1, firstComma);
                    String data2 = data.substring(firstComma + 1);

                    int secondComma = data2.indexOf(',');
                    String date = data2.substring(0, secondComma);
                    String data3 = data2.substring(secondComma);


                    newButton.setText(name + " - " + date);
                    newButton.setTag(data3.toString());
                    System.out.println(data3);

                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String gameHistory = b.getTag().toString();

                            Intent i = new Intent(recordedGames.this, game_playback.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();
                            //Add your data to bundle
                            bundle.putString("gameHistory", gameHistory);

                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    });

                    scrollView.addView(newButton);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            // sort by name

            try {

                ArrayList<String> list = new ArrayList<String>();

                fis = openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String data;

                while ((data = br.readLine()) != null) {
                    list.add(data);
                }

                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

                System.out.println("List:");
                System.out.println(list);

                int i = 0;
                while (i < list.size()) {

                    data = list.get(i);
                    i++;

                    Button newButton = new Button(this);

                    fileData.add(data);

                    String label = "";

                    int firstComma = data.indexOf(',');
                    String name = data.substring(1, firstComma);
                    String data2 = data.substring(firstComma + 1);

                    int secondComma = data2.indexOf(',');
                    String date = data2.substring(0, secondComma);
                    String data3 = data2.substring(secondComma);


                    newButton.setText(name + " - " + date);
                    newButton.setTag(data3.toString());
                    System.out.println(data3);

                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String gameHistory = b.getTag().toString();

                            Intent i = new Intent(recordedGames.this, game_playback.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();
                            //Add your data to bundle
                            bundle.putString("gameHistory", gameHistory);

                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    });

                    scrollView.addView(newButton);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }


        }
    }
}
