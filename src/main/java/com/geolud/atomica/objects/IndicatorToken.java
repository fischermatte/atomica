package com.geolud.atomica.objects;

/**
 * Represents an indicator on a field.
 *
 * @author Georg Ludewig
 */
public class IndicatorToken extends Token {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 20354525286921690L;

    /**
     * Creates an indicator with the given color identifier.
     *
     * @param colorIndex the color identifier for the new indicator
     */
    public IndicatorToken(int colorIndex) {
        super(colorIndex);
    }
}
