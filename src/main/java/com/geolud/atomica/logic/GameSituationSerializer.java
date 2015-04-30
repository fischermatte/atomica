package com.geolud.atomica.logic;

import com.geolud.atomica.objects.GameSituation;
import com.geolud.atomica.util.logging.Logging;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.Preferences;

/**
 * Simple class for De-/Serialization of a <code>GameSituation</code> object
 * into/from a binary file.
 *
 * @author Georg Ludewig
 */
public class GameSituationSerializer {
    /**
     * The file extension for a serialized game (.atomica).
     */
    public static final String ATOMICA_FILEEXTENSION = "com/geolud/atomica";

    /**
     * Creates a GameSituationSerializer. Tries to read the last used path from
     * user preferences.
     */
    public GameSituationSerializer() {
        loadPrefs();
    }

    private String lastPath = "";

    /**
     * Reads a GameSituation from a binary file at the given <code>path</code>.
     *
     * @param path the absolute path to binary file
     * @return true if the deserializaion successful
     */
    public GameSituation load(String path) {
        GameSituation gameSituation = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(path);
            ObjectInputStream os = new ObjectInputStream(fs);
            gameSituation = (GameSituation) os.readObject();
            setLastPath(path);
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to save GameSituation");
        }

        try {
            if (fs != null) {
                fs.close();
            }
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Could not close File Handle");
        }

        return gameSituation;
    }

    /**
     * Saves a GameSituation in a binary file at the given <code>path</code>.
     *
     * @param gameSituation the situation to be serialized
     * @param path          the absolute path to the binary file
     */
    public void save(GameSituation gameSituation, String path) {
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(path);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(gameSituation);
            setLastPath(path);
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to save GameSituation");
        }

        try {
            if (fs != null) {
                fs.close();
            }
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Could not close File Handle");
        }
    }

    /**
     * Returns the last selected path of a de-/serialized game situation so the
     * JFileChooser will suggest that location at first.
     *
     * @return the last selected path of a de-/serialized game situation
     */
    public String getLastPath() {
        return lastPath;
    }

    /**
     * @param path
     */
    private void setLastPath(String path) {
        lastPath = path;
        savePrefs();
    }

    /**
     * Stores the last last selected path of a de-/serialized game situation in
     * user preferences.
     */
    private void savePrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(GameSituationSerializer.class);

            prefs.put("LAST_PATH", lastPath);
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to save last path to user prefs.");
        }
    }

    /**
     * Loads the last last selected path of a de-/serialized game situation from
     * user preferences.
     */
    private void loadPrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(GameSituationSerializer.class);
            lastPath = prefs.get("LAST_PATH", lastPath);
        } catch (Exception e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Failed to load last path from user prefs.");
        }
    }
}
