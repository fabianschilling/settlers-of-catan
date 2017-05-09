package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import model.IslandOfCatan;
import controller.Controller;

/**
 * Enth&auml;lt alle visuellen Bestandteile des Spiels.
 * 
 * @author Thomas Wimmer, Fabian Schilling, Florian Weiss, Christian Hemauer
 * 
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame{

	/**
	 * Boolean-Variable, die ansagt, ob die GUI schon gestartet wurde
	 */
	private static boolean isStarted;

	/**
	 * Das Spielermenu - Objekt der Klasse <code>SettlerFrame</code>.
	 */
	private SettlerFrame playerFrame;

	/**
	 * Der Chat - Objekt der Klasse <code>ChatGUI</code> .
	 */
	private ChatGUI chatFrame;

	/**
	 * Die Anzeigen der gegenerischen Spieler - Objekte der Klasse
	 * <code>OpponentFrame</code> als Array.
	 */
	private OpponentFrame[] opponentFrames;

	/**
	 * Panel, das die <code>OpponentFrame</code>s enth&aulm;lt - Objekt der
	 * Klasse <code>JPanel</code>
	 */
	private JPanel opponentsPanel;

	/**
	 * Die Insel mit allem was dazu geh&ouml;rt - Objekt der Klasse
	 * <code>PolygonMap</code>
	 */
	private PolygonMap polygonMap;

	/**
	 * Baukostenmen&uuml; mit den Kosten f&uuml;r Geb&auml;ude - Objekt der
	 * Klasse <code>PlayerButton</code>
	 */
	private PlayerButton buildingcosts;

	/**
	 * Die Breite des Panels.
	 */
	private int width;

	/**
	 * Die H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Die Gr&ouml;&szlig;e des Bildschirms - Objekt der Klasse
	 * <code>Dimensions</code>
	 */
	private Dimension screenSize;

	/**
	 * Die Insel aus dem Model - Objekt der Klasse <code>IslandOfCatan</code>
	 */
	private IslandOfCatan island;

	/**
	 * X-Koordinate des Punktes, an dem das erste Hexagon gezeichnet wird.
	 */
	@SuppressWarnings("unused")
	private int x;

	/**
	 * Y-Koordinate des Punktes, an dem das erste Hexagon gezeichnet wird.
	 */
	@SuppressWarnings("unused")
	private int y;
	/**
	 * Die halbe Breite eines Hexagons.
	 */
	private int radius;

	/**
	 * Der gute alte Controller
	 */
	private Controller controller;

	/**
	 * Besonderes Panel, auf dem man die GUI-Elemente bequemer und
	 * &uuml;bersichtlicher anordnen kann.
	 */
	private JLayeredPane layeredPane;

	/**
	 * GUI-Element, das es den Handelspartners (=Gegenspielern) m&ouml;glich
	 * macht, einen Handel anzunehmen oder abzulehnen.
	 */
	private ConfirmTradePanel confirmTradePanel;

	/**
	 * Wird bspw. f&uuml;r die Auswahl der Rohstoffe nach einem R&auml;uberklau
	 * verwendet
	 */
	private ResourcePanel resourcePanel;

	/**
	 * Hauptpanel, zu dem alle GUI-Elemente hinzugef&uuml;gt werden
	 */
	private JPanel contentPanel;
	
	/**
	 * Gibt die x-Koordinate der <code>PolygonMap</code> an
	 */
	private int polygonMapPos;

	/**
	 * Panel, das angezeigt wird, wenn man eine Stra&szlige;e bauen will
	 */
	private BuildPanelRoad buildPanelRoad;

	/**
	 * Ist das Men&uumL;, das den Handel mit der Bank, mit den H&auuml;fen
	 * m&ouml;glich macht
	 */
	private TradingMenu tradePanel;

	/**
	 * Panel, auf dem die gew&uuml;rfelten Pips angezeigt werden (im
	 * PlayerFrame)
	 */
	private DicePanel rollDicePanel;

	/**
	 * Men&uuml;, das es dem Spieler erm&ouml;glicht Karten zu kaufen und
	 * auszuspielen.
	 */
	private CardMenu cardMenu;

	/**
	 * Hier werden die vorhandenen Rohstoffe, Ritterkarten, Siegpunkte und
	 * weitere Informatiionen &uuml;ber den eigenen Spieler angezeigt
	 */
	private SupplyPanel supplyPanel;

	/**
	 * Das <code>BuildingCostMenu</code>, das die ben&ouml;tigten Rohstoffe
	 * anzeigt
	 */
	private BuildingCostsMenu buildingCostsMenuPanel;

	/**
	 * Panel, das aufgerufen wird, wenn man sich aussuchen will, von wem man
	 * Rohstoffe klauen m&ouml;chte
	 */
	private PlayerChoosePanel robberPanel;

	/**
	 * Panel, das aufgerufen wird, wenn man sich aussuchen will, mit welchem
	 * Spieler man den Handel vollziehen willl
	 */
	private TradeChoosePanel tradeePanel;

	/**
	 * Grafik-Objekt
	 */
	private Graphics g;

	/**
	 * Button, um die Musik zu starten
	 */
	private PlayerButton playMusic;
	
	/**
	 * Button, um die Musik zu muten
	 */
	private PlayerButton muteMusic;

	/**
	 * Panel, zu dem die Musik-Buttons geaddet werden
	 */
	private JPanel musicButtons;

	/**
	 * Soll den momentanen Status (playing, not playing) anzeigen
	 */
	private JLabel musicStatus;

	/**
	 * Panel, das die Musik-Elemente enth&auml;lt
	 */
	private JPanel musicContent;
	
	/**
	 * Klasse um die Bilder zu importieren
	 */
	private ImportImages importImages;
	
	/**
	 * Klasse um die Bilder des Servers zu importieren
	 */
	private ImportServerImages importServerImages;
	
	/**
	 * Das Bild des Spielers
	 */
	private static int playerImage;
	
	/**
	 * Button f&uuml;r den Siedlungs- und St&auml;dtebau
	 */
	private PlayerButton buildButton;
	
	/**
	 * Konstruktor der MainGUI.
	 * @param controller Controller
	 */
	public MainGUI(Controller controller) {
		this.island = controller.getIsland();
		this.controller = controller;
		init();
		g = super.getGraphics();
	}

	/**
	 * Initialisiert die <code>MainGUI</code>.
	 */
	public void init() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("Die Siedler von Catan - by H16 - All Rights Reversed"); //$NON-NLS-1$
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenSize.getWidth());
		height = (int) (screenSize.getHeight());
		
		if(width > 2000) {
			width /= 2;
		}
		
		double value = (double) width / (double) height;
		polygonMapPos = 0;
		if(value < 1.5) {
			height = (int) (width / 1.77);
			if(screenSize.getWidth() > 2000)
				polygonMapPos = - (int) (width * 0.9 / 5.5);
		}
		
		width *= 0.9;
		height *= 0.9;

		screenSize = new Dimension(width, height);

		this.setPreferredSize(screenSize);
		this.setMinimumSize(screenSize);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				int n = JOptionPane.showConfirmDialog(
					    getThis(),
					    "Willst Du das Spiel wirklich beenden?\n",
					    "Spiel Beenden",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE);
				if(n==0){
					System.exit(0);
				}
			}
		});

		importImages = new ImportImages();
		importImages.loadPics();
		
		importServerImages = new ImportServerImages();
		importServerImages.loadServerPics();
		
		playerImage = controller.getClient().getSettler().getAvatarNumber();

		createWidgets();
		setupInteraction();
		addWidgets();
	}

	public void drawImage() {
		super.setVisible(true);
	}

	public void paintComponent(Graphics g, int centerX, int centerY) {
		Image img = ImportImages.testvillage;
		g.drawImage(img, centerX, centerY, null); // see javadoc for more info
													// on the parameters

	}

	public void paintComponent(Graphics g, Polygon polygon) {
		g = super.getGraphics();
		Graphics2D graphic = (Graphics2D) g;
		graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphic.setColor(new Color(139, 69, 19));
		graphic.draw(polygon);
		graphic.fill(polygon);
	}

	/**
	 * Erstellt die Komponenten der <code>MainGUI</code>
	 */
	private void createWidgets() {

		int opponentsFrameWidth = (int) (width * 0.12);
		@SuppressWarnings("unused")
		int opponentsFrameHeight = (int) (height * 0.15);

		int chatFrameWidth = (int) (width * 0.28);
		int chatFrameHeight = (int) (height * 0.50);

		@SuppressWarnings("unused")
		int menuFrameWidth = (int) (width * (3.0 / 8.0));
		int menuFrameHeight = (int) (height * (1.5 / 8.0));

		radius = height / 11;

		contentPanel = new JPanel();
		opponentsPanel = new JPanel();
		opponentsPanel.setOpaque(false);
		
			playerFrame = new SettlerFrame(controller,
				ImportImages.avatarArray[playerImage], menuFrameHeight * 2,
				menuFrameHeight * 2);
			
		chatFrame = new ChatGUI(controller, chatFrameWidth, chatFrameHeight);
		polygonMap = new PolygonMap(island,  polygonMapPos, 0, radius);

		contentPanel.setOpaque(false);

		opponentFrames = new OpponentFrame[island.getSettlers().length - 1];
		int c = 0;

		for (int i = 0; i < island.getSettlers().length; i++) {

			if (controller.getClient().getID() == i) {
				playerFrame.addNameLbl(controller.getClient().getUsername(),
						island.getSettler(i).getColor());
				playerFrame.addColorLbl(island.getSettler(i).getColor());
				playerFrame.repaint();
			} else {
				opponentFrames[c] = new OpponentFrame(
						ImportImages.avatarArray[island.getSettler(i).getAvatarNumber()], opponentsFrameWidth,
						opponentsFrameWidth);
				opponentFrames[c].setOpponent(island.getSettlers()[i]);
				c++;
			}
		}
		
		buildButton = new PlayerButton(ImportImages.settlementBtn,(int) ((double)width / 10.0 *0.3),(int) ((double)width / 10.0 *0.3));
		buildButton.setBounds(150, 150, width / 10, width / 10);
		buildButton.setVisible(false);

		buildPanelRoad = new BuildPanelRoad(controller, width / 10, width / 10);
		buildPanelRoad.setBounds(150, 150, width / 10, width / 10);
		buildPanelRoad.setVisible(false);

		rollDicePanel = new DicePanel(width / 10, height / 10);
		rollDicePanel.setBounds((int) ((width / 10) * 8.5),
				(int) ((height / 10) * 7.25), width / 10, height / 10);
		rollDicePanel.setVisible(false);

		musicStatus = new JLabel("Music: off"); //$NON-NLS-1$
		musicStatus.setVisible(true);

		playMusic = new PlayerButton(ImportImages.confirmBtn, width/50, width/50);
		playMusic.addActionListener(controller);
		playMusic.setActionCommand("musi.play"); //$NON-NLS-1$
		playMusic.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(!((PlayerButton)me.getSource()).isActive()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.confirmBtnActive);
				}else{
					((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(!((PlayerButton)me.getSource()).isActive()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.confirmBtn);
				}else{
					((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtn);
				}
			}
		});
		playMusic.setToolTipText(Messages.getString("MainGUI.PlayMusic")); //$NON-NLS-1$
		
		muteMusic = new PlayerButton(ImportImages.notMuteBtn, width/50, width/50);
		muteMusic.addActionListener(controller);
		muteMusic.setActionCommand("musi.mute"); //$NON-NLS-1$
		muteMusic.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(!((PlayerButton)me.getSource()).isActive()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.notMuteBtnActive);
				}else{
					((PlayerButton)me.getSource()).changeIcon(ImportImages.muteBtnActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(!((PlayerButton)me.getSource()).isActive()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.notMuteBtn);
				}else{
					((PlayerButton)me.getSource()).changeIcon(ImportImages.muteBtn);
				}
			}
		});
		muteMusic.setToolTipText(Messages.getString("MainGUI.MuteSound")); //$NON-NLS-1$

		musicButtons = new JPanel();
		musicButtons.setLayout(new FlowLayout());
		musicButtons.add(playMusic);
		musicButtons.add(muteMusic);
		musicButtons.setBounds(width - width/21, width/50, width/21, width/20);
		musicButtons.setVisible(true);
		musicButtons.setOpaque(false);

		musicContent = new JPanel();
		musicContent.setLayout(new BorderLayout());
		musicContent.add(musicStatus, BorderLayout.NORTH);
		musicContent.add(musicButtons, BorderLayout.CENTER);

		tradePanel = new TradingMenu(controller, (int) (width / 2.5),
				(int) ((height / 5) * 3.2));
		tradePanel.setBounds((int) ((width / 3) * 1.10), (height / 6),
				(int) (width / 2.5), (int) ((height / 5) * 3.2));
		tradePanel.setVisible(false);

		cardMenu = new CardMenu(controller, (int) (width / 2),
				(int) ((height / 5) * 4));
		cardMenu.setBounds((int) ((width / 3.5)), (height / 8),
				(int) (width / 2), (int) ((height / 5) * 5));
		cardMenu.setVisible(false);

		buildingcosts = new PlayerButton(ImportImages.buildingcostsmenuBtn,
				radius, radius);
		buildingcosts.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){		
				((PlayerButton)me.getSource()).changeIcon(ImportImages.buildingcostsmenuBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.buildingcostsmenuBtn);
			}
		});
		
		buildingcosts.setToolTipText(Messages.getString("MainGUI.Baukosten")); //$NON-NLS-1$
		buildingcosts.setVisible(true);

		buildingCostsMenuPanel = new BuildingCostsMenu((int) (width / 2.5),
				(int) ((height / 5) * 3));
		buildingCostsMenuPanel.setBounds((int) ((width / 3) * 1.10),
				(height / 6), (int) (width / 2.5), (int) ((height / 5) * 4));
		buildingCostsMenuPanel.setVisible(false);

		resourcePanel = new ResourcePanel(controller, (int) (width * 0.3), (int) (height * 0.65));
		resourcePanel.setBounds((int) ((width / 3) * 1.10), (height / 6),
				(int) (width * 0.3), (int) (height * 0.65));

		supplyPanel = new SupplyPanel(controller, (int) (width * 0.1),
				(int) (height * 0.35));
		supplyPanel.setVisible(true);

		contentPanel.setBounds(0, 0, width, height);
		polygonMap.setBounds(0, 0, width, height);

		contentPanel.setOpaque(false);
		polygonMap.setOpaque(false);

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * F&uuml;gt der <code>MainGUI</code> die Interaktion mit dem <code>Controller</code> hinzu.
	 */
	private void setupInteraction() {

		buildingcosts.setActionCommand("menu.buildingcosts"); //$NON-NLS-1$
		buildingcosts.addActionListener(controller);
		
		buildButton.setActionCommand("node.settlement");
		buildButton.addActionListener(controller);

		polygonMap.addMouseListener(controller);
		this.addMouseListener(controller);
	}

	/**
	 * F&uuml;gt der <code>MainGUI</code> alle Komponenten hinzu.
	 */
	private void addWidgets() {
		Container c = this.getContentPane();
		c.setBackground(new Color(30, 144, 255));

		contentPanel.setLayout(new GridBagLayout());
		opponentsPanel.setLayout(new FlowLayout());

		for (int i = 0; i < opponentFrames.length; i++) {
			opponentsPanel.add(opponentFrames[i]);
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(5, 5, 5, 0);
		contentPanel.add(opponentsPanel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = GridBagConstraints.LAST_LINE_START;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.insets = new Insets(5, 5, 40, 5);
		contentPanel.add(chatFrame, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.insets = new Insets(0, 0, 0, (int) (width * 0.01));
		contentPanel.add(buildingcosts, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(0, 0, 0, (int) (width * 0.01));
		contentPanel.add(supplyPanel, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.weighty = GridBagConstraints.LAST_LINE_END;
		gbc.insets = new Insets(5, 5, 35, 20);
		contentPanel.add(playerFrame, gbc);
		
		layeredPane.add(polygonMap, new Integer(1));
		layeredPane.add(contentPanel, new Integer(2));
		layeredPane.add(buildButton, new Integer(3));
		layeredPane.add(buildPanelRoad, new Integer(3));
		layeredPane.add(rollDicePanel, new Integer(4));
		layeredPane.add(tradePanel, new Integer(5));
		layeredPane.add(resourcePanel, new Integer(5));
		layeredPane.add(cardMenu, new Integer(5));
		layeredPane.add(buildingCostsMenuPanel, new Integer(5));
		layeredPane.add(musicButtons, new Integer(5));

		add(layeredPane);

		this.pack();
		setIsStarted(true);
	}

	private void setIsStarted(boolean b) {
		isStarted = b ;
	}

	/**
	 * Erstellt ein neues <code>PlayerChoosePanel</code> mit den
	 * <code>playerIDs</code> erzeugt. Hier kann man ausw&auml;hlen, von wem man
	 * klauen will.
	 * 
	 * @param playerIds
	 *            sind die IDs der <code>Settlers</code>
	 */
	public void showRobberPanel(ArrayList<Integer> playerIds) {
		robberPanel = new PlayerChoosePanel(controller, playerIds, false);
		robberPanel.setBounds((int) ((width / 3) * 1.10), (height / 6),
				robberPanel.getWidth(), robberPanel.getHeight());
		robberPanel.setVisible(true);
		layeredPane.add(robberPanel, new Integer(5));
	}

	/**
	 * Erstellt ein neues <code>PlayerChoosePanel</code> mit den
	 * <code>playerIDs</code> erzeugt. Hier kann man ausw&auml;hlen, mit welchem
	 * Spieler man handeln will.
	 * 
	 * @param playerIds
	 *            sind die IDs der <code>Settlers</code>
	 */
	public void showTradeesPanel() {
		tradeePanel = new TradeChoosePanel(controller);
		tradeePanel.setBounds((int) ((width / 3) * 1.10), (height / 6),
				tradeePanel.getWidth(), tradeePanel.getHeight());
		tradeePanel.setVisible(true);
		layeredPane.add(tradeePanel, new Integer(5));
	}

	/**
	 * Erstellt ein neues <code>ConfirmTradePanel</code> auf Grundlage der
	 * <code>offR</code> (=die angebotenen Rohstoffe) und der <code>expR</code>
	 * (die erwarteten Rohstoffe). Hier kann man ausw&auml;hlen, ob man einen
	 * Handel annimmt (als Handelspartner).
	 * 
	 * @param offR
	 *            (= offered Resources)
	 * @param expR
	 *            (= expected Resources)
	 */
	public void showConfirmTradePanel(int[] offR, int[] expR) {
		confirmTradePanel = new ConfirmTradePanel(controller,
				(int) (width / 2.5), (int) ((height / 5) * 3.2), offR, expR);
		confirmTradePanel.setBounds((int) ((width / 3) * 1.10), (height / 6),
				(int) (width / 2.5), (int) ((height / 5) * 3.2));
		confirmTradePanel.setVisible(true);
		layeredPane.add(confirmTradePanel, new Integer(5));
	}

	public PlayerChoosePanel getRobberPanel() {
		return robberPanel;
	}

	public ChatGUI getChatGUI() {
		return chatFrame;
	}

	public PolygonMap getPolygonMap() {
		return polygonMap;
	}

	public SettlerFrame getPlayerFrame() {
		return playerFrame;
	}

	public TradingMenu getTradingMenu() {
		return tradePanel;
	}

	public BuildPanelRoad getBuildPanelRoad() {
		return buildPanelRoad;
	}

	public DicePanel getDicePanel() {
		return rollDicePanel;
	}

	public ResourcePanel getResourcePanel() {
		return resourcePanel;
	}

	public CardMenu getCardMenu() {
		return cardMenu;
	}

	public OpponentFrame[] getOpponentFrames() {
		return opponentFrames;
	}

	public SupplyPanel getSupplyPanel() {
		return supplyPanel;
	}

	public BuildingCostsMenu getBuildingCostsMenuPanel() {
		return buildingCostsMenuPanel;
	}

	public ConfirmTradePanel getConfirmTradePanel() {
		return confirmTradePanel;
	}

	public TradeChoosePanel getTradeePanel() {
		return tradeePanel;
	}

	public static boolean isStarted() {
		return isStarted;
	}
	

	public PlayerButton getMuteMusic() {
		return muteMusic;
	}
	public PlayerButton getPlayMusic() {
		return playMusic;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	public JFrame getThis(){
		return this;
	}

	public static void setFrameCount(int i) {
		playerImage = i;
	}

	public PlayerButton getBuildButton() {		
		return buildButton;
	}


}
