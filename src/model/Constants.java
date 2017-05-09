package model;

/**
 * Eine Klasse, in der alle Konstanten gespeichert sind.
 * 
 * @author Michael Strobl, Fabian Schilling, Thomas Wimmer, Florian Weiss, Christian Hemauer
 * 
 */
public class Constants {

	/**
	 * Konstante f&uuml;r den Zustand: Unbebaut
	 */
	public static final byte UNBUILT = 0;
	/**
	 * Konstante f&uuml;r den Zustand: Besiedelt mit Dorf
	 */
	public static final byte SETTLEMENT = 1;
	/**
	 * Konstante f&uuml;r den Zustand: Besiedelt mit Stadt
	 */
	public static final byte CITY = 2;
	/**
	 * Konstante f&uuml;r die Mindestanzahl ben&ouml;tigter Ritter um die
	 * gr&ouml;&szlig;te Rittermacht zu bekommen.
	 */
	public static final byte MIN_ARMY_NUM = 3;
	/**
	 * Konstante f&uuml;r den Zustand: Bebaut mit Strasse
	 */
	public static final byte ROAD = 9;
	/**
	 * Konstante f&uuml;r kein Spieler
	 */
	public static final byte NOBODY = 6;
	/**
	 * Konstante f&uuml;r die maximale Anzahl von Karten die man auf der Hand
	 * haben darf, wenn eine sieben gew&uuml;rfelt wurde.
	 */
	public static final byte MAX_CARDS_SEVEN = 7;
	/**
	 * Konstante f&uuml;r "3 zu 1" Hafen
	 */
	public static final byte HARBOR = 44;
	/**
	 * Konstante f&uuml;r "2 Wolle zu 1" Hafen
	 */
	public static final byte WOOLHARBOR = 10;
	/**
	 * Konstante f&uuml;r "2 Eisen zu 1" Hafen
	 */
	public static final byte OREHARBOR = 11;
	/**
	 * Konstante f&uuml;r "2 Holz zu 1" Hafen
	 */
	public static final byte LUMBERHARBOR = 12;
	/**
	 * Konstante f&uuml;r "2 Weizen zu 1" Hafen
	 */
	public static final byte GRAINHARBOR = 13;
	/**
	 * Konstante f&uuml;r "2 Lehm zu 1" Hafen
	 */
	public static final byte BRICKHARBOR = 14;
	/**
	 * Konstante f&uuml;r Woll-Rohstoffkarte
	 */
	public static final byte WOOL = 8;
	/**
	 * Konstante f&uuml;r Eisen-Rohstoffkarte
	 */
	public static final byte ORE = 16;
	/**
	 * Konstante f&uuml;r Holz-Rohstoffkarte
	 */
	public static final byte LUMBER = 17;
	/**
	 * Konstante f&uuml;r Weizen-Rohstoffkarte
	 */
	public static final byte GRAIN = 18;
	/**
	 * Konstante f&uuml;r Lehm-Rohstoffkarte
	 */
	public static final byte BRICK = 19;
	/**
	 * Konstante f&uuml;r keinen Hafen
	 */
	public static final byte NOHARBOR = 20;

	/**
	 * Konstante f&uuml;r die W&uuml;ste (Rohstoffleeres Feld)
	 */
	public static final byte DESERT = 21;
	/**
	 * Konstante f&uuml;r den Zustand: Unbebaubar
	 */
	public static final byte IRRECLAIMABLE = 22;
	/**
	 * Konstante f&uuml;r die Ritter-Ereigniskarte
	 */
	public static final byte KNIGHT = 23;
	/**
	 * Konstante f&uuml;r die Siegpunkt-Ereigniskarte
	 */
	public static final byte VICTORYPOINTS = 24;
	/**
	 * Konstante f&uuml;r die maximale Anzahl der Entwicklungskarten
	 */
	public static final byte DEVCARDS_MAX = 25;
	/**
	 * Konstante f&uuml;r die "Baue zwei Stra&szlig;en"-Ereigniskarte
	 */
	public static final byte BUILDSTREETS = 45;
	/**
	 * Konstante f&uuml;r die "Bekomme zweit Rohstoffkarten"-Ereigniskarte
	 */
	public static final byte GETRESOURCES = 26;
	/**
	 * Konstante f&uuml;r die Monopol-Ereigniskarte
	 */
	public static final byte MONOPOLY = 27;
	/**
	 * Konstante f&uuml;r die Entwicklungskarten
	 */
	public static final byte DEVELOPMENTCARD = 28;
	/**
	 * Konstante f&uuml;r Wasser
	 */
	public static final byte WATER = 29;

	/**
	 * Messagetype f�r das Initialisieren der Clients.
	 */
	public static final byte MESSAGE_INIT_CLIENT = 30;

	/**
	 * Messagetype f�r das Initialisieren der Insel.
	 */
	public static final byte MESSAGE_INIT_ISLAND = 31;

	/**
	 * Messagetype f�r eine Chatnachricht.
	 */
	public static final byte MESSAGE_CHAT = 32;

