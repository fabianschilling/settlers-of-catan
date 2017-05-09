package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Spieler- und Statusanzeige des eigenen Spielers sowie der Gegner.
 * 
 * @author Thomas Wimmer, Fabian Schilling, Elisabeth Friedrich
 * 
 */
@SuppressWarnings("serial")
public class PlayerFrame extends JPanel {

	/**
	 * Eigenes ImagePanel f&uuml;r den Heiligenschein (der anzeigt wer an der Reihe ist)
	 */
	private ImagePanel myTurnPanel;
	/**
	 * Anzeigebild (Avatar) des <code>PlayerFrames</code>.
	 */
	private Image img;
	/**
	 * Breite des <code>PlayerFrames</code>.
	 */
	private int width;
	/**
	 * Houml;he des <code>PlayerFrames</code>.
	 */
	private int height;
	/**
	 * Breite der Avatar-Anzeige.
	 */
	private int panelWidth;
	/**
	 * H&ouml;he der Avatar-Anzeige.
	 */
	private int panelHeight;
	/**
	 * Enth&auml;lt die Avatar-Anzeige.
	 */
	private ImagePanel avatarPanel;
	/**
	 * Vector mit den einzelnen <code>JComponents</code> die zum
	 * <code>PlayerFrame</code> hinzugef&uuml;gt werden k&ouml;nnen.
	 */
	private Vector<JComponent> components = new Vector<JComponent>();
	/**
	 * Anzeige der l&auml;ngsten Handelsstra&szlige.
	 */
	private PlayerLabel lblRoad;
	/**
	 * Anzeige der gr&ouml;&szlig;ten Rittermacht.
	 */
	private PlayerLabel lblArmy;
	/**
	 * Enth&auml;lt die Bestandteile des <code>PlayerFrames</code> auf
	 * verschiedenen Ebenen.
	 */
	private JLayeredPane bgPane;
	
	/**
	 * Breite des Buttons
	 */
	private int buttonWidth;
	/**
	 * H&ouml;he des Buttons
	 */
	private int buttonHeight;
	/**
	 * Breite des Labels
	 */
	private int labelWidth;
	/**
	 * H&ouml;he des Labels
	 */
	private int labelHeight;
	/**
	 * Panel Abstand
	 */
	private int panelDistance;
	/**
	 * Anzeige der Farbe des Spielers
	 */
	private ImagePanel colorPanel;


	/**
	 * Erstellt die Bestandteile des <code>PlayerFrames</code>.
	 */
	public void createWidgets() {

		panelWidth = (int) (width * 0.8);
		panelHeight = (int) (height * 0.8);
		buttonWidth = (int) (width * 0.2);
		buttonHeight = (int) (height * 0.2);
		labelWidth = (int) (width * 0.13);
		labelHeight = (int) (height * 0.13);
		panelDistance = width - panelWidth;

		bgPane = new JLayeredPane();
		bgPane.setPreferredSize(new Dimension(width, height));

		myTurnPanel = new ImagePanel(ImportImages.myturn, panelWidth,
				panelHeight);
		myTurnPanel.setBounds(panelDistance, buttonHeight / 2, panelWidth,
				panelHeight);
		myTurnPanel.setVisible(false);

		avatarPanel = new ImagePanel(img, panelWidth, panelHeight);
		avatarPanel.setBounds(panelDistance, buttonHeight / 2, panelWidth,
				panelHeight);

		lblRoad = new PlayerLabel(ImportImages.roadLbl, labelWidth, labelHeight);
		lblArmy = new PlayerLabel(ImportImages.armyLbl, labelWidth, labelHeight);
		lblRoad.setBounds(width - (int) (panelWidth * 0.3),
				(int) (panelHeight * 0.15), labelWidth, labelHeight);
		lblArmy.setBounds(width - (int) (panelWidth * 0.18),
				(int) (panelHeight * 0.3), labelWidth, labelHeight);

		lblRoad.setVisible(false);
		lblArmy.setVisible(false);

		lblRoad.setToolTipText(Messages.getString("PlayerFrame.LaengsteHandelsstrasse")); //$NON-NLS-1$
		lblArmy.setToolTipText(Messages.getString("PlayerFrame.GroessteRittermacht")); //$NON-NLS-1$

		components.elementAt(0).setBounds(
				panelDistance + (int) (buttonWidth * 0.28), buttonHeight / 2,
				buttonWidth, buttonHeight);
		components.elementAt(1).setBounds(
				panelDistance - (int) (buttonWidth * 0.3),
				(int) (panelHeight * 0.35), buttonWidth, buttonHeight);
		components.elementAt(2).setBounds(
				panelDistance - (int) (buttonWidth * 0.3),
				(int) (panelHeight * 0.62), buttonWidth, buttonHeight);
		components.elementAt(3).setBounds(
				panelDistance + (int) (buttonWidth * 0.3),
				(int) (panelHeight * 0.85), buttonWidth, buttonHeight);

		if (components.size() == 5) {
			components.elementAt(4).setBounds(
					panelDistance + (int) (buttonWidth * 1.3),
					(int) (panelHeight * 0.02), buttonWidth, buttonHeight);
		}
	}

