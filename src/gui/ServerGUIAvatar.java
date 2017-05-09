package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Klasse, die das Avatarwahlfenster erzeugt
 * @author Fabian Schilling, Christian Hemauer, Florian Weiss
 *
 */
@SuppressWarnings("serial")
public class ServerGUIAvatar extends JFrame {
	/**
	 * Die Breite des Bildschirms
	 */
	private int width;
	/**
	 * Die H&ouml;he des Bildschirms
	 */
	private int height;
	/**
	 * Die Gr&ouml;&szlig;e der Avatarbuttons
	 */
	private int size;
	/**
	 * Die Abst&auml;nde zwischen den Avatarbuttons
	 */
	private int inset;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton kerrigan;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton toss;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton zerg;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton artas;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton daemon;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton human;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton nightelf;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton orc;
	/**
	 * Button, der einen Avatar darstellt - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton undead;
	/**
	 * Button zum Festlegen der Avatarwahl - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton confirm;
	/**
	 * &Uuml;berschrift der Avatarauswahl
	 */ 
	private RichJLabel choose;
	/**
	 * Panel, das den Gesamtinhalt des Frames h&auml;lt
	 */
	private JPanel content;
	@SuppressWarnings("unused")
	private JPanel contentPane;
	/**
	 * Nummer f&uuml;r Avatar im AvatarArray
	 */
	private int avatarNumber;
	
	
	
	/**
	 * Anzeige des Zeitraums zur Avatarwahl
	 */
	private RichJLabel countDown;

	public ServerGUIAvatar(int width, int height) throws HeadlessException{
		this.width = width;
		this.height = height;
		this.size = width/4;
		this.inset = width/100;
		avatarNumber = 0;
		
		createWidgets();
		setupInteraction();
		addWidgets();
		
		setPreferredSize(new Dimension(width, height+height/3));
		setMinimumSize(new Dimension(width, height+height/3));
		setMaximumSize(new Dimension(width, height+height/3));
		setBackground(Color.darkGray);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
	}
	/**
	 * Erzeugen der Fensterelemente
	 */
	public void createWidgets(){
		content = new JPanel();
		content.setPreferredSize(new Dimension(width, height));
		content.setLayout(new GridBagLayout());
		content.setBackground(Color.darkGray);
		content.setBorder(BorderFactory.createLineBorder(Color.WHITE,6));
		
		choose = new RichJLabel("Choose your Avatar", 1);
		choose.setFont(new Font("Impact", Font.BOLD, inset * 10));
		choose.setLeftShadow(1, 1, Color.BLACK);
	    choose.setRightShadow(3, 3, Color.BLACK);
	    choose.setForeground(Color.WHITE);
	    choose.setFont(choose.getFont().deriveFont(width/12f));
		
		kerrigan = new PlayerButton(ImportServerImages.kerriganAvatar, size, size);
		toss = new PlayerButton(ImportServerImages.tossAvatar, size, size);
		zerg = new PlayerButton(ImportServerImages.zergAvatar, size, size);
		artas = new PlayerButton(ImportServerImages.artasAvatar, size, size);
		daemon = new PlayerButton(ImportServerImages.daemonAvatar, size, size);
		daemon.setEnabled(false);
		human = new PlayerButton(ImportServerImages.humanAvatar, size, size);
		nightelf = new PlayerButton(ImportServerImages.nightelfAvatar, size, size);
		orc = new PlayerButton(ImportServerImages.orcAvatar, size, size);
		undead = new PlayerButton(ImportServerImages.undeadAvatar, size, size);
		confirm = new PlayerButton(ImportServerImages.confirmBtn,size/2,size/2);
		
		countDown = new RichJLabel("10",1);
		countDown.setFont(new Font("Impact", Font.BOLD, height/10));
		countDown.setLeftShadow(1, 1, Color.black);
		countDown.setRightShadow(4, 4, Color.black);
		countDown.setForeground(Color.WHITE);
		countDown.setFont(countDown.getFont().deriveFont(width/11f));
	}
	/**
	 * Interaktion der Buttons
	 */
	public void setupInteraction(){
		kerrigan.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(false);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 1;
			}
			
		});
		zerg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(false);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 2;
			}
			
		});
		toss.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(false);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 3;
			}
			
		});
		artas.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(false);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 4;
			}
			
		});
		human.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(false);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 6;
			}
			
		});
		daemon.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(false);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 5;
			}
			
		});
		undead.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(false);
				orc.setEnabled(true);
				nightelf.setEnabled(true);
				avatarNumber = 9;
			}
			
		});
		orc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(false);
				nightelf.setEnabled(true);
				avatarNumber = 8;
			}	
		});
		nightelf.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kerrigan.setEnabled(true);
				zerg.setEnabled(true);
				toss.setEnabled(true);
				artas.setEnabled(true);
				human.setEnabled(true);
				daemon.setEnabled(true);
				undead.setEnabled(true);
				orc.setEnabled(true);
				nightelf.setEnabled(false);
				avatarNumber = 7;
			}	
		});
	}
	/**
	 * Hinzuf&uuml;gen der einzelnen Elemente
	 */
	public void addWidgets(){
		/**
		 * Countdown
		 */
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,inset*5,0);
		content.add(countDown, c);
		/**
		 * Heading
		 */
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,inset*5,0);
		content.add(choose, c);
		
		/**
		 * First Row
		 */
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(kerrigan, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(toss, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(zerg, c);
		
		/**
		 * Second Row
		 */
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(artas, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(daemon, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(human, c);
		
		/**
		 * Third Row
		 */
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(nightelf, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(orc, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(undead, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(inset, inset, inset, inset);
		content.add(confirm, c);

		add(content);
		
	}

	public JLabel getCountDown() {
		return countDown;
	}
	public void setText(String string){
		countDown.setText(string);	
	}
	
	public PlayerButton getUndead() {
		return undead;
	}

	public PlayerButton getOrc() {
		return orc;
	}

	public PlayerButton getNightelf() {
		return nightelf;
	}

	public PlayerButton getHuman() {
		return human;
	}

	public PlayerButton getDaemon() {
		return daemon;
	}

	public PlayerButton getArtas() {
		return artas;
	}
		public PlayerButton getKerrigan() {
		return kerrigan;
	}
	public PlayerButton getZerg() {
		return zerg;
	}
	public PlayerButton getToss() {
		return toss;
	}

	public PlayerButton getConfirm() {
		return confirm;
	}

	public int getAvatarNumber() {
		return avatarNumber;
	}

}
