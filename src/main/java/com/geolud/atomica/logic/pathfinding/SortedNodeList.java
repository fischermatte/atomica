package com.geolud.atomica.logic.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A sorted list with nodes.
 *
 * @author Georg Ludewig
 */
public class SortedNodeList {
    /**
     * The list of nodes
     */
    private ArrayList<Node> list = new ArrayList<Node>();

    /**
     * Adds a node to the list and sorts the list afterwards.
     *
     * @param n the node to add
     */
    public void add(Node n) {
        list.add(n);
        Collections.sort(list);
    }

    /**
     * Removes all nodes form the list.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if a node is in the list.
     *
     * @param n the node to search for
     * @return true if the node is in the list
     */
    public boolean contains(Node n) {
        return list.contains(n);
    }

    /**
     * Retrieve the first node from the list
     *
     * @return the first node from the list
     */
    public Node first() {
        return list.get(0);
    }

    /**
     * Remove a node from the list
     *
     * @param n The node to remove
     */
    public void remove(Node n) {
        list.remove(n);
    }

    /**
     * Get the number of nodes in the list
     *
     * @return the number of nodes in the list
     */
    public int size() {
        return list.size();
    }
}
