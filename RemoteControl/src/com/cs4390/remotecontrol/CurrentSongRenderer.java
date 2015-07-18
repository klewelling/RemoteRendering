package com.cs4390.remotecontrol;

import Song.Song;

public interface CurrentSongRenderer {

	void setSong(Song song);
	
	void setState(boolean playing);
}
