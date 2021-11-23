package controller;

import stream.EchoClient;
import view.Chat;
import view.LandingPage;

public class Controller {
	LandingPage landingPage;
	EchoClient ec;
	Chat chat;
	
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
	}
	
	public void sendMessage(String message) {
		ec.sendMessage(message);
	}
	
	public void receiveMessage(String message) {
		System.out.println("before chat");
		chat.receiveMessage(message);
		System.out.println("after chat");
	}
	
	public void leaveChat() {
		ec.sendMessage("#DISCONNECT");
	}
	
	public void addGroup(String newGroupName, String newGroupMembers,String username) {
		chat.getGroupForm().getPage().dispose();
		chat.addGroup(newGroupName, newGroupMembers+"\n"+username);
		ec.sendMessage("#CREATEGROUP#"+newGroupName+"#"+newGroupMembers+"\n"+username);		
	}

}