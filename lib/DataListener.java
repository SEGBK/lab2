/**
 * lib/DataListener.java - lab2
 * A simple class for creating data events.
 */

package lib;

public abstract class DataListener {
    /**
     * The method that will be called when the event is dispatched.
     * @param data the data that was received
     */
    public abstract void eventHandler(String data);
}