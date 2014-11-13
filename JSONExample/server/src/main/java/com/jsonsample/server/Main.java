package com.jsonsample.server;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerThread sthread = new ServerThread();
		Thread serverThread = new Thread(sthread);
		serverThread.start();

		//give thread time to bind to socket
		try{
			Thread.sleep(1000);
		}catch(Exception ex){}
		
		while(sthread.isRunning()){
			try{
				serverThread.join(1000);
			}catch(Exception ex){
				ex.printStackTrace();				
			}
		}
				
	}

	
	
}
