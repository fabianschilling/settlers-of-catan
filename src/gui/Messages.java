package gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "gui.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(
			"de", "DE"));

	private Messages() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(
				"de", "DE"));
	}

	public static void initDE() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(
				"de", "DE"));
	}

	public static void initEN() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(
				"en", "US"));
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
