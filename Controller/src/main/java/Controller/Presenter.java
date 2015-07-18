package Controller;

import java.io.*;
import java.net.*;

import org.json.*;

public class Presenter {
	
	static final int PRESENTER_PORT = 8080;
	SocketAddress presenterAddress;
	int port;
	PrintWriter socketWriteChannel;
	BufferedReader socketReadChannel;
	Socket presenter;
	
	public Presenter() throws Exception{
		this("127.0.0.1", PRESENTER_PORT);
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
		
		// Check if presenter has sent any error messages
		try {
			presenter.setSoTimeout(500);
			inboundError = socketReadChannel.readLine();
			presenter.setSoTimeout(0);
			fromString = new JSONObject(new JSONTokener(inboundError));
			socketWriteChannel.println((new JSONObject().put("result", "success")).toString());
			throw new Exception(fromString.getString("error"));
			
		} catch (SocketException ex){
			throw new Exception ("Connection to Presenter lost");
		}
		catch (IOException ex) {
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
		} catch (ConnectException ex){
			throw new Exception("Connection refused while connecting to presenter", ex);
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
		
		outbound.put("Type", "Controller");
		if("Play".equalsIgnoreCase(command)){
			outbound.put("Action",command);
			outbound.put("Path", parameter);
		}else {
			outbound.put("Action",command);	
		}
		

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
		fromString = new JSONObject(new JSONTokener(inbound));
		if (fromString.getString("Result").equals("Failure")) {
			throw new Exception(fromString.getString("Reason"));
		} else if (fromString.getString("Result").equals("Success")) {
			;
		} else {
			throw new Exception("Invalid response from Presenter");
		}
	}
	
	// Sends a Play command to the Presenter to play the file specified
	public void Play(String filename) throws Exception{
		SendCommand("Play", filename);
	}
	
	// Sends a Pause command to the Presenter
	public void Pause() throws Exception{
		SendCommand("Pause", "");
	}
	
	// Sends a rewind command to the Presenter
	public void Rewind() throws Exception{
		SendCommand("Rewind", "");
	}
	
	// Sends a fast forward command to the Presenter
	public void Forward() throws Exception{
		SendCommand("Forward", "");
	}
	
	// Sends a stop command to the Presenter
	public void Stop() throws Exception{
		SendCommand("stop", "");
	}

}
