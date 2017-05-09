package networking;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.*;

/**
 * Diese Klasse dient zum erstellen von Logfiles
 * 
 * @author Elisabeth Friedrich
 * 
 */
public class LoggerClass {

	/**
	 * Logger
	 */
	Logger logger;
	/** 
	 * FileHandler, der daf&uuml;r sorgt, dass Log-Nachrichten in eine Textdatei geschrieben werden
	 */
	FileHandler fileHandler;
	/**
	 * Sorgt, daf&uuml;r, das die Nachrichten in einem einfachen Ausgabeformat geschrieben werde
	 */
	SimpleFormatter formatter;
	/**
	 * Kalender, der die aktuellen Werte zurückgibt
	 */
	Calendar calendar;
	/**
	 * ExceptionLogger zum Protokollieren von auftretenden Fehlern
	 */
	ExceptionLogger exceptionLogger;
	/**
	 * Instanz der LoggerClass
	 */
	private static LoggerClass instance;


	private LoggerClass() {
		exceptionLogger = ExceptionLogger.getInstance();
		initLogger();
	}

	/**
	 * initialisiert den Logger
	 */
	private void initLogger() {
		logger = Logger.getLogger("logger"); //$NON-NLS-1$
		formatter = new SimpleFormatter();
		calendar = Calendar.getInstance();
		try {

			fileHandler = new FileHandler("Logging/"  //$NON-NLS-1$
					+ calendar.get(Calendar.YEAR) + "." //$NON-NLS-1$
					+ calendar.get(Calendar.MONTH + 1) + "." //$NON-NLS-1$
					+ calendar.get(Calendar.DAY_OF_MONTH) + "_" //$NON-NLS-1$
					+ calendar.get(Calendar.HOUR_OF_DAY) + "." + calendar.get(Calendar.MINUTE) //$NON-NLS-1$
					+ "__Siedler_Logfile.txt"); //$NON-NLS-1$
		} catch (SecurityException e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("LoggerClass.SecurityException.FileHandler")); //$NON-NLS-1$
		} catch (IOException e) {
			exceptionLogger.writeException(Level.SEVERE, Messages.getString("LoggerClass.IOException.FileHandler")); //$NON-NLS-1$
		}
		logger.setLevel(Level.ALL);
		fileHandler.setFormatter(formatter);
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);
	}
	
	/**
	 * sorgt daf&uuml;r, das nur genau eine Instanz der Klasse erzeugt wird
	 * @return instance
	 */
	public static synchronized LoggerClass getInstance() {
		if (instance == null) {
			instance = new LoggerClass();
		}
		return instance;
	}
	
	/**
	 * Schreibt eine Log-Nachricht in die erstellte Datei
	 * 
	 * @param level
	 *            Das level der Nachricht
	 * @param loggingMessage
	 *            die Nachricht
	 */
	public void writeLog(Level level, String loggingMessage) {
		// TODO Auto-generated method stub
		logger.log(level,loggingMessage); //$NON-NLS-1$
	}


}
