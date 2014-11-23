package com.cs4390.remotecontrol;

import org.json.JSONObject;

public interface CurrentSongRenderer {

	void setSong(JSONObject song);
	
	void setState(boolean playing);
}
