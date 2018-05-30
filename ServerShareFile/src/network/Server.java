package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import model.MyThread;

public class Server extends MyThread implements IObserver {

	private static final String SERVER = "Servidor";
	private static final int SLEEP = 1000;
	private ServerSocket serverSocket;
	private ArrayList<Connection> connections;
	private Socket socket;

	public Server(int port) throws IOException {
		super(SERVER, SLEEP);
		serverSocket = new ServerSocket(port);
		connections = new ArrayList<>();
		ConstantList.LOGGER.log(Level.INFO, "Server create at port " + port);
		start();
	}

	@Override
	public void execute() {
		try {
			socket = serverSocket.accept();
			Connection connection = new Connection(socket);
			connection.addObserver(this);
			connections.add(connection);
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	private void sendUsersList(Connection connection) {
		ArrayList<String> users = new ArrayList<>();
		for (Connection actual : connections) {
			if (!(connection == actual)) {
				users.add(actual.getName());
			}
		}
		connection.sendUsers(connection.getName() + ConstantList.USERS_FILE, users);
	}

	@Override
	public void update(Connection connection) {
		ConstantList.LOGGER.log(Level.INFO,
				"Nueva conexion " + connection.getName() + " - " + connection.getInetAddress());
		for (Connection actual : connections) {
			sendUsersList(actual);
		}
	}

	@Override
	public void removeConnection(Connection connection) {
		connections.remove(connection);
	}

	@Override
	public void sendFileList(Connection connection, String userName) {
		for (Connection user : connections) {
			if (userName.equals(user.getName())) {
				connection.sendFile(user.getFileList());
				break;
			}
		}
	}
}