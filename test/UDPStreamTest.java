/**
 * test/UDPStreamTest.java - lab2
 * Unit tests for UDPStream class in a client role.
 */

package test;

import lib.UDPStream;
import lib.DataListener;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

class UDPStreamTest extends Test {
    public UDPStreamTest() {
        super("test lib.UDPStream");
        this.setTimeout(30000);
    }

    public void test(final Runnable end) throws Throwable {
        // setup the "server"
        final DatagramSocket server = new DatagramSocket(8080);
        server.connect(InetAddress.getByName("localhost"), 8081);

        // create a new instance of UDPStream as client
        final UDPStream client = new UDPStream("localhost", 8080, 8081);

        // setup server as an echo server
        new Thread() {
            public void run() {
                // loop and echo the data back, if things
                // go wrong, just ignore
                try {
                    while (true) {
                        // receive size
                        byte[] buffer = new byte[4];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        server.receive(packet);

                        // receive data
                        int size = ByteBuffer.wrap(packet.getData()).getInt();
                        //System.out.format("preparing to receive %d bytes\n", size);
                        buffer = new byte[size];
                        packet = new DatagramPacket(buffer, buffer.length);
                        server.receive(packet);

                        String data = new String(packet.getData());
                        //System.out.format("data :: %s\n", data);

                        // if data is ping, ignore
                        if (data.equals("PP")) {
                            // send size
                            buffer = new byte[4];
                            ByteBuffer.wrap(buffer).putInt(2);
                            packet = new DatagramPacket(buffer, buffer.length);
                            server.send(packet);

                            // send data
                            buffer = "PP".getBytes();
                            packet = new DatagramPacket(buffer, buffer.length);
                            server.send(packet);

                            continue;
                        }

                        // send size
                        buffer = new byte[4];
                        ByteBuffer.wrap(buffer).putInt(size + 7);
                        packet = new DatagramPacket(buffer, buffer.length);
                        server.send(packet);

                        // send data
                        buffer = ("server:" + data).getBytes();
                        packet = new DatagramPacket(buffer, buffer.length);
                        server.send(packet);
                    }
                } catch (Throwable t) {
                    Test.equal(t.getMessage(), "null", "should not error out");
                }
            }
        }.start();

        // array of inputs that should also be outputs
        final String[] inputs = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };

        // create a wrapper for the end
        final FBool expectDone = new FBool();
        final Runnable done = new Runnable() {
            public void run() {
                Test.equal(expectDone.state(), true, "should not end prematurely");
                end.run();
            }
        };

        // bind to connection event
        client.onConnect(new Runnable() {
            private boolean testRun = false;
            public void run() {
                Test.equal(testRun, false, "should only call onConnect once");
                Test.equal(client.isOpen(), true, "socket should be open during onConnect");
                testRun = true;

                // write all inputs to pipe
                for (String data : inputs) {
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
    }
}