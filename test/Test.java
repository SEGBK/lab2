/**
 * test/Test.java - lab2
 * An abstract class describing a runnable test.
 */

package test;

abstract class Test {
    private String name;
    private static int passed, attempted;

    /**
     * Runs the assertions for this test. The assertion
     * of 'does not throw' is automatically wrapped over
     * the test.
     * @param end a runnable that should be called at the end of a test
     */
    public abstract void test(Runnable end) throws Throwable;

    /**
     * Tests if two values are equal. Passes the assertion
     * if the values are equal, otherwise fails.
     * @param expected the value that is expected from the test
     * @param given the value that was generated by the test
     * @param message a string explaining the assertion to the user
     */
    public static <T> void equal(T given, T expected, String message) {
        boolean success = expected.equals(given);

        // writes the assertion in red upon failure,
        // and green upon success. a checkmark preceeds the
        // success message and an x preceeds a failed message
        System.out.format(
            " \u001b[%dm%s %s%s\u001b[39m\n",
            success ? 32 : 31,
            success ? "\u2713" : "x",
            message,
            !success ? " (expected " + expected + ", got " + given + ")" : ""
        );

        // talls the number of assertions run by this test
        attempted += 1;
        passed += success ? 1 : 0;
    }

    /**
     * Runs the test and measures success rate.
     */
    public void run(final TestEnd next) {
        final Test that = this;

        try {
            System.out.format("\n\u001b[36m%s\u001b[39m\n\n", this.name);
            this.test(new Runnable() {
                public void run() {
                    System.out.println();
                    System.out.format(" \u001b[36m%d/%d assertions passed\u001b[39m\n", that.passed, that.attempted);
                    next.run(that.passed == that.attempted);
                }
            });
        } catch (Throwable th) {
            this.attempted += 1;
            th.printStackTrace();
            System.out.format("\n \u001b[31mTest threw exception: %s\u001b[39m\n", th.getMessage());
            System.out.format(" \u001b[36m%d/%d assertions passed\u001b[39m\n", this.passed, this.attempted);
        }
    }

    /**
     * Creates a new test with a given name.
     */
    public Test(String testName) {
        this.passed = 0;
        this.attempted = 0;
        this.name = testName;
    }
}