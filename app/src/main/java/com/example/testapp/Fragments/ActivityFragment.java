package com.example.testapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import com.example.testapp.R;
import com.example.testapp.Threads.ArduinoConnectBluetooth;
import com.example.testapp.Threads.ArduinoReadBluetooth;
import com.example.testapp.Utilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ActivityFragment extends Fragment {

    private Button startButton;
    private boolean errorReadingData = false;
    private final int delay = 1000;
    private Handler readHandler;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private Chronometer chronometer;
    private Float peso;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        startButton = view.findViewById(R.id.button);
        readHandler = new Handler(Looper.getMainLooper());
        startButton.setOnClickListener(this::startHandler);
        chronometer = view.findViewById(R.id.chronometer);
        loadUnit(view.findViewById(R.id.caloU), "calorie", R.string.unita_kcal);
        loadUnit(view.findViewById(R.id.veloU), "velocita", R.string.unita_velocita);
        loadUnit(view.findViewById(R.id.distU), "distanza", R.string.unita_metri);
        peso = Utilities.getPreference(getContext(), "peso", 0f);

        // Solo per test
        view.findViewById(R.id.test_btt).setOnClickListener(l -> {
            Utilities.setStatsValues(getView(), 0.51, peso);
        });

        return view;
    }

    private void loadUnit(@NonNull TextView view, String id, int defaultId) {
        view.setText(Utilities.getPreference(getContext(), id, getString(defaultId)));
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (!result) {
                    Toast.makeText(getContext(), R.string.permessi_necessari, Toast.LENGTH_SHORT);
                }
            }
    );
    private final ActivityResultLauncher<Intent> requestEnableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), R.string.bluetooth_abilitato, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), R.string.bluetooth_necessario, Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void setBottomNavigation(boolean enable) {
        Utilities.setPreference(getContext(), "navigation_enabled", enable);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }

    private void startHandler(View v) {
        if (!Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)) {
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            return;
        }
        // TODO: usare BluetoothManager.getAdapter() dato che questo è obsoleto?
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestEnableBluetoothLauncher.launch(enableBtIntent);
            return;
        }
        Toast.makeText(getContext(), R.string.connessione_in_corso, Toast.LENGTH_LONG).show();
        setBottomNavigation(false);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new ArduinoConnectBluetooth(getContext(), (s, inputStream, e) -> {
            if (e != null) {
                getActivity().runOnUiThread(
                        () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }else {
                getActivity().runOnUiThread(
                        () -> Toast.makeText(getContext(), R.string.connessione_effettuata, Toast.LENGTH_SHORT).show()
                );
                startButton.setText(R.string.termina_allenamento);
                startButton.setOnClickListener(this::stopHandler);
                this.bluetoothSocket = s;
                this.inputStream = inputStream;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                handler.postDelayed(this::readHandler, delay);
            }
            getActivity().runOnUiThread(
                    () -> setBottomNavigation(true)
            );
        }, handler));
    }

    private void readHandler() {
        if (!errorReadingData) {
            try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executorService.execute(new ArduinoReadBluetooth(bluetoothSocket, inputStream, (v, e) -> {
                if (e != null) {
                    getActivity().runOnUiThread(
                        () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                } else {
                    getActivity().runOnUiThread(
                        () -> {
                            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                            bottomNavigationView.setOnItemSelectedListener(null);
                            Utilities.setStatsValues(getView(), v, peso);
                        }
                    );
                }
            }, handler));
            readHandler.postDelayed(this::readHandler, delay);
            } catch (Exception e) {
                errorReadingData = true;
                getActivity().runOnUiThread(
                        () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }
    }

    private void stopHandler(View v) {
        startButton.setText(R.string.inizia_allenemento);
        startButton.setOnClickListener(this::startHandler);
        readHandler.removeCallbacks(this::readHandler);
        chronometer.stop();
        try {
            bluetoothSocket.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}