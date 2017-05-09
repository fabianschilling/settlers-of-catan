package networking;

import gui.ServerHost;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.*;

import model.Constants;
import model.GameLogic;
import model.IslandOfCatan;
import model.Settler;

/**
 * Thread, welcher vom Client Nachrichten empfangen kann und sie verarbeitet.
 * 
 * @author Thomas Wimmer, Michael Strobl
 * 
 */
public class ServerThread extends Thread {

	public static boolean isFull;
	/**
	 * Socket des Spielers, der mit diesem Thread verbunden ist.
	 */
	private Socket socket;
	/**
	 * Vector der die Sockets der einzelnen Clients/Spieler enth\u00E4lt.
	 */
	private Vector<Socket> clientVector;
	/**
	 * Vector der die Namen der einzelnen Clients/Spieler enth\u00E4lt.
	 */
	private Vector<String> namesVector;
	/**
	 * Die Insel <code>IslandOfCatan</code> (entspricht dem Model).
	 */
	private IslandOfCatan island;

	/**
	 * Z&auml;hler f&uuml;r die Spieler.
	 */
	private int playerCounter;

	/**
	 * Anzahl der teilnehmenden Spieler.
	 */
	private int maximumPlayers;

	/**
	 * Enth�lt die <code>GameLogic</code>.
	 */
	private GameLogic gameLogic;

	/**
	 * Outputstream an dem <code>ClientThread</code>.
	 */
	private ObjectOutputStream out;

	/**
	 * Anzahl der durch die Stra&szlig;enbaukarte frei zu bauenden
	 * Stra&szlig;en.
	 */
	private int roadCardCount;

	/**
	 * Gerade Karte gespielt oder nicht.
	 */
	private boolean cardIsPlayed;

	/**
	 * Ist die erste Karte die in diesem Zug gespielt wurde.
	 */
	private boolean firstCard;

	/**
	 * Logger, der Meldungen &uuml;ber den Status des Spiels speichert
	 */
	public LoggerClass islandLogger;
	
	/**
	 * Logger, der abgefangene Exceptions ausgibt
	 */
	public ExceptionLogger exceptionLogger;

	/**
	 * Es wurde bereits gew&uuml;rfelt oder nicht
	 */
	private boolean rolled;
	
	/**
	 * ID des eigenen Spielers.
	 */
	private int settlerID;

	/**
	 * Konstruktor f\uu00FCr den ServerThread.
	 * 
	 * @param socket
	 *            Socket des Clients
	 * @param clientVector
	 *            Vector, der die Sockets der einzelnen Clients/Spieler
	 *            enth\u00E4lt
	 * @param namesVector
	 *            Vector, der die Namen der einzelnen Clients/Spieler
	 *            enth\u00E4lt
	 * @param serverGUI
	 *            Die GUI des Servers
	 */
	public ServerThread(Socket socket, Vector<Socket> clientVector,
			Vector<String> namesVector, IslandOfCatan island,
			int maximumPlayers, GameLogic gameLogic, int settlerID) {
		if (maximumPlayers == 1000) {
			this.maximumPlayers = 3;
		}
		this.maximumPlayers = maximumPlayers;
		this.socket = socket;
		this.clientVector = clientVector;
		this.namesVector = namesVector;
		this.island = island;
		this.gameLogic = gameLogic;
		this.settlerID = settlerID;
		playerCounter = namesVector.size();
		roadCardCount = 0;
		firstCard = true;
		cardIsPlayed = false;
		islandLogger = LoggerClass.getInstance();
		exceptionLogger = ExceptionLogger.getInstance();
	}

