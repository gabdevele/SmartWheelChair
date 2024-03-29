package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.testapp.databinding.ActivityMainBinding;
import com.example.testapp.fragments.ActivityFragment;
import com.example.testapp.fragments.HomeFragment;
import com.example.testapp.fragments.SettingsFragment;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utilities.getPreference(this, "new_start", true)) {
            startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setFragment(new HomeFragment());
            Utilities.setPreference(this, "navigation_enabled", true);
            binding.bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
        }
    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        if(!Utilities.getPreference(this,"navigation_enabled", true)){
            return false;
        }
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            setFragment(new HomeFragment());
        } else if (itemId == R.id.navigation_activity) {
            setFragment(new ActivityFragment());
        } else if (itemId == R.id.navigation_settings) {
            setFragment(new SettingsFragment());
        }
        return true;
    }
}