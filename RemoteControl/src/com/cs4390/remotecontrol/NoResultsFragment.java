package com.cs4390.remotecontrol;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoResultsFragment extends Fragment implements CurrentSongRenderer, PlayControls{

	private CurrentSongView currentSongView;
	private JSONObject tempSong;
	private Boolean tempState;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View toReturn = inflater.inflate(R.layout.activity_main, container, false);
		currentSongView = new CurrentSongView(toReturn, this);
		if(tempSong != null){
			currentSongView.setSong(tempSong);
			tempSong = null;
		}
		
		if(tempState != null){
			currentSongView.setState(tempState);
			tempState = null;
		}
		return toReturn;
	}

	
	@Override
	public void setSong(JSONObject song) {
		if(currentSongView == null){
			tempSong = song;
		}else{
			tempSong = null;
			if(song == null){
				currentSongView.hide();
			}else{
				currentSongView.setSong(song);
			}	
		}
		
	}
	
	@Override
	public void setState(boolean playing) {
		if(currentSongView == null){
			tempState = playing;
		}else{
			tempState = null;
			currentSongView.setState(playing);
		}
		
	}
	
	@Override
	public void onPlayClicked(boolean playing) {
		setState(playing);
		
		//TODO: move PLayControls to MainActivity
		//This will use the presenter thread to send the play message
	}
	
}
