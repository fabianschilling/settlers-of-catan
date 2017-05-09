package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Die Klasse erzeugt ein einfaches <code>JPanel</code> mit Hintergrund
 * 
 * @author Fabian Schilling, Thomas Wimmer
 */

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	/**
	 * Breite des <code>ImagePanels</code>
	 */
	private int width;
	/**
	 * H&ouml;he des <code>ImagePanels</code>
	 */
	private int height;
	/**
	 * Anzuzeigendes Bild
	 */
	private Image img;
	/**
	 * Optionales Label f&uuml;r m&ouml;glichen Text
	 */
	private JLabel label;

	/**
	 * Der Font der Zahlen
	 */
	private Font font;

	/**
	 * Konstruktor f&uuml;r das <code>ImagePanel</code>.
	 * 
	 * @param img
	 *            Hintergundbild
	 * @param width
	 *            Breite des <code>ImagePanels</code>
	 * @param height
	 *            H&ouml;he des <code>ImagePanels</code>
	 */
	public ImagePanel(Image img, int width, int height) {
		this.img = img.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		this.width = width;
		this.height = height;
		this.font = new Font("Times New Roman", Font.BOLD, (int) (width * 0.7)); //$NON-NLS-1$
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setOpaque(false);
		setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this);
	}

	/**
	 * F&uuml;gt einen Label mit Text zum <code>ImagePanel</code> hinzu.
	 * 
	 * @param text
	 *            anzuzeigender Text
	 */
	public void addLabel(String text) {
		label = new JLabel();
		label.setBackground(Color.YELLOW);
		label.setPreferredSize(new Dimension((int) (width * 0.9),
				(int) (height * 0.6)));
		label.setText(text);
		label.setFont(font);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.CENTER);
		add(label);
	}

	/**
	 * F&uuml;gt einen Label mit Text zum <code>ImagePanel</code> hinzu.
	 * 
	 * @param text
	 *            anzuzeigender Text
	 * @param c
	 *            Farbe des Texts
	 */
	public void addLabel(String text, Color c) {
		Font font = new Font("Times New Roman", Font.BOLD, //$NON-NLS-1$
				(int) (height * 0.65));
		label = new JLabel();
		label.setPreferredSize(new Dimension((int) (width * 0.7),
				(int) (height * 0.7)));
		label.setText(text);
		label.setFont(font);
		label.setForeground(c);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.TOP);
		add(label);
	}

	public void setText(String text) {
		label.setText(text);
	}

	public JLabel getLabel() {
		return label;
	}
}
