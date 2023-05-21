package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.testapp.Fragments.InfoFragment;

/**
 * Usata per visualizzare pagine minori,
 * passare un int Extra di chiave "fragment_id"
 * come id del fragment e poi aggiungerlo allo switch case
 */
public class BlankActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Fragment fragment;
        switch (getIntent().getExtras().getInt("fragment_id")) {
            case 0:
                fragment = new InfoFragment();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}