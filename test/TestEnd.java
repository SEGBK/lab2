/**
 * test/TestEnd.java - lab2
 * A runnable to signal the end of a test.
 */

package test;

public abstract class TestEnd {
    /**
     * Method to call when the test has ended.
     * @param passed a boolean signifying if the test passed.
     */
    public abstract void run(Boolean passed);
}