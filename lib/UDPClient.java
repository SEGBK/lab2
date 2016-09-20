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

class RecieveThread extends Thread {

}