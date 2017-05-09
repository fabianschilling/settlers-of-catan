package controller;

import gui.*;
import model.*;
import audio.*;
import networking.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Der Controller \u00FCberwacht die Benutzereingaben auf der GUI und verwertet
 * sie.
 * 
 * @author Michael Strobl, Thomas Wimmer, Florian Weiss, Fabian Schilling, Christian Hemauer
 */

public class Controller implements ActionListener, KeyListener, MouseListener,
		MouseMotionListener, ControllerInterface {

	/**
	 * GUI des Chats.
	 */
	private ChatGUI chatGUI;

	/**
	 * Client des Spielers.
	 */
	private Client client;

	/**
	 * Komplette Spieloberfl&auml;che
	 */
	private MainGUI mainGUI;

	/**
	 * GUI der Insel.
	 */
	private PolygonMap polygonMap;

	/**
	 * Button f&uuml;r die Bebauung eines Knotens.
	 */
	@SuppressWarnings("unused")
	private BuildPanelNode buildPanelNode;

	/**
	 * Button f&uuml;r die Bebauung einer Stra&szlig;e.
	 */
	private BuildPanelRoad buildPanelRoad;

	/**
	 * GUI zum ausw&auml;hlen von Rohstoffen zum Abwerfen und f&uuml;r die
	 * Rohstoffkarte.
	 */
	private ResourcePanel resourcePanel;

	/**
	 * Handelsmen&uuml;
	 */
	private TradingMenu tradingMenu;

	/**
	 * Kartenmen/uuml;
	 */
	private CardMenu cardMenu;

	/**
	 * W&uuml;rfel
	 */
	private DicePanel dicePanel;

	/**
	 * Zeigt die Rohstoffe, Stra&szlig;en, Siedlungen, St&auml;dte und Karten
	 * des Spielers an.
	 */
	private SupplyPanel supplyPanel;

	/**
	 * Anzeige f&uuml;r die Baukosten.
	 */
	private BuildingCostsMenu buildingCostsMenuPanel;

	/**
	 * Geklickter Knoten.
	 */
	private Node clickedNode;

	/**
	 * Knoten, auf dem die letzte Siedlung gebaut wurde.
	 */
	private Node lastSettlementNode;

	/**
	 * Geklickte Stra&szlig;e.
	 */
	private Road clickedRoad;

	/**
	 * Insel
	 */
	private IslandOfCatan island;

	/**
	 * Thread, der Nachrichten vom Server empf&auml;ngt.
	 */
	private ClientThread thread;

	/**
	 * Anzahl der Siedlungen, die der Spieler in der Anfangsphase schon gebaut
	 * hat.
	 */
	private int beginningRoadCount;

	/**
	 * Anzahl an Stra&szlig;en, die der Spieler in der Anfangsphase schon gebaut
	 * hat.
	 */
	private int beginningSettlementCount;

	/**
	 * Blockiert die GUI, wenn gerade geklaut wird.
	 */
	private boolean isRobbing;

	/**
	 * Bereich des gerade geklickten Knotens.
	 */
	private Ellipse2D clickedNodeArea;

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
	 * x-Koordinate des Klicks.
	 */
	private int xCoord;

	/**
	 * y-Koordinate des Klicks.
	 */
	private int yCoord;

	/**
	 * Wenn gerade gebaut wurde.
	 */
	private boolean built;

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
	private boolean allowedToRoll;

	/**
	 * Wenn die komplette GUI geblockt ist.
	 */
	private boolean blocked;

	/**
	 * Enth&auml;lt die gecheateten Rohstoffe.
	 */
	private Vector<String> resourcesForCheat;
	
	/**
	 * Spieler ist gerade am Handeln.
	 */
	private boolean choosing;
	/**
	 * Variable f&uuml;r das Spielende
	 */
	private boolean gameEnd;
	
	/**
	 * Wahr, wenn es sich um einen Handel handelt
	 */
	@SuppressWarnings("unused")
	private boolean trading;
	
	/**
	 * Objekt zum abspielen der Audiofiles
	 */
	private MP3 song;

	/**
	 * Angebotene Rohstoffe im Handel.
	 */
	private int[] offR;

	/**
	 * Gesuchte Rohstoffe im handel.
	 */
	private int[] expR;

	private boolean isTradeActivated;

	private ExceptionLogger exceptionLogger;
	
	/**
	 * ServerGUI
	 */
	private ServerGUI serverGUI;
	
	/**
	 * Button f&uuml;r Siedlung oder Stadtbau
	 */
	private PlayerButton buildButton;
	/**
	 * Erzeugt ein Objekt der Klasse Controller
	 * 
	 * @param client
	 *            ist der Client aus dem Networking-Package
	 * @throws IOException
	 */
	public Controller(Client client, ServerGUI serverGUI) throws IOException {
		this.client = client;
		this.serverGUI = serverGUI;
		beginningRoadCount = 0;
		beginningSettlementCount = 0;
		built = false;
		allowedToRoll = false;
		song = new MP3();
		addAllowedCheats();
		thread = new ClientThread(client, client.getSocket(), this);
		thread.start();
		gameEnd = false;
		exceptionLogger = ExceptionLogger.getInstance();
	}

	public void startMainGUI(IslandOfCatan island) {
		serverGUI.setVisible(false);
		this.island = island;
		mainGUI = new MainGUI((Controller) this);
		this.chatGUI = mainGUI.getChatGUI();
		this.polygonMap = mainGUI.getPolygonMap();
//		this.buildPanelNode = mainGUI.getBuildPanel();
		this.buildPanelRoad = mainGUI.getBuildPanelRoad();
//		this.buildPanelCity = mainGUI.getBuildPanelCity();
		this.resourcePanel = mainGUI.getResourcePanel();
		this.tradingMenu = mainGUI.getTradingMenu();
		this.cardMenu = mainGUI.getCardMenu();
		this.supplyPanel = mainGUI.getSupplyPanel();
		this.dicePanel = mainGUI.getDicePanel();
		this.buildingCostsMenuPanel = mainGUI.getBuildingCostsMenuPanel();
		this.buildButton = mainGUI.getBuildButton();
	}

	/**
	 * Verwertet die Aktionen, auf die der Controller angewendet wurde
	 */
	public void actionPerformed(ActionEvent event) {
//		buildPanelNode.setVisible(false);
		buildPanelRoad.setVisible(false);
		buildButton.setVisible(false);
		polygonMap.setShowSettlementNodes(false);
		polygonMap.setShowStreets(false);
		polygonMap.setShowCityNodes(false);
		polygonMap.repaint();
		String cmd = event.getActionCommand().substring(5);
		String source = event.getActionCommand().substring(0, 4);

		if (client.getID() != island.getCurrentPlayer().getID()
				&& !source.equals("conf") && !source.equals("resP") //$NON-NLS-1$ //$NON-NLS-2$
				&& !cmd.equals("buildingcosts") && !source.equals("musi")) { //$NON-NLS-1$ //$NON-NLS-2$
			source = ""; //$NON-NLS-1$
		}
		
		if(discarding && !source.equals("resP")) { //$NON-NLS-1$
			source = ""; //$NON-NLS-1$
		}
		if (choosing && !source.equals("chos")) { //$NON-NLS-1$
			source = ""; //$NON-NLS-1$
		}
		if (tradingMenu.isVisible() && source.equals("chat") //$NON-NLS-1$
				&& source.equals("deal")) { //$NON-NLS-1$
			tradingMenu.setVisible(false);
			source = ""; //$NON-NLS-1$
		}
		if (roadCardCount > 0 && !source.equals("road")) { //$NON-NLS-1$
			appendMessage(Messages.getString("Controller.BauenStrasse1") + roadCardCount //$NON-NLS-1$
					+ Messages.getString("Controller.BauenStrasse2")); //$NON-NLS-1$
			source = ""; //$NON-NLS-1$
		}
		if (isRobbing && !source.equals("robb")) { //$NON-NLS-1$
			appendMessage(Messages.getString("Controller.GegnerWaehlen")); //$NON-NLS-1$
			source = ""; //$NON-NLS-1$
		}
		if (resourcePanel.isVisible() && !source.equals("resP")) { //$NON-NLS-1$
			appendMessage(Messages.getString("Controller.RohstoffeWaehlen")); //$NON-NLS-1$
			source = ""; //$NON-NLS-1$
		}
		if (isRobberActivated) {
			source = ""; //$NON-NLS-1$
		}
		if (blocked) {
			source = ""; //$NON-NLS-1$
		}
		if (source.equals("musi")) { //$NON-NLS-1$
			if (cmd.equals("play")) { //$NON-NLS-1$
				boolean playing = song.isAudioIsPlaying();
				if (playing) {
					song.stopSound();
					mainGUI.getPlayMusic().changeIconActivity(ImportImages.confirmBtn);
					mainGUI.getPlayMusic().setToolTipText(Messages.getString("Controller.MusicAbspielen")); //$NON-NLS-1$
				}
				else {
					song.playSound(ImportAudio.piratesTheme, true);
					mainGUI.getPlayMusic().changeIconActivity(ImportImages.cancelBtn);
					mainGUI.getPlayMusic().setToolTipText(Messages.getString("Controller.MusicStopen")); //$NON-NLS-1$
				}				
				
			}
			if (cmd.equals("mute")) {	 //$NON-NLS-1$
				boolean mute = !thread.getSong().isMuted();				
				thread.getSong().setMuted(mute);
				if (mute) {
					mainGUI.getMuteMusic().changeIconActivity(ImportImages.muteBtn);
					mainGUI.getMuteMusic().setToolTipText(Messages.getString("Controller.SoundUnMuten")); //$NON-NLS-1$
				}
				else {
					mainGUI.getMuteMusic().changeIconActivity(ImportImages.notMuteBtn);
					mainGUI.getMuteMusic().setToolTipText(Messages.getString("Controller.SoundMuten")); //$NON-NLS-1$
				}
		}
		}
		if (source.equals("node")) { //$NON-NLS-1$
			if (client.isFirstRoll()) {
				if (allowedToRoll) {
					appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
				} else {
					appendMessage(Messages.getString("Controller.NichtAnDerReihe")); //$NON-NLS-1$
				}
			} else {
				if (cmd.equals("settlement")) { //$NON-NLS-1$
					if ((clickedNode.isBuildable(island,
							client.getCurrentPlayer(), client.isBeginning())
							&& clickedNode.getBuilding() == Constants.UNBUILT && !client
								.isBeginning()) || client.getIsland().isTest()) {
						buildSettlement();
					} else if (!built
							&& clickedNode.isBuildable(island,
									client.getCurrentPlayer(),
									client.isBeginning())
							&& clickedNode.getBuilding() == Constants.UNBUILT
							&& client.isBeginning()
							&& beginningSettlementCount == 0
							&& beginningRoadCount == 0) {
						beginningSettlementCount++;
						buildSettlement();
						appendMessage(Messages.getString("Controller.DarfstStrasseBauen")); //$NON-NLS-1$
						built = true;
					} else if (!built
							&& clickedNode.isBuildable(island,
									client.getCurrentPlayer(),
									client.isBeginning())
							&& clickedNode.getBuilding() == Constants.UNBUILT
							&& client.isBeginning()
							&& beginningSettlementCount == 1
							&& beginningRoadCount == 1) {
						beginningSettlementCount++;
						buildSettlement();
						built = true;
						appendMessage(Messages.getString("Controller.DarfstStrasseBauen")); //$NON-NLS-1$
					} else {
						appendMessage(Messages.getString("Controller.DarfstKeineSiedlungBauen")); //$NON-NLS-1$
					}
				}
				if (cmd.equals("city")) { //$NON-NLS-1$
					if ((clickedNode.getBuilding() == Constants.SETTLEMENT
							&& clickedNode.getOwnerID() == client
									.getCurrentPlayer() && !client
								.isBeginning()) || client.getIsland().isTest()) {
						buildCity();
						built = true;
					} else {
						appendMessage(Messages.getString("Controller.DarfstKeineStadtBauen")); //$NON-NLS-1$
					}
				}
			}
		}
		if (source.equals("road") && cmd.equals("road")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (client.isFirstRoll()) {
				if (allowedToRoll) {
					appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
				} else {
					appendMessage(Messages.getString("Controller.NichtAnDerReihe")); //$NON-NLS-1$
				}
			} else {
				if ((clickedRoad.getOwner() == Constants.NOBODY
						&& !client.isBeginning() && clickedRoad.isBuildable(
						island, client.getCurrentPlayer()))
						|| client.getIsland().isTest()) {
					if (roadCardCount > 0)
						roadCardCount--;
					buildStreet();
				} else if (client.isBeginning() && beginningRoadCount == 0
						&& beginningSettlementCount == 1) {
					if (buildStreetIsAllowed()) {
						beginningRoadCount++;
						buildStreet();
					} else {
						appendMessage(Messages.getString("Controller.StrasseAnSiedlung")); //$NON-NLS-1$
					}
				} else if (client.isBeginning() && beginningRoadCount == 1
						&& beginningSettlementCount == 2) {
					if (buildStreetIsAllowed()) {
						beginningRoadCount++;
						buildStreet();
					} else {
						appendMessage(Messages.getString("Controller.StrasseAnSiedlung")); //$NON-NLS-1$
					}
				} else {
					appendMessage(Messages.getString("Controller.DarfstHierKeineStrasseBauen")); //$NON-NLS-1$
				}
			}
		}
		if (source.equals("menu")) { //$NON-NLS-1$
			if (cmd.equals("trade")) { //$NON-NLS-1$
				if (client.isFirstRoll()) {
					if (allowedToRoll) {
						appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
					} else {
						appendMessage(Messages.getString("Controller.NichtAnDerReihe")); //$NON-NLS-1$
					}
				} else {
					if (client.getCurrentPhase() >= Constants.PHASE_2
							&& !client.isBeginning()) {
						refresh();
						buildingCostsMenuPanel.setVisible(false);
						cardMenu.setVisible(false);
						tradingMenu.setVisible(!tradingMenu.isVisible());
					} else {
						appendMessage(Messages.getString("Controller.DarfstNichtHandeln")); //$NON-NLS-1$
					}
				}
			}
			if (cmd.equals("card")) { //$NON-NLS-1$
				if (client.isFirstRoll()) {
					if (allowedToRoll) {
						appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
					} else {
						appendMessage(Messages.getString("Controller.NichtAnDerReihe")); //$NON-NLS-1$
					}
				} else {
					buildingCostsMenuPanel.setVisible(false);
					tradingMenu.setVisible(false);
					refresh();
					cardMenu.setVisible(!cardMenu.isVisible());
				}
			}
			if (cmd.equals("buildingcosts")) { //$NON-NLS-1$
				buildingCostsMenuPanel.setVisible(!buildingCostsMenuPanel
						.isVisible());
				cardMenu.setVisible(false);
				tradingMenu.setVisible(false);
			}
			if (cmd.equals("next")) { //$NON-NLS-1$
				if (client.isFirstRoll()) {
					if (allowedToRoll) {
						appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
					} else {
						appendMessage(Messages.getString("Controller.NichtAnDerReihe")); //$NON-NLS-1$
					}
				} else {
					if (!client.isBeginning()) {
						if (client.getCurrentPhase() > Constants.PHASE_1) {
							built = false;
							if ((beginningRoadCount == 2 && beginningSettlementCount == 2)
									|| client.getIsland().isTest()) {
								client.setBeginning(false);
							}
							Message newMessage = new Message(false,
									client.isBeginning(), client.getID(), false);
							sendMessage(newMessage);
						} else {
							appendMessage(Messages.getString("Controller.Wuerfeln")); //$NON-NLS-1$
						}
					}
				}
			}
			if (cmd.equals("roll")) { //$NON-NLS-1$
				if ((client.getCurrentPhase() == Constants.PHASE_1 && !client
						.isBeginning())
						|| (client.isFirstRoll() && allowedToRoll)) {
					allowedToRoll = false;
					if (!client.isFirstRoll()) {
						client.setCurrentPhase(Constants.PHASE_2);
					}
					int firstDice = (int) Math
							.round((Math.random() * 6.0 + 0.5));
					int secondDice = (int) Math
							.round((Math.random() * 6.0 + 0.5));
					sendMessage(new Message(firstDice, secondDice,
							client.getID()));
				} else {
					appendMessage(Messages.getString("Controller.DarfstNichtWuerfeln")); //$NON-NLS-1$
				}
			}
		}
		if (source.equals("card")) { //$NON-NLS-1$
			if (cmd.equals("monopoly")) { //$NON-NLS-1$
				sendMessage(new Message(Constants.MONOPOLY, false));
			}
			if (cmd.equals("invention")) { //$NON-NLS-1$
				sendMessage(new Message(Constants.GETRESOURCES, (byte) -1,
						(byte) -1));
			}
			if (cmd.equals("victory")) { //$NON-NLS-1$
//				auskommentiert lassen
//				sendMessage(new Message(Constants.VICTORYPOINTS, false));
			}
			if (cmd.equals("streets")) { //$NON-NLS-1$
				sendMessage(new Message(Constants.BUILDSTREETS, false));
			}
			if (cmd.equals("knight")) { //$NON-NLS-1$
				sendMessage(new Message(Constants.KNIGHT, false));
			}
			if (cmd.equals("draw")) { //$NON-NLS-1$
				if (client.getCurrentPhase() == Constants.PHASE_3
						|| client.getCurrentPhase() == Constants.PHASE_2) {
					sendMessage(new Message(Constants.VICTORYPOINTS, true));
				} else {
					appendMessage(Messages.getString("Controller.DarfstKeineKarteKaufen")); //$NON-NLS-1$
				}
			}
			if(!cmd.equals("draw")) //$NON-NLS-1$
				cardMenu.setVisible(false);
		}
		if (source.equals("deal")) { //$NON-NLS-1$
			if (cmd.equals("trade")) { //$NON-NLS-1$
				sendMessage(tradingMenu.getMessage());
				setNoInteraction(true);
				tradingMenu.setVisible(false);
			}
		}
		if (source.equals("conf")) { //$NON-NLS-1$
			if (cmd.equals("confirm")) { //$NON-NLS-1$
				sendMessage(new Message(mainGUI.getConfirmTradePanel()
						.getOffR(), mainGUI.getConfirmTradePanel().getExpR(),
						client.getID(), false, true));
				appendMessage(Messages.getString("Controller.HandelAngenommen")); //$NON-NLS-1$
			}
			if (cmd.equals("cancel")) { //$NON-NLS-1$
				sendMessage(new Message(mainGUI.getConfirmTradePanel()
						.getOffR(), mainGUI.getConfirmTradePanel().getExpR(),
						client.getID(), false, false));
				appendMessage(Messages.getString("Controller.HandelAbgelehnt")); //$NON-NLS-1$
			}
			mainGUI.getConfirmTradePanel().setVisible(false);
		}
		if (source.equals("chos")) { //$NON-NLS-1$
			mainGUI.getTradeePanel().setVisible(false);
			choosing = false;
			sendMessage(new Message(offR, expR, Integer.parseInt(cmd), true, true));
			offR = new int[0];
			expR = new int[0];
		}
		if (source.equals("robb")) { //$NON-NLS-1$
			mainGUI.getRobberPanel().setVisible(false);
			isRobbing = false;
			sendMessage(new Message(Integer.parseInt(cmd), (byte) -1));
		}
		if (source.equals("resP")) { //$NON-NLS-1$
			if (isResourceCard) {
				if (cmd.equals("confirm")) { //$NON-NLS-1$
					if (resourcePanel.hasFirstResource()
							&& resourcePanel.hasSecondResource()) {
						sendMessage(new Message(Constants.GETRESOURCES,
								resourcePanel.getResourceByIndex(1),
								resourcePanel.getResourceByIndex(2)));
						resourcePanel.resetResources();
						isResourceCard = false;
						resourcePanel.setVisible(false);
					} else {
						appendMessage(Messages.getString("Controller.RohstoffeAuswaehlen2")); //$NON-NLS-1$
					}
				} else if (cmd.equals("cancel")) { //$NON-NLS-1$
					resourcePanel.resetResources();
				} else if (!resourcePanel.hasFirstResource()) {
					resourcePanel.setFirstResource(cmd, false);
				} else if (!resourcePanel.hasSecondResource()) {
					resourcePanel.setSecondResource(cmd);
				} else {
					appendMessage(Messages.getString("Controller.Bereits2Rohstoffe")); //$NON-NLS-1$
				}
			} else if (isMonopolyCard) {
				if (cmd.equals("confirm")) { //$NON-NLS-1$
					if (resourcePanel.hasFirstResource()) {
						sendMessage(new Message(Constants.MONOPOLY,
								resourcePanel.getResourceByIndex(1), -1, -1));
						resourcePanel.resetResources();
						isMonopolyCard = false;
						resourcePanel.setVisible(false);
					} else {
						appendMessage(Messages.getString("Controller.WaehleRohstoffe")); //$NON-NLS-1$
					}
				} else if (cmd.equals("cancel")) { //$NON-NLS-1$
					resourcePanel.resetResources();
				} else if (!resourcePanel.hasFirstResource()) {
					resourcePanel.setFirstResource(cmd, true);
				} else {
					appendMessage(Messages.getString("Controller.BereitsGewaehlt")); //$NON-NLS-1$
				}
			} else if (discarding) {
				if (cmd.equals("confirm")) { //$NON-NLS-1$
					if (resourcePanel.getResAmount() < discardAmount) {
						appendMessage(Messages.getString("Controller.RohstoffeWaehlen")); //$NON-NLS-1$
					} else {
						sendMessage(new Message(
								resourcePanel.getDiscardedResources(),
								client.getID()));
						discardAmount = 0;
						resourcePanel.resetAll();
						resourcePanel.setVisible(false);
					}
				} else if (cmd.equals("cancel")) { //$NON-NLS-1$
					resourcePanel.resetDiscardingResources();
				} else {
					if (resourcePanel.getResAmount() < discardAmount) {
						if (resourcePanel.getResourceAmount(cmd) < client
								.getSettler().getResourceAmount(cmd)) {
							resourcePanel.addResource(cmd);
						} else {
							appendMessage(Messages.getString("Controller.WaehleAnderenRohstoff")); //$NON-NLS-1$
						}
					} else {
						appendMessage(Messages.getString("Controller.GenugRohstoffeGewaehlt")); //$NON-NLS-1$
					}
				}
			}
		}
	}

	@Override
	/**
	 * Verwertet die Eingaben �ber das Keyboard
	 */
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			String c = chatGUI.getInput();
			if (c.startsWith("/cheat")) { //$NON-NLS-1$
				if (client.isBeginning()) {
					appendMessage(Messages.getString("Controller.NochNichtCheaten")); //$NON-NLS-1$
				} else {
					Matcher match = Pattern.compile(
							"(/cheat) (\\w*)(?: (\\d*))?").matcher(c); //$NON-NLS-1$
					if (match.find()) {
						String cmd = match.group(2);
						if (cmd.equals("on") && match.matches()) { //$NON-NLS-1$
							try {
								sendMessage(new Message(true));
								sendChatMessage(Messages.getString("Controller.CheatsAktiviert")); //$NON-NLS-1$

							} catch (IOException e) {
								exceptionLogger.writeException(Level.SEVERE,
										Messages.getString("Controller.IOExceptionCheatsAktivieren")); //$NON-NLS-1$
							}
						} else if (cmd.equals("off") && match.matches()) { //$NON-NLS-1$
							try {
								sendMessage(new Message(false));
								sendChatMessage(Messages.getString("Controller.CheatsDeaktiviert")); //$NON-NLS-1$

							} catch (IOException e) {
								exceptionLogger.writeException(Level.SEVERE,
										Messages.getString("Controller.IOExceptionCheatsDeaktivieren")); //$NON-NLS-1$
							}
						} else if (match.matches()
								&& resourcesForCheat.contains(cmd)
								&& match.group(3) != null) {

							int amount = Integer.parseInt(match.group(3));
							if (amount > 99) {
								amount = 99;
							} else if (amount < 0) {
								amount = 0;
							}
							boolean allowedToCheat = island.isAllowedToCheat();
							if (cmd.equals("wool") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.WOOL, amount,
										client.getID()));
							} else if (cmd.equals("brick") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.BRICK,
										amount, client.getID()));
							} else if (cmd.equals("ore") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.ORE, amount,
										client.getID()));
							} else if (cmd.equals("grain") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.GRAIN,
										amount, client.getID()));
							} else if (cmd.equals("lumber") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.LUMBER,
										amount, client.getID()));
							} else if (cmd.equals("all") && allowedToCheat) { //$NON-NLS-1$
								sendMessage(new Message(Constants.LUMBER,
										amount, client.getID()));
								sendMessage(new Message(Constants.GRAIN,
										amount, client.getID()));
								sendMessage(new Message(Constants.ORE, amount,
										client.getID()));
								sendMessage(new Message(Constants.BRICK,
										amount, client.getID()));
								sendMessage(new Message(Constants.WOOL, amount,
										client.getID()));
							} else {
								appendMessage(Messages.getString("Controller.CheatAus")); //$NON-NLS-1$
							}
						} else {
							appendMessage(Messages.getString("Controller.CheatFalsch")); //$NON-NLS-1$
						}
					}
				}
			}
			if (!c.equals("") && !c.startsWith("/")) { //$NON-NLS-1$ //$NON-NLS-2$
				try {
					sendChatMessage(c);
				} catch (IOException e) {
					exceptionLogger.writeException(Level.SEVERE,
							Messages.getString("Controller.IOExceptionMessageSenden")); //$NON-NLS-1$
				}
			}
		}
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Hier werden Mouse-Events verwertet.
	 */
	public void mouseClicked(MouseEvent event) {
		buildButton.setActionCommand("node.settlement");
		buildButton.setVisible(false);
		buildButton.changeIconActivity(ImportImages.settlementBtn);
		buildPanelRoad.setVisible(false);
//		buildPanelNode.setVisible(false);
		polygonMap.setShowSettlementNodes(false);
		polygonMap.setShowStreets(false);
		polygonMap.setShowCityNodes(false);
		polygonMap.repaint();
		if (!blocked || !choosing || !discarding) {
			if (event.getSource() instanceof PolygonMap
					&& client.getID() == island.getCurrentPlayer().getID()) {
				xCoord = event.getX();
				yCoord = event.getY();
				if (isRobberActivated) {
					Tile newRobberTile = polygonMap.getTile(xCoord, yCoord);
					if(newRobberTile != null) {
						int resource = newRobberTile.getResource();
						if (!(resource == Constants.WATER
								|| resource == Constants.HARBOR
								|| resource == Constants.WOOLHARBOR
								|| resource == Constants.GRAINHARBOR
								|| resource == Constants.LUMBERHARBOR
								|| resource == Constants.BRICKHARBOR || resource == Constants.OREHARBOR)
								&& newRobberTile != null
								&& newRobberTile != island.getRobberTile()) {
							isRobberActivated = false;
							sendMessage(new Message(newRobberTile, null));
						} else {
							appendMessage(Messages.getString("Controller.UngueltigesFeld")); //$NON-NLS-1$
						}
					}
				} else if (!isRobbing) {
					clickedRoad = polygonMap.getClickedRoad(xCoord, yCoord);
					if (clickedRoad != null) {
						if ((client.getCurrentPhase() == Constants.PHASE_3 || client
								.getCurrentPhase() == Constants.PHASE_2)
								&& !client.isBeginning()) {
							if (!isWaterStreet(clickedRoad)) {
								buildPanelRoad.setVisible(true);
								findStreets();
								polygonMap.setShowStreets(true);
								polygonMap.repaint();
								buildPanelRoad.setBounds(event.getX()-buildPanelRoad.getWidth()/3,
										event.getY()-buildPanelRoad.getHeight()/3,
										buildPanelRoad.getWidth(),
										buildPanelRoad.getHeight());
							}
						} else if (client.isBeginning()) {
							if (!isWaterStreet(clickedRoad)) {
								buildPanelRoad.setVisible(true);
								findStreets();
								polygonMap.setShowStreets(true);
								polygonMap.repaint();
								buildPanelRoad.setBounds(event.getX()-buildPanelRoad.getWidth()/3,
										event.getY()-buildPanelRoad.getHeight()/3,
										buildPanelRoad.getWidth(),
										buildPanelRoad.getHeight());
							}
						} else if(roadCardCount > 0) {
							if (!isWaterStreet(clickedRoad)) {
								buildPanelRoad.setVisible(true);
								findStreets();
								polygonMap.setShowStreets(true);
								polygonMap.repaint();
								buildPanelRoad.setBounds(event.getX()-buildPanelRoad.getWidth()/3,
										event.getY()-buildPanelRoad.getHeight()/3,
										buildPanelRoad.getWidth(),
										buildPanelRoad.getHeight());
							}
						} else {
							appendMessage(Messages.getString("Controller.ErstWuerfeln")); //$NON-NLS-1$
						}
					}
					clickedNode = polygonMap.getClickedNode(xCoord, yCoord);
					if (clickedNode != null) {
						if (getClickedNode().getBuilding() == Constants.SETTLEMENT) {
							buildButton.setActionCommand("node.city");
							buildButton.changeIconActivity(ImportImages.cityBtn);
//							buildPanelNode.getbSettlement().setActionCommand(
//									"node.city"); //$NON-NLS-1$
							findCities();
							polygonMap.setShowCityNodes(true);
							repaintMap();
						}
						if ((client.getCurrentPhase() == Constants.PHASE_3 || client
								.getCurrentPhase() == Constants.PHASE_2)
								&& !client.isBeginning()) {
							if ((polygonMap.getClickedNode(xCoord, yCoord)
									.isWaterNode() & polygonMap.getClickedNode(
									xCoord, yCoord).isCoastalNode())
									|| (!polygonMap.getClickedNode(xCoord,
											yCoord).isWaterNode())) {
								setClickedNodeArea(polygonMap.getActionArea(
										xCoord, yCoord));
//								buildPanelNode.setVisible(true);
								buildButton.setVisible(true);
								findSettlementNodes();
								polygonMap.setShowSettlementNodes(true);
								polygonMap.repaint();
								buildButton.setBounds(event.getX()-buildButton.getWidth()/3,
										event.getY()-buildButton.getHeight()/3,
										buildButton.getWidth(),
										buildButton.getHeight());
//								buildPanelNode.setBounds(event.getX(),
//										event.getY(),
//										buildPanelNode.getWidth(),
//										buildPanelNode.getHeight());
								
							
							}
						} else if (client.isBeginning()) {
							if ((polygonMap.getClickedNode(xCoord, yCoord)
									.isWaterNode() & polygonMap.getClickedNode(
									xCoord, yCoord).isCoastalNode())
									|| (!polygonMap.getClickedNode(xCoord,
											yCoord).isWaterNode())) {
								setClickedNodeArea(polygonMap.getActionArea(
										xCoord, yCoord));
								buildButton.setVisible(true);
								findSettlementNodes();
								polygonMap.setShowSettlementNodes(true);
								polygonMap.repaint();
								buildButton.setBounds(event.getX()-buildButton.getWidth()/3,
										event.getY()-buildButton.getHeight()/3,
										buildButton.getWidth(),
										buildButton.getHeight());
							}
						} else {
							appendMessage(Messages.getString("Controller.ErstWuerfeln")); //$NON-NLS-1$
						}
					}
				}
			} else if (event.getSource() instanceof DicePanel) {
				mainGUI.getDicePanel().setVisible(true);
			} else {
				
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {	}

	@Override
	public void mousePressed(MouseEvent arg0) {	
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	/**
	 * Refresht die angezeigte GUI unter Verwendung des zu Grunde liegenden Models
	 */
	public void refresh() {
		mainGUI.getPlayerFrame().setMyTurn(
				client.getID() == island.getCurrentPlayer().getID());
		tradingMenu.update();
		supplyPanel.update();
		cardMenu.update();
		refreshOpponents();
	}

	/**
	 * Baut eine Siedlung auf der Insel (GUI).
	 */
	private void buildSettlement() {
		// client.setCurrentPhase(Constants.PHASE_3);
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
	
	public void chooseAction() {
		
	}

	/**
	 * Eine Stra&szlig;e wird gebaut
	 */
	private void buildCity() {
		Message message = new Message(clickedNode, true, null);
		sendMessage(message);
	}

	/**
	 * Baut eine Stra\u00DFe auf der Insel (GUI).
	 */
	private void buildStreet() {
		built = false;
		buildPanelRoad.setVisible(false);
		Message message = new Message(clickedRoad, true, null);
		sendMessage(message);
	}

	/**
	 * Pr&uuml;ft, ob der Stra&szlig;enbau erlaubt ist
	 * 
	 * @return wahr, wenn man Roads bauen kann
	 */
	private boolean buildStreetIsAllowed() {
		if (clickedRoad.getEnd() == lastSettlementNode
				|| clickedRoad.getStart() == lastSettlementNode) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Pr&uuml;ft, ob der Stra&szlig;enbau erlaubt ist
	 * 
	 * @return wahr, wenn man Roads bauen kann
	 */
	private boolean buildStreetIsAllowed(Road road) {
		if (!client.isBeginning()
				&& road.isBuildable(island, client.getSettler().getID())
				&& road.getOwner() == Constants.NOBODY) {
			return true;
		} else if (((client.isBeginning() && road.getEnd() == getLastSettlementNode()) || (client
				.isBeginning() && road.getStart() == getLastSettlementNode()))
				&& road.getOwner() == Constants.NOBODY) {
			return true;
		} else
			return false;
	}

	/**
	 * Pr&uuml;ft, ob die Stra&szlig;e im Wasser liegt
	 * 
	 * @return wahr, wenn man Roads bauen kann
	 */
	public boolean isWaterStreet(Road road) {
		if ((road.getStart().isWaterNode() && road.getStart().isCoastalNode()
				&& !road.getEnd().isCoastalNode() && road.getEnd()
				.isWaterNode())
				|| (road.getEnd().isWaterNode()
						&& road.getEnd().isCoastalNode()
						&& road.getStart().isWaterNode() && !road.getStart()
						.isCoastalNode())
				|| ((road.getStart().isWaterNode() && !road.getStart()
						.isCoastalNode()) && (road.getEnd().isWaterNode() && !road
						.getEnd().isCoastalNode()))) {
			return true;
		}

		return false;
	}

	/**
	 * Sucht sich alle Orte, an denen eine Stra&szlig;e gebaut werden kann
	 */
	public void findStreets() {
		polygonMap.resetStreets();
		polygonMap.setCurrentPlayerID(client.getSettler().getID());
		for (int i = 0; i < island.getRoads().length; i++) {
			if (buildStreetIsAllowed(island.getRoads()[i])
					&& !isWaterStreet(island.getRoads()[i])) {
				polygonMap.addStreet(i);
			}
		}
	}
	
	/**
	 * Sucht sich alle Orte, an denen Stadt gebaut werden kann
	 */
	public void findCities() {
		polygonMap.setCurrentPlayerID(client.getSettler().getID());
		int counter = 0;
		for (int i = 0; i < island.getNodes().length; i++) {
			if (island.getNodes()[i].getBuilding() == Constants.SETTLEMENT
					&& island.getNodes()[i].getOwnerID() == client.getSettler()
							.getID()) {
				polygonMap.setCityNodes(counter, i);
				counter++;
			} else {
				polygonMap.setCityNodes(counter, -1);
				counter++;
			}
		}
	}
	
	public void gameEnd() {
		gameEnd = true;
	}

	/**
	 * Sendet eine Nachricht an den Server
	 */
	public void sendMessage(Message message) {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(client.getSocket().getOutputStream());
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			exceptionLogger.writeException(Level.SEVERE,
					Messages.getString("Controller.IOExceptionNachricht")); //$NON-NLS-1$
		}
	}

	/**
	 * Verschickt eine Chatnachicht (\u00FCber das Netzwerk).
	 * 
	 * @param chatMessage
	 *            ist der Nachricht-String
	 * @throws IOException
	 */
	public void sendChatMessage(String chatMessage) throws IOException {
		if (chatMessage.length() > 2
				&& chatMessage.substring(0, 2).equals("/w ")) { //$NON-NLS-1$
			chatMessage = "/w [" + client.getUsername() + "]: " //$NON-NLS-1$ //$NON-NLS-2$
					+ chatMessage.substring(3);
		} else {
			chatMessage = "[" + client.getUsername() + "]: " + chatMessage; //$NON-NLS-1$ //$NON-NLS-2$
		}
		sendMessage(new Message(chatMessage, client.getID()));
	}

	/**
	 * Die Methode gibt die angeklickte NodeArea zur\u00FCck .
	 * 
	 * @return die angeklickte NodeArea wird zur\u00FCck gegeben
	 */
	public Ellipse2D getClickedNodeArea() {
		return clickedNodeArea;
	}

	/**
	 * Die Mehtode setzt die angeklickte NodeArea.
	 * 
	 * @param clickedNodeArea
	 *            ist die angeklickte NodeArea
	 */
	public void setClickedNodeArea(Ellipse2D clickedNodeArea) {
		this.clickedNodeArea = clickedNodeArea;
	}

	/**
	 * Die Methode gibt die y-Koordinate zur\u00FCck.
	 * 
	 * @return die y-Koordinate
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * Die Methode gibt die x-Koordinate zur\u00FCck.
	 * 
	 * @return die x-Koordinate
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * Die Methode gibt die angekickte Node (Model) zur\u00FCck.
	 * 
	 * @return die angeklickte Node
	 */
	public Node getClickedNode() {
		return clickedNode;
	}

	/**
	 * Die Methode setzt die angekickte Node (Model).
	 * 
	 * @param die
	 *            angeklickte Node
	 */
	public void setClickedNode(Node currentNode) {
		this.clickedNode = currentNode;
	}

	public IslandOfCatan getIsland() {
		return island;
	}

	public int getPossibleSettlement(int index) {
		return possibleSettlement[index];
	}

	/**
	 * Zeigt die Augenzahl auf der GUI an
	 */
	public void showDices(int pipOne, int pipTwo) {
		dicePanel.setPips(pipOne, pipTwo);
		dicePanel.setVisible(true);
	}

	/**
	 * Versteckt die Augenzahl
	 */
	public void hideDices() {
		dicePanel.setVisible(false);
	}

	/**
	 * Zeigt das Resourcenpanel an
	 */
	public void showResources(byte devCard) {
		if (devCard == Constants.GETRESOURCES)
			isResourceCard = true;
		if (devCard == Constants.MONOPOLY)
			isMonopolyCard = true;
		resourcePanel.setVisible(true);
	}

	/**
	 * Aktiviert den R&auml;ber
	 */
	public void activateRobberMoving() {
		isRobberActivated = true;
	}

	/**
	 * Aktiviert das Klauen
	 */
	public void activateRobbing(ArrayList<Integer> playerIds) {
		isRobbing = true;
		mainGUI.showRobberPanel(playerIds);
	}

	/**
	 * zeichnet die PolygonMap neu
	 */
	public void repaintMap() {
		polygonMap.repaint();
	}

	public void activateTradees(int[] offR,
			int[] expR) {
		choosing = true;
		this.offR = offR;
		this.expR = expR;
		mainGUI.showTradeesPanel();
	}
	
	public void switchTradeeStatus(int settlerID, boolean isAccepted) {
		mainGUI.getTradeePanel().switchStatus(settlerID, isAccepted);
	}

	public void activateRoadCard() {
		roadCardCount = (client.getIsland().getCurrentPlayer().getRoadCount() == 14) ? 1
				: 2;
		Vector<Road> buildableRoads = new Vector<Road>();
		for (int i = 0; i < island.getRoads().length; i++) {
			if (island.getRoads()[i].isBuildable(island, client.getID())) {
				buildableRoads.add(island.getRoads()[i]);
			}
		}
		if (buildableRoads.size() < roadCardCount) {
			roadCardCount = buildableRoads.size();
		}
	}

	public void repaintRoads(int roadIndex, int id) {
		polygonMap.addRoad(roadIndex, id);
		repaintMap();
	}

	public void block(boolean blocked) {
		this.blocked = blocked;
	}

	public Client getClient() {
		return client;
	}

	public void setBuilt(boolean built) {
		this.built = built;
	}

	public PolygonMap getPolygonMap() {
		return polygonMap;
	}

	public MainGUI getMainGUI() {
		return mainGUI;
	}

	private void findSettlementNodes() {
		polygonMap.setCurrentPlayerID(client.getSettler().getID());
		int counter = 0;
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
				polygonMap.setSettlementNodes(counter, i);
				// settlementNodes[counter] = i;
				counter++;
			} else {
				polygonMap.setSettlementNodes(counter, -1);
				counter++;
			}
		}
	}

	public void refreshOpponents() {
		OpponentFrame[] oppFrames = mainGUI.getOpponentFrames();
		for (int i = 0; i < oppFrames.length; i++) {
			oppFrames[i].setMyTurn(oppFrames[i].getOpponent().getID() == island
					.getCurrentPlayer().getID());
			oppFrames[i].update();
			oppFrames[i].setMyTurn(oppFrames[i].getOpponent().getID() == island
					.getCurrentPlayer().getID());
		}
	}

	public void activateConfirmTrade(int[] offR, int[] expR) {
		trading = true;
		mainGUI.showConfirmTradePanel(offR, expR);
	}

	public void discard(int amount) {
		resourcePanel.showDiscard();
		discarding = true;
		discardAmount = amount;
	}
	
	public void setAllowedToRoll(boolean allowedToRoll) {
		this.allowedToRoll = allowedToRoll;
	}

	public void refreshLongestRoadView(int settlerID) {
		if(settlerID >= 0) {
			if (settlerID == client.getID()) {
				mainGUI.getPlayerFrame().setLongestRoad(true);
				for (int j = 0; j < mainGUI.getOpponentFrames().length; j++)
					mainGUI.getOpponentFrames()[j].setLongestRoad(false);
			} else {
				mainGUI.getPlayerFrame().setLongestRoad(false);
				for (int i = 0; i < mainGUI.getOpponentFrames().length; i++) {
					if (mainGUI.getOpponentFrames()[i].getOpponent().getID() == settlerID)
						mainGUI.getOpponentFrames()[i].setLongestRoad(true);
					else
						mainGUI.getOpponentFrames()[i].setLongestRoad(false);
				}
			}
		} else {
			mainGUI.getPlayerFrame().setLongestRoad(false);
			for (int i = 0; i < mainGUI.getOpponentFrames().length; i++) {
				mainGUI.getOpponentFrames()[i].setLongestRoad(false);
			}
		}
	}

	public void refreshLargestArmyView(int settlerID) {
		if (settlerID == client.getID()) {
			mainGUI.getPlayerFrame().setLargestArmy(true);
			for (int j = 0; j < mainGUI.getOpponentFrames().length; j++)
				mainGUI.getOpponentFrames()[j].setLargestArmy(false);
		} else {
			mainGUI.getPlayerFrame().setLargestArmy(false);
			for (int i = 0; i < mainGUI.getOpponentFrames().length; i++) {
				if (mainGUI.getOpponentFrames()[i].getOpponent().getID() == settlerID)
					mainGUI.getOpponentFrames()[i].setLargestArmy(true);
				else
					mainGUI.getOpponentFrames()[i].setLargestArmy(false);
			}
		}
	}

	public void addRoad(int index, int owner) {
		polygonMap.addRoad(index, owner);
	}

	public void setCardMenuVisible(boolean visible) {
		cardMenu.setVisible(visible);
	}

	public void setViewsVisible(boolean visible) {
		buildingCostsMenuPanel.setVisible(visible);
		cardMenu.setVisible(visible);
		tradingMenu.setVisible(visible);
	}

	public void setNoInteraction(boolean active) {
		isTradeActivated = active;
	}

	public boolean isTradeActivated() {
		return isTradeActivated;

	}


	public void setDiscarding(boolean discarding) {
		this.discarding = discarding;
	}

	public void appendMessage(String message, Color c) {
		if(!gameEnd) {
			chatGUI.append(message + "\n", c); //$NON-NLS-1$
			chatGUI.getOutputField().setCaretPosition(
					chatGUI.getOutputField().getDocument().getLength());
		}
	}

	public void appendMessage(String message) {
		if(!gameEnd) {
			chatGUI.append(message + "\n"); //$NON-NLS-1$
			chatGUI.getOutputField().setCaretPosition(
					chatGUI.getOutputField().getDocument().getLength());	
		}
	}
	
	/**
	 * Setzt die erlaubten Cheats
	 */
	private void addAllowedCheats() {
		resourcesForCheat = new Vector<String>();
		resourcesForCheat.add("wool"); //$NON-NLS-1$
		resourcesForCheat.add("brick"); //$NON-NLS-1$
		resourcesForCheat.add("grain"); //$NON-NLS-1$
		resourcesForCheat.add("ore"); //$NON-NLS-1$
		resourcesForCheat.add("lumber"); //$NON-NLS-1$
		resourcesForCheat.add("all");	 //$NON-NLS-1$
	}
	
	public void hideConfirmPanel() {
		mainGUI.getConfirmTradePanel().setVisible(false);
	}

	@Override
	public void setAllowedToBuild(boolean allowedToBuild) {
		//for AI only
	}
	
	public void cardPlayed(byte devCard) {
		//for AI only
	}

}
