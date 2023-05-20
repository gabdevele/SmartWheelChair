package com.example.testapp.Threads;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArduinoReadBluetooth implements Runnable{

    private final AsyncResponse delegate;
    private final Handler handler;
    private final BluetoothSocket socket;
    private final InputStream inputStream;

    public interface AsyncResponse{
        void processFinish(Double velocity, Exception e);
    }

    public ArduinoReadBluetooth(BluetoothSocket socket, InputStream inputStream, AsyncResponse delegate, Handler handler){
        this.socket = socket;
        this.inputStream = inputStream;
        this.delegate = delegate;
        this.handler = handler;
    }
    private void disconnectArduino(){
        try{
            if(socket.isConnected())
                socket.close();
        } catch (IOException e){}
    }
    public Double readData() throws Exception{
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
                        if(data.length() > 2){
                            try{
                                return Double.parseDouble(data);
                            }catch (NumberFormatException e){
                                throw new Exception("Errore nel parsing dei dati da Arduino");
                            }
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
        return 0.0;
    }
    @Override
    public void run() {
        try{
            delegate.processFinish(readData(), null);
        } catch (Exception e) {
            delegate.processFinish(null, e);
        }
    }
}
