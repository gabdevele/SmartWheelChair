package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;


public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private EditText pesoInput;
    private Spinner temaSpinner;
    private Spinner calorieSpinner;
    private Spinner velocitaSpinner;
    private Spinner distanzaSpinner;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        pesoInput = view.findViewById(R.id.pesoInput);
        temaSpinner = view.findViewById(R.id.temaSpinner);
        calorieSpinner = view.findViewById(R.id.calorieSpinner);
        velocitaSpinner = view.findViewById(R.id.velocitaSpinner);
        distanzaSpinner = view.findViewById(R.id.distanzaSpinner);

        float peso = sharedPreferences.getFloat("peso", 0);
        pesoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("peso", Float.parseFloat(s.toString()));
                editor.apply();

            }
        });
        pesoInput.setText(String.valueOf(peso));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, new String[]{"Normale"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temaSpinner.setAdapter(adapter);

        //calorie
        String[] unitaCalorie = {"kCal", "cal"};
        String calorie = sharedPreferences.getString("calorie", unitaCalorie[0]);
        calorieSpinner.setSelection(Arrays.asList(unitaCalorie).indexOf(calorie));

        ArrayAdapter<CharSequence> calorieAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, unitaCalorie);
        calorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calorieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("calorie", unitaCalorie[(int)l]);
                editor.apply();
                Toast.makeText(getContext(), "ciao", Toast.LENGTH_SHORT);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Questo serve per forza anche se vuoto
            }
        });
        calorieSpinner.setAdapter(calorieAdapter);

        // velocita
        String[] unitaVelocita = {"km/h", "m/s"};
        String velocita = sharedPreferences.getString("velocita", unitaVelocita[0]);
        velocitaSpinner.setSelection(Arrays.asList(unitaVelocita).indexOf(velocita));

        ArrayAdapter<CharSequence> velocitaAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, unitaVelocita);
        velocitaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        velocitaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("velocita", unitaVelocita[(int)l]);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Questo serve per forza anche se vuoto
            }
        });
        velocitaSpinner.setAdapter(velocitaAdapter);

        // distanza
        String[] unitaDistanza = {"km", "m"};
        String distanza = sharedPreferences.getString("distanza", unitaDistanza[0]);
        distanzaSpinner.setSelection(Arrays.asList(unitaDistanza).indexOf(distanza));

        ArrayAdapter<CharSequence> distanzaAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, unitaDistanza);
        distanzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanzaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Quindii", Toast.LENGTH_SHORT);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("distanza", unitaDistanza[(int)l]);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Questo serve per forza anche se vuoto
            }
        });

        distanzaSpinner.setAdapter(distanzaAdapter);

        return view;
    }
}