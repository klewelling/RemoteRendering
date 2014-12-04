
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.*;


/**
 * Created by alexbiju on 11/8/14.
 */
public class RenderService implements Runnable{

	private RenderController RenderController;
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	MediaPlayer mediaplayer = new MediaPlayer("C:\\Users\\Isaac\\Documents\\vlc-2.1.5");
	
    public RenderService(String filePath, RenderController mediaThread, Socket client, MediaPlayer media){
        RenderController = mediaThread;
        mediaplayer = media;
        this.client = client;
        
        try {
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			this.out = new PrintWriter(this.client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    
	public void run() {
		
			try {
				while(!Thread.currentThread().isInterrupted()){
					
					while(!in.ready()){
						;
					}
					String recieved = in.readLine();
					System.out.println("Recieved: "+recieved);
					
					JSONObject jsonIn = new JSONObject(new JSONTokener(recieved));
					
					if(jsonIn.has("Type")){
						if("Controller".equalsIgnoreCase(jsonIn.getString("Type"))){
							if(jsonIn.has("Action")){
								if("Play".equalsIgnoreCase(jsonIn.getString("Action"))){
									mediaplayer.play(jsonIn.getString("Id"));
								}else if("Pause".equalsIgnoreCase(jsonIn.getString("Action"))){
									mediaplayer.pause();
								}else{
									JSONObject jsonOut = new JSONObject();
									jsonOut.put("Result", "Failure");
									jsonOut.put("Reason", "You done goofed");
									out.println(jsonOut.toString());
								}
							}else{
								JSONObject jsonOut = new JSONObject();
								jsonOut.put("Result", "Failure");
								jsonOut.put("Reason", "You done goofed");
								out.println(jsonOut.toString());
							}
						}else{
							JSONObject jsonOut = new JSONObject();
							jsonOut.put("Result", "Failure");
							jsonOut.put("Reason", "You done goofed");
							out.println(jsonOut.toString());
						}
					}else if(jsonIn.has("Command")){
						if("SHUTDOWN".equalsIgnoreCase(jsonIn.getString("Command"))){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Shut Down server");
							RenderController.close();
							break;
						}
					}else{
						JSONObject jsonOut = new JSONObject();
						jsonOut.put("Result", "Failure");
						jsonOut.put("Reason", "You done goofed");
						out.println(jsonOut.toString());
					}
				}
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
}

