package gui;

import java.awt.*;
import javax.swing.*;

/**
 * Diese Klasse stellt das Baukostenmen&uuml; aus der GUI dar
 * 
 * @author Florian Weiss, Fabian Schilling
 * 
 */

@SuppressWarnings("serial")
public class BuildingCostsMenu extends JPanel {

	/**
	 * Die Breite des Panels
	 */
	private int width;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Das Hintergrundpanel
	 */
	private ImagePanel bgpanel;

	/**
	 * Ein Siedlungs-Icon
	 */
	private ImagePanel settlementpanel;

	/**
	 * Ein Stra&szlig;en-Icon
	 */
	private ImagePanel roadpanel;

	/**
	 * Ein Stadt-Icon
	 */
	private ImagePanel citypanel;

	/**
	 * Ein EntwicklungskartenIcon
	 */
	private ImagePanel cardpanel;

	/**
	 * Istgleichzeichen
	 */
	private JLabel costs1;

	/**
	 * Istgleichzeichen
	 */
	private JLabel costs2;

	/**
	 * Istgleichzeichen
	 */
	private JLabel costs3;

	/**
	 * Istgleichzeichen
	 */
	private JLabel costs4;

	/**
	 * Wolle-Icon
	 */
	private ImagePanel wool;

	/**
	 * Wolle-Icon
	 */
	private ImagePanel wool1;

	/**
	 * Erz-Icon
	 */
	private ImagePanel ore;

	/**
	 * Erz-Icon
	 */
	private ImagePanel ore1;

	/**
	 * Erz-Icon
	 */
	private ImagePanel ore2;

	/**
	 * Erz-Icon
	 */
	private ImagePanel ore3;

	/**
	 * Lehm-Icon
	 */
	private ImagePanel brick;

	/**
	 * Lehm-Icon
	 */
	private ImagePanel brick1;

	/**
	 * Holz-Icon
	 */
	private ImagePanel lumber;

	/**
	 * Holz-Icon
	 */
	private ImagePanel lumber1;

	/**
	 * Getreide-Icon
	 */
	private ImagePanel grain;

	/**
	 * Getreide-Icon
	 */
	private ImagePanel grain1;

	/**
	 * Getreide-Icon
	 */
	private ImagePanel grain2;

	/**
	 * Getreide-Icon
	 */
	private ImagePanel grain3;

	/**Konstruktor der Anzeige des Baukostenmenus
	 * @param width Breite
	 * @param height H&ouml;he
	 */
	public BuildingCostsMenu(int width, int height) {
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		init();

	}

