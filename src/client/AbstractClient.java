package client;

import java.io.*;
import java.net.*;

public abstract class AbstractClient implements Runnable {

	private Socket				clientSocket;

	/**
	 * The stream to handle data going to the server.
	 */
	private ObjectOutputStream	output;

	/**
	 * The stream to handle data from the server.
	 */
	private ObjectInputStream	input;

	/**
	 * The thread created to read data from the server.
	 */
	private Thread				clientReader;

	/**
	 * Indicates if the thread is ready to stop. Needed so that the loop in the
	 * run method knows when to stop waiting for incoming messages.
	 */
	private boolean				readyToStop	= false;

	/**
	 * The server's host name.
	 */
	private String				host;

	/**
	 * The port number.
	 */
	private int					port;

	public AbstractClient(String host, int port) {
		// Initialize variables
		this.host = host;
		this.port = port;
	}

	final public void openConnection() throws IOException {
		// Do not do anything if the connection is already open
		if (isConnected())
			return;

		// Create the sockets and the data streams
		try {
			clientSocket = new Socket(host, port);
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException ex)
		// All three of the above must be closed when there is a failure
		// to create any of them
		{
			try {
				closeAll();
			} catch (Exception exc) {
			}

			throw ex; // Rethrow the exception.
		}

		clientReader = new Thread(this); // Create the data reader thread
		readyToStop = false;
		clientReader.start(); // Start the thread
	}
	final public void sendToServer(Object msg) throws IOException {
		if (clientSocket == null || output == null)
			throw new SocketException("socket does not exist");

		output.writeObject(msg);
	}

	final public void forceResetAfterSend() throws IOException {
   output.reset();
	}

	final public void closeConnection() throws IOException {
		// Prevent the thread from looping any more
		readyToStop = true;

		try {
			closeAll();
		} finally {
			// Call the hook method
			connectionClosed();
		}
	}

	final public boolean isConnected() {
		return clientReader != null && clientReader.isAlive();
	}

	final public int getPort() {
		return port;
	}

	final public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getHost() {
		return host;
	}

	final public void setHost(String host) {
		this.host = host;
	}

	final public InetAddress getInetAddress() {
		return clientSocket.getInetAddress();
	}

	final public void run() {
		connectionEstablished();

		// The message from the server
		Object msg;

		// Loop waiting for data

		try {
			while (!readyToStop) {
				// Get data from Server and send it to the handler
				// The thread waits indefinitely at the following
				// statement until something is received from the server
				msg = input.readObject();

				// Concrete subclasses do what they want with the
				// msg by implementing the following method
				handleMessageFromServer(msg);
			}
		} catch (Exception exception) {
			if (!readyToStop) {
				try {
					closeAll();
				} catch (Exception ex) {
				}

				connectionException(exception);
			}
		} finally {
			clientReader = null;
		}
	}

	protected void connectionClosed() {
	}

	protected void connectionException(Exception exception) {
	}

	protected void connectionEstablished() {
	}

	protected abstract void handleMessageFromServer(Object msg);

	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (clientSocket != null)
				clientSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			output = null;
			input = null;
			clientSocket = null;
		}
	}
}
//end of AbstractClient class
