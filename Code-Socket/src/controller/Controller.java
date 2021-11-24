package controller;

import stream.EchoClient;
import view.Chat;
import view.LandingPage;

/**
 * Controller is the class that links the graphical interface of the chat with the echo client that communicates with the server.
 * 
 * A controller is composed of :
 * <ul>
 * <li>A Landing Page where the user will choose its username.</li>
 * <li>An EchoClient which will communicate with the server and exchange messages.</li>
 * </ul>
 * 
 * @author Camille Migozzi and Karina Du
 * 
 * @see stream.EchoClient
 * @see view.LandingPage
 *
 */
public class Controller {
	LandingPage landingPage;
	EchoClient ec;
	Chat chat = null;
	
	/**
	 * Default class constructor.
	 * The controller will automatically create a landingPage.
	 * 
	 * @see view.LandingPage
	 */
	public Controller () {
		landingPage = new LandingPage(this);
	}
	
	/**
	 * Close the landingPage.
	 * 
	 * @see view.LandingPage
	 */
	public void closeLandingPage() {
		landingPage.getPage().dispose();
	}
	
	/**
	 * Create a Chat the graphical interface for the user to enter messages and see its received messages.
	 * Create an Echo Listener that will exchange messages with the server.
	 * 
	 * @param username String of the username chose by the user.
	 *
	 * @see view.Chat
	 * @see stream.EchoClient
	 */
	public void createChat(String username) {
		chat = new Chat (this, username);
		ec = new EchoClient(this, username);
		ec.messageListener();
		sendMessage("#INIT");
	}
	
	/**
	 * Send a message written by the user in the GUI to the EchoClient.
	 * 
	 * @param message String of the message written by the user.
	 *
	 * @see stream.EchoClient
	 */
	public void sendMessage(String message) {
		ec.sendMessage(message);
	}
	
	/**
	 * Receive a message from the EchoClient in the GUI to display it for the user.
	 * 
	 * @param message String of the message received.
	 *
	 * @see stream.EchoClient
	 */
	
	public void receiveMessage(String message) {
		chat.receiveMessage(message);
	}
	
	/**
	 * Leave the chat, it will disconnect the EchoClient.
	 *
	 * @see stream.EchoClient
	 */
	
	public void leaveChat() {
		ec.sendMessage("#DISCONNECT");
	}
	
	/**
	 * Add a chat group created by the user.
	 * 
	 * @param newGroupName String of the name of the group.
	 * @param newGroupMembers String of all the usernames of the member.
	 * @param username String of the username creator of the group.
	 *  
	 * @see stream.EchoClient
	 */
	public void addGroup(String newGroupName, String newGroupMembers,String username) {
		chat.getGroupForm().getPage().dispose();
		chat.addGroup(newGroupName, newGroupMembers+"\n"+username);
		String[] arrayMembers = newGroupMembers.split("\n");
		String members = "";
		for (int i=0; i<arrayMembers.length; i++) {
			members+=arrayMembers[i]+"#";
		}
		ec.sendMessage("#CREATEGROUP#"+newGroupName+"#"+members+username);		
	}
	
	/**
	 * Change the username.
	 * 
	 * @param newUsername String of the new username.
	 * 
	 * @see stream.EchoClient
	 */
	public void changeUsername(String newUsername) {
		ec.changeUsername(newUsername);
	}

}
