package networking;

import gui.MainGUI;
import gui.ServerGUIClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import audio.ImportAudio;
import audio.MP3;

import controller.ControllerInterface;
import model.Constants;
import model.Settler;
import model.Tile;

/**
 * <code>Thread</code> zur Verbindung mit dem Server.
 * 
 * @author Michael Strobl, Thomas Wimmer
 * 
 */
public class ClientThread extends Thread {

	/**
	 * <code>Socket</code> zur Verbindung mit dem Server.
	 */
	private Socket socket;

	/**
	 * <code>Client</code> mit dem Zustand des Spielers.
	 */
	private ClientInterface client;

	/**
	 * <code>Controller</code> f&uuml;r die GUI.
	 */
	private ControllerInterface controller;

	/**
	 * Enth&auml;lt die Anzahl der frei zu bauenden Stra&szlig;en.
	 */
	private int roadCardCount;

	/**
	 * Anzahl der Spieler mit mehr als sieben Rohstoffkarten auf der Hand.
	 */
	private int moreThanSevenCount;

	/**
	 * Anzahl der bereits eingegangen Antworten beim Handeln.
	 */
	private int traderCounter;
	
	/**
	 * Anzahl der bereits akzeptierten Angebote beim Handeln.
	 */
	private int acceptedCounter;

	/**
	 * Liste der Spieler-IDs, derer die bereit sind zu handeln.
	 */
	ArrayList<Integer> tradeeIDs;

	/**
	 * Logger
	 */
	private LoggerClass clientLogger;

	/**
	 * Exceptionlogger
	 */
	private ExceptionLogger clientExceptionLogger;

	/**
	 * Steuert die Ausgabe des Strings, wenn alle ihre ersten Siedlungen gebaut
	 * haben und das Spiel beginnt.
	 */
	private boolean startGame;
	
	/**
	 * Spielende
	 */
	private boolean end;
	
	/**
	 * Objekt zum abspielen der Audiofiles
	 */
	private MP3 song;

	/**
	 * Konstruktor f&uuml;r den <code>ClientThread</code>.
	 * 
	 * @param client2
	 *            Zustand des Spielers
	 * @param socket
	 *            Verbindungssocket zum Server
	 * @param aiController
	 *            Controller der GUI
	 */
	public ClientThread(ClientInterface client, Socket socket,
			ControllerInterface controller) { // + GUI
		startGame = true;
		this.socket = socket;
		this.client = client;
		this.controller = controller;
		song = new MP3();
		end = false;
		clientLogger = LoggerClass.getInstance();
		clientExceptionLogger = ExceptionLogger.getInstance();
	}

