package Controller;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.*;

import Song.Song;

public class Server {
	
	static final int SERVER_PORT = 8080;
	SocketAddress serverAddress;
	int port;
	PrintWriter socketWriteChannel;
	BufferedReader socketReadChannel;
	Socket server;
	
	public Server() throws Exception{
		this("127.0.0.1", SERVER_PORT);
	}
	
	public Server(String ipAddress, int port) throws Exception{
		this.port = port;
		try {
			//this.serverAddress = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
			this.serverAddress = new InetSocketAddress(ipAddress, port);
		} catch (Exception ServEx) {
			throw new Exception("Unknown Server Host", ServEx);
		}
		
		Connect();
	}
	
	public void Disconnect() throws IOException{
		this.server.close();
	}
		
	public void Connect() throws Exception{	
		// Attempt to connect to Server
		this.server = new Socket();
		try {
			this.server.connect(this.serverAddress, 60000);
		} catch (SocketTimeoutException ex) {
			throw new Exception("Timed out while connecting to server", ex);
		} catch (ConnectException ex){
			throw new Exception("Connection refused while connecting to server", ex);
		}
		// If server connection fails; alert user
		if (!this.server.isConnected()){
			throw new Exception("Unable to connect to Server");
		} else {
			this.server.setKeepAlive(true);
		}
		
		socketWriteChannel = new PrintWriter(this.server.getOutputStream(), true);
		socketReadChannel = new BufferedReader(new InputStreamReader(this.server.getInputStream()));		
	}
	
	// Sends a search command to the Server, with a string containing the search params
	public List<Song> Search(String searchParams) throws Exception{
		JSONObject outbound = new JSONObject();
		List<Song> temp = new ArrayList<Song>();
		JSONObject fromString;
		JSONArray tempResults;
		String inbound = "";
		
		try{
		
			outbound.put("Type","Controller");
			outbound.put("Request","Search");
			outbound.put("Search", searchParams);
			socketWriteChannel.println(outbound.toString());
			inbound = socketReadChannel.readLine();
		} catch (SocketException ex){
			throw new Exception ("Connection to Server lost");
		}
		
		//JSONArray blah = new JSONArray();
		fromString = new JSONObject(new JSONTokener(inbound));
		// Parse list from inbound info
		
		if(fromString.has("Music")){
			tempResults = fromString.getJSONArray("Music");
			if (tempResults != null){
				for (int i = 0; i < tempResults.length(); i++){
					JSONObject jsonObject = tempResults.getJSONObject(i);
					temp.add(i, Song.fromString( jsonObject ) );
				}
			}	
		}
		
		
		return temp;
	}
	
	// Sends a search command to the Server, with a string containing the search params
		public List<String> TopTen() throws Exception{
			JSONObject outbound = new JSONObject();
			List<String> temp = new ArrayList<String>(10);
			JSONObject fromString;
			JSONArray tempResults;
			String inbound;
			
			try{
				outbound.put("type","controller");
				outbound.put("request","top");
				socketWriteChannel.println(outbound.toString());
				inbound = socketReadChannel.readLine();
			} catch (SocketException ex){
				throw new Exception ("Connection to Server lost");
			}
			fromString = new JSONObject(new JSONTokener(inbound));
			// Parse list from inbound JSONArray
			tempResults = (JSONArray) fromString.get("results");
			if (tempResults != null){
				for (int i = 0; i < tempResults.length() && i < 10; i++){
					temp.add(i, tempResults.getString(i));
				}
			}
			
			return temp;
		}
}
