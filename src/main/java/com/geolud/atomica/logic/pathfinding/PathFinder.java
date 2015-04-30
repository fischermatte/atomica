package com.geolud.atomica.logic.pathfinding;

import com.geolud.atomica.objects.Field;
import com.geolud.atomica.objects.GameSituation;

import java.util.ArrayList;

/**
 * A path finder implementation that uses the AStar algorithm to determine a
 * path.
 *
 * @author Georg Ludewig
 */
public class PathFinder {
    /**
     * The maximum depth of search.
     */
    private static final int MAX_SEARCH_DISTANCE = 500;

    /**
     * The Nodes to search through.
     */
    private Node[][] nodes = null;

    /**
     * The list of nodes that have been searched through.
     */
    private ArrayList<Node> closed = null;

    /**
     * The list of nodes not yet searched.
     */
    private SortedNodeList open = null;

    /**
     * Holds the current GameSituation.
     */
    private GameSituation gameSituation = null;

    /**
     * Creates a PathFinder.
     */
    public PathFinder() {
        open = new SortedNodeList();
        closed = new ArrayList<Node>();
    }

    /**
     * Finds the shortest path from the starting field (fromField) to the
     * destination field (toField).
     *
     * @param gameSituation the GameSituation where to search
     * @param fromField     the starting field
     * @param toField       the destination field
     * @return the shortest Path or null if there is non
     */
    public Path findShortestPath(GameSituation gameSituation, Field fromField,
                                 Field toField) {
        // first check if the destination field is already blocked or the same
        // as the starting field
        if (toField.isBlocked() || fromField == toField) {
            return null;
        }

        // initialize all nodes in accordance to the fields of the given
        // GameSituation
        initNodes(gameSituation);

        int fromRow = fromField.getRow();
        int fromCol = fromField.getCol();
        int toRow = toField.getRow();
        int toCol = toField.getCol();

        Node fromNode = nodes[fromCol][fromRow];
        Node toNode = nodes[toCol][toRow];

        // Initial state: The closed list is empty. Only the starting
        // node is in the open list and it's cost is zero
        fromNode.setCost(0);
        fromNode.setDepth(0);
        closed.clear();
        open.clear();
        open.add(fromNode);
        toNode.setParent(null);

        int maxDepth = 0;
        // while the destination field is not reached and the max search depth
        // is not exceeded
        while ((maxDepth < MAX_SEARCH_DISTANCE) && (open.size() != 0)) {
            // get the first node in open list
            Node current = open.first();
            if (current == toNode) {
                break;
            }

            open.remove(current);
            closed.add(current);

            // search through all neighbours of the current node evaluating
            // them as next nodes
            for (int row = -1; row < 2; row++) {
                for (int col = -1; col < 2; col++) {
                    // current node
                    if ((row == 0) && (col == 0)) {
                        continue;
                    }

                    // no diagonal movement
                    if ((row != 0) && (col != 0)) {
                        continue;
                    }

                    int r = row + current.getRow();
                    int c = col + current.getCol();

                    // check if the destination field is a valid field to move
                    // to
                    if (isValidMove(fromCol, fromRow, c, r)) {
                        // the cost to get to this node is the cost of the
                        // current node plus the movement cost (1) to reach this
                        // node.
                        Node neighbour = nodes[c][r];
                        float nextStepCost = current.getCost() + 1;

                        // if the new cost for this node is lower than the
                        // previous makes sure the node hasn't been
                        // discarded. There might have been a better path
                        // to get to this node so it needs to be re-evaluated
                        if (nextStepCost < neighbour.getCost()) {
                            if (open.contains(neighbour)) {
                                open.remove(neighbour);
                            }
                            if (closed.contains(neighbour)) {
                                closed.remove(neighbour);
                            }
                        }

                        // if the node hasn't already been processed and
                        // discarded then reset it's cost to current cost and
                        // add it as a next possible step (i.e. to the open
                        // list)
                        if (!open.contains(neighbour)
                                && !(closed.contains(neighbour))) {
                            neighbour.setCost(nextStepCost);
                            neighbour.setHeuristic(getHeuristicCost(neighbour,
                                    toNode));
                            maxDepth = Math.max(maxDepth, neighbour
                                    .setParent(current));
                            open.add(neighbour);
                        }
                    }
                }
            }
        }

        // if there is no path, the parent node of the destination node is null
        if (toNode.getParent() == null) {
            return null;
        }

        // A valid Path was found. Create the path by adding its fields
        Path path = new Path();
        while (toNode != fromNode) {
            path.prependField(toNode.getField());
            toNode = toNode.getParent();
        }
        path.prependField(fromNode.getField());

        return path;
    }

    /**
     * Calculates the heuristic costs between the the given nodes.
     *
     * @param from a starting node
     * @param to   a destination node
     * @return the heuristic costs
     */
    private float getHeuristicCost(Node from, Node to) {
        float dx = to.getCol() - from.getCol();
        float dy = to.getRow() - from.getRow();

        float result = (float) (Math.sqrt((dx * dx) + (dy * dy)));

        return result;
    }

    /**
     * Initialize all nodes in accordance to the fields of the given
     * GameSituation.
     *
     * @param gameSituation the initial GameSituation
     */
    private void initNodes(GameSituation gameSituation) {
        this.gameSituation = gameSituation;
        this.nodes = new Node[gameSituation.getCols()][gameSituation.getRows()];

        for (int row = 0; row < gameSituation.getRows(); row++) {
            for (int col = 0; col < gameSituation.getCols(); col++) {
                nodes[col][row] = new Node(gameSituation.getField(col, row));
            }
        }
    }

    /**
     * Checks if the destination Field is a valid Field to move to.
     *
     * @param fromCol the column of the starting field
     * @param fromRow the row of the starting field
     * @param toCol   the column of the destination field
     * @param toRow   the row of the destination field
     * @return true if the destination field is valid
     */
    private boolean isValidMove(int fromCol, int fromRow, int toCol, int toRow) {
        Field toField = gameSituation.getField(toCol, toRow);
        if (toField == null)
            return false;

        boolean invalid = false;
        if ((fromRow != toRow) || (fromCol != toCol)) {
            invalid = toField.isBlocked();
        }

        return !invalid;
    }

}
