package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import networking.Message;
import model.*;
import controller.Controller;

/**
 * Die Klasse stellt das Frontend f&uuml;r die Handelsfunktion zur
 * Verf&uuml;gung. Die Klaase updated sich teilweise selbst. (nicht &uuml;ber
 * den <code> Controller </code>
 * 
 * @author Fabian Schilling, Florian Weiss
 */

@SuppressWarnings("serial")
public class TradingMenu extends JPanel implements ChangeListener,
		ActionListener {

	/**
	 * Hintergrundbild
	 */
	private ImagePanel bgpanel;

	/**
	 * DropDown Menu, in dem die m&ouml;glichen Handelspartner befinden
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox tradepartners;

	/**
	 * Panel, das auf die eigenen Rohstoffe hinweisen soll
	 */
	private JLabel ich;

	/**
	 * Gr&ouml;&szlig;e der Buttons
	 */
	private int size;

	/**
	 * Wolle-Icon
	 */
	private PlayerLabel wool;

	/**
	 * Erz-Icon
	 */
	private PlayerLabel ore;

	/**
	 * Brick-Icon
	 */
	private PlayerLabel brick;

	/**
	 * Lumber-Icon
	 */
	private PlayerLabel lumber;

	/**
	 * Grain-Icon
	 */
	private PlayerLabel grain;

	/**
	 * Wolle-Icon
	 */
	private PlayerLabel wool2;

	/**
	 * Erz-Icon
	 */
	private PlayerLabel ore2;

	/**
	 * Brick-Icon
	 */
	private PlayerLabel brick2;

	/**
	 * Lumber-Icon
	 */
	private PlayerLabel lumber2;

	/**
	 * Grain-Icon
	 */
	private PlayerLabel grain2;

	/**
	 * Button, mit dem man den Handel starten kann
	 */
	private PlayerButton trade;

	/**
	 * Wolle-JSpinner
	 */
	private JSpinner woolspinner;

	/**
	 * Wolle-SpinnerModel
	 */
	private SpinnerNumberModel woolnum;

	/**
	 * Erz-JSpinner
	 */
	private JSpinner orespinner;

	/**
	 * Erz-SpinnerModel
	 */
	private SpinnerNumberModel orenum;

	/**
	 * Lehm-JSpinner
	 */
	private JSpinner brickspinner;

	/**
	 * Lehm-SpinnerModel
	 */
	private SpinnerNumberModel bricknum;

	/**
	 * Holz-JSpinner
	 */
	private JSpinner lumberspinner;

	/**
	 * Holz-SpinnerModel
	 */
	private SpinnerNumberModel lumbernum;

	/**
	 * Weizen-JSpinner
	 */
	private JSpinner grainspinner;

	/**
	 * Weizen-SpinnerModel
	 */
	private SpinnerNumberModel grainnum;

	/**
	 * SpinnerModel-Array, das die JSpinnerModels der Rohstoffe enth&auml;lt
	 */
	private SpinnerNumberModel[] numbers;

	/**
	 * Spinner-Array, das die JSpinner der Rohstoffe enth&auml;lt
	 */
	private JSpinner[] spinners;

	/**
	 * In diesem Array sind die Wechselkurse des Spielers gespeichert
	 */
	int[] rates;

	/**
	 * Wolle-JSpinner
	 */
	private JSpinner woolspinner2;

	/**
	 * Wolle-SpinnerModel
	 */
	private SpinnerNumberModel woolnum2;

	/**
	 * Erz-Spinner
	 */
	private JSpinner orespinner2;

	/**
	 * Erz-SpinnerModel
	 */
	private SpinnerNumberModel orenum2;

	/**
	 * Lehm-Spinner
	 */
	private JSpinner brickspinner2;

	/**
	 * Lehm-SpinnerModel
	 */
	private SpinnerNumberModel bricknum2;

	/**
	 * Holz-Spinner
	 */
	private JSpinner lumberspinner2;

	/**
	 * Holz-SpinnerModel
	 */
	private SpinnerNumberModel lumbernum2;

	/**
	 * Weizen-Spinner
	 */
	private JSpinner grainspinner2;

	/**
	 * Weizen-SpinnerModel
	 */
	private SpinnerNumberModel grainnum2;

	/**
	 * Array f&uuml;r die Spinners
	 */
	private JSpinner[] spinners2;

	/**
	 * Array f&uuml;r die NumberModels der Spinners
	 */
	private SpinnerNumberModel[] numbers2;

	/**
	 * Hier werden die H&auml;fen des Spielers zwischengespeichert
	 */
	private ArrayList<Byte> harbors;

	/**
	 * Breite des Panels
	 */
	private int width;

	/**
	 * H&ouml;he des Panels
	 */
	private int height;

	/**
	 * Der allseitsbekannte Controller
	 */
	private Controller controller;

	/**
	 * Der Spieler, der momentan am Zug ist
	 */
	private Settler current;

	/**
	 * Enth&auml;lt, ob es sich um einen Bankhandel handelt
	 */
	private boolean banktrade = true;

	/**
	 * Enh&auml;lt, ob ich die linken (oder rechten) Spinners ver&auml;ndert
	 * haeb
	 */
	private boolean left;
	
	/**
	 * Z&auml;hler f&uuml;r die Anzahl der zu erhaltenden Rohstoffe von der Spielerseite
	 */
	private int counterLeft;
	
	/**
	 * Z&auml;hler f&uuml;r die Anzahl der hochgez&auml;hlten Rohstoffe der Bankseite
	 */
	private int counterRight;
	
	/**
	 * Enth&auml;lt den letzten Stand der linken Seite
	 */
	private int[] lastScoreL;
	
	/**
	 * Enth&auml;lt den letzten Stand der rechten Seite
	 */
	private int[] lastScoreR;
	
	/**
	 * Nummer im Array, von der das ChangeEvent gekommen ist
	 */
	private int source;
	/**
	 * Button um das Panel zu schlie�en
	 */
	private PlayerButton exit;
	/**
	 * Label unter dem Schlie�en-Button
	 */
	private JLabel exitLabel;


	/**
	 * Der Konstruktor erzeugt ein Objekt des TradingMenus, welches das Frontend
	 * f&uuml;r den Bank-, Hafen- und Spielerhandel darstellt.
	 * 
	 * @param controller
	 *            ist der Controller
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public TradingMenu(Controller controller, int width, int height) {
		this.controller = controller;
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		this.current = controller.getIsland().getCurrentPlayer();
		this.size = width/10;
		// aktueller kurs (bankhandel) - abh�ngig von h�fen
		// reihenfolge: wool, ore, brick, lumber, grain
		// standardm��ig ist der Wechselkurs anfangs 4 zu 1
		rates = new int[] { 4, 4, 4, 4, 4 };
		lastScoreL = new int[] {0,0,0,0,0};
		lastScoreR = new int[] {0,0,0,0,0};
		counterLeft = 0;
		counterRight = 0;
		source = -1;
		init();
	}

	/**
	 * Initialisiert das <code>TradingMenu</code>.
	 */
	public void init() {
		createWidgets();
		setupInteraction();
		addWidgets();
	}

	/**
	 * Erstellt die Bestandteile des <code>TradingMenus<code>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createWidgets() {
		/**
		 * Hier wird der Hintergrund (noch: Schriftrolle hinzugef&uuml;gt)
		 */
		bgpanel = new ImagePanel(ImportImages.chatBg, width, height);

		/**
		 * Hier werden die JSpinners f&uuml;r den Spieler initialisiert. Es
		 * wird: - Der Anfangswert auf 0 gesetzt - Der Minimalwert auf 0 gesetzt
		 * (keine negativen JSpinners) - Der Maximalwert (immer die Anzahl der
		 * aktuellen Resourcen des Spielers), anfangs alles 0 Danach werden die
		 * JSpinners uneditierbar gesetzt, damit man keine Buchstaben oder
		 * komische Zahlen eingeben kann Danach wird die maximale Anzahl der
		 * Ziffern auf maximal 2 (h&ouml;chstens 99) gesetzt.
		 */
		woolnum = new SpinnerNumberModel(0, 0, current.getWool(), 1);
		woolspinner = new JSpinner(woolnum);
		((JSpinner.DefaultEditor) woolspinner.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) woolspinner.getEditor()).getTextField()
				.setColumns(2);
		orenum = new SpinnerNumberModel(0, 0, current.getOre(), 1);
		orespinner = new JSpinner(orenum);
		((JSpinner.DefaultEditor) orespinner.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) orespinner.getEditor()).getTextField()
				.setColumns(2);
		bricknum = new SpinnerNumberModel(0, 0, current.getBrick(), 1);
		brickspinner = new JSpinner(bricknum);
		((JSpinner.DefaultEditor) brickspinner.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) brickspinner.getEditor()).getTextField()
				.setColumns(2);
		lumbernum = new SpinnerNumberModel(0, 0, current.getLumber(), 1);
		lumberspinner = new JSpinner(lumbernum);
		((JSpinner.DefaultEditor) lumberspinner.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) lumberspinner.getEditor()).getTextField()
				.setColumns(2);
		grainnum = new SpinnerNumberModel(0, 0, current.getGrain(), 1);
		grainspinner = new JSpinner(grainnum);
		((JSpinner.DefaultEditor) grainspinner.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) grainspinner.getEditor()).getTextField()
				.setColumns(2);
		
		exit = new PlayerButton(ImportImages.cancelBtn, size/2,size/2);
		exitLabel = new JLabel("Menu verlassen");
		exitLabel.setFont(new Font("Times New Roman", Font.ITALIC, size/3));

		/**
		 * Sowohl die JSpinners, als auch die SpinnerNumberModels werden zur
		 * Vereinfachung in Arrays gepackt
		 */
		numbers = new SpinnerNumberModel[] { woolnum, orenum, bricknum,
				lumbernum, grainnum };
		spinners = new JSpinner[] { woolspinner, orespinner, brickspinner,
				lumberspinner, grainspinner };

		/**
		 * Hier werden die JSpinners f&uuml;r den Handelspartner initialisiert.
		 * Es wird: - Der Anfangswert auf 0 gesetzt - Der Minimalwert auf 0
		 * gesetzt (keine negativen JSpinners) - Der Maximalwert 99 gesetzt
		 * Danach werden die JSpinners uneditierbar gesetzt, damit man keine
		 * Buchstaben oder komische Zahlen eingeben kann Danach wird die
		 * maximale Anzahl der Ziffern auf maximal 2 (h&ouml;chstens 99)
		 * gesetzt.
		 */
		woolnum2 = new SpinnerNumberModel(0, 0, 99, 1);
		woolspinner2 = new JSpinner(woolnum2);
		((JSpinner.DefaultEditor) woolspinner2.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) woolspinner2.getEditor()).getTextField()
				.setColumns(2);
		orenum2 = new SpinnerNumberModel(0, 0, 99, 1);
		orespinner2 = new JSpinner(orenum2);
		((JSpinner.DefaultEditor) orespinner2.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) orespinner2.getEditor()).getTextField()
				.setColumns(2);
		bricknum2 = new SpinnerNumberModel(0, 0, 99, 1);
		brickspinner2 = new JSpinner(bricknum2);
		((JSpinner.DefaultEditor) brickspinner2.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) brickspinner2.getEditor()).getTextField()
				.setColumns(2);
		lumbernum2 = new SpinnerNumberModel(0, 0, 99, 1);
		lumberspinner2 = new JSpinner(lumbernum2);
		((JSpinner.DefaultEditor) lumberspinner2.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) lumberspinner2.getEditor()).getTextField()
				.setColumns(2);
		grainnum2 = new SpinnerNumberModel(0, 0, 99, 1);
		grainspinner2 = new JSpinner(grainnum2);
		((JSpinner.DefaultEditor) grainspinner2.getEditor()).getTextField()
				.setEditable(false);
		((JSpinner.DefaultEditor) grainspinner2.getEditor()).getTextField()
				.setColumns(2);

		/**
		 * Sowohl die JSpinners, als auch die SpinnerNumberModels werden zur
		 * Vereinfachung in Arrays gepackt
		 */
		numbers2 = new SpinnerNumberModel[] { woolnum2, orenum2, bricknum2,
				lumbernum2, grainnum2 };
		spinners2 = new JSpinner[] { woolspinner2, orespinner2, brickspinner2,
				lumberspinner2, grainspinner2 };

		/**
		 * Hierbei handelt es sich um das DropDown Menu f&uuml;r die Auswahl des
		 * Handelspartners
		 */
		tradepartners = new JComboBox();
		tradepartners.addItem(Messages.getString("TradingMenu.Hafen")); //$NON-NLS-1$
		tradepartners.addItem(Messages.getString("TradingMenu.Spieler")); //$NON-NLS-1$

		/**
		 * Im Folgenden wird der Font f&uuml;r die Labels und JComboBoxes
		 * gesetzt
		 */
		Font f = new Font("Times New Roman", Font.ITALIC, size/3); //$NON-NLS-1$
		tradepartners.setFont(f);
		ich = new JLabel("Meine Rohstoffe"); //$NON-NLS-1$
		ich.setFont(f);

		/**
		 * Im Folgenden werden die zugeh&ouml;rigen Bilder reingeladen und in
		 * PlayerLabels gespeichert
		 */
		wool = new PlayerLabel(ImportImages.woolBtn, size, size);
		ore = new PlayerLabel(ImportImages.oreBtn, size, size);
		brick = new PlayerLabel(ImportImages.brickBtn, size, size);
		lumber = new PlayerLabel(ImportImages.lumberBtn, size, size);
		grain = new PlayerLabel(ImportImages.grainBtn, size, size);

		wool2 = new PlayerLabel(ImportImages.woolBtn, size, size);
		ore2 = new PlayerLabel(ImportImages.oreBtn, size, size);
		brick2 = new PlayerLabel(ImportImages.brickBtn, size, size);
		lumber2 = new PlayerLabel(ImportImages.lumberBtn, size, size);
		grain2 = new PlayerLabel(ImportImages.grainBtn, size, size);

		/**
		 * Hier wird der Button f&uuml;r die Durchf&uuml;hrung des Handels
		 * initialisiert
		 */
		trade = new PlayerButton(ImportImages.arrowDouble, (int)(1.5*size), size);
		trade.setToolTipText(Messages.getString("TradingMenu.Handeln")); //$NON-NLS-1$
		trade.setEnabled(false);
	}

	/**
	 * F&uuml;gt die Interaktion mit dem <code>Controller</code> hinzu.
	 */
	public void setupInteraction() {
		addMouseListener(controller);

		trade.addActionListener(controller);
		trade.setActionCommand("deal.trade"); //$NON-NLS-1$
		trade.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.arrowDoubleActive);
				}
			}
			public void mouseExited(MouseEvent me){
				if(((PlayerButton)me.getSource()).isEnabled()){
					((PlayerButton)me.getSource()).changeIcon(ImportImages.arrowDouble);
				}
			}
		});

		woolspinner.addChangeListener(this);
		orespinner.addChangeListener(this);
		brickspinner.addChangeListener(this);
		lumberspinner.addChangeListener(this);
		grainspinner.addChangeListener(this);

		woolspinner2.addChangeListener(this);
		orespinner2.addChangeListener(this);
		brickspinner2.addChangeListener(this);
		lumberspinner2.addChangeListener(this);
		grainspinner2.addChangeListener(this);

		tradepartners.addActionListener(this);
		tradepartners.setActionCommand("choose"); //$NON-NLS-1$
		
		exit.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				getThis().setVisible(false);	
			}
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cancelBtn);
			}
		});

	}

	/**
	 * F&uuml;gt dem <code>TradingMenu</code> die Bestandteile hinzu.
	 */
	public void addWidgets() {

		add(bgpanel);
		bgpanel.setLayout(new GridBagLayout());

		/**
		 * Erste Spalte des Grids
		 */
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(ich, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(wool, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(ore, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(brick, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(lumber, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(grain, c);

		/**
		 * Zweite Spalte des Grids
		 */
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(woolspinner, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(orespinner, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(brickspinner, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(lumberspinner, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(grainspinner, c);

		/**
		 * Dritte Spalte des Grids
		 */

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 20, 0, 20);
		// bgpanel.add(new JLabel("<- Trade ->"), c);
		bgpanel.add(trade, c);

		/**
		 * Vierte Spalte des Grids
		 */
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridwidth = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 0, 0, 20);
		bgpanel.add(tradepartners, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(woolspinner2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(orespinner2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(brickspinner2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(lumberspinner2, c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 0, 0, 0);
		bgpanel.add(grainspinner2, c);

		/**
		 * F�nfte Spalte des Grids
		 */

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(wool2, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(ore2, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(brick2, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(lumber2, c);

		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bgpanel.add(grain2, c);
		
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 6;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10,0,5,0);
		bgpanel.add(exit, c);
		
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 7;
		c.anchor = GridBagConstraints.CENTER;
		bgpanel.add(exitLabel, c);

	}

	/**
	 * Die Methode ist Zust&auml;ndig f&uuml;r die Generierung der
	 * Handelsnachricht, die vom Server bzw. von den Clients verarbeitet werden
	 * kann.
	 * 
	 * @return Handelsnachricht f&uuml;r Server und Clients
	 */
	public Message getMessage() {

		int[] offeredResources = new int[5];
		for (int i = 0; i < offeredResources.length; i++) {
			offeredResources[i] = (Integer) spinners[i].getValue();
		}

		int[] expectedResources = new int[5];
		for (int i = 0; i < expectedResources.length; i++) {
			expectedResources[i] = (Integer) spinners2[i].getValue();
		}

		String partner = (String) tradepartners.getSelectedItem();
		if (partner.equals(Messages.getString("TradingMenu.Hafen"))) { //$NON-NLS-1$
			return new Message(offeredResources, expectedResources);
		} else {
			return new Message(offeredResources, expectedResources, true);
		}
	}

	/**
	 * Die Methode setzt die Werte aller JSpinners auf 0
	 */
	public void reset() {
		for (int i = 0; i < numbers.length; i++) {
			numbers[i].setValue(0);
			numbers2[i].setValue(0);
			lastScoreL[i] = 0;
			lastScoreR[i] = 0;
		}
		counterLeft = 0;
		counterRight = 0;
	}

	/**
	 * Setzt alle Schritte auf die richtige Gr&ouml;&szlig;e.
	 * Stellt dabei die ChangeListener ab, um keine weiteren ChangeEvents auszul&ouml;sen.
	 */
	public void update() {
		killAllChangeListener();
		reset();
		int[] playerRes = getResources();
		current = controller.getIsland().getCurrentPlayer();
		harbors = current.getHarbors();
		if (banktrade) {
			if (harbors.contains(Constants.HARBOR)) {
				// wenn 3zu1 hafen vorhanden ist setze die rate auf 3zu1
				for (int i = 0; i < rates.length; i++) {
					rates[i] = 3;
				}
			}
			// wenn einer der spezialh�fen vorhanden ist wird der jeweilige wert
			// auf 2 gesetzt
			if (harbors.contains(Constants.WOOLHARBOR)) {
				rates[0] = 2;
			}
			if (harbors.contains(Constants.OREHARBOR)) {
				rates[1] = 2;
			}
			if (harbors.contains(Constants.BRICKHARBOR)) {
				rates[2] = 2;
			}
			if (harbors.contains(Constants.LUMBERHARBOR)) {
				rates[3] = 2;
			}
			if (harbors.contains(Constants.GRAINHARBOR)) {
				rates[4] = 2;
			}
			for (int i = 0; i < spinners.length; i++) {
				// setzt den spinner nur aktiv, wenn �berhaupt genug Resourcen
				// vorhanden sind
				spinners[i].setEnabled(playerRes[i] >= rates[i]);
				// setzt die Schrittgr��e auf die Art des Hafens, den der
				// Spieler besitzt
				numbers[i].setStepSize(rates[i]);
				// setzt das Spinnermaximum auf die Anzahl des Spielers
//				numbers[i].setMaximum(playerRes[i]);
				// setzt die Hafenspinner auf die maximal m�gliche Anzahl
//				numbers2[i].setMaximum(1);
				spinners2[i].setEnabled(false);
			}
			
		} else {
			for (int i = 0; i < spinners.length; i++) {
				// beim Spielerhandel wird der Spinner nur disabled wenn gar
				// keine Rohstoffe der Kategorie vorhanden sind
				spinners[i].setEnabled(playerRes[i] != 0);
				// die Schrittgr�e wird wieder auf 1 gesetzt
				numbers[i].setStepSize(1);
				// das Maximum der Resourcen des Spielers ist 99
				numbers2[i].setMaximum(99);
				spinners2[i].setEnabled(true);

			}
		}

		// wird unabh�ngig von Bank oder PlayerTrade geupdatet
		// ein Spieler kann nur die Resourcen angeben, die er auch wirklich hat
		// zus�tzlich werden die Minima wieder auf 0 gesetzt von der linken Seite
		for (int i = 0; i < numbers.length; i++) {
			numbers[i].setMaximum(playerRes[i]);
			numbers[i].setMinimum(0);
		}
		trade.setEnabled(false);
		reviveAllChangeListener();
	}

	public void stateChanged(ChangeEvent ce) {			
		if (banktrade) {
			JSpinner src = (JSpinner) ce.getSource();
//			SpinnerNumberModel snm = (SpinnerNumberModel) src.getModel();
//			int max = (Integer) snm.getNumber();
			left = false;
			int[] currentL = new int[5];
			int[] currentR = new int[5];
			source = -1;
			
			killAllChangeListener();
			
			// pr�ft, ob das stateChanged-Event auf der linken oder rechten
			// Seite stattfindet (buttons enable)
			for (int i = 0; i < spinners.length; i++) {
				currentL[i] = numbers[i].getNumber().intValue();
				if (spinners[i].equals(src)) {
					source = i;
					left = true;
					break;
				}
			}
			
			if (left) {
				if (currentL[source] > lastScoreL[source]) {
					counterLeft++;
				}
				if (currentL[source] < lastScoreL[source]) {
					counterLeft--;
				}
				for (int i = 0; i < currentL.length; i++) {
					lastScoreL[i] = currentL[i];
				}
			} else {
				for (int i = 0; i < spinners2.length; i++) {
					currentR[i] = numbers2[i].getNumber().intValue();
					if (spinners2[i].equals(src)) {
						source = i;						
						break;
					}
				}
				if (currentR[source] > lastScoreR[source]) {
					counterRight++;
				}
				if (currentR[source] < lastScoreR[source]) {
					counterRight--;
					setMinAll(numbers, new int[]{0,0,0,0,0});
				}
				for (int i = 0; i < currentR.length; i++) {
					lastScoreR[i] = currentR[i];
				}
			}
			if (counterLeft > counterRight) {
				setMaxAll(numbers2,new int[]{99,99,99,99,99});
				enableAll(spinners2);
			}
			if (counterLeft == 0) {
				disableAll(spinners2);
			}
			if (counterLeft == counterRight) {	
				setMinAll(numbers,lastScoreL);
				setMaxAll(numbers2,lastScoreR);
			}
			
			reviveAllChangeListener();
			
			trade.setEnabled(counterRight == counterLeft && counterRight != 0);
			
		}
		/*
		 * Bewirkt, dass man nicht mehr verschenken darf.
		 */
		else{
			JSpinner src = (JSpinner) ce.getSource();
			for (int i = 0; i < spinners.length; i++) {
				if (spinners[i].equals(src)) {
					if (spinners[i].getValue().equals(new Integer(0))) {
						spinners2[i].setEnabled(true);
					}
					else{
						spinners2[i].setValue(new Integer(0));
						spinners2[i].setEnabled(false);
					}
					break;
				}
			}
			// der tradeButton wird nur aktiv, wenn auf beiden seiten rohstoffe
			// gew�hlt wurden
			trade.setEnabled(hasValue(numbers) && hasValue(numbers2));
		}
	}

	public void actionPerformed(ActionEvent ae) {
		banktrade = tradepartners.getSelectedItem().equals(Messages.getString("TradingMenu.Hafen")); //$NON-NLS-1$
		reset();
		update();
	}

	/**
	 * Die Methode disabled alle JSpinners sps au&szlig;er den JSpinner sp
	 * 
	 * @param sps
	 *            ist ein Array von JSpinnern
	 * @param sp
	 *            ist ein einzelner JSpinner
	 */
	public void disableAllBut(JSpinner[] sps, JSpinner sp) {
		for (int i = 0; i < sps.length; i++) {
			if (!sps[i].equals(sp)) {
				sps[i].setEnabled(false);
			}
		}
	}
	/**Disabled alle Spinner des &uuml;bergebenen Arrays
	 * @param sps zu disablendes Jspinner Array
	 */
	public void disableAll(JSpinner[] sps) {
		for (int i = 0; i < sps.length; i++) {			
				sps[i].setEnabled(false);			
		}
	}
	
	/**Setzt alle Werte des &uuml;bergebenen Arrays auf 0
	 * @param numbers SpinnerNumberModel Array
	 */
	public void resetAllOf( SpinnerNumberModel[] numbers) {
		for (int i = 0; i < numbers.length; i++) {	
			numbers[i].setValue(new Integer(0));						
		}
	}

	/**
	 * Die Methode setzt alle JSpinners enabled Danach wird jedoch update()
	 * aufgerufen und die Spinners, die vorher schon disabled waren, bleiben es
	 * auch
	 * 
	 * @param sps
	 *            ist ein Array von JSpinnern
	 */
	public void enableAll(JSpinner[] sps) {

		for (int i = 0; i < sps.length; i++) {
			sps[i].setEnabled(true);
		}
	}

	/**
	 * Die Methode gibt zur&uuml;ck, ob in einem Spinner ein anderer Wert als 0
	 * steht
	 * 
	 * @param snm
	 *            ist das Array mit <code>SpinnerNumberModel</code>s
	 * @return true, wenn ein Wert au&szlig;er 0 drinsteht.
	 */
	public boolean hasValue(SpinnerNumberModel[] snm) {
		boolean hasValue = false;
		for (int i = 0; i < numbers.length; i++) {
			if (!snm[i].getNumber().equals(new Integer(0))) {
				hasValue = true;
				break;
			}
		}
		return hasValue;
	}

	/**
	 * Die Methode gibt alle Rohstoffe, die der Player inneh&auml;lt als
	 * sch&ouml;nes Array zur&uuml;ck
	 * 
	 * @return Array der Rohstoffe (Wolle, Erz, Lehm, Holz, Weizen)
	 */
	public int[] getResources() {
		Settler c = controller.getIsland().getCurrentPlayer();
		return new int[] { c.getWool(), c.getOre(), c.getBrick(),
				c.getLumber(), c.getGrain() };

	}
	
	/**Setzt alle Minima des Arrays auf die Werte des zweiten Arrays
	 * @param snm Array mit Spinnern
	 * @param value Array mit den zu setzenden Minima
	 */
	public void setMinAll(SpinnerNumberModel[] snm, int[] value){
		for (int i = 0; i < snm.length; i++) {
			snm[i].setMinimum(value[i]);
		}
	}
	
	/**Setzt alle Maxima des Arrays auf die Werte des zweiten Arrays
	 * @param snm Array mit Spinnern
	 * @param value Array mit den zu setzenden Maxima
	 */
	public void setMaxAll(SpinnerNumberModel[] snm, int[] value){
		for (int i = 0; i < snm.length; i++) {
			snm[i].setMaximum(value[i]);
		}
	}
	
	/**
	 * Entfernt alle ChangeListener
	 */
	public void killAllChangeListener(){
		woolspinner.removeChangeListener(this);
		orespinner.removeChangeListener(this);
		brickspinner.removeChangeListener(this);
		lumberspinner.removeChangeListener(this);
		grainspinner.removeChangeListener(this);

		woolspinner2.removeChangeListener(this);
		orespinner2.removeChangeListener(this);
		brickspinner2.removeChangeListener(this);
		lumberspinner2.removeChangeListener(this);
		grainspinner2.removeChangeListener(this);
	}
	
	/**
	 * F&uuml;gt die ChangeListener wieder hinzu
	 */
	public void reviveAllChangeListener(){
		woolspinner.addChangeListener(this);
		orespinner.addChangeListener(this);
		brickspinner.addChangeListener(this);
		lumberspinner.addChangeListener(this);
		grainspinner.addChangeListener(this);

		woolspinner2.addChangeListener(this);
		orespinner2.addChangeListener(this);
		brickspinner2.addChangeListener(this);
		lumberspinner2.addChangeListener(this);
		grainspinner2.addChangeListener(this);
	}
	
	public TradingMenu getThis(){
		return this;
	}
}