	@SuppressWarnings("unused")
	public void run() {
		boolean add = true;
		ObjectInputStream in = null;
		Message message = null;
		Settler[] settlers = new Settler[maximumPlayers];
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			exceptionLogger
					.writeException(Level.SEVERE,
							Messages.getString("ServerThread.IOExceptionOutputStream")); //$NON-NLS-1$
		}
		loop: while (true) {
			try {
				in = new ObjectInputStream(socket.getInputStream());
				message = (Message) in.readObject();
				switch (message.getTypeOfMessage()) {
				case Constants.MESSAGE_INIT_CLIENT:
					add = true;
					boolean first = false;
					String answer = null;
					if (namesVector.size() == 0) {
						namesVector.add(message.getArgs()[0]);
						settlers[playerCounter] = new Settler(
								message.getArgs()[0], playerCounter,
								getPlayerColor(playerCounter), Integer.parseInt(message.getArgs()[1]));
						if (!gameLogic.isBeginning()) {
							settlers[playerCounter].addBrick(5);
							settlers[playerCounter].addLumber(5);
							settlers[playerCounter].addOre(5);
							settlers[playerCounter].addGrain(5);
							settlers[playerCounter].addWool(5);
						}
						island.setCurrentPlayer(settlers[0]);
						island.getSettlers()[playerCounter] = settlers[playerCounter];
						clientVector.add(socket);
						answer = "accepted"; //$NON-NLS-1$
						java.util.Date date= new java.util.Date();
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
						String uhrzeit = sdf.format(new Date());
						ServerHost.write(uhrzeit+ " : "+ namesVector.elementAt(0) + " joined \n"); //$NON-NLS-1$ //$NON-NLS-2$
						ServerHost.setKiActive();
						islandLogger.writeLog(Level.INFO,
								namesVector.firstElement()
										+ Messages.getString("ServerThread.Teilnehmen")); //$NON-NLS-1$
						first = true;
					} else if (!isFull) {
						for (int i = 0; i < namesVector.size(); i++) {
							if (namesVector.get(i).equals(message.getArgs()[0])) {
								add = false;
							}
						}
					}

					if (add && !first && !isFull) {
						namesVector.add(message.getArgs()[0]);
						settlers[playerCounter] = new Settler(
								message.getArgs()[0], playerCounter,
								getPlayerColor(playerCounter), Integer.parseInt(message.getArgs()[1]));
						if (!gameLogic.isBeginning()) {
							settlers[playerCounter].addBrick(5);
							settlers[playerCounter].addLumber(5);
							settlers[playerCounter].addOre(5);
							settlers[playerCounter].addGrain(5);
							settlers[playerCounter].addWool(5);
						}
						island.getSettlers()[playerCounter] = settlers[playerCounter];
						clientVector.add(socket);
						answer = "accepted"; //$NON-NLS-1$
						if (namesVector.size() == 2) {
							islandLogger.writeLog(Level.INFO,
									namesVector.elementAt(1)
											+ Messages.getString("ServerThread.Teilnehmen")); //$NON-NLS-1$
							java.util.Date date= new java.util.Date();							
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
							String uhrzeit = sdf.format(new Date());
							ServerHost.write(uhrzeit+ " : "+ namesVector.elementAt(1) + " joined \n"); //$NON-NLS-1$ //$NON-NLS-2$
						} else if (namesVector.size() == 3) {
							islandLogger.writeLog(Level.INFO,
									namesVector.elementAt(2)
											+ Messages.getString("ServerThread.Teilnehmen")); //$NON-NLS-1$
							java.util.Date date= new java.util.Date();							
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
							String uhrzeit = sdf.format(new Date());
							ServerHost.write(uhrzeit+ " : "+ namesVector.elementAt(2) + " joined \n"); //$NON-NLS-1$ //$NON-NLS-2$
						
						} else if (namesVector.size() == 4) {
							ServerHost.setKiInactive();
							islandLogger.writeLog(Level.INFO,
									namesVector.elementAt(3)
											+ Messages.getString("ServerThread.Teilnehmen")); //$NON-NLS-1$
							java.util.Date date= new java.util.Date();							
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
							String uhrzeit = sdf.format(new Date());
							ServerHost.write(uhrzeit+ " : "+ namesVector.elementAt(3) + " joined \n"); //$NON-NLS-1$ //$NON-NLS-2$
						}
					} else if (!first && !isFull) {
						answer = "denied"; //$NON-NLS-1$
						out.writeObject(answer);
						out.flush();
						out.close();
						clientVector.remove(socket);
						break loop;
					} else if (isFull) {
						ServerHost.setKiInactive();
						answer = "full"; //$NON-NLS-1$
						out.writeObject(answer);
						out.flush();
						out.close();
						clientVector.remove(socket);
						break loop;
					}
					out.writeObject(answer);
					out.flush();
					if (clientVector.size() == maximumPlayers && !isFull) {
						setIsFull(true);
						for (int i = 0; i < clientVector.size(); i++) {
							out = new ObjectOutputStream(clientVector.get(i)
									.getOutputStream());

							String[] tempUsernames = new String[maximumPlayers - 1];
							int count = 0;

							for (int j = 0; j < clientVector.size(); j++) {
								if (j != i) {
									tempUsernames[count] = namesVector
											.elementAt(j);
									count++;
								}
							}
							out.writeObject(new Message(island, i));
							out.flush();
							if (i == 0) {
														
							gameLogic.startGame();
							
							}
						}
					}
					break;
				case Constants.MESSAGE_CHAT:
					String chatMessage = message.getMessage();
					if (chatMessage.substring(0, 2).equals("/w ")) { //$NON-NLS-1$
						String username = whisperingUser(chatMessage);
						for (int i = 0; i < namesVector.size(); i++) {
							if (namesVector.elementAt(i).equals(username)) {
								sendMessage(clientVector.get(i), message);
							}
						}
					} else {
						for (int i = 0; i < clientVector.size(); i++) {
							sendMessage(clientVector.get(i), message);
						}
					}
					break;
				case Constants.MESSAGE_PLAYER_TRADE:
//					int offR[] = message.getOfferedResources();
//					int expR[] = message.getExpectedResources();
//					Settler current = island.getCurrentPlayer();
//					if (message.isAllowed()) {
//						if (current.getWool() < offR[0]
//								|| current.getOre() < offR[1]
//								|| current.getBrick() < offR[2]
//								|| current.getLumber() < offR[3]
//								|| current.getGrain() < offR[4]) {
//							sendMessage(clientVector.get(current.getID()),
//									new Message(new int[0], new int[0], false));
//						} else {
//							broadcast(message);
//						}
//					} else if (!message.isAnswered()) {
//						if (message.getSettlerID() >= 0) {
//							Settler settler = island.getSettler(message
//									.getSettlerID());
//							if (settler.getWool() < expR[0]
//									|| settler.getOre() < expR[1]
//									|| settler.getBrick() < expR[2]
//									|| settler.getLumber() < expR[3]
//									|| settler.getGrain() < expR[4]) {
//								sendMessage(clientVector.get(message
//										.getSettlerID()), new Message(
//										new int[0], new int[0], false));
//								broadcast(new Message(offR, expR, -1, false));
//							} else {
//								broadcast(message);
//							}
//						} else {
//							broadcast(message);
//						}
//					} else if (message.isAnswered()) {
//						Settler tradee = null;
//						if (message.getSettlerID() >= 0) {
//							current.addWool(expR[0] - offR[0]);
//							current.addOre(expR[1] - offR[1]);
//							current.addBrick(expR[2] - offR[2]);
//							current.addLumber(expR[3] - offR[3]);
//							current.addGrain(expR[4] - offR[4]);
//							tradee = island.getSettler(message.getSettlerID());
//							tradee.addWool(offR[0] - expR[0]);
//							tradee.addOre(offR[1] - expR[1]);
//							tradee.addBrick(offR[2] - expR[2]);
//							tradee.addLumber(offR[3] - expR[3]);
//							tradee.addGrain(offR[4] - expR[4]);
//						}
//						broadcast(message);
//					}
//					break;
					
					int offR[] = message.getOfferedResources();
					int expR[] = message.getExpectedResources();
					Settler current = island.getCurrentPlayer();
					if (message.isAllowed()) {
						if (current.getWool() < offR[0]
								|| current.getOre() < offR[1]
								|| current.getBrick() < offR[2]
								|| current.getLumber() < offR[3]
								|| current.getGrain() < offR[4]) {
							sendMessage(clientVector.get(current.getID()),
									new Message(new int[0], new int[0], false));
						} else {
							broadcast(message);
						}
					} else if(!message.isAnswered()) {
						if(message.isAccepted()) {
							Settler settler = island.getSettler(message
									.getSettlerID());
							if (settler.getWool() < expR[0]
									|| settler.getOre() < expR[1]
									|| settler.getBrick() < expR[2]
									|| settler.getLumber() < expR[3]
									|| settler.getGrain() < expR[4]) {
								sendMessage(clientVector.get(message
										.getSettlerID()), new Message(
										new int[0], new int[0], false));
								broadcast(new Message(offR, expR, settler.getID(), false, false));
							} else {
								broadcast(message);
							}
						} else {
							broadcast(message);
						}
					} else if (message.isAnswered()) {
						Settler tradee = null;
						if (message.getSettlerID() >= 0) {
							current.addWool(expR[0] - offR[0]);
							current.addOre(expR[1] - offR[1]);
							current.addBrick(expR[2] - offR[2]);
							current.addLumber(expR[3] - offR[3]);
							current.addGrain(expR[4] - offR[4]);
							tradee = island.getSettler(message.getSettlerID());
							tradee.addWool(offR[0] - expR[0]);
							tradee.addOre(offR[1] - expR[1]);
							tradee.addBrick(offR[2] - expR[2]);
							tradee.addLumber(offR[3] - expR[3]);
							tradee.addGrain(offR[4] - expR[4]);
						}
						broadcast(message);
					}
					break;
					
					
				case Constants.MESSAGE_BANK_TRADE:

					/**
					 * Anfang
					 */
					int[] expectedResources1 = message.getExpectedResources();
					int[] offeredResources1 = message.getOfferedResources();
					island.getCurrentPlayer().addWool(
							expectedResources1[0] - offeredResources1[0]);
					island.getCurrentPlayer().addOre(
							expectedResources1[1] - offeredResources1[1]);
					island.getCurrentPlayer().addBrick(
							expectedResources1[2] - offeredResources1[2]);
					island.getCurrentPlayer().addLumber(
							expectedResources1[3] - offeredResources1[3]);
					island.getCurrentPlayer().addGrain(
							expectedResources1[4] - offeredResources1[4]);
					broadcast(message);
					break;
				case Constants.MESSAGE_ROAD:
					if (island.getCurrentPlayer().getRoadCount() < Constants.ROADS_MAX) {
						if ((island.getCurrentPlayer()
								.canAfford(Constants.ROAD) || roadCardCount > 0)
								|| gameLogic.isBeginning()) {
							if (roadCardCount > 0) {
								roadCardCount--;
							} else {
								discount(island.getCurrentPlayer().getID(),
										Constants.ROAD);
							}
							island.getRoads()[message.getRoad().getIndex()]
									.setOwner(island.getCurrentPlayer().getID());
							Message newMessage = message;
							newMessage.getRoad().setOwner(
									island.getCurrentPlayer().getID());
							broadcast(newMessage);
							if (gameLogic.getCurrentID() == gameLogic
									.getFirstPlayer()
									&& gameLogic.getN() == -1
									&& gameLogic.isBeginning()) {
								gameLogic.setBeginning(false);
								gameLogic.build(true);
								int[][] resources = gameLogic.distribute();
								broadcast(new Message(0, 0, resources));
							} else if (gameLogic.isBeginning()) {
								gameLogic.build(false);
							}
							island.getCurrentPlayer().addRoad();
							calculateRoadLength();
							islandLogger.writeLog(Level.INFO, island
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ServerThread.StrasseGebaut")); //$NON-NLS-1$
							island.getCurrentPlayer().calculateVictoryPoints();
							if (island.getCurrentPlayer().getTempScore() >= 10) {
								broadcast(new Message(island.getCurrentPlayer()
										.getID(), true));
							}
						} else {
							Message newMessage = new Message(message.getRoad(),
									false,
									Messages.getString("ServerThread.NichtGenuegendRohstoffe")); //$NON-NLS-1$
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						}
					} else {
						Message newMessage = new Message(message.getRoad(),
								false, Messages.getString("ServerThread.KeineStrassenMehr")); //$NON-NLS-1$
						sendMessage(clientVector.get(island.getCurrentPlayer()
								.getID()), newMessage);
					}
					break;
				case Constants.MESSAGE_NODE:
					if (message.getNode().getBuilding() == Constants.UNBUILT
							&& island.getCurrentPlayer().getSettlementCount() < Constants.SETTLEMENTS_MAX) {
						if (island.getCurrentPlayer().canAfford(
								Constants.SETTLEMENT)
								|| gameLogic.isBeginning()) {
							island.getCurrentPlayer().setSecondSettlement(
									island.getNodes()[message.getNode()
											.getIndex()]);
							changeNode(message, out, Constants.SETTLEMENT);
							discount(island.getCurrentPlayer().getID(),
									Constants.SETTLEMENT);
							island.getCurrentPlayer().addSettlement();
							island.getCurrentPlayer().addScore(1);
							calculateRoadLength();
							islandLogger.writeLog(Level.INFO, island
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ServerThread.SiedlungGebaut")); //$NON-NLS-1$
							island.getCurrentPlayer().calculateVictoryPoints();
							if (island.getCurrentPlayer().getTempScore() >= 10) {
								broadcast(new Message(island.getCurrentPlayer()
										.getID(), true));
							}
						} else {
							Message newMessage = new Message(message.getNode(),
									false,
									Messages.getString("ServerThread.NichtGenuegendRohstoffe")); //$NON-NLS-1$
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						}
					} else if (message.getNode().getBuilding() == Constants.SETTLEMENT
							&& island.getCurrentPlayer().getCityCount() < Constants.CITIES_MAX) {
						if (island.getCurrentPlayer().canAfford(Constants.CITY)) {
							discount(island.getCurrentPlayer().getID(),
									Constants.CITY);
							island.getCurrentPlayer().removeSettlement();
							changeNode(message, out, Constants.CITY);
							island.getCurrentPlayer().addCity();
							island.getCurrentPlayer().addScore(1);
							islandLogger.writeLog(Level.INFO, island
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ServerThread.StadtGebaut")); //$NON-NLS-1$
							calculateRoadLength();
							island.getCurrentPlayer().calculateVictoryPoints();
							if (island.getCurrentPlayer().getTempScore() >= 10) {
								broadcast(new Message(island.getCurrentPlayer()
										.getID(), true));
							}
						} else {
							Message newMessage = new Message(message.getNode(),
									false,
									Messages.getString("ServerThread.NichtGenuegendRohstoffe")); //$NON-NLS-1$
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						}
						islandLogger.writeLog(Level.INFO, island
								.getCurrentPlayer().getUsername()
								+ Messages.getString("ServerThread.StadtGebaut")); //$NON-NLS-1$
					} else {
						Message newMessage = null;
						if (message.getNode().getBuilding() == Constants.UNBUILT) {
							newMessage = new Message(message.getNode(), false,
									Messages.getString("ServerThread.KeineSiedlungMehr")); //$NON-NLS-1$
						} else {
							newMessage = new Message(message.getNode(), false,
									Messages.getString("ServerThread.KeineStaedteMehr")); //$NON-NLS-1$
						}
						sendMessage(clientVector.get(island.getCurrentPlayer()
								.getID()), newMessage);
					}
					break;
				case Constants.MESSAGE_PIPS:
					rolled = true;
					int pipsOne = message.getPips1();
					int pipsTwo = message.getPips2();
					int pips = pipsOne + pipsTwo;
					if (pips == 7 && !gameLogic.isFirstRoll()) {
						broadcast(new Message(pipsOne, pipsTwo, null));
					} else if (!gameLogic.isFirstRoll()) {
						int[] grain = new int[maximumPlayers];
						int[] wool = new int[maximumPlayers];
						int[] lumber = new int[maximumPlayers];
						int[] ore = new int[maximumPlayers];
						int[] brick = new int[maximumPlayers];
						int[][] resources = island.distribute(pips);

						for (int j = 0; j < resources.length; j++) {
							grain[j] = resources[j][0];
							wool[j] = resources[j][1];
							lumber[j] = resources[j][2];
							ore[j] = resources[j][3];
							brick[j] = resources[j][4];
						}
						broadcast(new Message(pipsOne, pipsTwo, resources));
					} else {
						if (gameLogic.getRollCount() < (gameLogic.getPlayers() - 1)) {
							int counter = 0;
							while (gameLogic.getNewRollIDs()[counter] == -1) {
								counter++;
								gameLogic.setRollCount(gameLogic.getRollCount() + 1);
								int id = (gameLogic.getCurrentID() + 1) % clientVector.size();
								gameLogic.setCurrentID(id);
							}
							if (gameLogic.getLastRoller() == gameLogic.getCurrentID()) {
								gameLogic.setCurrentID((gameLogic.getCurrentID() + 1) % clientVector.size());
							}
							if (gameLogic.getNewRollIDs()[gameLogic.getCurrentID()] == -1) {
								int id = (gameLogic.getCurrentID() + 1) % clientVector.size();
								gameLogic.setCurrentID(id);
							}
							gameLogic.setRollCount(gameLogic.getRollCount() + 1);
							gameLogic.getFirstPips()[message.getSettlerID()] = pips;
							broadcast(message);
							gameLogic.setLastRoller(gameLogic.getCurrentID());
							gameLogic.nextRoll();
							gameLogic.setCurrentID((gameLogic.getCurrentID() + 1) % clientVector.size());
						} else {
							gameLogic.getFirstPips()[message.getSettlerID()] = pips;
							broadcast(message);
							int newRollCount = 0;
							int maximumPips = 0;
							int currentPlayer = -1;
							for (int i = 0; i < gameLogic.getFirstPips().length; i++) {
								if (gameLogic.getFirstPips()[i] > maximumPips) {
									maximumPips = gameLogic.getFirstPips()[i];
								}
							}
							newRollCount = 0;
							for (int i = 0; i < gameLogic.getFirstPips().length; i++) {
								if (gameLogic.getFirstPips()[i] == maximumPips) {
									gameLogic.getNewRollIDs()[i] = i;
									newRollCount++;
									currentPlayer = i;
								} else if (gameLogic.getFirstPips()[i] != 0) {
									gameLogic.getNewRollIDs()[i] = -1;
									gameLogic.decrementPlayers();
								}
							}
							for (int i = 0; i < gameLogic.getFirstPips().length; i++) {
								if (gameLogic.getNewRollIDs()[i] == -1) {
									gameLogic.getFirstPips()[i] = 0;
								}
							}
							if (newRollCount == 1) {
								gameLogic.setFirstRoll(false);
								gameLogic.setCurrentID(currentPlayer);
								island.setCurrentPlayer(island.getSettler(currentPlayer));
								gameLogic.startGame();
							} else {
								int counter = 0;
								while (gameLogic.getNewRollIDs()[counter] == -1) {
									counter++;
									int id = (gameLogic.getCurrentID() + 1) % clientVector.size();
									gameLogic.setCurrentID(id);
								}
								if(gameLogic.getNewRollIDs()[gameLogic.getCurrentID()] == -1) {
									int id = (gameLogic.getCurrentID() + 1) % clientVector.size();
									gameLogic.setCurrentID(id);
								}
								gameLogic.setRollCount(0);
								gameLogic.setLastRoller(gameLogic.getCurrentID());
								gameLogic.nextRoll();
								gameLogic.setCurrentID(0);
							}
						}
					}
					break;
				case Constants.MESSAGE_DISCARDING:
					int[] resources = message.getDiscardedResources();
					island.getSettler(message.getSettlerID()).addGrain(
							-resources[0]);
					island.getSettler(message.getSettlerID()).addWool(
							-resources[1]);
					island.getSettler(message.getSettlerID()).addLumber(
							-resources[2]);
					island.getSettler(message.getSettlerID()).addOre(
							-resources[3]);
					island.getSettler(message.getSettlerID()).addBrick(
							-resources[4]);
					broadcast(message);
					break;
				case Constants.MESSAGE_ROBBER:
					if (message.getTile() != null) {
						island.moveRobber(message.getTile());
						island.getRobberVictims(message.getTile());
						Message newMessage = new Message(message.getTile(),
								island.getRobberVictims(message.getTile()));
						broadcast(newMessage);
					} else if (message.getResource() < 0) {
						byte resource = island.getSettler(
								message.getSettlerID()).getRandomResource();
						if (resource != 0) {
							island.getSettler(message.getSettlerID())
									.addResource(resource, -1);
							island.getCurrentPlayer().addResource(resource, 1);
						}
						Message newMessage = new Message(
								message.getSettlerID(), resource);
						broadcast(newMessage);
					} else {
						broadcast(message);
					}
					break;
				case Constants.MESSAGE_DEV_CARD:
					boolean cardDrawn = message.isCardDrawn();
					if (cardDrawn) {
						if (island.getCurrentPlayer().canAfford(
								Constants.DEVELOPMENTCARD)) {
							byte drawnCard = island.getBank().drawCard();
							if (drawnCard < 0) {
								Message newMessage = new Message(drawnCard,
										cardDrawn, false, true);
								broadcast(newMessage);
							} else {
								discount(island.getCurrentPlayer().getID(),
										Constants.DEVELOPMENTCARD);
								if (drawnCard != Constants.VICTORYPOINTS) {
									island.getCurrentPlayer()
											.addTempDevelopmentCard(drawnCard);
								} else {
									island.getCurrentPlayer()
											.addDevelopmentCard(drawnCard);
									island.getCurrentPlayer().addScore(1);
								}
								Message newMessage = new Message(drawnCard,
										cardDrawn, true, true);
								broadcast(newMessage);
								island.getCurrentPlayer().calculateVictoryPoints();
								if (island.getCurrentPlayer().getTempScore() >= 10) {
									broadcast(new Message(island.getCurrentPlayer().getID(), true));
								}
							}
						} else {
							Message newMessage = new Message(
									Constants.VICTORYPOINTS, cardDrawn, false,
									true);
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						}
					} else {
						byte devCard = message.getDevCard();
						if (!island.getCurrentPlayer().hasCard(devCard)
								&& !cardIsPlayed && !message.isMonopoly()) {
							Message newMessage = new Message(devCard, false,
									false, true);
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						} else if (!firstCard) {
							Message newMessage = new Message(devCard, false,
									true, false);
							sendMessage(clientVector.get(island
									.getCurrentPlayer().getID()), newMessage);
						} else {
							cardIsPlayed = true;
							switch (devCard) {
							case Constants.KNIGHT:
								island.getCurrentPlayer()
										.removeDevelopmentCard(devCard);
								island.getCurrentPlayer().addKnight();
								int playerId = island.getLargestArmyHolderID();
								island.getCurrentPlayer().calculateVictoryPoints();
								cardIsPlayed = false;
								if(island.getCurrentPlayer().getID() == settlerID)
									firstCard = false;
								broadcast(new Message(Constants.KNIGHT,
										playerId));
								island.getCurrentPlayer()
										.calculateVictoryPoints();
								if (island.getCurrentPlayer().getTempScore() >= 10) {
									broadcast(new Message(island.getCurrentPlayer().getID(), true));
								}
								break;
							case Constants.VICTORYPOINTS:
								island.getCurrentPlayer()
										.removeDevelopmentCard(devCard);
								island.getCurrentPlayer().addScore(1);
								cardIsPlayed = false;
								if(island.getCurrentPlayer().getID() == settlerID)
									firstCard = false;
								broadcast(message);
								break;
							case Constants.BUILDSTREETS:
								island.getCurrentPlayer()
										.removeDevelopmentCard(devCard);
								activateRoadCard();
								cardIsPlayed = false;
								if(island.getCurrentPlayer().getID() == settlerID)
									firstCard = false;
								broadcast(message);
								break;
							case Constants.GETRESOURCES:
								if (message.getResource() < 0) {
									island.getCurrentPlayer()
											.removeDevelopmentCard(devCard);
									broadcast(message);
								} else {
									island.getCurrentPlayer().addResource(
											message.getResource(), 1);
									island.getCurrentPlayer().addResource(
											message.getResource2(), 1);
									cardIsPlayed = false;
									if(island.getCurrentPlayer().getID() == settlerID)
										firstCard = false;
									broadcast(message);
								}
								break;
							case Constants.MONOPOLY:
								if (message.getResource() > 0) {
									int amount = message.getAmountOfResource();
									if (amount < 0) {
										broadcast(message);
									} else {
										island.getSettler(
												message.getSettlerID())
												.addResource(
														message.getResource(),
														-message.getAmountOfResource());
										island.getCurrentPlayer().addResource(
												message.getResource(),
												message.getAmountOfResource());
										cardIsPlayed = false;
										if(island.getCurrentPlayer().getID() == settlerID)
											firstCard = false;
										broadcast(message);
									}
								} else {
									island.getCurrentPlayer()
											.removeDevelopmentCard(devCard);
									broadcast(message);
								}
								break;
							default:
								break;
							}
						}
					}
					break;
				case Constants.MESSAGE_GAME_END:
					broadcast(message);
					islandLogger.writeLog(Level.INFO, Messages.getString("ServerThread.SpielBeendet")); //$NON-NLS-1$
					break;
				case Constants.MESSAGE_CHEAT:
					int id = message.getSettlerID();
					byte resource = message.getResource();
					int amountOfResource = message.getAmountOfResource();
					island.changeAmountOfResource(resource, amountOfResource,
							id);
					broadcast(message);

					break;
				case Constants.MESSAGE_CHEAT_ACTIVATION:
					boolean allowedToCheat = message.isAllowedToCheat();
					island.setAllowedToCheat(allowedToCheat);
					broadcast(message);
					break;
				case Constants.MESSAGE_END_OF_TURN:
					if (rolled || gameLogic.isBeginning()) {
						rolled = false;
						cardIsPlayed = false;
						island.getCurrentPlayer().addTempCardsToDevCards();
						firstCard = true;
						island.nextPlayer();
						island.getCurrentPlayer().calculateVictoryPoints();
						if (island.getCurrentPlayer().getTempScore() >= 10) {
							broadcast(new Message(island.getCurrentPlayer()
									.getID(), true));
						} else {
							for (int i = 0; i < clientVector.size(); i++) {
								if (island.getCurrentPlayer().getID() != i) {
									sendMessage(clientVector.get(i), message);
								}
							}
							gameLogic.build(true);
						}
					}
					break;
				default:
					break;
				}
			} catch (IOException e1) {
				exceptionLogger
				.writeException(Level.SEVERE, Messages.getString("ServerThread.IOExceptionServerThread")); //$NON-NLS-1$
				namesVector.remove(playerCounter);
				clientVector.remove(socket);
				break loop;
			} catch (ClassNotFoundException e1) {
				exceptionLogger
				.writeException(Level.SEVERE, Messages.getString("ServerThread.ClassNotFoundExceptionSeverThread")); //$NON-NLS-1$
				break loop;
			}
		}
	}

	private static void setIsFull(boolean b) {
		isFull = b;
	}
	public static boolean getIsFull(){
		return isFull;
	}
	/**
	 * Zieht Rohstoffe ab.
	 * 
	 * @param id
	 *            ID des Spielers, welcher gebaut hat
	 * @param building
	 *            Bauwerk
	 */
	private void discount(int id, byte building) {
		if (!gameLogic.isBeginning()) {
			Settler settler = island.getSettler(id);
			switch (building) {
			case Constants.CITY:
				settler.addOre(-3);
				settler.addGrain(-2);
				break;
			case Constants.SETTLEMENT:
				settler.addLumber(-1);
				settler.addBrick(-1);
				settler.addGrain(-1);
				settler.addWool(-1);
				break;
			case Constants.ROAD:
				settler.addLumber(-1);
				settler.addBrick(-1);
				break;
			case Constants.DEVELOPMENTCARD:
				settler.addWool(-1);
				settler.addOre(-1);
				settler.addGrain(-1);
			default:
				break;
			}
		}
	}

	/**
	 * Schickt eine Nachricht.
	 * 
	 * @param socket
	 *            <code>Socket</code>, an welches die Nachricht geschickt werden
	 *            soll
	 * @param message
	 *            Zu schickende Nachricht
	 */
	private void sendMessage(Socket socket, Message message) {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("ServerThread.IOException.FehlerSenden")); //$NON-NLS-1$
		}
	}

