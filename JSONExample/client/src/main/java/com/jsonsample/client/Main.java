package com.jsonsample.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			Socket socket = new Socket("localhost", 4545);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			JSONObject toSend = new JSONObject();

			toSend.put("Type", "Controller");
			toSend.put("Request", "Top Ten");
			
			out.println(toSend.toString());
			
			String received = in.readLine();
			System.out.print(received);
			
			toSend = new JSONObject();
			toSend.put("Command", "shutdown");			
			out.println(toSend.toString());
			out.flush();
			
			out.close();
			in.close();
			socket.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		

	}

}
