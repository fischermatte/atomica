package com.geolud.atomica.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds information about a certain game situation, like the
 * arrangement of tokens on the board. It is used for displaying the situation
 * on the UI as well as for calculations.
 *
 * @author Georg Ludewig
 */
public class GameSituation implements Serializable {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = -1934734150265472250L;

    /**
     * The number of indicators appearing every round.
     */
    public static final int INDICATORS_IN_ROUND = 5;

    /**
     * The game settings of the game situation keeping the board size, the base
     * factor and the levels to go through.
     */
    private GameSettings gameSettings;

    /**
     * Holds the score during a game.
     */
    private int score;

    /**
     * Holds the current level during a game.
     */
    private Level currentLevel;

    /**
     * All fields of the game.
     */
    private Field[][] fields;

    /**
     * Creates an game situation considering the given settings. Initializes all
     * fields where the board size is defined in the given settings.
     *
     * @param gameSettings the settings to initialize the game with
     */
    public GameSituation(GameSettings gameSettings) {
        this.currentLevel = gameSettings.getLevel(1);
        this.score = 0;
        this.gameSettings = gameSettings;

        initFields();
    }

    /**
     * Adds score to the current score of the game.
     *
     * @param newScore the score to add
     */
    public void addScore(int newScore) {
        this.score += newScore;
    }

    /**
     * Removes all token form the fields.
     */
    public void clear() {
        initFields();
    }

    /**
     * Returns all tokens which are in the game situation.
     *
     * @return all tokens which are in the game situation
     */
    public ArrayList<AtomToken> getAtoms() {
        ArrayList<AtomToken> atoms = new ArrayList<AtomToken>();

        int rows = gameSettings.getRows();
        int cols = gameSettings.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                AtomToken atom = queryAtom(c, r);
                if (atom != null)
                    atoms.add(atom);
            }
        }

        return atoms;
    }

    /**
     * Returns the base factor.
     *
     * @return the base factor
     */
    public static int getBaseFactor() {
        return GameSettings.getBaseFactor();
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return gameSettings.getCols();
    }

    /**
     * Returns the current level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns all empty fields in the situation.
     *
     * @param allowIndicators indicates if fields with indicators are considered as empty
     * @return all empty fields
     */
    public ArrayList<Field> getEmptyFields(boolean allowIndicators) {
        ArrayList<Field> emptyFields = new ArrayList<Field>();

        int rows = gameSettings.getRows();
        int cols = gameSettings.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Field field = fields[c][r];
                Token token = field.getToken();
                if (token == null) {
                    emptyFields.add(field);
                } else if (allowIndicators) {
                    if (token.getClass() == IndicatorToken.class) {
                        emptyFields.add(field);
                    }
                }
            }
        }

        return emptyFields;
    }

    /**
     * Returns the field at the given position.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the field at the given position
     */
    public Field getField(int col, int row) {
        Field field = null;
        try {
            field = fields[col][row];
        } catch (ArrayIndexOutOfBoundsException e) {
            field = null;
            // Logging.getLogger().log(java.util.logging.Level.SEVERE,
            // "Invalid Field ("+ row + ", " + col + ")");
        }

        return field;
    }

    /**
     * Returns all indicators which take place in the situation.
     *
     * @return all indicators
     */
    public ArrayList<IndicatorToken> getIndicators() {
        ArrayList<IndicatorToken> indicators = new ArrayList<IndicatorToken>();

        int cols = gameSettings.getCols();
        int rows = gameSettings.getRows();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                IndicatorToken atom = queryIndicatorToken(c, r);
                if (atom != null)
                    indicators.add(atom);
            }
        }

        return indicators;
    }

    /**
     * Returns the last level defined by the game settings.
     *
     * @return the last level
     */
    public Level getLastLevel() {
        return gameSettings.getLastLevel();
    }

    /**
     * Returns the level defined by the game settings at the given index.
     *
     * @param i the level number
     * @return the level at the given index
     */
    public Level getLevel(int i) {
        return gameSettings.getLevel(i);
    }

    /**
     * Returns the number of rows.
     *
     * @return the number of rows
     */
    public int getRows() {
        return gameSettings.getRows();
    }

    /**
     * Returns the reached score of the situation.
     *
     * @return the currently reached score
     */
    public int getCurrentScore() {
        return score;
    }

    /**
     * Returns all tokens which take place in the situation.
     *
     * @return all tokens of the situation
     */
    public ArrayList<Token> getTokens() {
        ArrayList<Token> tokens = new ArrayList<Token>();

        int rows = gameSettings.getRows();
        int cols = gameSettings.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Token token = queryToken(c, r);
                if (token != null)
                    tokens.add(token);
            }
        }

        return tokens;
    }

    /**
     * Initializes all fields considering the number of rows and columns given
     * by the game settings.
     */
    private void initFields() {
        int cols = gameSettings.getCols();
        int rows = gameSettings.getRows();

        fields = new Field[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Field newField = new Field(c, r);
                fields[c][r] = newField;
            }
        }
    }

    /**
     * Returns the atom at the given position if there is one.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the atom at the given position, null if there is non
     */
    public AtomToken queryAtom(int col, int row) {
        Token token = queryToken(col, row);

        if (token == null || token.getClass() != AtomToken.class)
            return null;

        return (AtomToken) token;
    }

    /**
     * Returns the atom with the given color if there is one at the given
     * position.
     *
     * @param col        the column of the field
     * @param row        the row of the field
     * @param colorIndex the color the atom shall have
     * @return the atom at the given position, null if there is non
     */
    public AtomToken queryAtom(int col, int row, int colorIndex) {
        AtomToken atom = queryAtom(col, row);
        if (atom == null || atom.getColorIndex() != colorIndex)
            return null;

        return atom;
    }

    /**
     * Returns the indicator at the given position if there is one.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the indicator at the given position, null if there is non
     */
    private IndicatorToken queryIndicatorToken(int col, int row) {
        Token token = queryToken(col, row);

        if (token == null || token.getClass() != IndicatorToken.class)
            return null;

        return (IndicatorToken) token;
    }

    /**
     * Returns the token at the given position if there is one.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the token at the given position, null if there is non
     */
    public Token queryToken(int col, int row) {
        Token token = null;
        try {
            token = fields[col][row].getToken();
        } catch (ArrayIndexOutOfBoundsException e) {
            token = null;
            // Logging.getLogger().log(java.util.logging.Level.SEVERE,
            // "Invalid Field ("+ row + ", " + col + ")");
        }

        return token;
    }

    /**
     * Sets a new number of columns and reinitializes the fields. Removes also
     * all tokens.
     *
     * @param cols the new number of columns
     */
    public void setCols(int cols) {
        gameSettings.setCols(cols);
        initFields();

    }

    /**
     * Sets the current score of the game.
     *
     * @param score the new score to be set
     */
    public void setCurrentScore(int score) {
        this.score = score;
    }

    /**
     * Sets the current level of the game.
     *
     * @param level the new level to be set
     */
    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    /**
     * Sets a new number of rows and reinitializes the fields. Removes also all
     * tokens.
     *
     * @param rows the new number of rows
     */
    public void setRows(int rows) {
        gameSettings.setRows(rows);
        initFields();
    }
}
