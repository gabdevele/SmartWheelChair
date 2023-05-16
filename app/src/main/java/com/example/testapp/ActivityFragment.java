package com.example.testapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityFragment extends Fragment {

    private Button startButton;
    private ArduinoBluetooth arduinoBluetooth;
    private Intent enableBtIntent;
    private Handler handler;
    private boolean errorReadingData = false;
    private final int delay = 1000;
    private Runnable startActivity;
    private Runnable readActivity;
    private Runnable stopActivity;

    private TextView velocityTextView;
    private double velocity;
    public ActivityFragment() {
        startActivity = () -> {
            if(arduinoBluetooth.permissionGranted){
                if(!arduinoBluetooth.isBluetoothEnabled()){
                    enableBluetooth();
                }else{
                    try{
                        arduinoBluetooth.connectToArduino();
                        handler.postDelayed(readActivity, delay);
                        startButton.setText("Termina allenamento");
                        startButton.setOnClickListener(v -> handler.post(stopActivity));
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                requestPermission();
            }
        };
        readActivity = () -> {
            if(!errorReadingData){
                try {
                    velocity = arduinoBluetooth.readData();
                    velocityTextView.setText(velocity + " m/s");
                    handler.postDelayed(readActivity, delay);
                } catch (Exception e) {
                    errorReadingData = true;
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        stopActivity = () -> {
            arduinoBluetooth.disconnectArduino();
            handler.removeCallbacks(readActivity);
            handler.removeCallbacks(stopActivity);
            errorReadingData = false;
        };
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arduinoBluetooth = new ArduinoBluetooth(getContext());
        handler = new Handler();
        startButton = view.findViewById(R.id.button);
        velocityTextView = view.findViewById(R.id.velocita);
        startButton.setOnClickListener(v -> handler.post(startActivity));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }


    public void requestPermission(){
        requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, arduinoBluetooth.REQUEST_ENABLE_BT);
    }

    public void enableBluetooth(){
        enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, arduinoBluetooth.REQUEST_ENABLE_BT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == arduinoBluetooth.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getContext(), "Bluetooth abilitato!", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Bluetooth è necessario per il funzionamento!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == arduinoBluetooth.REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                arduinoBluetooth.permissionGranted = true;
                if (!arduinoBluetooth.isBluetoothEnabled()) {
                    enableBluetooth();
                }
            }
        }
    }
}