package com.jsonsample.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
	
	private ServerSocket serverSocket;
	
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(6000);
			System.out.println("SocketServer started");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		while (!Thread.currentThread().isInterrupted()
				&& !serverSocket.isClosed()) {
			try {
				socket = serverSocket.accept();

				System.out.println("Socket connected");

				CommunicationThread commThread = new CommunicationThread(socket, this);
				new Thread(commThread).start();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isRunning(){
		return serverSocket != null && !serverSocket.isClosed();
	}
	
	public void close(){
		if(serverSocket != null){
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
