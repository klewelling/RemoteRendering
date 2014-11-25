package com.cs4390.remotecontrol;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class MediaServerLoader extends AsyncTaskLoader<JSONObject> {

	private CommunicatonThread mediaServer;
	private JSONObject messageToSend;
	
	public MediaServerLoader(Context context, CommunicatonThread _mediaServer, JSONObject _messageToSend) {
		super(context);
		this.mediaServer = _mediaServer;
		this.messageToSend = _messageToSend;
	}
	
	@Override
	public JSONObject loadInBackground() {
		
		
		return mediaServer.sendMessage(messageToSend);

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
