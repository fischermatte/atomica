package com.geolud.atomica.ui.util.language;

import com.geolud.atomica.util.logging.Logging;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;

/**
 * Providing texts of the ui in a required language. The mechanism is based on a
 * ResourceBundles for each language. Implemented as singleton.
 *
 * @author Georg Ludewig
 */
public class Language {
    /**
     * Due to the singleton pattern the only instance of this class.
     */
    private static Language instance;

    /**
     * Constant for German language.
     */
    public final static String DE = "de";

    /**
     * Constant for English language.
     */
    public final static String EN = "en";

    /**
     * Returns the only instance of this class. At first call it is initialized.
     *
     * @return the only instance of this class
     */
    public static Language getInstance() {
        if (instance == null) {
            instance = new Language();
        }

        return instance;
    }

    /**
     * Gets a string for the given key from the resource bundle of the current
     * language.
     *
     * @param key the key for the desired string
     * @return the string for the given key in the current language.
     */
    public static String getString(String key) {
        String value;

        try {
            // try to get bundle of current language
            ResourceBundle messages = ResourceBundle.getBundle("ResourceBundle", getInstance().currentLocale);
            // bundle found, lookup text
            value = messages.getString(key);
        } catch (RuntimeException e) {
            value = key;
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Could not find resource for " + key);
        }

        return value;
    }

    /**
     * The current Locale, can be "de" or "en".
     */
    private Locale currentLocale = null;

    /**
     * The currently supported languages.
     */
    private Set<String> supportedLanguages = null;

    /**
     * Creates a Language object by default in German.
     */
    private Language() {
        if (currentLocale == null) {
            try {
                currentLocale = new Locale(DE);
            } catch (Exception e) {
                currentLocale = Locale.getDefault();
            }
        }

        supportedLanguages = new HashSet<String>();
        supportedLanguages.add("de");
        supportedLanguages.add("en");
    }

    /**
     * Gets the current activated language.
     *
     * @return the active language
     */
    public String getLanguage() {
        return currentLocale.getLanguage();
    }

    /**
     * Returns a list of all currently supported languages.
     *
     * @return a list of all currently supported languages
     */
    public Set<String> getLanguages() {
        return supportedLanguages;
    }

    /**
     * Switches to the given language.
     *
     * @param lang the new language to switch to
     */
    public void switchLanguage(String lang) {
        if (!supportedLanguages.contains(lang)) {
            Logging.getLogger().log(Level.SEVERE, lang + " is a unsupported language.");
        } else {
            currentLocale = new Locale(lang);
        }
    }

}
