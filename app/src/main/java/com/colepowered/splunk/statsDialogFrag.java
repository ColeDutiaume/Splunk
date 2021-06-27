package com.colepowered.splunk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.util.Objects;

public class statsDialogFrag extends DialogFragment {

    private TextView wins_txt;
    private TextView loss_txt;
    private TextView total_shots_textview;
    private TextView accuracy_txtview;
    private Button ok;
    private Button clear_stats;
    public SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.stats_screen, null);
        ok = view.findViewById(R.id.stats_close_btn);
        clear_stats = view.findViewById(R.id.erase_stats_btn);
        Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.startAnimation(bounce);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        clear_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_stats.startAnimation(bounce);
                resetStats();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        sharedPreferences = getActivity().getSharedPreferences("stats", Context.MODE_PRIVATE);
        wins_txt = view.findViewById(R.id.wins_txt);
        loss_txt = view.findViewById(R.id.losses_txt);
        total_shots_textview = view.findViewById(R.id.shots_txt);
        accuracy_txtview = view.findViewById(R.id.accuracy_txt);

        int wins = sharedPreferences.getInt("wins", 0);
        int losses = sharedPreferences.getInt("losses", 0);
        int hits = sharedPreferences.getInt("hits", 0);
        int misses = sharedPreferences.getInt("misses", 0);

        int total_shots = hits+misses;
        double accuracy = getAccuracy(hits, total_shots);
        DecimalFormat df = new DecimalFormat("#.#");
        String acc_txt = df.format(accuracy)+"%";

        wins_txt.setText(String.valueOf(wins));
        loss_txt.setText(String.valueOf(losses));
        total_shots_textview.setText(String.valueOf(total_shots));

        if(Double.isNaN(accuracy)){
            accuracy_txtview.setText("0%");
        }else{
            accuracy_txtview.setText(acc_txt);
        }




        builder.setView(view);
        return builder.create();

    }

    private double getAccuracy(double hits, double total){
        double accuracy;
        try{
            accuracy = (hits/total)*100;
        }catch (ArithmeticException e){
            accuracy = 0;
        }
        return accuracy;
    }

    private void resetStats() {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putInt("wins", 0);
        preferencesEditor.putInt("losses", 0);
        preferencesEditor.putInt("hits", 0);
        preferencesEditor.putInt("misses", 0);
        preferencesEditor.apply();
        Toast.makeText(getContext(), "Stats have been wiped!", Toast.LENGTH_SHORT).show();
    }


}
