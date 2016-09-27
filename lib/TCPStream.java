/**
 * lib/TCPStream.java - lab2
 * An implementation of event-based TCP streaming
 * server & client.
 */

package lib;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TCPStream extends NetworkStream {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * Creates an instance of TCPStream as a client.
     * @param host a string containing the ip address or hostname of the server
     * @param port an integer containing the port number to connect to
     */
    public TCPStream(final String host, final int port) {
        final TCPStream that = this;

        this.onDisconnect(new Runnable() {
            public void run() {
                that.close();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    that.socket = new Socket(host, port);
                    that.setup();
                } catch (Throwable t) {
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

        this.onDisconnect(new Runnable() {
            public void run() {
                that.close();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    // await connection, then let GBC destroy the server
                    that.socket = new ServerSocket(port).accept();
                    that.setup();
                } catch (Throwable t) {
                    if (t.getMessage() != null) {
                        that.dispatchErrorListener(t.getMessage());
                    } else {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        t.printStackTrace(pw);
                        that.dispatchErrorListener(sw.toString());
                    }
                }
            }
        }).start();
    }

    /**
     * Returns the state of the TCPStream.
     * @return true if the socket is open, false otherwise
     */
    public boolean isOpen() { return this.socket != null && this.socket.isConnected(); }

    /**
     * Create the i/o streams and start their processes.
     */
    private void setup() {
        final TCPStream that = this;

        new Thread(new Runnable() {
            public void run() {
                try {
                    // grab i/o
                    that.reader = new BufferedReader(new InputStreamReader(that.socket.getInputStream()));
                    that.writer = new BufferedWriter(new OutputStreamWriter(that.socket.getOutputStream()));

                    // dispatch connected event
                    that.dispatchConnectListener();

                    // as data comes in, dispatch events
                    while (that.socket.isConnected()) {
                        while (!that.reader.ready());
                        that.dispatchDataListener(that.reader.readLine());
                    }
                } catch (Throwable t) {
                    if (t.getMessage() != null) {
                        if (!t.getMessage().equals("Stream closed")) {
                            that.dispatchErrorListener(t.getMessage());
                        }
                    } else {
                        //System.out.format("error: %s\n", t.getMessage());
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        t.printStackTrace(pw);
                        that.dispatchErrorListener(sw.toString());
                    }
                } finally {
                    // then dispatch disconnect
                    that.dispatchDisconnectListener();
                }
            }
        }).start();
    }

    /**
     * Send data across the socket.
     * @param data the data to send over in string form
     */
    public void send(final String data) {
        if (this.socket != null && this.socket.isConnected()) {
            try {
                if (!this.socket.isConnected()) return;
                this.writer.write(data);
                this.writer.newLine();
                this.writer.flush();
            } catch (Throwable t) {
                if (t.getMessage() != null) {
                    this.dispatchErrorListener(t.getMessage());
                } else {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);
                    this.dispatchErrorListener(sw.toString());
                }
            }
        } else this.dispatchErrorListener("Error: Connection is not open.");
    }

    /**
     * Close the underlying connection.
     */
    public void close() {
        if (this.socket != null && this.socket.isConnected()) {
            try {
                if (this.reader != null) this.reader.close();
                if (this.writer != null) this.writer.close();
                this.socket.close();
            } catch (Throwable t) {}
        } else this.dispatchErrorListener("Error: Connection is not open.");
    }
}