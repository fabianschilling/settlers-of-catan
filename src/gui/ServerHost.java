package gui;

import gui.ImportServerImages;
import gui.PlayerButton;
import gui.ServerGUIBg;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JPanel;
import settlersOfCatan.AITest;
import networking.ExceptionLogger;
import controller.ServerController;

@SuppressWarnings("serial")
/**
 * Das Hostpanel der Server-Gui
 * @author Christian Hemauer
 *
 */
public class ServerHost extends JPanel {
	/**
	 * Der Hintergrund der Server-Gui - Objekt der Klasse
	 * <code>ServerGUIBg</code>
	 */
	private static ServerGUIBg serverGuiBg;
	/**
	 * Das Server-Statusfenster - Objekt der Klasse <code>ServerGUIState</code>
	 */
	private PlayerButton serverStart_3;
	/**
	 * Der Button zum Start eines 4-Spieler-Servers - Objekt der Klasse
	 * <code>PlayerButton</code>
	 */
	private PlayerButton serverStart_4;
	/**
	 * Der Button zum Start eines Bots - Objekt der Klasse
	 * <code>PlayerButton</code>
	 */
	private static PlayerButton ki;
	/**
	 * Die Anzahl der Spieler, die am Spiel teilnehmen d&uuml;rfen
	 */
	private int playerCount;
	/**
	 * Die Bildschrimbreite
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
	 * Logger f&uuml;r die Errors
	 */
	private ExceptionLogger exceptionLogger;
	/**
	 * Die Server-Statusanzeige
	 */
	private static JLabel serverState;
	/**
	 * Anzahl der Gesamtslots eines Servers
	 */
	private static int slot;

	private AITest ai;
	
	
	public ServerHost(ServerGUIBg serverGuiBg) {
		ai = new AITest();
		ServerHost.serverGuiBg = serverGuiBg;
		super.setLayout(new GridBagLayout());
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		width = d.width;
		height = d.height;
		rectWidth = width/4+width/16;
		rectHeight = height/5;
		playerCount = 4;
		exceptionLogger = ExceptionLogger.getInstance();
		createWidgets();
		addWidgets();
		setupInteraction();
	}
	/**
	 * Erzeugt die einzelnen Panelelemente
	 */
	public void createWidgets() {
		serverStart_3 = new PlayerButton(ImportServerImages.serverButton_3,
				width/10, height/12);
		serverStart_4 = new PlayerButton(ImportServerImages.serverButton_4,
				width/10, height/12);
		ki = new PlayerButton(ImportServerImages.joinKi,
				width/10, height/12);
		serverState = new JLabel(Messages.getString("ServerHost.ServerOffline")); //$NON-NLS-1$
		serverState.setForeground(Color.WHITE);
		serverState.setFont(new Font("Courier", Font.BOLD, height/40)); //$NON-NLS-1$
		serverState.setSize(width/30, height/40);
	}

	/**
	 * F&uuml;gt dem Hostpanel die Elemente hinzu
	 */
	public void addWidgets() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.ipadx = 1;
		gbc.ipady = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		super.add(serverStart_3, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.ipadx = 1;
		gbc.ipady = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		super.add(ki, gbc);
		ki.setVisible(false);
		ki.setEnabled(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.ipadx = 1;
		gbc.ipady = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		super.add(serverStart_4, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 7;
		gbc.anchor = GridBagConstraints.CENTER;
		super.add(serverState, gbc);
		
	}

	/**
	 * Interaktion der Buttons
	 */
	public void setupInteraction() {
		ki.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
					try {
						ai.join();
					} catch (IOException e) {

						exceptionLogger.writeException(Level.SEVERE, Messages.getString("ServerHost.IOException.KI")); //$NON-NLS-1$

					}
				
			}
		});
		serverStart_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				slot = 3;
				setPlayerCount(3);
				Thread actionThread = new Thread(new Runnable() {
					public void run() {
						try {
							ServerController serverController = new ServerController();
							if (serverController.getIsServerRunning()) {
								serverState.setText(Messages.getString("ServerHost.ServerOnline")); //$NON-NLS-1$
								serverState.setForeground(Color.GREEN);
								@SuppressWarnings("unused")
								java.util.Date date= new java.util.Date();
								SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
								String uhrzeit = sdf.format(new Date());
								serverGuiBg.getServerLog().append(uhrzeit+" :"); //$NON-NLS-1$
								serverGuiBg.getServerLog().setFont(new Font("Impact", Font.BOLD, height/60)); //$NON-NLS-1$
								serverGuiBg.getServerLog().append(Messages.getString("ServerHost.ServerRunning")); //$NON-NLS-1$
								serverGuiBg.getServerLog().setFont(new Font("Courier", Font.BOLD, height/60)); //$NON-NLS-1$
								serverGuiBg.getServerLog().setVisible(true);
								serverStart_3.setVisible(false);
								ki.setVisible(true);
								serverStart_4.changeIcon(ImportServerImages.shutDownIcon);
								serverStart_4.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										System.exit(0);
									}
								});
								
							}
							serverController.startServer(3);
						} catch (Exception e) {
							exceptionLogger.writeException(Level.SEVERE, Messages.getString("ServerHost.Exception.3Spieler")); //$NON-NLS-1$
						}
					}
				});
				actionThread.start();
			}
		});
		serverStart_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPlayerCount(4);
				Thread actionThread = new Thread(new Runnable() {
					public void run() {
						try {
							ServerController serverController = new ServerController();
							if (serverGuiBg.getServerGUI()
									.getServerController().getIsServerRunning()) {
								serverState.setText(Messages.getString("ServerHost.ServerOnline")); //$NON-NLS-1$
								serverState.setForeground(Color.GREEN);
								@SuppressWarnings("unused")
								java.util.Date date= new java.util.Date();
								serverGuiBg.getServerLog().setFont(new Font("Impact", Font.BOLD, height/60)); //$NON-NLS-1$
								serverGuiBg.getServerLog().setFont(new Font("Courier", Font.BOLD, height/60)); //$NON-NLS-1$
								serverGuiBg.getServerLog().append(Messages.getString("ServerHost.ServerRunning")); //$NON-NLS-1$
								serverGuiBg.getServerLog().setVisible(true);
								serverStart_3.setVisible(false);
								ki.setVisible(true);
								serverStart_4.changeIcon(ImportServerImages.shutDownIcon);
								serverStart_4.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										System.exit(0);
									}
								
								});
								
							}
							serverController.startServer(4);
						} catch (Exception e) {
							exceptionLogger.writeException(Level.SEVERE, Messages.getString("ServerHost.Exception.4Spieler")); //$NON-NLS-1$
						}
					}
				});
				actionThread.start();
			}
		});
	}
	/**
	 * Setzt den Hintergrund des Panels farbig
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphic = (Graphics2D) g;
		Rectangle rect = new Rectangle(0, 0, rectWidth, rectHeight);
		graphic.setColor(new Color(221, 48, 60));
		graphic.fill(rect);
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public static void write(String string) {
		serverGuiBg.getServerLog().append(string);
	}
	public static void writeState(String string){
		serverState.setText(string);
	}
	public ServerGUIBg getServerGuiBg() {
		return serverGuiBg;
	}
	public static JLabel getServerState(){
		return serverState;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public static int getSlot() {
		return slot;
	}
	public static PlayerButton getKiKey() {
		return ki;
	}
	public static void setKiActive() {
		getKiKey().setEnabled(true);
	}
	public static void setKiInactive() {
		getKiKey().setEnabled(false);
	}
	


}
