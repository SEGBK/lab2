/**
 * lib/TCPStream.java - lab2
 * An implementation of event-based TCP streaming
 * server & client.
 */

package lib;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.ArrayList;

public class TCPStream extends NetworkStream {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean open = false;
    private ArrayList<Runnable> onConnectListeners = new ArrayList<Runnable>(),
                                onDisconnectListeners = new ArrayList<Runnable>();

    /**
     * Creates an instance of TCPStream as a client.
     * @param host a string containing the ip address or hostname of the server
     * @param port an integer containing the port number to connect to
     */
    public TCPStream(final String host, final int port) {
        final TCPStream that = this;

        new Thread(new Runnable() {
            public void run() {
                try {
                    that.socket = new Socket(host, port);
                    that.setup();
                } catch (Throwable t) {
                    that.open = false;
                    that.dispatchErrorListener(t.getMessage());
                }
            }
        }).start();
    }

    /**
     * Creates an instance of TCPStream as a server.
     * @param port an integer containing the port number to bind to
     */
    public TCPStream(final int port) {
        final TCPStream that = this;

        new Thread(new Runnable() {
            public void run() {
                try {
                    // await connection, then let GBC destroy the server
                    that.socket = new ServerSocket(port).accept();
                    that.setup();
                } catch (Throwable t) {
                    that.open = false;
                    that.dispatchErrorListener(t.getMessage());
                }
            }
        }).start();
    }

    /**
     * Returns the state of the TCPStream.
     * @return true if the socket is open, false otherwise
     */
    public boolean isOpen() { return this.open; }

    /**
     * Add an event listener for the connect event.
     * @param listener the event listener in the form of a Runnable
     */
    public void onConnect(Runnable listener) {
        this.onConnectListeners.add(listener);
    }

    /**
     * Add an event listener for the disconnect event.
     * @param listener the event listener in the form of a Runnable
     */
    public void onDisconnect(Runnable listener) {
        this.onDisconnectListeners.add(listener);
    }

    /**
     * Dispatch all the event listeners for the connect listeners.
     */
    private void dispatchConnectListener() {
        for (Runnable listener : this.onConnectListeners) {
            listener.run();
        }
    }

    /**
     * Dispatch all the event listeners for the disconnect listeners.
     */
    private void dispatchDisconnectListener() {
        for (Runnable listener : this.onDisconnectListeners) {
            listener.run();
        }
    }

    /**
     * Create the i/o streams and start their processes.
     */
    private void setup() {
        final TCPStream that = this;

        new Thread(new Runnable() {
            public void run() {
                try {
                    that.open = true;

                    // dispatch connected event
                    that.dispatchConnectListener();

                    // grab i/o
                    that.reader = new BufferedReader(new InputStreamReader(that.socket.getInputStream()));
                    that.writer = new BufferedWriter(new OutputStreamWriter(that.socket.getOutputStream()));

                    // as data comes in, dispatch events
                    while (that.isOpen()) {
                        while (!that.reader.ready());
                        that.dispatchDataListener(that.reader.readLine());
                    }

                    // then dispatch disconnect
                    that.dispatchDisconnectListener();
                } catch (Throwable t) {
                    that.open = false;
                    that.dispatchErrorListener(t.getMessage());
                }
            }
        }).start();
    }

    /**
     * Send data across the socket.
     * @param data the data to send over in string form
     */
    public void send(final String data) {
        final TCPStream that = this;

        if (this.open) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        that.writer.write(data);
                        that.writer.newLine();
                        that.writer.flush();
                    } catch (Throwable t) {
                        that.open = false;
                        that.dispatchErrorListener(t.getMessage());
                    }
                }
            }).start();
        }
    }
}