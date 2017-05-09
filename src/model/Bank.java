package model;

import java.io.Serializable;
import java.util.*;

/**
 * Die Bank verwaltet die Ereigniskarten. Die Bank stellt einen Stack dar, auf
 * dem die Ereigniskarten liegen. Beim Erstellen der Bank wird der Inhalt
 * gemischt.
 * 
 * @author Fabian Schilling, Florian Weiss, Christian Hemauer
 * 
 */
public class Bank extends Stack<Byte> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int knightCount = 14;
	private int victoryPointCount = 5;
	private int developmentCount = 6;

	/**
	 * Erstellt ein Objekt der Klasse <code>Bank</code>. Es werden die
	 * Ereigniskarten erstellt und danach durchgemischt.
	 */
	public Bank() {
		for (int i = 0; i < knightCount; i++) {
			this.push(Constants.KNIGHT);
		}
		for (int i = 0; i < victoryPointCount; i++) {
			this.push(Constants.VICTORYPOINTS);
		}
		for (int i = 0; i < developmentCount; i = i + 3) {
			this.push(Constants.GETRESOURCES);
			this.push(Constants.BUILDSTREETS);
			this.push(Constants.MONOPOLY);
		}
		Collections.shuffle(this);
	}

	/**
	 * Zieht eine Karte vom Kartenstapel.
	 * 
	 * @return Die vom Stapel gezogene Ereigniskarte
	 */
	public byte drawCard() {
		try {
			return pop();
		} catch (EmptyStackException ese) {
			return -1;
		}
	}

	/**
	 * Legt die ausgepielte Ereigniskarte zur&uuml;ck auf den Kartenstapel.
	 * Danach wird der Kartenstapel durchgemischt.
	 * 
	 * @param card
	 *            Die Ereigniskarte die ausgespielt und zur&uuml;ck auf den
	 *            Kartenstapel kommt
	 */
	public void returnCard(byte card) {
		push(card);
		Collections.shuffle(this);
	}

}
