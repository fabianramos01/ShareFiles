package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

import controller.ICObservable;
import controller.ICObserver;
import model.MyThread;

public class Server extends MyThread implements IObserver, ICObservable {

	private static final String SERVER = "Servidor";
	private static final int SLEEP = 1000;
	private ServerSocket serverSocket;
	private ArrayList<Connection> connections;
	private Socket socket;
	private ICObserver icObserver;

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
			Connection connection = new Connection(socket, Calendar.getInstance());
			connection.addObserver(this);
			connections.add(connection);
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	private void sendUsersList(Connection connection) {
		ArrayList<Connection> users = new ArrayList<>();
		for (Connection actual : connections) {
			if (!(connection == actual)) {
				users.add(actual);
			}
		}
		connection.sendUsers(connection.getName() + ConstantList.USERS_FILE, users);
	}

	@Override
	public void update(Connection connection) {
		ConstantList.LOGGER.log(Level.INFO,
				"Nueva conexion " + connection.getName() + " - " + connection.getInetAddress());
		icObserver.update();
		for (Connection actual : connections) {
			sendUsersList(actual);
		}
	}

	@Override
	public void removeConnection(Connection connection) {
		connections.remove(connection);
		icObserver.update();
		for (Connection actual : connections) {
			sendUsersList(actual);
		}
	}

	@Override
	public void sendFileList(Connection connection, String userName) {
		for (Connection user : connections) {
			if (userName.equals(user.getName())) {
				connection.sendUsersFile(user.getFileList());
				break;
			}
		}
	}

	@Override
	public void downloadFile(Connection connection, String user, String fileName) {
		for (Connection actual : connections) {
			if (user.equals(actual.getName())) {
				actual.shareFile(fileName);
				break;
			}
		}
	}

	public ArrayList<String> getConnections() {
		ArrayList<String> list = new ArrayList<>();
		for (Connection connection : connections) {
			list.add(connection.getName() + "  " + connection.getInetAddress());
//			list.add(connection.getName() + "  " + connection.getInetAddress() + "  " + connection.getConnTime());
		}
		return list;
	}

	@Override
	public void addObserver(ICObserver icObserver) {
		this.icObserver = icObserver;
	}

	@Override
	public void removeObserver(ICObserver icObserver) {
		this.icObserver = null;
	}
}