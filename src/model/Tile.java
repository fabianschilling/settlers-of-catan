package model;

import java.io.Serializable;

/**
 * Repr&auml;sentiert ein Feld auf dem Spielbrett. Diese Klasse enth&auml;lt
 * Informationen &uuml;ber die anliegenden Knoten, ihre Nummer, ihre Ressource,
 * sowie ob der R&auml;uber darauf steht oder nicht.
 * 
 * @author Christian Hemauer
 * 
 */
public class Tile implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Variable f&uuml;r die Nummer des Chips, der auf dem Feld liegt
	 */
	private int chitNumber;
	/**
	 * Konstante f&uuml;r den jeweiligen Rohstoffes des Feldes
	 */
	private int resource;
	/**
	 * Boolean f&uuml;r das Aufhalten des R&auml;bers auf dem Feld
	 */
	private boolean robber;

	/**
	 * Node-Array mit anliegenden Knoten
	 */
	private Node[] nodes;

	/**
	 * Index im Array
	 */
	private int index;

	/**
	 * Erstellt ein neues Objekt der Klasse <code>Tile</code>. Das Objekt stellt
	 * ein Feld auf dem Spielbrett dar.
	 * 
	 * @param resource
	 *            Die Konstante f&uuml;r den Rohstoff des Feldes
	 * @param number
	 *            Die Nummer des daraufliegenden Chips
	 */
	public Tile(int resource) {
		this.resource = resource;
		this.robber = false;
	}
	
	/**
	 * Fügt das Tile den entsprechenden Nodes hinzu
	 */
	public void addToNodes() {
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].getTiles().add(this);
		}
	}

	public boolean getRobber() {
		return robber;
	}

	public void setRobber(boolean robber) {
		this.robber = robber;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public Node getNodeOfIndex(int index) {
		return nodes[index];
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	public int getResource() {
		return resource;
	}

	public int getChitNumber() {
		return chitNumber;
	}
	
	public void setChitNumber(int chitNumber) {
		this.chitNumber = chitNumber;
	}

	public String toString() {
		return "[" + resource + ", " + chitNumber + ", " + robber + "]";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
