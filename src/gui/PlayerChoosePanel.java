package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import model.Settler;
import controller.Controller;

/**
 * Anzeige von einzelnen Spielern.
 * 
 * @author Thomas Wimmer, Fabian Schilling
 * 
 */
@SuppressWarnings("serial")
public class PlayerChoosePanel extends JPanel {

	/**
	 * Panel in dem die Nachricht angezeigt wird.
	 */
	private JPanel upperPanel;

	/**
	 * Hintergrundbild
	 */
	private JPanel choosePanel;

	/**
	 * Der gute alte Controller
	 */
	private Controller controller;

	/**
	 * Die Breite des Panels
	 */
	private int width;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Die Buttons, die die Gegner Darstellen sollen als Array
	 */
	private PlayerButton[] opponentBtns;

	/**
	 * Die Gegner aus dem Model als Array
	 */
	private Settler[] opponents;

	/**
	 * Die InhaltsPanels f&uuml;r jeden Spieler
	 */
	private JPanel[] contentPanels;

	/**
	 * LayeredPanes f&uuml;r die Gegenspieler als Array (zur Anordnung)
	 */
	private JLayeredPane[] layeredPanes;

	/**
	 * Die Usernames der Gegenspieler in Array-Form
	 */
	private ImagePanel[] namePanels;

	/**
	 * Die Identifikationsnummern der Spieler
	 */
	@SuppressWarnings("unused")
	private ArrayList<Integer> playerIds;

	/**
	 * Gibt an, ob es sich um einen Handel handelt (je nachdem sieht das
	 * Men&uuml; anders aus.
	 */
	private boolean trading;

	/**
	 * Die Gr&ouml;&szlig;e des Arrays bzw. die Anzahl der Spieler
	 */
	private int arraySize;

	/**
	 * Ein Button zum Abbrechen
	 */
	private PlayerButton cancelBtn;

	/**
	 * Konstruktor f&uuml;r das <code>PlayerChoosePanel</code>.
	 * 
	 * @param controller
	 *            Controller f&uuml;r die Aktionselemente
	 * @param playerIds
	 *            <code>ArrayList</code> f&uuml;r die Spieler-IDs die angezeigt
	 *            werden sollen
	 */
	// public PlayerChoosePanel(Controller controller, int width, int height,
	// ArrayList<Integer> playerIds, boolean trading) {
	public PlayerChoosePanel(Controller controller,
			ArrayList<Integer> playerIds, boolean trading) {
		this.controller = controller;
		this.playerIds = playerIds;
		this.trading = trading;
		arraySize = (trading) ? playerIds.size() + 1 : playerIds.size();
		height = (int) (controller.getMainGUI().getHeight() * 0.25);
		width = height * arraySize;
		// this.width = width;
		// this.height = height;
		opponents = new Settler[playerIds.size()];
		opponentBtns = new PlayerButton[playerIds.size()];
		contentPanels = new JPanel[arraySize];
		layeredPanes = new JLayeredPane[arraySize];
		namePanels = new ImagePanel[playerIds.size()];

		for (int i = 0; i < opponents.length; i++) {
			opponents[i] = controller.getIsland().getSettler(playerIds.get(i));
		}

		init();

		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		setVisible(true);
	}

	/**
	 * Initialisiert das <code>PlayerChoosePanel</code>.
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	/**
	 * Erstellt die Bestandteile des <code>PlayerChoosePanels</code>.
	 */
	public void createWidgets() {

		upperPanel = new JPanel();
		upperPanel.setPreferredSize(new Dimension(width, height));
		choosePanel = new JPanel();
		choosePanel.setPreferredSize(new Dimension(width + width/10, height + height/10));
		choosePanel.setOpaque(false);

		int btnSize = (int) (height * 0.8);
		int lblHeight = (int) (btnSize * 0.4);
		int panelHeight = (int) (btnSize * 1.1);
		for (int i = 0; i < opponents.length; i++) {
			contentPanels[i] = new JPanel();
			contentPanels[i].setOpaque(false);
			contentPanels[i].setVisible(true);
			contentPanels[i].setPreferredSize(new Dimension(btnSize,
					panelHeight));

			layeredPanes[i] = new JLayeredPane();
			layeredPanes[i].setOpaque(false);
			layeredPanes[i]
					.setPreferredSize(new Dimension(btnSize, panelHeight));

			opponentBtns[i] = new PlayerButton(ImportImages.kerriganAvatar,
					btnSize, btnSize);
			opponentBtns[i].setBounds(0, 0, btnSize, btnSize);

			namePanels[i] = new ImagePanel(ImportImages.nameLbl, btnSize,
					lblHeight);
			namePanels[i].setBounds(0, (int) (btnSize * 0.7), btnSize,
					lblHeight);
			namePanels[i].addLabel(opponents[i].getUsername(),
					opponents[i].getColor());
			namePanels[i].getLabel().setFont(
					new Font("Times New Roman", Font.BOLD, //$NON-NLS-1$
							(int) (lblHeight * 0.38)));
			namePanels[i].getLabel().setVerticalTextPosition(JLabel.CENTER);
			namePanels[i].getLabel()
					.setVerticalAlignment(SwingConstants.BOTTOM);
		}

		if (trading) {
			cancelBtn = new PlayerButton(ImportImages.cancelBtn, btnSize,
					btnSize);
			cancelBtn.setBounds(0, 0, btnSize, btnSize);
			layeredPanes[arraySize - 1] = new JLayeredPane();
			layeredPanes[arraySize - 1].setOpaque(false);
			layeredPanes[arraySize - 1].setVisible(true);
			layeredPanes[arraySize - 1].setPreferredSize(new Dimension(btnSize,
					panelHeight));
			contentPanels[arraySize - 1] = new JPanel();
			contentPanels[arraySize - 1].setOpaque(false);
			contentPanels[arraySize - 1].setVisible(true);
			contentPanels[arraySize - 1].setPreferredSize(new Dimension(
					btnSize, panelHeight));
		}

	}

	/**
	 * F&uuml;gt die Interaktion mit dem <code>Controller</code> hinzu.
	 */
	public void setupInteraction() {
		if (trading) {
			for (int i = 0; i < opponents.length; i++) {
				opponentBtns[i].addActionListener(controller);
				opponentBtns[i]
						.setActionCommand("chos." + opponents[i].getID()); //$NON-NLS-1$
			}
			cancelBtn.addActionListener(controller);
			cancelBtn.setActionCommand("chos.-1"); //$NON-NLS-1$
		} else {
			for (int i = 0; i < opponents.length; i++) {
				opponentBtns[i].addActionListener(controller);
				opponentBtns[i]
						.setActionCommand("robb." + opponents[i].getID()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * F&uuml;gt die Bestandteile dem <code>PlayerChoosePanel</code> hinzu.
	 */
	public void addWidgets() {
		add(choosePanel);
		choosePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		for (int i = 0; i < opponentBtns.length; i++) {
			layeredPanes[i].add(opponentBtns[i], new Integer(1));
			layeredPanes[i].add(namePanels[i], new Integer(2));
			contentPanels[i].setLayout(new BorderLayout());
			contentPanels[i].add(layeredPanes[i], BorderLayout.CENTER);
			choosePanel.add(contentPanels[i]);
		}

		if (trading) {
			layeredPanes[arraySize - 1].add(cancelBtn, new Integer(2));
			contentPanels[arraySize - 1].add(layeredPanes[arraySize - 1],
					BorderLayout.CENTER);
			choosePanel.add(contentPanels[arraySize - 1]);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
