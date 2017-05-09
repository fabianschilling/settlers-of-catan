package gui;

import java.awt.*;
import javax.swing.*;
import model.Constants;
import controller.Controller;

/**
 * Anzeige und Auswahlmen&uuml; f&uuml;r Rohstoffe (z.B. bei Monopol-Karte).
 * 
 * @author Thomas Wimmer, Fabian Schilling
 * 
 */
@SuppressWarnings("serial")
public class ResourcePanel extends ImagePanel {

	/**
	 * Breite des <code>ResourcePanels</code>.
	 */
	private int width;
	/**
	 * H&ouml;he des <code>ResourcePanels</code>
	 */
	private int height;
	/**
	 * <code>Controller</code> f&uuml;r die Aktionselemente des
	 * <code>ResourcePanels</code>.
	 */
	private Controller controller;
	/**
	 * Enth&auml;lt die <code>PlayerButtons</code> des
	 * <code>ResourcePanels</code>.
	 */
	private JPanel contentPanel;
	/**
	 * Enth&auml;lt die Anzahl der bereits ausgew&auml;ten Rohstoffe.
	 */
	private JPanel discardPanel;
	/**
	 * Enth&auml;lt alle Bestandteile des <code>ResourcePanels</code> auf
	 * verschiedenen Ebenen.
	 */
	private JLayeredPane layeredPane;
	/**
	 * Platzhalter im <code>GridBagLayout</code> des <code>contentPanels</code>.
	 */
	private JLabel fillerLabel1;
	/**
	 * Platzhalter im <code>GridBagLayout</code> des <code>contentPanels</code>.
	 */
	private JLabel fillerLabel2;
	/**
	 * <code>PlayerButton</code> f&uuml;r Getreide.
	 */
	private PlayerButton grainBtn;
	/**
	 * <code>PlayerButton</code> f&uuml;r Erz.
	 */
	private PlayerButton oreBtn;
	/**
	 * <code>PlayerButton</code> f&uuml;r Wolle.
	 */
	private PlayerButton woolBtn;
	/**
	 * <code>PlayerButton</code> f&uuml;r Lehm.
	 */
	private PlayerButton brickBtn;
	/**
	 * <code>PlayerButton</code> f&uuml;r Holz.
	 */
	private PlayerButton lumberBtn;
	/**
	 * <code>PlayerLabel</code> f&uuml;r den ersten Rohstoff bei der
	 * Erfindungskarte.
	 */
	private PlayerLabel resource1Lbl;
	/**
	 * <code>PlayerLabel</code> f&uuml;r den zweiten Rohstoff bei der
	 * Erfindungskarte.
	 */
	private PlayerLabel resource2Lbl;
	/**
	 * <code>PlayerButton</code> zum Best&auml;tigen.
	 */
	private PlayerButton confirmBtn;
	/**
	 * <code>PlayerButton</code> zum Zur&uuml;cksetzen der Auswahl.
	 */
	private PlayerButton cancelBtn;
	/**
	 * <code>PlayerLabel</code> f&uuml;r den Rohstoff bei der Monopolkarte.
	 */
	private PlayerLabel resourceMonoLbl;
	/**
	 * <code>ImagePanel</code> f&uuml;r Getreide.
	 */
	private ImagePanel grainLbl;
	/**
	 * <code>ImagePanel</code> f&uuml;r Erz.
	 */
	private ImagePanel oreLbl;
	/**
	 * <code>ImagePanel</code> f&uuml;r Wolle.
	 */
	private ImagePanel woolLbl;
	/**
	 * <code>ImagePanel</code> f&uuml;r Lehm.
	 */
	private ImagePanel brickLbl;
	/**
	 * <code>ImagePanel</code> f&uuml;r Holz.
	 */
	private ImagePanel lumberLbl;
	/**
	 * Der erstgew&auml;lte Rohstoff.
	 */
	private String firstResource;
	/**
	 * Der zweitgew&auml;lte Rohstoff.
	 */
	private String secondResource;
	/**
	 * Gr&ouml;&szlig;e eines <code>PlayerButtons</code>
	 */
	private int btnWidth;
	/**
	 * Z&auml;hler f&uuml;r Getreide.
	 */
	private int grain;
	/**
	 * Z&auml;hler f&uuml;r Erz.
	 */
	private int ore;
	/**
	 * Z&auml;hler f&uuml;r Wolle.
	 */
	private int wool;
	/**
	 * Z&auml;hler f&uuml;r Lehm.
	 */
	private int brick;
	/**
	 * Z&auml;hler f&uuml;r Holz.
	 */
	private int lumber;
	

