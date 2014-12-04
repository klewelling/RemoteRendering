
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MediaPlayer {

	
	private JFrame ourFrame = new JFrame();
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private String mediaPath = "C:\\Users\\Isaac\\Downloads\\song.mp3";
	private boolean flag = true;
	private URL fileURL = null;
	
	MediaPlayer(String vlcPath)
	{
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
		
		ourMediaPlayer = new EmbeddedMediaPlayerComponent();
		
		ourFrame.setContentPane(ourMediaPlayer);
		ourFrame.setSize(1200, 800);
		ourFrame.setVisible(true);
		ourFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void pause()
	{
		if(flag){
			ourMediaPlayer.getMediaPlayer().pause();
		}
		else {
			ourMediaPlayer.getMediaPlayer().play();
			}
		
		flag = !flag;
	}
	
	public void play(String mediaURL) throws IOException
	{
		if(mediaURL.startsWith("C:\\")){
			mediaPath = mediaURL;
		}
		else {
		fileURL = new URL(mediaURL);
		org.apache.commons.io.FileUtils.copyURLToFile(fileURL, new File(mediaPath));
		}
		ourMediaPlayer.getMediaPlayer().playMedia(mediaPath);		
	}
}
