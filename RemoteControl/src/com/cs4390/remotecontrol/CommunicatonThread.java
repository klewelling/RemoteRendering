package com.cs4390.remotecontrol;

import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONObject;


public class CommunicatonThread implements Runnable {

	private String serverAddress;
	
	//NEVER ACCESS THESE DIRECTLY. use the thread-safe get/set methods
	private JSONObject messageToSend;
	private JSONObject messageReceived;
	
	private Semaphore newMessage = new Semaphore(0);
	private Semaphore responseMessage = new Semaphore(0);
	
	public CommunicatonThread(String _serverAddress){
		this.serverAddress = _serverAddress;
	}
	
	public JSONObject sendQuery(JSONObject query){
		
		setMessageToSend(query);
		
		newMessage.release();
		
		try{
			responseMessage.acquire();
		}catch(InterruptedException ex){
			throw new RuntimeException(ex);
		}
		
		JSONObject toReturn = getMessageReceived();
		return toReturn;
	}
	
	private synchronized JSONObject getMessageToSend() {
		return messageToSend;
	}

	private synchronized void setMessageToSend(JSONObject messageToSend) {
		this.messageToSend = messageToSend;
	}

	private synchronized JSONObject getMessageReceived() {
		return this.messageReceived;
	}

	private synchronized void setMessageReceived(JSONObject messageReceived) {
		this.messageReceived = messageReceived;
	}

	@Override
	public void run() {
		try{
			while(!Thread.currentThread().isInterrupted() ){
				
				//Create a socket and get the in/out streams
				
				
				//wait for a message to send
				newMessage.acquire();
				
				
				JSONObject msgToSendToMediaServer = getMessageToSend();
				//send message to media server and get the response				
				
				//Faked remote communication session
				JSONObject toReturn = new JSONObject();
				if(msgToSendToMediaServer.has(MainActivity.MEDIA_QUERY)){
					String searchString = msgToSendToMediaServer.getString(MainActivity.MEDIA_QUERY);
					
					JSONArray array = new JSONArray();
					if(searchString.startsWith("B") || searchString.startsWith("b")){
						
						for(int i=0; i < 10; i++){
							JSONObject result = new JSONObject();
							result.put(MainActivity.ARIST, "David Bowie");
							result.put(MainActivity.ALBUM, "The man who sold the world");
							result.put(MainActivity.SONG, "The man who sold the world " + i);
							array.put(result);
						}
					}
					toReturn.put(MainActivity.RESULTS, array);
					
				}
				setMessageReceived(toReturn);
				
				//let the thread that called sendQuery know its response is ready
				responseMessage.release();
				
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}


}