	/**
	 * Messagetype f�r den Stra&szlig;enbau.
	 */
	public static final byte MESSAGE_ROAD = 33;

	/**
	 * Messagetype f�r die &Auml;nderung eines Knotens.
	 */
	public static final byte MESSAGE_NODE = 34;

	/**
	 * Messagetype f�r die Augenzahlen der W&uuml;rfel.
	 */
	public static final byte MESSAGE_PIPS = 35;

	/**
	 * Messagetype f�r die &Uuml;bertragung der Rohstoffe.
	 */
	public static final byte MESSAGE_RESOURCES = 36;

	/**
	 * Messagetype f�r Entwicklungskarten.
	 */
	public static final byte MESSAGE_DEV_CARD = 37;

	/**
	 * Messagetype f�r das Spielende.
	 */
	public static final byte MESSAGE_GAME_END = 38;

	/**
	 * Messagetype f�r den R&auml;uber.
	 */
	public static final byte MESSAGE_ROBBER = 39;

	/**
	 * Messagetype f�r einen akzeptierten Handel.
	 */
	public static final byte MESSAGE_PLAYER_TRADE_CONFIRM = 40;

	/**
	 * Messagetype f�r einen Handel mit einem Mitspieler.
	 */
	public static final byte MESSAGE_PLAYER_TRADE = 41;

	/**
	 * Messagetype f�r einen Bankhandel.
	 */
	public static final byte MESSAGE_BANK_TRADE = 42;

	/**
	 * Messagetype f�r einen beendeten Zug.
	 */
	public static final byte MESSAGE_END_OF_TURN = 43;

	/**
	 * Messagetype f�r den Anfang eines Zugs.
	 */
	public static final byte MESSAGE_YOUR_TURN = 44;

	/**
	 * Messagetype f�r die Benachrichtigung wer gerade an der Reihe ist.
	 */
	public static final byte MESSAGE_ID = 51;

	/**
	 * Messagetype f�r die &Auml;nderung der l&auml;ngsten Handelsstra&szlig;e.
	 */
	public static final byte MESSAGE_LONGEST_ROAD = 52;

	/**
	 * Erste Phase eines Spielzugs.
	 */
	public static final byte PHASE_1 = 46;

	/**
	 * Zweite Phase eines Spielzugs.
	 */
	public static final byte PHASE_2 = 47;

	/**
	 * Dritte Phase eines Spielzugs.
	 */
	public static final byte PHASE_3 = 48;

	/**
	 * <code>Node</code> am Wasser.
	 */
	public static final byte WATERNODE = 49;

	/**
	 * Konstante f&uuml;r die Nachricht beim Abwerfen der Karten.
	 */
	public static final byte MESSAGE_DISCARDING = 50;

	/**
	 * Anzahl an vorhandenen Siedlungen.
	 */
	public static final byte SETTLEMENTS_MAX = 5;

	/**
	 * Anzahl an vorhandenen St&auml;dten.
	 */
	public static final byte CITIES_MAX = 4;

	/**
	 * Anzahl an vorhandenen Stra&szlig;en.
	 */
	public static final byte ROADS_MAX = 15;
	/**
	 * Konstante f&uuml;r die Message, die die Cheats aktiviert
	 */
	public static final byte MESSAGE_CHEAT_ACTIVATION = 97;
	/**
	 * Konstante f&uuml;r die Message, die die Cheats ausf&uuml;hrt
	 */
	public static final byte MESSAGE_CHEAT = 99;
	/**
	 * Stra&szlig;e die von unten nach oben geht
	 */
	public static final byte ROAD_UP = 90;
	/**
	 * Stra&szlig;e die von oben nach unten geht
	 */
	public static final byte ROAD_DOWN = 91;
	/**
	 * Stra&szlig;e die senkrecht nach unten geht
	 */
	public static final byte ROAD_VERT = 92;
	/**
	 * Vorinitialisierung f&uuml;r eine ungerichtete Stra&szlig;e
	 */
	public static final byte ROAD_NO_DIRECTION = 93;
	
	/**
	 * Bei der Berechnung der l&auml;ngsten Handelsstra&szlig;e der Fall, dass keine Stra&szlig;e gek&uuml;rzt wird
	 */
	public static final byte ROAD_CASE1 = -1;
	
	/**
	 * Bei der Berechnung der l&auml;ngsten Handelsstra&szlig;e der Fall, dass keiner die Karte f&uuml;r die l&auml;ngste Handelsstra&szlig;e bekommt
	 */
	public static final byte ROAD_CASE2 = -2;
	
	/**
	 * Bei der Berechnung der l&auml;ngsten Handelsstra&szlig;e der Fall, dass sich nichts &auml;ndert
	 */
	public static final byte ROAD_CASE3 = -3;
	
	/**
	 * Konstante f&uuml;r den Fall, wenn ein Spieler das Spiel verl&auml;sst
	 */
	public static final byte MESSAGE_PLAYER_LEFT = 53;

}