	/**
	 * F&uuml;gt die Bestandteile des <code>PlayerFrames</code> zum
	 * <code>PlayerFrames</code> hinzu.
	 */
	public void addWidgets() {

		for (int i = 0; i < components.size(); i++) {
			bgPane.add(components.elementAt(i), new Integer(2));
		}
		bgPane.add(lblRoad, new Integer(3));
		bgPane.add(lblArmy, new Integer(3));
		bgPane.add(myTurnPanel, new Integer(1));
		bgPane.add(avatarPanel, new Integer(0));

		add(bgPane);
	}

	/**
	 * F&uuml;gt eine <code>JComponent</code> in den <code>Vector</code> hinzu,
	 * der die zus&auml;tzlichen Bestandteile des <code>PlayerFrames</code>
	 * h&auml;lt. (Maximal 5 m&ouml;glich)
	 * 
	 * @param component
	 *            beliebige <code>JComponent</code> die zum
	 *            <code>PlayerFrame</code> hinzugef&uuml;gt werden soll
	 */
	protected void addComponent(JComponent component) {
		if (components.size() != 5) {
			components.add(component);
		}
	}

	/**
	 * F&uuml;gt ein <code>JLabel</code> mit beliebigem Text zum
	 * <code>PlayerFrame</code> hinzu.
	 * 
	 * @param username
	 *            der anzuzeigende Text (Username)
	 */
	public void addNameLbl(String username) {
		int lblWidth = (int) (width * 0.6);
		int lblHeight = (int) (height * 0.3);
		ImagePanel namePanel = new ImagePanel(ImportImages.nameLbl, lblWidth,
				lblHeight);
		namePanel.setBounds((int) (panelWidth * 0.4),
				(int) (panelHeight * 0.85), lblWidth, lblHeight);
		namePanel.addLabel(username);
		namePanel.getLabel()
				.setFont(
						new Font("Times New Roman", Font.BOLD, //$NON-NLS-1$
								(int) (lblHeight * 0.3)));
		namePanel.getLabel().setForeground(Color.WHITE);
		namePanel.getLabel().setVerticalTextPosition(JLabel.TOP);
		namePanel.getLabel().setVerticalAlignment(SwingConstants.BOTTOM);
		bgPane.add(namePanel, new Integer(2));
	}

