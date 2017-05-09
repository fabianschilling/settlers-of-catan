package gui;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Klasse, die ein Sechseck darstellt.
 * Verwendet um die <code>Tiles</code> beim Klick zu erkennen.
 * @author Florian Weiss, Christian Hemauer, Fabian Schilling, Elisabeth Friedrich
 * 
 */
@SuppressWarnings("serial")
public class Hexagon extends Polygon {

	/**
	 * Radius
	 */
	double radius;

	/**
	 * Sinus
	 */
	double sin = (Math.sin(Math.PI / 6));
	/**
	 * Cosinus
	 */
	double cos = (Math.cos(Math.PI / 6));
	/**
	 * Sinus mal Raduis
	 */
	double multSin;
	/**
	 * Cosinus mal Radius
	 */
	double multCos;
	/**
	 * Sinus von 30 Grad
	 */
	double sin30;
	/**
	 * Cosinus von 30 Grad
	 */
	double cosin30;

	/**
	 * ActionArea des ersten Knotens
	 */
	private Ellipse2D.Double nodeCircleOne;
	/**
	 * ActionArea des zweiten Knotens
	 */
	private Ellipse2D.Double nodeCircleTwo;
	/**
	 * ActionArea des dritten Knotens
	 */
	private Ellipse2D.Double nodeCircleThree;
	/**
	 * ActionArea des vierten Knotens
	 */
	private Ellipse2D.Double nodeCircleFour;
	/**
	 * ActionArea des f&uuml;nften Knotens
	 */
	private Ellipse2D.Double nodeCircleFive;
	/**
	 * ActionArea des sechsten Knotens
	 */
	private Ellipse2D.Double nodeCircleSix;
	/**
	 * Gr&ouml;&szlig;e der Knoten ActionArea
	 */
	double circleSize;
	/**
	 * Array mit den ActionAreas der Knoten
	 */
	private Ellipse2D[] nodeCircles;

	/**
	 * X-Koordinate des ersten Knotens
	 */
	double pointCircleOne_X;
	/**
	 * Y-Koordinate des ersten Knotens
	 */
	double pointCircleOne_Y;
	/**
	 * X-Koordinate des zweiten Knotens
	 */
	double pointCircleTwo_X;
	/**
	 * Y-Koordinate des zweiten Knotens
	 */
	double pointCircleTwo_Y;
	/**
	 * X-Koordinate des dritten Knotens
	 */
	double pointCircleThree_X;
	/**
	 * Y-Koordinate des dritten Knotens
	 */
	double pointCircleThree_Y;
	/**
	 * X-Koordinate des vierten Knotens
	 */
	double pointCircleFour_X;
	/**
	 * Y-Koordinate des vierten Knotens
	 */
	double pointCircleFour_Y;
	/**
	 * X-Koordinate des f&uuml;nften Knotens
	 */
	double pointCircleFive_X;
	/**
	 * Y-Koordinate des f&uuml;nften Knotens
	 */
	double pointCircleFive_Y;
	/**
	 * X-Koordinate des sechsten Knotens
	 */
	double pointCircleSix_X;
	/**
	 * Y-Koordinate des sechsten Knotens
	 */
	double pointCircleSix_Y;

	/**
	 * ActionArea der ersten Stra&szlig;e
	 */
	private Polygon rectOne;
	/**
	 * ActionArea der zweiten Stra&szlig;e
	 */
	private Polygon rectTwo;
	/**
	 * ActionArea der dritten Stra&szlig;e
	 */
	private Polygon rectThree;
	/**
	 * ActionArea der vierten Stra&szlig;e
	 */
	private Polygon rectFour;
	/**
	 * ActionArea der f&uuml;nften Stra&szlig;e
	 */
	private Polygon rectFive;
	/**
	 * ActionArea der sechsten Stra&szlig;e
	 */
	private Polygon rectSix;
	
	/**
	 * Array mit den ActionAreas der Stra&szlig;en
	 */
	private Polygon[] roadRects;

	/**
	 * X-Koordinate des ersten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointOne_X;
	/**
	 * Y-Koordinate des ersten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointOne_Y;
	/**
	 * X-Koordinate des zweiten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointTwo_X;
	/**
	 * Y-Koordinate des zweiten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointTwo_Y;
	/**
	 * X-Koordinate des dritten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointThree_X;
	/**
	 * Y-Koordinate des dritten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointThree_Y;
	/**
	 * X-Koordinate des vierten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointFour_X;
	/**
	 * Y-Koordinate des vierten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointFour_Y;
	/**
	 * X-Koordinate des f&uuml;nften Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointFive_X;
	/**
	 * Y-Koordinate des f&uuml;nften Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointFive_Y;
	/**
	 * X-Koordinate des sechsten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointSix_X;
	/**
	 * Y-Koordinate des sechsten Punkts des Berechnung der Stra&szlig;en ActionAreas
	 */
	private double pointSix_Y;

