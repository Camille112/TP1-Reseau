package streamudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * UDP_Client is the class that defines a Client connected to others with UDP
 * 
 * A UDP_Client is composed of:
 * <ul>
 * <li>A Scanner</li>
 * <li>A String username</li>
 * <li>A MulticastSocket</li>
 * <li>A String addressGroup</li>
 * <li>A UDP_ClientThreadListener</li>
 * </ul>
 * 
 * @author Camille MIGOZZI & Karina DU
 */
public class UDP_Client {
	Scanner sc = null;
	String username = "";
	MulticastSocket ms = null;
	String addressGroup = "224.0.0.0";
	UDP_ClientThreadListener ctl;
	
	/**
	 * Class constructor.
	 * Initializes : MulticastSocket, username and Scanner
	 */
	public UDP_Client(String username, Scanner sc) {
		try {
			this.sc = sc;
			this.username = username;
			ms = new MulticastSocket();
		} catch (Exception e) {
			closeEverything();
		}
	}

	/**
	 * This method sends a message in the form of a DatagramPacket through the MulticastSocket, so that other Clients receive it
	 */
	public void sendMessage() throws IOException {
		sc = new Scanner(System.in);
		while(true) {
			String messageToSend = sc.nextLine();
			messageToSend = username + " : " + messageToSend;
			DatagramPacket dp = new DatagramPacket(messageToSend.getBytes(),messageToSend.length(),InetAddress.getByName(addressGroup),5000);
			ms.send(dp);
			
			if(messageToSend.equals("STOP")) {
				closeEverything();
				break;
			}
		}
		sc.close();
	}
	
	/**
	 * This method initialize a UDP_ClientThreadListener to listen to incoming messages that other Clients might send
	 */
	public void messageListener() throws UnknownHostException, IOException {
		ctl = new UDP_ClientThreadListener(addressGroup, username);
		ctl.start();
	}
	
	/**
	 * This method closes the Scanner and the MulticastSocket
	 */
	public void closeEverything() {
		sc.close();
		ms.close();
	}
	
	/**
	 * Main method
	 * Initializes a new UDP_Client
	 */
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your username :");
		String username = sc.nextLine();
		
		UDP_Client ec = new UDP_Client(username,sc);
		ec.messageListener();
		ec.sendMessage();

	}

}
