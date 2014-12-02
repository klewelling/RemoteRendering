package com.MediaServer;

public class Song {
	
	String title;
	String artist;
	String album;
	String id;
	String path;
	
	public Song(){
		this("", "", "", "","");
	}
	
	public Song(String title, String artist, String album, String id, String path){
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.id = id;
		this.path = path;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getArtist(){
		return this.artist;
	}
	
	public String getAlbum(){
		return this.album;
	}
	
	public String getID(){
		return this.id;
	}
	
	public String toString(){
		return String.format("%s;%s;%s;%s;%s", this.title, this.artist, this.album, this.id,this.path);
	}
	
	public static Song fromString(String dataString){
		String[] elements = dataString.split(";");
		return new Song(elements[0], elements[1], elements[2], elements[3],elements[4]);
	}
	public static void main(){
    	String pat = "/Users/alexbiju/Desktop/Mus/";
    	Song s1 = new Song("Big Parade","The Lumineers","The Lumineers","1",pat+"Big_Parade.mp3");
    	System.out.println(s1.toString());
	}
}

