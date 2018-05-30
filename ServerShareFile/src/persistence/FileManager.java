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

import network.ConstantList;

public class FileManager {

	public static void saveFile(String path, ArrayList<String> users) {
		Element root = new Element(ConstantList.FILES);
		Document doc = new Document(root);
		for (String user : users) {
			Element userElement = new Element(ConstantList.FILE).setText(user);
			doc.getRootElement().addContent(userElement);
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

	public static ArrayList<String> loadFiles(File file) {
		ArrayList<String> users = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document) builder.build(file);
			Element rootNode = (Element) ((org.jdom2.Document) document).getRootElement();
			List<Element> userFileList = ((org.jdom2.Element) rootNode).getChildren(ConstantList.FILE);
			for (Element matchElement : userFileList) {
				users.add(matchElement.getChildTextTrim(ConstantList.FILE));
			}
		} catch (IOException io) {
			System.err.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.err.println(jdomex.getMessage());
		}
		return users;
	}
}