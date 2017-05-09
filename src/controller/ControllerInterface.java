package controller;

import java.awt.Color;
import java.util.ArrayList;
import networking.ClientInterface;
import networking.Message;
import model.IslandOfCatan;
import model.Road;
/**
 * Interface f&uml;r die Controller.
 * @author Michael Strobl
 *
 */
public interface ControllerInterface {

	/**
	 * Startet die MainGUI auf Grundlage des Models (IslandOfCatan)
	 * 
	 * @param island
	 *            ist das Insel-Model
	 */
	public void startMainGUI(IslandOfCatan island);

	/**
	 * Die Methode schreibt eine Nachricht an alle Opponents im Chat (und an
	 * sich selbst).
	 * 
	 * @param message
	 *            ist die Nachricht (ohne Newline!)
	 */
	public void appendMessage(String message);

	/**
	 * Die Methode schreibt eine Nachricht an alle Opponents im Chat (und an
	 * sich selbst).
	 * 
	 * @param message
	 *            ist die Nachricht (ohne Newline!)
	 */
	public void appendMessage(String message, Color c);

	/**
	 * Updatet die GUI
	 */
	public void refresh();

	/**
	 * Blockt Interaktionen mit der GUI.
	 */
	public void block(boolean block);

	/**
	 * Pr&uuml;ft, ob die Stra&szlig;e im Wasser liegt
	 * 
	 * @return wahr, wenn man Roads bauen kann
	 */
	public boolean isWaterStreet(Road road);

	/**
	 * Schickt die Nachricht an den Server (Networking).
	 * 
	 * @param message
	 *            ist die Networking, nicht die Chat-Message
	 */
	public void sendMessage(Message message);

	/**
	 * Zeigt die Augenzahl auf der GUI an.
	 * 
	 * @param pipOne
	 *            ist die Augenzahl des ersten W\u00FCrfels
	 * @param pipTwo
	 *            ist die Augenzahl des zweiten W\u00FCrfels
	 */
	public void showDices(int pipOne, int pipTwo);

	/**
	 * Versteckt die W\u00FCrfel.
	 */
	public void hideDices();

	/**
	 * Macht das ResourcePanel aktive.
	 * 
	 * @param devCard
	 *            die ausgew\u00E4hlte Entwicklungskarte
	 */
	public void showResources(byte devCard);

	/**
	 * Die Methode aktiviert den R\u00E4uber.
	 */
	public void activateRobberMoving();

	/**
	 * Die Methode aktiviert den R\u00E4uber und setzt ihn auf ein RobberTile.
	 * 
	 * @param robberTile
	 */
	public void activateRobbing(ArrayList<Integer> playerIds);

	/**
	 * Die Mehtode verursacht ein repaint() der PolygonMap.
	 */
	public void repaintMap();

	/**Best&auml;tigt den Handel.
	 * @param offR Angebot
	 * @param expR Nachfrage
	 */
	public void activateTradees(int[] offR,
			int[] expR);

	/**
	 * W&auml;hlt eine Aktion f&uuml;r die KI aus.
	 */
	public void chooseAction();
	
	/**
	 * Setzt das Spielende.
	 */
	public void gameEnd();

	/**Spielt eine Karte
	 * @param devCard Konstante f&uuml;r die jeweilige Karte
	 */
	public void cardPlayed(byte devCard);
	
	/**
	 * Spielt die Stra&szlig;baukarte.
	 */
	public void activateRoadCard();

	/**Zeichnet die Stra&szlig;en neu.
	 * @param roadIndex neue Stra&szlig;e
	 * @param id Spieler
	 */
	public void repaintRoads(int roadIndex, int id);

	/**
	 * Aktualisiert die Anzeige der Gegner
	 */
	public void refreshOpponents();

	/**Aktiviert den Handel.
	 * @param offR Angebot
	 * @param expR Nachfrage
	 */
	public void activateConfirmTrade(int[] offR, int[] expR);

	/**Wirft Anzahl von Karten ab.
	 * @param amount Anzahl
	 */
	public void discard(int amount);

	/**Aktualisiert die Anzeige des Besitzers der l&auml;ngsten Handelsstra&sulig;e.
	 * @param settlerID SpielerID
	 */
	public void refreshLongestRoadView(int settlerID);

	/**Aktualisiert die Anzeige des Besitzers der gr&ouml;&szlig;ten Rittermacht.
	 * @param settlerID SpielerID
	 */
	public void refreshLargestArmyView(int settlerID);

	/**F&uuml;gt eine Stra&szlig;e hinzu.
	 * @param index Index der Stra&szlig;e
	 * @param owner Besitzer
	 */
	public void addRoad(int index, int owner);
	
	/**&Auml;ndert den Status.
	 * @param settlerID SpielerID
	 * @param isAccepted true falls akzeptiert
	 */
	public void switchTradeeStatus(int settlerID, boolean isAccepted);
	
	/**
	 * Versteckt das Handelsfenster.
	 */
	public void hideConfirmPanel();

	public int getPossibleSettlement(int index);

	public void setCardMenuVisible(boolean visible);

	public void setViewsVisible(boolean visible);

	public void setNoInteraction(boolean active);

	public boolean isTradeActivated();

	public void setDiscarding(boolean discarding);

	public void setAllowedToBuild(boolean allowedToBuild);
	
	public IslandOfCatan getIsland();
	
	public ClientInterface getClient();

	public void setBuilt(boolean built);
	
	public void setAllowedToRoll(boolean allowedToRoll);
}
