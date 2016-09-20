/**
 * test/Runner.java - lab2
 * Runs tests and contains test helpers.
 */

package test;

public final class Runner {
    public static <T> void equal(T expected, T given, String message) {
        System.out.format(
            "\u001b[%dm %s %s%s\u001b[39m\n",
            expected == given ? 32 : 31,
            expected == given ? "\u2713" : "x",
            message,
            expected != given ? " (expected " + expected + ", got " + given + ")" : ""
        );
    }

    public static void main(String[] args) {
        Runner.equal(10, 15, "things should have happened");
    }
}