/**
 * test/TimeoutHandler.java - lab2
 * Handles timeouts for tests to force hanging tests to fail.
 */

package test;

import java.util.Timer;
import java.util.TimerTask;

class TimeoutHandler {
    private long startTime = 0;
    private final Timeout timeout;
    private final FBool isOn = new FBool();
    private final RunnableContainer onFail = new RunnableContainer();

    /**
     * Creates a new timeout handler with a maximum
     * timeout in milliseconds.
     * @param timeout the maximum time to wait before failing
     */
    public TimeoutHandler(final long timeout) {
        final TimeoutHandler that = this;
        this.timeout = new Timeout(timeout);

        new Timer().schedule(new TimerTask() {
            public void run() {
                long now = System.currentTimeMillis();

                if (that.isOn.state()) {
                    if ((now - that.startTime) > that.timeout.get()) {
                        that.fail();
                    }
                }
            }
        }, 0, 100);
    }

    /**
     * Set the span of the timeout.
     * @param timeout the span given in milliseconds
     */
    public void setTimeout(long timeout) {
        this.timeout.set(timeout);
    }

    /**
     * Sets the on fail callback.
     * @param runnable the runnable to run when timeout fails
     */
    public void onFail(Runnable run) {
        this.onFail.set(run);
    }

    /**
     * Starts the wait for the timeout.
     */
    public void start() {
        this.reset();
        this.isOn.set(true);
    }

    /**
     * Resets the start time to extend timeout.
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Stops the timeout handler.
     */
    public void stop() {
        this.isOn.set(false);
    }

    /**
     * Causes the timeout to occur.
     */
    private void fail() {
        this.stop();
        this.onFail.run();
    }
}

class RunnableContainer {
    private Runnable runnable = new Runnable(){public void run(){}};
    public void set(Runnable runnable) { this.runnable = runnable; }
    public void run() { this.runnable.run(); }
}

class Timeout {
    private long timeout;
    public Timeout(long timeout) { this.set(timeout); }
    public void set(long timeout) { this.timeout = timeout; }
    public long get() { return this.timeout; }
}