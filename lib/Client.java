package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;
import java.util.ArrayList;

public class Client {
	private InetAddress client;
	private int port;

	public Client(InetAddress client, int port){
		this.client = client;
		this.port = port;
	}

	public InetAddress getInetAddress(){
		return client;
	}
	public int getPort(){
		return port;
	}
}