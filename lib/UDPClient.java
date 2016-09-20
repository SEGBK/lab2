package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;

public class UDPClient extends NetworkStream{
	private int port;
	private InetAddress addr;
	private DatagramSocket socket;
	private DatagramPacket packet;

	public void connect(InetAddress address, int port){
		this.port = port;
		this.addr = address;

		UDPServerListener dataListener = new UDPServerListener();
		receive(dataListener);

		try {
			socket = new DatagramSocket();
		}
		catch(SocketException ex){
			System.out.println(ex.getMessage());		
		}

		socket.connect(address, port);
		while(!socket.isConnected()){
			System.out.println("Waiting for connection...");
		}
		try {
			String handshakeData = "@" + addr.getHostAddress() + "@";
			byte[] buf = handshakeData.getBytes();
			DatagramPacket handshake = new DatagramPacket(buf, buf.length, addr, port);
			socket.send(handshake);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}

		ListenerThread l = new ListenerThread(socket);
		(new Thread(l)).start();

	}
	public void send(final String data){
		try {
			byte[] buf = data.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
			socket.send(packet);
		} catch (IOException ex){
			System.out.println(ex.getMessage());		
		}
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