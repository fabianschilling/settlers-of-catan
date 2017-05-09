package gui;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import model.*;

/**
 * Die Klasse stellt das gesamte Spielfeld graphisch dar.
 * 
 * @author Florian Weiss, Christian Hemauer, Elisabeth Friedrich
 * 
 */
@SuppressWarnings("serial")
public class PolygonMap extends JLayeredPane {
	/**
	 * Das Model des Spielfelds.
	 */
	private IslandOfCatan island;
	/**
	 * Die X-Koordinate, an die das Starthexagon gelegt wird.
	 */
	private int x;

	/**
	 * Die Y-Koordinate, an die das Starthexagon gelegt wird.
	 */
	private int y;
	/**
	 * Der Radius des Innenkreises eines Hexagons.
	 */
	private int radius;
	/**
	 * Die H&ouml;he eines Hexagons.
	 */
	private double hexagonLength;
	/**
	 * Die Breite eines Hexagons.
	 */
	private double hexagonWidth;
	/**
	 * Array, das die komplette Spielwelt repr&auml;sentiert.
	 */
	private Tile[][] informationMap;
	/**
	 * Das Hexagon-Array, das ein 7x7-Spielfeld bestehend aus Hexagonen
	 * repr&auml;sentiert.
	 */
	private Hexagon[][] hexagonMap = new Hexagon[7][7];
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung S&uuml;dost.
	 */
	private Image harborTypeSO;
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung S&uuml;dwest.
	 */
	private Image harborTypeSW;
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung Westen.
	 */
	private Image harborTypeW;
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung Nordwesten.
	 */
	private Image harborTypeNW;
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung Nordosten.
	 */
	private Image harborTypeNO;
	/**
	 * Grafik f&uuml;r den Hafentyp Ausrichtung Osten.
	 */
	private Image harborTypeO;
	/**
	 * Array, das die Schaltfl&auml;chen f&uuml;r die Knotenanwahl beinhaltet.
	 */
	private Ellipse2D[] nodesActionAreas;
	/**
	 * H&ouml;he eines Tiles.
	 */
	private int heightTile;
	/**
	 * Breite eines Tiles.
	 */
	private int widthTile;
	/**
	 * Breite eines Zahlenchits.
	 */
	private int widthChit;
	/**
	 * H&ouml;he eines Zahlenchits.
	 */
	private int heightChit;
	/**
	 * Breite eines Hafenicons.
	 */
	private int widthIcon;
	/**
	 * H&ouml;he eines Hafenicons.
	 */
	private int heightIcon;
	/**
	 * Array, das die Schaltfl&auml;chen der Stra&szlig;en enth&auml;lt.
	 */
	private Polygon[] roadActionAreas;
	/**
	 * Array, das die einzelnen Hexagone in einem 1-D-Array enth&auml;lt.
	 */
	private Hexagon[] hexagonMap1D;
	/**
	 * Array, das die komplette Spielwelt in einem 1-D-Array repr&auml;sentiert.
	 */
	private Tile[] informationMap1D;
	/**
	 * Vector, der die Indizes der bereits gebauten Stra&szlig;en enth&auml;lt.
	 */
	private Vector<Integer> filledRects;
	/**
	 * Vector, der die Besitzer-IDs der bereits gebauten Stra&szlig;en
	 * enth&auml;lt.
	 */
	private Vector<Integer> filledRectsOwner;
	/**
	 * Array, das die Seidlungen h&auml;lt
	 */
	private int[] settlementNodes;
	/**
	 * Wenn dieser Wert auf true, kann man die Sieldungen angezeigt bekommen
	 */
	private boolean showSettlementNodes;
	/**
	 * Wenn dieser Wert auf true ist, kann man die St&auml;dte angezeigt
	 * bekommen
	 */
	private boolean showCityNodes;
	/**
	 * Die ID des Spielers, der an der Reihe ist
	 */
	private int currentPlayerID;
	/**
	 * In diesem Vektor werden die Stra&uml;&szlig;en gehalten
	 */
	private Vector<Integer> streets;
	/**
	 * Ist dieser Wert auf true, so bekommt man die Stra&szlig;en angezeigt
	 */
	private boolean showStreets;
	/**
	 * Array, das die St&auml;dte h&auml:lt
	 */
	private int[] cityNodes;
	/**
	 * Breite einer nicht vertikal verlaufenden Stra&szlig;e
	 */
	private double imageRoadWidth_NotVert;
	/**
	 * H&ouml;he einer nicht vertikal verlaufenden Stra&szlig;e
	 */
	private double imageRoadHeight_NotVert;

