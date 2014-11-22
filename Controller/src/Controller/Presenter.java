package Controller;

import java.io.*;
import java.net.*;

import org.json.*;

public class Presenter {
	
	static final int PRESENTER_PORT = 10023;
	SocketAddress presenterAddress;
	int port;
	PrintWriter socketWriteChannel;
	BufferedReader socketReadChannel;
	Socket presenter;
	
	public Presenter() throws Exception{
		this("1.1.1.1", PRESENTER_PORT);
	}
	
	public Presenter(String ipAddress, int port) throws Exception{
		this.port = port;
		try {
			this.presenterAddress = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
		} catch (UnknownHostException PresEx) {
			throw new Exception("Unknown Server Host", PresEx);
		}
		
		Connect();
	}
	
	private void ErrorCheck() throws Exception{
		String inboundError;
		JSONObject fromString;
		
		// Test connection
		if (!this.presenter.isConnected()){
			throw new Exception ("Connection to Presenter lost");
		}
		
		// Check if presenter has sent any error messages
		try {
			presenter.setSoTimeout(500);
			inboundError = socketReadChannel.readLine();
			presenter.setSoTimeout(0);
			fromString = new JSONObject(new JSONTokener(inboundError));
			socketWriteChannel.println((new JSONObject().append("result", "success")).toString());
			throw new Exception(fromString.getString("error"));
			
		} catch (IOException ex) {
			presenter.setSoTimeout(0);
		}
	}
	
	public void Disconnect() throws IOException{
		this.presenter.close();
	}
	
	public void Connect() throws Exception{
		
		this.presenter = new Socket();
		try {
			this.presenter.connect(presenterAddress, 60000);
		} catch (SocketTimeoutException ex) {
			throw new Exception("Timed out while connecting to presenter", ex);
		}
		// If presenter connection fails; alert user
		if (!this.presenter.isConnected()){
			throw new Exception("Unable to connect to Presenter");
		} else {
			this.presenter.setKeepAlive(true);
		}
		
		socketReadChannel = new BufferedReader(new InputStreamReader(this.presenter.getInputStream()));
		socketWriteChannel = new PrintWriter(this.presenter.getOutputStream(), true);
	}
	
	private void SendCommand(String command, String parameter) throws Exception{
		JSONObject outbound = new JSONObject();
		JSONObject fromString;
		String inbound;
		
		ErrorCheck();
		
		switch (command){
		case "play":
			outbound.append("action",command);
			outbound.append("id", parameter);
			break;
		default:
			outbound.append("action",command);
			break;
		}

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
		fromString = new JSONObject(new JSONTokener(inbound));
		if (fromString.getString("result") == "failure") {
			throw new Exception(fromString.getString("reason"));
		} else if (fromString.getString("result") == "success") {
			;
		} else {
			throw new Exception("Invalid response from Presenter");
		}
	}
	
	// Sends a Play command to the Presenter to play the file specified
	public void Play(String filename) throws Exception{
		SendCommand("play", filename);
	}
	
	// Sends a Pause command to the Presenter
	public void Pause() throws Exception{
		SendCommand("pause", "");
	}
	
	// Sends a rewind command to the Presenter
	public void Rewind() throws Exception{
		SendCommand("rewind", "");
	}
	
	// Sends a fast forward command to the Presenter
	public void Forward() throws Exception{
		SendCommand("forward", "");
	}
	
	// Sends a stop command to the Presenter
	public void Stop() throws Exception{
		SendCommand("stop", "");
	}

}
