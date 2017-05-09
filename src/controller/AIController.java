package controller;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import networking.AIClient;
import networking.ClientInterface;
import networking.ClientThread;
import networking.ExceptionLogger;
import networking.Message;
import model.Constants;
import model.IslandOfCatan;
import model.Node;
import model.Road;
import model.Settler;
import model.Tile;

/**
 * Controller des <code>AIClient</code>
 * 
 * @author Michael Strobl, Thomas Wimmer
 * 
 */
public class AIController implements ControllerInterface {

	/**
	 * Client des Spielers
	 */
	private ClientInterface client;
	/**
	 * Geklickter Knoten
	 */
	private Node clickedNode;
	/**
	 * Knoten, auf dem die letzte Siedlung gebaut wurde.
	 */
	private Node lastSettlementNode;
	/** 
	 * geklickte Stra&zlig;e
	 */

	/**
	 * Insel
	 */
	private IslandOfCatan island;

	/**
	 * Socket f&uuml;r die Verbindung zum Server.
	 */
	private Socket socket;

	/**
	 * Logger, der auftretende Exceptions protokolliert
	 */
	private ExceptionLogger exceptionLogger;

	/** 
	 * Blockiert die GUI, wenn gerade geklaut wird.
	 */
	private boolean isRobbing;
	/** 
	 * Rohstoffkarte wird gerade gespielt.
	 */
	private boolean isResourceCard;
	/** 
	 * Monopolkarte wird gerade gespielt.
	 */
	private boolean isMonopolyCard;
	/** 
	 * R&auml;uber ist gerade aktiv.
	 */
	private boolean isRobberActivated;
	/** 
	 * Wenn gerade abgeworfen wird.
	 */
	private boolean discarding;
	/** 
	 * Wenn gerade gebaut wurde.
	 */
	private boolean built = false;
	/** 
	 * Knoten, auf denen man Siedlungen bauen k&ouml;nnte.
	 */
	private int[] possibleSettlement;

	/** 
	 * Anzahl der Stra&szlig;en, die umsonst gebaut werden d&uuml;rfen.
	 */
	private int roadCardCount;
	/** 
	 * Anzahl an Rohstoffen, die abgeworfen werden m&uuml;ssen.
	 */
	private int discardAmount;
	/** 
	 * Wenn es erlaubt ist zu w&uuml;rfeln.
	 */
	private boolean allowedToRoll = false;

	/**
	 * Spieler ist gerade am Handeln.
	 */
	private boolean choosing;
	/** 
	 * Wahr, wenn es sich um einen Handel handelt
	 */
	private boolean trading;
	/** 
	 * Angebotene Rohstoffe im Handel.
	 */
	private int[] offR;
	/** 
	 * Gesuchte Rohstoffe im handel.
	 */
	private int[] expR;
	
	/**
	 * Karte wurde ausgespielt.
	 */
	private boolean cardPlayed;
	
	/**
	 * Nachricht wurde versandt.
	 */
	private boolean messageIsSent;
	/** 
	 * Variable f&uuml;r das Spielende
	 */
	private boolean gameEnd;
	
	/**
	 * Zuletzt gebaute Stra&szlig;e.
	 */
	private Road lastRoad;
	
	/**
	 * Zuletzt gebaute Stra&szlig; ist die zweite Stra&szlig;e.
	 */
	private boolean lastRoadIsSecondRoad;
	
	/**
	 * Bis jetzt wurde noch nicht gew&uuml;rfelt.
	 */
	private boolean notRolledYet;
	
	/**
	 * Enth&auml;lt die Rohstoffe der ersten Siedlung.
	 */
	private Vector<Byte> resources;
	
	/**
	 * Anfangsphase
	 */
	private boolean first;
	
	
	/** 
	 * Erzeugt ein Objekt der Klasse AIController
	 * @param client
	 * 			ist ein Client aus dem Networking-Package
	 */
	public AIController(AIClient client, Socket socket) {
		this.client = client;
		this.socket = socket;
		messageIsSent = false;
		gameEnd = false;
		first = true;
		lastRoadIsSecondRoad = true;
		resources = new Vector<Byte>();
		//LoggerClass logger = thread.getClientLogger();
		ClientThread thread = new ClientThread(client, this.socket, this);
		thread.start();
		exceptionLogger = ExceptionLogger.getInstance();
	}

	public void appendMessage(String message) {
		
	}

	public void appendMessage(String message, Color c) {}

	public void refresh() {}

	private void buildSettlement() {
		lastSettlementNode = clickedNode;
		Message message = new Message(clickedNode, true, null);
		sendMessage(message);
	}
	/**
	 * Gibt die Node zur&uuml;ck, wo die letzte Siedlung gebaut wurde
	 * 
	 * @return
	 */
	public Node getLastSettlementNode() {
		return lastSettlementNode;
	}
	/**
	 * Eine Stra&szlig;e wird gebaut
	 */
	private void buildCity() {}
	/**
	 * Baut eine Stra\u00DFe auf der Insel (GUI).
	 */
	private void buildStreet() {}
	/**
	 * Pr&uuml;ft, ob der Stra&szlig;enbau erlaubt ist
	 * 
	 * @return wahr, wenn man Roads bauen kann
	 */
	private boolean buildStreetIsAllowed() {
		return false;
	}

	/**
	 * Gibt zur&uuml;ck ob die &uuml;bergebene Stra&szlig;e erlaubt ist.
	 * @param road Stra&szlig;e
	 * @return true - wenn gebaut werden kann
	 */
	private boolean buildStreetIsAllowed(Road road) {
		if (!client.isBeginning()
				&& road.isBuildable(island, client.getSettler().getID())) {
			return true;
		} else if (client.isBeginning()
				&& (road.getEnd() == getLastSettlementNode() || road.getStart() == getLastSettlementNode())) {
			return true;
		} else
			return false;
	}

	/**
	 * Gibt zur&uuml;ck ob die &uuml;bergebene Stra&szlig;e am Wasser liegt.
	 * @param road Stra&szlig;e
	 * @return true - wenn sie am Wasser liegt
	 */
	public boolean isWaterStreet(Road road) {
		if ((road.getStart().isWaterNode() && !road.getEnd().isCoastalNode())
				|| (road.getEnd().isWaterNode() && !road.getStart()
						.isCoastalNode())) {
			return true;
		}
		return false;
	}

