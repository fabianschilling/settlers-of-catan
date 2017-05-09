package networking;

import gui.PolygonMap;

import java.net.Socket;
import model.Constants;
import model.IslandOfCatan;
import model.Settler;

/**
 * Enth&auml;t den Zustand des Spielers.
 * 
 * @author Michael Strobl, Thomas Wimmer
 * 
 */
public class Client implements ClientInterface {

	/**
	 * Verbindungssocket zum Server.
	 */
	private Socket socket;

	/**
	 * Enth&auml;lt den Zustand der Insel.
	 */
	private IslandOfCatan island;

	/**
	 * In der Anfangsphase oder nicht.
	 */
	private boolean beginning;

	/**
	 * Siedler des Spielers.
	 */
	private Settler settler;

	/**
	 * Graphische Oberfl&auml;che der Insel.
	 */
	private PolygonMap polygonMap;

	/**
	 * Aktuelle Phase.
	 */
	private int currentPhase;

	/**
	 * In der W&uuml;rfelphase oder nicht.
	 */
	private boolean firstRoll;

	/**
	 * Logger
	 */
	@SuppressWarnings("unused")
	private LoggerClass clientLogger;

	/**
	 * Konstruktor f&uuml;r die Klasse Client.
	 */
	public Client(Socket socket, String username) {
		this.socket = socket;
		beginning = true;
		currentPhase = Constants.PHASE_1;
	}

	public void nextPlayer() {
		island.nextPlayer();
	}

	public PolygonMap getPolygonMap() {
		return polygonMap;
	}

	public void setPolygonMap(PolygonMap polygonMap) {
		this.polygonMap = polygonMap;
	}

	public int getCurrentPlayer() {
		return island.getCurrentPlayer().getID();
	}

	public Socket getSocket() {
		return socket;
	}

	public IslandOfCatan getIsland() {
		return island;
	}

	public void setIsland(IslandOfCatan island) {
		this.island = island;
	}

	public boolean isBeginning() {
		return beginning;
	}

	public void setBeginning(boolean beginning) {
		this.beginning = beginning;
	}

	public Settler getSettler() {
		return settler;
	}

	public void setSettler(Settler settler) {
		this.settler = settler;
	}

	public int getID() {
		return settler.getID();
	}

	public String getUsername() {
		return settler.getUsername();
	}

	public int getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}

	public boolean isFirstRoll() {
		return firstRoll;
	}

	public void setFirstRoll(boolean firstRoll) {
		this.firstRoll = firstRoll;
	}

}
