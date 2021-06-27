package com.colepowered.splunk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class GameSettings extends AppCompatActivity {

    private static int x_value;
    private static int y_value;
    private static int shipSmall;
    private static int shipLarge;
    private static double difficulty;

    public static int getX_value() {
        return x_value;
    }

    public static void setX_value(int x_value) {
        GameSettings.x_value = x_value;
    }

    public static int getY_value() {
        return y_value;
    }

    public static void setY_value(int y_value) {
        GameSettings.y_value = y_value;
    }

    public static int getShipSmall() {
        return shipSmall;
    }

    public static void setShipSmall(int shipSmall) {
        GameSettings.shipSmall = shipSmall;
    }

    public static int getShipLarge() {
        return shipLarge;
    }

    public static void setShipLarge(int shipLarge) {
        GameSettings.shipLarge = shipLarge;
    }

    public static double getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(double difficulty) {
        GameSettings.difficulty = difficulty;
    }

    Button next, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        next = findViewById(R.id.play_btn);
        back = findViewById(R.id.back_btn_game_settings);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.startAnimation(bounce);
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.startAnimation(bounce);
                toMainGame();
            }
        });
    }

    public void toMainGame(){
        Intent intent = new Intent(this, MainGame.class);
        //making sure they actually select settings before continuing.
        if(x_value != 0 && difficulty != 0){
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Please Select a Size and a Difficulty", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSizeRadioButtonClicked(View view) {
        //bool to check if an button was pushed
        boolean checked = ((RadioButton) view).isChecked();
        //switch checks which button is selected
        switch(view.getId()) {
            case R.id.small_radio_btn:
                if (checked) {
                    //sets appropriate values if selected
                    x_value = 4;
                    y_value = 5;
                    shipSmall = 2;
                    shipLarge = 2;
                }
                break;
            case R.id.med_radio_btn:
                if (checked) {
                    x_value = 5;
                    y_value = 7;
                    shipSmall = 3;
                    shipLarge = 4;
                }
                break;
            case R.id.large_radio_btn:
                if(checked){
                    x_value = 7;
                    y_value = 9;
                    shipSmall = 5;
                    shipLarge = 5;
                }
                break;
            default:
                break;
        }
    }

    public void onDiffRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.easy_diff_radio_btn:
                if(checked){
                    difficulty = 0.8;
                }
                break;
            case R.id.medium_diff_radio_btn:
                if(checked){
                    difficulty = 0.65;
                }
                break;
            case R.id.hard_diff_radio_btn:
                if(checked){
                    difficulty = 0.5;
                }
                break;
            default:
                break;
        }
    }
}