	/**
	 * Schickt eine Nachricht an den Server.
	 * @param message Nachricht
	 */
	public void sendMessage(Message message) {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIController.AIControllerIOException.FehlerNachrichtSenden")); //$NON-NLS-1$
		}
	}

	public IslandOfCatan getIsland() {
		return island;
	}

	public int getPossibleSettlement(int index) {
		return possibleSettlement[index];
	}

	public void showDices(int pipOne, int pipTwo) {}

	public void hideDices() {}

	/**
	 * Verarbeitet eine gespielte Monopol- oder Rohstoffkarte weiter und w&auml;hlt die Rohstoffe aus.
	 * @param devCard Konstante f&uuml;r die Entwicklungskarte 
	 */
	public void showResources(byte devCard) {
		if(devCard == Constants.GETRESOURCES) {
			byte firstRes = -1;
			byte secondRes = -1;
			int[] missRes = new int[5];
			if(getMissingAmount(getMissingResources(Constants.SETTLEMENT)) <= 2) {
				missRes = getMissingResources(Constants.SETTLEMENT);	
			} else if(getMissingAmount(getMissingResources(Constants.CITY)) <= 2) {
				missRes = getMissingResources(Constants.CITY);
			} else if(getMissingAmount(getMissingResources(Constants.ROAD)) <= 2) {
				missRes = getMissingResources(Constants.ROAD);
			} else if(getMissingAmount(getMissingResources(Constants.DEVELOPMENTCARD)) <= 2) {
				missRes = getMissingResources(Constants.DEVELOPMENTCARD);
			}	
			for(int i = 0; i < missRes.length; i++) {
				if(missRes[i] == 2) {
					firstRes = getResourceByIndex(i);
					secondRes = firstRes;
					break;
				} else if(missRes[i] == 1) {
					if(firstRes != -1)
						firstRes = getResourceByIndex(i);
					else
						secondRes = getResourceByIndex(i);
				}
			}
			if(firstRes == -1)
				firstRes = Constants.BRICK;
			if(secondRes == -1)
				secondRes = Constants.LUMBER;
			messageIsSent = true;
			Message message = new Message(Constants.GETRESOURCES, firstRes, secondRes);
			sendMessage(message);
		}
		if(devCard == Constants.MONOPOLY) {
			int[] missRes = {-1, -1, -1, -1, -1};
			byte res = -1;
			if(getDiffMissingAmount(getMissingResources(Constants.SETTLEMENT)) == 1) {
				missRes = getMissingResources(Constants.SETTLEMENT);
			} else if(getDiffMissingAmount(getMissingResources(Constants.CITY)) == 1) {
				missRes = getMissingResources(Constants.CITY);
			} else if(getDiffMissingAmount(getMissingResources(Constants.ROAD)) == 1) {
				missRes = getMissingResources(Constants.ROAD);
			} else if(getDiffMissingAmount(getMissingResources(Constants.DEVELOPMENTCARD)) == 1) {
				missRes = getMissingResources(Constants.DEVELOPMENTCARD);
			}
			
			for(int i = 0; i < missRes.length; i++) {
				if(missRes[i] > 0)
					res = getResourceByIndex(i);
			}
			if(res == -1) {
				Random rnd = new Random();
				int num = rnd.nextInt(5);
				res = getResourceByIndex(num);
			}
			messageIsSent = true;
			Message message = new Message(Constants.MONOPOLY, res, -1, -1);
			sendMessage(message);
		}
	}
	
	//kann vergessen werden
	private Road getRoadToNextBestNode() {
		Road[] roads = island.getRoads();
		ArrayList<Node> possNodes = new ArrayList<Node>();
		for(int i = 0; i < roads.length; i++) {
			if(roads[i].getOwner() == client.getID()) {
				Vector<Node> endNextNodes = roads[i].getEnd().getNeighborNodes();
				Vector<Node> startNextNodes = roads[i].getStart().getNeighborNodes();
				
				for(int j = 0; j < endNextNodes.size(); j++) {
					if(endNextNodes.get(j).isBuildable(island, client.getID(), false) && !possNodes.contains(endNextNodes.get(j)))
						possNodes.add(endNextNodes.get(j));
				}
				for(int k = 0; k < startNextNodes.size(); k++) {
					if(startNextNodes.get(k).isBuildable(island, client.getID(), false) && !possNodes.contains(startNextNodes.get(k)))
						possNodes.add(startNextNodes.get(k));
				}
			}
		}
		
		Node bestNode = null;
		int value = 0;
		for(int i = 0; i < possNodes.size(); i++) {
			if(calculateNodeChitValue(possNodes.get(i)) > value) {
				bestNode = possNodes.get(i);
				value = calculateNodeChitValue(possNodes.get(i));
			} else if(calculateNodeChitValue(possNodes.get(i)) == value) {
				Random rnd = new Random();
				int num = rnd.nextInt();
				if(num % 2 == 1)
					bestNode = possNodes.get(i);
				value = calculateNodeChitValue(possNodes.get(i));
			}
		}

		for(int i = 0; i < bestNode.getNeighborNodes().size(); i++) {
			for(int j = 0; j < roads.length; j++) {
				if(getRoadByNodes(bestNode, roads[j].getEnd()) != null) {
					return roads[j];
				}
				if(getRoadByNodes(bestNode, roads[j].getStart()) != null) {
					return roads[j];
				}			
			}
		}
		return null;
	}
	
	/**
	 * Gibt die <code>Road</code> zur&uuml;ck die zwischen zwei gegebenen <code>Nodes</code> liegt.
	 * @param end erste <code>Node</code>
	 * @param start zweite <code>Node</code>
	 * @return Road
	 */
	private Road getRoadByNodes(Node end, Node start) {
		Road[] roads = island.getRoads();
		for(int i = 0; i < roads.length; i++) {
			if((roads[i].getEnd() == end && roads[i].getStart() == start) || (roads[i].getEnd() == start && roads[i].getStart() == end)) {
				return roads[i];
			}
		}
		return null;
	}
	
	/**
	 * Gibt die Wahrscheinlichkeit der Chips zur&uuml;ck die auf den <code>Tiles</code> einer gegebenen <code>Node</code> liegen.
	 * @param node <code>Node</code>
	 * @return Wahrscheinlichkeitswert
	 */
	private int calculateNodeChitValue(Node node) {
		int[] probabilities = {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1};
		int value = 0;
		for(int i = 0; i < node.getTiles().size(); i++) {
			if(node.getTiles().get(i).getChitNumber() > 0)
				value += probabilities[node.getTiles().get(i).getChitNumber() - 2];
		}
		return value;
	}
	
	/**
	 * Gibt die Konstante f&uuml;r einen Rohstoff zur&uuml;ck.
	 * @param index 0 - Wolle; 1 - Erz; 2 - Lehm; 3 - Holz; 4 - Getreide
	 * @return Konstante f&uuml;r entsprechenden Rohstoff
	 */
	private byte getResourceByIndex(int index) {
		switch(index) {
		case 0:
			return Constants.WOOL;
		case 1:
			return Constants.ORE;
		case 2:
			return Constants.BRICK;
		case 3:
			return Constants.LUMBER;
		case 4:
			return Constants.GRAIN;
		default:
			return -1;
		}
	}
	
	/**
	 * Verschiebt den R&auml;ber auf ein g&uuml;nstiges Feld.
	 */
	public void activateRobberMoving() {
		isRobberActivated = true;
		int[] positions = {9, 10, 11,
				        15, 16, 17, 18,
				      22, 23, 24, 25, 26,
				        29, 30, 31, 32,
				          37, 38, 39};
		int[] possNew = new int[positions.length];
		for(int i = 0; i < positions.length; i++) {
			int c = 0;
			Node[] nodes = island.getTileArray1DOfIndex(positions[i]).getNodes();
			for(int j = 0; j < nodes.length; j++) {
				if(nodes[j].getOwnerID() == client.getID() || island.getTileArray1DOfIndex(positions[i]).getRobber() 
						|| island.getTileArray1DOfIndex(positions[i]).getResource() == Constants.DESERT) {
					break;
				} else if(nodes[j].getOwnerID() != Constants.NOBODY) {
					c++;
				}
			}
			possNew[i] = c;
		}
		
		int max = 0;
		int tileNum = 0;
		for(int i = 0; i < possNew.length; i++) {
			if(possNew[i] > max) {
				max = possNew[i];
				tileNum = i;
			} else if(possNew[i] == max) {
				Random rnd = new Random();
				if(rnd.nextInt() % 2 == 0) {
					max = possNew[i];
					tileNum = i;
				}
			}
		}
		Tile newRobberTile = island.getTileArray1DOfIndex(positions[tileNum]);
		sendMessage(new Message(newRobberTile, null));
	}
	/**
	 * Aktiviert das Klauen
	 */
	public void activateRobbing(ArrayList<Integer> playerIds) {
		isRobbing = true;
		if(playerIds.size() == 1) {
			sendMessage(new Message(playerIds.get(0), (byte) -1));
		} else if(playerIds.size() == 2) {
			Settler first = island.getSettler(playerIds.get(0));
			Settler second = island.getSettler(playerIds.get(1));
			if(second.getAmountOfResources() == 0) {
				sendMessage(new Message(first.getID(), (byte) -1));
			} else if(first.getAmountOfResources() == 0) {
				sendMessage(new Message(second.getID(), (byte) -1));
			} else {
				if(compareScore(first, second) != -1) {
					sendMessage(new Message(compareScore(first, second), (byte) -1));
				} else {
					if(compareResources(first, second) != -1) {
						sendMessage(new Message(compareResources(first, second), (byte) -1));
					} else {
						sendMessage(new Message(chooseRandomSettler(first, second), (byte) -1));
					}
				}
			}
		} else {
			Settler first = island.getSettler(playerIds.get(0));
			Settler second = island.getSettler(playerIds.get(1));
			Settler third = island.getSettler(playerIds.get(2));
			int scoreValue = compareScore(first, second, third);
			switch(scoreValue) {
			case -4:
				if(compareResources(second, third) != -1)
					sendMessage(new Message(compareResources(second, third), (byte) -1));
				else
					sendMessage(new Message(chooseRandomSettler(second, third), (byte) -1));
				break;
			case -3:
				if(compareResources(first, third) != -1)
					sendMessage(new Message(compareResources(first, third), (byte) -1));
				else
					sendMessage(new Message(chooseRandomSettler(first, third), (byte) -1));
				break;
			case -2:
				if(compareResources(first, second) != -1)
					sendMessage(new Message(compareResources(first, second), (byte) -1));
				else
					sendMessage(new Message(chooseRandomSettler(first, second), (byte) -1));
				break;
			case -1:
				int resValue = compareResources(first, second, third);
				switch(resValue) {
				case -4:
					sendMessage(new Message(chooseRandomSettler(second, third), (byte) -1));
					break;
				case -3:
					sendMessage(new Message(chooseRandomSettler(first, third), (byte) -1));
					break;
				case -2:
					sendMessage(new Message(chooseRandomSettler(first, second), (byte) -1));
					break;
				case -1:
					sendMessage(new Message(chooseRandomSettler(first, second, third), (byte) -1));
					break;
				default:
					sendMessage(new Message(resValue, (byte) -1));
					break;
				}
				break;
			default:
				sendMessage(new Message(scoreValue, (byte) -1));
				break;
			}
		}
	}
	
	/**
	 * W&auml;hlt zwischen zwei <code>Settlern</code> einen zuf&auml;lligen aus. 
	 * @param first <code>Settler</code>
	 * @param second <code>Settler</code>
	 * @return <code>Settler</code>
	 */
	private int chooseRandomSettler(Settler first, Settler second) {
		Random rnd = new Random();
		if(rnd.nextInt() % 2 == 0)
			return first.getID();
		return second.getID();
	}
	
	/**
	 * W&auml;hlt zwischen drei <code>Settlern</code> einen zuf&auml;lligen aus. 
	 * @param first <code>Settler</code>
	 * @param second <code>Settler</code>
	 * @param third <code>Settler</code>
	 * @return <code>Settler</code>
	 */
	private int chooseRandomSettler(Settler first, Settler second, Settler third) {
		Random rdn = new Random();
		switch(rdn.nextInt(3)) {
		case 0:
			return first.getID();
		case 1:
			return second.getID();
		default:
			return third.getID();
		}
	}
	
	/**
	 * Vergleicht die Rohstoffe von zwei <code>Settlern</code> und gibt die ID zur&uuml;ck.
	 * @param first <code>Settler</code>
	 * @param second <code>Settler</code>
	 * @return ID des <code>Settlers</code>; -1 wenn gleich
	 */
	private int compareResources(Settler first, Settler second) {
		if(first.getAmountOfResources() == second.getAmountOfResources())
			return -1;
		else if(first.getAmountOfResources() > second.getAmountOfResources())
			return first.getID();
		return second.getID();
	}
	
	/**
	 * Vergleicht die Rohstoffe von drei <code>Settlern</code> und gibt die ID zur&uuml;ck.
	 * @param first <code>Settler</code> 
	 * @param second <code>Settler</code>
	 * @param third <code>Settler</code>
	 * @return ID des <code>Settlers</code>;
	 * 	<ul>
	 *  <li>-1 wenn alle gleich
	 *  <li>-2 wenn 1 und 2 gleich
	 *  <li>-3 wenn 1 und 3 gleich
	 *  <li>-4 wenn 2 und 3 gleich
	 *  </ul>
	 */
	private int compareResources(Settler first, Settler second, Settler third) {
		if(compareResources(first, second) == -1) {
			if(compareResources(first, third) == first.getID()) {
				//1 + 2 > 3
				return -2;
			} else if(compareResources(first, third) == third.getID()) {
				//3 > 1 + 2
				return third.getID();
			} else {
				// 1 = 2 = 3
				return -1;
			}
		} else if(compareResources(first, third) == -1) {
			if(compareResources(first, second) == first.getID()) {
				//1 + 3 > 2
				return -3;
			} else {
				// 2 > 1 + 3
				return second.getID();
			}
		} else if(compareResources(second, third) == -1) {
			if(compareResources(second, first) == second.getID()) {
				//2 + 3 > 1
				return -4;
			} else {
				return first.getID();
			}
		} else {
			return compareResources(island.getSettler(compareResources(first, second)), third);
		}		
	}

	/**
	 * Vergleicht die Siegpunktzahl von zwei <code>Settlern</code>.
	 * @param first erster <code>Settler</code>
	 * @param second zweiter <code>Settler</code>
	 * @return -1 wenn gleich, ansonsten ID des f&uuml;hrenden
	 */
	private int compareScore(Settler first, Settler second) {
		if(first.getScore() == second.getScore())
			return -1;
		else if(first.getScore() > second.getScore())
			return first.getID();
		return second.getID();
	}
	
	/**
	 * Vergleicht die Siegpunkte von drei <code>Settlern</code> und gibt die ID zur&uuml;ck.
	 * @param first <code>Settler</code> 
	 * @param second <code>Settler</code>
	 * @param third <code>Settler</code>
	 * @return ID des <code>Settlers</code>;
	 * 	<ul>
	 *  <li>-1 wenn alle gleich
	 *  <li>-2 wenn 1 und 2 gleich
	 *  <li>-3 wenn 1 und 3 gleich
	 *  <li>-4 wenn 2 und 3 gleich
	 *  </ul>
	 */
	private int compareScore(Settler first, Settler second, Settler third) {
		if(compareScore(first, second) == -1) {
			if(compareScore(first, third) == first.getID()) {
				//1 + 2 > 3
				return -2;
			} else if(compareScore(first, third) == third.getID()) {
				//3 > 1 + 2
				return third.getID();
			} else {
				// 1 = 2 = 3
				return -1;
			}
		} else if(compareScore(first, third) == -1) {
			if(compareScore(first, second) == first.getID()) {
				//1 + 3 > 2
				return -3;
			} else {
				// 2 > 1 + 3
				return second.getID();
			}
		} else if(compareScore(second, third) == -1) {
			if(compareScore(second, first) == second.getID()) {
				//2 + 3 > 1
				return -4;
			} else {
				return first.getID();
			}
		} else {
			return compareScore(island.getSettler(compareScore(first, second)), third);
		}		
	}
	
	/**
	 * Die Mehtode verursacht ein repaint() der PolygonMap.
	 */
	public void repaintMap() {}

	public void activateTradees(int[] offR,
			int[] expR) {
		choosing = true;
		this.offR = offR;
		this.expR = expR;
		// mainGUI.showTradeesPanel(playerIds);
	}

	public void activateRoadCard() {
		roadCardCount = (client.getIsland().getCurrentPlayer().getRoadCount() == 14) ? 1 : 2;
		chooseAction();
	}

	public void repaintRoads(int roadIndex, int id) {
	}

	public void block() {
	}

	public ClientInterface getClient() {
		return client;
	}

	public void setBuilt(boolean built) {
		this.built = built;
	}

	public void refreshOpponents() {
	}

	public void activateConfirmTrade(int[] offR, int[] expR) {
		trading = true;
		// mainGUI.showConfirmTradePanel(offR, expR);
	}

	public void discard(int amount) {
		// resourcePanel.showDiscard();
		discarding = true;
		discardAmount = amount;	
		sendMessage(new Message(calculateDiscarding(amount), client.getID()));
	}

	public void setAllowedToRoll(boolean allowedToRoll) {
		this.allowedToRoll = allowedToRoll;
		if (allowedToRoll) {
			notRolledYet = false;
			for(int i = 0; i < island.getRobberTile().getNodes().length; i++) {
				if(island.getRobberTile().getNodes()[i].getOwnerID() == client.getID() && client.getSettler().hasCard(Constants.KNIGHT) && !notRolledYet) {
					playCard(Constants.KNIGHT);
					notRolledYet = true;
				}	
			}
			if(!notRolledYet) {
				int firstDice = (int) Math.round((Math.random() * 6.0 + 0.5));
				int secondDice = (int) Math.round((Math.random() * 6.0 + 0.5));
				int dice = firstDice + secondDice;
				if(dice == Constants.MAX_CARDS_SEVEN && first && !client.isBeginning()) {
					setAllowedToRoll(true);
				} else {
					if(!client.isBeginning()) 
						first = false;

					notRolledYet = false;
					timer();
					sendMessage(new Message(firstDice, secondDice, client.getID()));
				}
			}
		}
	}
	
	/**
	 * Setzt die KI f&uuml;r kurze Zeit schlafend.
	 */
	private void timer() {
		long time = System.currentTimeMillis();
		long futureTime = time + 1100;
		while(time < futureTime) {
			time = System.currentTimeMillis();
		}
	}
	
	public void setAllowedToBuild(boolean allowedToBuild) {
		if(allowedToBuild) {
			Node buildPos = calculateBuildPos();
			buildSettlement(buildPos);
			int upperBoundForRandomNumber = buildPos.getRoads().size();
			Random random = new Random();
			int roadIndex = random.nextInt(upperBoundForRandomNumber);
			timer();
			buildRoad(buildPos.getRoads().get(roadIndex));
		}
	}
	
	/**
	 * Gibt den <code>Node</code> mit der h&ouml;chsten Wahrscheinlichkeit aller anliegenden Tiles zur&uuml;ck.
	 * @return <code>Node</code>
	 */
	public Node calculateBuildPos() {
		Node[] allNodes = island.getNodes();
		Node bestNode = null;
		int[] probabilities = {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1};
		int sum = 0;
		
		for(int i = 0; i < allNodes.length; i++) {
			int tempSum = 0;
			if(allNodes[i].isBuildable(island, client.getID(), true) && !allNodes[i].isBuiltOn()) {
				for(int j = 0; j < allNodes[i].getTiles().size(); j++) {
					if(allNodes[i].getTiles().get(j).getChitNumber() > 0)
						tempSum += probabilities[allNodes[i].getTiles().get(j).getChitNumber() - 2];
				}
			}
			if(tempSum > sum) {
				if(resources.size() != 0) {
					boolean oldLumber = false;
					boolean oldBrick = false;
					boolean newLumber = false;
					boolean newBrick = false;
					for(int j = 0; j < resources.size(); j++) {
						if(resources.get(j) == Constants.BRICK) {
							oldBrick = true;
						}
						if(resources.get(j) == Constants.LUMBER) {
							oldLumber = true;
						}
					}
					for(int j = 0; j < allNodes[i].getTiles().size(); j++) {
						if(allNodes[i].getTiles().get(j).getResource() == Constants.BRICK) {
							newBrick = true;
						}
						if(allNodes[i].getTiles().get(j).getResource() == Constants.LUMBER) {
							newLumber = true;
						}
					}
					if(oldLumber && oldBrick) {
						sum = tempSum;
						bestNode = allNodes[i];
					} else if (oldLumber && newBrick) {
						sum = tempSum;
						bestNode = allNodes[i];
					} else if (oldBrick && newLumber) {
						sum = tempSum;
						bestNode = allNodes[i];
					} else if (newBrick && newLumber) {
						sum = tempSum;
						bestNode = allNodes[i];
					}
				} else {
					sum = tempSum;
					bestNode = allNodes[i];
				}
			}	
		}
		for(int i = 0; i < bestNode.getTiles().size(); i++) {
			resources.add((byte)bestNode.getTiles().get(i).getResource());
		}
		
		return bestNode;
	}
	
	private boolean isNodeOnDesert(Node node) {
		for(int i = 0; i < node.getTiles().size(); i++) {
			if(node.getTiles().get(i).getResource() == Constants.DESERT)
				return true;
		}
		return false;
	}
	
	/**
	 * Gibt zur&uuml;ck ob der Spieler an der Reihe ist.
	 * @return true - wenn an der Reihe
	 */
	public boolean isTurn() {
		return client.getID() == island.getCurrentPlayer().getID();
	}

	@Override
	public void startMainGUI(IslandOfCatan island) {
		this.island = island;

	}
	
	/**
	 * W&auml;hlt die n&auml;chste Aktion der KI aus.
	 */
	public void chooseAction() {
		timer();
		
		Random random = new Random();
		int number = random.nextInt(10);
		if(!gameEnd) {
			if(client.getID() == island.getCurrentPlayer().getID() && !client.isBeginning()) {
				if(notRolledYet) {
					setAllowedToRoll(true);
				} else if(roadCardCount != 0) {
					buildRoad();
				} else if(!cardPlayed && client.getSettler().hasOtherThanVictoryCard() && number < 7) {
					for(int i = 0; i < client.getSettler().getDevelopmentCards().size(); i++) { 
						if(client.getSettler().getDevelopmentCards().get(i) != Constants.VICTORYPOINTS) {
							playCard(client.getSettler().getDevelopmentCards().get(i));
						}	
					}
				} else {
					messageIsSent = false;
					if(findSettlementNodes() != null) {
						chooseActionWithBuilding(Constants.SETTLEMENT);
					} else if (findCities() != null) {
						chooseActionWithBuilding(Constants.CITY);
					} else if (client.getSettler().canAfford(Constants.DEVELOPMENTCARD)) {
						messageIsSent = true;
						Message message = new Message(Constants.VICTORYPOINTS, true);
						sendMessage(message);
					} else if (canAffordWithTrade(Constants.DEVELOPMENTCARD)) {
						
					}
					if(!messageIsSent) {
						Message newMessage = new Message(false,
								client.isBeginning(), client.getID(), false);
						sendMessage(newMessage);
					}
				}
			}
		}
	}
	
	/**
	 * Testet ob etwas gebaut oder gekauft werden kann und f&uuml;hrt dieses aus.
	 * @param asset zu testendes Bauwerk oder Karte
	 */
	private void chooseActionWithBuilding(Byte asset) {
		if(buildBuilding()) {
			
		} else if (canAffordWithTrade(asset)) {
			
		} else if(buildRoad()) {

		} else if (client.getSettler().canAfford(Constants.DEVELOPMENTCARD)) {
			messageIsSent = true;
			Message message = new Message(Constants.VICTORYPOINTS, true);
			sendMessage(message);
		} else if (canAffordWithTrade(Constants.DEVELOPMENTCARD)) {
			
		}
	}
	
	public void gameEnd() {
		gameEnd = true;
	}
	
	/**
	 * Baut eine Stra&szlig;e, wenn m&ouml;glich.
	 * @return true wenn m&ouml;glich
	 */
	private boolean buildRoad() {
		Road road = null;
		if(roadCardCount != 0 && (road = findRoads()) != null) {
			roadCardCount--;
			messageIsSent = true;
			lastRoad = road;
			Message message = new Message(road, true, null);
			sendMessage(message);
			lastRoadIsSecondRoad = false;
			return true;
		} else if((road = findRoads()) != null && client.getSettler().canAfford(Constants.ROAD) && client.getSettler().getRoadCount() < Constants.ROADS_MAX) {
				messageIsSent = true;
				lastRoad = road;
				Message message = new Message(road, true, null);
				sendMessage(message);
				lastRoadIsSecondRoad = false;
				return true;
		} else if(canAffordWithTrade(Constants.ROAD)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Baut eine Stadt oder Siedlung, wenn m&ouml;glich.
	 * @return true wenn m&ouml;glich
	 */
	private boolean buildBuilding() {
		boolean build = false;
		Node node = null;
		if(client.getSettler().canAfford(Constants.SETTLEMENT) && client.getSettler().getSettlementCount() < 5 &&(node = findSettlementNodes()) != null) {
			messageIsSent = true;
			buildSettlement(node);
			build = true;
		} else if(client.getSettler().canAfford(Constants.CITY) && client.getSettler().getCityCount() < 4 && (node = findCities()) != null) {
			messageIsSent = true;
			buildCity(node);
			build = true;
		}
		return build;
	}
	
	private boolean captureArmyPossible() {
		int ownArmy = client.getSettler().getArmyCount();
		if(island.getArmyHolderID() != -1 && island.getArmyHolderID() != client.getID()) {
			if(island.getSettler(island.getArmyHolderID()).getArmyCount() == ownArmy && client.getSettler().hasCard(Constants.KNIGHT)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Spielt eine Entwicklungskarte.
	 * @param devCard Konstante f&uuml;r Entwicklungskarte
	 */
	private void playCard(byte devCard) {
		cardPlayed = true;
		Message message = new Message(devCard, false);
		sendMessage(message);
	}
	
	
	public void cardPlayed(byte devCard) {
		switch(devCard) {
		case Constants.KNIGHT:
			activateRobberMoving();
			break;
		}
	}
	
	/**
	 * Baut eine Siedlung.
	 * @param node zu bebauender Knoten
	 */
	private void buildSettlement(Node node) {
		Message message = new Message(node, true, null);
		sendMessage(message);
	}
	
	/**
	 * Baut eine Stadt.
	 * @param node zu bebauender Knoten
	 */
	private void buildCity(Node node) {
		Message message = new Message(node, true, null);
		sendMessage(message);
	}
	
	/**
	 * Baut eine Stra&szlig;e.
	 * @param road Stra&szlig;e
	 */
	private void buildRoad(Road road) {
		lastRoad = road;
		built = false;
		Message message = new Message(road, true, null);
		sendMessage(message);
	}
	
	/**
	 * Sucht sich alle Orte, an denen eine Stra&szlig;e gebaut werden kann
	 */
	private Road findRoads() {
		getRoadToNextBestNode();
		Vector<Road> roads = new Vector<Road>();
		for (int i = 0; i < island.getRoads().length; i++) {
			if (buildStreetIsAllowed(island.getRoads()[i]) && !isWaterStreet(island.getRoads()[i]) && island.getRoads()[i].getOwner() == Constants.NOBODY && 
					(island.getRoads()[i].getEnd() == lastRoad.getEnd() || island.getRoads()[i].getEnd() == lastRoad.getStart() 
					|| island.getRoads()[i].getStart() == lastRoad.getEnd() || island.getRoads()[i].getStart() == lastRoad.getStart()) && 
					(island.getRoads()[i].getStart().getOwnerID() == Constants.NOBODY || island.getRoads()[i].getStart().getOwnerID() == client.getSettler().getID()) &&
					(island.getRoads()[i].getEnd().getOwnerID() == Constants.NOBODY || island.getRoads()[i].getEnd().getOwnerID() == client.getSettler().getID())) {
				int roads1 = 0;
				int roads2 = 0;
				for(int j = 0; j < island.getRoads()[i].getEnd().getRoads().size(); j++) {
					if(island.getRoads()[i].getEnd().getRoads().get(j).getOwner() != Constants.NOBODY) {
						roads1++;
					}
				}
				for(int j = 0; j < island.getRoads()[i].getStart().getRoads().size(); j++) {
					if(island.getRoads()[i].getStart().getRoads().get(j).getOwner() != Constants.NOBODY) {
						roads2++;
					}
				}
				if(roads1 <= 1 && roads2 <= 1 && !lastRoadIsSecondRoad) {
					roads.add(island.getRoads()[i]);
				} else if (lastRoadIsSecondRoad && island.getRoads()[i].getEnd().getOwnerID() == Constants.NOBODY && island.getRoads()[i].getStart().getOwnerID() == Constants.NOBODY) {
					roads.add(island.getRoads()[i]);
				}
			}
		}
		if(roads.size() == 0) {
			for (int i = 0; i < island.getRoads().length; i++) {
				if (buildStreetIsAllowed(island.getRoads()[i]) && !isWaterStreet(island.getRoads()[i]) && island.getRoads()[i].getOwner() == Constants.NOBODY) {
					roads.add(island.getRoads()[i]);
				}
			}
		}
		if(roads.size() == 1) {
			return roads.get(0);
		} else {
			Random random = new Random();
			int index = random.nextInt(roads.size());
			return roads.get(index);
		}
	}
	
	/**
	 * Sucht sich alle Orte, an denen eine Siedlung gebaut werden kann.
	 * @return <code>Node</code>
	 */
	private Node findSettlementNodes() {
		for (int i = 0; i < island.getNodes().length; i++) {
			if ((island.getNodes()[i].isBuildable(island, client.getSettler()
					.getID(), client.isBeginning()) & !island.getNodes()[i]
					.isWaterNode())
					& island.getNodes()[i].getBuilding() == Constants.UNBUILT
					|| (island.getNodes()[i].isBuildable(island, client
							.getSettler().getID(), client.isBeginning())
							& island.getNodes()[i].isWaterNode()
							& island.getNodes()[i].isCoastalNode() & island
							.getNodes()[i].getBuilding() == Constants.UNBUILT)) {
				return island.getNodes()[i];
			}
		}
		return null;
	}
	
	/**
	 * Sucht sich alle Orte, an denen eine Stadt gebaut werden kann.
	 * @return <code>Node</code>
	 */
	private Node findCities() {
		for (int i = 0; i < island.getNodes().length; i++) {
			if (island.getNodes()[i].getBuilding() == Constants.SETTLEMENT
					&& island.getNodes()[i].getOwnerID() == client.getSettler()
							.getID()) {
				return island.getNodes()[i];
			}
		}
		return null;
	}

	public void refreshLongestRoadView(int settlerID) {
	}

	public void refreshLargestArmyView(int settlerID) {
	};

	public void addRoad(int index, int owner) {
	}

	public void setCardMenuVisible(boolean visible) {
	}

	public void setViewsVisible(boolean visible) {
	}


	public void setNoInteraction(boolean active) {
	}

	public boolean isTradeActivated() {
		return false;
	}

	@Override
	public void setDiscarding(boolean discarding) {
		// TODO Auto-generated method stub

	}

	@Override
	public void block(boolean block) {
		
	}

	@Override
	public void switchTradeeStatus(int settlerID, boolean isAccepted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideConfirmPanel() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Wirft zuf&auml;llig Rohstoffkarten ab.
	 * @param amount Anzahl der Rohstoffe
	 * @return Menge der einzelnen Rohstoffe (Getreide, Wolle, Holz, Erz, Lehm)
	 */
	private int[] calculateDiscarding(int amount) {
		int[] discarded = {0, 0, 0, 0, 0};
		// grain, wool, lumber, ore, brick
		Random rnd = new Random();
		int count = 0;
		while(count < amount) {
			int value = rnd.nextInt(5);
			switch(value) {
				case 0:
					if(client.getSettler().getGrain() > discarded[0]) {
						discarded[0] += 1;
						count++;
					}
					break;
				case 1:
					if(client.getSettler().getWool() > discarded[1]) {
						discarded[1] += 1;
						count++;
					}
					break;
				case 2:
					if(client.getSettler().getLumber() > discarded[2]) {
						discarded[2] += 1;
						count++;
					}
					break;
				case 3:
					if(client.getSettler().getOre() > discarded[3]) {
						discarded[3] += 1;
						count++;
					}
					break;
				case 4:
					if(client.getSettler().getBrick() > discarded[4]) {
						discarded[4] += 1;
						count++;
					}
					break;
			}
		}
		return discarded;
	}
	
	/**
	 * Handelt mit der Bank.
	 * @param asset Konstante f&uuml;r Karte oder Geb&auml;de
	 * @param missExRes fehlende und &uuml;bersch&uuml;ssige Rohstoffe
	 */
	private void bankTrade(byte asset, int[][] missExRes) {		
		int[] offRes = new int[5];
		int missing = 0;
		for(int i = 0; i < missExRes.length; i++) {
			missing += missExRes[i][0];
		}
		int amountSoFar = 0;
		for(int i = 0; i < missExRes.length; i++) {
			int rate = 4;
			if(hasHarbor(i))
				rate = 2;
			else if(hasHarbor(5))
				rate = 3;
			amountSoFar += missExRes[i][1] / rate;
			offRes[i] = (missExRes[i][1] / rate) * rate;
			if(amountSoFar == missing) {
				break;
			} else if(amountSoFar > missing) {
				while(amountSoFar > missing) {
					offRes[i] -= 1;
					amountSoFar -= 1;
				}
				break;
			}
		}

		int[] expRes = new int[5];
		for(int i = 0; i < missExRes.length; i++) {
			expRes[i] = missExRes[i][0];
		}
		messageIsSent = true;
		sendMessage(new Message(offRes, expRes));
	}
	
//	private Vector<int[]> getAllTradePossibilities(int[][] missExRes, int amountMiss) {
//		for(int i = 0; i < missExRes.length; i++) {
//			int rate = 4;
//			if(hasHarbor(i))
//				rate = 2;
//			else if(hasHarbor(5))
//				rate = 3;
//		}
//		
//	}
	
	private Vector<Byte> affordWithRes(int[] res) {
		//res -> wool ore brick lumber grain
		Vector<Byte> affordable = new Vector<Byte>();
		if(res[0] > 0 && res[2] > 0 && res[3] > 0 && res[0] > 0)
			affordable.add(Constants.SETTLEMENT);
		if(res[4] >= 2 && res[1] >= 3)
			affordable.add(Constants.CITY);
		if(res[0] > 0 && res[1] > 0 && res[4] > 0)
			affordable.add(Constants.DEVELOPMENTCARD);
		if(res[2] > 0 && res[3] > 0)
			affordable.add(Constants.ROAD);
		return affordable;
	}
	
	/**
	 * Testet ob die Karte oder das Bauwerk mit einem Handel zu erwerben ist und f&uuml;hrt den Handel durch.
	 * @param asset Konstante f&uuml;r Karte oder Geb&auml;de
	 * @return true wenn m&ouml;glich
	 */
	private boolean canAffordWithTrade(byte asset) {
		// wool, ore, lumber, brick, grain
		int[][] res = new int[5][5];
		switch(asset) {
		case Constants.SETTLEMENT:
			if(client.getSettler().getSettlementCount() == Constants.SETTLEMENTS_MAX)
				return false;
			res = getMissingAndExcessiveResources(asset);
			break;
		case Constants.CITY:
			if(client.getSettler().getCityCount() == Constants.CITIES_MAX)
				return false;
			res = getMissingAndExcessiveResources(asset);
			break;
		case Constants.ROAD:
			if(client.getSettler().getRoadCount() == Constants.ROADS_MAX)
				return false;
			res = getMissingAndExcessiveResources(asset);
			break;
		case Constants.DEVELOPMENTCARD:
			if(client.getIsland().getDrawnDevCard() == Constants.DEVCARDS_MAX)
				return false;
			res = getMissingAndExcessiveResources(asset);
			break;
		default:
			for(int i = 0; i < res.length; i++) {
				res[i][0] = 0;
				res[i][1] = 0;
			}
			break;
		}	
		int amountMiss = 0;
		int amountAfford = 0;
		for(int i = 0; i < res.length; i++) {
			amountMiss += res[i][0];
			int rate = 4;
			if(hasHarbor(i))
				rate = 2;
			else if(hasHarbor(5))
				rate = 3;
			amountAfford += res[i][1] / rate;
		}
		if(amountAfford >= amountMiss) {
			bankTrade(asset, res);
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt die fehlenden Rohstoffe f&uuml;r eine Karte oder ein Geb&auml;de zur&uuml;ck.
	 * @param asset Konstante f&uuml;r Karte oder Geb&auml;de
	 * @return Menge der einzelnen Rohstoffe (Wolle, Erz, Lehm, Holz, Getreide)
	 */
	private int[] getMissingResources(byte asset) {
		//wool, ore, brick, lumber, grain
		int[] res = new int[5];
		int[][] missEx = getMissingAndExcessiveResources(asset);
		
		for(int i = 0; i < missEx.length; i++) {
			res[i] = missEx[i][0];
		}
		
		return res;
	}
	
	/**
	 * Gibt die Anzahl der fehlenden Rohstoffe zur&uuml;ck.
	 * @param missing fehlende Rohstoffe
	 * @return Anzahl der fehlenden Rohstoffe
	 */
	private int getMissingAmount(int[] missing) {
		int amount = 0;
		for(int i = 0; i < missing.length; i++) {
			amount += missing[i];
		}
		return amount;
	}
	
	/**
	 * Gibt die Anzahl der unterschiedlichen fehlenden Rohstoffe zur&uuml;ck.
	 * @param missing fehlende Rohstoffe
	 * @return Anzahl der unterschiedlichen fehlenden Rohstoffe
	 */
	private int getDiffMissingAmount(int[] missing) {
		int amount = 0;
		for(int i = 0; i < missing.length; i++) {
			if(missing[i] != 0)
				amount++;
		}
		return amount;
	}
	
	/**
	 * Testet ob der Spieler einen Hafen hat.
	 * @param res zutestender Rohstoff
	 * @return true, wenn hat
	 */
	private boolean hasHarbor(int res) {
		ArrayList<Byte> harbors = client.getSettler().getHarbors();
		switch(res) {
		case 0:
			return harbors.contains(Constants.WOOLHARBOR);
		case 1:
			return harbors.contains(Constants.OREHARBOR);
		case 2:
			return harbors.contains(Constants.BRICKHARBOR);
		case 3:
			return harbors.contains(Constants.LUMBERHARBOR);
		case 4:
			return harbors.contains(Constants.GRAINHARBOR);
		case 5:
			return harbors.contains(Constants.HARBOR);
		}
		return false;
	}
	
	/**
	 * Gibt die fehlenden und &uuml;bersch&uuml;ssigen Rohstoffe zur&uuml;ck, wenn man eine Karte oder ein Geb&auml;de kaufen will.
	 * @param asset Konstante f&uuml;r Karte oder Geb&auml;de
	 * @return fehlende und &uuml;bersch&uuml;ssige Rohstoffe (Wolle, Erz, Lehm, Holz, Getreide)
	 */
	private int[][] getMissingAndExcessiveResources(byte asset) {
		int[][] resources = new int[5][5];
		switch(asset) {
		case Constants.SETTLEMENT:
			if(client.getSettler().getWool() == 0) {
				resources[0][0] = 1;
				resources[0][1] = 0;
			} else {
				resources[0][0] = 0;
				resources[0][1] = client.getSettler().getWool() - 1;
			}
			resources[1][0] = 0;
			resources[1][1] = client.getSettler().getOre();
			if(client.getSettler().getBrick() == 0) {
				resources[2][0] = 1;
			} else {
				resources[2][0] = 0;
				resources[2][1] = client.getSettler().getBrick() - 1;
			}
			if(client.getSettler().getLumber() == 0) {
				resources[3][0] = 1;
				resources[3][1] = 0;
			} else {
				resources[3][0] = 0;
				resources[3][1] = client.getSettler().getLumber() - 1;
			}
			if(client.getSettler().getGrain() == 0) {
				resources[4][0] = 1;
				resources[4][1] = 0;
			} else {
				resources[4][0] = 0;
				resources[4][1] = client.getSettler().getGrain() - 1;
			}
			break;
		case Constants.CITY:	
			if(client.getSettler().getGrain() < 2) {
				resources[4][0] = 2 - client.getSettler().getGrain();
				resources[4][1] = 0; 
			} else {
				resources[4][0] = 0;
				resources[4][1] = client.getSettler().getGrain() - 2;
			}
			if(client.getSettler().getOre() < 3) {
				resources[1][0] = 3 - client.getSettler().getOre();
				resources[1][1] = 0; 
			} else {
				resources[1][0] = 0;
				resources[1][1] = client.getSettler().getOre() - 3;
			}
			resources[2][0] = 0;
			resources[2][1] = client.getSettler().getBrick();
			resources[3][0] = 0;
			resources[3][1] = client.getSettler().getLumber();
			resources[0][0] = 0;
			resources[0][1] = client.getSettler().getWool();
			break;
		case Constants.ROAD:
			resources[4][0] = 0;
			resources[4][0] = client.getSettler().getGrain();
			resources[1][0] = 0;
			resources[1][1] = client.getSettler().getOre();
			if(client.getSettler().getBrick() == 0) {
				resources[2][0] = 1;
				resources[2][1] = 0; 
			} else {
				resources[2][0] = 0;
				resources[2][1] = client.getSettler().getBrick() - 1;
			}
			if(client.getSettler().getLumber() == 0) {
				resources[3][0] = 1;
				resources[3][1] = 0; 
			} else {
				resources[3][0] = 0;
				resources[3][1] = client.getSettler().getLumber() - 1;
			}
			resources[0][0] = 0;
			resources[0][1] = client.getSettler().getWool();
			break;
		case Constants.DEVELOPMENTCARD:
			if(client.getSettler().getGrain() == 0) {
				resources[4][0] = 1;
				resources[4][1] = 0; 
			} else {
				resources[4][0] = 0;
				resources[4][1] = client.getSettler().getGrain() - 1;
			}
			if(client.getSettler().getOre() == 0) {
				resources[1][0] = 1;
				resources[1][1] = 0; 
			} else {
				resources[1][0] = 0;
				resources[1][1] = client.getSettler().getOre() - 1;
			}
			resources[2][0] = 0;
			resources[2][1] = client.getSettler().getBrick();
			resources[3][0] = 0;
			resources[3][1] = client.getSettler().getLumber();
			if(client.getSettler().getWool() == 0) {
				resources[0][0] = 1;
				resources[0][1] = 0; 
			} else {
				resources[0][0] = 0;
				resources[0][1] = client.getSettler().getWool() - 1;
			}
			break;
		}
		return resources;
	}

}