	/**
	 * Schickt gegebene Nachricht an alle <code>Clients</code> raus.
	 * 
	 * @param message
	 *            Zu schickende Nachricht
	 */
	private void broadcast(Message message) {
		for (int i = 0; i < clientVector.size(); i++) {
			sendMessage(clientVector.get(i), message);
		}
	}

	/**
	 * Passt den Knoten aun, auf dem gebaut wurde.
	 * 
	 * @param message
	 *            Nachricht, die f&uuml;r den Bau eingegangen ist
	 * @param out
	 *            Outputstream, in den die Nachricht geschrieben wird
	 * @param building
	 *            Gebautes Bauwerk
	 * @throws IOException
	 */
	private void changeNode(Message message, ObjectOutputStream out,
			byte building) throws IOException {
		island.getNodes()[message.getNode().getIndex()].setOwnerID(island
				.getCurrentPlayer().getID());
		island.getNodes()[message.getNode().getIndex()].setBuilding(building);
		message.getNode().setBuilding(building);
		message.getNode().setOwnerID(island.getCurrentPlayer().getID());
		broadcast(message);
	}

	/**
	 * Erstellt <code>String</code> zum Fl&uuml;stern.
	 * 
	 * @param chatMessage
	 *            Nachricht, die gschickt wurde
	 * @return Fl&uuml;sterstring
	 */
	private String whisperingUser(String chatMessage) {
		String tempString = chatMessage.substring(2);
		int count = 3;
		char c = 'a';
		while (c != ' ') {
			c = tempString.charAt(count);
			count++;
		}
		return tempString.substring(2, count);
	}

