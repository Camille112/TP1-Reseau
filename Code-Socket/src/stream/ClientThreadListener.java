
package stream;

import java.io.*;
import java.net.*;

import controller.Controller;

public class ClientThreadListener extends Thread {
	private Socket socket;
	private BufferedReader socIn;
	private Controller controller;

	ClientThreadListener(Controller controller, Socket s) {
		this.controller = controller;
		this.socket = s;
		try {
			this.socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			closeEverything();
			System.err.println("Error in ClientThreadListener :" + e);
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
				System.out.println("try");
				messageReceived = socIn.readLine();
				System.out.println("msgReceived"+messageReceived);
				if (messageReceived != null && !messageReceived.isBlank()) {
					controller.receiveMessage(messageReceived);
				}
				System.out.println("receiveMessageSent");
			} catch (Exception e) {
				closeEverything();
				System.err.println("Error in ClientThreadListener :" + e);
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