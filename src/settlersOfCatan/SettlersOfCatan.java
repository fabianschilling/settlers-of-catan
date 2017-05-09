package settlersOfCatan;

import java.io.File;


import gui.ServerGUI;
import javax.swing.SwingUtilities;
import controller.ServerController;
/**
 * Startet die Server-Gui
 * @author Christian Hemauer, Michael Strobl
 *
 */
public class SettlersOfCatan {
	public static void main(final String[] args) {

		Runnable gui = new Runnable() {

			public void run() {
				new ServerGUI(new ServerController()).setVisible(true);
			}
		};
		SwingUtilities.invokeLater(gui);
		File file = new File("Logging");
		file.mkdir();
	}
}
