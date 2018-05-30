package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import controller.ConstantList;
import model.MyThread;
import model.User;
import persistence.FileManager;

public class ManagerUser extends MyThread implements IObservable {

	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private User user;
	private IObserver iObserver;

	public ManagerUser(String ip, int port, String name, File file) throws IOException {
		super("", ConstantList.SLEEP);
		socket = new Socket(ip, port);
		output = new DataOutputStream(socket.getOutputStream());
		input = new DataInputStream(socket.getInputStream());
		newUser(name, file);
		start();
	}

	private void newUser(String name, File file) throws IOException {
		user = new User(name, file);		
		output.writeUTF(Request.USER_NAME.toString());
		output.writeUTF(name);
		File root = new File(name);
		root.mkdirs();
		sendFile();
	}

	public void requestFiles(String userName) {
		try {
			if (!userName.equals(ConstantList.NOT_USERS_CONN)) {
				output.writeUTF(Request.GET_FILE.toString());
				output.writeUTF(userName);				
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void getFile(String response) throws IOException {
		File file = null;
		file = new File(user.getName() + "/" + input.readUTF());
		byte[] fileArray = new byte[input.readInt()];
		input.readFully(fileArray);
		writeFile(file, fileArray);
		if (response.equals(Request.GET_FILE.toString())) {
			user.setFileList(FileManager.loadFiles(file));
			iObserver.userListFile();
		} else if (response.equals(Request.USERS.toString())) {
			user.setUsers(FileManager.loadFiles(file));
			iObserver.newUser();
		}
	}

	private void writeFile(File file, byte[] array) throws IOException {
		FileOutputStream fOutputStream = new FileOutputStream(file);
		fOutputStream.write(array);
		fOutputStream.close();
	}

	private void sendFile() throws IOException {
		output.writeUTF(Request.SEND_FILE.toString());
		FileManager.saveFiles(user.getName(), user.getRootDirectory());
		File file = new File(user.getName() + "/" + ConstantList.MY_FILES);
		byte[] array = new byte[(int) file.length()];
		readFileBytes(file, array);
		output.writeUTF(ConstantList.MY_FILES);
		output.writeInt(array.length);
		output.write(array);
	}
	
	private void readFileBytes(File file, byte[] array) throws IOException {
		FileInputStream fInputStream = new FileInputStream(file);
		fInputStream.read(array);
		fInputStream.close();
	}
	
	public void downloadFile(String user, String fileName) throws IOException {
		output.writeUTF(Request.DOWNLOAD_FILE.toString());
		output.writeUTF(user);
		output.writeUTF(fileName);
	}

	private void responseManager(String response) throws IOException {
		switch (Request.valueOf(response)) {
		case GET_FILE:
			getFile(response);
			break;
		case SEND_FILE:
			sendFile();
			break;
		case USERS:
			getFile(response);
			break;
		case DOWNLOAD_FILE:
			getFile(response);
			break;
		case USER_NAME:
			break;
		case SHARE_FILE:
			shareFile(input.readUTF());
			break;
		}
	}

	private void shareFile(String filePath) throws IOException {
		output.writeUTF(Request.SHARE_FILE.toString());
		File file = user.getFile(filePath);
		byte[] array = new byte[(int) file.length()];
		readFileBytes(file, array);
		output.writeUTF(filePath);
		output.writeInt(array.length);
		output.write(array);
	}

	@Override
	public void execute() {
		String response;
		try {
			response = input.readUTF();
			if (response != null) {
				responseManager(response);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			stop();
		}
	}

	public User getUser() {
		return user;
	}

	@Override
	public void addObserver(IObserver iObserver) {
		this.iObserver = iObserver;
	}

	@Override
	public void removeObserver() {
		this.iObserver = null;		
	}
}