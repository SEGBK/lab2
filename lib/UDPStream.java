/**
 * lib/UDPStream.java - lab2
 * Functionality for UDP communications.
 */

package lib;

import lib.NetworkStream;
import lib.DataListener;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.StringWriter;
import java.io.PrintWriter;

public class UDPStream extends NetworkStream {
	private final long PING_TIMEOUT = 30000;

	private int port;
	private long lastPing;
	private String server;
	private ArrayList<DataListener> onDataListeners = new ArrayList<DataListener>();
	private DatagramSocket socket;

	/**
     * Creates an instance of UDPStream as a client&server.
     * @param host a string containing the ip address or hostname of the server
     * @param hostPort an integer containing the port number to connect to
	 * @param port the port to open on local machine
     */
	public UDPStream(String host, int hostPort, int port) {
		this.server = host;
		this.port = hostPort;

		// open socket and grab errors
		try {
			this.socket = new DatagramSocket(port);
			this.socket.connect(InetAddress.getByName(host), hostPort);

			this.setup();
		} catch (Throwable t) {
			this.dispatchErrorListener(t.getMessage());
		}
	}

	/**
	 * Sets up the ping send/receive and data listener.
	 */
	private void setup() {
		// start the ping thread
		final UDPStream that = this;
		final Timer timer = new Timer();

		lastPing = System.currentTimeMillis();
		timer.schedule(new TimerTask() {
			public void run() {
				if (that.isOpen()) that.send("PP");

				long span = System.currentTimeMillis() - lastPing;
				if (span > PING_TIMEOUT) {
					that.close();
					timer.cancel();
				}
			}
		}, 1, PING_TIMEOUT / 5);

		// start the receiving thread
		new Thread() {
			public void run() {
				try {
					// wait for 5 seconds
					final long start = System.currentTimeMillis();
					while ((System.currentTimeMillis() - start) < 5000);

					// wait for the connection to be opened
					while (!that.isOpen());
					that.dispatchConnectListener();

					while (that.isOpen()) {
						// receive data size
						byte[] buffSize = new byte[4];
						DatagramPacket packet = new DatagramPacket(buffSize, buffSize.length);
						that.socket.receive(packet);
						int size = ByteBuffer.wrap(packet.getData()).getInt();

						//System.out.format("size :: %d\n", size);

						// receive actual data
						byte[] buffer = new byte[size];
						packet = new DatagramPacket(buffer, size);
						that.socket.receive(packet);

						// convert to string
						String data = new String(packet.getData());

						// ...
						//System.out.format("received: '%s'\n", data);

						// if ping, then reset counter
						if (data.equals("PP")) {
							//System.out.format("PING\n");
							lastPing = System.currentTimeMillis();
						} else {
							for (DataListener evt : that.onDataListeners) {
								evt.eventHandler(data);
							}
						}
					}
				} catch (Throwable t) {
					if (t.getMessage() != null) {
						if (!t.getMessage().equals("Socket closed")) {
                        	that.dispatchErrorListener(t.getMessage());
						}
                    } else {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        t.printStackTrace(pw);
                        that.dispatchErrorListener(sw.toString());
                    }
				} finally {
					that.close();
				}
			}
		}.start();
	}

	/**
	 * @return true if the stream is open
	 */
	public boolean isOpen() {
		return this.socket != null && !this.socket.isClosed()
				&& this.socket.isBound() && this.socket.isConnected();
	}

	/**
	 * Closes all threads related to stream.
	 */
	public void close() {
		if (!this.isOpen()) return;

		try {
			this.socket.close();
			this.dispatchDisconnectListener();
		} catch (Throwable t) {}
	}

	/**
	 * Sends the data packet down the stream.
	 * @param data the string of data to send
	 */
	public void send(String data) {
		if (this.isOpen()) {
			final UDPStream that = this;

			try {
				byte[] buffSize = new byte[4],
					   buffer = data.getBytes();

				// awesome wrapper class that lets you convert data
				// into bytes properly
				ByteBuffer.wrap(buffSize).putInt(buffer.length);

				// send the buffer with the size then the data
				if (!that.isOpen()) return;
				this.socket.send(new DatagramPacket(buffSize, buffSize.length));
				this.socket.send(new DatagramPacket(buffer, buffer.length));
			} catch (Throwable t) {
				this.dispatchErrorListener(t.getMessage());
			}
		}
	}

	/**
	 * Sets on data event handler.
	 * @param listener an instance of the data listener object
	 */
	public void receive(DataListener onData) {
		this.onDataListeners.add(onData);
	}
}
