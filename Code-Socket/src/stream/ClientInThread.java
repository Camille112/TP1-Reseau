/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientInThread extends Thread {

	private Socket clientSocket;
	private String line;
	private boolean newMsg;

	ClientInThread(Socket s) {
		this.clientSocket = s;
		this.line = "";
		this.newMsg=false;
	}
	
	public String getLine() {
		return line;
	}

	public boolean getNewMsg() {
		return newMsg;
	}

	public void setNewMsg(boolean newMsg) {
		this.newMsg = newMsg;
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		try {
			BufferedReader socIn = null;
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				line = socIn.readLine();
				//if (!line.isBlank()) {
					this.newMsg=true;
				//}
				System.out.println("received : " + line);
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

}