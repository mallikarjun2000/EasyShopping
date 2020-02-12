package com.example.android.easyshopping;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MessageSender extends AsyncTask<String, Void, Void> {
    Socket s;
    PrintWriter pw;
    DataOutputStream dos;
    @Override
    protected Void doInBackground(String... voids) {
        String msg = voids[0];
        try {
            s = new Socket("192.168.0.5",1234);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(msg);
            pw.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
