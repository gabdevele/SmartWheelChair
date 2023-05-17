package com.example.testapp.Fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
    private final int REQUEST_ENABLE_BT = 1;
    private TextView velocityTextView;
    private double velocity;
    private Handler readHandler;
    private View.OnClickListener startActivity;
    private Runnable readActivity;
    private View.OnClickListener stopActivity;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    public ActivityFragment() {
        startActivity = (v -> {
            //TODO check if bluetooth is enabled

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
        velocityTextView = view.findViewById(R.id.veloV);
        readHandler = new Handler(Looper.getMainLooper());
        startButton.setOnClickListener(startActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    public void enableBluetooth(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getContext(), "Bluetooth abilitato!", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Bluetooth è necessario per il funzionamento!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}