package gui;

import java.awt.*;
import javax.swing.*;
import model.*;
import controller.Controller;

/**
 * Die Klasse erzeugt das GUI-Element Supply Panel, in dem die verf&uuml;gbaren
 * Resourcen des Spielers zu jedem Zeitpunkt angezeigt und geupdatet werden.
 * 
 * @author Fabian Schilling, Thomas Wimmer, Christian Hemauer, Florian Weiss
 */

@SuppressWarnings("serial")
public class SupplyPanel extends JPanel {

	/**
	 * Wolle-Icon
	 */
	private ImagePanel wool;

	/**
	 * Erz-Icon
	 */
	private ImagePanel ore;

	/**
	 * Lehm-Icon
	 */
	private ImagePanel brick;

	/**
	 * Holz-Icon
	 */
	private ImagePanel lumber;

	/**
	 * Getreide-Icon
	 */
	private ImagePanel grain;

	/**
	 * Anzahl der Siegpunkte-Icon
	 */
	private ImagePanel victoryPoints;

	/**
	 * Anzahl der Stra&szlig;en-Icon
	 */
	private ImagePanel road;

	/**
	 * Anzahl der Siedlungen-Icon
	 */
	private ImagePanel settlement;

	/**
	 * Anzahl der St&auml;dte-Icon
	 */
	private ImagePanel city;

	/**
	 * Anzahl der Rohstoffkarten-Icon
	 */
	private ImagePanel cards;

	/**
	 * Anzahl der Ritter-Icon
	 */
	private ImagePanel army;

	/**
	 * Der gute alte Controller
	 */
	private Controller controller;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Gr&ouml;&szlig;enangabe der Icons
	 */
	private int size;

	/**
	 * Der Konstrukor erzeugt ein neues SupplyPanel
	 * 
	 * @param controller
	 *            ist der Contoller
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public SupplyPanel(Controller controller, int width, int height) {
		this.controller = controller;
		this.height = height;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));
		init();
	}

	/**
	 * Initialisiert das <code>SupplyPanel</code>.
	 */
	public void init() {
		createWidgets();
		addWidgets();
	}

	/**
	 * Erstellt die Bestandteile des <code>SupplyPanels</code>.
	 */
	public void createWidgets() {
		size = (int) (height * 0.14);

		/**
		 * Buttons werden in der Gr&ouml;&szlig;e <code>size</code> erstellt
		 */
		wool = new ImagePanel(ImportImages.woolBtn, size, size);
		ore = new ImagePanel(ImportImages.oreBtn, size, size);
		brick = new ImagePanel(ImportImages.brickBtn, size, size);
		lumber = new ImagePanel(ImportImages.lumberBtn, size, size);
		grain = new ImagePanel(ImportImages.grainBtn, size, size);
		victoryPoints = new ImagePanel(ImportImages.victoryPointBtn, size, size);
		road = new ImagePanel(ImportImages.roadBtn, size, size);
		settlement = new ImagePanel(ImportImages.settlementBtn, size, size);
		city = new ImagePanel(ImportImages.cityBtn, size, size);
		cards = new ImagePanel(ImportImages.cardBtn, size, size);
		army = new ImagePanel(ImportImages.armyLbl, size, size);

		/**
		 * Zu den Icons werden Labels mit den jeweiligen Anzahlen und Tooltips
		 * hinzugef&uuml;gt
		 */
		wool.addLabel("0", Color.BLACK); //$NON-NLS-1$
		wool.setToolTipText(Messages.getString("SupplyPanel.Wolle")); //$NON-NLS-1$
		ore.addLabel("0", Color.BLACK); //$NON-NLS-1$
		ore.setToolTipText(Messages.getString("SupplyPanel.Erz")); //$NON-NLS-1$
		brick.addLabel("0", Color.BLACK); //$NON-NLS-1$
		brick.setToolTipText(Messages.getString("SupplyPanel.Lehm")); //$NON-NLS-1$
		lumber.addLabel("0", Color.BLACK); //$NON-NLS-1$
		lumber.setToolTipText(Messages.getString("SupplyPanel.Holz")); //$NON-NLS-1$
		grain.addLabel("0", Color.BLACK); //$NON-NLS-1$
		grain.setToolTipText(Messages.getString("SupplyPanel.Weizen")); //$NON-NLS-1$
		victoryPoints.addLabel("0", Color.BLACK); //$NON-NLS-1$
		victoryPoints.setToolTipText(Messages.getString("SupplyPanel.Siegpunkte")); //$NON-NLS-1$
		road.addLabel("15", Color.BLACK); //$NON-NLS-1$
		road.setToolTipText(Messages.getString("SupplyPanel.BenoetigenFall1")); //$NON-NLS-1$
		settlement.addLabel("5", Color.BLACK); //$NON-NLS-1$
		settlement
				.setToolTipText(Messages.getString("SupplyPanel.BenoetigenFall2")); //$NON-NLS-1$
		city.addLabel("4", Color.BLACK); //$NON-NLS-1$
		city.setToolTipText(Messages.getString("SupplyPanel.BenoetigenFall3")); //$NON-NLS-1$
		cards.addLabel("0", Color.BLACK); //$NON-NLS-1$
		cards.setToolTipText(Messages.getString("SupplyPanel.BenoetigenFall4")); //$NON-NLS-1$
		army.addLabel("0", Color.BLACK); //$NON-NLS-1$
		army.setToolTipText(Messages.getString("SupplyPanel.Ritter")); //$NON-NLS-1$
	}

	/**
	 * F&uuml;gt dem <code>SupplyPanel</code> alle Bestandteile hinzu.
	 */
	public void addWidgets() {
		/**
		 * Initialisierung und Bef&uuml;llung des Gridbags mit den Elementen
		 */
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(wool, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(ore, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(brick, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(lumber, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(grain, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 5, 0);
		add(victoryPoints, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(-size / 2, 0, 5, 10);
		add(road, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(-size / 2, 0, 5, 10);
		add(settlement, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(-size / 2, 0, 5, 10);
		add(city, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(-size / 2, 0, 5, 10);
		add(cards, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(-size / 2, 0, 5, 10);
		add(army, c);
	}

	/**
	 * Aktualisiert die Anzeige des SupplyPanels.
	 */
	public void update() {
		Settler settler = controller.getClient().getSettler();
		wool.setText("" + settler.getWool()); //$NON-NLS-1$
		ore.setText("" + settler.getOre()); //$NON-NLS-1$
		brick.setText("" + settler.getBrick()); //$NON-NLS-1$
		lumber.setText("" + settler.getLumber()); //$NON-NLS-1$
		grain.setText("" + settler.getGrain()); //$NON-NLS-1$
		victoryPoints.setText("" //$NON-NLS-1$
				+ (settler.getTempScore() + settler
						.getAmountOfDevCard(Constants.VICTORYPOINTS)));
		cards.setText("" //$NON-NLS-1$
				+ (settler.getAmountOfDevCards() + settler
						.getAmountOfTempDevCards()));
		road.setText("" + (Constants.ROADS_MAX - settler.getRoadCount())); //$NON-NLS-1$
		settlement.setText("" //$NON-NLS-1$
				+ (Constants.SETTLEMENTS_MAX - settler.getSettlementCount()));
		city.setText("" + (Constants.CITIES_MAX - settler.getCityCount())); //$NON-NLS-1$
		army.setText("" + settler.getArmyCount()); //$NON-NLS-1$
	}
}
