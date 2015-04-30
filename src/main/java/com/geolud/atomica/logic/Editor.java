package com.geolud.atomica.logic;

import com.geolud.atomica.objects.*;
import com.geolud.atomica.util.logging.Logging;

import java.util.ArrayList;
import java.util.Observable;

/**
 * The class contains the application logic for creating an initial game
 * situation.
 *
 * @author Georg Ludewig
 */
public class Editor extends Observable {
    /**
     * The different modes of an Editor. A GameSituation can be edited by
     * deleting, moving or adding Tokens. In accordance to that an Editor
     * instance can be in 3 different modes.
     *
     * @author Georg Ludewig
     */
    public enum EditorMode {
        /**
         * Mode for deleting Tokens.
         */
        DELETE,
        /**
         * Mode for adding Tokens.
         */
        ADD,
        /**
         * Mode for moving Tokens.
         */
        MOVE
    }

    /**
     * Holds the state of the editing game
     */
    private GameSituation gameSituation;

    /**
     * Holds the Token which shall be added when Editor is in ADD Mode.
     */
    private Token token2Add;

    /**
     * Holds the Token which shall be moved when Editor is in MOVE Mode.
     */
    private Token token2Move;

    /**
     * Holds the EditorMode.
     */
    private EditorMode mode;

    /**
     * Creates an Editor instance with a GameSituation defined by the given
     * DefaultGameSettings.
     *
     * @param gameSettings the game settings which defines the initial
     *                     GameSituation
     */
    public Editor(GameSettings gameSettings) {
        gameSituation = new GameSituation(gameSettings);
        mode = EditorMode.MOVE;
    }

    /**
     * Changes the boards number of columns. Will delete all Tokens in current
     * GameSituation.
     *
     * @param cols number of columns
     */
    public void changeCols(int cols) {
        if (gameSituation.getCols() != cols) {
            gameSituation.setCols(cols);

            setChanged();
            notifyObservers();
        }
    }

    /**
     * Changes the boards number of rows. Will remove all Tokens in current
     * GameSituation.
     *
     * @param rows number of rows
     */
    public void changeRows(int rows) {
        if (gameSituation.getRows() != rows) {
            gameSituation.setRows(rows);

            setChanged();
            notifyObservers();
        }
    }

    /**
     * Removes all Tokens in current GameSituation.
     */
    public void clearSituation() {
        gameSituation.clear();

        setChanged();
        notifyObservers();
    }

    /**
     * Returns the number of columns in current GameSituation.
     *
     * @return the number of columns
     */
    public int getCols() {
        return gameSituation.getCols();
    }

    /**
     * Returns the Field instance at the given Position.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the field at the given position
     */
    public Field getField(int col, int row) {
        return gameSituation.getField(col, row);
    }

    /**
     * Returns the current mode the Editor is in.
     *
     * @return the current mode of the Editor
     */
    public EditorMode getMode() {
        return mode;
    }

    /**
     * Returns the number of colors of the current Level.
     *
     * @return number of colors of current Level
     */
    public Integer getCurrentNumberOfColors() {
        return gameSituation.getCurrentLevel().getNumberOfColors();
    }

    /**
     * Returns the number of rows in current GameSituation.
     *
     * @return the number of rows
     */
    public int getRows() {
        return gameSituation.getRows();
    }

    /**
     * Returns the current GameSituation.
     *
     * @return the current GameSituation
     */
    public GameSituation getSituation() {
        return gameSituation;
    }

    /**
     * Returns the selected Token which can be added in GameSituation.
     *
     * @return the selected Token which can be added in GameSituation
     */
    public Token getToken2Add() {
        return token2Add;
    }

    /**
     * Returns the selected Token which can be moved in GameSituation.
     *
     * @return the selected Token which can be moved in GameSituation
     */
    public Token getToken2Move() {
        return token2Move;
    }

