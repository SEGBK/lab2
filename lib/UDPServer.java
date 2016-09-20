package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;

public class UDPServer extends NetworkStream {
	public DatagramSocket socket;
	public void start(int port){
		try {		
			InetAddress addr = InetAddress.getLocalHost();
			byte[] ipAddr = addr.getAddress();

			String hostname = addr.getHostName();
		    System.out.println("Server Name: " + addr.getHostAddress() + "\nServer Port: " + port);
		} catch (UnknownHostException e){

		}

		UDPServerListener dataListener = new UDPServerListener();
		receive(dataListener);

		try {
			server = new ServerSocket(port);
		}
		catch(SocketException ex){}
		catch(IOException ex){}
		//try {}
		//catch(SocketException ex){}
		//catch(IOException ex){}

	}

	public void send(String data){

	}
}

class SendThread extends Thread {

}

class UDPServerListener extends DataListener {

	public UDPServerListener() {}

	public void eventHandler(String data) {
		System.out.println(data);
	}
}