	/**
	 * Radius des Kreises
	 */
	private double circleRadius;

	@SuppressWarnings("unused")
	private Image village;
	/**
	 * X-Koordinate
	 */
	private int x;
	/**
	 * Y-Koordinate
	 */
	private int y;
	/**
	 * ActionArea des Sechsecks
	 */
	private Rectangle tileActionArea;

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

	/**Konstruktor des Hexagons
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @param radius Radius
	 */
	public Hexagon(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		multSin = sin * radius;
		multCos = cos * radius;
		sin30 = 1.0 / 2.0;
		cosin30 = 1.0 / 2.0 * Math.sqrt(3);
		circleSize = radius / 3.0;
		circleRadius = circleSize / 2.0;
		tileActionArea = new Rectangle(radius, radius);
		initHexagon();

		initActionAreaNodes();

		initActionAreaRoads();
		initTileActionAreas();

	}

	/**
	 * Initialisiert die ActionAreas der Felder
	 */
	public void initTileActionAreas() {
		tileActionArea.setBounds((int) super.getBounds().getCenterX()
				- getRadius() / 2, (int) super.getBounds().getCenterY()
				- getRadius() / 2, getRadius(), getRadius());

	}

	/**
	 * Initialisiert das Sechseck
	 */
	public void initHexagon() {

		pointOne_X = (x);
		pointOne_Y = (y - radius);
		pointTwo_X = (x + multCos);
		pointTwo_Y = (y - multSin);
		pointThree_X = (x + multCos);
		pointThree_Y = (y + multSin);
		pointFour_X = (x);
		pointFour_Y = (y + radius);
		pointFive_X = (x - multCos);
		pointFive_Y = (y + multSin);
		pointSix_X = (x - multCos);
		pointSix_Y = (y - multSin);

		imageRoadWidth_NotVert = (pointOne_X - pointSix_X);
		imageRoadHeight_NotVert = (pointSix_Y - pointOne_Y);

		addPoint((int) pointOne_X, (int) pointOne_Y);
		addPoint((int) pointTwo_X, (int) pointTwo_Y);
		addPoint((int) pointThree_X, (int) pointThree_Y);
		addPoint((int) pointFour_X, (int) pointFour_Y);
		addPoint((int) pointFive_X, (int) pointFive_Y);
		addPoint((int) pointSix_X, (int) pointSix_Y);
	}

	/**
	 * Initialisiert die ActionAreas der Knoten
	 */
	public void initActionAreaNodes() {
		village = ImportImages.testvillage;

		pointCircleOne_X = (pointOne_X - circleRadius);
		pointCircleOne_Y = (pointOne_Y - circleRadius);
		pointCircleTwo_X = (pointTwo_X - circleRadius);
		pointCircleTwo_Y = (pointTwo_Y - circleRadius);
		pointCircleThree_X = (pointThree_X - circleRadius);
		pointCircleThree_Y = (pointThree_Y - circleRadius);
		pointCircleFour_X = (pointFour_X - circleRadius);
		pointCircleFour_Y = (pointFour_Y - circleRadius);
		pointCircleFive_X = (pointFive_X - circleRadius);
		pointCircleFive_Y = (pointFive_Y - circleRadius);
		pointCircleSix_X = (pointSix_X - circleRadius);
		pointCircleSix_Y = (pointSix_Y - circleRadius);

		nodeCircles = new Ellipse2D[6];

		nodeCircleOne = new Ellipse2D.Double(pointCircleOne_X,
				pointCircleOne_Y, circleSize, circleSize);
		nodeCircleTwo = new Ellipse2D.Double(pointCircleTwo_X,
				pointCircleTwo_Y, circleSize, circleSize);
		nodeCircleThree = new Ellipse2D.Double(pointCircleThree_X,
				pointCircleThree_Y, circleSize, circleSize);
		nodeCircleFour = new Ellipse2D.Double(pointCircleFour_X,
				pointCircleFour_Y, circleSize, circleSize);
		nodeCircleFive = new Ellipse2D.Double(pointCircleFive_X,
				pointCircleFive_Y, circleSize, circleSize);
		nodeCircleSix = new Ellipse2D.Double(pointCircleSix_X,
				pointCircleSix_Y, circleSize, circleSize);

		nodeCircles[0] = nodeCircleOne;
		nodeCircles[1] = nodeCircleTwo;
		nodeCircles[2] = nodeCircleThree;
		nodeCircles[3] = nodeCircleFour;
		nodeCircles[4] = nodeCircleFive;
		nodeCircles[5] = nodeCircleSix;
	}

