package networking;

import gui.PolygonMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

import controller.AIController;

import model.Constants;
import model.IslandOfCatan;
import model.Settler;

/**
 * Intelligenter Client.
 * 
 * @author Michael Strobl
 * 
 */
public class AIClient implements ClientInterface {

	/**
	 * Verbindungssocket zum Server.
	 */
	private Socket socket;

	/**
	 * Enth&auml;lt den Zustand der Insel.
	 */
	private IslandOfCatan island;

	/**
	 * In der Anfangsphase oder nicht.
	 */
	private boolean beginning;

	/**
	 * Siedler des Spielers.
	 */
	private Settler settler;

	/**
	 * Graphische Oberfl&auml;che der Insel.
	 */
	private PolygonMap polygonMap;

	/**
	 * Aktuelle Phase.
	 */
	private int currentPhase;

	/**
	 * In der W&uuml;rfelphase oder nicht.
	 */
	private boolean firstRoll;
	
	private ExceptionLogger exceptionLogger;

	public AIClient(Socket socket) {
		exceptionLogger = ExceptionLogger.getInstance();
		this.socket = socket;
		beginning = true;
		currentPhase = Constants.PHASE_1;
	}

	public void start(String serverIP, String username) {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			socket = new Socket(serverIP, 1337);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e1) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIClient.AIClient.UnKnownHostException.SocketNichterzeugt")); //$NON-NLS-1$
		} catch (IOException e1) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIClient.AIClientIOException.keinInOut")); //$NON-NLS-1$
		}
		try {
			String[] strings = { username, "Chuck Norris", "blue" }; //$NON-NLS-1$ //$NON-NLS-2$
			Message message = new Message(strings);
			out.writeObject(message);
			out.flush();
		} catch (IOException e2) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIClient.AIClient.IOException.Namensweitergabe")); //$NON-NLS-1$
		}
		String message = null;
		do {
			try {
				message = (String) in.readObject();
			} catch (IOException e2) {
				exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIClient.AIClient.IOException.MessagessendenMistake")); //$NON-NLS-1$
			} catch (ClassNotFoundException e1) {
				exceptionLogger.writeException(Level.SEVERE, Messages.getString("AIClient.AIClient.ClassNotFoundException.MessageFailed")); //$NON-NLS-1$
			}
		} while (!message.equals("accepted") && !message.equals("denied")); //$NON-NLS-1$ //$NON-NLS-2$
		new AIController(this, socket);
	}

	public void nextPlayer() {
		island.nextPlayer();
	}

	public PolygonMap getPolygonMap() {
		return polygonMap;
	}

	public void setPolygonMap(PolygonMap polygonMap) {
		this.polygonMap = polygonMap;
	}

	public int getCurrentPlayer() {
		return island.getCurrentPlayer().getID();
	}

	public Socket getSocket() {
		return socket;
	}

	public IslandOfCatan getIsland() {
		return island;
	}

	public void setIsland(IslandOfCatan island) {
		this.island = island;
	}

	public boolean isBeginning() {
		return beginning;
	}

	public void setBeginning(boolean beginning) {
		this.beginning = beginning;
	}

	public Settler getSettler() {
		return settler;
	}

	public void setSettler(Settler settler) {
		this.settler = settler;
	}

	public int getID() {
		return settler.getID();
	}

	public String getUsername() {
		return settler.getUsername();
	}

	public int getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}

	public boolean isFirstRoll() {
		return firstRoll;
	}

	public void setFirstRoll(boolean firstRoll) {
		this.firstRoll = firstRoll;
	}

}
