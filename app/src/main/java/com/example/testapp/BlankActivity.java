package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.testapp.Fragments.Pages.InfoFragment;
import com.example.testapp.Fragments.Pages.RaccoFragment;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        Fragment fragment;
        // TODO: aggiungere altri fragment e fare si che il default riporti alla MainActivity
        switch (getIntent().getExtras().getInt("fragment_id")) {
            case 0:
                fragment = new InfoFragment();
                break;
            default:
                fragment = new RaccoFragment();
                break;
        }
        setFragment(fragment);
    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
    }
}