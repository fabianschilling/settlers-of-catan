package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

/**
 * Speichert den Zustand der Insel.
 * 
 * @author Michael Strobl, Fabian Schilling , Thomas Wimmer, Florian Weiss, Christian Hemauer, Elisabeth Friedrich
 * 
 */
public class IslandOfCatan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ein Knoten-Array mit den vorhandenen Knoten.
	 */
	private Node[] nodes;
	/**
	 * Ein Hafen-Vector mit den vorhandenen H&auml;fen.
	 */
	private Vector<Byte> harbors;
	/**
	 * Array f&uuml;r alle Stra&szlig;en.
	 */
	private Road[] roads;

	/**
	 * <code>Tiles</code>-Matrix.
	 */
	private Tile[][] tiles;
	/**
	 * Ein Spieler-Array mit den Spielern der aktuellen Partie.
	 */
	private Settler[] settlers;
	/**
	 * Eine Variable f&uuml;r den Spieler, der gerade an der Reihe ist.
	 */
	private Settler currentPlayer;

	/**
	 * Die Bank, welche die Ereigniskarten verwaltet.
	 */
	private Bank bank;
	/**
	 * Das Hexagon, dass vom R&auml;uber besetzt ist.
	 */
	private Tile robberTile;
	/**
	 * Die Anzahl der Spieler.
	 */
	private int playerCount;

	/**
	 * Variable f&uuml;r den Spieler mit der Karte f&uuml;r die
	 * gr&ouml;&szlig;te Rittermacht.
	 */
	private int armyHolderID;
	
	/**
	 * Variable f&uuml;r den letzten Spieler mit der Karte f&uuml;r die
	 * gr&ouml;&szlig;te Rittermacht.
	 */
	private int prevArmyHolderID;

	/**
	 * Cheats freigeschalten.
	 */
	private boolean allowedToCheat;

	/**
	 * Ein-Dimensionales <code>Tile</code>-Array
	 */
	private Tile[] tileArray1D;

	/**
	 * Anzahl der bereits gezogenen Karten.
	 */
	private int drawnDevCard;
	
	private boolean test;
	
	

	public IslandOfCatan(int playerCount, boolean test) {
		this.playerCount = playerCount;
		this.test = test;
		bank = new Bank();
		init();
	}

	/**
	 * Initialisiert die Spielwelt.
	 */
	public void init() {
		armyHolderID = -1;
		prevArmyHolderID = -1;
		drawnDevCard = 0;
		settlers = new Settler[playerCount];
		nodes = new Node[126];
		roads = new Road[174];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i);
		}

		byte[][] island = {
				{ Constants.WATER, Constants.HARBOR, Constants.WATER,
						Constants.HARBOR, Constants.WATER, Constants.WATER,
						Constants.WATER },
				{ Constants.WATER, Constants.WATER, 0, 1, 2, Constants.HARBOR,
						Constants.WATER },
				{ Constants.HARBOR, 3, 4, 5, 6, Constants.WATER,
						Constants.WATER },
				{ Constants.WATER, 7, 8, 9, 10, 11, Constants.HARBOR },
				{ Constants.HARBOR, 12, 13, 14, 15, Constants.WATER,
						Constants.WATER },
				{ Constants.WATER, Constants.WATER, 16, 17, 18,
						Constants.HARBOR, Constants.WATER },
				{ Constants.WATER, Constants.HARBOR, Constants.WATER,
						Constants.HARBOR, Constants.WATER, Constants.WATER,
						Constants.WATER } };

		Vector<Tile> tilesVector = new Vector<Tile>();
		int[] chits = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };
		Random generator = new Random();
		int cornerIndex = generator.nextInt(6);
		int[][] tileIndices = {	{ 11, 10, 9, 15, 22, 29, 37, 38, 39, 32, 26, 18, 17, 16, 23, 30, 31, 25, 24 },
								{ 9, 15, 22, 29, 37, 38, 39, 32, 26, 18, 11, 10, 16, 23, 30, 31, 25, 17, 24 },
								{ 22, 29, 37, 38, 39, 32, 26, 18, 11, 10, 9, 15, 23, 30, 31, 25, 17, 16, 24 },
								{ 37, 38, 39, 32, 26, 18, 11, 10, 9, 15, 22, 29, 30, 31, 25, 17, 16, 23, 24 },
								{ 39, 32, 26, 18, 11, 10, 9, 15, 22, 29, 37, 38, 31, 25, 17, 16, 23, 30, 24 },
								{ 26, 18, 11, 10, 9, 15, 22, 29, 37, 38, 39, 32, 25, 17, 16, 23, 30, 31, 24 } };

		for (int i = 0; i < 4; i++) {
			tilesVector.add(new Tile(Constants.WOOL));
			tilesVector.add(new Tile(Constants.LUMBER));
			tilesVector.add(new Tile(Constants.GRAIN));
		}

		for (int i = 0; i < 3; i++) {
			tilesVector.add(new Tile(Constants.BRICK));
			tilesVector.add(new Tile(Constants.ORE));
		}
		tilesVector.add(new Tile(Constants.DESERT));

		Collections.shuffle(tilesVector);
		
		tiles = new Tile[7][7];
		tileArray1D = new Tile[tiles[0].length * tiles.length];

		harbors = new Vector<Byte>();

		for (int i = 0; i < 4; i++) {
			harbors.add(Constants.HARBOR);
		}
		harbors.add(Constants.BRICKHARBOR);
		harbors.add(Constants.GRAINHARBOR);
		harbors.add(Constants.LUMBERHARBOR);
		harbors.add(Constants.WOOLHARBOR);
		harbors.add(Constants.OREHARBOR);
		Collections.shuffle(harbors);

		int harborCounter = 0;
		int nodeCounter = 1;
		for (int y = 0; y < tiles.length; y++) {
			if (y % 2 == 1 && nodeCounter != 1) {
				nodeCounter += 1;
			} else if (nodeCounter != 1) {
				nodeCounter += 3;
			}
			for (int x = 0; x < tiles[0].length; x++) {
				if (island[y][x] == Constants.WATER) {
					tiles[y][x] = new Tile(Constants.WATER);
					tiles[y][x].setNodes(getNodesArray(nodeCounter));
					for (int n = 0; n < 6; n++) {
						tiles[y][x].getNodes()[n].setWaterNode(true);
					}
				} else if (island[y][x] == Constants.HARBOR) {
					tiles[y][x] = new Tile(harbors.elementAt(harborCounter));
					harborCounter++;
					tiles[y][x].setNodes(getNodesArray(nodeCounter));
				} else if (island[y][x] == Constants.DESERT) {
					tiles[y][x].setRobber(true);
					setRobberTile(tiles[y][x]);
				} else {
					tiles[y][x] = tilesVector.elementAt(island[y][x]);
					tiles[y][x].setNodes(getNodesArray(nodeCounter));
				}
				nodeCounter += 2;
			}
		}
		
		initNeighborNodes();

		addHarbors();
		initTileArray1D();
		int chitIndex = 0;
		for(int i = 0; i < tileIndices[0].length; i++) {
			if(tileArray1D[tileIndices[cornerIndex][i]].getResource() != Constants.DESERT) {
				tileArray1D[tileIndices[cornerIndex][i]].setChitNumber(chits[chitIndex]);
				chitIndex++;
			}
		}
		initRoads();
		desertRobber();
		initCoastalNodes();
		initTilesToNodes();
	}

	/**
	 * Initialisiert die <code>Nodes</codes> an der K&uuml;ste.
	 */
	private void initCoastalNodes() {
		for (int i = 0; i < tiles[0].length; i++) {
			this.getNodes()[19 + i].setCoastalNode(true);
			this.getNodes()[99 + i].setCoastalNode(true);
		}
		for (int i = 0; i < 2; i++) {
			this.getNodes()[34 + i].setCoastalNode(true);
			this.getNodes()[41 + i].setCoastalNode(true);
			this.getNodes()[49 + i].setCoastalNode(true);
			this.getNodes()[58 + i].setCoastalNode(true);
			this.getNodes()[65 + i].setCoastalNode(true);
			this.getNodes()[74 + i].setCoastalNode(true);
			this.getNodes()[82 + i].setCoastalNode(true);
			this.getNodes()[89 + i].setCoastalNode(true);
		}

	}
	/**
	 * F&uuml;gt jedem <code>Nodes</code>, der nicht im Wasser liegt, die entsprechenden anliegenden <code>Tiles</code> hinzu.
	 */
	private void initTilesToNodes() {
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				if(tiles[i][j].getResource() != Constants.WATER)
					tiles[i][j].addToNodes();
			}
		}
	}

	/**
	 * Initialisiert das Ein-Dimensionale <code>Tile</code>-Array
	 */
	public void initTileArray1D() {
		int k = 0;
		for (int j = 0; j < tiles.length; j++) {
			for (int i = 0; i < tiles[0].length; i++) {
				tileArray1D[k] = tiles[j][i];
				tileArray1D[k].setIndex(k);
				k++;
			}
		}
	}

	/**
	 * Initialisiert die Stra&szlig;en.
	 */
	private void initRoads() {
		int roadCounter = 0;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].getNeighborNodes().size(); j++) {
				if (nodes[i].getNeighborNodes().get(j).getIndex() > i) {
					roads[roadCounter] = new Road(Constants.NOBODY, nodes[i],
							nodes[i].getNeighborNodes().get(j), roadCounter);
					roadCounter++;
				}
			}
		}

	}

	/**
	 * Gibt die Nachbar-<code>Nodes</code> aus dem <code>Nodes</code>-Array
	 * zur&uuml;ck.
	 * 
	 * @param index
	 *            <code>Node</code>
	 * 
	 * @return Nachbar-<code>Nodes</code>
	 */
	private Node[] getNodesArray(int index) {

		Node[] neighbors;

		if (index > 96) {
			Node[] neighborNodes = { nodes[index], nodes[index + 1],
					nodes[index + 16], nodes[index + 15], nodes[index + 14],
					nodes[index - 1] };
			neighbors = neighborNodes;
		} else {
			Node[] neighborNodes = { nodes[index], nodes[index + 1],
					nodes[index + 17], nodes[index + 16], nodes[index + 15],
					nodes[index - 1] };
			neighbors = neighborNodes;
		}
		return neighbors;
	}

	/**
	 * Initialisiert die Nachbarknoten jedes Knoten.
	 */
	private void initNeighborNodes() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				for (int i = 0; i < tiles[x][y].getNodes().length; i++) {
					if (i == 0) {
						insertNeighborNode(x, y, i,
								(tiles[x][y].getNodes().length - 1));
						insertNeighborNode(x, y, i, i + 1);
					} else if (i == 5) {
						insertNeighborNode(x, y, i, i - 1);
						insertNeighborNode(x, y, i, 0);
					} else {
						insertNeighborNode(x, y, i, i - 1);
						insertNeighborNode(x, y, i, i + 1);
					}
				}
			}
		}
		for (int i = 0; i < nodes.length; i++) {
			Vector<Node> tempNeighborNodesVector = nodes[i].getNeighborNodes();
			Collections.sort(tempNeighborNodesVector, new IndexComparator());
		}
	}

	/**
	 * F&uuml;gt Knoten in Nachbarknotenarray ein, falls dieser noch nicht
	 * enthalten ist.
	 * 
	 * @param x
	 *            x-Wert des aktuellen Tiles
	 * 
	 * @param y
	 *            y-Wert des aktuellen Tileknotens
	 * 
	 * @param index1
	 *            Index des aktuellen Knotens
	 * 
	 * @param index2
	 *            Index des Nachbarknotens
	 * 
	 */
	private void insertNeighborNode(int x, int y, int index1, int index2) {
		Node[] tempNodeArray = tiles[x][y].getNodes();
		Vector<Node> tempNeighborNodesVector = tempNodeArray[index1]
				.getNeighborNodes();
		Node tempNeighborNode = tempNodeArray[index2];

		if (!tempNeighborNodesVector.contains(tempNeighborNode)) {
			tempNeighborNodesVector.add(tempNeighborNode);
		}
	}

	/**
	 * Die Methode f&uuml;gt der Spielwelt die H&auml;fen hinzu.
	 */
	private void addHarbors() {
		int index = 0;

		nodes[19].setHarbor(harbors.get(index));
		nodes[20].setHarbor(harbors.get(index));
		index++;
		nodes[22].setHarbor(harbors.get(index));
		nodes[23].setHarbor(harbors.get(index));
		index++;
		nodes[41].setHarbor(harbors.get(index));
		nodes[42].setHarbor(harbors.get(index));
		index++;
		nodes[34].setHarbor(harbors.get(index));
		nodes[50].setHarbor(harbors.get(index));
		index++;
		nodes[59].setHarbor(harbors.get(index));
		nodes[75].setHarbor(harbors.get(index));
		index++;
		nodes[66].setHarbor(harbors.get(index));
		nodes[82].setHarbor(harbors.get(index));
		index++;
		nodes[89].setHarbor(harbors.get(index));
		nodes[105].setHarbor(harbors.get(index));
		index++;
		nodes[99].setHarbor(harbors.get(index));
		nodes[100].setHarbor(harbors.get(index));
		index++;
		nodes[102].setHarbor(harbors.get(index));
		nodes[103].setHarbor(harbors.get(index));

	}

	/**
	 * Gibt die Rohstoffe zur&uuml;ck die nach dem W&uuml;rfeln verteilt wurden.
	 * 
	 * @param pips
	 *            Augenzahl
	 * @return Rohstoffe jedes Spielers
	 */
	public int[][] distribute(int pips) {

		/*
		 * Resources[][] x = player (0, 1, 2, 3) y = resource ( grain, ore,
		 * lumber, wool, brick )
		 */
		int[][] resources = new int[playerCount][5];
		for (int x = 0; x < resources.length; x++) {
			for (int y = 0; y < resources[0].length; y++) {
				resources[x][y] = 0;
			}
		}

		int count = 0;
		int maxCount = 2;
		Node[] nodeArray;

		if (pips == 2 || pips == 12)
			maxCount = 1;

		FIRSTLOOP: for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (count == maxCount)
					break FIRSTLOOP;
				if (tiles[y][x].getChitNumber() == pips) {
					count++;
					if (!tiles[y][x].getRobber()) {
						nodeArray = tiles[y][x].getNodes();
						int resource = tiles[y][x].getResource();
						for (int i = 0; i < nodeArray.length; i++) {
							int playerIndex = nodeArray[i].getOwnerID();
							if (playerIndex != Constants.NOBODY) {
								int amount = 1;
								if (nodeArray[i].getBuilding() == Constants.CITY) {
									amount = 2;
								}
								switch (resource) {
								case Constants.GRAIN:
									settlers[playerIndex].addGrain(amount);
									resources[playerIndex][0] += amount;
									break;
								case Constants.ORE:
									settlers[playerIndex].addOre(amount);
									resources[playerIndex][1] += amount;
									break;
								case Constants.LUMBER:
									settlers[playerIndex].addLumber(amount);
									resources[playerIndex][2] += amount;
									break;
								case Constants.WOOL:
									settlers[playerIndex].addWool(amount);
									resources[playerIndex][3] += amount;
									break;
								case Constants.BRICK:
									settlers[playerIndex].addBrick(amount);
									resources[playerIndex][4] += amount;
									break;
								default:
									break;
								}
							}
						}
					}
				}
			}
		}

		return resources;
	}
	
	public void addDrawnDevCard() {
		drawnDevCard++;
	}
	
	public int getDrawnDevCard() {
		return drawnDevCard;
	}

	/**
	 * Vergleicht die Gewinnpunkte. &Uuml;berpr&uuml;ft ob ein Spieler 10
	 * Siegpunkte hat, und somit die aktuelle Partie gewinnt.
	 */
	public void compareScores() {
		for (int i = 0; i < settlers.length; i++) {
			if (settlers[i].getScore() == 10) {
			}
		}
	}

	/**
	 * Testet ob der Spieler die Anzahl der Siegpunkte erreicht hat, die zum
	 * gewinnen notwendig sind.
	 * 
	 * @param id
	 *            ID des jeweiligen Spieler
	 */
	public void checkScore(int id) {
		if (settlers[id].getScore() >= 10) {
		}
	}

	/**
	 * Setzt die Variable <code>currentPlayer</code>. Der aktuelle Spieler wird
	 * auf den Spieler gesetzt, der als n&auml;chstes an der Reihe ist.
	 */
	public void nextPlayer() {
		currentPlayer = settlers[(currentPlayer.getID() + 1) % settlers.length];
	}

	/**
	 * Mischt das &uuml;bergebene Array.
	 * 
	 * @param array
	 *            Das zu mischende Array
	 */
	public static void shuffle(int[] array) {

		int temp;
		int random;
		Random r = new Random();
		for (int i = array.length - 1; i >= 0; i--) {
			random = r.nextInt(array.length);
			temp = array[i];
			array[i] = array[random];
			array[random] = temp;
		}
	}

	/**
	 * Verschiebt den R&auml;ber auf ein anderes <code>Tile</code>.
	 * 
	 * @param tile
	 *            Ziel-<code>Tile</code>
	 */
	public void moveRobber(Tile tile) {
		robberTile.setRobber(false);
		tileArray1D[tile.getIndex()].setRobber(true);
		robberTile = tileArray1D[tile.getIndex()];
	}

	/**
	 * Gibt die anliegenden besiedelten Knoten in einem Vector zur&uuml;ck.
	 * 
	 * @param position_X
	 *            x-Koordinate der Feldkarte
	 * @param position_Y
	 *            y-Koordinate der Feldkarte
	 * 
	 * @return Ein Vector mit den Knoten, die besiedelt sind
	 */
	public Vector<Node> findSettledNeighbourNodes(int position_X, int position_Y) {
		Node[] neighbourNodes = tiles[position_Y][position_X].getNodes();
		Vector<Node> settledNeighbourNodes = new Vector<Node>();
		for (int i = 0; i < neighbourNodes.length; i++) {
			if (neighbourNodes[i].getOwnerID() != Constants.UNBUILT) {
				settledNeighbourNodes.add(neighbourNodes[i]);
			}
		}

		return settledNeighbourNodes;
	}

	/**
	 * &Auml;ndert einen Rohstoff eines bestimmten Spieler um einen bestimmten
	 * Wert.
	 * 
	 * @param resource
	 *            Der jeweilige Rohstoff
	 * @param amount
	 *            &Aum;nderungswert
	 * @param id
	 *            ID des Spielers
	 */
	public void changeAmountOfResource(int resource, int amount, int id) {
		switch (resource) {
		case (Constants.WOOL): {
			settlers[id].setWool((settlers[id].getWool() + amount));
		}
			break;
		case (Constants.ORE): {
			settlers[id].setOre((settlers[id].getOre() + amount));
		}

			break;
		case (Constants.LUMBER): {
			settlers[id].setLumber((settlers[id].getLumber() + amount));
		}
			break;
		case (Constants.GRAIN): {
			settlers[id].setGrain((settlers[id].getGrain() + amount));
		}
			break;
		case (Constants.BRICK): {
			settlers[id].setBrick((settlers[id].getBrick() + amount));
		}
			break;

		default:
			break;
		}
	}

	/**
	 * Gibt die Anzahl eines bestimmten Rohstoffs zur&uuml;ck.
	 * 
	 * @param resource
	 *            der gew&uuml;nschte Rohstoff
	 * @param id
	 *            ID des Spielers
	 * @return ist die Anzahl der Karten, die der Spieler von diesem Rohstoff
	 *         auf der Hand hat.
	 */
	public int getResource(int resource, int id) {
		if (resource == Constants.BRICK) {
			return settlers[id].getBrick();
		} else if (resource == Constants.GRAIN) {
			return settlers[id].getLumber();
		} else if (resource == Constants.LUMBER) {
			return settlers[id].getLumber();
		} else if (resource == Constants.ORE) {
			return settlers[id].getOre();
		} else
			return settlers[id].getWool();
	}

	/**
	 * Berechnet die L&auml;nge der Handelsstra&szlig;e f&uuml;r den Spieler mit der
	 * &uuml;bergebenen ID.
	 * 
	 * @param id
	 *            id des Spielers, der gerade eine Stra&szlig;e gebaut hat
	 * @return Gibt die L&auml;nge der Handelsstra&szlig; zur&uuml;ck
	 */
	public int calculateRoadLength(int id) {
		int roadsCount = 0;
		for (int i = 0; i < roads.length; i++) {
			if (roads[i].getOwner() == id && !roads[i].isChecked()) {
				roads[i].setChecked(true);
				int newRoadsCount = 1;
				if (roads[i].getEnd().getOwnerID() == id
						|| roads[i].getEnd().getOwnerID() == Constants.NOBODY) {
					newRoadsCount += countRoads(roads[i].getEnd(), id);
				}
				if (roads[i].getStart().getOwnerID() == id
						|| roads[i].getStart().getOwnerID() == Constants.NOBODY) {
					newRoadsCount += countRoads(roads[i].getStart(), id);
				}
				if (roadsCount < newRoadsCount) {
					roadsCount = newRoadsCount;
				}
			}
			for (int j = 0; j < roads.length; j++) {
				roads[j].setChecked(false);
			}
		}
		return roadsCount;
	}

	/**
	 * Berechnet die Anzahl der anh&auml;ngenden Stra&szlig;en f&uuml;r den
	 * gegebenen Knoten.
	 * 
	 * @param node
	 *            Knoten f&uuml;r den die Anzahl der anh&auml;ngenden
	 *            Stra&szlig;en berechnet wird.
	 * @param id
	 *            id des Spielers, der gerade eine Stra&szlig;e gebaut hat
	 * @return Anzahl der anh&auml;ngenden Stra&szlig;en
	 */
	private int countRoads(Node node, int id) {
		int roadsCount = 0;
		for (int i = 0; i < roads.length; i++) {
			Node newNode = null;
			if (roads[i].getStart() == node) {
				newNode = roads[i].getEnd();
			} else if (roads[i].getEnd() == node) {
				newNode = roads[i].getStart();
			}
			if (newNode != null && !roads[i].isChecked()
					&& roads[i].getOwner() == id) {
				if (newNode.getOwnerID() == id
						|| newNode.getOwnerID() == Constants.NOBODY) {
					roads[i].setChecked(true);
					int newRoadsCount = 1;
					newRoadsCount += countRoads(newNode, id);
					if (roadsCount < newRoadsCount) {
						roadsCount = newRoadsCount;
					} else {
						roads[i].setChecked(false);
					}
				}
			}
		}
		return roadsCount;
	}

	/**
	 * Berechnet den Besitzer der l&auml;ngsten Handelsstra&szlig;e.
	 * 
	 * @param currentSettler
	 *            <code>Settler</code>
	 * @return ID des Besitzers
	 */
	public int ownerOfLongestRoad(Settler currentSettler) {
		int settlersWithShorterRoads = 0;
		for (int i = 0; i < getSettlers().length; i++) {
			if (getSettler(i).getLongestRoadLength() < currentSettler.getLongestRoadLength()) {
				settlersWithShorterRoads++;
			}
		}
		int owner = -1;
		if (settlersWithShorterRoads == (getSettlers().length - 1)
				&& currentSettler.getLongestRoadLength() >= 5
				&& !currentSettler.isLongestRoad()) {
			owner = currentSettler.getID();
			for (int i = 0; i < getSettlers().length; i++) {
				if (getSettler(i) == currentSettler) {
					getSettler(i).setLongestRoad(true);
				} else {
					getSettler(i).setLongestRoad(false);
				}
			}
		}
		return owner;
	}

	/**
	 * Berechnet und gibt die m�glichen Opfer beim R&auml;ber verschieben
	 * zur&uuml;ck.
	 * 
	 * @param robberTile
	 * @return
	 */
	public ArrayList<Integer> getRobberVictims(Tile robberTile) {
		ArrayList<Integer> playerIDs = new ArrayList<Integer>();
		Node[] nodes = robberTile.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getOwnerID() != Constants.NOBODY
					&& !playerIDs.contains(nodes[i].getOwnerID())
					&& nodes[i].getOwnerID() != getCurrentPlayer().getID()) {
				playerIDs.add(nodes[i].getOwnerID());
			}
		}
		return playerIDs;
	}

	/**
	 * Gibt die ID des Besitzers der gr&ouml;&szlig;ten Rittermacht zur&uuml;ck.
	 * 
	 * @return ID des Besitzers
	 */
	public int getLargestArmyHolderID() {
		int army = 0;
		int id = getArmyHolderID();
		if (id > 0)
			army = settlers[id].getArmyCount();
		for (int i = 0; i < settlers.length; i++) {
			int tempArmy = settlers[i].getArmyCount();
			if (tempArmy >= Constants.MIN_ARMY_NUM && tempArmy > army) {
				army = tempArmy;
				id = i;
			}
			settlers[i].setLargestArmy(false);
		}
		if (id > 0) {
			settlers[id].setLargestArmy(true);
			setArmyHolder(id);
		}
		return id;
	}

	public void setArmyHolder(int armyHolderID) {
		this.armyHolderID = armyHolderID;
	}
	
	public void setPreviousArmyHolderID(int prevArmyHolderID) {
		this.prevArmyHolderID = prevArmyHolderID;
	}

	public int getArmyHolderID() {
		return armyHolderID;
	}
	
	public int getPreviousArmyHolderID() {
		return prevArmyHolderID;
	}

	public Road[] getRoads() {
		return roads;
	}

	public Settler getSettler(int index) {
		return settlers[index];
	}

	public Settler[] getSettlers() {
		return settlers;
	}

	public void setSettlers(Settler[] settlers) {
		this.settlers = settlers;
	}

	public Settler getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Settler currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Bank getBank() {
		return bank;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Tile getTiles(int x, int y) {
		return tiles[x][y];
	}

	public Vector<Byte> getHarbors() {
		return harbors;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public Tile getRobberTile() {
		return robberTile;
	}

	public void setRobberTile(Tile robberTile) {
		this.robberTile = robberTile;
	}

	/**
	 * Verschiebt den R&auml;ber auf die W&uuml;ste.
	 */
	public void desertRobber() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y].getResource() == Constants.DESERT) {
					setRobberTile(tiles[x][y]);
				}
			}
		}
	}

	public boolean isAllowedToCheat() {
		return allowedToCheat;
	}

	public void setAllowedToCheat(boolean allowedToCheat) {
		this.allowedToCheat = allowedToCheat;
	}

	public boolean isTest() {
		return test;
	}

	public Tile[] getTileArray1D() {
		return tileArray1D;
	}

	public Tile getTileArray1DOfIndex(int index) {
		return tileArray1D[index];
	}

	/**
	 * Klasse zum Vergleichen der Knoten. Diese Klasse wird verwendet um die
	 * Indizes der Knotes zu vergleichen und danach zu sortieren.
	 * 
	 * @author Michael Strobl, Florian Weiss
	 * 
	 */
	class IndexComparator implements Comparator<Object> {

		@Override
		public int compare(Object t, Object t1) {

			Node node1 = (Node) t;
			Node node2 = (Node) t1;

			Integer index1 = new Integer(node1.getIndex());
			Integer index2 = new Integer(node2.getIndex());

			return (index2.compareTo(index1));
		}
	}
}
