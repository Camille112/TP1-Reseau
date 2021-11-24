package stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDP_Client {
	Scanner sc = null;
	String username = "";
	MulticastSocket ms = null;
	String addressGroup = "224.0.0.0";
	UDP_ClientThreadListener ctl;
	
	public UDP_Client(String username, Scanner sc) {
		try {
			this.sc = sc;
			this.username = username;
			ms = new MulticastSocket();
		} catch (Exception e) {
			closeEverything();
		}
	}

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
	
	public void messageListener() throws UnknownHostException, IOException {
		ctl = new UDP_ClientThreadListener(addressGroup, username);
		ctl.start();
	}
	
	public void closeEverything() {
		sc.close();
		ms.close();
	}
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your username :");
		String username = sc.nextLine();
		
		UDP_Client ec = new UDP_Client(username,sc);
		ec.messageListener();
		ec.sendMessage();

	}

}
