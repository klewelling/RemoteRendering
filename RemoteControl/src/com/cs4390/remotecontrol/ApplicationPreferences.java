package com.cs4390.remotecontrol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ApplicationPreferences extends Activity implements OnEditorActionListener, OnFocusChangeListener{

	public static String MEDIA_IP_ADDRESS = "MEDIA_IP_ADDRESS";
	public static String MEDIA_PORT = "MEDIA_PORT";
	public static String RENDERER_IP_ADDRESS = "RENDERER_IP_ADDRESS";
	public static String RENDERER_PORT = "RENDERER_IP_PORT";
	
	private TextView mediaIP;
	private TextView mediaPort;
	private TextView rendererIP;
	private TextView rendererPort;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.application_prefs);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		TextView[] textViews = new TextView[4];
		mediaIP = (TextView)findViewById(R.id.media_ip_address);
		textViews[0] = mediaIP;
		mediaIP.setText(prefs.getString(MEDIA_IP_ADDRESS, ""));
		
		mediaPort = (TextView)findViewById(R.id.media_port);
		textViews[1] = mediaPort;
		mediaPort.setText(prefs.getString(MEDIA_PORT, ""));
		
		rendererIP = (TextView)findViewById(R.id.renderer_ip_address);
		textViews[2] = rendererIP;
		rendererIP.setText(prefs.getString(RENDERER_IP_ADDRESS, ""));
		
		rendererPort = (TextView)findViewById(R.id.renderer_port);
		textViews[3] = rendererPort;
		rendererPort.setText(prefs.getString(RENDERER_PORT, ""));
		
		for(TextView view: textViews){
			view.setOnEditorActionListener(this);
			view.setOnFocusChangeListener(this);
		}
	}	

	private void updatePrefs(TextView v){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		
		if(v == mediaIP){
			editor.putString(MEDIA_IP_ADDRESS, v.getText().toString());
			editor.commit();
		}else if(v == mediaPort){
			editor.putString(MEDIA_PORT, v.getText().toString());
			editor.commit();
		}else if(v == rendererIP){
			editor.putString(RENDERER_IP_ADDRESS, v.getText().toString());
			editor.commit();
		}else if(v == rendererPort){
			editor.putString(RENDERER_PORT, v.getText().toString());
			editor.commit();
		}
	}
	
	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		
		updatePrefs(arg0);
		
		return false;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		updatePrefs((TextView)v);
	}
}

