import lib.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Server {
	public static UDPServer server;
	public static UDPClient client;

	public static void main(String[] args){
		// InetAddress addr = null;
		// try {	
		// 	addr = InetAddress.getByName(args[0]);
		// } catch (UnknownHostException e){
		// }

		server = new UDPServer();
		server.start(9999);
	}
}
