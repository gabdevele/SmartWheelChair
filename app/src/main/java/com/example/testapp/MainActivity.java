package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.testapp.Fragments.ActivityFragment;
import com.example.testapp.Fragments.HomeFragment;
import com.example.testapp.Fragments.SettingsFragment;
import com.example.testapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("new_start", true)) {
            startActivity(new Intent(this, WelcomeActivity.class));
        } else {

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFragment(new HomeFragment());
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(new HomeFragment());
                    break;
                case R.id.navigation_activity:
                    setFragment(new ActivityFragment());
                    break;
                case R.id.navigation_settings:
                    setFragment(new SettingsFragment());
                    break;
            }
            return true;
        });

        }
    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}