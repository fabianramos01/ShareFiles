package controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import network.ConstantList;
import network.Server;
import view.FrameServer;

public class Controller implements ICObserver {

	private Server server;
	private FrameServer frameServer;
	
	public Controller() {
		int port = Integer.parseInt(JOptionPane.showInputDialog(ConstantList.GET_PORT));
		try {
			server = new Server(port);
			server.addObserver(this);
			frameServer = new FrameServer();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), ConstantList.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void update() {
		frameServer.refreshList(server.getConnections());
	}
}