	/**
	 * Initialisierung
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	private void createWidgets() {
		int size = (int) (height / 13);

		bgpanel = new ImagePanel(ImportImages.chatBg, width, height);

		roadpanel = new ImagePanel(ImportImages.roadBtn, size, size);
		settlementpanel = new ImagePanel(ImportImages.settlementBtn, size, size);
		citypanel = new ImagePanel(ImportImages.cityBtn, size, size);
		cardpanel = new ImagePanel(ImportImages.cardBtn, size, size);

		wool = new ImagePanel(ImportImages.woolBtn, size, size);
		wool1 = new ImagePanel(ImportImages.woolBtn, size, size);
		ore = new ImagePanel(ImportImages.oreBtn, size, size);
		ore1 = new ImagePanel(ImportImages.oreBtn, size, size);
		ore2 = new ImagePanel(ImportImages.oreBtn, size, size);
		ore3 = new ImagePanel(ImportImages.oreBtn, size, size);
		brick = new ImagePanel(ImportImages.brickBtn, size, size);
		brick1 = new ImagePanel(ImportImages.brickBtn, size, size);
		lumber = new ImagePanel(ImportImages.lumberBtn, size, size);
		lumber1 = new ImagePanel(ImportImages.lumberBtn, size, size);
		grain = new ImagePanel(ImportImages.grainBtn, size, size);
		grain1 = new ImagePanel(ImportImages.grainBtn, size, size);
		grain2 = new ImagePanel(ImportImages.grainBtn, size, size);
		grain3 = new ImagePanel(ImportImages.grainBtn, size, size);

		Font f = new Font("Times New Roman", Font.BOLD, 50); //$NON-NLS-1$

		costs1 = new JLabel("="); //$NON-NLS-1$
		costs1.setFont(f);
		costs2 = new JLabel("="); //$NON-NLS-1$
		costs2.setFont(f);
		costs3 = new JLabel("="); //$NON-NLS-1$
		costs3.setFont(f);
		costs4 = new JLabel("="); //$NON-NLS-1$
		costs4.setFont(f);
	}

	private void setupInteraction() {

		wool.setToolTipText(Messages.getString("BuildingCostsMenu.Wolle")); //$NON-NLS-1$
		wool1.setToolTipText(Messages.getString("BuildingCostsMenu.Wolle")); //$NON-NLS-1$
		ore.setToolTipText(Messages.getString("BuildingCostsMenu.Eisen")); //$NON-NLS-1$
		ore1.setToolTipText(Messages.getString("BuildingCostsMenu.Eisen")); //$NON-NLS-1$
		ore2.setToolTipText(Messages.getString("BuildingCostsMenu.Eisen")); //$NON-NLS-1$
		ore3.setToolTipText(Messages.getString("BuildingCostsMenu.Eisen")); //$NON-NLS-1$
		brick.setToolTipText(Messages.getString("BuildingCostsMenu.Lehm")); //$NON-NLS-1$
		brick1.setToolTipText(Messages.getString("BuildingCostsMenu.Lehm")); //$NON-NLS-1$
		lumber.setToolTipText(Messages.getString("BuildingCostsMenu.Holz")); //$NON-NLS-1$
		lumber1.setToolTipText(Messages.getString("BuildingCostsMenu.Holz")); //$NON-NLS-1$
		grain.setToolTipText(Messages.getString("BuildingCostsMenu.Weizen")); //$NON-NLS-1$
		grain1.setToolTipText(Messages.getString("BuildingCostsMenu.Weizen")); //$NON-NLS-1$
		grain2.setToolTipText(Messages.getString("BuildingCostsMenu.Weizen")); //$NON-NLS-1$
		grain3.setToolTipText(Messages.getString("BuildingCostsMenu.Weizen")); //$NON-NLS-1$

		roadpanel.setToolTipText(Messages.getString("BuildingCostsMenu.Strasse")); //$NON-NLS-1$
		settlementpanel.setToolTipText(Messages.getString("BuildingCostsMenu.Siedlung")); //$NON-NLS-1$
		citypanel.setToolTipText(Messages.getString("BuildingCostsMenu.Stadt")); //$NON-NLS-1$
		cardpanel.setToolTipText(Messages.getString("BuildingCostsMenu.Entwicklungskarte")); //$NON-NLS-1$

	}

	private void addWidgets() {
		add(bgpanel);
		bgpanel.setLayout(new GridBagLayout());

		/**
		 * Stra§e
		 */
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(roadpanel, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 0, 20);
		bgpanel.add(costs1, c);

		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(brick, c);

		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(lumber, c);

		/**
		 * Siedlung
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(settlementpanel, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 0, 20);
		bgpanel.add(costs2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(grain, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(wool, c);

		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(brick1, c);

		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(lumber1, c);

		/**
		 * Stadt
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(citypanel, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 0, 20);
		bgpanel.add(costs3, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(grain2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(grain1, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(ore1, c);

		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(ore2, c);

		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(ore, c);

		/**
		 * Entwicklungskarte
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(cardpanel, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 0, 20);
		bgpanel.add(costs4, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(grain3, c);

		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(wool1, c);

		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 5);
		bgpanel.add(ore3, c);

	}
}
