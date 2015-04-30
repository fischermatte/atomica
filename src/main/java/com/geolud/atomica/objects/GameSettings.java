package com.geolud.atomica.objects;

import com.geolud.atomica.util.logging.Logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Abstract class holding all necessary settings for the initialization of a
 * game situation. This includes the board size, the levels and a base factor
 * for score calculation. Since it can be defined in two modes - by the game
 * settings itself or by the editor there are two subclasses: EditorGameSettings
 * and DefaultGameSettings. The main difference consists in their
 * initialization.
 *
 * @author Georg Ludewig
 */
public abstract class GameSettings extends Observable implements Serializable,
        Cloneable {
    /**
     * The default base factor for calculating the score.
     */
    protected static final int DEFAULT_BASEFACTOR = 50;

    /**
     * The maximum base factor for calculating the score.
     */
    public static final int MAX_BASEFACTOR = 1000;

    /**
     * The minimum base factor for calculating the score.
     */
    public static final int MIN_BASEFACTOR = 1;

    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 1998631280550147737L;

    /**
     * Returns the base factor for score calculation.
     *
     * @return the base factor for score calculation
     */
    public static Integer getBaseFactor() {
        return baseFactor;
    }

    /**
     * Sets the base factor for calculation the score.
     *
     * @param newBaseFactor the new base factor
     */
    public static void setBaseFactor(int newBaseFactor) {
        baseFactor = newBaseFactor;
    }

    /**
     * The board defining the number of rows and columns.
     */
    protected Board board;

    /**
     * The base factor for calculating the score. Declared as static since it is
     * used for the editor and default game settings.
     */
    private static int baseFactor;

    /**
     * The levels a game can be played.
     */
    protected ArrayList<Level> levels;

    /**
     * Default constructor. Sets the base factor and the board size to their
     * default value.
     */
    public GameSettings() {
        levels = new ArrayList<Level>();
        board = new Board();
        baseFactor = DEFAULT_BASEFACTOR;
    }

    /**
     * Takes over the values of the given settings.
     *
     * @param gameSettings the settings to take over
     */
    @SuppressWarnings("unchecked")
    public void applyValues(GameSettings gameSettings) {
        this.board.setCols(gameSettings.getCols());
        this.board.setRows(gameSettings.getRows());
        this.levels = (ArrayList<Level>) gameSettings.getLevels().clone();

        setChanged();
        notifyObservers();
    }

    /**
     * Creates a deep clone of the settings.
     *
     * @return the clone of the settings
     */
    public GameSettings deepClone() {
        GameSettings settingsClone = null;
        try {
            settingsClone = (GameSettings) super.clone();
            settingsClone.applyValues(this);
        } catch (CloneNotSupportedException e) {
            Logging.getLogger().log(java.util.logging.Level.SEVERE,
                    "Could not clone settings.");
        }
        return settingsClone;

    }

    /**
     * Returns the number of columns of the board.
     *
     * @return the number of columns of the board
     */
    public int getCols() {
        return board.getCols();
    }

    /**
     * Returns the last level.
     *
     * @return the last level
     */
    public Level getLastLevel() {
        return levels.get(levels.size() - 1);
    }

    /**
     * Returns the Level for the given index. The index must be greater than 0
     * since it represents the level number where the first starts with 1.
     *
     * @param index the index of the wanted level
     * @return the level at the given index
     */
    public Level getLevel(int index) {
        // since there is no level named by the number "0" throw exception
        if (index <= 0) {
            throw new IllegalArgumentException("Invalid level number " + index);
        }

        return levels.get(index - 1);
    }

    /**
     * Returns all defined levels as ArrayList.
     *
     * @return all levels
     */
    public ArrayList<Level> getLevels() {
        return levels;
    }

    /**
     * Returns the number of rows of the board.
     *
     * @return the number of rows of the board
     */
    public int getRows() {
        return board.getRows();
    }

    /**
     * Sets the number of columns of the board.
     *
     * @param cols the new number of columns.
     */
    public void setCols(int cols) {
        board.setCols(cols);
    }

    /**
     * Sets the default settings. Since DefaultGameSettings and
     * EditorGameSettings have their own default settings this method is to
     * override.
     */
    public void setDefaultValues() {
        setChanged();
        notifyObservers();
    }

    /**
     * Sets the number of rows of the board.
     *
     * @param rows the new number of rows
     */
    public void setRows(int rows) {
        board.setRows(rows);
    }
}