	public void run() {
		loop: while (true) {
		try {
			ObjectInputStream in = null;
			Message message = null;
				in = new ObjectInputStream(socket.getInputStream());
				message = (Message) in.readObject();
				switch (message.getTypeOfMessage()) {
				case Constants.MESSAGE_ID:
					client.setBeginning(message.isBeginning());
					client.getIsland().setCurrentPlayer(client.getIsland().getSettler(message.getSettlerID()));
					controller.refresh();
					break;
				case Constants.MESSAGE_LONGEST_ROAD:
					if(message.getSettlerID() == Constants.ROAD_CASE2) {
						for (int i = 0; i < controller.getIsland().getSettlers().length; i++) {
							controller.getIsland().getSettler(i).setLongestRoad(false);
							controller.getIsland().getSettler(i).calculateVictoryPoints();
							controller.refreshLongestRoadView(-1);
							controller.refresh();
						}
					} else {
						controller.appendMessage(client.getIsland().getSettler(message.getSettlerID()).getUsername() + Messages.getString("ClientThread.OwnerLaengsteHandelsstrasse")); //$NON-NLS-1$
						for (int i = 0; i < controller.getIsland().getSettlers().length; i++) {
							if (i == message.getSettlerID()) {
								controller.getIsland().getSettler(i).setLongestRoad(true);
							} else {
								controller.getIsland().getSettler(i).setLongestRoad(false);
							}
							controller.getIsland().getSettler(i).calculateVictoryPoints();
						}
						clientLogger.writeLog(Level.INFO, client.getIsland().getSettler(message.getSettlerID()).getUsername() + Messages.getString("ClientThread.OwnerLaengsteHandelsstrasse")); //$NON-NLS-1$
						controller.refreshLongestRoadView(message.getSettlerID());
						controller.refresh();
					}
					break;
				case Constants.MESSAGE_YOUR_TURN:
					if (!client.isBeginning()) {
						controller.hideDices();
					}
					client.setBeginning(message.isBeginning());
					client.setCurrentPhase(Constants.PHASE_1);
					int currentPlayer = client.getCurrentPlayer();
					controller.getIsland().setCurrentPlayer(
							controller.getIsland().getSettler(
									message.getSettlerID()));
					if (currentPlayer != client.getCurrentPlayer()) {
						controller.appendMessage(Messages.getString("ClientThread.DuBistDran")); //$NON-NLS-1$
						if(!client.isBeginning())
							controller.setAllowedToRoll(true);
					}
					if (message.isFirstRoll()) {
						controller.setAllowedToRoll(true);
						controller
								.appendMessage(Messages.getString("ClientThread.DuDarfstWuerfeln")); //$NON-NLS-1$
					} else {
						if (message.isBeginning()) {
							controller.setAllowedToBuild(true);
							controller
									.appendMessage(Messages.getString("ClientThread.DuDarfstSiedlungBauen")); //$NON-NLS-1$

						}
					}
					controller.refresh();
					client.setFirstRoll(message.isFirstRoll());
					break;
				case Constants.MESSAGE_INIT_ISLAND:
					client.setIsland(message.getIsland());
					client.setSettler(message.getIsland().getSettler(
							message.getSettlerID()));
					controller.startMainGUI(message.getIsland());
					if (controller.getIsland().getCurrentPlayer().getID() != client
							.getSettler().getID()) {
						controller.appendMessage(client.getIsland()
								.getCurrentPlayer().getUsername()
								+ Messages.getString("ClientThread.DarfWuerfeln")); //$NON-NLS-1$
					}
					controller.refresh();
					clientLogger.writeLog(Level.INFO, Messages.getString("ClientThread.IslandErstellt")); //$NON-NLS-1$
					break;
				case Constants.MESSAGE_CHAT:
					String chatMessage = message.getMessage();
					if (chatMessage.substring(0, 2).equals("/w ")) { //$NON-NLS-1$
						chatMessage = chatMessage.substring(3);
						// Message in anderer Farbe ausgeben!!
					}
					controller.appendMessage(chatMessage, client.getIsland()
							.getSettler(message.getSettlerID()).getColor());
					break;
				case Constants.MESSAGE_ROAD:
					if (message.isAllowed()) {
						if (roadCardCount > 0) {
							roadCardCount--;
						} else {
							discount(message.getRoad().getOwner(),
									Constants.ROAD);
							if (message.getRoad().getOwner() == client.getID()) {
								client.setCurrentPhase(Constants.PHASE_3);
							}
						}

						if (message.getRoad().getOwner() == client.getID()) {
							client.getSettler().addRoad();
						} else {
							controller.appendMessage(client.getIsland()
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ClientThread.HatStrasseGebaut")); //$NON-NLS-1$
						}
						client.getIsland().getRoads()[message.getRoad()
								.getIndex()].setOwner(message.getRoad()
								.getOwner());
						controller.addRoad(message.getRoad().getIndex(),
								message.getRoad().getOwner());
						controller.refresh();
						controller.repaintRoads(message.getRoad().getIndex(),
								message.getRoad().getOwner());
						for (int i = 0; i < client.getIsland().getSettlers().length; i++) {
							client.getIsland()
									.getSettler(i)
									.setLongestRoadLength(
											client.getIsland()
													.calculateRoadLength(i));
						}
						controller.chooseAction();
						controller.refresh();
					} else {
						controller.appendMessage(message.getMessage());
						client.getIsland().getRoads()[message.getRoad()
								.getIndex()].setOwner(Constants.NOBODY);
					}
					break;
				case Constants.MESSAGE_NODE:
					if (message.isAllowed()) {
						if (client.isBeginning()) {
							client.getIsland().setCurrentPlayer(
									client.getIsland().getSettler(
											message.getNode().getOwnerID()));
						}
						if (message.getNode().getOwnerID() == client.getID()) {
							if (message.getNode().getBuilding() == Constants.SETTLEMENT) {
								client.getSettler().addSettlement();
							} else {
								client.getSettler().addCity();
								client.getSettler().removeSettlement();
							}
							client.setCurrentPhase(Constants.PHASE_3);
							if (message.getNode().getHarbor() != Constants.NOHARBOR) {
								client.getSettler().addHarbor(
										message.getNode().getHarbor());
							}
						} else {
							if (message.getNode().getBuilding() == Constants.SETTLEMENT) {
								controller.appendMessage(client.getIsland()
										.getCurrentPlayer().getUsername()
										+ Messages.getString("ClientThread.HatSiedlungGebaut")); //$NON-NLS-1$
							} else {
								controller.appendMessage(client.getIsland()
										.getCurrentPlayer().getUsername()
										+ Messages.getString("ClientThread.HatStadtGebaut")); //$NON-NLS-1$
							}
						}
						client.getIsland().getNodes()[message.getNode()
								.getIndex()].setOwnerID(message.getNode()
								.getOwnerID());
						client.getIsland().getNodes()[message.getNode()
								.getIndex()].setBuilding(message.getNode()
								.getBuilding());
						byte building;
						if (message.getNode().getBuilding() == Constants.SETTLEMENT) {
							building = Constants.SETTLEMENT;
						} else {
							building = Constants.CITY;
						}
						client.getIsland()
								.getSettler(message.getNode().getOwnerID())
								.addScore(1);
						client.getIsland()
								.getSettler(message.getNode().getOwnerID())
								.calculateVictoryPoints();
						for (int i = 0; i < client.getIsland().getSettlers().length; i++) {
							client.getIsland().getSettler(i).setLongestRoadLength(
									client.getIsland().calculateRoadLength(i));
						}
						controller.refreshOpponents();
						discount(message.getNode().getOwnerID(), building);
						controller.refresh();
						controller.chooseAction();
						controller.repaintMap();
					} else {
						controller.appendMessage(message.getMessage());
					}
					break;
				case Constants.MESSAGE_RESOURCES:
					int pipsOne = message.getPips1();
					int pipsTwo = message.getPips2();
					int pips = pipsOne + pipsTwo;

					// nach Startphase, nicht loeschen
					if (pips != 0) {
						controller.hideDices();
						song.playSound(ImportAudio.rollDice, false);
						controller.showDices(pipsOne, pipsTwo);
						if (client.getID() == client.getCurrentPlayer()) {
							controller.appendMessage(Messages.getString("ClientThread.DuHastEine") + pips //$NON-NLS-1$
									+ Messages.getString("ClientThread.Gewuerfelt")); //$NON-NLS-1$
						} else {
							controller.appendMessage(client.getIsland()
									.getSettler(client.getCurrentPlayer())
									.getUsername()
									+ Messages.getString("ClientThread.HatEine") + pips + Messages.getString("ClientThread.Gewuerfelt")); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					if (pips != 7) {
						int[][] resources = message.getResources();
						for (int i = 0; i < client.getIsland().getSettlers().length; i++) {
							if (client.getSettler().getID() == i) {
								client.getSettler().addGrain(resources[i][0]);
								client.getSettler().addOre(resources[i][1]);
								client.getSettler().addLumber(resources[i][2]);
								client.getSettler().addWool(resources[i][3]);
								client.getSettler().addBrick(resources[i][4]);
								String resourceString = ""; //$NON-NLS-1$
								for (int count = 0; count < resources[i].length; count++) {
									if (resources[i][count] > 0) {
										switch (count) {
										case 0:
											resourceString += resources[i][count]
													+ Messages.getString("ClientThread.XGetreide"); //$NON-NLS-1$
											break;
										case 1:
											resourceString += resources[i][count]
													+ Messages.getString("ClientThread.XErz"); //$NON-NLS-1$
											break;
										case 2:
											resourceString += resources[i][count]
													+ Messages.getString("ClientThread.XHolz"); //$NON-NLS-1$
											break;
										case 3:
											resourceString += resources[i][count]
													+ Messages.getString("ClientThread.XWolle"); //$NON-NLS-1$
											break;
										case 4:
											resourceString += resources[i][count]
													+ Messages.getString("ClientThread.XLehm"); //$NON-NLS-1$
											break;
										default:
										}
									}
								}
								if (resourceString.equals("")) { //$NON-NLS-1$
								} else {
									controller.appendMessage(Messages.getString("ClientThread.DuBekommst") //$NON-NLS-1$
											+ resourceString);
									if (startGame) {
										startGame = false;
										if (client.getCurrentPlayer() == client
												.getID()) {
											controller
													.appendMessage(Messages.getString("ClientThread.DuDarfstSpielStarten")); //$NON-NLS-1$
											controller.setAllowedToRoll(true);
										} else {
											controller
													.appendMessage(client
															.getIsland()
															.getCurrentPlayer()
															.getUsername()
															+ Messages.getString("ClientThread.StartetSpiel")); //$NON-NLS-1$
											client.setBeginning(false);
										}
									}
								}
							} else {
								int amount = resources[i][0] + resources[i][1]
										+ resources[i][2] + resources[i][3]
										+ resources[i][4];
								client.getIsland().getSettler(i)
										.addResources(amount);
							}

							clientLogger.writeLog(Level.INFO, client
									.getIsland().getSettler(i).getUsername()
									+ Messages.getString("ClientThread.BekommtFolgendeResourcen") //$NON-NLS-1$
									+ resources[i][0]
									+ Messages.getString("ClientThread.Brick-") //$NON-NLS-1$
									+ resources[i][4]
									+ Messages.getString("ClientThread.Ore-") //$NON-NLS-1$
									+ resources[i][1]
									+ Messages.getString("ClientThread.Lumber-") //$NON-NLS-1$
									+ resources[i][2]
									+ Messages.getString("ClientThread.Wool-") //$NON-NLS-1$
									+ resources[i][3]);

						}
						controller.refresh();
						controller.chooseAction();
					} else {
						moreThanSevenCount = 0;
						for (int j = 0; j < client.getIsland().getSettlers().length; j++) {
							if (client.getIsland().getSettlers()[j] == client
									.getSettler()) {
								if (client.getSettler().getResourcesNumber() > Constants.MAX_CARDS_SEVEN) {
									controller
											.appendMessage(Messages.getString("ClientThread.DuHastUeber7") //$NON-NLS-1$
													+ client.getSettler()
															.getResourcesNumber()
													/ 2 + Messages.getString("ClientThread.KartenAbwerfen")); //$NON-NLS-1$
									moreThanSevenCount++;
									controller.discard(client.getSettler()
											.getResourcesNumber() / 2);
								} else {
									controller
											.appendMessage(Messages.getString("ClientThread.Weniger7Karten")); //$NON-NLS-1$
								}
							} else {
								if (client.getIsland().getSettlers()[j]
										.getAmountOfResources() > Constants.MAX_CARDS_SEVEN) {
									controller
											.appendMessage(client.getIsland()
													.getSettlers()[j]
													.getUsername()
													+ Messages.getString("ClientThread.Muss") //$NON-NLS-1$
													+ client.getIsland()
															.getSettlers()[j]
															.getAmountOfResources()
													/ 2 + Messages.getString("ClientThread.KartenAbwerfen")); //$NON-NLS-1$
									moreThanSevenCount++;
								}
							}
						}
						if (moreThanSevenCount == 0) {
							controller.setDiscarding(false);
							if (isCurrentPlayer()) {
								controller.activateRobberMoving();
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberVerschieben")); //$NON-NLS-1$
							} else {
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberGehtUm")); //$NON-NLS-1$
							}
						} else {
							controller.setDiscarding(true);
						}
					}
					break;
				case Constants.MESSAGE_DISCARDING:
					int[] resources = message.getDiscardedResources();
					moreThanSevenCount--;
					if (client.getID() == message.getSettlerID()) {
						client.getSettler().addGrain(-resources[0]);
						client.getSettler().addWool(-resources[1]);
						client.getSettler().addLumber(-resources[2]);
						client.getSettler().addOre(-resources[3]);
						client.getSettler().addBrick(-resources[4]);
						controller.appendMessage(Messages.getString("ClientThread.DuVerlierst") //$NON-NLS-1$
								+ generateResourceString(resources));

					} else {
						int amount = resources[0] + resources[1] + resources[2]
								+ resources[3] + resources[4];
						client.getIsland().getSettler(message.getSettlerID())
								.addResources(-amount);
						controller.appendMessage(client.getIsland()
								.getSettler(message.getSettlerID())
								.getUsername() + Messages.getString("ClientThread.Verliert") + generateResourceString(message.getDiscardedResources())); //$NON-NLS-1$
					}
					clientLogger.writeLog(Level.INFO, client.getIsland()
							.getSettler(message.getSettlerID()).getUsername()
							+ Messages.getString("ClientThread.WerdenAbgezogen") //$NON-NLS-1$
							+ +-resources[0]
							+ Messages.getString("ClientThread.Wool-") //$NON-NLS-1$
							+ -resources[1]
							+ Messages.getString("ClientThread.Lumber-") //$NON-NLS-1$
							+ -resources[2]
							+ Messages.getString("ClientThread.Ore-") //$NON-NLS-1$
							+ -resources[3]
							+ Messages.getString("ClientThread.Brick-") + -resources[4]); //$NON-NLS-1$
					controller.refresh();
					if (moreThanSevenCount == 0) {
						controller.setDiscarding(false);
						if (isCurrentPlayer()) {
							controller.activateRobberMoving();
							controller
									.appendMessage(Messages.getString("ClientThread.RaeuberVerschieben")); //$NON-NLS-1$
						} else {
							controller
									.appendMessage(Messages.getString("ClientThread.RaeuberGehtUm")); //$NON-NLS-1$
						}
					}
					break;
				case Constants.MESSAGE_PIPS:
					pipsOne = message.getPips1();
					pipsTwo = message.getPips2();
					pips = pipsOne + pipsTwo;
					controller.hideDices();
					song.playSound(ImportAudio.rollDice, false);
					controller.showDices(pipsOne, pipsTwo);
					//currentPlayer = (message.getSettlerID() + 1) % client.getIsland().getSettlers().length;
					//controller.getIsland().setCurrentPlayer(controller.getIsland().getSettler(currentPlayer));
					if (client.getID() == message.getSettlerID()) {
						controller.appendMessage(Messages.getString("ClientThread.DuHastEine") + pips //$NON-NLS-1$
								+ Messages.getString("ClientThread.Gewuerfelt")); //$NON-NLS-1$
					} else {
						controller.appendMessage(client.getIsland()
								.getSettler(message.getSettlerID())
								.getUsername()
								+ Messages.getString("ClientThread.HatEine") + pips + Messages.getString("ClientThread.Gewuerfelt")); //$NON-NLS-1$ //$NON-NLS-2$
					}
					controller.refresh();
					break;
				case Constants.MESSAGE_ROBBER:
					Tile robberTile = message.getTile();
					if (robberTile != null) {
						client.getIsland().moveRobber(
								client.getIsland().getTileArray1DOfIndex(
										robberTile.getIndex()));
						controller.repaintMap();
						if (isCurrentPlayer()) {
							if (message.getPlayerIds().size() != 0) {
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberVerschobenKlauenDuerfen")); //$NON-NLS-1$
								controller.activateRobbing(message
										.getPlayerIds());
							} else {
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberVerschobenKannNichtKlauen")); //$NON-NLS-1$
								controller.chooseAction();
							}
						} else {
							if (message.getPlayerIds().size() != 0)
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberVerschobenKlauen")); //$NON-NLS-1$
							else
								controller
										.appendMessage(Messages.getString("ClientThread.RaeuberVerschobenNichtKlauen")); //$NON-NLS-1$
							controller.chooseAction();
						}
					} else if (message.getResource() == 0) {
						if (isCurrentPlayer()) {
							controller.appendMessage(Messages.getString("ClientThread.DuKonntestVon") //$NON-NLS-1$
									+ client.getIsland()
											.getSettler(message.getSettlerID())
											.getUsername() + Messages.getString("ClientThread.NichtsKlauen")); //$NON-NLS-1$
							controller.chooseAction();
						} else if (message.getSettlerID() == client.getID())
							controller.appendMessage(client.getIsland()
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ClientThread.KonnteDirNichtsKlauen")); //$NON-NLS-1$
						else
							controller.appendMessage(client.getIsland()
									.getCurrentPlayer().getUsername()
									+ "" //$NON-NLS-1$
									+ client.getIsland()
											.getSettler(message.getSettlerID())
											.getUsername() + Messages.getString("ClientThread.NichtsKlauen")); //$NON-NLS-1$
							
					} else if (message.getResource() > 0) {
						client.getIsland().getSettler(message.getSettlerID());
						if (isCurrentPlayer()) {
							client.getIsland().getCurrentPlayer()
									.addResource(message.getResource(), 1);
							client.getIsland()
									.getSettler(message.getSettlerID())
									.addResources(-1);
							controller.appendMessage(Messages.getString("ClientThread.DuKonntestVon") //$NON-NLS-1$
									+ client.getIsland()
											.getSettler(message.getSettlerID())
											.getUsername() + " 1 " //$NON-NLS-1$
									+ toStringResource(message.getResource())
									+ Messages.getString("ClientThread.Klauen")); //$NON-NLS-1$
							controller.chooseAction();
						} else if (client.getID() == message.getSettlerID()) {
							client.getSettler().addResource(
									message.getResource(), -1);
							client.getIsland().getCurrentPlayer()
									.addResources(1);
							controller.appendMessage(client.getIsland()
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ClientThread.HatDirGeklaut") //$NON-NLS-1$
									+ toStringResource(message.getResource())
									+ Messages.getString("ClientThread.Geklaut")); //$NON-NLS-1$
						} else {
							client.getIsland().getCurrentPlayer()
									.addResources(1);
							client.getIsland()
									.getSettler(message.getSettlerID())
									.addResources(-1);
							controller.appendMessage(client.getIsland()
									.getCurrentPlayer().getUsername()
									+ Messages.getString("ClientThread.HatVon") //$NON-NLS-1$
									+ client.getIsland()
											.getSettler(message.getSettlerID())
											.getUsername()
									+ " 1 " //$NON-NLS-1$
									+ toStringResource(message.getResource())
									+ Messages.getString("ClientThread.Klauen")); //$NON-NLS-1$
						}
						clientLogger.writeLog(
								Level.INFO,
								client.getIsland().getCurrentPlayer()
										.getUsername()
										+ Messages.getString("ClientThread.HatVon") //$NON-NLS-1$
										+ client.getIsland()
												.getSettler(
														message.getSettlerID())
												.getUsername()
										+ " " //$NON-NLS-1$
										+ toStringResource(message
												.getResource()) + Messages.getString("ClientThread.Geklaut")); //$NON-NLS-1$
						controller.refresh();
					}
					break;
				case Constants.MESSAGE_DEV_CARD:
					if (message.isCardDrawn()) {
						if (message.isAllowed()) {
							if (isCurrentPlayer()) {
								if(message.getDevCard() != Constants.VICTORYPOINTS)
									client.getSettler().addTempDevelopmentCard(
											message.getDevCard());
								else
									client.getSettler().addDevelopmentCard(message.getDevCard());
								controller
										.appendMessage(Messages.getString("ClientThread.DuErfolgreichEine") //$NON-NLS-1$
												+ toStringDevCard(message
														.getDevCard())
												+ Messages.getString("ClientThread.Erworben")); //$NON-NLS-1$
							} else {
								client.getIsland().getCurrentPlayer()
										.addOneDevelopmentCard();
								controller
										.appendMessage(client.getIsland()
												.getCurrentPlayer()
												.getUsername()
												+ Messages.getString("ClientThread.HatKarteErworben")); //$NON-NLS-1$
							}
							discount(client.getCurrentPlayer(),
									Constants.DEVELOPMENTCARD);
							client.getIsland().addDrawnDevCard();
							clientLogger.writeLog(Level.INFO, client
									.getIsland().getCurrentPlayer()
									.getUsername()
									+ Messages.getString("ClientThread.HatKarteErworben")); //$NON-NLS-1$
						} else {
							if(message.getDevCard() < 0)
								controller.appendMessage(Messages.getString("ClientThread.AllKartenVerkauft")); //$NON-NLS-1$
							else
								controller.appendMessage(Messages.getString("ClientThread.NichtRohstoffeFuerKarte")); //$NON-NLS-1$
						}
						controller.refresh();
						controller.chooseAction();
					} else {
						byte devCard = message.getDevCard();
						if (!message.isAllowed() && !message.getFirstCard()) {
							if (client.getSettler().hasTempCard(devCard))
								controller
										.appendMessage(Messages.getString("ClientThread.KarteNochNichtSpielen")); //$NON-NLS-1$
							else
								controller
										.appendMessage(Messages.getString("ClientThread.KarteNochNichtBesitzen")); //$NON-NLS-1$
						} else if (!message.getFirstCard()) {
							controller
									.appendMessage(Messages.getString("ClientThread.RundeSchonKarteGespielt")); //$NON-NLS-1$
						} else {
							switch (devCard) {
							case Constants.KNIGHT:
								if (isCurrentPlayer()) {
									client.getSettler().removeDevelopmentCard(
											devCard);
									client.getSettler().addKnight();
									controller.activateRobberMoving();
									controller.appendMessage(Messages.getString("ClientThread.DuRitterGespielt")); //$NON-NLS-1$
									controller.setCardMenuVisible(false);
								} else {
									client.getIsland().getCurrentPlayer()
											.removeOneDevelopmentCard();
									client.getIsland().getCurrentPlayer()
											.addKnight();
									controller
											.appendMessage(client.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.RitterGespielt")); //$NON-NLS-1$
								}
								controller.refresh();
								int playerId = message.getSettlerID();
								if (playerId > -1) {
									for (int i = 0; i < client.getIsland()
											.getSettlers().length; i++) {
										if (i == playerId) {
											client.getIsland()
													.getSettler(playerId)
													.setLargestArmy(true);
										} else {
											client.getIsland().getSettler(i)
													.setLargestArmy(false);
										}
										client.getIsland().getSettlers()[i]
												.calculateVictoryPoints();
									}
									controller.refreshLargestArmyView(message
											.getSettlerID());
									if(client.getIsland().getPreviousArmyHolderID() != playerId) {
										controller.appendMessage(client.getIsland().getCurrentPlayer().getUsername() + Messages.getString("ClientThread.GroessteRittermacht")); //$NON-NLS-1$
										client.getIsland().setPreviousArmyHolderID(playerId);
									}
									controller.refresh();
								}
								clientLogger.writeLog(Level.INFO, client
										.getIsland().getCurrentPlayer()
										.getUsername()
										+ Messages.getString("ClientThread.RitterKarteGespielt")); //$NON-NLS-1$
								break;
							case Constants.VICTORYPOINTS:
								if (isCurrentPlayer()) {
									client.getSettler().removeDevelopmentCard(
											devCard);
									client.getSettler().addScore(1);
									controller
											.appendMessage(Messages.getString("ClientThread.DuSiegpunktKarteGespielt")); //$NON-NLS-1$
									controller.refresh();
								} else {
									client.getIsland().getCurrentPlayer()
											.removeOneDevelopmentCard();
									client.getIsland().getCurrentPlayer()
											.addScore(1);
									controller
											.appendMessage(client.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.SiegpunktKarteGespielt")); //$NON-NLS-1$
									controller.refresh();
								}
								clientLogger.writeLog(Level.INFO, client
										.getIsland().getCurrentPlayer()
										.getUsername()
										+ Messages.getString("ClientThread.SiegpunktKarteGespieltLog")); //$NON-NLS-1$
								break;
							case Constants.BUILDSTREETS:
								if (isCurrentPlayer()) {
									client.getSettler().removeDevelopmentCard(
											devCard);
									activateRoadCard();
									controller.activateRoadCard();
									controller
											.appendMessage(Messages.getString("ClientThread.DuStrassenBauKarteGespielt")); //$NON-NLS-1$
									controller.setCardMenuVisible(false);
								} else {
									client.getIsland().getCurrentPlayer()
											.removeOneDevelopmentCard();
									activateRoadCard();
									controller
											.appendMessage(client.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.StrassenBauKarteGespielt")); //$NON-NLS-1$
									clientLogger
											.writeLog(
													Level.INFO,
													client.getIsland()
															.getCurrentPlayer()
															.getUsername()
															+ Messages.getString("ClientThread.StrassenBauKarteGespieltLog")); //$NON-NLS-1$
								}
								controller.refresh();
								break;
							case Constants.GETRESOURCES:
								if (message.getResource() < 0) {
									if (isCurrentPlayer()) {
										client.getSettler()
												.removeDevelopmentCard(devCard);
										controller.showResources(devCard);
										controller
												.appendMessage(Messages.getString("ClientThread.DuErfindungsKarteGespielt")); //$NON-NLS-1$
										controller.setCardMenuVisible(false);
									} else {
										client.getIsland().getCurrentPlayer()
												.removeOneDevelopmentCard();
										controller
												.appendMessage(client
														.getIsland()
														.getCurrentPlayer()
														.getUsername()
														+ Messages.getString("ClientThread.ErfindungsKarteGespielt")); //$NON-NLS-1$
									}
								} else {
									if (isCurrentPlayer()) {
										client.getSettler().addResource(
												message.getResource(), 1);
										client.getSettler().addResource(
												message.getResource2(), 1);
										controller.refreshOpponents();
										if (message.getResource() == message
												.getResource2())
											controller
													.appendMessage(Messages.getString("ClientThread.DuHast2") //$NON-NLS-1$
															+ toStringResource(message
																	.getResource())
															+ Messages.getString("ClientThread.Bekommen")); //$NON-NLS-1$
										else
											controller.appendMessage(Messages.getString("ClientThread.DuHast") //$NON-NLS-1$
													+ toStringResource(message
															.getResource())
													+ Messages.getString("ClientThread.Und") //$NON-NLS-1$
													+ toStringResource(message
															.getResource2())
													+ Messages.getString("ClientThread.Bekommen")); //$NON-NLS-1$
										controller.chooseAction();
									} else {
										client.getIsland().getCurrentPlayer()
												.addResources(2);
										if (message.getResource() == message
												.getResource2())
											controller.appendMessage(client
													.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.Hat2") //$NON-NLS-1$
													+ toStringResource(message
															.getResource())
													+ Messages.getString("ClientThread.Bekommen")); //$NON-NLS-1$
										else
											controller.appendMessage(client
													.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.Hat") //$NON-NLS-1$
													+ toStringResource(message
															.getResource())
													+ Messages.getString("ClientThread.Und") //$NON-NLS-1$
													+ toStringResource(message
															.getResource2())
													+ Messages.getString("ClientThread.Bekommen")); //$NON-NLS-1$

										clientLogger
												.writeLog(
														Level.INFO,
														client.getIsland()
																.getCurrentPlayer()
																.getUsername()
																+ Messages.getString("ClientThread.ErfindungsKarteGespieltBekommt") //$NON-NLS-1$
																+ toStringResource(message
																		.getResource())
																+ Messages.getString("ClientThread.Und") //$NON-NLS-1$
																+ toStringResource(message
																		.getResource2()));

									}
								}
								controller.refresh();
								break;
							case Constants.MONOPOLY:
								if (message.getResource() < 0) {
									if (isCurrentPlayer()) {
										client.getSettler()
												.removeDevelopmentCard(devCard);
										controller.showResources(devCard);
										controller
												.appendMessage(Messages.getString("ClientThread.DuMonopolKarteGespielt")); //$NON-NLS-1$
										controller.setCardMenuVisible(false);
									} else {
										client.getIsland().getCurrentPlayer()
												.removeOneDevelopmentCard();
										controller
												.appendMessage(client
														.getIsland()
														.getCurrentPlayer()
														.getUsername()
														+ Messages.getString("ClientThread.MonopolKarteGespielt")); //$NON-NLS-1$
									}
									clientLogger
											.writeLog(
													Level.INFO,
													client.getIsland()
															.getCurrentPlayer()
															.getUsername()
															+ Messages.getString("ClientThread.MonopolKarteGespielt")); //$NON-NLS-1$
								} else {
									if (message.getAmountOfResource() < 0) {
										if (!isCurrentPlayer()) {
											int amount = client
													.getSettler()
													.getAmountOfResource(
															message.getResource());
											controller.sendMessage(new Message(
													devCard, message
															.getResource(),
													amount, client.getID()));
										}
									} else {
										int amount = message
												.getAmountOfResource();
										if (isCurrentPlayer()) {
											client.getSettler().addResource(
													message.getResource(),
													amount);
											client.getIsland()
													.getSettler(
															message.getSettlerID())
													.addResources(-amount);
											controller
													.appendMessage(Messages.getString("ClientThread.DuBekommstVon") //$NON-NLS-1$
															+ client.getIsland()
																	.getSettler(
																			message.getSettlerID())
																	.getUsername()
															+ " " //$NON-NLS-1$
															+ amount
															+ " " //$NON-NLS-1$
															+ toStringResource(message
																	.getResource())
															+ "!"); //$NON-NLS-1$
											controller.chooseAction();
										} else if (client.getIsland()
												.getSettler(
														message.getSettlerID()) == client
												.getSettler()) {
											client.getIsland()
													.getCurrentPlayer()
													.addResources(amount);
											client.getSettler().addResource(
													message.getResource(),
													-amount);
											controller.appendMessage(client
													.getIsland()
													.getCurrentPlayer()
													.getUsername()
													+ Messages.getString("ClientThread.BekommtVonDir") //$NON-NLS-1$
													+ amount
													+ " " //$NON-NLS-1$
													+ toStringResource(message
															.getResource())
													+ "!"); //$NON-NLS-1$
										} else {
											client.getIsland()
													.getSettler(
															message.getSettlerID())
													.addResources(-amount);
											client.getIsland()
													.getCurrentPlayer()
													.addResources(amount);
											controller
													.appendMessage(client
															.getIsland()
															.getCurrentPlayer()
															.getUsername()
															+ Messages.getString("ClientThread.BekommtVon") //$NON-NLS-1$
															+ client.getIsland()
																	.getSettler(
																			message.getSettlerID())
																	.getUsername()
															+ " " //$NON-NLS-1$
															+ amount
															+ " " //$NON-NLS-1$
															+ toStringResource(message
																	.getResource())
															+ "!"); //$NON-NLS-1$
										}
									}
								}
								controller.refresh();
								break;
							default:
								break;
							}
						}
					}
					break;
				case Constants.MESSAGE_GAME_END:
					if(!end) {
						end = true;
						song.setMuted(false);
						song.playSound(ImportAudio.endingTheme, false);
						song.setMuted(true);
						if(client.getID() == message.getSettlerID()) {
							controller.appendMessage(Messages.getString("ClientThread.DuGewonnen")); //$NON-NLS-1$
						} else {
							controller.appendMessage(client.getIsland().getSettler(message.getSettlerID()).getUsername() + Messages.getString("ClientThread.Gewonnen")); //$NON-NLS-1$
						}
					}
					controller.block(true);
					controller.gameEnd();
					break;
				case Constants.MESSAGE_PLAYER_TRADE:
					if (message.getOfferedResources().length == 0) {
						controller.appendMessage(Messages.getString("ClientThread.NichtRohstoffeFuerHandel")); //$NON-NLS-1$
					} else if (message.isAllowed()) {
						traderCounter = 0;
						acceptedCounter = 0;
//						tradeeIDs = new ArrayList<Integer>();
						if (isCurrentPlayer()) {
							controller.appendMessage(Messages.getString("ClientThread.HandelEntscheiden")); //$NON-NLS-1$
//							controller.block(true);
							controller.activateTradees(message.getOfferedResources(), message.getExpectedResources());
						} else {
							controller.activateConfirmTrade(message.getOfferedResources(),message.getExpectedResources());
							controller.appendMessage(Messages.getString("ClientThread.HandelAnnehmenWaehlen")); //$NON-NLS-1$
						}
					} else if (!message.isAnswered()) {
						if(message.isAccepted()) {
							acceptedCounter++;
							if(isCurrentPlayer()) 
								controller.switchTradeeStatus(message.getSettlerID(), true);
						} else {
							if(isCurrentPlayer())
								controller.switchTradeeStatus(message.getSettlerID(), false);
						}
						if(traderCounter == client.getIsland().getSettlers().length -2 && acceptedCounter == 0) {
							if(isCurrentPlayer())
								controller.appendMessage(Messages.getString("ClientThread.KeinerHandelAngenommen")); //$NON-NLS-1$
							else
								controller.appendMessage(Messages.getString("ClientThread.HandelNichtZustande")); //$NON-NLS-1$
						}
						traderCounter++;					
//						
//						
//						
//						
//						if (message.getSettlerID() >= 0) {
//							tradeeIDs.add(message.getSettlerID());
//						}
//						if (traderCounter == client.getIsland().getSettlers().length - 2) {
//							controller.block(false);
//							if (tradeeIDs.size() == 0) {
//								if (isCurrentPlayer()) {
//									controller.appendMessage("Keiner deiner Mitspieler hat den Handel angenommen. Sorry!");
//								} else {
//									controller
//											.appendMessage("Der Handel kam nicht zustande.");
//								}
//							} else {
//								if (isCurrentPlayer()) {
//									controller
//											.appendMessage("W\u00E4hle ob und mit wem du handeln willst.");
//									controller.activateTradees(tradeeIDs,
//											message.getOfferedResources(),
//											message.getExpectedResources());
//								} else {
//									controller
//											.appendMessage(client.getIsland()
//													.getCurrentPlayer()
//													.getUsername()
//													+ " entscheidet gerade ob er den Handel vollziehen will.");
//								}
//							}
//						}
//						traderCounter++;
//						
//						
					} else if (message.isAnswered()) {
						if(!isCurrentPlayer()) {
							controller.hideConfirmPanel();
						}
						int[] offR = message.getOfferedResources();
						int[] expR = message.getExpectedResources();
						int amountTradee = (offR[0] - expR[0])
								+ (offR[1] - expR[1]) + (offR[2] - expR[2])
								+ (offR[3] - expR[3]) + (offR[4] - expR[4]);
						int amountTrader = (expR[0] - offR[0])
								+ (expR[1] - offR[1]) + (expR[2] - offR[2])
								+ (expR[3] - offR[3]) + (expR[4] - offR[4]);
						if (message.getSettlerID() < 0) {
							if (isCurrentPlayer())
								controller
										.appendMessage(Messages.getString("ClientThread.DuHandelAbgelehnt")); //$NON-NLS-1$
							else
								controller.appendMessage(client.getIsland()
										.getCurrentPlayer().getUsername()
										+ Messages.getString("ClientThread.HandelAbgebrochen")); //$NON-NLS-1$
						} else {
							if (isCurrentPlayer()) {
								Settler current = client.getSettler();
								current.addWool(expR[0] - offR[0]);
								current.addOre(expR[1] - offR[1]);
								current.addBrick(expR[2] - offR[2]);
								current.addLumber(expR[3] - offR[3]);
								current.addGrain(expR[4] - offR[4]);
								client.getIsland()
										.getSettler(message.getSettlerID())
										.addResources(amountTradee);
								controller.appendMessage(Messages.getString("ClientThread.DuHastMit") //$NON-NLS-1$
										+ client.getIsland()
												.getSettler(
														message.getSettlerID())
												.getUsername()
										+ Messages.getString("ClientThread.HandelnErfolgreich")); //$NON-NLS-1$
								controller.refresh();
							} else if (client.getID() == message.getSettlerID()) {
								Settler tradee = client.getSettler();
								tradee.addWool(offR[0] - expR[0]);
								tradee.addOre(offR[1] - expR[1]);
								tradee.addBrick(offR[2] - expR[2]);
								tradee.addLumber(offR[3] - expR[3]);
								tradee.addGrain(offR[4] - expR[4]);
								client.getIsland().getCurrentPlayer()
										.addResources(amountTrader);
								controller.appendMessage(Messages.getString("ClientThread.DuHastMit") //$NON-NLS-1$
										+ client.getIsland().getCurrentPlayer()
												.getUsername()
										+ Messages.getString("ClientThread.HandelnErfolgreich")); //$NON-NLS-1$
								controller.refresh();
							} else {
								client.getIsland().getCurrentPlayer()
										.addResources(amountTrader);
								client.getIsland()
										.getSettler(message.getSettlerID())
										.addResources(amountTradee);
								controller.appendMessage(client.getIsland()
										.getCurrentPlayer().getUsername()
										+ Messages.getString("ClientThread.ErfolgreichGehandelt") //$NON-NLS-1$
										+ client.getIsland()
												.getSettler(
														message.getSettlerID())
												.getUsername() + Messages.getString("ClientThread.Gehandelt")); //$NON-NLS-1$
								controller.refresh();
							}
							clientLogger.writeLog(
									Level.INFO,
									client.getIsland()
											.getSettler(message.getSettlerID())
											.getUsername()
											+ Messages.getString("ClientThread.HatMit") //$NON-NLS-1$
											+ client.getIsland()
													.getCurrentPlayer()
													.getUsername()
											+ Messages.getString("ClientThread.HandelnErfolgreich")); //$NON-NLS-1$
							controller.refresh();
						}
					}
					break;
				case Constants.MESSAGE_BANK_TRADE:
					int[] expectedResources = message.getExpectedResources();
					int[] offeredResources = message.getOfferedResources();

					// nachfolgendend wird der buchungssatz gebildet
					String expR = ""; //$NON-NLS-1$
					String offR = ""; //$NON-NLS-1$
					int counterL = 0;
					int counterR = 0;
					for (int i = 0; i < expectedResources.length; i++) {
						if (expectedResources[i] != 0) {
							String append = "";							 //$NON-NLS-1$
							switch (i) {
							case 0:
								append = Messages.getString("ClientThread.Wolle"); //$NON-NLS-1$
								break;
							case 1:
								append = Messages.getString("ClientThread.Erz"); //$NON-NLS-1$
								break;
							case 2:
								append = Messages.getString("ClientThread.Lehm"); //$NON-NLS-1$
								break;
							case 3:
								append = Messages.getString("ClientThread.Holz"); //$NON-NLS-1$
								break;
							case 4:
								append = Messages.getString("ClientThread.Weizen"); //$NON-NLS-1$
								break;
							default:
								break;
							}
							if (counterL == 0) {
								expR = expectedResources[i] + append;
							}
							else {
								expR = expR + Messages.getString("ClientThread.Und") + expectedResources[i] + append; //$NON-NLS-1$
							}
							
							counterL++;
						}
						if (offeredResources[i] != 0) {
							String append = "";	 //$NON-NLS-1$
							switch (i) {
							case 0:
								append = Messages.getString("ClientThread.Wolle"); //$NON-NLS-1$
								break;
							case 1:
								append = Messages.getString("ClientThread.Erz"); //$NON-NLS-1$
								break;
							case 2:
								append = Messages.getString("ClientThread.Lehm"); //$NON-NLS-1$
								break;
							case 3:
								append = Messages.getString("ClientThread.Holz"); //$NON-NLS-1$
								break;
							case 4:
								append = Messages.getString("ClientThread.Weizen"); //$NON-NLS-1$
								break;
							default:
								break;
							}
							if (counterR == 0) {
								offR = offeredResources[i] + append;
							}
							else {
								offR = offR + Messages.getString("ClientThread.Und") + offeredResources[i] + append; //$NON-NLS-1$
							}
							counterR++;
						}
					}
					// wenn es sich um den handelnden spieler handelt
					if (isCurrentPlayer()) {
						controller
								.appendMessage(Messages.getString("ClientThread.DuBankHandel") //$NON-NLS-1$
										+ System.getProperty("line.separator") //$NON-NLS-1$
										+ Messages.getString("ClientThread.DuTauscht") //$NON-NLS-1$
										+ offR
										+ Messages.getString("ClientThread.Gegen") //$NON-NLS-1$
										+ expR + "!"); //$NON-NLS-1$
					} else {
						controller.appendMessage(client.getIsland()
								.getCurrentPlayer().getUsername()
								+ Messages.getString("ClientThread.MitBankGehandelt") //$NON-NLS-1$
								+ client.getIsland().getCurrentPlayer()
										.getUsername()
								+ Messages.getString("ClientThread.Tauscht") //$NON-NLS-1$
								+ offR
								+ Messages.getString("ClientThread.Gegen") + expR + "!"); //$NON-NLS-1$ //$NON-NLS-2$
					}

					clientLogger.writeLog(Level.INFO, client.getIsland()
							.getCurrentPlayer().getUsername()
							+ Messages.getString("ClientThread.MitBankGehandelt") //$NON-NLS-1$
							+ client.getIsland().getCurrentPlayer()
									.getUsername()
							+ Messages.getString("ClientThread.Tauscht") //$NON-NLS-1$
							+ offR
							+ Messages.getString("ClientThread.Gegen") + expR + "!"); //$NON-NLS-1$ //$NON-NLS-2$

					// resourcen werden abgezogen
					client.getIsland()
							.getCurrentPlayer()
							.addWool(expectedResources[0] - offeredResources[0]);
					client.getIsland().getCurrentPlayer()
							.addOre(expectedResources[1] - offeredResources[1]);
					client.getIsland()
							.getCurrentPlayer()
							.addBrick(
									expectedResources[2] - offeredResources[2]);
					client.getIsland()
							.getCurrentPlayer()
							.addLumber(
									expectedResources[3] - offeredResources[3]);
					client.getIsland()
							.getCurrentPlayer()
							.addGrain(
									expectedResources[4] - offeredResources[4]);

					// anzahl der resourcen wird hochgesetzt
					int amount = 0;
					for (int i = 0; i < message.getExpectedResources().length; i++) {
						amount += (expectedResources[i] - offeredResources[i]);
					}
					client.getIsland().getCurrentPlayer().addResources(amount);

					controller.refresh();
					controller.chooseAction();
					break;
				case Constants.MESSAGE_CHEAT:
					int id = message.getSettlerID();
					byte resource = message.getResource();
					int amountOfResource = message.getAmountOfResource();
					if (id == client.getID()) {
						client.getIsland().changeAmountOfResource(resource,
								amountOfResource, id);
//						controller.appendMessage("cheating bastard ;)");
						controller.appendMessage(Messages.getString("ClientThread.CheatAktiviert")); //$NON-NLS-1$
					}
					else {
						client.getIsland().getSettler(id).addResources(amountOfResource);
					}
					controller.refresh();
					clientLogger.writeLog(Level.INFO, client.getIsland()
							.getSettler(id).getUsername()
							+ Messages.getString("ClientThread.HatGecheatet")); //$NON-NLS-1$
					break;
				case Constants.MESSAGE_CHEAT_ACTIVATION:
					boolean allowedToCheat = message.isAllowedToCheat();
					client.getIsland().setAllowedToCheat(allowedToCheat);
					clientLogger.writeLog(Level.INFO,
							Messages.getString("ClientThread.CheatsVonAktiviert")); //$NON-NLS-1$
					break;
				case Constants.MESSAGE_END_OF_TURN:
					if (!client.isBeginning()) {
						controller.hideDices();
					}
					if (isCurrentPlayer()) {
						// alle gui-elemente, die noch offen sind, werden nach
						// beenden der zuges unsichtbar gemacht
						controller.setViewsVisible(false);
					}
					client.setBeginning(message.isBeginning());
					currentPlayer = client.getCurrentPlayer();
					if (message.isBeginning()) {
						client.getIsland().setCurrentPlayer(
								client.getIsland().getSettler(
										message.getSettlerID()));
					} else {
						client.getSettler().addTempCardsToDevCards();
						client.nextPlayer();
					}
					if (currentPlayer != client.getCurrentPlayer()) {
						controller.appendMessage(client.getIsland()
								.getSettler(client.getCurrentPlayer())
								.getUsername()
								+ Messages.getString("ClientThread.AnDerReihe")); //$NON-NLS-1$
					}
					controller.refresh();
					break;
				case Constants.MESSAGE_PLAYER_LEFT:
					controller.appendMessage(client.getIsland().getSettler(message.getSettlerID()).getUsername() + Messages.getString("ClientThread.SpielVerlassen")); //$NON-NLS-1$
					controller.block(true);
					break;
				default:
					break;
				}
			
		} catch (IOException e) {
			if(MainGUI.isStarted()){
			clientExceptionLogger.writeException(Level.SEVERE,
					Messages.getString("ClientThread.ClientThread.IOException")); //$NON-NLS-1$
			controller.appendMessage(Messages.getString("ClientThread.ServerGeschlossen")); //$NON-NLS-1$
			controller.block(true);
			}
			ServerGUIClient.serverClosed();

			break loop;
		} catch (ClassNotFoundException e) {
			clientExceptionLogger.writeException(Level.SEVERE,
					Messages.getString("ClientThread.ClassNotFoundExceptionRun")); //$NON-NLS-1$
			break loop;
		}
		}
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
		if (!client.isBeginning()) {
			Settler settler = client.getIsland().getSettler(id);
			switch (building) {
			case Constants.CITY:
				settler.addOre(-3);
				settler.addGrain(-2);
				settler.addResources(-5);
				break;
			case Constants.SETTLEMENT:
				settler.addLumber(-1);
				settler.addBrick(-1);
				settler.addGrain(-1);
				settler.addWool(-1);
				settler.addResources(-4);
				break;
			case Constants.ROAD:
				settler.addLumber(-1);
				settler.addBrick(-1);
				settler.addResources(-2);
				break;
			case Constants.DEVELOPMENTCARD:
				settler.addGrain(-1);
				settler.addWool(-1);
				settler.addOre(-1);
				settler.addResources(-3);
			default:
				break;
			}
		}
	}

	/**
	 * Gibts zur&uuml;ck, ob man der aktuelle Spieler ist.
	 * 
	 * @return Ja oder nein
	 */
	public boolean isCurrentPlayer() {
		return client.getIsland().getCurrentPlayer().getID() == client.getID();
	}

	/**
	 * Erzeugt einen <code>String</code> f&uuml;r eine Entwicklungskarten.
	 * 
	 * @param devCard
	 *            Entwicklungskarte
	 * @return <code>String</code> zur Entwicklungskarte
	 */
	public String toStringDevCard(byte devCard) {
		switch (devCard) {
		case Constants.VICTORYPOINTS:
			return Messages.getString("ClientThread.SiegpunktKarte"); //$NON-NLS-1$
		case Constants.KNIGHT:
			return Messages.getString("ClientThread.RitterKarte"); //$NON-NLS-1$
		case Constants.GETRESOURCES:
			return Messages.getString("ClientThread.ErfindungsKarte"); //$NON-NLS-1$
		case Constants.BUILDSTREETS:
			return Messages.getString("ClientThread.StrassenBauKarte"); //$NON-NLS-1$
		case Constants.MONOPOLY:
			return Messages.getString("ClientThread.MonopolKarte"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Erzeugt einen <code>String</code> f&uuml;r eine Rohstoffkarte.
	 * 
	 * @param resource
	 *            Rohstoff
	 * @return <code>String</code> zum Rohstoff
	 */
	public String toStringResource(byte resource) {
		switch (resource) {
		case Constants.BRICK:
			return Messages.getString("ClientThread.Lehm"); //$NON-NLS-1$
		case Constants.LUMBER:
			return Messages.getString("ClientThread.Holz"); //$NON-NLS-1$
		case Constants.WOOL:
			return Messages.getString("ClientThread.Wolle"); //$NON-NLS-1$
		case Constants.ORE:
			return Messages.getString("ClientThread.Erz"); //$NON-NLS-1$
		case Constants.GRAIN:
			return Messages.getString("ClientThread.Getreide"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Ermittelt, wie viele Stra&szlig;en mit der Stra&szlig;enkarte gebaut
	 * werden k&ouml;nnen.
	 */
	public void activateRoadCard() {
		roadCardCount = (client.getIsland().getCurrentPlayer().getRoadCount() == 14) ? 1
				: 2;
	}
	
	public String generateResourceString(int[] resources) {
		String resourceString = ""; //$NON-NLS-1$
		for (int count = 0; count < resources.length; count++) {
			if (resources[count] > 0) {
				switch (count) {
				case 0:
					resourceString += resources[count]
							+ Messages.getString("ClientThread.XGetreide"); //$NON-NLS-1$
					break;
				case 1:
					resourceString += resources[count]
							+ Messages.getString("ClientThread.XWolle"); //$NON-NLS-1$
					break;
				case 2:
					resourceString += resources[count]
							+ Messages.getString("ClientThread.XHolz"); //$NON-NLS-1$
					break;
				case 3:
					resourceString += resources[count]
							+ Messages.getString("ClientThread.XErz"); //$NON-NLS-1$
					break;
				case 4:
					resourceString += resources[count]
							+ Messages.getString("ClientThread.XLehm"); //$NON-NLS-1$
					break;
				default:
				}
			}
		}
		return resourceString;
	}

	public LoggerClass getClientLogger() {
		return clientLogger;
	}

	public void setClientLogger(LoggerClass clientLogger) {
		this.clientLogger = clientLogger;
	}
	
	public MP3 getSong() {
		return song;
	}
}
