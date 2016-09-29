/**
 * test/Runner.java - lab2
 * Runs tests and contains test helpers.
 */

package test;

public final class Runner {
    public static void main(String[] args) {
        new TestEnd() {
            private int i = -1, passed = 0;
            private Test[] tests = new Test[] {
                /**
                 * List of tests to run.
                 * All the objects below should implement
                 * the interface Test.
                 */
                new DataListenerTest(),
                new TCPStreamClientTest(),
                //new TCPStreamServerTest(),
                new UDPStreamTest()
            };

            public void run(Boolean success) {
                i ++;
                if (success != null && success) passed ++;

                if (i == tests.length) {
                    // if any tests failed, report failure to the shell
                    // and the user
                    System.out.format(
                        "\n\u001b[%dm%d/%d Tests passed.\u001b[39m\n\n",
                        passed < tests.length ? 31 : 32,
                        passed,
                        tests.length
                    );
                
                    System.exit(passed < tests.length ? -1 : 0);
                } else {
                    tests[i].run(this);
                }
            }
        }.run(null);
    }
}