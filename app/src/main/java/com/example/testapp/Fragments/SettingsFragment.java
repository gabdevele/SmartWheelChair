package com.example.testapp.Fragments;

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

import com.example.testapp.R;
import com.example.testapp.Utilities;

import java.util.Arrays;


public class SettingsFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    /**
     * Crea un nuovo watcher per le impostazioni
     */
    private TextWatcher makeWatcher(String id) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    Utilities.setPreference(getContext(), id, Float.parseFloat(s.toString()));
                }
            }
        };
    }
    /**
     * Crea un nuovo listener per le impostazioni
     */
    private AdapterView.OnItemSelectedListener makeSelectedListener(String id, String[] impostazioni) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Utilities.setPreference(getContext(), id, impostazioni[(int)l]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Questo serve per forza anche se vuoto
            }
        };
    }
    /**
     * Imposta lo spinner con i dati, così da occupare meno spazio
     */
    private void setupSpinner(String[] unita, String id, Spinner spinner) {
        String calorie = sharedPreferences.getString(id, unita[0]);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, unita);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(makeSelectedListener(id, unita));
        spinner.setAdapter(adapter);
        spinner.setSelection(Arrays.asList(unita).indexOf(calorie));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        EditText pesoInput = view.findViewById(R.id.pesoInput);
        float peso = sharedPreferences.getFloat("peso", 0);
        pesoInput.addTextChangedListener(makeWatcher("peso"));
        pesoInput.setText(String.valueOf(peso));

        setupSpinner(new String[]{getString(R.string.tema_normale)}, "tema", view.findViewById(R.id.temaSpinner));
        setupSpinner(new String[]{getString(R.string.unita_kcal), getString(R.string.unita_calorie)}, "calorie", view.findViewById(R.id.calorieSpinner));
        setupSpinner(new String[]{getString(R.string.unita_kmorari), getString(R.string.unita_velocita)}, "velocita", view.findViewById(R.id.velocitaSpinner));
        setupSpinner(new String[]{getString(R.string.unita_km), getString(R.string.unita_metri)}, "distanza", view.findViewById(R.id.distanzaSpinner));

        return view;
    }
}