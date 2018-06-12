package network;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Logger;

public class ConstantList {

	public static final int WIDTH = 550;
	public static final int HEIGHT = 700;
	public static final String APP_ICON = "/data/iconApp.png";
	public static final Color APP_COLOR = Color.decode("#136EF1");
	public static final Font AGENCY_FB = new Font("Agency FB", Font.BOLD, 35);
	
	public static final Logger LOGGER = Logger.getGlobal();
	public static final String CONNECTION_ERROR = "Puerto invalido";
	public static final String ERROR = "ERROR";
	public static final String GET_PORT = "Ingrese el puerto";
	
	public static final String FILES = "Files";
	public static final String FILE = "File";
	public static final String USERS_FILE = "usersFiles.xml";
	public static final String USER_NAME = "UserName";
	public static final String IP = "Ip";
	public static final String PORT = "Port";
	public static final String USER = "User";
	
	public static final String USER_LIST = "Lista de usuarios";
}