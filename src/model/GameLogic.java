package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import networking.Message;

/**
 * Enth&auml;lt die Anfangslogik und steuert die Reihenfolge der Spieler.
 * 
 * @author Michael Strobl
 * 
 */
public class GameLogic {

	/**
	 * Enth&auml;lt die Sockets zu den Clients.
	 */
	private Vector<Socket> clientVector;

	/**
	 * In der Anfangsphase oder nicht.
	 */
	private boolean beginning;

	/**
	 * Aktuelle Spieler-ID.
	 */
	private int currentID;

	/**
	 * Variable zur Inkrementierung der <code>currentID</code> in der
	 * Anfangsphase.
	 */
	private int n;

	/**
	 * Insel auf dem Server.
	 */
	private IslandOfCatan island;

	/**
	 * Anzahl der Stra&szlig;, die der letzte Spieler gebaut hat.
	 */
	private int builtRoadsOfLastPlayer = 0;

	/**
	 * W&uuml;rfelphase am Anfang oder schon weiter.
	 */
	private boolean firstRoll;

	/**
	 * Augenzahlen der Spieler beim Ausw&uuml;rfeln des Startspielers.
	 */
	private int[] firstPips;

	/**
	 * Enthält die Spieler, die w&uuml;rfeln m&uuml;ssen.
	 */
	private int[] newRollIDs;

	/**
	 * ID des letzten Spielers.
	 */
	private int lastPlayer;

	/**
	 * ID des ersten Spielers.
	 */
	private int firstPlayer;

	/**
	 * Anzahl der Spieler, die schon gew&uuml,rfelt haben.
	 */
	private int rollCount = 0;

	/**
	 * Anzahl an Spieler, die in der Anfangsphase noch w&uuml;rfeln d&uuml;rfen.
	 */
	private int players;

	/**
	 * ID des letzten W&uuml;rflers.
	 */
	private int lastRoller;

	/**
	 * Konstruktor f&uuml;r die <code>GameLogic</code>
	 * 
	 * @param clientVector
	 *            <code>Vector</code> mit den Sockets zu den Clients
	 * @param island
	 *            Insel
	 * @param maximumPlayers
	 *            Anzahl der Mitspieler
	 */
	public GameLogic(Vector<Socket> clientVector, IslandOfCatan island,
			int maximumPlayers, boolean beginning) {
		this.clientVector = clientVector;
		this.island = island;
		this.beginning = beginning;
		;
		this.firstRoll = beginning;
		currentID = 0;
		if (beginning) {
			n = 1;
		} else {
			firstPlayer = 0;
			n = -1;
		}
		firstPips = new int[maximumPlayers];
		newRollIDs = new int[maximumPlayers];
		players = maximumPlayers;
		for (int i = 0; i < firstPips.length; i++) {
			firstPips[i] = -1;
		}
		for (int i = 0; i < newRollIDs.length; i++) {
			newRollIDs[i] = i;
		}
	}

	/**
	 * Startet Spielablauf, vor und nach der Anfangsphase.
	 * 
	 * @throws IOException
	 */
	public void startGame() throws IOException {
		// int players = clientVector.size();
		// int id = (int)Math.round((Math.random() * players));
		if (!firstRoll) {
			island.setCurrentPlayer(island.getSettler(currentID));
			if (currentID == 0) {
				lastPlayer = clientVector.size() - 1;
			} else {
				lastPlayer = currentID - 1;
			}
		}
		firstPlayer = currentID;

		for (int i = 0; i < clientVector.size(); i++) {
			Message message = null;
			if (i == currentID) {
				message = new Message(true, beginning, currentID, firstRoll);
			} else if (!firstRoll) {
				message = new Message(beginning, currentID);
			} else if (i != currentID) {
				// message = new Message(beginning, currentID);
			}
			if (message != null) {
				ObjectOutputStream out = new ObjectOutputStream(clientVector
						.get(i).getOutputStream());
				out.writeObject(message);
				out.flush();
			}
		}
		if (firstRoll) {
			currentID += 1;
		}
	}

