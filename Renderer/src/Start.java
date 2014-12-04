

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Start {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		String vlcPath = "C:\\Users\\Isaac\\Documents\\vlc-2.1.5";
		String fileURL = "http://192.168.1.132:8080/Mus/Big_Parade.mp3";	
		String fileURL2 = "http://d1.mymp3song.com/files/Bollywood/Exclusive%20Single%20Track/Naina_-_Sonu_Kakkar(MyMp3Song.Com).mp3";
		String localFile = "C:\\Users\\Isaac\\Downloads\\1989\\BlankSpace.mp3";
		
		
		MediaPlayer ourPlayer = new MediaPlayer(vlcPath);
		ourPlayer.play(fileURL2);
		Thread.sleep(3000);
		ourPlayer.pause();
		Thread.sleep(1000);
		ourPlayer.pause();
		Thread.sleep(5000);
		ourPlayer.play(fileURL2);
		
	}
}
