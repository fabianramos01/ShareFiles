package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Command;
import controller.ConstantList;

public class PanelSignIn extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField fieldIP;
	private JTextField fieldPort;
	private JTextField fieldUserName;
	private JTextField fieldLocalPort;
	private JLabel labelDirectory;

	public PanelSignIn(ActionListener listener) {
		setLayout(new BorderLayout());
		panelInfo(listener);
		add(UtilityList.createJButtonText(Command.COMMAND_ACCEPT_INFO.getCommand(),
				Command.COMMAND_ACCEPT_INFO.getTitle(), Color.WHITE, ConstantList.APP_COLOR, ConstantList.AGENCY_FB,
				listener), BorderLayout.SOUTH);
	}

	public void panelInfo(ActionListener listener) {
		JPanel panel = new JPanel(new GridLayout(5, 2));
		panel.add(UtilityList.createJLabel(ConstantList.GET_IP, ConstantList.AGENCY_FB, ConstantList.APP_COLOR));
		fieldIP = new JTextField();
		fieldIP.setHorizontalAlignment(JTextField.CENTER);
		fieldIP.setFont(ConstantList.AGENCY_FB);
		panel.add(fieldIP);
		panel.add(UtilityList.createJLabel(ConstantList.GET_PORT, ConstantList.AGENCY_FB, ConstantList.APP_COLOR));
		fieldPort = new JTextField();
		fieldPort.setHorizontalAlignment(JTextField.CENTER);
		fieldPort.setFont(ConstantList.AGENCY_FB);
		panel.add(fieldPort);
		panel.add(UtilityList.createJLabel(ConstantList.USER_NAME, ConstantList.AGENCY_FB, ConstantList.APP_COLOR));
		fieldUserName = new JTextField();
		fieldUserName.setHorizontalAlignment(JTextField.CENTER);
		fieldUserName.setFont(ConstantList.AGENCY_FB);
		panel.add(fieldUserName);
		panel.add(
				UtilityList.createJLabel(ConstantList.GET_LOCAL_PORT, ConstantList.AGENCY_FB, ConstantList.APP_COLOR));
		fieldLocalPort = new JTextField();
		fieldLocalPort.setHorizontalAlignment(JTextField.CENTER);
		fieldLocalPort.setFont(ConstantList.AGENCY_FB);
		panel.add(fieldLocalPort);
		labelDirectory = new JLabel("");
		labelDirectory.setBorder(BorderFactory.createLineBorder(ConstantList.APP_COLOR));
		labelDirectory.setFont(ConstantList.AGENCY_FB);
		panel.add(labelDirectory);
		panel.add(UtilityList.createJButtonText(Command.COMMAND_SHOW_FILECHOOSER.getCommand(),
				Command.COMMAND_SHOW_FILECHOOSER.getTitle(), Color.WHITE, ConstantList.APP_COLOR,
				ConstantList.AGENCY_FB, listener));
		add(panel, BorderLayout.CENTER);
	}
	
	public void showFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(fileChooser);
		if (fileChooser.getSelectedFile() != null) {
			labelDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
			revalidate();
		}
	}

	public String[] getInfo() {
		String[] info = { fieldIP.getText(), fieldPort.getText(), fieldUserName.getText(), fieldLocalPort.getText(),
				labelDirectory.getText() };
		return info;
	}
}