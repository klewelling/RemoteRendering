package com.cs4390.remotecontrol;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentSongView implements View.OnClickListener{

	private LinearLayout wrappingLayout;
	private TextView song;
	private TextView artist;
	private ImageButton playPause;
	private PlayControls playControls;
	private boolean playingState;
	
	public CurrentSongView(View entireFragment, PlayControls _playControls){
		
		if(entireFragment == null || _playControls == null){
			throw new IllegalArgumentException();
		}
		
		wrappingLayout = (LinearLayout)entireFragment.findViewById(R.id.player_controls);
		song = (TextView)entireFragment.findViewById(R.id.song);
		artist = (TextView)entireFragment.findViewById(R.id.artist);
		playPause = (ImageButton)entireFragment.findViewById(R.id.play_button);
		playingState = true;
		playControls = _playControls;
		
		playPause.setOnClickListener(this);
	}
	
	public void hide(){
		wrappingLayout.setVisibility(View.GONE);
	}
	
	public void setSong(JSONObject song){
		
		try{
			this.wrappingLayout.setVisibility(View.VISIBLE);
			this.song.setText(song.getString(MainActivity.SONG));
			this.artist.setText(song.getString(MainActivity.ARIST));
		}catch(JSONException ex){throw new RuntimeException(ex);}
		
	}
	
	public void setState(boolean playing){
		if(playing){
			playPause.setImageResource(android.R.drawable.ic_media_pause);
		}else{
			playPause.setImageResource(android.R.drawable.ic_media_play);
		}
		
		playingState = playing;
	}
	
	
	@Override
	public void onClick(View v) {
		
		if(v == playPause){
			playControls.onPlayClicked(!playingState);
		}
		
	}
}