	/**
	 * Setzt den <code>roadCardCount</code>.
	 */
	private void activateRoadCard() {
		roadCardCount = (island.getCurrentPlayer().getRoadCount() == 14) ? 1
				: 2;
	}

	/**
	 * Gibt die Farbe des Spielers zur�ck.
	 * 
	 * @param count
	 *            ID des Spielers
	 * @return Farbe des Spielers mit count als ID
	 */
	public Color getPlayerColor(int count) {
		switch (count) {
		case 0:
			return Color.BLUE;
		case 1:
			return Color.GREEN;
		case 2:
			return Color.RED;
		case 3:
			return Color.YELLOW;
		default:
			return Color.WHITE;
		}
	}

	/**
	 * Bereechnet und setzt die L&auml;nge der Handelsstra&szlig;e
	 */
	public void calculateRoadLength() {
		int newLength = Constants.ROAD_CASE1;
		for (int i = 0; i < island.getSettlers().length; i++) {
			int oldRoadLength = island.getSettler(i).getLongestRoadLength();
			int newRoadLength = island.calculateRoadLength(i);
			if(oldRoadLength > newRoadLength && island.getSettler(i).isLongestRoad()) {
				newLength = newRoadLength;
			}
			island.getSettler(i).setLongestRoadLength(newRoadLength);
		}
		int ownerOfLongestRoad = -1;
		boolean sameLength = false;
		int currentLongestRoadLength = 0;
		for(int j = 0; j < island.getSettlers().length; j++) {
			
			int settlerLongestRoadLength = island.getSettler(j).getLongestRoadLength();
			if (currentLongestRoadLength < settlerLongestRoadLength) {
				currentLongestRoadLength = settlerLongestRoadLength;
				sameLength = false;
			} else if (currentLongestRoadLength == settlerLongestRoadLength) {
				sameLength = true;
			}
		}
		
		if((newLength < currentLongestRoadLength && sameLength && newLength != Constants.ROAD_CASE1) || currentLongestRoadLength < 5) {
			ownerOfLongestRoad = Constants.ROAD_CASE2;			
		} else {
			for(int i = 0; i < island.getSettlers().length; i++) {
				if(island.getSettler(i).isLongestRoad() && island.getSettler(i).getLongestRoadLength() == currentLongestRoadLength) {
					ownerOfLongestRoad = Constants.ROAD_CASE3;	
				}
			}
		}
		
		if(ownerOfLongestRoad == Constants.ROAD_CASE1) {
			for (int i = 0; i < island.getSettlers().length; i++) {
				int tempOwner = island.ownerOfLongestRoad(island.getSettler(i));
				if (tempOwner != -1) {
					ownerOfLongestRoad = tempOwner;
				}
			}
		}
		
		if (ownerOfLongestRoad >= 0) {
			broadcast(new Message(ownerOfLongestRoad, false, island.getSettler(
					ownerOfLongestRoad).getLongestRoadLength()));
			island.getSettler(ownerOfLongestRoad).calculateVictoryPoints();
			if (island.getSettler(ownerOfLongestRoad).getID() == island
					.getCurrentPlayer().getID()) {
				if (island.getCurrentPlayer().getTempScore() >= 10) {
					broadcast(new Message(island.getCurrentPlayer().getID(),
							true));
				}
			}
		} else if (ownerOfLongestRoad == -2) {
			int length = 0;
			for (int i = 0; i < island.getSettlers().length; i++) {
				if(island.getSettler(i).getLongestRoadLength() <= 4)
					island.getSettler(i).setLongestRoad(false);
			}
			broadcast(new Message(ownerOfLongestRoad, false, length));
		}
	}

	public void setIsland(IslandOfCatan island) {
		this.island = island;
	}

}