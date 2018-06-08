package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.ConstantList;
import model.Client;
import model.MyThread;

public class LocalServer extends MyThread {

	private ServerSocket server;
	private Client client;

	public LocalServer(Client client) throws IOException {
		super(client.getName(), ConstantList.SLEEP);
		this.client = client;
		server = new ServerSocket(client.getMyPort());
		start();
	}

	@Override
	public void execute() {
		try {
			Socket socket = server.accept();
			shareFile(socket);
		} catch (IOException e) {
			stop();
		}
	}

	private void shareFile(Socket socket) throws IOException {
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		if (inputStream.readUTF().equals(Request.DOWNLOAD_FILE.toString())) {
			File file = client.getFile(inputStream.readUTF());			
			sendFile(file, outputStream);
		}
	}

	public void sendFile(File file, DataOutputStream output) throws IOException {
		output.writeUTF(Request.SHARE_FILE.toString());
		byte[] array = new byte[(int) file.length()];
		readFileBytes(file, array);
		output.writeUTF(client.getName() + file.getName());
		output.writeInt(array.length);
		output.write(array);
		System.out.println("Enviado");
	}

	private void readFileBytes(File file, byte[] array) throws IOException {
		FileInputStream fInputStream = new FileInputStream(file);
		fInputStream.read(array);
		fInputStream.close();
	}

}