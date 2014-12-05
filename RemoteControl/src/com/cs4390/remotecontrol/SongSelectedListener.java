package com.cs4390.remotecontrol;

import Song.Song;

public interface SongSelectedListener {

	void setSelectedSong(Song selectedSong);
	
	Song getSelectedSong();
}