	/**
	 * Initialisiert die ActionAreas der Stra&szlig;en
	 */
	public void initActionAreaRoads() {
		roadRects = new Polygon[6];

		double pointX_Touching_One = pointOne_X + cosin30 * circleRadius;
		double pointY_Touching_One = pointOne_Y + sin30 * circleRadius;

		double pointX_PolyOne_One = pointX_Touching_One + sin30 * circleRadius;
		double pointY_PolyOne_One = pointY_Touching_One - cosin30
				* circleRadius;

		double pointX_PolyOne_Two = pointX_PolyOne_One + cosin30
				* (radius - 2 * circleRadius);
		double pointY_PolyOne_Two = pointY_PolyOne_One + sin30
				* (radius - 2 * circleRadius);

		double pointX_PolyOne_Three = pointX_PolyOne_Two - sin30 * 2.0
				* circleRadius;
		double pointY_PolyOne_Three = pointY_PolyOne_Two + cosin30 * 2.0
				* circleRadius;

		double pointX_PolyOne_Four = pointX_PolyOne_One - sin30 * 2.0
				* circleRadius;
		double pointY_PolyOne_Four = pointY_PolyOne_One + cosin30 * 2.0
				* circleRadius;

		rectOne = new Polygon(new int[] { (int) (pointX_PolyOne_One),
				(int) (pointX_PolyOne_Two), (int) (pointX_PolyOne_Three),
				(int) (pointX_PolyOne_Four) }, new int[] {
				(int) (pointY_PolyOne_One), (int) (pointY_PolyOne_Two),
				(int) (pointY_PolyOne_Three), (int) (pointY_PolyOne_Four) }, 4);

		rectTwo = new Polygon(new int[] { (int) (pointTwo_X + circleRadius),
				(int) (pointTwo_X - circleRadius),
				(int) (pointThree_X - circleRadius),
				(int) (pointThree_X + circleRadius) }, new int[] {
				(int) (pointTwo_Y + circleRadius),
				(int) (pointTwo_Y + circleRadius),
				(int) (pointThree_Y - circleRadius),
				(int) (pointThree_Y - circleRadius) }, 4);

		double pointX_Touching_Three = pointThree_X - cosin30 * circleRadius;
		double pointY_Touching_Three = pointThree_Y + sin30 * circleRadius;

		double pointX_PolyThree_One = pointX_Touching_Three - sin30
				* circleRadius;
		double pointY_PolyThree_One = pointY_Touching_Three - cosin30
				* circleRadius;

		double pointX_PolyThree_Two = pointX_PolyThree_One - cosin30
				* (radius - 2 * circleRadius);
		double pointY_PolyThree_Two = pointY_PolyThree_One + sin30
				* (radius - 2 * circleRadius);

		double pointX_PolyThree_Three = pointX_PolyThree_Two + sin30 * 2.0
				* circleRadius;
		double pointY_PolyThree_Three = pointY_PolyThree_Two + cosin30 * 2.0
				* circleRadius;

		double pointX_PolyThree_Four = pointX_PolyThree_One + sin30 * 2.0
				* circleRadius;
		double pointY_PolyThree_Four = pointY_PolyThree_One + cosin30 * 2.0
				* circleRadius;

		rectThree = new Polygon(new int[] { (int) (pointX_PolyThree_One),
				(int) (pointX_PolyThree_Two), (int) (pointX_PolyThree_Three),
				(int) (pointX_PolyThree_Four) },
				new int[] { (int) (pointY_PolyThree_One),
						(int) (pointY_PolyThree_Two),
						(int) (pointY_PolyThree_Three),
						(int) (pointY_PolyThree_Four) }, 4);

		double pointX_Touching_Four = pointFive_X + cosin30 * circleRadius;
		double pointY_Touching_Four = pointFive_Y + sin30 * circleRadius;

		double pointX_PolyFour_One = pointX_Touching_Four + sin30
				* circleRadius;
		double pointY_PolyFour_One = pointY_Touching_Four - cosin30
				* circleRadius;

		double pointX_PolyFour_Two = pointX_PolyFour_One + cosin30
				* (radius - 2 * circleRadius);
		double pointY_PolyFour_Two = pointY_PolyFour_One + sin30
				* (radius - 2 * circleRadius);

		double pointX_PolyFour_Three = pointX_PolyFour_Two - sin30 * 2.0
				* circleRadius;
		double pointY_PolyFour_Three = pointY_PolyFour_Two + cosin30 * 2.0
				* circleRadius;

		double pointX_PolyFour_Four = pointX_PolyFour_One - sin30 * 2.0
				* circleRadius;
		double pointY_PolyFour_Four = pointY_PolyFour_One + cosin30 * 2.0
				* circleRadius;

		rectFour = new Polygon(new int[] { (int) (pointX_PolyFour_One),
				(int) (pointX_PolyFour_Two), (int) (pointX_PolyFour_Three),
				(int) (pointX_PolyFour_Four) }, new int[] {
				(int) (pointY_PolyFour_One), (int) (pointY_PolyFour_Two),
				(int) (pointY_PolyFour_Three), (int) (pointY_PolyFour_Four) },
				4);

		rectFive = new Polygon(new int[] { (int) (pointFive_X + circleRadius),
				(int) (pointFive_X - circleRadius),
				(int) (pointSix_X - circleRadius),
				(int) (pointSix_X + circleRadius) }, new int[] {
				(int) (pointFive_Y - circleRadius),
				(int) (pointFive_Y - circleRadius),
				(int) (pointSix_Y + circleRadius),
				(int) (pointSix_Y + circleRadius) }, 4);

		double pointX_Touching_Six = pointOne_X - cosin30 * circleRadius;
		double pointY_Touching_Six = pointOne_Y + sin30 * circleRadius;

		double pointX_PolySix_One = pointX_Touching_Six - sin30 * circleRadius;
		double pointY_PolySix_One = pointY_Touching_Six - cosin30
				* circleRadius;

		double pointX_PolySix_Two = pointX_PolySix_One - cosin30
				* (radius - 2 * circleRadius);
		double pointY_PolySix_Two = pointY_PolySix_One + sin30
				* (radius - 2 * circleRadius);

		double pointX_PolySix_Three = pointX_PolySix_Two + sin30 * 2.0
				* circleRadius;
		double pointY_PolySix_Three = pointY_PolySix_Two + cosin30 * 2.0
				* circleRadius;

		double pointX_PolySix_Four = pointX_PolySix_One + sin30 * 2.0
				* circleRadius;
		double pointY_PolySix_Four = pointY_PolySix_One + cosin30 * 2.0
				* circleRadius;

		rectSix = new Polygon(new int[] { (int) (pointX_PolySix_One),
				(int) (pointX_PolySix_Two), (int) (pointX_PolySix_Three),
				(int) (pointX_PolySix_Four) }, new int[] {
				(int) (pointY_PolySix_One), (int) (pointY_PolySix_Two),
				(int) (pointY_PolySix_Three), (int) (pointY_PolySix_Four) }, 4);

		roadRects[0] = rectOne;
		roadRects[1] = rectTwo;
		roadRects[2] = rectThree;
		roadRects[3] = rectFour;
		roadRects[4] = rectFive;
		roadRects[5] = rectSix;

		imageRoadWidth_Vert = rectTwo.getBounds().getWidth();
		imageRoadHeight_Vert = rectTwo.getBounds().getHeight();
	}

	public Ellipse2D[] getNodeCircles() {
		return nodeCircles;
	}

	public Ellipse2D getNodeCircleOfIndex(int index) {
		return nodeCircles[index];
	}

	public Polygon[] getRoadRects() {
		return roadRects;
	}

	public Polygon getRoadRectOfIndex(int index) {
		return roadRects[index];
	}

	public int getRadius() {
		return (int) radius;
	}

	public Rectangle getTileActionArea() {
		return tileActionArea;
	}

	public double getImageRoadWidth_NotVert() {
		return imageRoadWidth_NotVert;
	}

	public double getImageRoadHeight_NotVert() {
		return imageRoadHeight_NotVert;
	}

	public double getImageRoadHeight_Vert() {
		return imageRoadHeight_Vert;
	}

	public double getImageRoadWidth_Vert() {
		return imageRoadWidth_Vert;
	}

}
