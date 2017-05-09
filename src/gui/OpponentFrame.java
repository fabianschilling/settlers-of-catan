package gui;

import java.awt.*;
import model.Settler;

/**
 * Anzeige des Gegenspielers.
 * 
 * @author Thomas Wimmer, Fabian Schilling
 * 
 */
@SuppressWarnings("serial")
public class OpponentFrame extends PlayerFrame {

	/**
	 * Anzeige f&uuml;r die Anzahl der gespielten Ritterkarten.
	 */
	private ImagePanel armyPanel;
	/**
	 * Anzeige f&uuml;r die l&auml;nge der l&auml;ngsten Handelsstra&szlig;e
	 */
	private ImagePanel roadsPanel;
	/**
	 * Anzeige f&uuml;r die Anzahl der Entwicklungskarten auf der Hand.
	 */
	private ImagePanel cardsPanel;
	/**
	 * Anzeige f&uuml;r die Anzahl der Rohstoffkarten auf der Hand.
	 */
	private ImagePanel resourcesPanel;
	/**
	 * Anzeige f&uuml;r die Siegpunkte.
	 */
	private ImagePanel victoryPointsPanel;
	/**
	 * Der <code>Settler</code> f&uuml;r das <code>OpponentFrame</code>.
	 */
	private Settler opponent;

	/**
	 * Erzeugt eine Gui-Repr&auml;sentation des Gegenspielers
	 * 
	 * @param img
	 *            Bild des Opponents
	 * @param width
	 *            Breite
	 * @param height
	 *            H&ouml;he
	 */
	public OpponentFrame(Image img, int width, int height) {
		setWidth(width);
		setHeight(height);
		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		setAvatarImage(img);
		init();
	}

	/**
	 * Initialisiert das <code>OpponentFrame</code>
	 */
	public void init() {
		createLabels();
		addLabels();
		createWidgets();
		addWidgets();
	}

	/**
	 * Erstellt die <code>Labels</code> f&uuml;r die Z&auml;hler auf den
	 * einzelnen Anzeige-Icons.
	 */
	public void createLabels() {
		int labelWidth = getComponentWidth();
		int labelHeight = getComponentHeight();
		armyPanel = new ImagePanel(ImportImages.armyLbl, labelWidth,
				labelHeight);
		roadsPanel = new ImagePanel(ImportImages.roadLbl, labelWidth,
				labelHeight);
		cardsPanel = new ImagePanel(ImportImages.cardBtn, labelWidth,
				labelHeight);
		resourcesPanel = new ImagePanel(ImportImages.resourceLbl, labelWidth,
				labelHeight);
		victoryPointsPanel = new ImagePanel(ImportImages.victoryPointBtn,
				labelWidth, labelHeight);
		armyPanel.setToolTipText(Messages.getString("OpponentFrame.Ritter")); //$NON-NLS-1$
		roadsPanel.setToolTipText(Messages.getString("OpponentFrame.LaengsteHandelsstrasse")); //$NON-NLS-1$
		cardsPanel.setToolTipText(Messages.getString("OpponentFrame.Entwicklungskarten")); //$NON-NLS-1$
		resourcesPanel.setToolTipText(Messages.getString("OpponentFrame.Rohstoffe")); //$NON-NLS-1$
		victoryPointsPanel.setToolTipText(Messages.getString("OpponentFrame.Siegespunkte")); //$NON-NLS-1$
	}

	/**
	 * F&uuml;gt die <code>Labels</code> f&uuml;r die Z&auml;hler auf den
	 * einzelnen Anzeige-Icons hinzu.
	 */
	public void addLabels() {
		addComponent(armyPanel);
		addComponent(roadsPanel);
		addComponent(cardsPanel);
		addComponent(resourcesPanel);
		addComponent(victoryPointsPanel);
	}

	/**
	 * Die Methode macht es m&ouml;glich den Opponent (Settler) aus dem Model
	 * als Grundlage f&uuml;r die GUI herzunehmen.
	 * 
	 * @param opponent
	 *            gegnerischer <code>Settler</code>
	 */
	public void setOpponent(Settler opponent) {
		this.opponent = opponent;
		armyPanel.addLabel("" + this.opponent.getArmyCount()); //$NON-NLS-1$
		roadsPanel.addLabel("" + this.opponent.getLongestRoadLength()); //$NON-NLS-1$
		cardsPanel.addLabel("" + this.opponent.getAmountOfDevCards()); //$NON-NLS-1$
		resourcesPanel.addLabel("" + this.opponent.getAmountOfResources()); //$NON-NLS-1$
		victoryPointsPanel.addLabel("" + this.opponent.getScore()); //$NON-NLS-1$
		addNameLbl(this.opponent.getUsername(), opponent.getColor());
		addColorLbl(opponent.getColor());
	}

	/**
	 * Aktualisiert die Anzeigewerte.
	 */
	public void update() {
		armyPanel.setText("" + opponent.getArmyCount()); //$NON-NLS-1$
		roadsPanel.setText("" + opponent.getLongestRoadLength()); //$NON-NLS-1$
		cardsPanel.setText("" + opponent.getAmountOfDevCards()); //$NON-NLS-1$
		resourcesPanel.setText("" + opponent.getAmountOfResources()); //$NON-NLS-1$
		victoryPointsPanel.setText("" + opponent.getTempScore()); //$NON-NLS-1$
	}

	/**
	 * Gibt den gegenerischen Settler zur&uuml;ck, dem dieses GUI-Element zu
	 * Grunde liegt.
	 * 
	 * @return einen gegnerischen <code>Settler</code>
	 */
	public Settler getOpponent() {
		return opponent;
	}
}
