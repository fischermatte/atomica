package com.geolud.atomica.objects;

/**
 * Represents an atom on a field.
 *
 * @author Georg Ludewig
 */
public class AtomToken extends Token {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = -6870258898263677228L;

    /**
     * Creates an atom with the given color identifier.
     *
     * @param colorIndex the color identifier for the new atom
     */
    public AtomToken(int colorIndex) {
        super(colorIndex);
    }

}
