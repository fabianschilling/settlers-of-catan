package networking;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import model.Constants;
import model.IslandOfCatan;
import model.Node;
import model.Road;
import model.Tile;

/**
 * Nachrichten f&uuml;r die &Uuml;bertragung &uuml;ber das Netz.
 * 
 * @author Michael Strobl, Thomas Wimmer, Florian Weiss
 * 
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private Color color;

	private String[] args;

	/**
	 * Typ der Nachricht.
	 */
	private int typeOfMessage;

	/**
	 * Enth&auml;lt eine gebaute Stra&szlig;e.
	 */
	private Road road;

	/**
	 * Augenzahl des ersten W&uuml;rfels.
	 */
	private int pips1;

	/**
	 * Augenzahl des zweiten W&uuml;rfels.
	 */
	private int pips2;

	/**
	 * Menge eines Rohstoffs.
	 */
	private int amountOfResource;

	/**
	 * Enth&auml;lt einen bebauten Knoten.
	 */
	private Node node;

	/**
	 * Zu &uuml;bertragende Nachricht.
	 */
	private String message;

	/**
	 * <code>Vector</code> f&uuml;r IDs von mehreren Spielern
	 */
	private Vector<Integer> players;

	/**
	 * Ausgespielte Entwicklungskarte.
	 */
	private byte devCard;

	/**
	 * Gibt an ob die Karte eine Monopol-Karte ist.
	 */
	private boolean monopoly;

	/**
	 * Gibt an ob die Entwicklungskarte gezogen wurde.
	 */
	private boolean cardDrawn;

	/**
	 * Gibt an ob es sich um den letzten Schritt, die endg&uuml;ltige Annahme
	 * des Handels, handelt.
	 */
	private boolean answered;

	/**
	 * Ende des Spiels.
	 */
	private boolean gameOver;

	/**
	 * Ein Rohstoff.
	 */
	private byte resource;

	/**
	 * Ein Rohstoff.
	 */
	private byte resource2;

	/**
	 * Tile.
	 */
	private Tile tile;

	/**
	 * Angebotene Rohstoffe.
	 */
	private int[] offeredResources;

	/**
	 * Gesuchte Rohstoffe.
	 */
	private int[] expectedResources;

	/**
	 * Insel.
	 */
	private IslandOfCatan island;

	/**
	 * ID eines Spielers.
	 */
	private int settlerID;

	/**
	 * In der Anfangsphase oder nicht.
	 */
	private boolean beginning;

	/**
	 * Erlaubnis zu bauen.
	 */
	private boolean allowed;

	/**
	 * Rohstoff-Matrix.
	 */
	private int[][] resources;

	/**
	 * <code>ArrayList</code> f&uuml;r mehrere Spieler-IDs
	 */
	private ArrayList<Integer> playerIds;

	/**
	 * In der W&uuml;rfelphase oder nicht.
	 */
	private boolean firstRoll;

	/**
	 * 
	 */
	private byte cheat;

	/**
	 * Abgeworfene Rohstoffe.
	 */
	private int[] discardedResources;

	/**
	 * L&auml;nge der Handelsstra&szlig;e
	 */
	private int length;

	/**
	 * Gibt an ob es die erste gespielte Karte in der Runde ist.
	 */
	private boolean firstCard;

	/**
	 * Boolean f&uuml;r das Cheaten.
	 */
	private boolean allowedToCheat;
	
	private boolean accepted;

	/**
	 * Nachricht f&uuml;r das initialisieren der Clients.
	 * 
	 * @param args
	 *            Werte des Clients
	 */
	public Message(String[] args) {
		this.args = args;
		this.typeOfMessage = Constants.MESSAGE_INIT_CLIENT;
	}

	/**
	 * Nachricht f&uuml;r den n&auml;chsten Zug.
	 * 
	 * @param startTurn
	 *            Spieler ist dran oder nicht
	 * @param beginning
	 *            Anfangsphase oder nicht
	 * @param settlerID
	 *            Aktuelle Spieler-ID
	 * @param firstRoll
	 *            W&uuml;rfelphase oder nicht
	 */
	public Message(boolean startTurn, boolean beginning, int settlerID,
			boolean firstRoll) {
		if (startTurn) {
			this.typeOfMessage = Constants.MESSAGE_YOUR_TURN;
		} else {
			this.typeOfMessage = Constants.MESSAGE_END_OF_TURN;
		}
		this.beginning = beginning;
		this.settlerID = settlerID;
		this.firstRoll = firstRoll;
	}

	/**
	 * Nachricht f&uuml;r die Anfangsphase, welcher Spieler an der Reihe ist.
	 * 
	 * @param beginning
	 *            Anfangsphase oder nicht
	 * @param settlerID
	 *            Aktuelle Spieler-ID
	 */
	public Message(boolean beginning, int settlerID) {
		this.beginning = beginning;
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_ID;
	}

	/**
	 * Nachricht f&uuml;r die Abwurfphase, falls eine sieben gew&uuml;rfelt
	 * wurde.
	 * 
	 * @param discardedResources
	 *            Resourcen die abgeworfen werden
	 * @param settlerID
	 *            ID des Spielers der die Karten abwirft
	 */
	public Message(int[] discardedResources, int settlerID) {
		this.discardedResources = discardedResources;
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_DISCARDING;
	}

	/**
	 * Initialisierungsnachricht f&uuml;r alle Spieler.
	 * 
	 * @param island
	 *            Insel
	 * @param settlerID
	 *            Spieler-ID
	 */
	public Message(IslandOfCatan island, int settlerID) {
		this.island = island;
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_INIT_ISLAND;
	}

	/**
	 * Chatnachricht
	 * 
	 * @param chatMessage
	 *            Nachricht
	 */
	public Message(String chatMessage, int id) {
		this.settlerID = id;
		this.message = chatMessage;
		this.typeOfMessage = Constants.MESSAGE_CHAT;
	}

	/**
	 * Nachricht f&uuml;r den Stra&szlig;enbau.
	 * 
	 * @param road
	 *            Stra&szlig;e
	 * @param allowed
	 *            Bauerlaubnis
	 * @param message
	 *            Ausgabestring
	 */
	public Message(Road road, boolean allowed, String message) {
		this.message = message;
		this.allowed = allowed;
		this.road = road;
		this.typeOfMessage = Constants.MESSAGE_ROAD;
	}

	/**
	 * Nachricht f&uum;r den Bau einer Siedlung oder Stadt.
	 * 
	 * @param node
	 *            Knoten, auf dem gebaut werden soll.
	 * @param allowed
	 *            Bauerlaubnis
	 * @param message
	 *            Ausgabestring
	 */
	public Message(Node node, boolean allowed, String message) {
		this.message = message;
		this.allowed = allowed;
		this.node = node;
		this.typeOfMessage = Constants.MESSAGE_NODE;
	}

	/**
	 * Nachricht f&uuml;r die gew&uuml;rfelte Augenzahl.
	 * 
	 * @param pips1
	 *            Erster W&uuml;rfel
	 * @param pips2
	 *            Zweiter W&uuml;rfel
	 * @param id
	 *            ID des Spielers
	 */
	public Message(int pips1, int pips2, int id) {
		this.settlerID = id;
		this.pips1 = pips1;
		this.pips2 = pips2;
		this.typeOfMessage = Constants.MESSAGE_PIPS;
	}

	/**
	 * Nachricht zum Senden der Augenzahl und der Rohstoffe an alle Spieler.
	 * 
	 * @param pips1
	 *            Augenzahl erster W&uuml;rfel
	 * @param pips2
	 *            Augenzahl zweiter W&uuml;rfel
	 * @param resources
	 *            Verteilte Rohstoffe
	 */
	public Message(int pips1, int pips2, int[][] resources) {
		this.pips1 = pips1;
		this.pips2 = pips2;
		this.resources = resources;
		this.typeOfMessage = Constants.MESSAGE_RESOURCES;
	}

	/**
	 * Nachricht f&uuml;r das Spielen und Ziehen einer Entwicklungskarte.
	 * 
	 * @param devCard
	 *            Entwicklungskartenkonstante
	 * @param cardDrawn
	 *            Karte gezogen
	 */
	public Message(byte devCard, boolean cardDrawn) {
		this.devCard = devCard;
		this.cardDrawn = cardDrawn;
		this.resource = -1;
		this.allowed = true;
		this.firstCard = true;
		this.typeOfMessage = Constants.MESSAGE_DEV_CARD;
	}

	/**
	 * Nachricht f&uuml;r das Ziehen und Spielen einer Entwicklungskarte als
	 * Antwort vom Server.
	 * 
	 * @param devCard
	 *            Entwicklungskartenkonstante
	 * @param cardDrawn
	 *            Karte gezogen
	 * @param allowed
	 *            erlaubt zu spielen
	 * @param firstCard
	 *            erste Karte im Zug
	 */
	public Message(byte devCard, boolean cardDrawn, boolean allowed,
			boolean firstCard) {
		this.devCard = devCard;
		this.cardDrawn = cardDrawn;
		this.allowed = allowed;
		this.firstCard = firstCard;
		this.typeOfMessage = Constants.MESSAGE_DEV_CARD;
	}

	/**
	 * Nachricht f&uuml;r die Erfindungskarte.
	 * 
	 * @param devCard
	 *            Entwicklungskartenkonstante
	 * @param resource
	 *            erster Rohstoff
	 * @param resource2
	 *            zweiter Rohstoff
	 */
	public Message(byte devCard, byte resource, byte resource2) {
		this.devCard = devCard;
		this.resource = resource;
		this.resource2 = resource2;
		this.allowed = true;
		this.firstCard = true;
		this.typeOfMessage = Constants.MESSAGE_DEV_CARD;
	}

	/**
	 * Nachricht f&uuml;r den Ritter.
	 * 
	 * @param devCard
	 *            Entwicklungskartenkonstante
	 * @param settlerID
	 *            Spieler-ID
	 */
	public Message(byte devCard, int settlerID) {
		this.devCard = devCard;
		this.settlerID = settlerID;
		this.allowed = true;
		this.firstCard = true;
		this.typeOfMessage = Constants.MESSAGE_DEV_CARD;
	}

	/**
	 * Nachricht f&uuml;r die Monopoly-Karte.
	 * 
	 * @param devCard
	 *            Entwicklungskartenkonstante
	 * @param resource
	 *            Rohstoff
	 * @param amountOfResource
	 *            Menge des Rohstoffs
	 * @param settlerID
	 *            Spieler-ID
	 */
	public Message(byte devCard, byte resource, int amountOfResource,
			int settlerID) {
		this.devCard = devCard;
		this.resource = resource;
		this.settlerID = settlerID;
		this.amountOfResource = amountOfResource;
		this.allowed = true;
		this.firstCard = true;
		this.monopoly = true;
		this.typeOfMessage = Constants.MESSAGE_DEV_CARD;
	}

	/**
	 * Nachricht f&uuml;r das Verschieben und Ausw&auml;hlen des Gegenspielers
	 * beim R&auml;ber.
	 * 
	 * @param tile
	 *            Ziel-Tile
	 * @param playerIds
	 *            Spieler-IDs
	 */
	public Message(Tile tile, ArrayList<Integer> playerIds) {
		this.tile = tile;
		this.playerIds = playerIds;
		this.typeOfMessage = Constants.MESSAGE_ROBBER;
	}

	/**
	 * Nachricht f&uuml;r den geklauten Rohstoff beim R&auml;ber.
	 * 
	 * @param settlerID
	 *            Spieler-ID
	 * @param resource
	 *            Rohstoff
	 */
	public Message(int settlerID, byte resource) {
		this.settlerID = settlerID;
		this.resource = resource;
		this.tile = null;
		this.typeOfMessage = Constants.MESSAGE_ROBBER;
	}

	/**
	 * Nachricht f&uuml;r Ende des Spiels oder &Auml;nderung der l&auml;ngsten
	 * Handelsstra&szlig;e.
	 * 
	 * @param id
	 *            Spieler-ID
	 * @param gameOver
	 *            Spielende oder l&auml;ngste Handelsstra&szlig;e
	 */
	public Message(int settlerID, boolean gameOver) {
		if (gameOver) {
			this.settlerID = settlerID;
			this.typeOfMessage = Constants.MESSAGE_GAME_END;
		} else {
			this.settlerID = settlerID;
			this.typeOfMessage = Constants.MESSAGE_LONGEST_ROAD;
		}
	}

	/**
	 * Nachricht f&uuml;r l&auml;ngste Handelsstra&szlig;e an alle Mitspieler.
	 * 
	 * @param id
	 *            Spieler-ID
	 * @param gameOver
	 * @param length
	 *            L&auml;nge der Handelsstra&szlig;e
	 */
	public Message(int settlerID, boolean gameOver, int length) {
		this.length = length;
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_LONGEST_ROAD;
	}

	/**
	 * Nachricht f&uuml;r den Bank-Handel.
	 * 
	 * @param offeredResources
	 *            angebotene Rohstoffe
	 * @param expectedResources
	 *            gesuchte Rohstoffe
	 */
	public Message(int[] offeredResources, int[] expectedResources) {
		this.offeredResources = offeredResources;
		this.expectedResources = expectedResources;
		this.typeOfMessage = Constants.MESSAGE_BANK_TRADE;
	}

	/**
	 * Nachricht f&uuml;r den Spieler-Handel.
	 * 
	 * @param offeredResources
	 *            angebotene Rohstoffe
	 * @param expectedResources
	 *            gesuchte Rohstoffe
	 * @param allowed
	 *            erlaubter Handel
	 */
	public Message(int[] offeredResources, int[] expectedResources,
			boolean allowed) {
		this.offeredResources = offeredResources;
		this.expectedResources = expectedResources;
		this.allowed = allowed;
		this.typeOfMessage = Constants.MESSAGE_PLAYER_TRADE;
	}

	/**
	 * Nachricht f&uuml;r den Spieler-Handel und wer diesen angenommen hat.
	 * 
	 * @param offeredResources
	 *            angebotene Rohstoffe
	 * @param expectedResources
	 *            gesuchte Rohstoffe
	 * @param playerIds
	 *            Spieler-IDs
	 */
	public Message(int[] offeredResources, int[] expectedResources,
			ArrayList<Integer> playerIds) {
		this.offeredResources = offeredResources;
		this.expectedResources = expectedResources;
		this.playerIds = playerIds;
		this.typeOfMessage = Constants.MESSAGE_PLAYER_TRADE;
	}

	/**
	 * Nachricht f&uuml;r den Spieler-Handel und ob dieser angenommen wurde.
	 * 
	 * @param offeredResources
	 *            angebotene Rohstoffe
	 * @param expectedResources
	 *            gesuchte Rohstoffe
	 * @param settlerID
	 *            Spieler-ID
	 * @param answered
	 *            durchgef&uuml;hrter Handel
	 */
	public Message(int[] offeredResources, int[] expectedResources,
			int settlerID, boolean answered, boolean accepted) {
		this.offeredResources = offeredResources;
		this.expectedResources = expectedResources;
		this.settlerID = settlerID;
		this.answered = answered;
		this.accepted = accepted;
		this.typeOfMessage = Constants.MESSAGE_PLAYER_TRADE;
	}

	/**
	 * Nachricht f&uuml;r die Cheats.
	 * 
	 * @param resource
	 *            Rohstoff
	 * @param amountOfResource
	 *            Menge des Rohstoffs
	 * @param id
	 *            Spieler-ID
	 */
	public Message(byte resource, int amountOfResource, int settlerID) {
		this.resource = resource;
		this.amountOfResource = amountOfResource;
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_CHEAT;
	}

	/**
	 * Nachricht f&uuml;r die Cheats.
	 * 
	 * @param allowedToCheat
	 *            Erlaubnis zum cheaten
	 */
	public Message(boolean allowedToCheat) {
		this.setAllowedToCheat(allowedToCheat);
		this.typeOfMessage = Constants.MESSAGE_CHEAT_ACTIVATION;
	}
	
	public Message(int settlerID) {
		this.settlerID = settlerID;
		this.typeOfMessage = Constants.MESSAGE_PLAYER_LEFT;
	}

	public int getTypeOfMessage() {
		return typeOfMessage;
	}

	public Road getRoad() {
		return road;
	}

	public int getPips1() {
		return pips1;
	}

	public int getPips2() {
		return pips2;
	}

	public Node getNode() {
		return node;
	}

	public String getMessage() {
		return message;
	}

	public byte getDevCard() {
		return devCard;
	}

	public boolean isCardDrawn() {
		return cardDrawn;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isAnswered() {
		return answered;
	}

	public Tile getTile() {
		return tile;
	}

	public byte getResource() {
		return resource;
	}

	public byte getResource2() {
		return resource2;
	}

	public Vector<Integer> getPlayers() {
		return players;
	}

	public int getAmountOfResource() {
		return amountOfResource;
	}

	public IslandOfCatan getIsland() {
		return island;
	}

	public int getSettlerID() {
		return settlerID;
	}

	public boolean isBeginning() {
		return beginning;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public int[][] getResources() {
		return resources;
	}

	public ArrayList<Integer> getPlayerIds() {
		return playerIds;
	}

	public int[] getOfferedResources() {
		return offeredResources;
	}

	public int[] getExpectedResources() {
		return expectedResources;
	}

	public boolean isFirstRoll() {
		return firstRoll;
	}

	public byte getCheat() {
		return cheat;
	}

	public int[] getDiscardedResources() {
		return discardedResources;
	}

	public int getLength() {
		return length;
	}

	public boolean getFirstCard() {
		return firstCard;
	}

	public String[] getArgs() {
		return args;
	}

	public boolean isAllowedToCheat() {
		return allowedToCheat;
	}

	public boolean isMonopoly() {
		return monopoly;
	}

	public void setAllowedToCheat(boolean allowedToCheat) {
		this.allowedToCheat = allowedToCheat;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
}