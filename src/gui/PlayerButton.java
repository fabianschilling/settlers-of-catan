package gui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
/**
 * <code>JButton</code> mit einem Anzeigebild.
 * 
 * @author Thomas Wimmer, Fabian Schilling
 *
 */
public class PlayerButton extends JButton{
	
	/**
	 * Breite des <code>PlayerButtons</code>.
	 */
	private int width;
	
	/**
	 * H&ouml;he des <code>PlayerButtons</code>.
	 */
	private int height;

	/**
	 * 
	 */
	private boolean active;
	/**
	 * Erzeugt einen <code>PlayerButton</code> mit Hintergrundbild.
	 * 
	 * @param img
	 *            Hintergrundbild
	 * @param width
	 *            Breite des <code>PlayerButtons</code>
	 * @param height
	 *            H&ouml;he des <code>PlayerButtons</code>
	 */
	public PlayerButton(Image img, int width, int height) {
		this.width = width;
		this.height = height;
		this.active = false;
		img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		setPreferredSize(new Dimension(width, height));
		setIcon(new ImageIcon(img));
		setVisible(true);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setDoubleBuffered(true);
	}
	
	/**
	 * &Auml;ndert das Bild eines Buttons zu <code>img</code>
	 * 
	 * @param img ist das neue Bild
	 */
	public void changeIcon(Image img) {
		img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(img));
	}
	
	/**
	 * &Auml;ndert das Bild eines Buttons zu <code>img</code>
	 * Zus&auml;tzlich wird eine Boolean-Variable gesetzt, die geswappt wird, wenn der Button-Status sich &auml;ndert
	 * 
	 * @param img ist das neue Bild
	 */
	public void changeIconActivity(Image img) {
		this.active = !active;
		img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(img));
	}
	
	public boolean isActive(){
		return active;
	}

}
