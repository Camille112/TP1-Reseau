
package stream;

import java.io.*;
import java.net.*;

public class ClientThreadListener extends Thread {
	private Socket socket;
	private BufferedReader socIn;

	ClientThreadListener(Socket s) {
		this.socket = s;
		try {
			this.socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			closeEverything();
		}
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		String messageReceived = "";
		while (socket.isConnected()) {
			try {
				messageReceived = socIn.readLine();
				System.out.println(messageReceived);
			} catch (Exception e) {
				closeEverything();
				break;
			}
		}

	}
	
	public void closeEverything() {
		try {
			socIn.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}