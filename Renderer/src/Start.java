

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Start {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		String vlcPath = "C:\\Users\\Isaac\\Documents\\vlc-2.1.5";
		String mediaFile="C:\\Users\\Isaac\\Downloads\\1989\\test.mp3";
		URL fileURL = new URL("http://192.168.1.132:8080/Mus/Big_Parade.mp3");

		org.apache.commons.io.FileUtils.copyURLToFile(fileURL, new File(mediaFile));
		
		MediaPlayer ourPlayer = new MediaPlayer(vlcPath, mediaFile);
		ourPlayer.run();
		Thread.sleep(3000);
		ourPlayer.pause();
		Thread.sleep(1000);
		ourPlayer.play();
		
	}
}
