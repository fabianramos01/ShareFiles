package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

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
	}

	private void connect() {
		String[] info = frameHome.getInfo();
		if (!info[1].equals("")) {
			newConnection(info);
		} else {
			JOptionPane.showMessageDialog(null, ConstantList.PORT_ERROR, ConstantList.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void newConnection(String[] info) {
		try {
			managerUser = new ManagerUser(info[0], Integer.parseInt(info[1]), info[2], Integer.parseInt(info[3]),
					new File(info[4]));
			managerUser.addObserver(this);
			frameHome.init();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, ConstantList.CONNECTION_ERROR, ConstantList.ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case COMMAND_CONNECT:
			// connect();
			break;
		case COMMAND_DOWNLOAD_FILE:
			downloadOption();
			break;
		case COMMAND_SELCET_USER:
			managerUser.requestFiles(frameHome.getSelectedUser().getName());
			break;
		case COMMAND_SHOW_FILECHOOSER:
			frameHome.showFileChooser();
			break;
		case COMMAND_ACCEPT_INFO:
			connect();
			break;
		}
	}

	private void downloadOption() {
		int option = JOptionPane.showOptionDialog(null, ConstantList.DOWNLOAD_TYPE, ConstantList.DOWNLOAD_FILE,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new Object[] { ConstantList.FROM_SERVER, ConstantList.FROM_USER }, ConstantList.FROM_SERVER);
		if (option == 0) {
			downloadFromS();
		} else if (option == 1) {
			downloadFromU();
		}
	}

	private void downloadFromU() {
		try {
			time = System.currentTimeMillis();
			managerUser.downloadDirectFile(
					new Socket(frameHome.getSelectedUser().getIp(), frameHome.getSelectedUser().getPort()),
					frameHome.getFileName());
			downloadFile();
		} catch (IOException e) {
			System.err.println(ConstantList.CONNECTION_ERROR);
		}
	}

	private void downloadFromS() {
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