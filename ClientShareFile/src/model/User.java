package model;

import java.io.File;
import java.util.ArrayList;

public class User {

	private String name;
	private File rootDirectory;
	private ArrayList<String> users;
	private ArrayList<String> fileList;
	
	public User(String name, File rootDirectory) {
		super();
		this.name = name;
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

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
	
	public String getName() {
		return name;
	}
	
	public File getRootDirectory() {
		return rootDirectory;
	}
	
	public ArrayList<String> getUsers() {
		return users;
	}
	
	public ArrayList<String> getFileList() {
		return fileList;
	}
}