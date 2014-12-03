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
			
			for(int i = 0; i <1; i++){
				
			
				JSONObject toSend = new JSONObject();
				toSend.put("Type", "Controller");
				toSend.put("Request", "Search");
				toSend.put("Search", "Parade");
				
				
				out.println(toSend.toString());
				
				String received = in.readLine();
				System.out.println(received);
			}
			
			JSONObject toSend = new JSONObject();
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
