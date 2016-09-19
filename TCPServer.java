import java.net.*;
import java.io.*;
public class TCPServer {
	public static void main (String args[]) {
		int serverPort = 7896; // the server port
	   	try {
	        InetAddress addr = InetAddress.getLocalHost();
	    
	        // Get IP Address
	        byte[] ipAddr = addr.getAddress();
	    
	        // Get hostname
	        String hostname = addr.getHostName();
	        System.out.println("Server Name: " + hostname + "\nServer Port: " + serverPort);
	    } catch (UnknownHostException e) {
	    }

		try{
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("Server is Ready");
			while(true) {
        System.out.println("listening to client sockets");
				Socket clientSocket = listenSocket.accept();
        System.out.println("connection found, creating a new connection thread");
				Connection c = new Connection(clientSocket);
			}
		} catch(IOException e) {System.out.println("IOException Listen socket:"+e.getMessage());}
	}
}
class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	public Connection (Socket aClientSocket) {
    System.out.println("in new connection thread");
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
	public void run(){
    System.out.println("server thread started");
		try {			                 // an echo server

			String data = in.readUTF();	                  // read a line of data from the stream
			out.writeUTF(data);
			System.out.println("Reply: "+data);
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
		

	}
}
