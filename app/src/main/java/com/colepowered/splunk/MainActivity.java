package com.colepowered.splunk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button start, stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        SharedPreferences sharedPreferences = getSharedPreferences("stats", Context.MODE_PRIVATE);

        start = findViewById(R.id.start_button);
        stats = findViewById(R.id.stats_open_btn);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.startAnimation(bounce);
                toGameSettings();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatsFragment();
            }
        });
    }

    private void openStatsFragment() {
        statsDialogFrag statsDialog = new statsDialogFrag();
        statsDialog.show(getSupportFragmentManager(), "Stats Dialog");
    }

    public void toGameSettings(){
        Intent intent = new Intent(this, GameSettings.class);
        startActivity(intent);
    }
}