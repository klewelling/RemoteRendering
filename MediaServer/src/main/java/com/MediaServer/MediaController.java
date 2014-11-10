package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import static com.MediaServer.JsonUtil.*;
public class MediaController  extends Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public MediaController(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while(true){
                if(in.ready()){
                    String input = in.readLine();
                }

            }
        }catch (IOException e) {
            System.out.println(e);
        }finally {

        }
    }
}
