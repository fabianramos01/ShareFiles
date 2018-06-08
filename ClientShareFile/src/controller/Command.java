package controller;

import javax.swing.ImageIcon;

public enum Command {

	COMMAND_CONNECT("COMMAND_CONNECT", "Conetarse" , ""),
	COMMAND_SELCET_USER("COMMAND_SELCET_USER", "Seleccionar" , ""),
	COMMAND_DOWNLOAD_FILE("COMMAND_DOWNLOAD_FILE", "Descargar archivo" , "/data/download.png");
	
	private String command;
	private String title;
	private String pathImg;
	
	private Command(String command, String title, String pathImg) {
		this.command = command;
		this.title = title;
		this.pathImg = pathImg;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ImageIcon getImg() {
		return new ImageIcon(getClass().getResource(pathImg));
	}
}