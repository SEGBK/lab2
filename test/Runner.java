/**
 * test/Runner.java - lab2
 * Runs tests and contains test helpers.
 */

package test;

public final class Runner {
    public static void main(String[] args) {
        int attempted = 0, passed = 0;

        for (Test test : new Test[] {
            /**
             * List of tests to run.
             * All the objects below should implement
             * the interface Test.
             */
            new DataListenerTest(),
            //new TCPStreamClientTest(),
            //new TCPStreamServerTest(),
            //new UDPStreamClientTest(),
            //new UDPStreamServerTest()
        }) {
            attempted += 1;
            if (test.run()) passed += 1;
        }

        // if any tests failed, report failure to the shell
        // and the user
        System.out.format(
            "\n\u001b[%dm%d/%d Tests passed.\u001b[39m\n\n",
            passed < attempted ? 31 : 32,
            passed,
            attempted
        );

        System.exit(passed < attempted ? -1 : 0);
    }
}
