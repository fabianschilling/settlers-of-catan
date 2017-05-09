package gui;

import java.awt.*;

import javax.swing.*;

import controller.Controller;

/**
 * GUI-Element das die Augenzahlen der W&uuml;rfel anzeigt
 * 
 * @author Fabian Schilling, Thomas Wimmer
 */

@SuppressWarnings("serial")
public class DicePanel extends JPanel {

	/**
	 * Der erste W&uuml;rfel
	 */
	private PlayerLabel diceOne;
	/**
	 * Der zweite W&uuml;rfel
	 */
	private PlayerLabel diceTwo;
	/**
	 * Controller
	 */
	private Controller controller;
	/**
	 * Breite
	 */
	private int width;

	/**
	 * Konstruktor f&uuml;r das <code>DicePanel</code>.
	 * 
	 * @param width
	 *            Breite des <code>DicePanels</code>
	 * @param height
	 *            H&ouml;he des <code>DicePanels</code>
	 */
	public DicePanel(int width, int height) {
		this.width = width;

		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));

	}

	/**
	 * Setzt die W&uuml;rfel-Anzeige auf die entsprechenden Augenzahlen.
	 * 
	 * @param dice1
	 *            Augen des ersten W&uuml;rfels
	 * @param dice2
	 *            Augen des zweiten W&uuml;rfels
	 */
	public void setPips(int dice1, int dice2) {
		if (!(diceOne == null) || !(diceTwo == null)) {
			remove(diceOne);
			remove(diceTwo);
		}

		diceOne = new PlayerLabel(new ImageIcon(getClass().getResource("graphics/dice/dice" + dice1 //$NON-NLS-1$
				+ ".png")).getImage(), width / 3, width / 3); //$NON-NLS-1$
		diceTwo = new PlayerLabel(new ImageIcon(getClass().getResource("graphics/dice/dice" + dice2 //$NON-NLS-1$
				+ ".png")).getImage(), width / 3, width / 3); //$NON-NLS-1$
		addMouseListener(controller);
		setLayout(new FlowLayout());
		add(diceOne);
		add(diceTwo);
	}
}
