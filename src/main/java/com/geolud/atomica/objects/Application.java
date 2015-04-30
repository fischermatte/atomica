package com.geolud.atomica.objects;

import com.geolud.atomica.util.logging.Logging;

import java.util.prefs.Preferences;

/**
 * This class holds information of the application itself like the default game
 * and editor settings and the language to start with. It is responsible to
 * retrieve and store them from/into user preferences folder.
 *
 * @author Georg Ludewig
 */
public class Application {
    /**
     * The game settings a new game is starting with.
     */
    private DefaultGameSettings defaultGameSettings;

    /**
     * The editor settings the editor is starting with.
     */
    private EditorGameSettings editorGameSettings;

    /**
     * The language of all texts in the ui of the application.
     */
    private String language;

    /**
     * Constructs an Application retrieving the settings of the last session
     * from user preferences.
     */
    public Application() {
        super();

        defaultGameSettings = new DefaultGameSettings();
        editorGameSettings = new EditorGameSettings();

        loadPrefs();
    }

    /**
     * Returns the default settings for an editing game.
     *
     * @return the default settings for a game to be edited.
     */
    public EditorGameSettings getEditorGameSettings() {
        return editorGameSettings;
    }

    /**
     * Returns the default settings for a new game to play.
     *
     * @return the default settings for a new game to play.
     */
    public DefaultGameSettings getGameSettings() {
        return defaultGameSettings;
    }

    /**
     * Returns the language of the application like "de" for German or "en" for
     * English.
     *
     * @return the language of the application
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Retrieves the application settings of the last session from the user
     * directory. This includes the last selected language, the game settings
     * and the editor settings.
     */
    private void loadPrefs() {
        // retrieve language
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(Application.class);
            language = prefs.get("ATOMICA_LANG", "de");
        } catch (RuntimeException e) {
            language = "de";
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to load user prefs for Application.");
        }

        // retrieve game settings
        defaultGameSettings.loadPrefs();

        // retrieve editor settings
        editorGameSettings.loadPrefs();
    }

    /**
     * Stores the current settings as preferences in user preferences.
     */
    public void savePrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(Application.class);

            // store the current language
            prefs.put("ATOMICA_LANG", language);

            // store the game preferences
            defaultGameSettings.savePrefs();

            // store the editor preferences
            editorGameSettings.savePrefs();
        } catch (RuntimeException e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to store user prefs for Application.");
        }

    }

    /**
     * Sets the current Language of the application.
     *
     * @param language the new language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

}
