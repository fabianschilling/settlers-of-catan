package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * Panel, das als Content-Panel f&uuml;r die <code>ServerGUI</code> dient
 * 
 * @author Christian Hemauer
 * 
 */
@SuppressWarnings("serial")
public class ServerGUIBg extends JPanel {
	/**
	 * Die H&ouml;he des Bildschirms
	 */
	private int screenHeight;
	/**
	 * Die Breite des Bildschirms
	 */
	private int screenWidth;
	/**
	 * Zeigt den Server-Status an
	 */
	@SuppressWarnings("unused")
	private JLabel serverState;
	/**
	 * Das Panel, dem alle Elemente hinzugef&uuml;gt werden
	 */
	private JPanel content;
	/**
	 * Das Exitpanel der Server-Gui - Objekt der Klasse
	 * <code>ServerExitButton</code>
	 */
	private JPanel rect_0;
	/**
	 * Das Panel, das das <i>Siedler von Catan</i> Logo enth&auml;lt - Objekt
	 * der Klasse <code>ServerGUILogo</code>
	 */
	private ServerGUILogo rect_1;
	/**
	 * Das Hostpanel der Server-Gui - Objekt der Klasse <code>ServerHost</code>
	 */
	private ServerHost rect_2;
	/**
	 * Das Clientpanel der Server-Gui - Objekt der Klasse
	 * <code>ServerGUIClient</code>
	 */
	private ServerGUIClient rect_3;
	/**
	 * Das Verbindungspanel zwischen Serverstatus-Panel und Hostpanel Objekt der
	 * Klasse <code>GuiConnection</code>
	 */
	/**
	 * Das Titelpanel f&uuml;r das Hostpanel - Objekt der Klasse
	 * <code>ServerGUITitle_1</code>
	 */
	private JPanel hostTitle;
	/**
	 * Das Titelpanel f&uuml;r das Clientpanel - Objekt der Klasse
	 * <code>ServerGUITitle_2</code>
	 */
	private JPanel joinTitle;
	/**
	 * Das ServerGUI-Fenster - Objekt der Klasse <code>ServerGUI</code>
	 */
	private ServerGUI serverGUI;
	/**
	 * Die Bildschirmbreite
	 */
	private int width;
	/**
	 * Die Bildschirmh&ouml;he
	 */
	private int height;
	/**
	 * Die Breite eines Abschnitts der Server-Gui
	 */
	private int rectWidth;
	/**
	 * Die H&ouml;he eines Abschnitts der Server-Gui
	 */
	private int rectHeight;
	/**
	 * Das Logfenster f&uuml;r den Server
	 */
	private JTextArea serverLog;
	/**
	 * Das Panel f&uuml;r die Statusanzeige des Clients
	 */
	private JPanel clientLog;
	/**
	 * Der Status des Clients
	 */
	private JLabel clientState;
	/**
	 * &Uuml;berschrift des Hostbereichs der Server-Gui
	 */
	private RichJLabel hostLabel;
	/**
	 * &Uuml;berschrift des Hostbereichs der Server-Gui
	 */
	private RichJLabel joinLabel;
	/**
	 * Exit-Button der Server-Gui - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton exit;
	
	public ServerGUIBg(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		width = d.width;
		height = d.height;
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		rectWidth = width/4+width/16;
		rectHeight = height/5;
		setSize(screenWidth / 2, screenHeight / 4);
		super.setLayout(null);
		createWidgets();
		addWidgets();
		super.add(content);
	}
	/**
	 * Erzeugt die einzelnen Panelelemente
	 */
	public void createWidgets() {
		
		content = new JPanel();
		content.setSize(width/2-width/6, 7*height/8);
		content.setBackground(new Color(221, 48, 60,0));
		content.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));
		
		rect_0 = new JPanel();
		rect_0.setOpaque(false);
		exit = new PlayerButton(ImportServerImages.quitKey,width/42,height/30);
		rect_0.setSize(width/12, height/12);
		rect_0.setLocation(width/4+width/42, 0);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		rect_0.add(exit);
		
		
		rect_1 = new ServerGUILogo();
		rect_1.setSize(rectWidth, rectHeight);
		rect_1.setLocation((content.getWidth()-rectWidth)/2, rect_0.getHeight());
		rect_1.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		serverLog = new JTextArea();
		serverLog.setSize(rectWidth-rectWidth/12, rectHeight-rectHeight/6);
		serverLog.setPreferredSize(new Dimension(rectWidth-rectWidth/12, rectHeight-rectHeight/6));
		serverLog.setVisible(false);
		serverLog.setLocation((content.getWidth()-rectWidth)/2+(rect_1.getWidth()-serverLog.getWidth())/2, rect_1.getY()+(rect_1.getHeight()-serverLog.getHeight())/2);
		serverLog.setBorder(BorderFactory.createLineBorder(Color.black,2));
		serverLog.setEditable(false);
		
		clientState = new JLabel(""); //$NON-NLS-1$
		clientState.setForeground(Color.WHITE);
		clientState.setFont(new Font("Impact", Font.BOLD, height/30)); //$NON-NLS-1$
		
		clientLog = new JPanel();
		clientLog.setSize(rectWidth-rectWidth/12, rectHeight-rectHeight/6);
		clientLog.setPreferredSize(new Dimension(rectWidth-rectWidth/12, rectHeight-rectHeight/6));
		clientLog.setBackground(new Color(221, 48, 60));
		clientLog.setVisible(false);
		clientLog.setLocation((content.getWidth()-rectWidth)/2+(rect_1.getWidth()-serverLog.getWidth())/2, rect_1.getY()+(rect_1.getHeight()-serverLog.getHeight())/2);
		
		rect_2 = new ServerHost(this);
		rect_2.setSize(rectWidth, rectHeight);
		rect_2.setLocation((content.getWidth()-rectWidth)/2, rect_1.getY()+rect_1.getHeight()+rectHeight/3);
		rect_2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	
		rect_3 = new ServerGUIClient(rect_2, serverGUI);
		rect_3.setSize(rectWidth, rectHeight);
		rect_3.setLocation((content.getWidth()-rectWidth)/2, rect_2.getY()+rect_2.getHeight()+rectHeight/3);
		rect_3.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		hostLabel = new RichJLabel("Host",2); //$NON-NLS-1$
		hostLabel.setFont(new Font("Impact", Font.BOLD, height/20)); //$NON-NLS-1$
		hostLabel.setSize(width/20, height/20);
		hostLabel.setLeftShadow(1, 1, Color.black);
	    hostLabel.setRightShadow(4, 4, Color.black);
	  
	    hostLabel.setForeground(Color.WHITE);
	    hostLabel.setFont(hostLabel.getFont().deriveFont(width/30f));
		hostTitle = new JPanel();
		hostTitle.setOpaque(false);
		hostTitle.setSize(rectWidth/4, rectHeight/3);
		hostTitle.setLocation((content.getWidth()-rectWidth)/2, rect_1.getY()+rect_3.getHeight());
		hostTitle.add(hostLabel);
		
		joinTitle = new JPanel();
		joinTitle.setOpaque(false);
		joinTitle.setSize(7*rectWidth/20, rectHeight/3);
		joinTitle.setLocation((content.getWidth()-rectWidth)/2, rect_2.getY()+rect_3.getHeight());
		joinLabel = new RichJLabel("Client",2); //$NON-NLS-1$
		joinLabel.setFont(new Font("Impact", Font.BOLD, height/20)); //$NON-NLS-1$
		joinLabel.setSize(width/20, height/20);
		joinLabel.setLeftShadow(1, 1, Color.black);
	    joinLabel.setRightShadow(4, 4, Color.black);
	  
	    joinLabel.setForeground(Color.WHITE);
	    joinLabel.setFont(hostLabel.getFont().deriveFont(width/30f));
		joinTitle.add(joinLabel);
	}
	/**
	 * F&uuml;gt dem Panel die Elemente hinzu
	 */
	public void addWidgets() {
		rect_1.add(serverLog);
		clientState.setLocation(rect_1.getX()+(rect_1.getWidth()-clientLog.getWidth())/2,
				rect_1.getY()+(rect_1.getHeight()-clientLog.getHeight())/2);
		clientLog.add(clientState);
		rect_1.add(clientLog);
		super.add(rect_0);
		super.add(rect_2);
		super.add(rect_1);
		super.add(rect_3);
		super.add(hostTitle);
		super.add(joinTitle);	
	}
	
	public ServerGUI getServerGUI() {
		return serverGUI;
	}
	public ServerGUILogo getRect_1() {
		return rect_1;
	}
	public JLabel getClientState() {
		return clientState;
	}
	public JPanel getClientLog() {
		return clientLog;
	}
	public ServerHost getRect_2() {
		return rect_2;
	}
	public JTextArea getServerLog() {
		return serverLog;
	}

}