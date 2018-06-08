package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import controller.Command;
import controller.ConstantList;
import controller.Controller;
import model.User;

public class PanelUserList extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<User> comboList;
	private JComboBox<User> comboBoxUsers;

	public PanelUserList(Controller listener) {
		setLayout(new GridLayout(1, 3));
		add(UtilityList.createJLabel(ConstantList.USER_LIST, ConstantList.AGENCY_FB, Color.BLUE));
		comboBoxUsers = new JComboBox<>();
		comboBoxUsers.addItemListener(listener);
		comboList = new DefaultComboBoxModel<>();
		comboList.addElement(new User(ConstantList.NOT_USERS_CONN, "", 0));
		comboBoxUsers.setModel(comboList);
		comboBoxUsers.setFont(ConstantList.AGENCY_FB);
		add(comboBoxUsers);
		add(UtilityList.createJButtonText(Command.COMMAND_SELCET_USER.getCommand(),
				Command.COMMAND_SELCET_USER.getTitle(), Color.WHITE, Color.BLUE, ConstantList.AGENCY_FB, listener));
	}

	public void refreshUserList(ArrayList<User> users) {
		comboList = new DefaultComboBoxModel<>();
		if (!users.isEmpty()) {
			for (User user: users) {
				comboList.addElement(user);
			}
		} else {
			comboList.addElement(new User(ConstantList.NOT_USERS_CONN, "", 0));
		}
		comboBoxUsers.setModel(comboList);
		revalidate();
	}

	public User getSelectedUser() {
		return (User) comboBoxUsers.getSelectedItem();
	}
}