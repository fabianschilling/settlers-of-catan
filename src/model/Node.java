package model;

import java.io.Serializable;
import java.util.Vector;

/**
 * Speichert den Status eines Knotens ab.
 * 
 * @author Michael Strobl, Fabian Schilling, Christian Hemauer, Thomas Wimmer
 * 
 */
public class Node implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID des Spielers, der darauf gebaut hat.
	 */
	private int ownerID;

	/**
	 * Geb&auml;de auf dem Knoten.
	 */
	private int building;

	/**
	 * Hafen am Knoten
	 */
	private byte harbor;

	/**
	 * Nachbarknoten
	 */
	private Vector<Node> neighborNodes;

	/**
	 * Index im Node-Array in der <code>IslandOfCatan</code>.
	 */
	private int index;

	/**
	 * Knoten im Wasser.
	 */
	private boolean waterNode;

	/**
	 * Knoten am Wasser.
	 */
	private boolean coastalNode;
	
	/**
	 * Anliegende <code>Tiles</code>.
	 */
	private Vector<Tile> tiles;
	
	/**
	 * Anliegende <code>Roads</code>.
	 */
	Vector<Road> roads;

	/**
	 * Konstruktor der Knoten.
	 * 
	 * @param index
	 *            Index im Knoten-Array
	 */
	public Node(int index) {
		this.index = index;
		setOwnerID(Constants.NOBODY);
		setBuilding(Constants.UNBUILT);
		harbor = Constants.NOHARBOR;
		neighborNodes = new Vector<Node>();
		roads = new Vector<Road>();
		tiles = new Vector<Tile>();
		waterNode = false;
		coastalNode = false;
	}

	/**
	 * Testet, ob auf dem Knoten gebaut werden darf.
	 * 
	 * @return false wenn nicht gebaut werden darf, true wenn man darauf bauen
	 *         darf
	 */
	public boolean isBuildable(IslandOfCatan island, int ownerID,
			boolean beginning) {
		int buildCounter = 0;
		for (int i = 0; i < neighborNodes.size(); i++) {
			if (neighborNodes.get(i).getBuilding() == Constants.UNBUILT) {
				buildCounter++;
			}
		}
		if (buildCounter == 3 && allowedToBuild(island, ownerID)) {
			return true;
		} else if (buildCounter == 3 && beginning) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gibt die Erlaubnis zum Bauen zur&uuml;ck.
	 * 
	 * @param island
	 *            Insel
	 * @param ownerID
	 *            ID des Besitzers des Knotens
	 * @return <code>true</code> f&uuml; Bauerlaubnis
	 */
	private boolean allowedToBuild(IslandOfCatan island, int ownerID) {
		for (int i = 0; i < island.getRoads().length; i++) {
			for (int j = 0; j < neighborNodes.size(); j++) {
				if (island.getRoads()[i].getEnd() == this
						&& island.getRoads()[i].getStart() == neighborNodes
								.get(j)
						&& island.getRoads()[i].getOwner() == ownerID) {
					return true;
				}
				if (island.getRoads()[i].getEnd() == neighborNodes.get(j)
						&& island.getRoads()[i].getStart() == this
						&& island.getRoads()[i].getOwner() == ownerID) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector<Node> getNeighborNodes() {
		return neighborNodes;
	}

	public void setNeighborNodes(Vector<Node> neighborNodes) {
		this.neighborNodes = neighborNodes;
	}

	public int getBuilding() {
		return building;
	}

	public void setBuilding(int building) {
		this.building = building;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getNeighborNode(int index) {
		return neighborNodes.get(index).getBuilding();
	}

	public byte getHarbor() {
		return harbor;
	}

	public void setHarbor(byte harbor) {
		this.harbor = harbor;
	}

	public int getIndex() {
		return index;
	}

	public String toString() {
		return "" + index;
	}

	public boolean isBuiltOn() {
		return !(building == Constants.UNBUILT);
	}

	public boolean isWaterNode() {
		return waterNode;
	}

	public void setWaterNode(boolean waterNode) {
		this.waterNode = waterNode;
	}

	public boolean isCoastalNode() {
		return coastalNode;
	}

	public void setCoastalNode(boolean coastalNode) {
		this.coastalNode = coastalNode;
	}
	
	public Vector<Tile> getTiles() {
		return tiles;
	}
	
	public Vector<Road> getRoads() {
		return roads;
	}
}
