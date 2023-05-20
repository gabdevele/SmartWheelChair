package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utilities {
    public static String randomTips(Context context) {
        String[] tips = context.getResources().getStringArray(R.array.suggerimenti);
        return tips[new Random().nextInt(tips.length)];
    }
    public static boolean checkPermission(Context context, String permission){
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void setStatsValues(View view, double velocity, float peso){
        Map<String, Double> conversioni = new HashMap<>();
        conversioni.put("km/h", 3.6);
        conversioni.put("km", 0.001);
        conversioni.put("kCal", 0.001);

        TextView veloV = view.findViewById(R.id.veloV);
        Double veloMol = conversioni.get(
                (String) ((TextView) view.findViewById(R.id.veloU))
                        .getText()
        );
        veloMol = ((veloMol != null) ? veloMol : 1);
        TextView distV = view.findViewById(R.id.distV);
        Double distMol = conversioni.get(
                (String) ((TextView) view.findViewById(R.id.distU))
                        .getText()
        );
        distMol = ((distMol != null) ? distMol : 1);
        TextView caloV = view.findViewById(R.id.caloV);
        Double caloMol = conversioni.get(
                (String) ((TextView) view.findViewById(R.id.caloU))
                        .getText()
        );
        caloMol = ((caloMol != null) ? caloMol : 1);
        TextView acceV = view.findViewById(R.id.acceV);
        TextView forzV = view.findViewById(R.id.forzV);

        double pesoTotale = (peso+16.0);
        double oldV = Double.parseDouble((String) veloV.getText());

        veloV.setText(String.valueOf(velocity * veloMol));
        updateStat(distV, velocity, distMol);
        updateStat(caloV, calculateCalories(velocity,peso), caloMol);
        double accelerazione = (velocity-(oldV / veloMol))/2;
        acceV.setText(String.valueOf(roundTo(accelerazione, 2)));
        /**
         * 16 è il peso della sedia a rotelle
         * 0.02 è il coefficiente di attrito scelto
         * 9.81 è la forza di gravità
         * pesoTotale = (peso_persona+peso_sedia)
         *
         * Formula: pesoTotale*accelerazione+pesoTotale*gravità+coefficiente
         */
        forzV.setText(String.valueOf(roundTo(pesoTotale*accelerazione + pesoTotale*9.81*0.02, 2)));
    }
    private static void updateStat(TextView v, Double newValue, double mul) {
        double result = (
                (Double.parseDouble(
                        (String) v.getText()
                ) / mul) + newValue
        ) * mul;
        if (mul != 0.001) result = roundTo(result, 2);
        else result = roundTo(result, 5);
        v.setText(
                String.valueOf(result)
        );
    }
    public static double roundTo(double value, int to) {
        return Math.round(value * Math.pow(10, to))/Math.pow(10, to);
    }
    public static double calculateCalories(double velms, float peso){
        double ore = (double)1000/3600000;
        double velkm = velms*3.6;
        double met = 0.0862*Math.pow(velkm,3)-0.7847*Math.pow(velkm, 2)+2.5322*velkm - 0.0096;
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
    public static void setPreference(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static String getPreference(Context context, String key, String def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, def);
    }
    public static float getPreference(Context context, String key, float def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, def);
    }
    public static Boolean getPreference(Context context, String key, Boolean def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, def);
    }

}
