package com.example.wengturtle.arphone;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

interface NetHandler {
    void onRecvData(String data);
}

class WriteBytesRunnable implements Runnable {
    DataOutputStream os;
    private byte[] data;
    private int size;
    WriteBytesRunnable(DataOutputStream os, byte[] data, int size) {
        this.os = os;
        this.data = data;
        this.size = size;
    }
    public void run() {
        try {
            os.write(data, 0, size);
        }
        catch(Exception e) {
            System.out.println("Error: send message failed.");
            e.printStackTrace();
        }
    }
}

public class Net {
    private static Net net;

    private String ip;
    private  int port;
    private Socket socket;
    private DataOutputStream os;
    private BufferedReader is;

    public NetHandler delegate;

    private Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (socket != null) {
                    socket.close();
                }
                socket = new Socket(ip, port);
                os = new DataOutputStream(socket.getOutputStream());
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                new Thread(receiveRunnable).start();

            }
            catch(Exception e) {
                System.out.println("Error: create socket failed.");
                e.printStackTrace();
            }
        }
    };

    private Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                socket.close();
                net = null;
            }
            catch(Exception e) {
                System.out.println("Error: close socket failed.");
                e.printStackTrace();
            }
        }
    };

    private Runnable receiveRunnable = new Runnable() {
        @Override
        public void run() {
            while(true){

                try{
                    if(socket.isClosed() || socket == null) { continue; }

                    String response = is.readLine();

                    if(response == null) { continue; }

                    Log.d("YueTing",response);

                    if(delegate!=null)
                        delegate.onRecvData(response);


                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };


    public Net(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static Net getInstance(String ip, int port) {
        if (net == null) {
            net = new Net(ip, port);
        }
        return net;
    }

    public static Net getInstance() {
        return net;
    }

    public void close() {
        new Thread(closeRunnable).start();
    }

    public void connect() {
        new Thread(connectRunnable).start();
    }

    public boolean connected() {
        return socket != null && socket.isConnected();
    }

    public void sendBytes(byte[] data, int size) {
        Thread thread = new Thread(new WriteBytesRunnable(os, data, size));
        thread.start();
        try {
            thread.join();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendByte(byte num) {
        byte[] data = new byte[1];
        data[0] = num;
        sendBytes(data, 1);
    }

    public void sendShort(short num) {
        byte[] data = new byte[2];
        data[0] = (byte)(num & 0xff);
        data[1] = (byte)((num >> 8) & 0xff);
        sendBytes(data, 2);
    }

    public void sendInt(int num) {
        byte[] data = new byte[4];
        data[0] = (byte)(num & 0xff);
        data[1] = (byte)((num >> 8) & 0xff);
        data[2] = (byte)((num >> 16) & 0xff);
        data[3] = (byte)((num >> 24) & 0xff);
        sendBytes(data, 4);
    }

    public void sendDouble(double num) {
        byte[] data = new byte[8];
        long value = Double.doubleToRawLongBits(num);
        for(int i = 0; i < 8; i++) {
            data[i] = (byte)((value >> (i << 3)) & 0xff);
        }
        sendBytes(data, 8);
    }

    public void sendString(String str, int size) {
        sendBytes(str.getBytes(), str.length());
        for(int i = 0; i < size - str.length(); i++) {
            sendByte((byte)0);
        }
    }

    public void sendString(String str) {
        final String str1 = str + "\n";
        try {
            sendBytes(str1.getBytes("utf-8"), str1.length());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