	/**
	 * Entscheidet, welcher Spieler als n&auml;chstes an der Reihe ist und
	 * benachrichtigt diesen.
	 * 
	 * @param next
	 *            N&auml;chster Spieler oder noch in der Anfangsphase
	 * @throws IOException
	 */
	public synchronized void build(boolean next) throws IOException {
		if (currentID == lastPlayer && !next) {
			builtRoadsOfLastPlayer++;
		}
		ObjectOutputStream out = null;
		if (!(currentID == firstPlayer && n == -1) && !next) {
			if (currentID == lastPlayer && n == 1) {
				n = -1;
			} else {
				if (n < 0) {
					currentID -= 1;
					if (currentID == -1) {
						currentID = clientVector.size() - 1;
					}
				} else {
					currentID = (currentID + n) % clientVector.size();
				}
			}
			Message message = null;
			for (int i = 0; i < clientVector.size(); i++) {
				if (i == currentID) {
					message = new Message(true, beginning, currentID, firstRoll);
					island.setCurrentPlayer(island.getSettler(currentID));
				} else {
					message = new Message(false, beginning, currentID,
							firstRoll);
				}
				out = new ObjectOutputStream(clientVector.get(i)
						.getOutputStream());
				out.writeObject(message);
				out.flush();
			}
		} else if (next) {
			Message message = new Message(true, beginning, island
					.getCurrentPlayer().getID(), firstRoll);
			out = new ObjectOutputStream(clientVector.get(
					island.getCurrentPlayer().getID()).getOutputStream());
			out.writeObject(message);
			out.flush();
		}
	}

	/**
	 * Benachrichtigt den Spieler, der als n&auml;chstes w&uuml;rfeln soll,
	 * falls in der Anfangsphase.
	 * 
	 * @throws IOException
	 */
	public void nextRoll() throws IOException {
		Message message = null;
		for(int i = 0; i < clientVector.size(); i++) {
			if(i == currentID) {
				message = new Message(true, beginning, currentID, firstRoll);
			} else {
				message = new Message(beginning, currentID);
			}
			ObjectOutputStream out = new ObjectOutputStream(clientVector.get(
					i).getOutputStream());
			out.writeObject(message);
			out.flush();
		}
	}

	/**
	 * Verteilt die ersten Rohstoffe der zweiten Siedlung an jeden Spieler.
	 * 
	 * @return <code>Array</code> mit den Rohstoffen
	 */
	public int[][] distribute() {
		int[][] resources = new int[island.getSettlers().length][5];
		for (int x = 0; x < resources.length; x++) {
			for (int y = 0; y < resources[0].length; y++) {
				resources[x][y] = 0;
			}
		}

		for (int i = 0; i < island.getTileArray1D().length; i++) {
			for (int j = 0; j < island.getTileArray1DOfIndex(i).getNodes().length; j++) {
				for (int n = 0; n < island.getSettlers().length; n++) {
					if (island.getTileArray1DOfIndex(i).getNodeOfIndex(j) == island
							.getSettler(n).getSecondSettlement()) {
						int resource = island.getTileArray1DOfIndex(i)
								.getResource();
						switch (resource) {
						case Constants.GRAIN:
							island.getSettlers()[n].addGrain(1);
							resources[n][0] += 1;
							break;
						case Constants.ORE:
							island.getSettlers()[n].addOre(1);
							resources[n][1] += 1;
							break;
						case Constants.LUMBER:
							island.getSettlers()[n].addLumber(1);
							resources[n][2] += 1;
							break;
						case Constants.WOOL:
							island.getSettlers()[n].addWool(1);
							resources[n][3] += 1;
							break;
						case Constants.BRICK:
							island.getSettlers()[n].addBrick(1);
							resources[n][4] += 1;
							break;
						default:
							break;
						}
					}
				}
			}
		}
		return resources;
	}

	public boolean isBeginning() {
		return beginning;
	}

	public void setBeginning(boolean beginning) {
		this.beginning = beginning;
	}

	public int getBuiltRoadsOfLastPlayer() {
		return builtRoadsOfLastPlayer;
	}

	public int getCurrentID() {
		return currentID;
	}

	public int getN() {
		return n;
	}

	public boolean isFirstRoll() {
		return firstRoll;
	}

	public void setFirstRoll(boolean firstRoll) {
		this.firstRoll = firstRoll;
	}

	public void setCurrentID(int currentID) {
		this.currentID = currentID;
	}

	public int[] getFirstPips() {
		return firstPips;
	}

	public void setFirstPips(int[] firstPips) {
		this.firstPips = firstPips;
	}

	public int[] getNewRollIDs() {
		return newRollIDs;
	}

	public void setNewRollIDs(int[] newRollIDs) {
		this.newRollIDs = newRollIDs;
	}

	public int getFirstPlayer() {
		return firstPlayer;
	}

	public int getRollCount() {
		return rollCount;
	}

	public void setRollCount(int rollCount) {
		this.rollCount = rollCount;
	}

	public void decrementPlayers() {
		players--;
	}

	public int getPlayers() {
		return players;
	}

	public int getLastRoller() {
		return lastRoller;
	}

	public void setLastRoller(int lastRoller) {
		this.lastRoller = lastRoller;
	}
}
