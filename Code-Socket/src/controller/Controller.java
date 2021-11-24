package controller;

import stream.EchoClient;
import view.Chat;
import view.LandingPage;

public class Controller {
	LandingPage landingPage;
	EchoClient ec;
	Chat chat = null;
	
	public Controller () {
		landingPage = new LandingPage(this);
	}
	
	public void closeLandingPage() {
		landingPage.getPage().dispose();
	}
	
	public void createEchoListener(String username) {
		ec = new EchoClient(this, username);
		ec.messageListener();
	}
	
	public void createChat(String username) {
		chat = new Chat (this, username);
		createEchoListener(username);
		sendMessage("#INIT");
	}
	
	public void sendMessage(String message) {
		ec.sendMessage(message);
	}
	
	public void receiveMessage(String message) {
		chat.receiveMessage(message);
	}
	
	public void leaveChat() {
		ec.sendMessage("#DISCONNECT");
	}
	
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
	
	public void changeUsername(String newUsername) {
		ec.changeUsername(newUsername);
	}

}
