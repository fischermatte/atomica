package com.geolud.atomica.logic.pathfinding;

import com.geolud.atomica.objects.Field;

import java.util.ArrayList;

/**
 * A Path represents a list of fields which define a way from a starting field
 * to a destination field.
 *
 * @author Georg Ludewig
 */
public class Path {
    /**
     * The list of fields defining the path.
     */
    private ArrayList<Field> pathFields = null;

    /**
     * Creates an empty path.
     */
    public Path() {
        pathFields = new ArrayList<Field>();
    }

    /**
     * Returns the field at the specific position of the path.
     *
     * @param n the specific position
     * @return the field at the specific position of the path
     */
    public Field get(int n) {
        return pathFields.get(n);
    }

    /**
     * Prepends the given field to the path list.
     *
     * @param field the field to prepend
     */
    public void prependField(Field field) {
        pathFields.add(0, field);

    }

    /**
     * Returns the number of fields defining the current path.
     *
     * @return number of fields
     */
    public int size() {
        return pathFields.size();
    }
}
