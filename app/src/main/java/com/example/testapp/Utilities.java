package com.example.testapp;

import android.content.Context;
import android.content.pm.PackageManager;

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
}
