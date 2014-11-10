package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class MediaServer {
    Map media = new Hashtable();

    private static final int PORT = 4545;
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new MediaController(listener.accept()).start();
            }
        } finally{
            listener.close();
        }

    }
}
