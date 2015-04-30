package com.geolud.atomica.objects;

import java.io.Serializable;

/**
 * Abstract class representing a token on a field. A token object has a certain
 * color and can be created either as an AtomToken or IndicatorToken. If a token
 * is placed on a field it holds a reference to it.
 *
 * @author Georg Ludewig
 */
public abstract class Token implements Serializable {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = -8586453434274809648L;

    /**
     * The color index of the token.
     */
    private int colorIndex = -1;

    /**
     * The field on which the token is currently placed on.
     */
    private Field field = null;

    /**
     * Default constructor for creating a token with the given color index.
     *
     * @param colorIndex the color identifier of the token
     */
    public Token(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    /**
     * Returns the color identifier of the token.
     *
     * @return the color identifier
     */
    public int getColorIndex() {
        return colorIndex;
    }

    /**
     * Creates a copy of the token without taking over the fields reference.
     *
     * @return a copy of the token
     */
    public Token getCopy() {
        Token newToken = null;
        // the token is an atom
        if (this.getClass() == AtomToken.class) {
            newToken = new AtomToken(colorIndex);
        }
        // the token is an indicator
        else if (this.getClass() == IndicatorToken.class) {
            newToken = new IndicatorToken(colorIndex);
        }

        return newToken;
    }

    /**
     * Returns the field on which the token is currently placed on.
     *
     * @return the field on which the token is currently placed on
     */
    public Field getField() {
        return field;
    }

    /**
     * Sets the color identifier of the token.
     *
     * @param colorIndex the new color identifier
     */
    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    /**
     * Sets the field on which the token shall be placed on.
     *
     * @param field the field on which the token shall be placed on.
     */
    public void setField(Field field) {
        this.field = field;
    }
}
