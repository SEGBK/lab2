package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;

public class UDPClient extends NetworkStream{

	public void connect(int port){
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
			socket = new Socket(addr, port);
			System.out.println(socket.isConnected());
		}
		catch(SocketException ex){}
		catch(IOException ex){}

	}
	public void send(String data){
		
	}
}

class RecieveThread extends Thread {

}