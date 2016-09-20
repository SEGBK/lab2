import lib.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server2 {
	public static UDPServer server;
	public static UDPClient client;

	public static void main(String[] args){
		// InetAddress addr = null;
		// try {	
		// 	addr = InetAddress.getByName(args[0]);
		// } catch (UnknownHostException e){
		// }
		ReadLineThread t = new ReadLineThread();
		(new Thread(t)).start();
		System.out.println("Pre Start");

		server = new UDPServer();
		server.start(9999);
		System.out.println("Post Start");
	}
}
class ReadLineThread implements Runnable {
	public void run(){

		System.out.println("Started");
		Scanner scanner = new Scanner(System.in); 
		while(true){
			System.out.println("Waiting");
			String s = scanner.nextLine();
			Server.server.send(s);
			System.out.println("Sent: " + s);
		}
	}
}
