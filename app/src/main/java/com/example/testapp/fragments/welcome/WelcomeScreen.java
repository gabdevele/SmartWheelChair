package com.example.testapp.fragments.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;

public class WelcomeScreen extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.next).setOnClickListener(v -> getParentFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.by_right, R.anim.to_left)
            .replace(R.id.fragments, new WelcomePermessi())
            .addToBackStack(null)
            .commit());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_screen, container, false);
    }
}