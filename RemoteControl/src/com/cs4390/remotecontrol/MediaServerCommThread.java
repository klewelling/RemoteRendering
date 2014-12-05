package com.cs4390.remotecontrol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import Controller.Server;
import Song.Song;

public class MediaServerCommThread extends CommunicatonThread {

	private String serverAddress;
	private String serverPort;
	private Server mediaServer;
	
	public MediaServerCommThread(String _serverAddress, String _serverPort){
		this.serverAddress = _serverAddress;
		this.serverPort = _serverPort;
		
		
	}
	
	@Override
	public void run() {
		try{
			while(!Thread.currentThread().isInterrupted() ){
				
				//Create a socket and get the in/out streams
				try{
					mediaServer = new Server(serverAddress, Integer.parseInt(serverPort) );	
				}catch(Exception ex){
					throw new RuntimeException(ex);
				}
				mediaServer.Connect();
				
				//wait for a message to send
				newMessage.acquire();
				
				JSONObject msgToSendToMediaServer = getMessageToSend();
				if(msgToSendToMediaServer.has(MainActivity.MEDIA_QUERY)){
					String searchString = msgToSendToMediaServer.getString(MainActivity.MEDIA_QUERY);
					List<Song> results = mediaServer.Search(searchString);
					setSongsReceived(results);
				}else{
					setSongsReceived(new ArrayList<Song>());
				}
				
				//let the thread that called sendQuery know its response is ready
				responseMessage.release();
				
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
