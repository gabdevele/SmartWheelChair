package com.example.testapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
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
    public static void setStatsValues(View view, Double velocity){
        TextView velocityTextView = view.findViewById(R.id.veloV);
        TextView accelerationTextView = view.findViewById(R.id.acceV);
        TextView distanceTextView = view.findViewById(R.id.distV);
        TextView forceTextView = view.findViewById(R.id.forzV);
        TextView caloriesTextView = view.findViewById(R.id.caloV);
        velocityTextView.setText(String.valueOf(velocity));
    }
}
