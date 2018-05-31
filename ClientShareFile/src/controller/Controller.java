package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import network.IObserver;
import network.ManagerUser;
import view.FrameHome;

public class Controller implements ActionListener, ItemListener, IObserver {

	private ManagerUser managerUser;
	private FrameHome frameHome;

	public Controller() {
		frameHome = new FrameHome(this);
		connect();
	}

	private void connect() {
		frameHome.setVisible(false);
		String ip = JOptionPane.showInputDialog(ConstantList.GET_IP);
		String port = JOptionPane.showInputDialog(ConstantList.GET_PORT);
		if (!port.equals("")) {
			newConnection(ip, Integer.parseInt(port));
		} else {
			JOptionPane.showMessageDialog(null, ConstantList.PORT_ERROR, ConstantList.ERROR,
					JOptionPane.ERROR_MESSAGE);			
		}	
		frameHome.setVisible(true);
	}
	
	private void newConnection(String ip, int port) {
		try {
			String name = JOptionPane.showInputDialog(ConstantList.USER_NAME);
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(fileChooser);
			managerUser = new ManagerUser(ip, port, name, fileChooser.getSelectedFile());
			managerUser.addObserver(this);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, ConstantList.CONNECTION_ERROR, ConstantList.ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case COMMAND_CHANGE_IP:
//			connect();
			break;
		case COMMAND_DOWNLOAD_FILE:
			download();
			break;
		case COMMAND_SELCET_USER:
			managerUser.requestFiles(frameHome.getSelectedUser());
			break;
		}
	}

	private void download() {
		try {
			if (frameHome.getFileName() != null) {				
				managerUser.downloadFile(frameHome.getSelectedUser(), frameHome.getFileName());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println(e.getItem());
		if (e.getStateChange() == ItemEvent.SELECTED) {
			managerUser.requestFiles(e.getItem().toString());
		}
	}

	@Override
	public void newUser() {
		frameHome.refreshUserList(managerUser.getUser().getUsers());
	}

	@Override
	public void userListFile() {
		frameHome.loadFileList(managerUser.getUser().getFileList());
		
	}
}