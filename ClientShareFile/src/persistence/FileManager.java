package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import controller.ConstantList;
import model.User;

public class FileManager {

	public static void saveFiles(String user, File directory) {
		Element root = new Element(ConstantList.FILES);
		Document doc = new Document(root);
		for (File file : directory.listFiles()) {
			if (!file.isDirectory()) {
				Element fileElement = new Element(ConstantList.FILE).setText(file.getName());
				doc.getRootElement().addContent(fileElement);				
			}
		}
		try {
			FileWriter fileWriter = new FileWriter(user + "/" + user + ConstantList.MY_FILES);
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			xmlOutputter.setFormat(Format.getCompactFormat());
			xmlOutputter.output(doc, fileWriter);
			fileWriter.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static ArrayList<String> loadFiles(File file) {
		ArrayList<String> users = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();
	    try {
	        Document document = (Document) builder.build(file);
	        Element rootNode = (Element) ((org.jdom2.Document) document).getRootElement();
	        List<Element> userFileList = ((org.jdom2.Element) rootNode).getChildren(ConstantList.FILE);
	        for (Element matchElement : userFileList) {
	            users.add(matchElement.getText());
	        }
	    }catch (IOException io) {
	        System.err.println(io.getMessage());
	    }catch (JDOMException jdomex) {
	        System.err.println(jdomex.getMessage());
	    }
		return users;
	}
	
	public static ArrayList<User> loadUsersFile(File file) {
		ArrayList<User> users = new ArrayList<User>();
		SAXBuilder builder = new SAXBuilder();
	    try {
	        Document document = (Document) builder.build(file);
	        Element rootNode = (Element) ((org.jdom2.Document) document).getRootElement();
	        List<Element> userFileList = ((org.jdom2.Element) rootNode).getChildren(ConstantList.USER);
	        for (Element matchElement : userFileList) {
	        	String userName = matchElement.getChildTextTrim(ConstantList.USER_NAME_ELEM);
	        	String ip = matchElement.getChildTextTrim(ConstantList.IP);
	        	String port = matchElement.getChildTextTrim(ConstantList.PORT);
	        	System.out.println(userName + ip + port);
	            users.add(new User(userName, ip, Integer.parseInt(port)));
	        }
	    }catch (IOException io) {
	        System.err.println(io.getMessage());
	    }catch (JDOMException jdomex) {
	        System.err.println(jdomex.getMessage());
	    }
		return users;
	}
}