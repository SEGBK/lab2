package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;

public class UDPServer extends NetworkStream{
	public DatagramSocket server;
	public void start(int port){
		InetAddress addr = null;
		try {	
			addr = InetAddress.getLocalHost();
			byte[] ipAddr = addr.getAddress();

			String hostname = addr.getHostName();
		    System.out.println("Server Name: " + addr.getHostAddress() + "\nServer Port: " + port);
		} catch (UnknownHostException e){

		}

		UDPServerListener dataListener = new UDPServerListener();
		receive(dataListener);

		try {
			server = new DatagramSocket(port, addr);
		}
		catch(SocketException ex){
			System.out.println(ex.getMessage());		
		}

		ListenerThread l = new ListenerThread(server);
		(new Thread(l)).start();
		//try {}
		//catch(SocketException ex){}
		//catch(IOException ex){}

	}

	public void send(final String data){

	}
}

class ListenerThread implements Runnable {
	private DatagramSocket socket; 

	public void run(){
		while(true){
			byte[] buf = new byte[256];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				String received = new String(p.getData(), 0, p.getLength());
				System.out.println(received);
			} catch (IOException ex){
				System.out.println(ex.getMessage());		
			}
		}
	}
	public ListenerThread(DatagramSocket socket){
		this.socket = socket;
	}
}

class UDPServerListener extends DataListener {

	public UDPServerListener() {}

	public void eventHandler(String data) {
		System.out.println(data);
	}
}