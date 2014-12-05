package com.cs4390.remotecontrol;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Song.Song;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements
		SearchView.OnQueryTextListener, SearchView.OnSuggestionListener,
		LoaderCallbacks<List<Song>>, SongSelectedListener, PlayControls {

	private Song selectedSong;
	private CurrentSongRenderer currentSongRenderer;
	private boolean playing = true;
	
	private SearchView searchView;
	
	private MenuItem searchItem;
	private MenuItem preferenceItem;

	private boolean expandSearch = false;
	private String searchQuery;
	
	private CommunicatonThread mediaServer;
	private CommunicatonThread rendererServer;

	private static final String CURRENTLY_SEARCHING = "CURRENTLY_SEARCHING";
	
	//TODO: replace with real message components
	public static final String MEDIA_QUERY = "MEDIA_QUERY";
	
	public static final String ARIST = "ARIST";
	public static final String SONG = "SONG";
	public static final String ALBUM = "ALBUM";
	public static final String ID = "ID";
	public static final String PAUSE = "PAUSE";
	public static final String RESULTS = "RESULTS";
	
	private static final int URL_SEARCH_LOADER = 0;
	
	private boolean goToPrefs = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if( "".equals(prefs.getString(ApplicationPreferences.MEDIA_IP_ADDRESS, ""))  || 
				"".equals(prefs.getString(ApplicationPreferences.MEDIA_PORT, ""))  ||
				"".equals(prefs.getString(ApplicationPreferences.RENDERER_IP_ADDRESS, ""))  ||
				"".equals(prefs.getString(ApplicationPreferences.RENDERER_PORT, ""))  ){
			//goto prefs screen
			goToPrefs = true;
			startActivity(new Intent(this, ApplicationPreferences.class));
		}else{
			goToPrefs = false;
			NoResultsFragment noResultsFragment = new NoResultsFragment();
			currentSongRenderer = noResultsFragment;
			getSupportFragmentManager().beginTransaction()
	        	.add(android.R.id.content, noResultsFragment).commit();

			
			String mediaServerAddress = prefs.getString(ApplicationPreferences.MEDIA_IP_ADDRESS, null);
			String mediaServerPort = prefs.getString(ApplicationPreferences.MEDIA_PORT, null);
			String rendererServerAddress = prefs.getString(ApplicationPreferences.RENDERER_IP_ADDRESS, null);
			String rendererServerPort = prefs.getString(ApplicationPreferences.RENDERER_PORT, null);
			
			
			this.mediaServer = new MediaServerCommThread(mediaServerAddress, mediaServerPort);
			this.rendererServer = new RendererCommThread(rendererServerAddress, rendererServerPort);
			Thread mediaThread = new Thread(mediaServer);
			mediaThread.start();
			
			Thread rendererThread = new Thread(rendererServer);
			rendererThread.start();
			
			expandSearch = false;
	        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENTLY_SEARCHING) && 
	        		savedInstanceState.getBoolean(CURRENTLY_SEARCHING)){
	        	expandSearch = true;
	        }	
		}
		
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!goToPrefs)
			getSupportLoaderManager().initLoader(URL_SEARCH_LOADER, null, this);
	}

	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	boolean searching = false;
    	if(searchView != null)
    		searching = !searchView.isIconified();
    	outState.putBoolean(CURRENTLY_SEARCHING, searching);
    	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
		searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for music");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
		
        //searchView.setSuggestionsAdapter(mSuggestionsAdapter);
        
		searchItem = menu.add(Menu.NONE, Menu.NONE, 1,R.string.menu_search);
		searchItem.setIcon(R.drawable.ic_search).
			setActionView(searchView).
			setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	    
	    if(expandSearch)
	    		searchItem.expandActionView();
	    
	    preferenceItem =  menu.add(Menu.NONE, 100, 2,R.string.action_settings)
	        	.setIcon(android.R.drawable.ic_menu_preferences);
	    
	    
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == preferenceItem.getItemId()) {
			startActivity(new Intent(this, ApplicationPreferences.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String newText) {
		searchQuery = newText;		
		getSupportLoaderManager().restartLoader(URL_SEARCH_LOADER, null, this);
    	return false;		
	}

	@Override
	public boolean onSuggestionClick(int arg0) {
		return false;
	}

	public boolean onSuggestionSelect(int arg0) {
		return false;
	};
	
	@Override
	public Loader<List<Song>> onCreateLoader(int id, Bundle arg1) {
		
		if(id == URL_SEARCH_LOADER){
			
			JSONObject mediaServerQuery = new JSONObject();
			try{
				mediaServerQuery.put(MEDIA_QUERY, searchQuery);	
			}catch(JSONException ex){
				throw new RuntimeException(ex);
			}
			
			return new MediaServerLoader(this, this.mediaServer, mediaServerQuery);
		}
		
		return null;
		
	}
	
	@Override
	public void onLoaderReset(Loader<List<Song>> arg0) {
		
	}
	
	@Override
	public void onLoadFinished(Loader<List<Song>> loader, List<Song> response) {
		if(loader.getId() == URL_SEARCH_LOADER){
			//if(response.has(RESULTS)){
			{
				if(response.size() != 0){
					if(searchItem != null)
						searchItem.collapseActionView();
					
					ResultsFragment listFragment = new ResultsFragment(response);
					currentSongRenderer = listFragment;
					
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

					transaction.replace(android.R.id.content, listFragment);
					transaction.addToBackStack(null);

					// Commit the transaction
					transaction.commitAllowingStateLoss();
					
				}else{
					NoResultsFragment noResultsFragment = new NoResultsFragment();
					currentSongRenderer = noResultsFragment;
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

					transaction.replace(android.R.id.content, noResultsFragment);
					transaction.addToBackStack(null);

					// Commit the transaction
					transaction.commitAllowingStateLoss();						
				}
				
				if(currentSongRenderer != null){
					if(selectedSong != null){
						currentSongRenderer.setSong(selectedSong);
						currentSongRenderer.setState(playing);
					}else{
						currentSongRenderer.setSong(null);
					}
				}

				
			}
			
		}
	}
	
	@Override
	public Song getSelectedSong() {
		return selectedSong;
	}
	
	@Override
	public void setSelectedSong(Song _selectedSong) {
		selectedSong = _selectedSong;
		
		//tell the renderer to play the selected song
		AsyncTask<Song, Integer, Song> task = new AsyncTask<Song, Integer, Song>(){
			@Override
			protected Song doInBackground(Song... params) {
				
				//TODO: call Pauls API
				try{
					String idToSend = params[0].getID();
					JSONObject toSend = new JSONObject();
					toSend.put(ID, idToSend);
					rendererServer.sendMessage(toSend);	
				}catch(JSONException ex){
					return null;
				}
				
				return params[0];
			}
			
			@Override
			protected void onPostExecute(Song result) {
				if(result != null){
					//update the fragment to display the selected song
					currentSongRenderer.setSong(result);
					currentSongRenderer.setState(true);
				}else{
					//TODO: put up a failed dialog.
					currentSongRenderer.setSong(null);
				}
			}
		};
		task.execute(_selectedSong);
		
	}
	
	@Override
	public void onPlayClicked(boolean _playing) {
		
		AsyncTask<Boolean, Integer, Boolean> task = new AsyncTask<Boolean, Integer, Boolean>(){
			@Override
			protected Boolean doInBackground(Boolean... params) {
				
				//pause
				try{
					JSONObject toSend = new JSONObject();
					toSend.put(PAUSE, params[0]);
					rendererServer.sendMessage(toSend);	
				}catch(JSONException ex){
					return null;
				}
				
				return params[0];
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				if(result.booleanValue()){
					playing = result;
					currentSongRenderer.setState(playing);
				}else{
					//TODO: error message, in unknown state right now. 
					playing = false;
					currentSongRenderer.setState(false);
				}
			}
		};
		task.execute(_playing);
		
		
		
	}
}
