package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class MediaController  implements Runnable{
	private ServerSocket serverSocket;
    public void run(){
    	
    	Socket socket = null;
    	
    	try {
			serverSocket = new ServerSocket(4545);
			System.out.println("Server Started");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	while(!Thread.currentThread().isInterrupted() && !serverSocket.isClosed()){
    		try {
				socket = serverSocket.accept();
				System.out.println("Started socket");
				
				MediaService media = new MediaService("/Users/alexbiju/Desktop/Musi",this,socket);
				new Thread(media).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	if(serverSocket != null){
        	try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