	/**
	 * Der Konstruktor erzeugt ein neues ResourcePanel, dass es Erm&ouml;glicht
	 * mit den Ressourcen zu interagieren (z.B. beim R&auml;uber)
	 * 
	 * @param controller
	 *            <code>Controller</code>
	 * @param width
	 *            Breite des <code>ResourcePanels</code>
	 * @param height
	 *            H&ouml;he des <code>ResourcePanels</code>
	 */
	public ResourcePanel(Controller controller, int width, int height) {
		super(ImportImages.chatBg, width, height);
		this.controller = controller;
		this.width = width;
		this.height = height;
		this.firstResource = ""; //$NON-NLS-1$
		this.secondResource = ""; //$NON-NLS-1$
		btnWidth = (int) (width * 0.2);

		grain = 0;
		ore = 0;
		wool = 0;
		brick = 0;
		lumber = 0;

		init();

		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		setVisible(false);
	}

	/**
	 * Initialisiert das <code>ResourcePanel</code>.
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	/**
	 * Erstellt die Bestandteile des <code>ResourcePanels</code>.
	 */
	public void createWidgets() {
		int sBtnWidth = (int) (width * 0.13);
		int lblWidth = (int) (width * 0.15);

		discardPanel = new JPanel();
		discardPanel.setPreferredSize(new Dimension(width,
				(int) (btnWidth * 1.2)));
		contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(width, height));
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(width, height));

		fillerLabel1 = new JLabel();
		fillerLabel1.setPreferredSize(new Dimension(sBtnWidth, sBtnWidth));

		fillerLabel2 = new JLabel();
		fillerLabel2.setPreferredSize(new Dimension(sBtnWidth, sBtnWidth));

		grainBtn = new PlayerButton(ImportImages.grainBtn, btnWidth, btnWidth);
		oreBtn = new PlayerButton(ImportImages.oreBtn, btnWidth, btnWidth);
		woolBtn = new PlayerButton(ImportImages.woolBtn, btnWidth, btnWidth);
		lumberBtn = new PlayerButton(ImportImages.lumberBtn, btnWidth, btnWidth);
		brickBtn = new PlayerButton(ImportImages.brickBtn, btnWidth, btnWidth);
		resource1Lbl = new PlayerLabel(ImportImages.confirmBtn, sBtnWidth,
				sBtnWidth);
		resource2Lbl = new PlayerLabel(ImportImages.confirmBtn, sBtnWidth,
				sBtnWidth);
		resourceMonoLbl = new PlayerLabel(ImportImages.confirmBtn, sBtnWidth,
				sBtnWidth);
		confirmBtn = new PlayerButton(ImportImages.confirmBtn,
				(int) (sBtnWidth * 0.7), (int) (sBtnWidth * 0.7));
		confirmBtn.setToolTipText(Messages.getString("ResourcePanel.AuswahlBestaetigen")); //$NON-NLS-1$
		cancelBtn = new PlayerButton(ImportImages.cancelBtn,
				(int) (sBtnWidth * 0.7), (int) (sBtnWidth * 0.7));
		cancelBtn.setToolTipText(Messages.getString("ResourcePanel.AuswahlAufheben")); //$NON-NLS-1$

		grainLbl = new ImagePanel(ImportImages.grainBtn, lblWidth, lblWidth);
		oreLbl = new ImagePanel(ImportImages.oreBtn, lblWidth, lblWidth);
		woolLbl = new ImagePanel(ImportImages.woolBtn, lblWidth, lblWidth);
		lumberLbl = new ImagePanel(ImportImages.lumberBtn, lblWidth, lblWidth);
		brickLbl = new ImagePanel(ImportImages.brickBtn, lblWidth, lblWidth);

		grainLbl.addLabel("" + grain, Color.BLACK); //$NON-NLS-1$
		oreLbl.addLabel("" + ore, Color.BLACK); //$NON-NLS-1$
		woolLbl.addLabel("" + wool, Color.BLACK); //$NON-NLS-1$
		lumberLbl.addLabel("" + lumber, Color.BLACK); //$NON-NLS-1$
		brickLbl.addLabel("" + brick, Color.BLACK); //$NON-NLS-1$

		contentPanel.setBounds(0, - (int) (height * 0.1), width, height);
		discardPanel.setBounds(0, (int) (height * 0.725), width,
				(int) (btnWidth * 1.2));

		resourceMonoLbl.setBounds((int) (width * 0.43), (int) (height * 0.35),
				sBtnWidth, sBtnWidth);
		resource1Lbl.setBounds((int) (width * 0.365), (int) (height * 0.35),
				sBtnWidth, sBtnWidth);
		resource2Lbl.setBounds((int) (width * 0.5), (int) (height * 0.35),
				sBtnWidth, sBtnWidth);

		cancelBtn.setBounds((int) (width * 0.7), (int) (height * 0.675),
				(int) (sBtnWidth * 0.7), (int) (sBtnWidth * 0.7));
		confirmBtn.setBounds((int) (width * 0.8), (int) (height * 0.6),
				(int) (sBtnWidth * 0.7), (int) (sBtnWidth * 0.7));

		contentPanel.setOpaque(false);
		layeredPane.setOpaque(false);
		discardPanel.setOpaque(false);

		discardPanel.setVisible(false);
		resource1Lbl.setVisible(false);
		resource2Lbl.setVisible(false);
		resourceMonoLbl.setVisible(false);
	}

	/**
	 * F&uuml;gt dem <code>ResourcePanel</code> die Interaktion mit dem
	 * <code>Controller</code> hinzu.
	 */
	public void setupInteraction() {
		grainBtn.setActionCommand("resP.Grain"); //$NON-NLS-1$
		grainBtn.addActionListener(controller);
		oreBtn.setActionCommand("resP.Ore"); //$NON-NLS-1$
		oreBtn.addActionListener(controller);
		woolBtn.setActionCommand("resP.Wool"); //$NON-NLS-1$
		woolBtn.addActionListener(controller);
		lumberBtn.setActionCommand("resP.Lumber"); //$NON-NLS-1$
		lumberBtn.addActionListener(controller);
		brickBtn.setActionCommand("resP.Brick"); //$NON-NLS-1$
		brickBtn.addActionListener(controller);
		confirmBtn.setActionCommand("resP.confirm"); //$NON-NLS-1$
		confirmBtn.addActionListener(controller);
		cancelBtn.setActionCommand("resP.cancel"); //$NON-NLS-1$
		cancelBtn.addActionListener(controller);
	}

	/**
	 * F&uuml;gt die Bestandteile des <code>ResourcePanels</code> dem
	 * <code>ResourcePanel</code> hinzu.
	 */
	public void addWidgets() {

		discardPanel.setLayout(new FlowLayout());
		contentPanel.setLayout(new GridBagLayout());

		discardPanel.add(grainLbl);
		discardPanel.add(oreLbl);
		discardPanel.add(woolLbl);
		discardPanel.add(lumberLbl);
		discardPanel.add(brickLbl);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.ipadx = (int) (btnWidth * 0.2);
		gbc.ipady = (int) (btnWidth * 0.2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPanel.add(grainBtn, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.ipadx = (int) (btnWidth * 0.2);
		gbc.ipady = (int) (btnWidth * 0.2);
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(oreBtn, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.ipadx = (int) (btnWidth * 0.1);
		contentPanel.add(fillerLabel1, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.ipadx = (int) (btnWidth * 0.1);
		contentPanel.add(fillerLabel2, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.ipadx = (int) (btnWidth * 0.2);
		gbc.ipady = (int) (btnWidth * 0.2);
		contentPanel.add(woolBtn, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.ipadx = (int) (btnWidth * 0.2);
		gbc.ipady = (int) (btnWidth * 0.2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(lumberBtn, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.ipadx = (int) (btnWidth * 0.2);
		gbc.ipady = (int) (btnWidth * 0.2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(brickBtn, gbc);

		layeredPane.add(contentPanel, new Integer(1));
		layeredPane.add(resourceMonoLbl, new Integer(2));
		layeredPane.add(resource1Lbl, new Integer(2));
		layeredPane.add(resource2Lbl, new Integer(2));
		layeredPane.add(confirmBtn, new Integer(2));
		layeredPane.add(cancelBtn, new Integer(2));
		layeredPane.add(discardPanel, new Integer(2));
		add(layeredPane);
		

	}

	/**
	 * Setzt den ersten gew&auml;hlten Rohstoff.
	 * 
	 * @param firstResource
	 *            erster Rohstoff
	 * @param isMonopoly
	 *            trifft Monopoly-Karte zu
	 */
	public void setFirstResource(String firstResource, boolean isMonopoly) {
		this.firstResource = firstResource;
		if (isMonopoly) {
			resourceMonoLbl.setNewIcon(new ImageIcon(getClass().getResource("graphics/buttonPics/button" //$NON-NLS-1$
					+ firstResource + ".png")).getImage()); //$NON-NLS-1$
			resourceMonoLbl.setVisible(true);
		} else {
			resource1Lbl.setNewIcon(new ImageIcon(getClass().getResource("graphics/buttonPics/button" //$NON-NLS-1$
					+ firstResource + ".png")).getImage()); //$NON-NLS-1$
			resource1Lbl.setVisible(true);
		}
	}

	/**
	 * Setzt den zweiten gew&auml;hlten Rohstoff.
	 * 
	 * @param secondResource
	 *            zweiter Rohstoff
	 */
	public void setSecondResource(String secondResource) {
		this.secondResource = secondResource;
		resource2Lbl.setNewIcon(new ImageIcon(getClass().getResource("graphics/buttonPics/button" //$NON-NLS-1$
				+ secondResource + ".png")).getImage()); //$NON-NLS-1$
		resource2Lbl.setVisible(true);
	}

	/**
	 * Gibt zur&uuml;ck ob der erste Rohstoff gew&auml;hlt wurde.
	 * 
	 * @return erster Rohstoff gew&auml;hlt
	 */
	public boolean hasFirstResource() {
		return !firstResource.equals(""); //$NON-NLS-1$
	}

	/**
	 * Gibt zur&uuml;ck ob der zweite Rohstoff gew&auml;hlt wurde.
	 * 
	 * @return zweite Rohstoff gew&auml;hlt
	 */
	public boolean hasSecondResource() {
		return !secondResource.equals(""); //$NON-NLS-1$
	}

	/**
	 * Gibt die Rohstoff-Konstante an beliebiger Position zur&uuml;ck.
	 * 
	 * @param index
	 *            Position
	 * @return Rohstoff-Konstante
	 */
	public byte getResourceByIndex(int index) {
		if (index == 1 && hasFirstResource()) {
			if (firstResource.equals("Grain")) //$NON-NLS-1$
				return Constants.GRAIN;
			else if (firstResource.equals("Ore")) //$NON-NLS-1$
				return Constants.ORE;
			else if (firstResource.equals("Lumber")) //$NON-NLS-1$
				return Constants.LUMBER;
			else if (firstResource.equals("Wool")) //$NON-NLS-1$
				return Constants.WOOL;
			else if (firstResource.equals("Brick")) //$NON-NLS-1$
				return Constants.BRICK;
			else
				return -1;
		} else if (index == 2 && hasSecondResource()) {
			if (secondResource.equals("Grain")) //$NON-NLS-1$
				return Constants.GRAIN;
			else if (secondResource.equals("Ore")) //$NON-NLS-1$
				return Constants.ORE;
			else if (secondResource.equals("Lumber")) //$NON-NLS-1$
				return Constants.LUMBER;
			else if (secondResource.equals("Wool")) //$NON-NLS-1$
				return Constants.WOOL;
			else if (secondResource.equals("Brick")) //$NON-NLS-1$
				return Constants.BRICK;
			else
				return -1;
		}
		return -1;
	}

	/**
	 * Setzt die Rohstoffe zur&uuml;ck.
	 */
	public void resetResources() {
		firstResource = ""; //$NON-NLS-1$
		secondResource = ""; //$NON-NLS-1$
		resource1Lbl.setVisible(false);
		resource2Lbl.setVisible(false);
		resourceMonoLbl.setVisible(false);
	}

	/**
	 * F&uuml;gt einen Rohstoff hinzu.
	 * 
	 * @param resource
	 *            Rohstoff-String (z.b. "Grain")
	 */
	public void addResource(String resource) {
		if (resource.equals("Grain")) //$NON-NLS-1$
			grain++;
		else if (resource.equals("Wool")) //$NON-NLS-1$
			wool++;
		else if (resource.equals("Lumber")) //$NON-NLS-1$
			lumber++;
		else if (resource.equals("Ore")) //$NON-NLS-1$
			ore++;
		else if (resource.equals("Brick")) //$NON-NLS-1$
			brick++;
		update();
	}

	/**
	 * Gibt die Menge eines Rohstoffs zur&uuml;ck.
	 * 
	 * @param resource
	 *            Rohstoff-String (z.B. "Grain")
	 * @return Menge eines Rohstoffs
	 */
	public int getResourceAmount(String resource) {
		if (resource.equals("Grain")) //$NON-NLS-1$
			return grain;
		else if (resource.equals("Wool")) //$NON-NLS-1$
			return wool;
		else if (resource.equals("Lumber")) //$NON-NLS-1$
			return lumber;
		else if (resource.equals("Ore")) //$NON-NLS-1$
			return ore;
		else if (resource.equals("Brick")) //$NON-NLS-1$
			return brick;
		return 0;
	}

	/**
	 * Gibt ein Array mit den abgeworfenen Rohstoffen zur&uuml;ck.
	 * 
	 * @return abgeworfene Rohstoffe (Reihenfolge: grain, wool, lumber, ore,
	 *         brick)
	 */
	public int[] getDiscardedResources() {
		int[] resources = { grain, wool, lumber, ore, brick };
		return resources;
	}

	/**
	 * Setzt die ausgew&auml;hlten abzuwerfenden Rohstoffe zur&uuml;ck
	 */
	public void resetDiscardingResources() {
		grain = 0;
		wool = 0;
		lumber = 0;
		ore = 0;
		brick = 0;
		update();
	}

	/**
	 * Gibt die Anzahl der ausgew&auml;hlten Rohstoffe zur&uuml;ck.
	 * 
	 * @return Anzahl der ausgew&auml;hlten Rohstoffe
	 */
	public int getResAmount() {
		return (grain + wool + lumber + ore + brick);
	}

	/**
	 * Aktualisiert die Anzeige der Rohstoffe.
	 */
	public void update() {
		grainLbl.setText("" + grain); //$NON-NLS-1$
		woolLbl.setText("" + wool); //$NON-NLS-1$
		oreLbl.setText("" + ore); //$NON-NLS-1$
		lumberLbl.setText("" + lumber); //$NON-NLS-1$
		brickLbl.setText("" + brick); //$NON-NLS-1$
	}

	/**
	 * Zeigt die Anzeige der abzuwerfenden Rohstoffe an.
	 */
	public void showDiscard() {
		this.setVisible(true);
		discardPanel.setVisible(true);
	}

	/**
	 * Setzt das komplette <code>ResourcePanel</code> zur&uuml;ck.
	 */
	public void resetAll() {
		resetResources();
		resetDiscardingResources();
		discardPanel.setVisible(false);
	}
}
