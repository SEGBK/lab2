/**
 * lib/NetworkStream.java - lab2
 * An interface for the TCPStream and UDPStream classes.
 */

package lib;

import java.util.ArrayList;

public abstract class NetworkStream {
    /**
     * Send data across the socket.
     * @param data the data to send over in string form
     */
    public abstract void send(final String data);

    /**
     * Closes the underlying connection.
     */
    public abstract void close();

    private ArrayList<DataListener> onDataListeners = new ArrayList<DataListener>(),
                                    onErrorListeners = new ArrayList<DataListener>();
    private ArrayList<Runnable> onConnectListeners = new ArrayList<Runnable>(),
                                onDisconnectListeners = new ArrayList<Runnable>();

    /**
     * Add an event listener for when errors happen.
     * @param listener an instance of DataListener to act as an event handler
     */
    public void onError(DataListener listener) {
        this.onErrorListeners.add(listener);
    }

    /**
     * Add an event listener for when data is received
     * from the other side.
     * @param listener an instance of DataListener to act as an event handler
     */
    public void receive(DataListener listener) {
        this.onDataListeners.add(listener);
    }

    /**
     * Dispatches the data event for all listeners with
     * given data. Not a public method.
     *
     * @param data the data to send to event handlers
     */
    protected void dispatchDataListener(String data) {
        for (DataListener listener : this.onDataListeners) {
            listener.eventHandler(data);
        }
    }

    /**
     * Dispatches the error event for all listeners with
     * given error message. Not a public method.
     *
     * @param data the data to send to event handlers
     */
    protected void dispatchErrorListener(String message) {
        for (DataListener listener : this.onErrorListeners) {
            listener.eventHandler(message);
        }
    }

    /**
     * Add an event listener for the connect event.
     * @param listener the event listener in the form of a Runnable
     */
    public void onConnect(Runnable listener) {
        this.onConnectListeners.add(listener);
    }

    /**
     * Add an event listener for the disconnect event.
     * @param listener the event listener in the form of a Runnable
     */
    public void onDisconnect(Runnable listener) {
        this.onDisconnectListeners.add(listener);
    }

    /**
     * Dispatch all the event listeners for the connect listeners.
     */
    protected void dispatchConnectListener() {
        for (Runnable listener : this.onConnectListeners) {
            listener.run();
        }
    }

    /**
     * Dispatch all the event listeners for the disconnect listeners.
     */
    protected void dispatchDisconnectListener() {
        for (Runnable listener : this.onDisconnectListeners) {
            listener.run();
        }
    }
}