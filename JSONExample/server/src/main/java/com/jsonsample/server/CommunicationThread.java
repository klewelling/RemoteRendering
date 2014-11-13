package com.jsonsample.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;
import org.json.JSONTokener;

public class CommunicationThread implements Runnable{

	private ServerThread serverThread;
	private Socket clientSocket;
	private BufferedReader input;
	private PrintWriter writer;

	public CommunicationThread(Socket clientSocket, ServerThread sthread) {
		if (clientSocket == null || sthread == null) {
			throw new IllegalArgumentException();
		}

		this.serverThread = sthread;
		this.clientSocket = clientSocket;

		try {
			
			this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));			
			this.writer = new PrintWriter(this.clientSocket.getOutputStream(), true);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		try{
			while(!Thread.currentThread().isInterrupted() ){
				String received = input.readLine();
				System.out.print("Recieved: " + received);
				
				JSONObject jsonReceived = new JSONObject(new JSONTokener(received)) ;
				if(jsonReceived.has("Artist")){
					if("Taylor Swift".equalsIgnoreCase(jsonReceived.getString("Artist"))){
						JSONObject toReturn = new JSONObject();
						toReturn.put("sold", "Bazillion");
						
						writer.println(toReturn.toString());
					}
				}else if(jsonReceived.has("Command")){
					if("SHUTDOWN".equalsIgnoreCase(jsonReceived.getString("Command"))){
						serverThread.close();
						break;	
					}
				}
			}
			
			clientSocket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		

	}

}
