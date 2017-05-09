package model;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.*;

/**
 * Enth&auml;t den Zustand des Siedlers.
 * 
 * @author Michael Strobl, Fabian Schilling, Thomas Wimmer, Florian Weiss, Christian Hemauer
 * 
 */
public class Settler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Die zugeteile ID (identisch zur Position im <code>settlers</code>)
	 */
	private int id;
	/**
	 * Das verwendete Anzeigebild (Avatar) des Spielers.
	 */
	private Image img;
	/**
	 * Der verwendete Benutzername des Spielers
	 */
	private String username;
	/**
	 * Variable f&uuml;r die Anzahl der Weizen-Rohstoffkarten des Spielers
	 */
	private int grain;
	/**
	 * Variable f&uuml;r die Anzahl der Lehm-Rohstoffkarten des Spielers
	 */
	private int brick;
	/**
	 * Variable f&uuml;r die Anzahl der Holz-Rohstoffkarten des Spielers
	 */
	private int lumber;
	/**
	 * Variable f&uuml;r die Anzahl der Woll-Rohstoffkarten des Spielers
	 */
	private int wool;
	/**
	 * Variable f&uuml;r die Anzahl der Eisen-Rohstoffkarten des Spielers
	 */
	private int ore;
	/**
	 * Variable f&uuml;r die Anzahl &uuml;brigen Stra&szlig;en des Spielers
	 */
	private int roadCount;
	/**
	 * Variable f&uuml;r die Anzahl &uuml;brigen D&ouml;rfer des Spielers
	 */
	private int settlementCount;
	/**
	 * Variable f&uuml;r die Anzahl &uuml;brigen St&auml;dte des Spielers
	 */
	private int cityCount;
	/**
	 * ArrayList f&uuml;r die ausgespielten Ereigniskarten des Spielers
	 */
	@SuppressWarnings("unused")
	private ArrayList<Byte> revealedDevelopmentCards;
	/**
	 * ArrayList f&uuml;r die verdeckten Ereigniskarten des Spielers
	 */
	@SuppressWarnings("unused")
	private ArrayList<Byte> hiddenDevelopmentCards;
	// ersetzt hidden und revealed
	/**
	 * ArrayList f&uuml;r die Ereigniskarten die erst in dieser Runde gekauft
	 * wurden.
	 */
	private ArrayList<Byte> tempDevelopmentCards;

	/**
	 * ArrayList f&uuml;r die Ereigniskarten des Spielers
	 */
	private ArrayList<Byte> developmentCards;
	/**
	 * Gibt an ob schon eine Ereigniskarte gespielt wurde
	 */
	@SuppressWarnings("unused")
	private boolean devCardPlayed;
	/**
	 * ArrayList f&uuml;r die H&auml;fen der Spieler
	 */
	private ArrayList<Byte> harbors;

	/**
	 * Variable f&uuml;r die Gewinnpunkte des Spielers
	 */
	private int score;

	/**
	 * Tempor&auml;rer Score
	 */
	private int tempScore;

	/**
	 * Gibt an ob ein Spieler die l&auml;ngste Handelsstra&szlig;e besitzt
	 */
	private boolean longestRoad;

	/**
	 * Gibt an ob ein Spieler die gr&ouml;&szlig;te Rittermacht besitzt
	 */
	private boolean largestArmy;

	/**
	 * Gibt an, wie viele Ritterkarten der Spieler besitzt
	 */
	private int armyCount;
	/**
	 * L&auml;nge der l&auml;ngsten Handelsstra�e
	 */
	private int longestRoadLength;

	/**
	 * Anzahl der Rohstoffe
	 */
	private int amountOfResources;

	/**
	 * Anzahl der Entwicklungskarten
	 */
	private int amountOfDevCards;

	/**
	 * Knoten mit der zweiten Siedlung
	 */
	private Node secondSettlement;

	/**
	 * Farbe des Settlers
	 */
	private Color color;
	
	/**
	 * Position des Avatars im AvatarArray
	 */
	private int avatarNumber;

	/**
	 * Erstellt ein neues Objekt der Klasse <code>Settler</code>. Der
	 * Konstruktor setzt die Starteinstellungen f&uuml;r den Spieler. Er
	 * erstellt die Arrays und "teilt" die Spielsteine f&uuml;r D&ouml;rfer,
	 * St&auml;dten und Stra&szlig;en aus.
	 * 
	 * 
	 * @param name
	 *            Der Benutzername des Spielers
	 * @param id
	 *            Die ID oder die Position im Spieler-Array
	 * @param age
	 *            Das Alter des Spielers
	 * @param island
	 *            Das Spielfeld
	 */
	public Settler(String name, int id, Color color, int avatarNumber) {
		this.id = id;
		this.username = name;
		this.grain = 0;
		this.brick = 0;
		this.lumber = 0;
		this.wool = 0;
		this.ore = 0;
		this.roadCount = 0;
		this.settlementCount = 0;
		this.cityCount = 0;
		this.score = 0;
		this.setLargestArmy(false);
		this.setLongestRoad(false);
		this.longestRoadLength = 0;
		this.developmentCards = new ArrayList<Byte>();
		this.tempDevelopmentCards = new ArrayList<Byte>();
		this.amountOfResources = 0;
		this.amountOfDevCards = 0;
		this.harbors = new ArrayList<Byte>();
		this.color = color;
		this.avatarNumber = avatarNumber;
	}


	/**
	 * Anzahl an Karten mit dem &uuml;bergebenen Wert.
	 * 
	 * @param devCard
	 *            Karte
	 * @return Anzahl
	 */
	public int getAmountOfDevCard(byte devCard) {
		int amount = 0;
		for (int i = 0; i < developmentCards.size(); i++) {
			if (developmentCards.get(i) == devCard)
				amount++;
		}
		return amount;
	}

	/** 
	 * Gibt die Anzahl der Resourcen des Settlers zur&uuml;ck
	 * @return amountOfResources
	 */
	public int getAmountOfResources() {
		return amountOfResources;
	}

	/**
	 * gibt die Anzahl der entwicklungskarten des settlers zur&uuml;ck
	 * @return amountOfDevCards
	 */
	public int getAmountOfDevCards() {
		return amountOfDevCards;
	}

	/**
	 * Developement-Karte hinzuf&uuml;gen.
	 */
	public void addOneDevelopmentCard() {
		amountOfDevCards++;
	}

	/**
	 * Developement-Karte entfernen.
	 */
	public void removeOneDevelopmentCard() {
		amountOfDevCards--;
	}

	/**
	 * Ritterkarte hinzuf&uuml;gen.
	 */
	public void addKnight() {
		armyCount++;
	}

	/**
	 * Lehm hinzuf&uuml;gen.
	 * 
	 * @param brickAmount
	 *            Anzahl Lehm
	 */
	public void addBrick(int brickAmount) {
		brick += brickAmount;
	}

	/**
	 * Wolle hinzuf&uuml;gen.
	 * 
	 * @param woolAmount
	 *            Anzahl Wolle
	 */
	public void addWool(int woolAmount) {
		wool += woolAmount;
	}

	/**
	 * Holz hinzuf&uuml;gen.
	 * 
	 * @param lumberAmount
	 *            Anzahl Holz
	 */
	public void addLumber(int lumberAmount) {
		lumber += lumberAmount;
	}

	/**
	 * Erz hinzuf&uuml;gen.
	 * 
	 * @param oreAmount
	 *            Anzahl Erz
	 */
	public void addOre(int oreAmount) {
		ore += oreAmount;
	}

	/**
	 * Weizen hinzuf&uuml;gen.
	 * 
	 * @param grainAmount
	 *            Anzahl Weizen
	 */
	public void addGrain(int grainAmount) {
		grain += grainAmount;
	}

	/**
	 * Rohstoff hinzuf&uuml;gen.
	 * 
	 * @param resource
	 *            Rohstoff
	 * @param amount
	 *            Anzahl
	 */
	public void addResource(byte resource, int amount) {
		switch (resource) {
		case Constants.BRICK:
			addBrick(amount);
			break;
		case Constants.WOOL:
			addWool(amount);
			break;
		case Constants.LUMBER:
			addLumber(amount);
			break;
		case Constants.ORE:
			addOre(amount);
			break;
		case Constants.GRAIN:
			addGrain(amount);
			break;
		}
	}

	/**
	 * Stra&szlig;e hinzuf&uuml;gen.
	 */
	public void addRoad() {
		roadCount++;
	}

	/**
	 * Siedlung hinzuf&uuml;gen.
	 */
	public void addSettlement() {
		settlementCount++;
	}

	/**
	 * Siedlung entfernen.
	 */
	public void removeSettlement() {
		settlementCount--;
	}

	/**
	 * Stadt hinzuf&uuml;gen.
	 */
	public void addCity() {
		cityCount++;
	}

	/**
	 * Anzahl am &uuml;bergebenen Rohstoff.
	 * 
	 * @param resource
	 *            Rohstoff
	 * @return Anzahl
	 */
	public int getResourceAmount(String resource) {
		if (resource.equals("Grain"))
			return getGrain();
		else if (resource.equals("Wool"))
			return getWool();
		else if (resource.equals("Lumber"))
			return getLumber();
		else if (resource.equals("Ore"))
			return getOre();
		else if (resource.equals("Brick"))
			return getBrick();
		return -1;
	}

	/**
	 * Anzahl am &uuml;bergebenen Rohstoff.
	 * 
	 * @param resource
	 *            Rohstoff
	 * @return Anzahl
	 */
	public int getAmountOfResource(byte resource) {
		switch (resource) {
		case Constants.BRICK:
			return getBrick();
		case Constants.ORE:
			return getOre();
		case Constants.WOOL:
			return getWool();
		case Constants.LUMBER:
			return getLumber();
		case Constants.GRAIN:
			return getGrain();
		default:
			return 0;
		}
	}

	/**
	 * Gibt zur&uuml;ck, ob &uuml;bergebenes Bauwerk gebaut werden kann.
	 * 
	 * @param asset
	 *            Bauwerk
	 * @return Baum&ouml;glichkeit
	 */
	public boolean canAfford(int asset) {
		if (asset == Constants.ROAD)
			return lumber > 0 && brick > 0;
		else if (asset == Constants.SETTLEMENT)
			return lumber > 0 && brick > 0 && grain > 0 && wool > 0;
		else if (asset == Constants.CITY)
			return ore > 2 && grain > 1;
		else if (asset == Constants.DEVELOPMENTCARD)
			return grain > 0 && wool > 0 && ore > 0;
		else
			return false;
	}
	
	

	/**
	 * Gibt zur&uuml;ck, ob der Spieler die &uuml;bergebene Karte besitzt und
	 * auspielen darf.
	 * 
	 * @param devCard
	 *            Developement-Karte
	 * @return Spieler hat Karte oder nicht
	 */
	public boolean hasCard(byte devCard) {
		for (int i = 0; i < developmentCards.size(); i++) {
			if (developmentCards.get(i) == devCard)
				return true;
		}
		return false;
	}

	/**
	 * Gibt zur&uuml;ck, ob man die &uuml;bergebene Karte im Besitz des Spielers
	 * ist.
	 * 
	 * @param devCard
	 *            Developement-Karte
	 * @return Spieler besitzt die Karte oder nicht
	 */
	public boolean hasTempCard(byte devCard) {
		for (int i = 0; i < tempDevelopmentCards.size(); i++) {
			if (tempDevelopmentCards.get(i) == devCard)
				return true;
		}
		return false;
	}

	/**
	 * Die Methode baut ein Geb&auml;ude (Siedlung oder Stadt) auf einen Knoten.
	 * Besitzt der Knoten einen Hafen, wird dieser dem Spieler hinzugef�gt.
	 * 
	 * @param building
	 *            ist das Geb&auml;ude.
	 * @param buildingLot
	 *            ist der gew&uuml;nschte Bauplatz.
	 */
	public void build(int building, Node buildingLot) {
		buildingLot.setBuilding(building);
		if (building == Constants.SETTLEMENT
				&& buildingLot.getHarbor() != Constants.NOHARBOR) {
			harbors.add(buildingLot.getHarbor());
		}
		buildingLot.setOwnerID(getID());
	}

	/**
	 * W&auml;hlt den Rohstoff aus, der geklaut werden soll.
	 * 
	 * @return Rohstoff
	 */
	public byte getRandomResource() {
		if (getResourcesNumber() > 0) {
			return getResourceVector().elementAt(
					((int) Math.round(Math.random()
							* (getResourcesNumber() - 1))));
		}
		return 0;
	}

	/**
	 * Gibt die ID des Spielers zur&uuml;ck.
	 * 
	 * @return ID des Spielers (identisch zur Position im <code>settlers</code>
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gibt die Anzahl der Rohstoffe des Spieler zur&uuml;ck.
	 * 
	 * @return die Anzahl der Rohstoffe
	 */
	public int getResourcesNumber() {
		return (grain + brick + lumber + wool + ore);
	}

	/**
	 * Gibt den Vector mit allen Rohstoffen des Spieler zur&uuml;ck.
	 * 
	 * @return Den Vector mit allen Rohstoffen des Spieler
	 */
	public Vector<Byte> getResourceVector() {

		Vector<Byte> resourceVector = new Vector<Byte>(getResourcesNumber());
		for (int i = 0; i < grain; i++) {
			resourceVector.add(Constants.GRAIN);
		}
		for (int i = 0; i < brick; i++) {
			resourceVector.add(Constants.BRICK);
		}
		for (int i = 0; i < lumber; i++) {
			resourceVector.add(Constants.LUMBER);
		}
		for (int i = 0; i < wool; i++) {
			resourceVector.add(Constants.WOOL);
		}
		for (int i = 0; i < ore; i++) {
			resourceVector.add(Constants.ORE);
		}

		return resourceVector;
	}

	/**
	 * F&uuml;gt gekauft Karte dem Spieler zum Auspielen hinzu.
	 */
	public void addTempCardsToDevCards() {
		amountOfDevCards += tempDevelopmentCards.size();
		developmentCards.addAll(tempDevelopmentCards);
		tempDevelopmentCards = new ArrayList<Byte>();
	}

	/**
	 * F&uuml;gt gekauft Karte dem Spieler hinzu.
	 * 
	 * @param devCard
	 *            Gekaufte Karte
	 */
	public void addTempDevelopmentCard(byte devCard) {
		tempDevelopmentCards.add(devCard);
	}

	/**
	 * F&uuml;gt Karte dem Spieler hinzu.
	 * 
	 * @param devCard
	 */
	public void addDevelopmentCard(byte devCard) {
		developmentCards.add(devCard);
		amountOfDevCards++;
	}

	/**
	 * Entfernt Developement-Karte
	 * 
	 * @param devCard
	 *            Karte zum Entfernen
	 */
	public void removeDevelopmentCard(byte devCard) {
		developmentCards.remove((Object) devCard);
		amountOfDevCards--;
	}

	/**
	 * F&uuml;gt Rohstoffe dem Spieler hinzu.
	 * 
	 * @param amount
	 *            Anzahl an Rohstoffen
	 */
	public void addResources(int amount) {
		amountOfResources += amount;
	}

	/**
	 * F&uuml;gt dem Spieler einen Hafen hinzu.
	 * 
	 * @param harbor
	 *            Hafen
	 */
	public void addHarbor(byte harbor) {
		harbors.add(harbor);
	}

	/**
	 * Rechnet die momentanen Siegpunkte aus.
	 */
	public void calculateVictoryPoints() {
		tempScore = score;
		if (longestRoad) {
			tempScore += 2;
		}
		if (largestArmy) {
			tempScore += 2;
		}
	}

	/**
	 * F&uuml;gt dem Spieler Siegpunkte hinzu.
	 * 
	 * @param scoreToAdd
	 *            Siegpunkte
	 */
	public void addScore(int scoreToAdd) {
		score += scoreToAdd;
	}
	
	public boolean hasOtherThanVictoryCard() {
		return hasCard(Constants.KNIGHT) || hasCard(Constants.GETRESOURCES) || hasCard(Constants.MONOPOLY) || hasCard(Constants.BUILDSTREETS);
	}

	public int getGrain() {
		return grain;
	}

	public void setGrain(int grain) {
		this.grain = grain;
	}

	public int getBrick() {
		return brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public int getLumber() {
		return lumber;
	}

	public void setLumber(int lumber) {
		this.lumber = lumber;
	}

	public int getWool() {
		return wool;
	}

	public void setWool(int wool) {
		this.wool = wool;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getRoadCount() {
		return roadCount;
	}

	public void setRoadCount(int roadCount) {
		this.roadCount = roadCount;
	}

	public int getScore() {
		return this.score;
	}

	public int getLongestRoadLength() {
		return longestRoadLength;
	}

	public void setLongestRoadLength(int length) {
		this.longestRoadLength = length;
	}

	public void setLongestRoad(boolean longestRoad) {
		this.longestRoad = longestRoad;
	}

	public boolean isLongestRoad() {
		return longestRoad;
	}

	public void setLargestArmy(boolean largestArmy) {
		this.largestArmy = largestArmy;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getArmyCount() {
		return armyCount;
	}

	public ArrayList<Byte> getHarbors() {
		return harbors;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getSettlementCount() {
		return settlementCount;
	}

	public int getCityCount() {
		return cityCount;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public Node getSecondSettlement() {
		return secondSettlement;
	}

	public void setSecondSettlement(Node secondSettlement) {
		this.secondSettlement = secondSettlement;
	}

	public boolean isLargestArmy() {
		return largestArmy;
	}

	public int getTempScore() {
		return tempScore;
	}

	public ArrayList<Byte> getDevelopmentCards() {
		return developmentCards;
	}

	public int getAmountOfTempDevCards() {
		return tempDevelopmentCards.size();
	}
	

	public int getAvatarNumber() {
		return avatarNumber;
	}

	public Color getColor() {
		return color;
	}
}
