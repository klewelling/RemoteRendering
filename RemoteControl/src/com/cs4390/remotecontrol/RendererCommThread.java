package com.cs4390.remotecontrol;

import org.json.JSONObject;

import Controller.Presenter;

public class RendererCommThread extends CommunicatonThread {

	private String serverAddress;
	private String serverPort;
	private Presenter presenter;
	
	public RendererCommThread(String _serverAddress, String _serverPort){
		this.serverAddress = _serverAddress;
		this.serverPort = _serverPort;
		
		
	}
	
	@Override
	public void run() {
		try{
			while(!Thread.currentThread().isInterrupted() ){
				
				//Create a socket and get the in/out streams
				try{
					presenter = new Presenter(serverAddress, Integer.parseInt(serverPort) );	
				}catch(Exception ex){
					throw new RuntimeException(ex);
				}
				presenter.Connect();
				
				//wait for a message to send
				newMessage.acquire();
				
				JSONObject msgToSendToMediaServer = getMessageToSend();
				
				if(msgToSendToMediaServer.has(MainActivity.ID)){
					String idToSend = msgToSendToMediaServer.getString(MainActivity.ID);
					presenter.Play(idToSend);
					setMessageReceived(new JSONObject());
				}else if(msgToSendToMediaServer.has(MainActivity.PAUSE)){
					presenter.Pause();
					setMessageReceived(new JSONObject());
				}else{
					setMessageReceived(new JSONObject());
				}
				
				//let the thread that called sendQuery know its response is ready
				responseMessage.release();
				
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
}
