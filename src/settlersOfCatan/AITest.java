package settlersOfCatan;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Vector;
import networking.*;
import controller.*;
/**
 * Klasse, die einen KI-Gegner joinen l&auml;sst
 * @author Michael Strobl, Thomas Wimmer
 *
 */
public class AITest {
	/**
	 * String-Vektor f&uuml;r die Namen der KI-Gegner
	 */
	private Vector<String> names;

	
	public AITest(){
		names = new Vector<String>();
		names.add("Eve");
		names.add("Adam");
		names.add("Gandalf");
	}
	
	/**
	 * Methode, die einen KI-Gegner joinen l&auml;sst
	 */
	public void join() throws IOException {
		
		Socket socket = new Socket("localhost", 1337);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		Random random = new Random();
		String image = "" + random.nextInt(10);
		int index = random.nextInt(names.size());
		String[] strings = { names.get(index),
				image, "blue" };
		names.remove(index);
		Message message = new Message(strings);
		out.writeObject(message);
		out.flush();
		String mes = null;
		do {
			try {
				mes = (String) in.readObject();
			} catch (IOException e2) {

			} catch (ClassNotFoundException e1) {

			}
		} while (!mes.equals("accepted")
				&& !mes.equals("denied") && !mes.equals("full"));
		if(mes.equals("accepted")) {
			new AIController(new AIClient(socket), socket);
		}
	}
}
