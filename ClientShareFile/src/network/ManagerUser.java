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
import model.Client;
import persistence.FileManager;

public class ManagerUser extends MyThread implements IObservable {

	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private LocalServer localServer;
	private IObserver iObserver;

	public ManagerUser(String ip, int port, String name, int myPort, File file) throws IOException {
		super("", ConstantList.SLEEP);
		socket = new Socket(ip, port);
		output = new DataOutputStream(socket.getOutputStream());
		input = new DataInputStream(socket.getInputStream());
		newUser(name, myPort, file);
		localServer = new LocalServer(client);
		start();
	}

	private void newUser(String name, int myPort, File file) throws IOException {
		client = new Client(name, myPort, file);		
		output.writeUTF(Request.USER_NAME.toString());
		output.writeUTF(name);
		output.writeInt(myPort);
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

	public void readFile(File file) throws IOException {
		byte[] fileArray = new byte[input.readInt()];
		input.readFully(fileArray);
		writeFile(file, fileArray);
	}

	private void writeFile(File file, byte[] array) throws IOException {
		FileOutputStream fOutputStream = new FileOutputStream(file);
		fOutputStream.write(array);
		fOutputStream.close();
	}

	private void sendFile() throws IOException {
		output.writeUTF(Request.SEND_FILE.toString());
		FileManager.saveFiles(client.getName(), client.getRootDirectory());
		File file = new File(client.getName() + "/" + client.getName() + ConstantList.MY_FILES);
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
			downloadFileList();
			break;
		case SEND_FILE:
			sendFile();
			break;
		case USERS:
			downloadUsers();
			break;
		case DOWNLOAD_FILE:
			downloadUserFile();
			break;
		case USER_NAME:
			break;
		case SHARE_FILE:
			shareFile(input.readUTF());
			break;
		}
	}

	private void downloadUserFile() throws IOException {
		File file = new File(client.getName() + "/" + input.readUTF());
		readFile(file);
		iObserver.downloadFile();
	}

	private void downloadUsers() throws IOException {
		File file = new File(client.getName() + "/" + input.readUTF());
		readFile(file);
		client.setUsers(FileManager.loadUsersFile(file));
		iObserver.newUser();
	}
	
	public void downloadDirectFile(Socket socketClient, String fileName) throws IOException {
		DataOutputStream outputStream = new DataOutputStream(socketClient.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socketClient.getInputStream());
		outputStream.writeUTF(Request.DOWNLOAD_FILE.toString());
		outputStream.writeUTF(fileName);
		if (inputStream.readUTF().equals(Request.SHARE_FILE.toString())) {
			File file = new File(client.getName() + "/" + inputStream.readUTF());
			byte[] fileArray = new byte[inputStream.readInt()];
			inputStream.readFully(fileArray);
			writeFile(file, fileArray);
			inputStream.close();
			outputStream.close();			
		}
	}

	private void downloadFileList() throws IOException {
		File file = new File(client.getName() + "/" + ConstantList.FILES_LIST);
		System.out.println(input.readUTF());
		readFile(file);
		client.setFileList(FileManager.loadFiles(file));
		iObserver.userListFile();
	}

	private void shareFile(String filePath) throws IOException {
		output.writeUTF(Request.SHARE_FILE.toString());
		File file = client.getFile(filePath);
		byte[] array = new byte[(int) file.length()];
		readFileBytes(file, array);
		output.writeUTF(filePath);
		output.writeInt(array.length);
		output.write(array);
		System.out.println("Archivo compartido: " + file.getName());
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
			localServer.stop();
			stop();
		}
	}

	public Client getUser() {
		return client;
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