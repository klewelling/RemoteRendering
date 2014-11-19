package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.simple.JSONObject;



public class MediaController  extends Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    MediaService mediaService;
    public MediaController(Socket socket,MediaService mediaService){
        this.socket = socket;
        this.mediaService = mediaService;
    }

    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (!in.ready()){
                ;
            }
            String js = in.readLine();
            while(true){
                if(in.ready()){
                    String input = in.readLine();
                    System.out.println(input);
                    out.println("hello");
                }

            }
        }catch (IOException e) {
            System.out.println(e);
        }finally {

        }
    }
}
