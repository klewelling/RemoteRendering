package com.jsonsample.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;
import org.json.JSONTokener;

public class client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			Socket socket = new Socket("localhost", 6000);
			System.out.println("Connected to port 6000");
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			JSONObject toSend = new JSONObject();
			toSend.put("Artist", "Taylor Swift");
			toSend.put("Album", "1989");
			out.println(toSend.toString());
			
			
			String received = in.readLine();
			JSONObject jsonReceived = new JSONObject(new JSONTokener(received));
			String soldString = jsonReceived.getString("sold");
			System.out.println("Taylor Swift's album 1989 sold: " + soldString + " copies.");
			Thread.sleep(10000);
			toSend = new JSONObject();
			toSend.put("Command", "shutdown");			
			out.println(toSend.toString());
			out.flush();
			
			socket.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		

	}

}