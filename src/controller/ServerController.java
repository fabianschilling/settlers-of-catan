package controller;

import gui.ImportServerImages;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import networking.ExceptionLogger;
import networking.LoggerClass;
import networking.ServerThread;
import model.GameLogic;
import model.IslandOfCatan;

/**
 * Controller des Servers, nimmt eingehende Verbindungsanfragen an
 * 
 * @author Michael Strobl, Christian Hemauer
 * 
 */
public class ServerController implements WindowListener {

	/**
	 * Port des Sockets
	 */
	private final int PORT = 1337;

	/**
	 * Anzahl der teilnehmenden Spieler
	 */
	private int maximumPlayers;

	/**
	 * Serversocket
	 */
	private ServerSocket serverSocket;

	/**
	 * Enth&auml;lt die Sockets zu den Clients
	 */
	private Vector<Socket> clientVector;

	/**
	 * Enth&auml;lt die namen der Spieler
	 */
	private Vector<String> namesVector;
	/**
	 * Vector der die Farben der einzelnen Clients/Spieler enth&auml;lt.
	 */
	@SuppressWarnings("unused")
	private Vector<String> colorVector;;
	/**
	 * Vector der die Links aus die Anzeigebilder der einzelnen Clients/Spieler
	 * enth&auml;lt.
	 */
	@SuppressWarnings("unused")
	private Vector<String> avatarVector;
	/**
	 * Logger
	 */
	private LoggerClass logger;
	private ExceptionLogger exceptionLogger;
	/**
	 * Klasse zum Importieren der ServerImages
	 */
	private ImportServerImages importServerImages;
	/**
	 * Boolean f&uuml;r den Status, dass der Server l&auml;uft
	 */
	private boolean isServerRunning;

	/**
	 * Konstruktor
	 */
	public ServerController() {
		importServerImages = new ImportServerImages();
		importServerImages.loadServerPics();
		namesVector = new Vector<String>();
		clientVector = new Vector<Socket>();
		avatarVector = new Vector<String>();
		colorVector = new Vector<String>();
		isServerRunning = true;
		exceptionLogger = ExceptionLogger.getInstance();
	}

	/**
	 * Startet den Server
	 * 
	 * @param playersCount
	 *            Anzahl an Spielern
	 * @throws IOException
	 */
	public void startServer(int playersCount) throws IOException {
		int blocker = 0;
		logger = LoggerClass.getInstance();
		maximumPlayers = playersCount;
		try {
			serverSocket = new ServerSocket(PORT);
			IslandOfCatan island = null;
			island = new IslandOfCatan(maximumPlayers, false);
			GameLogic gameLogic = null;
			gameLogic = new GameLogic(clientVector, island, maximumPlayers,
					true);
			while (clientVector.size() < maximumPlayers) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
				} catch (SocketException e) {
					exceptionLogger.writeException(Level.SEVERE,
							Messages.getString("ServerController.SocketExceptionServerController")); //$NON-NLS-1$
				}
				
				if(blocker == 0){
				setisServerRunning(true);
				blocker++;
				}
				ServerThread thread = new ServerThread(socket, clientVector,
						namesVector, island, maximumPlayers, gameLogic, clientVector.size());
				thread.start();
			}
			
		} catch(Exception e) {
			serverSocket.close();
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("ServerController.Exception.nichtErstellt")); //$NON-NLS-1$
		}
	}


	public void setisServerRunning(boolean b) {
		isServerRunning = b;
		
	}
	public boolean getIsServerRunning(){
		return isServerRunning;
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public LoggerClass getLogger() {
		return logger;
	}

	public void setLogger(LoggerClass logger) {
		this.logger = logger;
	}
}