	/**
	 * Breite einer vertikal verlaufenden Stra&szlig;e
	 */
	private double imageRoadWidth_Vert;
	/**
	 * H&ouml;he einer vertikal verlaufenden Stra&szlig;e
	 */
	private double imageRoadHeight_Vert;
	/**
	 * H&ouml;he eines Knotens
	 */
	private double nodeHeight;

	/**
	 * Konstruktor des Hexagonspielfeldes.
	 * 
	 * @param island
	 *            Die Modeldaten der Insel.
	 * @param x
	 *            X-Koordinate des Punktes, an dem das erste Hexagon gezeichnet
	 *            wird.
	 * @param y
	 *            Y-Koordinate des Punktes, an dem das erste Hexagon gezeichnet
	 *            wird.
	 * @param radius
	 *            Halbe Breite eines Hexagons.
	 */
	public PolygonMap(IslandOfCatan island, final int x, final int y, int radius) {
		this.island = island;
		this.x = x;
		this.y = y;
		this.radius = radius;
		showStreets = false;
		streets = new Vector<Integer>();
		filledRects = new Vector<Integer>();
		filledRectsOwner = new Vector<Integer>();
		settlementNodes = new int[island.getNodes().length];
		cityNodes = new int[island.getNodes().length];
		hexagonLength = 2 * radius;
		hexagonWidth = 2 * radius * Math.sin(Math.PI * (1.0 / 3.0));
		roadActionAreas = new Polygon[island.getRoads().length];
		nodesActionAreas = new Ellipse2D[island.getNodes().length];
		hexagonMap1D = new Hexagon[hexagonMap[0].length * hexagonMap.length];
		informationMap1D = new Tile[hexagonMap[0].length * hexagonMap.length];
		init();
	}

	/**
	 * Gibt ein Stra&szlig;enpolygon zur&uuml;ck
	 * 
	 * @param index
	 *            der Index
	 * @return Stra&szlig;enpolygon
	 */
	public Polygon getRoadActionAreas(int index) {
		return roadActionAreas[index];
	}

	/**
	 * Initialisiert das 1-D-Array, das die Hexagon h&auml;lt.
	 */
	public void initHexagonMap1D() {
		int k = 0;
		for (int i = 0; i < hexagonMap[0].length; i++) {
			for (int j = 0; j < hexagonMap[0].length; j++) {
				hexagonMap1D[k] = hexagonMap[j][i];
				k++;
			}
		}
	}

	/**
	 * Initialisiert das 1-D-Array, das die Tiles aus der Insel h&auml;lt.
	 */
	public void initInformationMap1D() {
		int k = 0;
		for (int j = 0; j < informationMap[0].length; j++) {
			for (int i = 0; i < informationMap[0].length; i++) {
				informationMap1D[k] = informationMap[i][j];
				k++;
			}
		}
	}

	/**
	 * Gibt den angeklickten Knoten zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            die X-Koordinate des Mausklicks
	 * @param yCoord
	 *            die Y-Koordinate des Mausklick
	 * @return der angeklickte Knoten
	 */
	public Node getClickedNode(int xCoord, int yCoord) {
		for (int i = 0; i < nodesActionAreas.length; i++) {
			if (nodesActionAreas[i].contains(xCoord - x, yCoord - y))
				return island.getNodes()[i];
		}
		return null;
	}

	/**
	 * Gibt die angeklickte Stra&szlig;e zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            die X-Koordinate des Mausklicks
	 * @param yCoord
	 *            die Y-Koordinate des Mausklicks
	 * @return
	 */
	public Road getClickedRoad(int xCoord, int yCoord) {
		for (int i = 0; i < roadActionAreas.length; i++) {
			if (roadActionAreas[i].contains(xCoord - x, yCoord - y))
				return island.getRoads()[i];
		}
		return null;
	}

