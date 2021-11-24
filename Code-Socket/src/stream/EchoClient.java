package stream;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import controller.Controller;

/**
 * EchoClient is the class that defines a TCP Client (thread that send the messages).
 * 
 * A EchoClient is composed of:
 * <ul>
 * <li>A Socket : socket to which the client is connected and on which messages between the server and the client are exchanged</li>
 * <li>A BufferedReader socIn : connected to the socket and receives data in form of Strings</li>
 * <li>A PrintStream socOut : connected to the socket and sends data in form of Strings</li>
 * <li>A String : username of the Client</li>
 * <li>A ClientThreadListener : list of the groups of which the client is a member</li>
 * <li>A Controller : a controller that will communicate with the GUI.</li>
 * </ul>
 * 
 * 
 * @author Camille MIGOZZI & Karina DU
 */

public class EchoClient {
	private String username = "";
	private Socket echoSocket = null;
	private PrintStream socOut = null;
	private BufferedReader socIn = null;
	private ClientThreadListener ctl;
	private Controller controller;
	
	/**
	 * Class constructor.
	 * Create an EchoClient and connect its socket to the server.
	 * 
	 * @param controller Controller that communicates between GUI and back end.
	 * @param username String that defines the username of the user.
	 * 
	 * @see controller.Controller
	 */	
	public EchoClient(Controller controller, String username) {
		try {
			this.controller = controller;
			this.username = username;
			echoSocket = new Socket("", 11);
			socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			socOut = new PrintStream(echoSocket.getOutputStream());
			socOut.println(username);
		} catch (Exception e) {
			closeEverything();
			//System.err.println("Error in EchoClient :" + e);
		}
	}
	
	/**
	 * Send message to the server.
	 * 
	 * @param messageToSend String of the message to send.
	 * 
	 */	
	public void sendMessage(String messageToSend) {
		socOut.println(username + " : " + messageToSend);
		if(messageToSend.equals("STOP")) {
			closeEverything();
		}
	}
	
	/**
	 * Create a thread listener that will listen every message received from the server.
	 *  
	 */	
	
	public void messageListener() {
		ctl = new ClientThreadListener(controller, echoSocket);
		ctl.start();
	}
	
	/**
	 * Change the username.
	 * 
	 * @param newUsername String of the new username.
	 * 
	 */
	public void changeUsername(String newUsername) {
		this.username=newUsername;
	}
	
	/**
     * This method closes the Socket, the BufferedReader, the PrintStream and close everything in the clientThreadListener.
     * 
     */
	public void closeEverything() {
		try {
			socOut.close();
			socIn.close();
			echoSocket.close();
			ctl.closeEverything();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
