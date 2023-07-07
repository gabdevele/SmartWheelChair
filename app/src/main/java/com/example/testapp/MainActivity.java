package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.testapp.fragments.ActivityFragment;
import com.example.testapp.fragments.HomeFragment;
import com.example.testapp.fragments.SettingsFragment;
import com.example.testapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utilities.getPreference(this,"new_start", true)) {
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
    }
}