	/**
	 * Gibt den Index im Array, das die Stra&szlig;enpolygone h&auml;lt, an der
	 * geklickten Stelle zur&uuml;ck
	 * 
	 * @param xCoord
	 *            die X-Koordinate des Mausklicks
	 * @param yCoord
	 *            die Y-Koordinate des Mausklicks
	 * @return der Index im Array
	 */
	public int getRoadActionAreasOfIndex(int xCoord, int yCoord) {
		for (int i = 0; i < roadActionAreas.length; i++) {
			if (roadActionAreas[i].contains(xCoord, yCoord)) {
				return i;
			}
		}
		return (Integer) null;
	}

	/**
	 * Gibt die angeklickte Knotenfl&auml;che zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            die X-Koordinate des Mausklicks
	 * @param yCoord
	 *            die Y-Koordinate des Mausklicks
	 * @return die angeklickte Knotenfl&auml;che
	 */
	public Ellipse2D getActionArea(int xCoord, int yCoord) {
		for (int i = 0; i < nodesActionAreas.length; i++) {
			if (nodesActionAreas[i].contains(xCoord - x, yCoord - y)) {
				return nodesActionAreas[i];
			}
		}
		return null;
	}

	/**
	 * Gibt das angeklickte Hexagon zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            X-Koordinate des Mausklicks
	 * @param yCoord
	 *            Y-Koordinate des Mausklicks
	 * @return das angeklickte Hexagon.
	 */
	public Hexagon getTileActionAreas(int xCoord, int yCoord) {
		for (int i = 0; i < hexagonMap1D.length; i++) {
			if (hexagonMap1D[i].contains(xCoord - x, yCoord - y)) {
				return hexagonMap1D[i];
			}
		}
		return null;
	}

