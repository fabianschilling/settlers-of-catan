package gui;

import java.awt.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import networking.ExceptionLogger;

import controller.Controller;

/**
 * Die Klasse repr&auml;sentiert das Chatfenster f&uuml;r jeden Spieler.
 * 
 * @author Fabian Schilling, Thomas Wimmer
 */

@SuppressWarnings("serial")
public class ChatGUI extends JPanel {

	/**
	 * Das Ausgabefeld des Chatfensters.
	 */
	private JTextPane timeline;
	/**
	 * Das StyledDocument der timeline
	 */
	private StyledDocument doc;
	/**
	 * 
	 */
	private SimpleAttributeSet set;
	/**
	 * Das Eingabefeld des Chatfensters.
	 */
	private JTextField inputField;
	/**
	 * Die Breite des Chatfensters.
	 */
	private int width;
	/**
	 * Die H&ouml;he des Chatfensters.
	 */
	private int height;
	/**
	 * Die Abstand des <code>inputField</code> vom unteren Rand des Panels.
	 */
	private int bottomSpace;
	/**
	 * Hintergrundbild des Panels.
	 */
	private ImagePanel backgroundPanel;
	/**
	 * Controller f&uuml;r den Chat.
	 */
	private Controller controller;
	/**
	 * Scrollbar f&uuml; fŸr den Chat
	 */
	private JScrollPane scrollBar;
	/**
	 * ExceptionLogger, der auftretende Exceptions protokolliert
	 */
	private ExceptionLogger exceptionLogger;
	/**
	 * Konstruktor des Chatfensters
	 * 
	 * @param width
	 *            Die Breite des Chatfensters.
	 * @param height
	 *            Die H&ouml;he des Chatfensters.
	 */
	public ChatGUI(Controller controller, int width, int height) {
		exceptionLogger = ExceptionLogger.getInstance();
		this.controller = controller;
		this.width = width;
		this.height = height;
		bottomSpace = (int) (width * 0.01);
		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		init();
	}

	/**
	 * Initialisiert das Chatfenster mit den zugeh&ouml;rigen Komponenten.
	 */
	private void init() {
		createWidgets();
		addWidgets();
		setupInteraction();
	}

	/**
	 * Erzeugt die zum Chatfenster geh&ouml;rigen Komponenten.
	 */
	private void createWidgets() {

		backgroundPanel = new ImagePanel(ImportImages.chatBg, width, height - height/20);

		int fieldWidth = (int) (width * 0.8);
		int fieldHeight = (int) (height * 0.6);

		timeline = new JTextPane();
		doc = timeline.getStyledDocument();
		set = new SimpleAttributeSet();

		inputField = new JTextField();

		scrollBar = new JScrollPane(timeline);
		scrollBar.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
		scrollBar.setBackground(new Color(51, 102, 255));

		inputField.setPreferredSize(new Dimension(fieldWidth, inputField
				.getPreferredSize().height));
		inputField.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(
				51, 102, 255), new Color(51, 102, 255)));
		inputField.setBackground(new Color(51, 102, 255));

		timeline.setEditable(false);
		timeline.setBackground(new Color(51, 102, 255));

	}

	private void setupInteraction() {
		inputField.addKeyListener(controller);
	}

	/**
	 * F&uuml;gt die Komponenten dem Chatfenster hinzu.
	 */
	private void addWidgets() {
		add(backgroundPanel);

		backgroundPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		backgroundPanel.add(scrollBar, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(5, 5, bottomSpace, 5);
		backgroundPanel.add(inputField, gbc);

	}

	/**
	 * Gibt den Inhalt der Eingabezeile zur&uuml;ck und löscht den Inhalt.
	 * 
	 * @return Eingabe
	 */
	public String getInput() {
		String input = inputField.getText();
		inputField.setText(null);
		return input;
	}

	public JTextPane getOutputField() {
		return timeline;
	}

	public JScrollPane getScrollBar() {
		return scrollBar;
	}

	/**
	 * F&uuml;gt dem Chat den farbigen String hinzu
	 * 
	 * @param s ist die Chatnachricht
	 * @param c ist die Farbe der Chatnachricht
	 */
	public void append(String s, Color c) {
		try {
			StyleConstants.setBold(set, false);
			StyleConstants.setForeground(set, c);
			doc = timeline.getStyledDocument();
			doc.insertString(doc.getLength(), s, set);
		} catch (Exception e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("ChatGUI.Exception.stringfarbig")); //$NON-NLS-1$
		}
	}

	/**
	 * F&uuml;gt dem Chat einen String hinzu
	 * 
	 * @param s ist die Chatnachricht
	 */
	public void append(String s) {
		try {
			StyleConstants.setBold(set, true);
			StyleConstants.setForeground(set, Color.BLACK);
			doc = timeline.getStyledDocument();
			doc.insertString(doc.getLength(), s, set);
		} catch (Exception e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("ChatGUI.Exception.stringschwarz")); //$NON-NLS-1$
		}
	}
}
