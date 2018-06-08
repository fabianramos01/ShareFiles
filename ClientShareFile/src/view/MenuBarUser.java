package view;

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import controller.Command;
import controller.ConstantList;

public class MenuBarUser extends JMenuBar {

	private static final long serialVersionUID = 1L;
	
	public MenuBarUser(ActionListener listener) {
		JMenu menuConn = new JMenu(ConstantList.CONNECTION_MENU);
		JMenuItem menuInitCon = new JMenuItem(Command.COMMAND_CONNECT.getTitle());
		menuInitCon.addActionListener(listener);
		menuInitCon.setActionCommand(Command.COMMAND_CONNECT.getCommand());
		menuInitCon.setAccelerator(KeyStroke.getKeyStroke('B', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuConn.add(menuInitCon);
		add(menuConn);
	}

}