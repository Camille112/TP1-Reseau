/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientOutThread extends Thread {

	private Socket clientSocket;

	ClientOutThread(Socket s) {
		this.clientSocket = s;
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void send(String line) {
		try {
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
			socOut.println(line);
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

}