package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ActivityFragment extends Fragment {

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.accelerazione);
        String text = "0 m/s2";
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(
                superscriptSpan,
                text.indexOf("2"),
                text.indexOf("2") + String.valueOf("2").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }
}