    /**
     * Places the given Token on the Field (col, row). If the Field is not empty
     * and or a new Molecule could be created, the token will not be placed.
     *
     * @param token the Token to be placed on the Field
     * @param col   the column of the destination Field
     * @param row   the row of the destination Field
     * @return true if Token could be placed
     */
    public boolean placeToken(Token token, int col, int row) {
        if (token == null) {
            return false;
        }

        Field destinationField = gameSituation.getField(col, row);
        if (destinationField == null) {
            return false;
        }

        // the Token can not be placed if the destination field already has one
        if (destinationField.getToken() != null) {
            Logging.getLogger().log(
                    java.util.logging.Level.INFO,
                    "The field (" + row + ", " + col
                            + ") already contains a Token");
            return false;
        }

        // an IndicatorToken can only be placed, if there are less than 3 in
        // current situation. Only Exception: If the Token is an IndicatorToken
        // and about to be moved.
        ArrayList<IndicatorToken> indicators = gameSituation.getIndicators();
        if (token.getClass() == IndicatorToken.class && indicators.size() >= GameSituation.INDICATORS_IN_ROUND
                && !indicators.contains(token)) {
            Logging
                    .getLogger()
                    .log(java.util.logging.Level.INFO,
                            "Indicator could not be placed, because situation has already 3");
            return false;
        }

        // an AtomToken can only be placed, if it does not lead to a Molecules
        if (token.getClass() == AtomToken.class) {
            Field oldField = token.getField();
            destinationField.placeToken(token);
            MoleculeDetector moleculeDetector = new MoleculeDetector(
                    gameSituation);
            ArrayList<Molecule> m = moleculeDetector.detectMolecules();
            if (m != null && m.size() > 0) {
                destinationField.removeToken();
                if (oldField != null) {
                    oldField.placeToken(token);
                }
                Logging
                        .getLogger()
                        .log(java.util.logging.Level.INFO,
                                "Atom could not be placed, because a Molecule was detected.");
                return false;
            }
        }

        destinationField.placeToken(token);

        setChanged();
        notifyObservers();

        return true;

    }

    /**
     * Checks if there is a Token on the given Position and returns it.
     *
     * @param col the column of the Field
     * @param row the row of the Field
     * @return the Token at the given Position
     */
    public Token queryToken(int col, int row) {
        return gameSituation.queryToken(col, row);
    }

    /**
     * Removes all Tokens with colors which are not included in current Level.
     */
    private void removeInvalidToken() {
        ArrayList<Token> tokens = gameSituation.getTokens();

        for (Token token : tokens) {
            if (token.getColorIndex() >= getCurrentNumberOfColors()) {
                Field field = token.getField();
                field.removeToken();
            }
        }

    }

    /**
     * Removes a Token at the given Position.
     *
     * @param col the column of the Field
     * @param row the row of the Field
     */
    public void removeToken(int col, int row) {
        Field destinationField = gameSituation.getField(col, row);
        if (destinationField == null) {
            return;
        }

        destinationField.removeToken();

        setChanged();
        notifyObservers();
    }

    /**
     * Sets the Editor mode.
     *
     * @param mode the new mode.
     */
    public void setMode(EditorMode mode) {
        this.mode = mode;

        if (mode == EditorMode.DELETE) {
            setToken2Add(null);
            setToken2Move(null);
        } else if (mode == EditorMode.MOVE) {
            setToken2Add(null);
            setToken2Move(null);
        } else if (mode == EditorMode.ADD) {
            setToken2Move(null);
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Sets the number of colors and removes invalid Tokens from current
     * GameSituation.
     *
     * @param numberOfColors the new number of colors
     */
    public void setNumberOfColors(int numberOfColors) {
        Level level = gameSituation.getCurrentLevel();
        if (level.getNumberOfColors() != numberOfColors) {
            level.setNumberOfColors(numberOfColors);
            removeInvalidToken();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Replaces current GameSituation.
     *
     * @param gameSituation the new GameSituation
     */
    public void setSituation(GameSituation gameSituation) {
        if (gameSituation != null) {
            this.gameSituation = gameSituation;

            setChanged();
            notifyObservers();
        }
    }

    /**
     * Sets the Token which can be added in GameSituation.
     *
     * @param token the new Token to be added
     */
    public void setToken2Add(Token token) {
        token2Add = token;
    }

    /**
     * Sets the Token which can be moved in GameSituation.
     *
     * @param token the new Token to be moved
     */
    public void setToken2Move(Token token) {
        token2Move = token;
    }

}
