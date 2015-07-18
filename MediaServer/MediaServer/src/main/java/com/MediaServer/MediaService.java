package com.MediaServer;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.*;


/**
 * Created by alexbiju on 11/8/14.
 */
public class MediaService implements Runnable{
    
	Map<String,Song> musicLibrary = new HashMap<String,Song>(10);
	private MediaController mediaController;
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
    public MediaService(String filePath, MediaController mediaThread, Socket client){
    	String pat = "http://192.168.43.194:8080//Mus/";
    	Song s1 = new Song("Big Parade","The Lumineers","The Lumineers","1",pat+"Big_Parade.mp3");
    	Song s2 = new Song("Flapper Girl","The Lumineers","The Lumineers","2",pat+"Flapper_Girl.mp3");
    	Song s3 = new Song("Flowers In Your Hair","The Lumineers","The Lumineers","3",pat+"Flowers_In_Your_Hair.mp3");
    	Song s4 = new Song("Morning Song","The Lumineers","The Lumineers","4",pat+"Morning_Song.mp3");
    	Song s5 = new Song("Classy Girls","The Lumineers","The Lumineers","5",pat+"Classy_Girls.mp3");
    	Song s6 = new Song("Dead Sea","The Lumineers","The Lumineers","6",pat+"Dead_Sea.mp3");
    	Song s7 = new Song("Ho Hey","The Lumineers","The Lumineers","7",pat+"Ho_Hey.mp3");
    	Song s8 = new Song("Slow it Down","The Lumineers","The Lumineers","8",pat+"Slow_It_Down.mp3");
    	Song s9 = new Song("Stubborn Love","The Lumineers","The Lumineers","9",pat+"Stubborn_Love.mp3");
    	Song s10 = new Song("Submarines","The Lumineers","The Lumineers","10",pat+"Submarines.mp3");
    	musicLibrary.put(s1.getTitle(), s1);
    	musicLibrary.put(s2.getTitle(), s2);
    	musicLibrary.put(s3.getTitle(), s3);
    	musicLibrary.put(s4.getTitle(), s4);
    	musicLibrary.put(s5.getTitle(), s5);
    	musicLibrary.put(s6.getTitle(), s6);
    	musicLibrary.put(s7.getTitle(), s7);
    	musicLibrary.put(s8.getTitle(), s8);
    	musicLibrary.put(s9.getTitle(), s9);
    	musicLibrary.put(s10.getTitle(), s10);
        mediaController = mediaThread;
        this.client = client;
        
        try {
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			this.out = new PrintWriter(this.client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    public JSONObject search(String searchTerm){
        
        JSONArray jsonArray = new JSONArray();
        
        for(String key : musicLibrary.keySet()) {
        	if(key.contains(searchTerm)){
	        	JSONObject toJ = new JSONObject();
	        	
	            toJ.put("Title", musicLibrary.get(key).getTitle());
	            toJ.put("Artist", musicLibrary.get(key).getArtist());
	            toJ.put("Album", musicLibrary.get(key).getAlbum());
	            toJ.put("ID", musicLibrary.get(key).getID());
	            toJ.put("Path", musicLibrary.get(key).getPath());
	           jsonArray.put(toJ);
        	}
        }
        JSONObject newJson = new JSONObject();
        
        newJson.put("Music", jsonArray);
        return newJson;
        

    }
public JSONObject findPath(String id){
        
	JSONObject newJson = new JSONObject();
        
        for(String key : musicLibrary.keySet()) {
        	if(musicLibrary.get(key).getID().contains(id)){
        		newJson.put("URL", musicLibrary.get(key).getPath());
        	}
        }
        
        
        
        return newJson;
        

    }
public Boolean findIfPath(String id){
    
	JSONObject newJson = new JSONObject();
        
        for(String key : musicLibrary.keySet()) {
        	if(musicLibrary.get(key).getID().contains(id)){
        		return true;
        	}
        }
        return false;
    }
    public JSONObject topTen(){
    	JSONArray jsonArray = new JSONArray();
        
        for(String key : musicLibrary.keySet()) {

        	JSONObject toJ = new JSONObject();
        	
            toJ.put("Title", musicLibrary.get(key).getTitle());
            toJ.put("Artist", musicLibrary.get(key).getArtist());
            toJ.put("Album", musicLibrary.get(key).getAlbum());
            toJ.put("ID", musicLibrary.get(key).getID());
            toJ.put("Path", musicLibrary.get(key).getPath());
           jsonArray.put(toJ);  	

        }
        JSONObject newJson = new JSONObject();
        
        newJson.put("Music", jsonArray);
        return newJson;
    }

	public void run() {
		
			try {
				while(!Thread.currentThread().isInterrupted()){
					
					String recieved = in.readLine();
					System.out.println("Recieved: "+recieved);
					JSONObject jsonIn = new JSONObject(new JSONTokener(recieved));
					
					if(jsonIn.has("Type")){
						if("Controller".equalsIgnoreCase(jsonIn.getString("Type"))){
							if(jsonIn.has("Request")){
								
								if("Search".equals(jsonIn.getString("Request"))){
									
									String searchT = jsonIn.getString("Search");
									JSONObject jsonOut = search(searchT);
									System.out.print(jsonOut.toString());
									out.println(jsonOut.toString());
									
								}else if("Top Ten".equalsIgnoreCase(jsonIn.getString("Request"))){		
									
									JSONObject jsonOut = new JSONObject(new JSONTokener(topTen().toString()));
									System.out.print(jsonOut.toString());
									out.println(jsonOut.toString());
									
								}else{
									
									JSONObject jsonOut = new JSONObject();
									jsonOut.put("Request", "Invalid");
									out.println(jsonOut.toString());
									
								}
								
							}else{
								
								JSONObject jsonOut = new JSONObject();
								jsonOut.put("Request", "Invalid");
								out.println(jsonOut.toString());
							}
						}else if("Presenter".equalsIgnoreCase(jsonIn.getString("Type"))){
							if("Get".equalsIgnoreCase(jsonIn.getString("Request"))){
								out.println(findPath(jsonIn.getString("Get")));
							}
						}else{
							JSONObject jsonOut = new JSONObject();
							jsonOut.put("Request", "Invalid");
							out.println(jsonOut.toString());
						}
					}else if(jsonIn.has("Command")){
						if("SHUTDOWN".equalsIgnoreCase(jsonIn.getString("Command"))){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Shut Down server");
							mediaController.close();
							break;
						}
					}else{
						JSONObject jsonOut = new JSONObject();
						jsonOut.put("Request", "Invalid");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
}
