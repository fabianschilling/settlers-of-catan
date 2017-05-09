package gui;

import java.awt.*;
import javax.swing.*;

/**
 * Die Klasse ermoeglicht das einfache und unkomplizierte Erzeugen eines Labels
 * mit Hintergund. Hinzu ist die Groesse frei skalierbar
 * 
 * @author Thomas Wimmer, Fabian Schilling
 */

@SuppressWarnings("serial")
public class PlayerLabel extends JLabel {

	/**
	 * Breite des Labels
	 */
	private int width;

	/**
	 * H&ouml;he des Labels
	 */
	private int height;

	/**
	 * Konstruktor ermoeglicht das einfache Erzeugen eines <code>Labels</code>
	 * mit Hintergrundbild.
	 * 
	 * @param img
	 *            ist das Hintergrundbild
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die Breite des Panels
	 */
	public PlayerLabel(Image img, int width, int height) {
		this.width = width;
		this.height = height;
		img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		setPreferredSize(new Dimension(width, height));
		setIcon(new ImageIcon(img));
		setVisible(true);
		setDoubleBuffered(true);
	}

	/**
	 * Die Methode setzt das Hintergrundbild des Labels neu
	 * 
	 * @param newImg
	 */
	public void setNewIcon(Image newImg) {
		newImg = newImg.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(newImg));
	}
}