/**
 * test/DataListenerTest.java - lab2
 * Unit tests for the DataListener class.
 */

package test;

import lib.DataListener;

class DataListenerTest extends Test {
    public DataListenerTest() {
        super("test lib.DataListener");
    }

    public void test(final Runnable end) throws Throwable {
        new DataListener() {
            public void eventHandler(String data) {
                Test.equal(data, "TST_EVT", "test event should receive proper data");
                end.run();
            }
        }.eventHandler("TST_EVT");
    }
}