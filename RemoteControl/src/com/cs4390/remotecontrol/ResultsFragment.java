package com.cs4390.remotecontrol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ResultsFragment extends ListFragment implements CurrentSongRenderer {

	private SongSelectedListener selectedListener;
	private PlayControls playControls;
	private JSONArray results;
	private CurrentSongView currentSongView;
	private JSONObject tempSong;
	private Boolean tempState;
	
	
	public ResultsFragment(JSONArray _results){
		this.results = _results;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		setListAdapter(new ResultsAdapter(getActivity(), results));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		selectedListener = (SongSelectedListener)activity;
		playControls = (PlayControls)activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.listview, container, false);
		currentSongView = new CurrentSongView(view, playControls);
		
		if(tempSong != null){
			currentSongView.setSong(tempSong);
			tempSong = null;
		}
		
		if(tempState != null){
			currentSongView.setState(tempState);
			tempState = null;
		}
		return view;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		try{
			selectedListener.setSelectedSong(results.getJSONObject(position));	
		}catch(JSONException ex){throw new RuntimeException(ex);}
		
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
		
	
}