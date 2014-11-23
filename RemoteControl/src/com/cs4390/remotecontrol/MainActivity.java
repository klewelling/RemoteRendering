package com.cs4390.remotecontrol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements
		SearchView.OnQueryTextListener, SearchView.OnSuggestionListener,
		LoaderCallbacks<JSONObject>, SongSelectedListener {

	private JSONObject selectedSong;
	private CurrentSongRenderer currentSongRenderer;
	private boolean playing = true;
	
	private SearchView searchView;
	
	private MenuItem searchItem;

	private boolean expandSearch = false;
	private String searchQuery;
	
	private CommunicatonThread mediaServer;
	private CommunicatonThread rendererServer;

	private static final String CURRENTLY_SEARCHING = "CURRENTLY_SEARCHING";
	private static final String MEDIA_SERVER_ADDRESS = "MEDIA_SERVER_ADDRESS";
	private static final String RENDERER_SERVER_ADDRESS = "RENDERER_SERVER_ADDRESS";
	
	//TODO: replace with real message components
	public static final String MEDIA_QUERY = "MEDIA_QUERY";
	
	public static final String ARIST = "ARIST";
	public static final String SONG = "SONG";
	public static final String ALBUM = "ALBUM";
	public static final String RESULTS = "RESULTS";
	
	private static final int URL_SEARCH_LOADER = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		/*if( !prefs.contains(MEDIA_SERVER_ADDRESS) || prefs.contains(RENDERER_SERVER_ADDRESS)){
			//goto prefs screen
			return;
		}*/
		
		NoResultsFragment noResultsFragment = new NoResultsFragment();
		currentSongRenderer = noResultsFragment;
		getSupportFragmentManager().beginTransaction()
        	.add(android.R.id.content, noResultsFragment).commit();

		/*
		String mediaServerAddress = prefs.getString(MEDIA_SERVER_ADDRESS, null);
		String rendererServerAddress = prefs.getString(RENDERER_SERVER_ADDRESS, null);
		*/
		
		this.mediaServer = new CommunicatonThread("mediaserver");
		this.rendererServer = new CommunicatonThread("rendererserver");
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
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
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
	public Loader<JSONObject> onCreateLoader(int id, Bundle arg1) {
		
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
	public void onLoaderReset(Loader<JSONObject> arg0) {
		
	}
	
	@Override
	public void onLoadFinished(Loader<JSONObject> loader, JSONObject response) {
		if(loader.getId() == URL_SEARCH_LOADER){
			if(response.has(RESULTS)){
				try
				{
					JSONArray array = response.getJSONArray(RESULTS);
					if(array.length() != 0){
						if(searchItem != null)
							searchItem.collapseActionView();
						
						ResultsFragment listFragment = new ResultsFragment(array);
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
					
				}catch(JSONException ex){
					throw new RuntimeException(ex);
				}
				
			}
			
		}
	}
	
	@Override
	public JSONObject getSelectedSong() {
		return selectedSong;
	}
	
	@Override
	public void setSelectedSong(JSONObject _selectedSong) {
		selectedSong = _selectedSong;
		
		//tell the renderer to play the selected song
		
		//update the fragment to display the selected song
		currentSongRenderer.setSong(_selectedSong);
		currentSongRenderer.setState(true);
		
	}
}
