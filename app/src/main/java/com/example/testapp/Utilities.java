package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.Random;

public class Utilities {
    public static String randomTips(Context context) {
        String[] tips = context.getResources().getStringArray(R.array.suggerimenti);
        return tips[new Random().nextInt(tips.length)];
    }
    public static boolean checkPermission(Context context, String permission){
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * TODO: sistemare questa funzione, troppo disordinata e incasinata
    **/
    public static void setStatsValues(Context context, View view, Double velocity){
        TextView velocityTextView = view.findViewById(R.id.veloV);
        TextView accelerationTextView = view.findViewById(R.id.acceV);
        TextView distanceTextView = view.findViewById(R.id.distV);
        TextView forceTextView = view.findViewById(R.id.forzV);
        TextView caloriesTextView = view.findViewById(R.id.caloV);
        Chronometer chronometer = view.findViewById(R.id.chronometer);
        velocityTextView.setText(String.valueOf(velocity));
        double prec_dest = Double.parseDouble((String) distanceTextView.getText());
        distanceTextView.setText(String.valueOf(roundTo2(prec_dest+velocity)));
        int elapsedMillis = (int)(SystemClock.elapsedRealtime() - chronometer.getBase());
        double prec_cal = Double.parseDouble((String) caloriesTextView.getText());
        caloriesTextView.setText(String.valueOf(roundTo2(calculateCalories(context, velocity,elapsedMillis) + prec_cal)));
    }
    public static double roundTo2(double num){
        return Math.round(num*100.0)/100.0;
    }
    public static double calculateCalories(Context context, double velms, int elapsedMillis){
        double ore = (double)1000/3600000;
        double velkm = velms*3.6;
        double met = 0.0862*Math.pow(velkm,3)-0.7847*Math.pow(velkm, 2)+2.5322*velkm - 0.0096;
        double peso = getPreference(context, "peso", 0f);
        return met*peso*ore;
    }
    public static void setPreference(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void setPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void setPreference(Context context, String key, Float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static String getPreference(Context context, String key, String def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, def);
    }
    public static Float getPreference(Context context, String key, Float def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, def);
    }
    public static Boolean getPreference(Context context, String key, Boolean def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, def);
    }

}
