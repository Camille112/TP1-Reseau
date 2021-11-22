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

public class EchoClient {
	String username = "";
	Socket echoSocket = null;
	PrintStream socOut = null;
	BufferedReader socIn = null;
	Scanner sc = null;
	ClientThreadListener ctl;
	
	public EchoClient(String username, Scanner sc) {
		try {
			this.sc = sc;
			this.username = username;
			echoSocket = new Socket("", 11);
			socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			socOut = new PrintStream(echoSocket.getOutputStream());

			socOut.println(username);
		} catch (Exception e) {
			closeEverything();
		}
	}
	
	public void sendMessage() {
		sc = new Scanner(System.in);
		while(echoSocket.isConnected()) {
			String messageToSend = sc.nextLine();
			socOut.println(username + " : " + messageToSend);
			if(messageToSend.equals("STOP")) {
				closeEverything();
				break;
			}
		}
		sc.close();
	}
	
	public void messageListener() {
		ctl = new ClientThreadListener(echoSocket);
		ctl.start();
	}
	
	public void closeEverything() {
		try {
			sc.close();
			socOut.close();
			socIn.close();
			echoSocket.close();
			ctl.closeEverything();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your username :");
		String username = sc.nextLine();
		
		EchoClient ec = new EchoClient(username,sc);
		ec.messageListener();
		ec.sendMessage();
	}
}
