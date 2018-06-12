package view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;

import network.ConstantList;

public class FrameServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JList<String> userList;
	private DefaultListModel<String> listModel;

	public FrameServer() {
		setSize(ConstantList.WIDTH, ConstantList.HEIGHT);
		setIconImage(new ImageIcon(getClass().getResource(ConstantList.APP_ICON)).getImage());
		setLayout(new BorderLayout());
		init();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void init() {
		add(UtilityList.createJLabel(ConstantList.USER_LIST, ConstantList.AGENCY_FB, ConstantList.APP_COLOR),
				BorderLayout.NORTH);
		userList = new JList<>();
		userList.setFont(ConstantList.AGENCY_FB);
		add(userList, BorderLayout.CENTER);
	}

	public void refreshList(ArrayList<String> users) {
		listModel = new DefaultListModel<>();
		for (String string : users) {
			listModel.addElement(string);
		}
		userList.setModel(listModel);
		revalidate();
	}
}