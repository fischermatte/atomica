package com.geolud.atomica.objects;

import com.geolud.atomica.util.logging.Logging;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Represents the default settings defining a game. It sets the required levels,
 * the board size and the base factor.
 *
 * @author Georg Ludewig
 */
public class DefaultGameSettings extends GameSettings {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 9074931489864252353L;

    /**
     * Creates and initializes the default settings.
     */
    public DefaultGameSettings() {
        super();

        setDefaultValues();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geolud.atomica.objects.GameSettings#deepClone()
     */
    @Override
    public DefaultGameSettings deepClone() {
        return (DefaultGameSettings) super.deepClone();
    }

    /**
     * Retrieves the settings from the user preferences.
     *
     * @return true if the settings could be loaded
     */
    protected boolean loadPrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(DefaultGameSettings.class);

            board.setRows(prefs.getInt("rows", Board.DEF_ROWS));
            board.setCols(prefs.getInt("cols", Board.DEF_COLS));
            GameSettings.setBaseFactor(prefs.getInt("baseFactor",
                    DEFAULT_BASEFACTOR));

            levels = new ArrayList<Level>();

            levels.add(new Level(1, prefs.getInt("level1", 100), 3));
            levels.add(new Level(2, prefs.getInt("level2", 200), 4));
            levels.add(new Level(3, prefs.getInt("level3", 300), 5));
            levels.add(new Level(4, prefs.getInt("level4", 400), 6));
            levels.add(new Level(5, prefs.getInt("level5", 500), 7));
            levels.add(new Level(6, prefs.getInt("level6", 600), 8));
            levels.add(new Level(7, prefs.getInt("level7", 700), 9));
            levels.add(new Level(8, prefs.getInt("level8", 800), 10));
            levels.add(new Level(9, prefs.getInt("level9", 900), 11));
            levels.add(new Level(10, 0, 12));

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
     * Stores the settings into the user preferences.
     */
    public void savePrefs() {
        try {
            Preferences prefs = Preferences
                    .userNodeForPackage(DefaultGameSettings.class);

            prefs.putInt("rows", board.getRows());
            prefs.putInt("cols", board.getCols());
            prefs.putInt("baseFactor", GameSettings.getBaseFactor());

            prefs.putInt("level1", levels.get(0).getScore());
            prefs.putInt("level2", levels.get(1).getScore());
            prefs.putInt("level3", levels.get(2).getScore());
            prefs.putInt("level4", levels.get(3).getScore());
            prefs.putInt("level5", levels.get(4).getScore());
            prefs.putInt("level6", levels.get(5).getScore());
            prefs.putInt("level7", levels.get(6).getScore());
            prefs.putInt("level8", levels.get(7).getScore());
            prefs.putInt("level9", levels.get(8).getScore());

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
        levels.add(new Level(1, 1000, 3));
        levels.add(new Level(2, 2000, 4));
        levels.add(new Level(3, 4000, 5));
        levels.add(new Level(4, 7000, 6));
        levels.add(new Level(5, 10000, 7));
        levels.add(new Level(6, 15000, 8));
        levels.add(new Level(7, 25000, 9));
        levels.add(new Level(8, 50000, 10));
        levels.add(new Level(9, 100000, 11));
        levels.add(new Level(10, 0, 12));

        setBaseFactor(DEFAULT_BASEFACTOR);
        board.setDefaultValues();

        Logging.getLogger().log(java.util.logging.Level.INFO,
                "Applied Default DefaultGameSettings");

        super.setDefaultValues();
    }

}
