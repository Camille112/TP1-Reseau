/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import controller.Controller;

public class EchoClient {
	private String username = "";
	private Socket echoSocket = null;
	private PrintStream socOut = null;
	private BufferedReader socIn = null;
	private Scanner sc = null;
	private ClientThreadListener ctl;
	private Controller controller;
	
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
			System.err.println("Error in EchoClient :" + e);
		}
	}
	
	public void sendMessage(String messageToSend) {
		socOut.println(username + " : " + messageToSend);
		if(messageToSend.equals("STOP")) {
			closeEverything();
		}
	}
	
	public void messageListener() {
		ctl = new ClientThreadListener(controller, echoSocket);
		ctl.start();
	}
	
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
