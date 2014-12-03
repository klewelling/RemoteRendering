
import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

public class MediaPlayer {

	
	private JFrame ourFrame = new JFrame();
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private String mediaPath = "";
	
	MediaPlayer(String vlcPath, String mediaURL)
	{
		
		this.mediaPath = mediaURL;
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
		//Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		//LibXUtil.initialise();
		
		ourMediaPlayer = new EmbeddedMediaPlayerComponent();
		
		ourFrame.setContentPane(ourMediaPlayer);
		ourFrame.setSize(1200, 800);
		ourFrame.setVisible(true);
		ourFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void run()
	{
		
		ourMediaPlayer.getMediaPlayer().playMedia(mediaPath);
	}
}
