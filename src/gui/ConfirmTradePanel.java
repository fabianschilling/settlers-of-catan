package gui;

import java.awt.*;
import javax.swing.*;
import model.*;
import controller.Controller;

/**
 * Die Klasse stellt das Frontend f&uuml;r die Annahme der Handelsfunktion zur
 * Verf&uuml;gung
 * 
 * @author Thomas Wimmer, Fabian Schilling
 */

@SuppressWarnings("serial")
public class ConfirmTradePanel extends JPanel {

	/**
	 * Ist das Hintergrundbild
	 */
	private ImagePanel bgpanel;
	
	/**
	 * Ist das Label f&uuml;r die &Uuml;berschrift
	 */
	private JLabel heading;
	
	/**
	 * "Du bekommst"
	 */
	private JLabel receive;
	
	/**
	 * "Du erh&auml;lst"
	 */
	private JLabel giveaway;

	/**
	 * Annehmen-Button
	 */
	private PlayerButton confirm;
	
	/**
	 * Ablehnen-Button
	 */
	private PlayerButton cancel;
	
	/**
	 * Die maximale Anzahl der Rohstoff-Buttons
	 */
	private int maximumButtons;

	/**
	 * Die Icons der angebotenen Rohstoffe als Array
	 */
	private ImagePanel[] offResPanels;
	
	/**
	 * Die Icons der erwarteten Rohstoffe als Array
	 */
	private ImagePanel[] expResPanels;

	/**
	 * Die Gr&ouml;&szlig;e der Buttons
	 */
	private int size;
	
	/**
	 * DieSchriftart
	 */
	private Font font;

	/**
	 * Die jeweilige Anzahl der angebotenen Rostoffe als Array
	 */
	private int[] offR;
	
	/**
	 * Die jeweilige Anzahl der erwarteten Rostoffe als Array
	 */
	private int[] expR;
	
	/**
	 * Die Breite des Panels
	 */
	private int width;
	
	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;
	
	/**
	 * Der gute alte Cntroller
	 */
	private Controller controller;
	
	/**
	 * Der Siedler, der momentan an der Reihe ist
	 */
	private Settler current;
	
	/**
	 * Ein Panel, auf dem die Buttons sind
	 */
	private JPanel buttons;

	/**
	 * Mit diesem Konstruktor wird ein neues <code>ConfirmTradePanel</code> erschaffen
	 * 
	 * @param controller ist der Controller
	 * @param width ist die Breite des Panels
	 * @param height ist die H&ouml;he des Panels
	 * @param offR sind die angebotenen Rohstoffe
	 * @param expR sind die erwarteten Rostoffe
	 */
	public ConfirmTradePanel(Controller controller, int width, int height,
			int[] offR, int[] expR) {
		this.controller = controller;
		this.offR = offR;
		this.expR = expR;
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		this.current = controller.getIsland().getCurrentPlayer();
		font = new Font("Times New Roman", Font.ITALIC, 20); //$NON-NLS-1$

		init();
	}

