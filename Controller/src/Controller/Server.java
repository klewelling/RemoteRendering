package Controller;

import java.io.*;
import java.net.*;

import org.json.*;

public class Server {
	
	static final int SERVER_PORT = 10022;
	SocketAddress serverAddress;
	int port;
	PrintWriter socketWriteChannel;
	BufferedReader socketReadChannel;
	Socket server;
	
	Server() throws Exception{
		this("1.1.1.1", SERVER_PORT);
	}
	
	Server(String ipAddress, int port) throws Exception{
		this.port = port;
		try {
			this.serverAddress = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
		} catch (UnknownHostException ServEx) {
			throw new Exception("Unknown Server Host", ServEx);
		}
		
		Connect();
	}
	
	public void Disconnect() throws IOException{
		this.server.close();
	}
		
	public void Connect() throws Exception{	
		// Attempt to connect to Server
		this.server = new Socket();
		try {
			this.server.connect(serverAddress, 60000);
		} catch (SocketTimeoutException ex) {
			throw new Exception("Timed out while connecting to server", ex);
		}
		// If server connection fails; alert user
		if (!this.server.isConnected()){
			throw new Exception("Unable to connect to Server");
		} else {
			this.server.setKeepAlive(true);
		}
		
		socketWriteChannel = new PrintWriter(this.server.getOutputStream(), true);
		socketReadChannel = new BufferedReader(new InputStreamReader(this.server.getInputStream()));		
	}

	
	// Sends a search command to the Server, with a string containing the search params
	public String[] search(String searchParams) throws Exception{
		JSONObject outbound = new JSONObject();
		String inbound;
		
		outbound.append("Artist", searchParams);
		socketWriteChannel.println(outbound.toString());
		inbound = socketReadChannel.readLine();
		// Parse list from inbound info
		return new String[0]; // This will change
	}
	
}
