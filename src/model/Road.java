package model;

import java.io.Serializable;

/**
 * Speichert den Zustand einer Stra&szlig;e ab.
 * 
 * @author Michael Strobl, Fabian Schilling, Florian Weiss, Christian Hemauer, Thomas Wimmer
 * 
 */
public class Road implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID des Besitzers der Stra&szlig;e.
	 */
	private int ownerID;

	/**
	 * Start-Knoten
	 */
	private Node start;

	/**
	 * End-Knoten
	 */
	private Node end;

	/**
	 * Steht f&uuml;r eine abgelaufene Stra&szli;e (bei der Berechnung der
	 * l&auml;ngsten Handelsstra&szlig;e).
	 */
	private boolean checked;

	/**
	 * Index im Road-Array in der Insel
	 */
	private int index;

	/**
	 * Ausrichtung der Stra&szlig;e.
	 */
	private byte direction;

	/**
	 * Konstruktor
	 * 
	 * @param owner
	 *            Besitzer
	 * @param start
	 *            Start-Knoten
	 * @param end
	 *            End-Knoten
	 * @param index
	 *            Index
	 */
	public Road(int owner, Node start, Node end, int index) {
		this.setOwner(owner);
		this.setStart(start);
		this.setEnd(end);
		this.checked = false;
		this.index = index;
		this.direction = Constants.ROAD_NO_DIRECTION;
		initRoadsToNodes();
	}

	/**
	 * Bebaubar oder nicht.
	 * 
	 * @param island
	 *            Insel
	 * @param owner
	 *            Besitzer
	 * @return Bauerlaubnis
	 */
	public boolean isBuildable(IslandOfCatan island, int owner) {
		if (end.getOwnerID() == owner) {
			return true;
		} else if (start.getOwnerID() == owner) {
			return true;
		} else if (allowedToBuild(island, owner)) {
			return true;
		}
		return false;
	}

	/**
	 * Hilfsmethode zur Bauerlaubnis
	 * 
	 * @param island
	 *            Insel
	 * @param owner
	 *            Besitzer
	 * @return Bauerlaubnis
	 */
	private boolean allowedToBuild(IslandOfCatan island, int owner) {
		for (int j = 0; j < island.getRoads().length; j++) {
			if (island.getRoads()[j].getEnd() == start
					|| island.getRoads()[j].getStart() == start) {
				if (island.getRoads()[j].getOwner() == owner
						&& (start.getOwnerID() == owner || start.getOwnerID() == Constants.NOBODY)) {
					return true;
				}
			}
		}
		for (int j = 0; j < island.getRoads().length; j++) {
			if (island.getRoads()[j].getEnd() == end
					|| island.getRoads()[j].getStart() == end) {
				if (island.getRoads()[j].getOwner() == owner
						&& (end.getOwnerID() == owner || end.getOwnerID() == Constants.NOBODY)) {
					return true;
				}
			}
		}
		return false;
	}
	/** 
	 * F&uuml;gt die Stra�e den anliegenden Knoten hinzu
	 */
	private void initRoadsToNodes() {
		start.getRoads().add(this);
		end.getRoads().add(this);
	}
	
	public int getOwner() {
		return ownerID;
	}

	public void setOwner(int owner) {
		this.ownerID = owner;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		return getOwner() + " [" + getStart().getIndex() + ", "
				+ getEnd().getIndex() + "]";
	}

	public int getIndex() {
		return index;
	}

	public byte getDirection() {
		return direction;
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}
}
