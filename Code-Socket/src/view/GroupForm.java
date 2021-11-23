package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import controller.Controller;


public class GroupForm {

	private Controller controller;
	private JFrame page = new JFrame();
	private JTextArea groupName = new JTextArea();
	private JTextArea membersName = new JTextArea();
	private String newGroupName = "";
	private String newGroupMembers = "";
	private String username = "";
	
	public GroupForm(Controller controller, String username) {
		this.controller = controller;
		this.username = username;
		
		JLabel createAGroup = new JLabel("Create a group :");
		JLabel labelName = new JLabel("Name of the group :");
		JLabel labelMembers = new JLabel("Members username :");
		JButton buttonOk = new JButton("OK");
		
		Dimension dim = new Dimension(250, 500);
		page.setSize(dim);
		page.setPreferredSize(dim);
		page.setMinimumSize(dim);
		page.setMaximumSize(dim);
		page.setResizable(false);
		page.setLocationRelativeTo(null);
		page.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		page.setTitle("Group form");
		page.setVisible(true);
		page.setLayout(null);
 
		createAGroup.setLayout(null);
		labelName.setLayout(null);
		groupName.setLayout(null);
		labelMembers.setLayout(null);
		membersName.setLayout(null);
		buttonOk.setLayout(null);
 
		createAGroup.setSize(200, 40);
		createAGroup.setLocation(60, 10);
		labelName.setSize(200, 40);
		labelName.setLocation(20, 40);
		groupName.setEditable(true);
		groupName.setSize(150, 40);
		groupName.setLocation(25, 80);
		labelMembers.setSize(200, 40);
		labelMembers.setLocation(20, 140);
		membersName.setEditable(true);
		membersName.setSize(150, 150);
		membersName.setLocation(25, 180);
		buttonOk.setSize(100, 50);
		buttonOk.setLocation(70, 380);
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newGroupName = groupName.getText();
				newGroupMembers = membersName.getText();
				controller.addGroup(newGroupName,newGroupMembers,username);
			}
		});
 
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		groupName.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		membersName.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
 
		page.add(createAGroup);
		page.add(labelName);
		page.add(groupName);
		page.add(labelMembers);
		page.add(membersName);
		page.add(buttonOk);
	}
	
	public JFrame getPage() {
		return page;
	}

}
