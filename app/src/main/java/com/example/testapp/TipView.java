package com.example.testapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipView extends LinearLayout {
    public TipView(Context context) {
        super(context);
        init();
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tip, this);
        TextView tipTextView = findViewById(R.id.tip_textview);
        tipTextView.setText(Utilities.randomTips(getContext()));
    }
}