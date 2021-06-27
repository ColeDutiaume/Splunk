package com.colepowered.splunk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;


public class MainGame extends AppCompatActivity {

    GridLayout gridLayout;
    TextView shots_display;

    private static int x_value;
    private static int y_value;

    public static int getX_value() {
        return x_value;
    }

    public static int getY_value() {
        return y_value;
    }

    private static int shipSmall;
    private static int shipLarge;

    private static int shots;
    private static double difficulty;
    private static String shots_txt;

    private static int ifWon;
    private static int ifLoss;
    private static int hit = 0;
    private static int miss = 0;

    Animation bounce;

    public static int[] tiles, ship1, ship2, ship3;

    public SharedPreferences sharedPreferences;
    //preference value keys
    private final String HITS = "hits";
    private final String MISSES = "misses";
    private final String WINS = "wins";
    private final String LOSSES = "losses";

    Dialog endScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        sharedPreferences = getSharedPreferences("stats", MODE_PRIVATE);

        endScreen = new Dialog(this);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        ifWon = 0;
        ifLoss = 0;
        hit = 0;
        miss = 0;

        x_value = GameSettings.getX_value();
        y_value = GameSettings.getY_value();
        difficulty = GameSettings.getDifficulty();
        shipSmall = GameSettings.getShipSmall();
        shipLarge = GameSettings.getShipLarge();
        shots = (int)((x_value*y_value) * difficulty);
        tiles = new int[x_value*y_value];
        ship1 = new int[shipLarge];
        ship2 = new int[shipSmall];
        ship3 = new int[shipSmall];

        shots_display = findViewById(R.id.ammo_count);
        updateShotsTxt();

        gridLayout = findViewById(R.id.tile_grid);
        gridLayout.setColumnCount(x_value);
        gridLayout.setRowCount(y_value);

        //populate tiles
        Arrays.fill(tiles, 0);
        //populate multiDimensional Array
        GameSide.popMatrix();

        //ship creation
        ship1 = GameSide.buildShip(shipLarge);
        GameSide.setShip(ship1);
        ship2 = GameSide.buildShip(shipSmall);
        GameSide.setShip(ship2);
        ship3 = GameSide.buildShip(shipSmall);
        GameSide.setShip(ship3);

        //make buttons.
        for(int i = 0; i < tiles.length; i++){
            createButtons(i);
        }


    }

    private void createButtons(int link) {

        Button button = new Button(this);
        GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
        button.setId(link);
        param.height = 0;
        param.width  = 0;
        button.setLayoutParams(param);
        button.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.grid_square));
        gridLayout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiles[link] == 0){
                    button.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.miss));
                    miss++;
                }else{
                    button.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.hit));
                    tiles[link] = 2;
                    hit++;
                }

                button.startAnimation(bounce);
                button.setClickable(false);
                shots = shots-1;
                updateShotsTxt();
                checkLoss();
                checkWin();

            }
        });

    }

    private void checkLoss() {
        //lose condition
        if(shots == 0){
            ifLoss++;
            showEndDialogue(false);
        }
    }

    private void checkWin() {
        if(checkSunk(ship1)&&checkSunk(ship2)&&checkSunk(ship3)){
            ifWon++;
            showEndDialogue(true);
        }
    }

    public boolean checkSunk(int[] ship){
        boolean sunk = true;
        for(int i = 0; i < ship.length; i++){
            if(tiles[ship[i]] != 2){
                sunk = false;
                break;
            }
        }
        return sunk;
    }

    private void updateShotsTxt() {
        shots_txt = "x" + shots;
        shots_display.setText(shots_txt);
    }

    private void showEndDialogue(boolean win) {
        Button toMain;
        if(win){
            endScreen.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            endScreen.setContentView(R.layout.win_dialog);
            endScreen.setCancelable(false);
            toMain = endScreen.findViewById(R.id.finish_btn_win);
        }else{
            endScreen.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            endScreen.setContentView(R.layout.lose_dialog);
            endScreen.setCancelable(false);
            toMain = endScreen.findViewById(R.id.finish_btn_lose);
        }
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMain.startAnimation(bounce);
                saveStats();
                toMainActivity();
                endScreen.dismiss();
                MainGame.this.finish();
            }
        });
        endScreen.show();
    }

    private void saveStats() {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        int totalWins = ifWon+sharedPreferences.getInt("wins", 0);
        int totalLosses = ifLoss+sharedPreferences.getInt("losses", 0);
        int totalHits = hit+sharedPreferences.getInt("hits", 0);
        int totalMisses = miss+sharedPreferences.getInt("misses", 0);
        preferencesEditor.putInt(WINS, totalWins);
        preferencesEditor.putInt(LOSSES, totalLosses);
        preferencesEditor.putInt(HITS, totalHits);
        preferencesEditor.putInt(MISSES, totalMisses);
        preferencesEditor.apply();
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Yo!");
        alertDialog.setMessage("Are you sure you want to quit to main menu?\nStats will not be saved.");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainGame.this, MainActivity.class);
                startActivity(intent);
                MainGame.this.finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();
        alertDialog.show();
    }

}