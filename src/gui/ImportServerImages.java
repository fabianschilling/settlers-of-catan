package gui;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Verwaltet importierte Grafiken des Servers.
 * 
 * @author Florian Weiss
 * 
 */
public class ImportServerImages {
	/**
	 * Shut Down Button
	 */
	public static Image shutDownIcon;
	/**
	 * Host Button
	 */
	public static Image hostIcon;
	
	/**
	 * Join Button
	 */
	public static Image joinIcon;
	
	/**
	 * Abbruch Button
	 */
	public static Image denyBtn;
	
	/**
	 * Best&auml;tigen
	 */
	public static Image confirmBtn;
	
	/**
	 * Hintergrundbild
	 */
	public static Image logoBackground;
	/**
	 * Button der ServerGUI
	 */
	public static Image serverButton;
	/**
	 * 3-Spieler Button
	 */
	public static Image serverButton_3;
	/**
	 * 4-Spieler Button
	 */
	public static Image serverButton_4;

	/**
	 * SpaceMarine Avatar
	 */
	public static Image spaceMarineAvatar;
	/**
	 * Kerrigan Avatar
	 */
	public static Image kerriganAvatar;
	/**
	 * Zerg Avatar
	 */
	public static Image zergAvatar;
	/**
	 * Toss Avatar
	 */
	public static Image tossAvatar;
	/**
	 * Artas Avatar
	 */
	public static Image artasAvatar;
	/**
	 * Daemon Avatar
	 */
	public static Image daemonAvatar;
	/**
	 * Human Avatar
	 */
	public static Image humanAvatar;
	/**
	 * Nightelf Avatar
	 */
	public static Image nightelfAvatar;
	/**
	 * Orc Avatar
	 */
	public static Image orcAvatar;
	/**
	 * Undead Avatar
	 */
	public static Image undeadAvatar;
	/**
	 * Join German Button
	 */
	public static Image joinGerman;
	/**
	 * Join English Button
	 */
	public static Image joinEnglish;
	/**
	 * Button zum Hinzuf&uuml;gen der KI
	 */
	public static Image joinKi;
	/**
	 * Button zum Schlie&szlig;en
	 */
	public static Image quitKey;
	
	/**
	 * L&auml;dt die Bilder
	 */
	public void loadServerPics() {
		joinEnglish = new ImageIcon(getClass().getResource("graphics/info/buttonAmerican.png")).getImage();
		joinGerman = new ImageIcon(getClass().getResource("graphics/info/buttonGerman.png")).getImage();
		joinIcon = new ImageIcon(getClass().getResource(
				"graphics/info/join.png")).getImage(); //$NON-NLS-1$
		
		denyBtn = new ImageIcon(getClass().getResource(
				"graphics/buttonPics/cancelBtn.png")).getImage(); //$NON-NLS-1$
		
		confirmBtn = new ImageIcon(getClass().getResource(
				"graphics/buttonPics/confirmBtn.png")).getImage(); //$NON-NLS-1$
		
		logoBackground  = new ImageIcon(getClass().getResource(
				"graphics/background/Siedler_von_Catan_Logo.png")).getImage(); //$NON-NLS-1$
		
		serverButton = new ImageIcon(getClass().getResource(
				"graphics/info/ServerButton.png")).getImage(); //$NON-NLS-1$
		
		serverButton_3 = new ImageIcon(getClass().getResource(
				"graphics/info/3Spieler.png")).getImage(); //$NON-NLS-1$
		
		serverButton_4 = new ImageIcon(getClass().getResource(
				"graphics/info/4Spieler.png")).getImage(); //$NON-NLS-1$
		
		shutDownIcon = new ImageIcon(getClass().getResource(
				"graphics/info/Quit.png")).getImage();

		kerriganAvatar = new ImageIcon(getClass().getResource(
				"graphics/avatars/kerrigan.png")).getImage(); //$NON-NLS-1$

		zergAvatar = new ImageIcon(getClass().getResource(
				"graphics/avatars/zerg.png")).getImage(); //$NON-NLS-1$

		tossAvatar = new ImageIcon(getClass().getResource(
				"graphics/avatars/toss.png")).getImage(); //$NON-NLS-1$
		
		artasAvatar = new ImageIcon(getClass().getResource("graphics/avatars/artas.png")).getImage();
		
		daemonAvatar = new ImageIcon(getClass().getResource("graphics/avatars/daemon.png")).getImage();
		
		humanAvatar = new ImageIcon(getClass().getResource("graphics/avatars/human.png")).getImage();
		
		nightelfAvatar = new ImageIcon(getClass().getResource("graphics/avatars/nightelf.png")).getImage();
		
		orcAvatar = new ImageIcon(getClass().getResource("graphics/avatars/orc.png")).getImage();
		
		undeadAvatar = new ImageIcon(getClass().getResource("graphics/avatars/undead.png")).getImage();
		
		joinKi = new ImageIcon(getClass().getResource("graphics/info/JoinKI.png")).getImage();
		
		quitKey = new ImageIcon(getClass().getResource("graphics/info/schlieb.png")).getImage();
	}
}
