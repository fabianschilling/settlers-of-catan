package networking;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.*;

/**
 * Diese Klasse dient zum Erstellen von Logfiles, die Excepions loggen
 * 
 * @author Elisabeth Friedrich
 * 
 */
public class ExceptionLogger {

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
	 * Instanz des ExceptionLoggers
	 */
	private static ExceptionLogger instance;
	
	private ExceptionLogger() {
		initExceptionLogger();
	}

	/**
	 * initialisiert den ExceptionLogger
	 */
	private void initExceptionLogger() {
		logger = Logger.getLogger("exceptionLogger"); //$NON-NLS-1$
		formatter = new SimpleFormatter();
		calendar = Calendar.getInstance();

		try {
			fileHandler = new FileHandler("Logging/" //$NON-NLS-1$
					+ calendar.get(Calendar.YEAR) + "." //$NON-NLS-1$
					+ calendar.get(Calendar.MONTH + 1) + "." //$NON-NLS-1$
					+ calendar.get(Calendar.DAY_OF_MONTH) + "_" //$NON-NLS-1$
					+ calendar.get(Calendar.HOUR_OF_DAY) + "." //$NON-NLS-1$
					+ calendar.get(Calendar.MINUTE)
					+ "__Siedler_Logfile_Exception.txt"); //$NON-NLS-1$
		} catch (SecurityException e) {
			this.writeException(Level.SEVERE, Messages.getString("ExceptionLogger.SecurityException.FielHandler")); //$NON-NLS-1$
		} catch (IOException e) {
			this.writeException(Level.SEVERE, Messages.getString("ExceptionLogger.IOException.FileHandler")); //$NON-NLS-1$

		}
		logger.setLevel(Level.SEVERE);
		fileHandler.setFormatter(formatter);
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);
	}
	/**
	 * Sorgt daf&uuml;r, das nur genau eine Instanz der Klasse erzeugt wird
	 * @return
	 */
	public static synchronized ExceptionLogger getInstance() {
		if (instance == null) {
			instance = new ExceptionLogger();
		}
		return instance;
	}

	/**
	 * Schreibt die Nachricht, die die Exception dokumentiert
	 * 
	 * @param level
	 *            Level der Nachricht
	 * @param loggingMessage
	 *            Nachricht, die ins Logfile geschrieben wird
	 */
	public void writeException(Level level, String loggingMessage) {
		logger.log(level, loggingMessage); //$NON-NLS-1$
	}
}
