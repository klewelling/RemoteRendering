


/**
 * Created by alexbiju on 11/8/14.
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class RenderServer {
    

    public static void main(String[] args) throws IOException {
    	
		String vlcPath = "C:\\Users\\Isaac\\Documents\\vlc-2.1.5";
		String mediaFile="C:\\Users\\Isaac\\Downloads\\1989\\test.mp3";
		URL fileURL = new URL("http://192.168.43.194:8080/Mus/Big_Parade.mp3");
    	
        MediaPlayer media = new MediaPlayer(vlcPath);
        
    	RenderController rThread = new RenderController(media);
        
        Thread mediaThread = new Thread(rThread);
        mediaThread.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(rThread.isRunning()){
        	try {
				mediaThread.join(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }
}