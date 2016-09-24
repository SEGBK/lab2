/**
 * test/TCPStreamClientTest.java - lab2
 * Unit tests for TCPStream class in a client role.
 */

package test;

import lib.TCPStream;
import lib.DataListener;
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

    public void test(final Runnable end) throws Throwable {
        // create a new instance of TCPStream as client
        final TCPStream client = new TCPStream("localhost", 8080);

        // array of inputs that should also be outputs
        final String[] inputs = "1,2,3,4,5,6,7,8,9".split(",");

        // create a wrapper for the end
        final FBool expectDone = new FBool();
        final Runnable done = new Runnable() {
            public void run() {
                Test.equal(expectDone.state(), true, "should not end prematurely");
                end.run();
            }
        };

        // at this time, the socket should be closed
        Test.equal(client.isOpen(), false, "socket should be closed initially");

        // bind to connection event
        client.onConnect(new Runnable() {
            private boolean testRun = false;
            public void run() {
                Test.equal(testRun, false, "should only call onConnect once");
                Test.equal(client.isOpen(), true, "socket should be open during onConnect");
                testRun = true;

                // write all inputs to pipe
                for (String data : inputs) {
                    System.out.format("send :: %s\n", data);
                    client.send(data);
                }
            }
        });

        // bind the end wrapper to the disconnect event
        client.onDisconnect(done);

        // bind to the error event
        client.onError(new DataListener() {
            public void eventHandler(String error) {
                Test.equal(error, "null", "should never error out");
            }
        });

        // bind to the data event
        client.receive(new DataListener() {
            private int i = 0;
            public void eventHandler(String data) {
                String[] parsed = data.split(":");
                
                Test.equal(parsed.length, 2, "should receive two-part data");
                Test.equal(parsed[0], "server", "should receive server headers as data");
                
                try {
                    Integer.parseInt(parsed[1], 10);
                    Test.equal(true, true, "should parse second part as integer");
                } catch (NumberFormatException ex) {
                    Test.equal(true, false, "should not fail to parse integer");
                }

                if ((++ i) == inputs.length) {
                    expectDone.set(true);
                    client.close();
                }
            }
        });

        // now we can accept a connection
        final Socket server = new ServerSocket(8080).accept();

        // setup server as an echo server
        new Thread() {
            private BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

            public void run() {
                // loop and echo the data back, if things
                // go wrong, just ignore
                try {
                    while (true) {
                        while (!this.reader.ready());

                        String rl = this.reader.readLine();
                        System.out.format("server echoes => %s\n", rl);
                        this.writer.write("server:" + rl);
                        this.writer.newLine();
                        this.writer.flush();
                    }
                } catch (Throwable th) {System.out.format("%s\n",th.getMessage());}
            }
        }.start();
    }
}