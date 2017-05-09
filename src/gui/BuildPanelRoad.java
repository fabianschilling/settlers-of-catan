package gui;

import java.awt.*;

import javax.swing.*;

import controller.Controller;

/**
 * Die Klasse ist das "Menu", das aufgerufen wird, wenn man eine Stra§e bauen
 * will (Bau-Menu)
 * 
 * @author Fabian Schilling, Thomas Wimmer, Christian Hemauer
 * 
 */

@SuppressWarnings("serial")
public class BuildPanelRoad extends JPanel {

	/**
	 * Die Breite des Panels
	 */
	private int width;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Der gute alte Controller
	 */
	private Controller controller;

	/**
	 * Ein Button f&uuml;r eine Stra&szlig;e
	 */
	private PlayerButton bRoad;

	/**
	 * Erzeugt ein neues Stra&szlig;en-Bau-Menu
	 * 
	 * @param controller
	 *            ist der gute alte Controller
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public BuildPanelRoad(Controller controller, int width, int height) {
		this.controller = controller;
		this.width = width;
		this.height = height;

		createWidgets();
		setupInteraction();
		addWidgets();

		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));
	}

	private void createWidgets() {
		int buttonWidth = (int) (width * 0.3);
		int buttonHeight = (int) (height * 0.3);
		bRoad = new PlayerButton(ImportImages.roadBtn, buttonWidth,
				buttonHeight);
	}

	private void setupInteraction() {
		bRoad.setActionCommand("road.road"); //$NON-NLS-1$
		bRoad.addActionListener(controller);
	}

	private void addWidgets() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(bRoad, gbc);
	}
	
	public void changeIcon(Image img) {
		bRoad.changeIcon(img);
	}
}
