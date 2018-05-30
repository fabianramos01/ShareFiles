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

public class PanelUserList extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> comboList;
	private JComboBox<String> comboBoxUsers;

	public PanelUserList(Controller listener) {
		setLayout(new GridLayout(1, 3));
		add(UtilityList.createJLabel(ConstantList.USER_LIST, ConstantList.AGENCY_FB, Color.BLUE));
		comboBoxUsers = new JComboBox<>();
		comboBoxUsers.addItemListener(listener);
		comboList = new DefaultComboBoxModel<>();
		comboList.addElement(ConstantList.NOT_USERS_CONN);
		comboBoxUsers.setModel(comboList);
		comboBoxUsers.setFont(ConstantList.AGENCY_FB);
		add(comboBoxUsers);
		add(UtilityList.createJButtonText(Command.COMMAND_CHANGE_IP.getCommand(), "Seleccionar", Color.WHITE, Color.BLACK,
				ConstantList.AGENCY_FB, listener));
	}

	public void refreshUserList(ArrayList<String> users) {
		comboList = new DefaultComboBoxModel<>();
		if (!users.isEmpty()) {
			for (String userName : users) {
				comboList.addElement(userName);
			}
		} else {
			comboList.addElement(ConstantList.NOT_USERS_CONN);
		}
		comboBoxUsers.setModel(comboList);
		revalidate();
	}
	
	public String getSelectedUser() {
		return comboBoxUsers.getSelectedItem().toString();
	}
}