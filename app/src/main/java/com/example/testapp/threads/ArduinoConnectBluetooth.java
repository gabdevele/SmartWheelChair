package com.example.testapp.threads;

import static android.Manifest.permission.BLUETOOTH_CONNECT;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.example.testapp.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ArduinoConnectBluetooth implements Runnable {
    private Context context;
    private final AsyncResponse delegate;
    private static final UUID SERVICE_ID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String ADDRESS;
    private BluetoothSocket socket;
    private InputStream inputStream;

    public interface AsyncResponse {
        void processFinish(BluetoothSocket socket, InputStream inputStream, Exception e);
    }

    public ArduinoConnectBluetooth(Context context, AsyncResponse delegate, Handler handler) {
        this.context = context;
        this.delegate = delegate;
        this.ADDRESS = Utilities.getPreference(context, "mac","00:18:E4:34:C7:1A");
    }

    private void connectToArduino() throws Exception {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled())
            throw new Exception("Bluetooth non attivo");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!Utilities.checkPermission(context, BLUETOOTH_CONNECT))
                throw new Exception("Permessi non concessi");
        } else {
            if (!Utilities.checkPermission(context, Manifest.permission.BLUETOOTH) || !Utilities.checkPermission(context, Manifest.permission.BLUETOOTH_ADMIN))
                throw new Exception("Permessi non concessi");
        }

        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(ADDRESS);
        try{
            socket = hc05.createRfcommSocketToServiceRecord(SERVICE_ID);
            socket.connect();
            inputStream = socket.getInputStream();
        } catch (Exception e) {
            disconnectArduino();
            throw new Exception("Errore di connessione");
        }
    }

    private void disconnectArduino(){
        try{
            if(socket.isConnected())
                socket.close();
        } catch (IOException ignored){}
    }

    @Override
    public void run() {
        try{
            connectToArduino();
            delegate.processFinish(socket, inputStream,null);
        } catch (Exception e){
            delegate.processFinish(null, null, e);
        }
    }
}
