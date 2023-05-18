package com.example.testapp.Fragments.Welcome;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.Utilities;

public class WelcomePeso extends Fragment {

    public WelcomePeso() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.chiudi).setOnClickListener(l -> {
            String peso = ""+((EditText) view.findViewById(R.id.pesoInput)).getText();
            if (peso.length() != 0) {
                Utilities.setPreference(getContext(), "peso", Float.parseFloat(peso));
            }
            Utilities.setPreference(getContext(), "new_start", false);
            startActivity(new Intent(getContext(), MainActivity.class));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_peso, container, false);
    }
}