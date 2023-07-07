package com.example.testapp.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
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
import java.util.Objects;

public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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
     * Crea un nuovo watcher per le impostazioni,
     * per il MAC input
     */
    private TextWatcher makeWatcher(int nextElement, int[] elements) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.isEmpty()) return;
                if (text.length() == 2) { // Se il mac è stato completato
                    if (nextElement == -1) {
                        String[] testi = Arrays.stream(elements).mapToObj(l -> {
                                    if (l == -1) return "";
                                    return ((EditText) getView().findViewById(l)).getText().toString().toUpperCase();
                                }
                        ).toArray(String[]::new);

                        Utilities.setPreference(getContext(), "mac", String.join(":", Arrays.copyOf(testi, testi.length-1)));
                        return;
                    }
                    getView().findViewById(nextElement).requestFocus();
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
        String calorie = Utilities.getPreference(getContext(), id, unita[0]);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, unita);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(makeSelectedListener(id, unita));
        spinner.setAdapter(adapter);
        spinner.setSelection(Arrays.asList(unita).indexOf(calorie));
    }

    private boolean firstStart = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        int[] mac_ids = { R.id.mac_1, R.id.mac_2, R.id.mac_3, R.id.mac_4, R.id.mac_5, R.id.mac_6, -1 };
        this.firstStart = true;

        EditText pesoInput = view.findViewById(R.id.pesoInput);
        float peso = Utilities.getPreference(getContext(), "peso", 0f);
        pesoInput.addTextChangedListener(makeWatcher("peso"));
        pesoInput.setText(String.valueOf(peso));

        setupSpinner(new String[]{getString(R.string.tema_normale)}, "tema", view.findViewById(R.id.temaSpinner));
        setupSpinner(new String[]{getString(R.string.unita_kcal), getString(R.string.unita_calorie)}, "calorie", view.findViewById(R.id.calorieSpinner));
        setupSpinner(new String[]{getString(R.string.unita_kmorari), getString(R.string.unita_velocita)}, "velocita", view.findViewById(R.id.velocitaSpinner));
        setupSpinner(new String[]{getString(R.string.unita_km), getString(R.string.unita_metri)}, "distanza", view.findViewById(R.id.distanzaSpinner));

        SwitchCompat macSwitch = view.findViewById(R.id.switch_mac);
        String mac = Utilities.getPreference(getContext(), "mac", "00:18:E4:34:C7:1A");
        macSwitch.setOnCheckedChangeListener((v, checked) -> {
            String newMac = checked ? "98:D3:31:F5:2E:C7" : "00:18:E4:34:C7:1A";
            if (!firstStart) {
                for (int i = 0; i < mac_ids.length; i++) {
                    if (i == 6) break;
                    EditText mac_view = view.findViewById(mac_ids[i]);
                    mac_view.setText(newMac.split(":")[i]);
                }
                Utilities.setPreference(getContext(), "mac", newMac);
            }
            this.firstStart = false;
        });

        if (!Objects.equals(mac, "00:18:E4:34:C7:1A")) macSwitch.toggle();

        String[] mac_elements = mac.split(":");

        for (int i = 0; i < mac_ids.length; i++) {
            if (i == 6) break;
            EditText mac_view = view.findViewById(mac_ids[i]);
            mac_view.setText(mac_elements[i]);
            mac_view.addTextChangedListener(makeWatcher(mac_ids[i+1], mac_ids));
        }

        return view;
    }

}