	/**
	 * Hier wird die GUI erstellt
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	/**
	 * Die Methode erschafft die n&ouml;tigen Objekte f&uuml;r das TradingMenu
	 */
	private void createWidgets() {
		int offNum = 0;
		int expNum = 0;
		for (int i = 0; i < offR.length; i++) {
			if (offR[i] != 0) {
				offNum++;
			}
			if (expR[i] != 0) {
				expNum++;
			}
		}
		maximumButtons = Math.max(offNum, expNum);

		/**
		 * Hier werden die gr&ouml;&szlig;en gesetzt
		 */
		this.size = (int) (width * 0.07);
		/**
		 * Hier wird der Hintergrund (noch: Schriftrolle hinzugef&uuml;gt)
		 */
		bgpanel = new ImagePanel(ImportImages.chatBg, width, height);

		/**
		 * Hierbei handelt es sich um die Labels f&uuml;r die &Uuml;berschrift
		 */
		heading = new JLabel(current.getUsername() + Messages.getString("ConfirmTradePanel.AnfrageHandeln")); //$NON-NLS-1$
		heading.setFont(font);
		receive = new JLabel(Messages.getString("ConfirmTradePanel.DuBekommst")); //$NON-NLS-1$
		receive.setFont(font);
		giveaway = new JLabel(Messages.getString("ConfirmTradePanel.DuGibstAb")); //$NON-NLS-1$
		giveaway.setFont(font);

		/**
		 * Im Folgenden werden die zugeh&ouml;rigen Bilder reingeladen und in
		 * PlayerLabels gespeichert
		 */
		offResPanels = new ImagePanel[offNum];
		expResPanels = new ImagePanel[expNum];

		int c = 0;
		for (int i = 0; i < offR.length; i++) {
			if (offR[i] != 0) {
				switch (i) {
				case 0:
					offResPanels[c] = new ImagePanel(ImportImages.woolBtn,
							size, size);
					break;
				case 1:
					offResPanels[c] = new ImagePanel(ImportImages.oreBtn, size,
							size);
					break;
				case 2:
					offResPanels[c] = new ImagePanel(ImportImages.brickBtn,
							size, size);
					break;
				case 3:
					offResPanels[c] = new ImagePanel(ImportImages.lumberBtn,
							size, size);
					break;
				case 4:
					offResPanels[c] = new ImagePanel(ImportImages.grainBtn,
							size, size);
					break;
				}
				offResPanels[c].addLabel("" + offR[i]); //$NON-NLS-1$
				offResPanels[c].getLabel().setFont(
						new Font("Times New Roman", Font.BOLD, 26)); //$NON-NLS-1$
				c++;
			}
		}

		c = 0;
		for (int i = 0; i < expR.length; i++) {
			if (expR[i] != 0) {
				switch (i) {
				case 0:
					expResPanels[c] = new ImagePanel(ImportImages.woolBtn,
							size, size);
					break;
				case 1:
					expResPanels[c] = new ImagePanel(ImportImages.oreBtn, size,
							size);
					break;
				case 2:
					expResPanels[c] = new ImagePanel(ImportImages.brickBtn,
							size, size);
					break;
				case 3:
					expResPanels[c] = new ImagePanel(ImportImages.lumberBtn,
							size, size);
					break;
				case 4:
					expResPanels[c] = new ImagePanel(ImportImages.grainBtn,
							size, size);
					break;
				}
				expResPanels[c].addLabel("" + expR[i]); //$NON-NLS-1$
				expResPanels[c].getLabel().setFont(
						new Font("Times New Roman", Font.BOLD, 26)); //$NON-NLS-1$
				c++;
			}
		}

		/**
		 * Cancel und Confirm Buttons
		 */

		buttons = new JPanel();
		buttons.setOpaque(false);
		confirm = new PlayerButton(ImportImages.confirmBtn, size, size);
		cancel = new PlayerButton(ImportImages.cancelBtn, size, size);
	}

	/**
	 * Die Methode setzt die Action-, Change- und MouseListener f&uuml;r die
	 * Interaktion mit dem TradingMenu Der TradeButton wird vom Controller
	 * abgefangen, die anderen internen Elemente werden intern verarbeitet
	 */
	public void setupInteraction() {
		confirm.addActionListener(controller);
		confirm.setActionCommand("conf.confirm"); //$NON-NLS-1$
		cancel.addActionListener(controller);
		cancel.setActionCommand("conf.cancel"); //$NON-NLS-1$
	}

	/**
	 * Im Folgenden werden die Elemente passend auf dem TradingMenu angeornet
	 */
	public void addWidgets() {

		add(bgpanel);
		bgpanel.setLayout(new GridBagLayout());

		buttons.setLayout(new FlowLayout());
		buttons.add(confirm);
		buttons.add(cancel);

		/**
		 * Erste Zeile des Grids
		 */
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 20, 0);
		bgpanel.add(heading, c);

		/**
		 * Zweite Zeile des Grids
		 */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 10, 0);
		bgpanel.add(receive, c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 10, 0);
		bgpanel.add(giveaway, c);

		/**
		 * linke Spalte (offResources)
		 */
		for (int i = 0; i < offResPanels.length; i++) {
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = i + 2;
			c.anchor = GridBagConstraints.CENTER;
			bgpanel.add(offResPanels[i], c);
		}

		/**
		 * rechte Spalte (expRes)
		 */
		for (int j = 0; j < expResPanels.length; j++) {
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = j + 2;
			c.anchor = GridBagConstraints.CENTER;
			bgpanel.add(expResPanels[j], c);
		}

		/**
		 * buttons
		 */
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = maximumButtons + 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 0, 0, 0);
		bgpanel.add(buttons, c);

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getOffR() {
		return offR;
	}

	public int[] getExpR() {
		return expR;
	}
}
