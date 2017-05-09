package networking;

import gui.PolygonMap;

import java.net.Socket;
import model.IslandOfCatan;
import model.Settler;

/**
 * Interface f&uuml;r die <code>Clients</code>.
 * 
 * @author Michael Strobl
 *
 */
public interface ClientInterface {

	/**
	 * Setzt den n&auml;chsten Spieler.
	 */
	public void nextPlayer();

	public PolygonMap getPolygonMap();

	public void setPolygonMap(PolygonMap polygonMap);

	public int getCurrentPlayer();

	public Socket getSocket();

	public IslandOfCatan getIsland();

	public void setIsland(IslandOfCatan island);

	public boolean isBeginning();

	public void setBeginning(boolean beginning);

	public Settler getSettler();

	public void setSettler(Settler settler);

	public int getID();

	public String getUsername();

	public int getCurrentPhase();

	public void setCurrentPhase(int currentPhase);

	public boolean isFirstRoll();

	public void setFirstRoll(boolean firstRoll);

}
