package com.example.testapp.Fragments.Welcome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.Utilities;

public class WelcomePermessi extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.concedi).setOnClickListener(l -> {
            if(!Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)){
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            }
            else {
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.by_right, R.anim.to_left)
                        .replace(R.id.fragments, new WelcomePeso())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_permessi, container, false);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            r -> {
                if (r) {
                    getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.by_right, R.anim.to_left)
                        .replace(R.id.fragments, new WelcomePeso())
                        .addToBackStack(null)
                        .commit();
                } else {
                    Toast.makeText(getContext(), "Per favore, accettare i permessi", Toast.LENGTH_SHORT);
                }
            }
    );
}