package audio;

import java.util.logging.Level;

import networking.ExceptionLogger;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**Klasse um Audiodateien &uuml;ber einen Thread laufen zu lassen.
 * @author Florian Weiss
 *
 */
public class MP3 {
	/**
	 * Thread um die Musik laufen zu lassen
	 */
	private MyThreadForMP3 thread;
	/**
	 * Boolean Flag, ob der Player gerade die Audiodatei abspielt oder nicht
	 */
	private boolean audioIsPlaying;

	/**
	 * Boolean Flag, ob der Player in der Dauerschleife spielen soll oder nicht
	 */
	private boolean loop;
	
	/**
	 * Boolean Flag, ob der Sound ausgestellt ist oder nicht
	 */
	private boolean muted;

	private ExceptionLogger exceptionLogger;
	
	/**
	 * Konstruktor f&uuml;r den Audiospieler
	 */
	public MP3() {
		exceptionLogger = ExceptionLogger.getInstance();
		audioIsPlaying = false;
		loop = false;
		muted = false;
	}

	/**Spielt das jeweilige Lied in einem Thread ab.
	 * @param audioPath Pfad zur Audiodatei
	 * @param loop true wenn es eine Dauerschleife sein soll, false wenn das Lied nur einmal gespielt werden soll
	 */
	public void playSound(String audioPath, boolean loop) {
		if (!audioIsPlaying && !muted) {
			thread = new MyThreadForMP3(audioPath);

			this.loop = loop;
		}

	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public boolean isAudioIsPlaying() {
		return audioIsPlaying;
	}

	/**
	 * Stopt das Lied und den Thread.
	 */
	public void stopSound() {
		if (audioIsPlaying) {			
			if (thread != null) {
				thread.player.close();
				thread = null;
			}
			audioIsPlaying = false;
			loop = false;
		}

	}


	/**Thread der die Musik abspielt.
	 * @author Florian Weiss
	 *
	 */
	class MyThreadForMP3 extends Thread {
		/**
		 * Audiospieler
		 */
		private volatile Player player;
		/**
		 * Pfad zur Audiodatei
		 */
		private String audioPath;

		/**Konstruktor des Threads.
		 * @param audioPath Pfad zur Audiodatei
		 */
		public MyThreadForMP3(String audioPath) {			
			this.audioPath = audioPath;
			start();
		}



		@Override
		public void run() {
			try {
				audioIsPlaying = true;
				do {
					player = new Player(getClass().getResourceAsStream(audioPath));
					player.play();
				} while (loop);

				player.close();
				thread = null;
				audioIsPlaying = false;

			} catch (JavaLayerException e) {
				exceptionLogger.writeException(Level.SEVERE, "JavaLayerException im MP3: Fehler beim Abspielen der MP3-Dateien");
			}
		}
	}

}
