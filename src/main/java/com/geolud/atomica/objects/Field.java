package com.geolud.atomica.objects;

import java.io.Serializable;

/**
 * Represents a field on the game board where a token can take place on. It is
 * defined by a row and a column number.
 *
 * @author Georg Ludewig
 */
public class Field implements Serializable {

    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 4394089786093024075L;

    /**
     * The column in which the field is located.
     */
    private int col;

    /**
     * The row in which the field is located.
     */
    private int row;

    /**
     * The token which takes place on the field. Can be null if no token on
     * field.
     */
    private Token token = null;

    /**
     * Creates a field for the given position.
     *
     * @param col the column of the field
     * @param row the row of the field
     */
    public Field(int col, int row) {
        super();
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the column of the field.
     *
     * @return the column of the field
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the row of the field.
     *
     * @return the row of the field
     */
    public int getRow() {
        return row;
    }

    public Token getToken() {
        return token;
    }

    /**
     * Indicates if an atom is located on the field.
     *
     * @return true if an atom is located on the field.
     */
    public boolean isBlocked() {
        Token token = getToken();
        if (token == null || token.getClass() == IndicatorToken.class)
            return false;

        return true;
    }

    /**
     * Removes the token from the field if there is one.
     */
    public void removeToken() {
        if (token != null) {
            token.setField(null);
        }
        token = null;
    }

    /**
     * Places the given token on the field and removes the old one.
     *
     * @param newToken the new token to place
     */
    public void placeToken(Token newToken) {
        if (newToken == null)
            throw new IllegalArgumentException();

        // remove the token from the previous field
        Field previousField = newToken.getField();
        if (previousField != null) {
            previousField.removeToken();
        }

        token = newToken;

        // assure the token knows its field
        token.setField(this);
    }
}
