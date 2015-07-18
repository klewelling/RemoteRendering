package Song;

import org.json.JSONObject;

public class Song {
	
	String title;
	String artist;
	String album;
	String id;
	
	public Song(){
		this("", "", "", "");
	}
	
	public Song(String title, String artist, String album, String id){
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.id = id;
	}
	
	public String getTitle(){
		return this.title;
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
	
	/*public String toString(){
		return String.format("%s;%s;%s;%s", this.title, this.artist, this.album, this.id);
	}*/
	
	public static Song fromString(JSONObject data){
		return new Song( data.getString("Title") , data.getString("Artist"), data.getString("Album"), data.getString("Path"));
	}
}
