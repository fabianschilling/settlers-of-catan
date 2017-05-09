package gui;

import java.awt.*;
import javax.swing.*;
import controller.Controller;

/**
 * Die Klasse ist das Panel, das angezeigt wird, wenn man auf einen Knoten
 * klickt (=Siedlung bauen)
 * 
 * @author Thomas Wimmer, Fabian Schilling, Christian Hemauer
 * 
 */

@SuppressWarnings("serial")
public class BuildPanelCity extends JPanel {

	/**
	 * Die Breite des Panels
	 */
	private int width;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Der gute alte Coontroller
	 */
	private Controller controller;

	/**
	 * Ein Button f&uuml;r eine Siedlung
	 */
	private PlayerButton bSettlement;

	/**
	 * Erzeugt ein neues Objekt des "Bau-Menus"
	 * 
	 * @param controller
	 *            ist der gute alte Controller
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public BuildPanelCity(Controller controller, int width, int height) {
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
		bSettlement = new PlayerButton(ImportImages.cityBtn, buttonWidth,
				buttonHeight);
	}

	private void setupInteraction() {
		bSettlement.setActionCommand("node.city"); //$NON-NLS-1$
		bSettlement.addActionListener(controller);
	}

	private void addWidgets() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(bSettlement, gbc);

	}

	public PlayerButton getbSettlement() {
		return bSettlement;
	}

	public void setbSettlement(PlayerButton bSettlement) {
		this.bSettlement = bSettlement;
	}
	public void changeBSettlement(){
		int buttonWidth = (int) (width * 0.3);
		int buttonHeight = (int) (height * 0.3);
		bSettlement = new PlayerButton(ImportImages.cityBtn, buttonWidth,
				buttonHeight);
	}
	public void resetBSettlement(){
		int buttonWidth = (int) (width * 0.3);
		int buttonHeight = (int) (height * 0.3);
		bSettlement = new PlayerButton(ImportImages.settlementBtn, buttonWidth,
				buttonHeight);
	}
}
