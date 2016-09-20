package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;
import java.util.ArrayList;

public class UDPServer extends NetworkStream{
	private DatagramSocket server;
	private InetAddress addr = null;
	private int port;
	private ArrayList<InetAddress> clients;

	public void start(int port){
		clients = new ArrayList<InetAddress>();
		try {
			this.port = port;
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
		ServerListenerThread l = new ServerListenerThread(server, clients);
		(new Thread(l)).start();
	}

	public void send(final String data){
		try {
			for(int i = 0; i < clients.size(); i++){
				byte[] buf = data.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, clients.get(i), port);
				server.send(packet);	
			}
		} catch (IOException ex){
			System.out.println(ex.getMessage());		
		}
	}
}

class ServerListenerThread implements Runnable {
	private DatagramSocket socket; 
	private ArrayList<InetAddress> clients;
	public void run(){
		while(true){
			byte[] buf = new byte[256];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				if ((char)p.getData()[0] == '@' && (char)p.getData()[p.getLength()-1] == '@'){
					System.out.println("Handshake received");
					String received = new String(p.getData(), 0, p.getLength());
					clients.add(InetAddress.getByName(received.replace('@',' ').trim()));
					System.out.println("Added: " + received.replace('@',' '));
				} else {
					String received = new String(p.getData(), 0, p.getLength());
					System.out.println("Message from " + p.getAddress().getHostAddress().trim() + ": " + received);
				}

			} catch (IOException ex){
				System.out.println(ex.getMessage());		
			}
		}
	}
	public ServerListenerThread(DatagramSocket socket, ArrayList<InetAddress> clients){
		this.socket = socket;
		this.clients = clients;
	}
}

class UDPServerListener extends DataListener {

	public UDPServerListener() {}

	public void eventHandler(String data) {
		System.out.println(data);
	}
}