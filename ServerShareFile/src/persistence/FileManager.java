package persistence;

import java.io.FileWriter;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import network.Connection;
import network.ConstantList;

public class FileManager {

	public static void saveFile(String path, ArrayList<Connection> connections) {
		Element root = new Element(ConstantList.FILES);
		Document doc = new Document(root);
		for (Connection connection : connections) {
			Element user = new Element(ConstantList.USER);
			Element userName = new Element(ConstantList.USER_NAME).setText(connection.getName());
			Element userIp = new Element(ConstantList.IP).setText(connection.getInetAddress());
			Element userPort = new Element(ConstantList.PORT).setText(String.valueOf(connection.getPort()));
			user.addContent(userName);
			user.addContent(userIp);
			user.addContent(userPort);
			doc.getRootElement().addContent(user);
		}
		try {
			FileWriter fileWriter = new FileWriter(path);
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			xmlOutputter.setFormat(Format.getCompactFormat());
			xmlOutputter.output(doc, fileWriter);
			fileWriter.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}