package lib;

import java.net.*;
import java.io.*;
import lib.NetworkStream;
import java.util.ArrayList;

public class UDPStream extends NetworkStream{
	private DatagramSocket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

	public UDPStream(final String ip, final int port) {
		UDPStream context = this;
		this.onDisconnect(new Runnable(){
			public void run(){
				context.close()
			}
		});
	}

	public UDPStream(final int port) {
		UDPStream context = this;
		this.onDisconnect(new Runnable(){
			public void run(){
				context.close()
			}
		});

	}

	private void setup() {
		UDPStream context = this;
		new Thread(new Runnable(){
			public void run(){

			}
		}).start();
	}

    public void send(final String data){
		if (this.socket != null && this.socket.isConnected()) {
			try {
				byte[] buf = data.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.send(packet);
			} catch (IOException ex){
				System.out.println(ex.getMessage());		
			}
		}
	}

	private void close() {
		socket.disconnect();
		socket.close();
	}
}