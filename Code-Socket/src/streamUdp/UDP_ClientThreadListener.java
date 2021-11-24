package streamudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * UDP_ClientThreadListener is the class that has a running thread to listen for incoming messages from clients
 * 
 * A UDP_ClientThreadListener is composed of:
 * <ul>
 * <li>A String username</li>
 * <li>A MulticastSocket</li>
 * <li>A String addressGroup</li>
 * </ul>
 * 
 * UDP_ClientThreadListener inherits Thread
 * 
 * @author Camille MIGOZZI & Karina DU
 */
public class UDP_ClientThreadListener extends Thread{
	MulticastSocket ms = null;
	String addressGroup = "";
	String username = "";
	
	/**
	 * Class constructor.
	 * Initializes : MulticastSocket, addressGroup and username
	 */
	@SuppressWarnings("deprecation")
	public UDP_ClientThreadListener(String addressGroup, String username) throws UnknownHostException, IOException {
		this.ms = new MulticastSocket(5000);
		this.ms.joinGroup(InetAddress.getByName(addressGroup));
		this.addressGroup = addressGroup;
		this.username = username;
	}

	/**
	 * This method listens to incoming DatagramPacket on the MulticastSocket, converts them to a String and prints the message
	 */
	public void run() {
		while (true) {
			try {
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf,1024);
				ms.receive(dp);
				String str = new String(dp.getData(),0,dp.getLength());
				if(!str.contains(username)) {
					System.out.println(str);
				}
			} catch (Exception e) {
				closeEverything();
				break;
			}
		}

	}
	
	/**
	 * This method closes the MulticastSocket
	 */
	@SuppressWarnings("deprecation")
	public void closeEverything() {
		try {
			ms.leaveGroup(InetAddress.getByName(addressGroup));
			ms.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}