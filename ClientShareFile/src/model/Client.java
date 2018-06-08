package model;

import java.io.File;
import java.util.ArrayList;

public class Client {

	private String name;
	private File rootDirectory;
	private int myPort;
	private ArrayList<User> users;
	private ArrayList<String> fileList;
	
	public Client(String name, int myPort, File rootDirectory) {
		super();
		this.name = name;
		this.myPort = myPort;
		this.rootDirectory = rootDirectory;
	}

	public File getFile(String fileName) {
		for (File file : rootDirectory.listFiles()) {
			if (fileName.equals(file.getName())) {
				return file;
			}
		}
		return null;

	}
	
	public void setFileList(ArrayList<String> userList) {
		this.fileList = userList;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMyPort() {
		return myPort;
	}
	
	public File getRootDirectory() {
		return rootDirectory;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public ArrayList<String> getFileList() {
		return fileList;
	}
}