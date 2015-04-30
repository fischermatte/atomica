package com.geolud.atomica.objects;

import com.geolud.atomica.util.logging.Logging;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Represents the default settings for an editing game. It sets the required
 * level, the board size and the base factor.
 *
 * @author Georg Ludewig
 */
public class EditorGameSettings extends GameSettings {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 687456749473399448L;

    /**
     * Creates and initializes the default settings for an editing game.
     */
    public EditorGameSettings() {
        super();

        setDefaultValues();
    }

    /**
     * Retrieves the settings from the user preferences.
     *
     * @return true if the settings could be loaded
     */
    protected boolean loadPrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(EditorGameSettings.class);

            board.setRows(prefs.getInt("rows", Board.DEF_ROWS));
            board.setCols(prefs.getInt("cols", Board.DEF_COLS));

            levels = new ArrayList<Level>();
            levels.add(new Level(1, 0, prefs.getInt("level1_colors", 3)));
        } catch (Exception e) {
            Logging.getLogger().log(
                    java.util.logging.Level.SEVERE,
                    "Failed to load user prefs for "
                            + this.getClass().getName() + ".");
            setDefaultValues();
            return false;
        }

        Logging.getLogger().log(
                java.util.logging.Level.INFO,
                "Succesfully loaded user prefs for  "
                        + this.getClass().getName() + ".");

        return true;
    }

    /**
     * Stores the settings into the user.
     */
    public void savePrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(EditorGameSettings.class);

            prefs.putInt("rows", board.getRows());
            prefs.putInt("cols", board.getCols());

            prefs.putInt("level1_colors", levels.get(0).getNumberOfColors());

        } catch (RuntimeException e) {
            Logging.getLogger().log(
                    java.util.logging.Level.SEVERE,
                    "Failed to store user prefs for  "
                            + this.getClass().getName() + ".");
        }

        Logging.getLogger().log(java.util.logging.Level.INFO,
                "Stored user prefs for  " + this.getClass().getName() + ".");
    }

    /**
     * Sets the default values.
     *
     * @see com.geolud.atomica.objects.GameSettings#setDefaultValues()
     */
    @Override
    public void setDefaultValues() {
        levels.clear();
        Level theOnlyLevel = new Level(1, 0, 3);
        levels.add(theOnlyLevel);

        super.setDefaultValues();
    }

}
