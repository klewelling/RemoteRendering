package com.cs4390.remotecontrol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultsAdapter extends ArrayAdapter<String>{

	private JSONArray results;
	
	public ResultsAdapter(Context context, JSONArray _results) {
		super(context, R.layout.results, new String[_results.length()]);
		this.results = _results;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.results, parent, false);
		
    	TextView text1 = (TextView)rowView.findViewById(android.R.id.text1);
    	TextView text2 = (TextView)rowView.findViewById(android.R.id.text2);
    	
    	try{
    		JSONObject row = results.getJSONObject(position);
    		text1.setText(row.getString(MainActivity.ARIST));
    		text2.setText(row.getString(MainActivity.SONG) + " (" + row.getString(MainActivity.ALBUM) + ")");
    	}catch(JSONException ex){
    		throw new RuntimeException(ex);
    	}
    	
    	
		return rowView;
	}
	
}
