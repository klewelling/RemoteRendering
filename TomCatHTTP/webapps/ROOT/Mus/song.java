import java.util.*;
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
	public static void main(String[] args){
    	String pat = "/Users/alexbiju/Desktop/Mus/";
    	Song s1 = new Song("Big Parade","The Lumineers","The Lumineers","1",pat+"Big_Parade.mp3");
    	System.out.println(s1.toString());
    	Map<String,Song> musicLibrary = new HashMap<String,Song>(10);
    	String pat = "/Users/alexbiju/Desktop/Mus/";
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
	}
}