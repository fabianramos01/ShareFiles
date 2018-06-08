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
	private long time;

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
			JOptionPane.showMessageDialog(null, ConstantList.PORT_ERROR, ConstantList.ERROR, JOptionPane.ERROR_MESSAGE);
		}
		frameHome.setVisible(true);
	}

	private void newConnection(String ip, int port) {
		try {
			String name = JOptionPane.showInputDialog(ConstantList.USER_NAME);
			int myPort = Integer.parseInt(JOptionPane.showInputDialog(ConstantList.GET_LOCAL_PORT));
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(fileChooser);
			managerUser = new ManagerUser(ip, port, name, myPort, fileChooser.getSelectedFile());
			managerUser.addObserver(this);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, ConstantList.CONNECTION_ERROR, ConstantList.ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case COMMAND_CONNECT:
			connect();
			break;
		case COMMAND_DOWNLOAD_FILE:
			download();
			break;
		case COMMAND_SELCET_USER:
			managerUser.requestFiles(frameHome.getSelectedUser().getName());
			break;
		}
	}

	private void download() {
		try {
			if (frameHome.getFileName() != null) {
				managerUser.downloadFile(frameHome.getSelectedUser().getName(), frameHome.getFileName());
				time = System.currentTimeMillis();
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

	@Override
	public void downloadFile() {
		int duration = (int) (System.currentTimeMillis() - time);
		JOptionPane.showMessageDialog(null,
				ConstantList.DOWNLOAD_FILE_MESS + "\n" + ConstantList.DURATION + duration + ConstantList.TIME_UNITS,
				ConstantList.DOWNLOAD_FILE, JOptionPane.INFORMATION_MESSAGE);

	}
}