

import java.io.File;
import java.net.URL;

import org.apache.commons.io.*;

import javax.swing.JFileChooser;

public class Start {

	
	private static JFileChooser ourFileSelector = new JFileChooser();
	
	public static void main(String[] args)
	{
		
		String vlcPath = "",mediaPath="";
		String URL = "";
		File mediaFile="C:\\Users\\Isaac\\Downloads\\1989";
		URL fileURL = new URL("https://github.com/isaacbutterfield/pdf/blob/master/02%20Blank%20Space.mp3");
		File ourFile;
		
		ourFileSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		ourFileSelector.showSaveDialog(null);
		ourFile = ourFileSelector.getSelectedFile();
		vlcPath = ourFile.getAbsolutePath();
		
		org.apache.commons.io.FileUtils.copyURLToFile(fileURL, mediaFile);
		
		ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ourFileSelector.showSaveDialog(null);
		ourFile = ourFileSelector.getSelectedFile();
		mediaPath = ourFile.getAbsolutePath();
		
		new MediaPlayer(vlcPath, mediaPath).run();
		
	}
}
