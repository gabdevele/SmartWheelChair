package com.example.testapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;
import com.example.testapp.Utilities;

public class InfoFragment extends Fragment {
    public InfoFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_informazioni);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utilities.makeLinkClickable(view.findViewById(R.id.spam_gabriele));
        Utilities.makeLinkClickable(view.findViewById(R.id.spam_renato));
        Utilities.makeLinkClickable(view.findViewById(R.id.spam_app));
        Utilities.makeLinkClickable(view.findViewById(R.id.spam_scuola));
        Utilities.makeLinkClickable(view.findViewById(R.id.spam_ricerca));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
}