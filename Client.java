import lib.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	public static UDPServer server;
	public static UDPClient client;

	public static void main(String[] args){
		InetAddress addr = null;
		try {	
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e){
		}

		client = new UDPClient();
		client.connect(addr,9999);
		Scanner scanner = new Scanner(System.in); 
		while(true){
			String s = scanner.nextLine();
			client.send(s);
			System.out.println("Sent: " + s);
		}
	}
}
