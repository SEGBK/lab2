import lib.*;
import java.net.*;
import java.io.*;

public class UDP {
	public static UDPServer server;
	public static UDPClient client;

	public static void main(String[] args){
		InetAddress addr = null;
		try {	
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e){
		}

		server = new UDPServer();
		client = new UDPClient();
		server.start(9999);
		System.out.println("Server Started");
		client.connect(addr,9999);
		System.out.println("Client Conntected");
		client.send("asdf");
		System.out.println("Message Sent");
	}
}
