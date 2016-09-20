import lib.*;

public class UDP {
	public static UDPServer server;
	public static UDPClient client;

	public static void main(String[] args){
		server = new UDPServer();
		client = new UDPClient();
		server.start(9999);
		client.connect(9999);
	}
}