	/**
	 * Gibt den Index im Array, das die Hexagone h&auml;lt, an der geklickten
	 * Stelle zur&uuml;ck
	 * 
	 * @param xCoord
	 *            X-Koordinate des Mausklicks
	 * @param yCoord
	 *            Y-Koordinate des Mausklicks
	 * @return der Index im Array
	 */
	public int getTileActionAreasOfIndex(int xCoord, int yCoord) {
		for (int i = 0; i < hexagonMap1D.length; i++) {
			if (hexagonMap1D[i].contains(xCoord - x, yCoord - y)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gibt das R&auml;uberfeld eines Tiles, das angeklickt wurde zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            X-Koordinate des Mausklicks
	 * @param yCoord
	 *            X-Koordinate des Mausklicks
	 * @return das angeklickte R&auml;uberfeld
	 */
	public Rectangle getRobberActionAreas(int xCoord, int yCoord) {
		for (int i = 0; i < hexagonMap1D.length; i++) {
			if (hexagonMap1D[i].getTileActionArea().contains(xCoord - x,
					yCoord - y)) {
				return hexagonMap1D[i].getTileActionArea();
			}
		}
		return null;
	}

	/**
	 * Gibt das angeklickte Stra&szlig;enpolygon zur&uuml;ck.
	 * 
	 * @param xCoord
	 *            X-Koordinate des Mausklicks
	 * @param yCoord
	 *            Y-Koordinate des Mausklicks
	 * @return das Stra&szlig;enpolygon
	 */
	public Polygon getRoadActionAreas(int xCoord, int yCoord) {
		for (int j = 0; j < hexagonMap[0].length; j++) {
			for (int i = 0; i < hexagonMap[0].length; i++) {
				for (int k = 0; k < hexagonMap[i][j].getRoadRects().length; k++) {
					if (hexagonMap[i][j].getRoadRectOfIndex(k).contains(
							xCoord - x, yCoord - y)) {
						return hexagonMap[i][j].getRoadRectOfIndex(k);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Initialisiert die PolygonMap und damit die sichtbare Insel.
	 */
	public void init() {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getHeight();
		/* Bef�llen des Hexagonarrays mit den zugeh�rigen Hexagons */
		int xx =  (width / 10) * 3 + this.x;
		int xxx = (int)(hexagonWidth / 2.0);
		for (int i = 0; i < hexagonMap.length; i++) {
			for (int j = 0; j < hexagonMap[0].length; j++) {
				if (i % 2 == 0) {					
					int x = (j * (int)(hexagonWidth)) + xx;
					int y = (int) (this.y + height / 15 + i * 3.0 / 4.0
							* hexagonLength);
					hexagonMap[i][j] = new Hexagon(x, y, radius);
				} else {					
					int x = ((j * (int)(hexagonWidth) + xx) - xxx);
					int y = (int) (this.y + height / 15 + i * 3.0 / 4.0
							* hexagonLength);
					hexagonMap[i][j] = new Hexagon(x, y, radius);
				}
			}
		}
		imageRoadWidth_NotVert = hexagonMap[0][0].getImageRoadWidth_NotVert();
		imageRoadHeight_NotVert = hexagonMap[0][0].getImageRoadHeight_NotVert();

		imageRoadWidth_Vert = hexagonMap[0][0].getImageRoadWidth_Vert();
		imageRoadHeight_Vert = hexagonMap[0][0].getImageRoadHeight_Vert();

		/* Dynamische Skalierung der Grafikkomponenten */
		widthTile = (int) hexagonMap[0][0].getBounds().getWidth();
		heightTile = (int) hexagonMap[0][0].getBounds().getHeight();
		widthChit = (int) (hexagonMap[0][0].getBounds().getWidth() * 1.0 / 3.0);
		heightChit = widthChit;
		widthIcon = (int) (hexagonMap[0][0].getBounds().getWidth() * 1.0 / 2.0);
		heightIcon = (int) (hexagonMap[0][0].getBounds().getHeight() * 1.0 / 2.0);
		informationMap = island.getTiles();
		
		/*
		 * Zuweisung der Schaltfl�chen der Knoten, zu den zugeh�rigen Knoten in
		 * der Hexagonmap
		 */
		for (int i = 0; i < informationMap.length; i++) {
			for (int j = 0; j < informationMap[0].length; j++) {
				for (int k = 0; k < informationMap[i][j].getNodes().length; k++) {
					if (nodesActionAreas[informationMap[i][j].getNodeOfIndex(k)
							.getIndex()] == null) {
						nodesActionAreas[informationMap[i][j].getNodeOfIndex(k)
								.getIndex()] = hexagonMap[i][j]
								.getNodeCircleOfIndex(k);
					}
				}
			}
		}

		nodeHeight = nodesActionAreas[0].getHeight();
		/*
		 * Initialisierung der RoadActionAreas
		 */
		Road[] islandRoads = island.getRoads();
		for (int i = 0; i < islandRoads.length; i++) {
			int startNodeIndex = islandRoads[i].getStart().getIndex();
			int endNodeIndex = islandRoads[i].getEnd().getIndex();
			double startX = nodesActionAreas[startNodeIndex].getCenterX();
			double startY = nodesActionAreas[startNodeIndex].getCenterY();
			double endX = nodesActionAreas[endNodeIndex].getCenterX();
			double endY = nodesActionAreas[endNodeIndex].getCenterY();
			double newX = 0;
			double newY = 0;

			if (startY > endY) {
				newY = startY - (startY - endY) / 2.0;
				island.getRoads()[i].setDirection(Constants.ROAD_UP);
			}
			if (startY < endY) {
				newY = endY - (endY - startY) / 2.0;
				island.getRoads()[i].setDirection(Constants.ROAD_DOWN);
			}
			if (endX - startX > 3.0) {
				newX = endX - (endX - startX) / 2.0;
			}
			if (endX - startX < 3.0) {
				newX = startX - (startX - endX) / 2.0;
				island.getRoads()[i].setDirection(Constants.ROAD_VERT);
			}

			for (int j = 0; j < informationMap.length; j++) {
				for (int k = 0; k < informationMap[0].length; k++) {
					Polygon[] roadRects = hexagonMap[j][k].getRoadRects();
					for (int k2 = 0; k2 < roadRects.length; k2++) {
						if (roadRects[k2].contains(newX, newY)) {
							if (roadActionAreas[i] == null) {
								roadActionAreas[i] = roadRects[k2];
							}
						}
					}
				}
			}
		}
		initInformationMap1D();
		initHexagonMap1D();
	}
	/**
	 * Zeichnet alle relevanten Elemente der PolygonMap.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphic = (Graphics2D) g;
		graphic.setStroke(new BasicStroke(2f));
		graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		/* Zeichnet die Tiles mit H�fen und Rohstoffen */
		for (int i = 0; i < informationMap.length; i++) {
			for (int j = 0; j < informationMap[0].length; j++) {
				Image img = null;
				Image img2 = null;
				Image img3 = null;
				switch (informationMap[i][j].getResource()) {
				case Constants.BRICK:
					img = ImportImages.brick;
					break;
				case Constants.LUMBER:
					img = ImportImages.lumber;
					break;
				case Constants.ORE:
					img = ImportImages.ore;
					break;
				case Constants.GRAIN:
					img = ImportImages.grain;
					break;
				case Constants.WOOL:
					img = ImportImages.wool;
					break;
				case Constants.DESERT:
					img = ImportImages.desert;
					break;
				case Constants.HARBOR:
					img2 = ImportImages.questionBtn;
					break;
				case Constants.LUMBERHARBOR:
					img2 = ImportImages.lumberBtn;
					break;
				case Constants.GRAINHARBOR:
					img2 = ImportImages.grainBtn;
					break;
				case Constants.OREHARBOR:
					img2 = ImportImages.oreBtn;
					break;
				case Constants.BRICKHARBOR:
					img2 = ImportImages.brickBtn;
					break;
				case Constants.WOOLHARBOR:
					img2 = ImportImages.woolBtn;
					break;

				default:
					break;
				}

				if (img != null) {
					g.drawImage(img, (int) hexagonMap[i][j].getBounds().getX(),
							(int) hexagonMap[i][j].getBounds().getY(),
							widthTile, heightTile, this);
				}
				if (img2 != null) {
					g.drawImage(
							img2,
							(int) (hexagonMap[i][j].getBounds().getCenterX() - heightIcon / 3.2),
							(int) (hexagonMap[i][j].getBounds().getCenterY() - widthIcon / 3.2),
							(int) (widthIcon * 0.7), (int) (widthIcon * 0.7),
							this);
				}
				/* Zeichnet die einzelnen Zahlenchips auf die Tiles */
				switch (informationMap[i][j].getChitNumber()) {
				case 2:
					img3 = ImportImages.chit02;
					break;
				case 3:
					img3 = ImportImages.chit03;
					break;
				case 4:
					img3 = ImportImages.chit04;
					break;
				case 5:
					img3 = ImportImages.chit05;
					break;
				case 6:
					img3 = ImportImages.chit06;
					break;
				case 8:
					img3 = ImportImages.chit08;
					break;
				case 9:
					img3 = ImportImages.chit09;
					break;
				case 10:
					img3 = ImportImages.chit10;
					break;
				case 11:
					img3 = ImportImages.chit11;
					break;
				case 12:
					img3 = ImportImages.chit12;
					break;

				default:
					break;
				}
				if (img3 != null) {
					g.drawImage(img3, (int) hexagonMap[i][j].getBounds()
							.getCenterX() - widthChit / 2,
							(int) hexagonMap[i][j].getBounds().getCenterY()
									- heightChit / 2, widthChit, heightChit,
							this);
				}
				if (island.getRobberTile() == informationMap[i][j]
						&& informationMap[i][j].getResource() != Constants.WATER
						&& informationMap[i][j].getResource() != Constants.WOOLHARBOR
						&& informationMap[i][j].getResource() != Constants.BRICKHARBOR
						&& informationMap[i][j].getResource() != Constants.OREHARBOR
						&& informationMap[i][j].getResource() != Constants.LUMBERHARBOR
						&& informationMap[i][j].getResource() != Constants.GRAINHARBOR
						&& informationMap[i][j].getResource() != Constants.HARBOR) {
					Image img1 = ImportImages.robber;
					g.drawImage(img1, (int) hexagonMap[i][j]
							.getTileActionArea().getBounds().getCenterX()
							- (widthTile / 3 + widthTile / 10),
							(int) hexagonMap[i][j].getTileActionArea()
									.getBounds().getCenterY()
									- heightTile / 3, widthTile / 2,
							widthTile / 2, this);
				}
			}
		}

		harborTypeSO = ImportImages.harbor;
		harborTypeSW = ImportImages.harbor1;
		harborTypeW = ImportImages.harbor2;
		harborTypeNW = ImportImages.harbor3;
		harborTypeNO = ImportImages.harbor4;
		harborTypeO = ImportImages.harbor5;
		/* Zeichnen der verschiedenen Hafentypen (Ausrichtungen) */
		graphic.drawImage(harborTypeSO, (int) hexagonMap[0][1].getBounds()
				.getX(), (int) hexagonMap[0][1].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeSW, (int) hexagonMap[0][3].getBounds()
				.getX(), (int) hexagonMap[0][3].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeSW, (int) hexagonMap[1][5].getBounds()
				.getX(), (int) hexagonMap[1][5].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeO, (int) hexagonMap[2][0].getBounds()
				.getX(), (int) hexagonMap[2][0].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeW, (int) hexagonMap[3][6].getBounds()
				.getX(), (int) hexagonMap[3][6].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeO, (int) hexagonMap[4][0].getBounds()
				.getX(), (int) hexagonMap[4][0].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeNW, (int) hexagonMap[5][5].getBounds()
				.getX(), (int) hexagonMap[5][5].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeNO, (int) hexagonMap[6][1].getBounds()
				.getX(), (int) hexagonMap[6][1].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.drawImage(harborTypeNW, (int) hexagonMap[6][3].getBounds()
				.getX(), (int) hexagonMap[6][3].getBounds().getY(), widthTile,
				heightTile, this);
		graphic.setColor(new Color(139, 69, 19));
		Road[] islandRoads = island.getRoads();		
		 /* Zeichnen der Stra�en */
		for (int i = 0; i < islandRoads.length; i++) {
			Image roadImage = null;
			int roadOwner = islandRoads[i].getOwner();
			double x_Start = 0;
			double y_Start = 0;
			double height = 0;
			double width = 0;
			if (roadOwner != Constants.NOBODY) {
				Color playerColor = island.getSettler(roadOwner).getColor();
				switch (islandRoads[i].getDirection()) {
				case Constants.ROAD_DOWN:
					x_Start = nodesActionAreas[islandRoads[i].getStart()
							.getIndex()].getCenterX();
					y_Start = nodesActionAreas[islandRoads[i].getStart()
							.getIndex()].getCenterY();
					height = imageRoadHeight_NotVert;
					width = imageRoadWidth_NotVert;
					if (playerColor.equals(Color.RED)) {
						roadImage = ImportImages.red_road_down;
					}
					if (playerColor.equals(Color.GREEN)) {
						roadImage = ImportImages.green_road_down;
					}
					if (playerColor.equals(Color.BLUE)) {
						roadImage = ImportImages.blue_road_down;
					}
					if (playerColor.equals(Color.YELLOW)) {
						roadImage = ImportImages.yellow_road_down;
					}

					break;

				case Constants.ROAD_UP:
					x_Start = nodesActionAreas[islandRoads[i].getStart()
							.getIndex()].getCenterX();
					y_Start = nodesActionAreas[islandRoads[i].getEnd()
							.getIndex()].getCenterY();
					height = imageRoadHeight_NotVert;
					width = imageRoadWidth_NotVert;
					if (playerColor.equals(Color.RED)) {
						roadImage = ImportImages.red_road_up;
					}
					if (playerColor.equals(Color.GREEN)) {
						roadImage = ImportImages.green_road_up;
					}
					if (playerColor.equals(Color.BLUE)) {
						roadImage = ImportImages.blue_road_up;
					}
					if (playerColor.equals(Color.YELLOW)) {
						roadImage = ImportImages.yellow_road_up;
					}

					break;

				case Constants.ROAD_VERT:
					height = imageRoadHeight_Vert + nodeHeight;
					width = imageRoadWidth_Vert / 2;
					x_Start = nodesActionAreas[islandRoads[i].getStart()
							.getIndex()].getX() + width / 2;
					y_Start = nodesActionAreas[islandRoads[i].getStart()
							.getIndex()].getY() + nodeHeight / 2;
					if (playerColor.equals(Color.RED)) {
						roadImage = ImportImages.red_road_vert;
					}
					if (playerColor.equals(Color.GREEN)) {
						roadImage = ImportImages.green_road_vert;
					}
					if (playerColor.equals(Color.BLUE)) {
						roadImage = ImportImages.blue_road_vert;
					}
					if (playerColor.equals(Color.YELLOW)) {
						roadImage = ImportImages.yellow_road_vert;
					}

					break;

				default:
					break;
				}
				if (roadImage != null) {

					g.drawImage(roadImage, (int) x_Start, (int) y_Start,
							(int) width, (int) height, this);
				}
			}
		}
		/* Zeichnet die Stra�envorschau */
		if (isShowStreets()) {
			graphic.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.4f));
			for (int i = 0; i < streets.size(); i++) {

				if (currentPlayerID == 0) {

					graphic.setColor(Color.BLUE);
					graphic.fill(roadActionAreas[streets.elementAt(i)]);
				} else if (currentPlayerID == 1) {
					graphic.setColor(Color.GREEN);
					graphic.fill(roadActionAreas[streets.elementAt(i)]);
				} else if (currentPlayerID == 2) {
					graphic.setColor(Color.RED);
					graphic.fill(roadActionAreas[streets.elementAt(i)]);
				} else if (currentPlayerID == 3) {
					graphic.setColor(Color.YELLOW);
					graphic.fill(roadActionAreas[streets.elementAt(i)]);
				} else {

				}

			}
		}
		/* Zeichnet die vorhandenen Geb�ude */
		for (int i = 0; i < informationMap[0].length; i++) {
			for (int j = 0; j < informationMap.length; j++) {
				for (int k = 0; k < island.getNodes().length; k++) {
					Image city = null;
					Image settlement = null;

					if (island.getNodes()[k].getBuilding() == Constants.CITY) {
						if (island.getNodes()[k].getOwnerID() == 0) {
							city = ImportImages.cityBlue;
						} else if (island.getNodes()[k].getOwnerID() == 1) {
							city = ImportImages.cityGreen;
						} else if (island.getNodes()[k].getOwnerID() == 2) {
							city = ImportImages.cityRed;
						} else if (island.getNodes()[k].getOwnerID() == 3) {
							city = ImportImages.cityYellow;
						}
						if (city != null) {
							g.drawImage(city,
									(int) getNodesActionAreaOfIndex(k)
											.getBounds().getCenterX()
											- (int) nodeHeight,
									(int) getNodesActionAreaOfIndex(k)
											.getBounds().getCenterY()
											- (int) nodeHeight,
									(int) nodeHeight * 2, (int) nodeHeight * 2,
									this);
						}
					}

					else if (island.getNodes()[k].getBuilding() == Constants.SETTLEMENT) {
						if (island.getNodes()[k].getOwnerID() == 0) {
							settlement = ImportImages.settlementBlue;
						} else if (island.getNodes()[k].getOwnerID() == 1) {
							settlement = ImportImages.settlementGreen;
						} else if (island.getNodes()[k].getOwnerID() == 2) {
							settlement = ImportImages.settlementRed;
						} else if (island.getNodes()[k].getOwnerID() == 3) {
							settlement = ImportImages.settlementYellow;
						}
						if (settlement != null) {
							int height = (int) (nodeHeight * 2.0 / 3.0);
							g.drawImage(settlement,
									(int) getNodesActionAreaOfIndex(k)
											.getBounds().getCenterX()
											- (height),
									(int) getNodesActionAreaOfIndex(k)
											.getBounds().getCenterY()
											- (height), (height * 2),
									(height * 2), this);
						}
					}
				}
			}
		}
		/* Zeichnet die Siedlungsvorschau */
		if (isShowSettlementNodes()) {
			Image settlement = null;
			for (int i = 0; i < island.getNodes().length; i++) {
				if (currentPlayerID == 0) {
					settlement = ImportImages.settlementBlue;
				} else if (currentPlayerID == 1) {
					settlement = ImportImages.settlementGreen;
				} else if (currentPlayerID == 2) {
					settlement = ImportImages.settlementRed;
				} else if (currentPlayerID == 3) {
					settlement = ImportImages.settlementYellow;
				} else {

				}
				if (getSettlementNodes(i) != -1) {
					int height = (int) (nodeHeight * 2.0 / 3.0);
					graphic.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.4f));
					g.drawImage(settlement,
							(int) nodesActionAreas[getSettlementNodes(i)]
									.getBounds().getCenterX() - (height),
							(int) nodesActionAreas[getSettlementNodes(i)]
									.getBounds().getCenterY() - (height),
							(height * 2), (height * 2), this);
					graphic.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.4f));
				}
			}
		}
		/* Zeichnet die St�dtevorschau */
		if (isShowCityNodes()) {
			Image city = null;
			for (int i = 0; i < island.getNodes().length; i++) {
				if (currentPlayerID == 0) {
					city = ImportImages.cityBlue;
				} else if (currentPlayerID == 1) {
					city = ImportImages.cityGreen;
				} else if (currentPlayerID == 2) {
					city = ImportImages.cityRed;
				} else if (currentPlayerID == 3) {
					city = ImportImages.cityYellow;
				} else {

				}
				if (getCityNodes(i) != -1) {
					graphic.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.8f));
					g.drawImage(city, (int) nodesActionAreas[getCityNodes(i)]
							.getBounds().getCenterX() - (int) nodeHeight,
							(int) nodesActionAreas[getCityNodes(i)].getBounds()
									.getCenterY() - (int) nodeHeight,
							(int) nodeHeight * 2, (int) nodeHeight * 2, this);
					graphic.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.4f));
				}

			}
		}
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeightTile() {
		return heightTile;
	}

	public void setHeightTile(int heightTile) {
		this.heightTile = heightTile;
	}

	public int getWidthTile() {
		return widthTile;
	}

	public void setWidthTile(int widthTile) {
		this.widthTile = widthTile;
	}

	public Ellipse2D[] getNodesActionAreas() {
		return nodesActionAreas;
	}

	public double getHexagonLength() {
		return hexagonLength;
	}

	public double getHexagonWidth() {
		return hexagonWidth;
	}

	public Ellipse2D getNodesActionAreaOfIndex(int index) {
		return nodesActionAreas[index];
	}

	@SuppressWarnings("unused")
	private int getFilledRectsOwner(int index) {
		return filledRectsOwner.elementAt(index);
	}

	public void addRoad(int roadIndex, int id) {
		filledRectsOwner.add(id);
		filledRects.add(roadIndex);
	}

	public Polygon getFilledRects(int index) {
		return roadActionAreas[filledRects.elementAt(index)];
	}

	public void setFilledRects(Vector<Integer> filledRects) {
		this.filledRects = filledRects;
	}

	public Tile getTile(int x, int y) {
		int index = getTileActionAreasOfIndex(x, y);
		if(index == -1)
			return null;
		return informationMap1D[getTileActionAreasOfIndex(x, y)];
	}

	public void setSettlementNodes(int index, int value) {
		settlementNodes[index] = value;
	}

	public int getSettlementNodes(int index) {
		return settlementNodes[index];
	}

	public boolean isShowSettlementNodes() {
		return showSettlementNodes;
	}

	public void setShowSettlementNodes(boolean showSettlementNodes) {
		this.showSettlementNodes = showSettlementNodes;
	}

	public int getCurrentPlayerID() {
		return currentPlayerID;
	}

	public void setCurrentPlayerID(int currentPlayerID) {
		this.currentPlayerID = currentPlayerID;
	}

	public void resetStreets() {
		streets = new Vector<Integer>();
	}

	public void addStreet(int index) {
		streets.add(index);
	}

	public int getStreet(int index) {
		return streets.elementAt(index);
	}

	public boolean isShowStreets() {
		return showStreets;
	}

	public void setShowStreets(boolean showStreets) {
		this.showStreets = showStreets;
	}

	public void setCityNodes(int index, int value) {
		cityNodes[index] = value;

	}

	public int getCityNodes(int index) {
		return cityNodes[index];
	}

	public boolean isShowCityNodes() {
		return showCityNodes;
	}

	public void setShowCityNodes(boolean showCityNodes) {
		this.showCityNodes = showCityNodes;
	}

	public Hexagon[] getHexagonMap1D() {
		return hexagonMap1D;
	}

}