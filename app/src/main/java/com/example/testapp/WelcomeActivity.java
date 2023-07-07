package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.testapp.fragments.welcome.WelcomeScreen;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        setFragment(new WelcomeScreen());
    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
    }
}