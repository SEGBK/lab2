/**
 * test/TCPStreamClientTest.java - lab2
 * Unit tests for TCPStream class in a client role.
 */

package test;

import lib.TCPStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class TCPStreamClientTest extends Test {
    public TCPStreamClientTest() {
        super("test lib.TCPStream as client");
    }

    public void test() throws Throwable {
        // create a new instance of TCPStream as client
        final TCPStream client = new TCPStream("localhost", 8080);

        // at this time, the socket should be closed
        Test.equal(client.isOpen(), false, "socket should be closed initially");

        // bind to connection event
        client.onConnect(new Runnable() {
            private boolean testRun = false;
            public void run() {
                Test.equal(testRun, false, "should only call onConnect once");
                Test.equal(client.isOpen(), true, "socket should be open during onConnect");
                testRun = true;
            }
        });

        // now we can accept a connection
        Socket server = new ServerSocket(8080).accept();

        // now, the socket should be open
        Test.equal(client.isOpen(), true, "socket should be open after accepting connection");
    }
}