	public void addNameLbl(String username, Color c) {
		int lblWidth = (int) (width * 0.6);
		int lblHeight = (int) (height * 0.3);
		ImagePanel namePanel = new ImagePanel(ImportImages.nameLbl, lblWidth,
				lblHeight);
		namePanel.setBounds((int) (panelWidth * 0.4),
				(int) (panelHeight * 0.95), lblWidth, lblHeight);
		namePanel.addLabel(username);
		namePanel.getLabel()
				.setFont(
						new Font("Times New Roman", Font.BOLD, //$NON-NLS-1$
								(int) (lblHeight * 0.3)));
		namePanel.getLabel().setForeground(c);
		namePanel.getLabel().setVerticalTextPosition(JLabel.TOP);
		namePanel.getLabel().setVerticalAlignment(SwingConstants.BOTTOM);
		bgPane.add(namePanel, new Integer(2));
	}

	/**
	 * Die Methode setzt einen Gradient &uuml;ber die PlayerFrames, um die Spielerfarbe leichter erkenntlich zu machen
	 * 
	 * @param c ist die gew&uuml;nschte Farbe f&uuml;r den Gradient
	 * 
	 * @author Elisabeth Friedrich, Fabian Schilling
	 */
	public void addColorLbl(Color c) {
		if(c.equals(Color.YELLOW)){
			colorPanel = new ImagePanel(ImportImages.playerYellow, panelWidth, panelHeight);
		}
		else if(c.equals(Color.BLUE)){
			colorPanel = new ImagePanel(ImportImages.playerBlue, panelWidth, panelHeight);
		}
		else if(c.equals(Color.RED)){
			colorPanel = new ImagePanel(ImportImages.playerRed, panelWidth, panelHeight);
		}
		else if(c.equals(Color.GREEN)){
			colorPanel = new ImagePanel(ImportImages.playerGreen, panelWidth, panelHeight);
		}
		colorPanel.setBounds(panelDistance, buttonHeight / 2, panelWidth, panelHeight);
		bgPane.add(colorPanel, new Integer(1));
		colorPanel.setVisible(true);
	}
	
	/**
	 * Zeigt oder versteckt das <code>PlayerLabel</code> f&uuml;r die
	 * gr&ouml;&szlig;te Rittermacht.
	 * 
	 * @param isLargestArmy
	 *            <code>true</code> - zeigt das <code>PlayerLabel</code>
	 */
	public void setLargestArmy(boolean isLargestArmy) {
		lblArmy.setVisible(isLargestArmy);
	}

	/**
	 * Zeigt oder versteckt das <code>PlayerLabel</code> f&uuml;r die
	 * l&auml;ngste Handelsstra&szlig;e.
	 * 
	 * @param isLongestRoad
	 *            <code>true</code> - zeigt das <code>PlayerLabel</code>
	 */
	public void setLongestRoad(boolean isLongestRoad) {
		lblRoad.setVisible(isLongestRoad);
	}

	/**
	 * Setzt das Anzeigebild f&uuml;r den <code>PlayerFrame</code>.
	 * 
	 * @param img
	 *            das anzuzeigende Bild
	 */
	protected void setAvatarImage(Image img) {
		this.img = img;
	}

	/**
	 * Setzt die Breite des <code>PlayerFrames</code>.
	 * 
	 * @param width
	 */
	protected void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Setzt die H&ouml;he des <code>PlayerFrames</code>.
	 * 
	 * @param height
	 */
	protected void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gibt die Breite der zus&auml;tzlichen <code>JComponents</code>
	 * zur&uuml;ck.
	 * 
	 * @return Breite der zus&auml;tzlichen <code>JComponents</code>
	 */
	protected int getComponentWidth() {
		return (int) (width * 0.2);
	}

	/**
	 * Gibt die H&ouml;he der zus&auml;tzlichen <code>JComponents</code>
	 * zur&uuml;ck.
	 * 
	 * @return H&ouml;he der zus&auml;tzlichen <code>JComponents</code>
	 */
	protected int getComponentHeight() {
		return (int) (height * 0.2);
	}

	public void setMyTurn(boolean myturn) {
		myTurnPanel.setVisible(myturn);
	}
}
