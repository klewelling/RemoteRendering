package com.cs4390.remotecontrol;

import org.json.JSONObject;

public interface SongSelectedListener {

	void setSelectedSong(JSONObject selectedSong);
	
	JSONObject getSelectedSong();
}
