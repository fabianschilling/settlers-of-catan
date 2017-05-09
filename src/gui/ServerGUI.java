package gui;

import controller.ServerController;
import java.awt.*;
import javax.swing.*;
/**
 * Das Fenster der Server-Gui
 * @author Christian Hemauer
 *
 */
@SuppressWarnings("serial")
public class ServerGUI extends JFrame {
	/**
	 * Zugriff auf den die Klasse <code>ServerController</code>
	 */
	private ServerController serverController;
	/**
	 * Panelinhalt der Server-Gui
	 */
	@SuppressWarnings("unused")
	private JPanel contentPane;
	/**
	 * Die Bildschirmbreite
	 */
	private int width;
	/**
	 * Die Bildschirmh&ouml;he
	 */
	private int height;
	
	public ServerGUI(final ServerController serverController)
			throws HeadlessException {
		this.serverController = serverController;
		super.setBackground(new Color(221, 48, 60));
		super.setBackground(Color.darkGray);
		setPreferredSize(new Dimension(width/2-width/6, 7*height/8));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		width = d.width;
		height = d.height;
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(500, 500, width/2-width/6, 7*height/8);
		setLocationRelativeTo(null);
		addWidgets();

	}
	/**
	 * F&uuml;gt dem Fenster die entsprechenden Panels hinzu
	 */
	public void addWidgets() {
		add(new ServerGUIBg(this));
	}

	public ServerController getServerController() {
		return serverController;
	}
	
	public ServerGUI getThis() {
		return this;
	}
}
