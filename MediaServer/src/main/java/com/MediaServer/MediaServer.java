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
import java.util.Map;

public class MediaServer {
    
    private static final int PORT = 4545;
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try {
            MediaService mediaService = new MediaService("/Users/alexbiju/Desktop/Musi");

            while (true) {
                new MediaController(listener.accept(),mediaService).start();
            }
        } finally{
            listener.close();
        }

    }
}
