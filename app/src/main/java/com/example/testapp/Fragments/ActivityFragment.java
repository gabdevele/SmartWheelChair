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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.Threads.ArduinoConnectBluetooth;
import com.example.testapp.Threads.ArduinoReadBluetooth;
import com.example.testapp.Utilities;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ActivityFragment extends Fragment {

    private Button startButton;
    private boolean errorReadingData = false;
    private final int delay = 1000;
    private Handler readHandler;
    private View.OnClickListener startActivity;
    private Runnable readActivity;
    private View.OnClickListener stopActivity;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    public ActivityFragment() {
        startActivity = (v -> {
            if(!Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)){
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
                return;
            }
            if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                requestEnableBluetoothLauncher.launch(enableBtIntent);
                return;
            }
            getActivity().runOnUiThread(
                    () -> Toast.makeText(getContext(), "Connessione in corso...", Toast.LENGTH_SHORT).show()
            );
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executorService.execute(new ArduinoConnectBluetooth(getContext(), (s, inputStream, e) -> {
                if(e != null){
                    getActivity().runOnUiThread(
                            () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }else{
                    getActivity().runOnUiThread(
                            () -> Toast.makeText(getContext(), "Connessione effettuata!", Toast.LENGTH_SHORT).show()
                    );
                    startButton.setText(R.string.termina_allenamento);
                    startButton.setOnClickListener(stopActivity);
                    this.bluetoothSocket = s;
                    this.inputStream = inputStream;
                    handler.postDelayed(readActivity, delay);
                }
            }, handler));
        });
        readActivity = () -> {
          if(!errorReadingData){
              try{
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executorService.execute(new ArduinoReadBluetooth(bluetoothSocket, inputStream, (v, e) -> {
                    if(e != null){
                        getActivity().runOnUiThread(
                                () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }else{
                        Utilities.setStatsValues(getView(), v);
                    }
                }, handler));
                readHandler.postDelayed(readActivity, delay);
              }catch(Exception e){
                  errorReadingData = true;
                  getActivity().runOnUiThread(
                          () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                  );
              }
          }
        };
        stopActivity = (v -> {
            startButton.setText(R.string.inizia_allenemento);
            startButton.setOnClickListener(startActivity);
            readHandler.removeCallbacks(readActivity);
            try{
                bluetoothSocket.close();
                readHandler.removeCallbacks(readActivity);
            }catch(Exception e){
                getActivity().runOnUiThread(
                        () -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startButton = view.findViewById(R.id.button);
        readHandler = new Handler(Looper.getMainLooper());
        startButton.setOnClickListener(startActivity);

        loadUnita(view.findViewById(R.id.caloU), "calorie", R.string.unita_kcal);
        loadUnita(view.findViewById(R.id.veloU), "velocita", R.string.unita_velocita);
        loadUnita(view.findViewById(R.id.distU), "distanza", R.string.unita_metri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    private void loadUnita(TextView view, String id, int defaultId) {
        view.setText(Utilities.getPreference(getContext(), id, getString(defaultId)));
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (!result) {
                    Toast.makeText(getContext(), "Permessi necessari!", Toast.LENGTH_SHORT);
                }
            }
    );
    private ActivityResultLauncher<Intent> requestEnableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "Bluetooth abilitato!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Bluetooth è necessario per il funzionamento!", Toast.LENGTH_SHORT).show();
                }
            }
    );

}