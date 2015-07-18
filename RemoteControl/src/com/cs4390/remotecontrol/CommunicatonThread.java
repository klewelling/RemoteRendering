package com.cs4390.remotecontrol;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.json.JSONObject;

import Song.Song;


public class CommunicatonThread implements Runnable {

	//NEVER ACCESS THESE DIRECTLY. use the thread-safe get/set methods
	private JSONObject messageToSend;
	private JSONObject messageReceived;
	private List<Song> songsReceived;
	
	protected Semaphore newMessage = new Semaphore(0);
	protected Semaphore responseMessage = new Semaphore(0);
	
	public CommunicatonThread(){
		
	}
	
	public List<Song> sendSongsQuery(JSONObject query){
		
		setMessageToSend(query);
		
		newMessage.release();
		
		try{
			responseMessage.acquire();
		}catch(InterruptedException ex){
			throw new RuntimeException(ex);
		}
		
		List<Song> toReturn = getSongsReceived();
		return toReturn;
		
	}
	
	public JSONObject sendMessage(JSONObject query){
		
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
	
	protected synchronized JSONObject getMessageToSend() {
		return messageToSend;
	}

	protected synchronized void setMessageToSend(JSONObject messageToSend) {
		this.messageToSend = messageToSend;
	}

	protected synchronized JSONObject getMessageReceived() {
		return this.messageReceived;
	}
	
	protected synchronized List<Song> getSongsReceived() {
		return this.songsReceived;
	}

	protected synchronized void setMessageReceived(JSONObject messageReceived) {
		this.messageReceived = messageReceived;
	}

	protected synchronized void setSongsReceived(List<Song> _songsReceived) {
		this.songsReceived = _songsReceived;
	}
	
	@Override
	public void run() {
		
	}


}
