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
	
	// Sends a Play command to the Presenter to play the file specified
	public void play(String filename) throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("action","play");
		outbound.append("id", filename);

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
	}
	
	// Sends a Pause command to the Presenter
	public void pause() throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("action","pause");

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
	}
	
	// Sends a rewind command to the Presenter
	public void rewind() throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("action","rewind");

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
	}
	
	// Sends a fast forward command to the Presenter
	public void forward() throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("action","forward");

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
	}
	
	// Sends a stop command to the Presenter
	public void stop() throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("action","stop");

		socketWriteChannel.println(outbound.toString());
		
		inbound = socketReadChannel.readLine();
	}

}
