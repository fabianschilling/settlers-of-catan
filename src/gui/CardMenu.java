package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import model.Constants;
import javax.swing.*;

import model.IslandOfCatan;
import model.Settler;
import controller.Controller;

/**
 * Die Klasse stellt das Menu dar, mit dem man Entwicklungkarten kaufen und
 * ausspielen kann
 * 
 * @author Fabian Schilling
 */

@SuppressWarnings("serial")
public class CardMenu extends JPanel {

	
	/**
	 * Schriftrolle als Hintergrund
	 */
	private ImagePanel bgpanel;

	/**
	 * Die Arraylist enth&auml;lt die Entwicklungskarten
	 */
	private ArrayList<Byte> dev;

	/**
	 * Text &uuml;ber den Buttons
	 */
	private JLabel choose;

	/**
	 * Text &uuml;ber den Buttons
	 */
	private JLabel choose2;

	/**
	 * Ritterkarte
	 */
	private PlayerButton knight;
	/**
	 * Ritterkarten Anzahl
	 */
	private ImagePanel knightAmount;

	/**
	 * Erfindungskarte
	 */
	private PlayerButton invention;
	/**
	 * Erfindungskarten Anzahl
	 */
	private ImagePanel inventionAmount;

	/**
	 * Monopolkarte
	 */
	private PlayerButton monopoly;
	/**
	 * Monopolkarten Anzahl
	 */
	private ImagePanel monopolyAmount;

	/**
	 * Stra&szlig;enkarte
	 */
	private PlayerButton streets;
	/**
	 * Stra&szlig;enkarten Anzahl
	 */
	private ImagePanel streetsAmount;

	/**
	 * Siegpunktkarte
	 */
	private PlayerButton victory;
	/**
	 * Siegpunktkarten Anzahl
	 */
	private ImagePanel victoryAmount;

	/**
	 * Kartenr&uuml;ckseite
	 */
	private PlayerButton backside;
	/**
	 * Anzahl der Karten auf dem Stapel
	 */
	private ImagePanel backsideAmount;
	
	/**
	 * Exit Button
	 */
	private PlayerButton exit;
	/**
	 * Exit Label
	 */
	private JLabel exitLabel;

	/**
	 * Breite der Buttons
	 */
	private int btnwidth;

	/**
	 * H&ouml;he der Buttons
	 */
	private int btnheight;
	/**
	 * Button Gr&ouml;&szlig;e
	 */
	private int sbtn;

	/**
	 * Breite des Panels
	 */
	private int width;

	/**
	 * H&ouml;he des Panles
	 */
	private int height;

	/**
	 * Der gute alte Controller
	 */
	private Controller controller;

	/**
	 * Das Modell der Insel
	 */
	private IslandOfCatan island;

	/**
	 * Der Current Settler
	 */
	private Settler settler;

	/**
	 * Erzeugt eine Instanz des CardMenus Hier kann man sich f&uuml;r das
	 * Ausspiel bzw. f&uuml;r den Kauf einer Entwicklungskarte entscheiden
	 * 
	 * @param controller
	 *            ist der Controller
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public CardMenu(Controller controller, int width, int height) {
		this.controller = controller;
		this.width = (int) (width*0.9);
		this.height = (int) (height*0.9);
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		this.island = controller.getIsland();
		this.settler = island.getCurrentPlayer();

		init();
	}

	/**
	 * Hiermit wird die GUI initialisiert
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	/**
	 * Hiermit werden die GUI-Elemente erzeugt
	 */
	private void createWidgets() {
		/**
		 * Hintergrundpanel (Schriftrolle)
		 */
		bgpanel = new ImagePanel(ImportImages.chatBg, width, height);

		btnwidth = (int) (width * 0.13);
		btnheight = (int) (width * 0.13);
		sbtn = btnheight/3;

		knight = new PlayerButton(ImportImages.knightBtn, btnwidth, btnheight);
		knight.setToolTipText("Ritter");
		knightAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		knightAmount.addLabel("0", Color.WHITE);
		invention = new PlayerButton(ImportImages.inventionBtn, btnwidth, btnheight);
		invention.setToolTipText("Erfindung");
		inventionAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		inventionAmount.addLabel("0", Color.WHITE);
		monopoly = new PlayerButton(ImportImages.monopolyBtn, btnwidth, btnheight);
		monopoly.setToolTipText("Monopol");
		monopolyAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		monopolyAmount.addLabel("0", Color.WHITE);
		streets = new PlayerButton(ImportImages.buildStreetBtn, btnwidth, btnheight);
		streets.setToolTipText("Strassenbau");
		streetsAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		streetsAmount.addLabel("0", Color.WHITE);
		victory = new PlayerButton(ImportImages.victoryPointBtn, btnwidth, btnheight);
		victory.setToolTipText("Siegpunkt");
		victoryAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		victoryAmount.addLabel("0", Color.WHITE);
		backside = new PlayerButton(ImportImages.buyCardBtn, btnwidth, btnheight);
		backside.setToolTipText("Ziehe eine Entwicklungskarte!");
		backsideAmount = new ImagePanel(ImportImages.buttonInvert, sbtn, sbtn);
		backsideAmount.addLabel(""+Constants.DEVCARDS_MAX, Color.WHITE);
		
		exit = new PlayerButton(ImportImages.cancelBtn, sbtn, sbtn);
		exitLabel = new JLabel("Menu verlassen");
		exitLabel.setFont(new Font("Times New Roman", Font.ITALIC, sbtn/2));

		Font f = new Font("Times New Roman", Font.ITALIC, (int)(sbtn * 0.7)); //$NON-NLS-1$
		choose = new JLabel(Messages.getString("CardMenu.Ausspielen")); //$NON-NLS-1$
		choose.setFont(f);
		choose2 = new JLabel(
				Messages.getString("CardMenu.Kaufen")); //$NON-NLS-1$
		choose2.setFont(f);

	}

