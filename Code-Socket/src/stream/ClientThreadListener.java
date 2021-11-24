
package stream;

import java.io.*;
import java.net.*;

import controller.Controller;


/**
 * ClientThreadListener is the class that defines a TCP Client (thread that listens the message).
 * 
 * A ClientThreadListener is composed of:
 * <ul>
 * <li>A Socket : socket to which the client is connected and on which messages between the server and the client are exchanged</li>
 * <li>A BufferedReader socIn : connected to the socket and receives data in form of Strings</li>
 * <li>A Controller : a controller that will communicate with the GUI.</li>
 * </ul>
 * 
 * 
 * @author Camille MIGOZZI & Karina DU
 */
public class ClientThreadListener extends Thread {
	private Socket socket;
	private BufferedReader socIn;
	private Controller controller;

	/**
	 * Class constructor.
	 * Create an ClientThreadListener that will listen on the socket.
	 * 
	 * @param controller Controller that communicates between GUI and back end.
	 * @param s Socket of the client that receive and send messages.
	 * 
	 * @see controller.Controller
	 */	
	ClientThreadListener(Controller controller, Socket s) {
		this.controller = controller;
		this.socket = s;
		try {
			this.socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			closeEverything();
			//System.err.println("Error in ClientThreadListener :" + e);
		}
	}

	/**
	 * Listen to every message receive and inform the controller.
	 * 
	 **/
	public void run() {
		String messageReceived = "";
		while (socket.isConnected()) {
			try {
				messageReceived = socIn.readLine();
				//System.out.println("msgReceived : "+messageReceived);
				if (messageReceived != null && !messageReceived.isBlank()) {
					controller.receiveMessage(messageReceived);
				}
			} catch (Exception e) {
				closeEverything();
				//System.err.println("Error in ClientThreadListener :" + e);
				break;
			}
		}
	}
	
	/**
     * This method closes the Socket and the BufferedReader.
     * 
     */
	public void closeEverything() {
		try {
			socIn.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}