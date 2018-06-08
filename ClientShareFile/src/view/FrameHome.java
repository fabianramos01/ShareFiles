package view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import controller.Command;
import controller.ConstantList;
import controller.Controller;
import model.User;

public class FrameHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private Controller listener;
	private PanelUserList panelUserList;
	private JList<String> fileList;
	private DefaultListModel<String> listModel;

	public FrameHome(Controller listener) {
		this.listener = listener;
		setTitle(ConstantList.APP_NAME);
		setLayout(new BorderLayout());
		setIconImage(new ImageIcon(getClass().getResource(ConstantList.APP_ICON)).getImage());
		setSize(ConstantList.WIDTH_FRAME, ConstantList.HEIGHT_FRAME);
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	private void init() {
		setJMenuBar(new MenuBarUser(listener));
		panelUserList = new PanelUserList(listener);
		add(panelUserList, BorderLayout.NORTH);
		fileList = new JList<>();
		fileList.setFont(ConstantList.AGENCY_FB);
		add(new JScrollPane(fileList), BorderLayout.CENTER);
		add(UtilityList.createJButton(Command.COMMAND_DOWNLOAD_FILE.getCommand(),
				Command.COMMAND_DOWNLOAD_FILE.getTitle(), Command.COMMAND_DOWNLOAD_FILE.getImg(), listener),
				BorderLayout.SOUTH);
	}

	public void refreshUserList(ArrayList<User> users) {
		panelUserList.refreshUserList(users);
		revalidate();
	}

	public void loadFileList(ArrayList<String> files) {
		listModel = new DefaultListModel<>();
		for (String fileName : files) {
			listModel.addElement(fileName);
		}
		fileList.setModel(listModel);
		revalidate();
	}

	public User getSelectedUser() {
		return panelUserList.getSelectedUser();
	}

	public String getFileName() {
		return fileList.getSelectedValue();
	}
}