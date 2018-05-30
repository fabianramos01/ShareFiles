package runner;

import java.io.IOException;

import javax.swing.JOptionPane;

import network.ConstantList;
import network.Server;

public class RunnerServerShare {

	public static void main(String[] args) {
		int port = Integer.parseInt(JOptionPane.showInputDialog(ConstantList.GET_PORT));
		try {
			new Server(port);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
