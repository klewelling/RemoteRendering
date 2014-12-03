
import javax.swing.JFrame;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MediaPlayer {

	
	private JFrame ourFrame = new JFrame();
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private String mediaPath = "";
	
	MediaPlayer(String vlcPath, String mediaURL)
	{
		
		this.mediaPath = mediaURL;
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
		
		ourMediaPlayer = new EmbeddedMediaPlayerComponent();
		
		ourFrame.setContentPane(ourMediaPlayer);
		ourFrame.setSize(1200, 800);
		ourFrame.setVisible(true);
		ourFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void run()
	{
		
		ourMediaPlayer.getMediaPlayer().startMedia(mediaPath);
	}
	
	public void pause()
	{
		
		ourMediaPlayer.getMediaPlayer().pause();
	}
	
	public void play()
	{
		
		ourMediaPlayer.getMediaPlayer().play();
	}
}