	/**
	 * F&uuml;gt dem <code>CardMenu</code> die Interaktion mit dem <code>Controller</code> hinzu.
	 */
	public void setupInteraction() {
		addMouseListener(controller);

		knight.addActionListener(controller);
		knight.setActionCommand("card.knight"); //$NON-NLS-1$
		knight.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.knightBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.knightBtn);
				}
			}
		});
		invention.addActionListener(controller);
		invention.setActionCommand("card.invention"); //$NON-NLS-1$
		invention.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.inventionBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.inventionBtn);
				}
			}
		});
		monopoly.addActionListener(controller);
		monopoly.setActionCommand("card.monopoly"); //$NON-NLS-1$
		monopoly.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.monopolyBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.monopolyBtn);
				}
			}
		});
		streets.addActionListener(controller);
		streets.setActionCommand("card.streets"); //$NON-NLS-1$
		streets.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.buildStreetBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.buildStreetBtn);
				}
			}
		});
		victory.addActionListener(controller);
		victory.setActionCommand("card.victory"); //$NON-NLS-1$
		backside.addActionListener(controller);
		backside.setActionCommand("card.draw"); //$NON-NLS-1$
		backside.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.buyCardBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.buyCardBtn);
				}
			}
		});
		exit.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				getThis().setVisible(false);	
			}
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtn);
			}
		});
	}

	/**
	 * F&uuml;gt die einzelnen Bestandteile dem <code>Cardmenu</code> hinzu.
	 */
	public void addWidgets() {

		add(bgpanel);
		bgpanel.setLayout(new GridBagLayout());

		/**
		 * oberste Reihe
		 */

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 7;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 10, 0);
		bgpanel.add(choose, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 7;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 20, 0);
		bgpanel.add(choose2, c);

		/**
		 * zweite Reihe
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(inventionAmount, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 40);
		bgpanel.add(invention, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(streetsAmount, c);
		
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 40);
		bgpanel.add(streets, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(monopolyAmount, c);
		
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(10, 10, 10, 10);
		bgpanel.add(monopoly, c);

		/**
		 * dritte Reihe
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(knightAmount, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 40);
		bgpanel.add(knight, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(victoryAmount, c);
		
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 40);
		bgpanel.add(victory, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(backsideAmount, c);

		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(10, 10, 10, 10);
		bgpanel.add(backside, c);
		
		/**
		 * vierte Reihe
		 */
		
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 4;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.insets = new Insets(10, 20, 5, 20);
		bgpanel.add(exit, c);
		
		/**
		 * f�nfte Reihe
		 */
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 5;
		c.anchor = GridBagConstraints.SOUTHEAST;
		bgpanel.add(exitLabel, c);
	}

	/**
	 * Aktualisierung des Karten Menus
	 */
	public void update() {
		settler = island.getCurrentPlayer();
		dev = settler.getDevelopmentCards();
		invention.setEnabled(dev.contains(Constants.GETRESOURCES));
		inventionAmount.getLabel().setText(settler.getAmountOfDevCard(Constants.GETRESOURCES) + "");
		streets.setEnabled(dev.contains(Constants.BUILDSTREETS));
		streetsAmount.getLabel().setText(settler.getAmountOfDevCard(Constants.BUILDSTREETS) + "");
		monopoly.setEnabled(dev.contains(Constants.MONOPOLY));
		monopolyAmount.getLabel().setText(settler.getAmountOfDevCard(Constants.MONOPOLY) + "");
		knight.setEnabled(dev.contains(Constants.KNIGHT));
		knightAmount.getLabel().setText(settler.getAmountOfDevCard(Constants.KNIGHT) + "");
		victory.setEnabled(dev.contains(Constants.VICTORYPOINTS));
		victoryAmount.getLabel().setText(settler.getAmountOfDevCard(Constants.VICTORYPOINTS) + "");
		backside.setEnabled(settler.getWool() >= 1 && settler.getOre() >= 1 && settler.getGrain() >= 1);
		backsideAmount.getLabel().setText((Constants.DEVCARDS_MAX - controller.getIsland().getDrawnDevCard())+ "");
	}
	
	public CardMenu getThis(){
		return this;
	}
}
