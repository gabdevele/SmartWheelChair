package com.example.testapp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class ArduinoBluetooth {
    final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private InputStream inputStream;
    public boolean permissionGranted;
    final Context context;
    private static final UUID SERVICE_ID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //SPP UUID
    private static final String ADDRESS = "98:D3:31:F5:2E:C7"; // HC-05 BT ADDRESS

    public ArduinoBluetooth(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.permissionGranted = Utilities.checkPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT);
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void connectToArduino() throws Exception {
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(ADDRESS);
        try {
            if (!Utilities.checkPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT)) {
                throw new Exception("Permessi non concessi");
            }
            socket = hc05.createInsecureRfcommSocketToServiceRecord(SERVICE_ID);
            socket.connect();
            inputStream = socket.getInputStream();
        } catch (IOException e){
            disconnectArduino();
            throw new Exception("Errore di connessione");
        }
    }
    public void disconnectArduino(){
        try{
            if(socket.isConnected())
                socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public double readData() throws Exception {
        final byte delimiter = 10;
        int readBufferPosition = 0;
        byte[] readBuffer = new byte[1024];
        try{
            int bytesAvailable = inputStream.available();
            if(bytesAvailable > 0){
                byte[] packetBytes = new byte[bytesAvailable];
                inputStream.read(packetBytes);
                for(int i=0; i<bytesAvailable; i++){
                    byte b = packetBytes[i];
                    if(b == delimiter){
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        String data = new String(encodedBytes, StandardCharsets.US_ASCII);
                        readBufferPosition = 0;
                        if(data.length() > 2)
                            try{
                                return Double.parseDouble(data);
                            }catch (NumberFormatException e){
                                throw new Exception("Errore nel parsing dei dati da Arduino");
                            }
                    }else{
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }
        } catch (IOException e) {
            disconnectArduino();
            throw new Exception("Errore nella lettura dei dati da Arduino");
        }
        return 0;
    }

}
