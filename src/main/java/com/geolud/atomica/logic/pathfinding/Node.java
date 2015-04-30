package com.geolud.atomica.logic.pathfinding;

import com.geolud.atomica.objects.Field;

/**
 * Represents a node in the search graph. Holds a reference of a related Field
 * in the GameSituation.
 *
 * @author Georg Ludewig
 */
public class Node implements Comparable<Node> {
    /**
     * Reference to a related Field in the GameSituation
     */
    private Field field = null;

    /**
     * The parent search Node of this Node
     */
    private Node parent = null;

    /**
     * The search depth of this Node
     */
    private int depth = 0;

    /**
     * The path cost for this Node
     */
    private float cost = 0;

    /**
     * The heuristic cost of this Node
     */
    private float heuristic = 0;

    /**
     * Creates a new Node.
     *
     * @param field the related Field of this Node
     */
    public Node(Field field) {
        this.field = field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node o) {
        float f = heuristic + cost;
        float of = o.heuristic + o.cost;

        if (f < of) {
            return -1;
        } else if (f > of) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the column of the related Field.
     *
     * @return the column of the related Field
     */
    public int getCol() {
        return field.getCol();
    }

    /**
     * Returns the path costs of this Node.
     *
     * @return the path costs of this Node
     */
    public float getCost() {
        return cost;
    }

    /**
     * Returns the related Field in current GameSituation.
     *
     * @return the related Field in current GameSituation
     */
    public Field getField() {
        return field;
    }

    /**
     * Returns the parent Node of this Node.
     *
     * @return the parent Node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the row of the related Field.
     *
     * @return the row of the related Field
     */
    public int getRow() {
        return field.getRow();
    }

    /**
     * Sets the path costs of this Node.
     *
     * @param cost
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Sets the search depth of this Node.
     *
     * @param depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Sets the heuristic costs of this Node.
     *
     * @param heuristic
     */
    public void setHeuristic(float heuristic) {
        this.heuristic = heuristic;

    }

    /**
     * Sets the parent of this Node and increases the depth.
     *
     * @param parent the parent Node
     * @return the resulting depth of this Node
     */
    public int setParent(Node parent) {
        if (parent != null) {
            depth = parent.depth + 1;
        }

        this.parent = parent;

        return depth;
    }

}