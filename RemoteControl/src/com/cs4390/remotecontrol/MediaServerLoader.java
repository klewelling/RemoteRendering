package com.cs4390.remotecontrol;

import java.util.List;

import org.json.JSONObject;

import Song.Song;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class MediaServerLoader extends AsyncTaskLoader<List<Song>> {

	private CommunicatonThread mediaServer;
	private JSONObject messageToSend;
	
	public MediaServerLoader(Context context, CommunicatonThread _mediaServer, JSONObject _messageToSend) {
		super(context);
		this.mediaServer = _mediaServer;
		this.messageToSend = _messageToSend;
	}
	
	@Override
	public List<Song> loadInBackground() {
		
		
		return mediaServer.sendSongsQuery(messageToSend);

	}
	
	
	@Override
	protected void onStartLoading() {
		forceLoad();
	}
	
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}
	


}
