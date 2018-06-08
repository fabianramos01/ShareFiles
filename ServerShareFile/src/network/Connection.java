package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import model.MyThread;
import persistence.FileManager;

public class Connection extends MyThread implements IObservable {

	private static int count = 0;
	private String name;
	private int port;
	private File fileList;
	private IObserver observer;
	private DataInputStream input;
	private DataOutputStream output;
	private Socket socket;

	public Connection(Socket socket) {
		super(String.valueOf(count++), 1000);
		this.socket = socket;
		try {
			input = new DataInputStream(this.socket.getInputStream());
			output = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
		start();
	}

	public void sendUsers(String path, ArrayList<Connection> users) {
		try {
			output.writeUTF(Request.USERS.toString());
			FileManager.saveFile(path, users);
			File file = new File(path);
			byte[] array = new byte[(int) file.length()];
			readFileBytes(file, array);
			output.writeUTF(file.getName());
			output.writeInt(array.length);
			output.write(array);
			file.delete();
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	private void readFileBytes(File file, byte[] array) throws IOException {
		FileInputStream fInputStream = new FileInputStream(file);
		fInputStream.read(array);
		fInputStream.close();
	}
	
	public void sendUsersFile(File file) {
		try {
			output.writeUTF(Request.GET_FILE.toString());
			sendFile(file);
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
	
	private void sendFile(File file) throws IOException {
		byte[] array = new byte[(int) file.length()];
		readFileBytes(file, array);
		output.writeUTF(file.getName());
		output.writeInt(array.length);
		output.write(array);
	}

	private void downloadFile(File file) throws IOException {
		byte[] fileArray = new byte[input.readInt()];
		input.readFully(fileArray);
		writeFile(file, fileArray);
	}
	
	private void writeFile(File file, byte[] array) throws IOException {
		FileOutputStream fOutputStream = new FileOutputStream(file);
		fOutputStream.write(array);
		fOutputStream.close();
	}
	
	public void shareFile(String filePath) {
		try {
			output.writeUTF(Request.SHARE_FILE.toString());
			output.writeUTF(filePath);
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
	
	private void managerRequest(String request) throws IOException {
		switch (Request.valueOf(request)) {
		case GET_FILE:
			observer.sendFileList(this, input.readUTF());
			break;
		case SEND_FILE:
			fileList = new File(name + "-" + input.readUTF());
			downloadFile(fileList);
			break;
		case USERS:
			break;
		case USER_NAME:
			userInfo();
			break;
		case DOWNLOAD_FILE:
			downLoadShareFile(input.readUTF(), input.readUTF());
			break;
		case SHARE_FILE:
			downloadFile(new File(name + "-" + input.readUTF()));
			break;
		}
	}
	
	private void userInfo() throws IOException {
		name = input.readUTF();
		port = input.readInt();
		observer.update(this);
	}
	
	private void downLoadShareFile(String userName, String fileName) {
		observer.downloadFile(this, userName, fileName);
		File file = new File(userName + "-" + fileName);
		try {
			while (!file.exists()) {
				System.out.println("No existe el archivo " + file.getName());
			}
			output.writeUTF(Request.DOWNLOAD_FILE.toString());
			sendFile(file);
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage() + "");
		}
	}

	@Override
	public void execute() {
		String request;
		try {
			request = input.readUTF();
			if (request != null) {
				managerRequest(request);
			}
		} catch (IOException e) {
			ConstantList.LOGGER.log(Level.WARNING, e.getMessage() + " IP: " + getInetAddress() + " User: " + name);
			observer.removeConnection(this);
			stop();
		}
	}

	public String getInetAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	@Override
	public void addObserver(IObserver observer) {
		this.observer = observer;
	}

	@Override
	public void removeObserver(IObserver observer) {
		observer = null;
	}

	public String getName() {
		return name;
	}
	
	public int getPort() {
		return port;
	}
	
	public File getFileList() {
		return fileList;
	}
}