package com.example.testapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.BlankActivity;
import com.example.testapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    PieChart pieChart;
    final int[] colors = new int[]{R.color.blue3, R.color.cold_white};
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.pieChart);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawMarkers(false);
        pieChart.setTouchEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);

        pieChart.setCenterText("30%");
        pieChart.setCenterTextSize(20);
        PieDataSet pieDataSet = new PieDataSet(dataValues(), "Obiettivo");
        pieDataSet.setColors(colors, getContext());
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        view.findViewById(R.id.go_activity).setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActivityFragment()).commit());
        view.findViewById(R.id.info_continue).setOnClickListener(l -> {
            Intent intent = new Intent(getContext(), BlankActivity.class);
            intent.putExtra("fragment_id", 0);
            startActivity(intent);
        });
    }

    private ArrayList<PieEntry> dataValues(){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry(30, ""));
        dataVals.add(new PieEntry(70, ""));
        return dataVals;
    }
}