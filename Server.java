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
		ReadLineThread t = new ReadLineThread();
		(new Thread(t)).start();

		server = new UDPServer();
		server.start(9999);
	}
}
class ReadLineThread implements Runnable {
	public void run(){

		Scanner scanner = new Scanner(System.in); 
		while(true){
			String s = scanner.nextLine();
			Server.server.send(s);
			System.out.println("Sent: " + s);
		}
	}
}
