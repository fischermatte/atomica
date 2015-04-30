package com.geolud.atomica.ui.util.board;

import com.geolud.atomica.logic.Game;
import com.geolud.atomica.logic.pathfinding.Path;
import com.geolud.atomica.objects.AtomToken;
import com.geolud.atomica.objects.Field;

import java.awt.*;

/**
 * Displays the game board when playing a game. It allows user interaction by
 * moving atoms to an empty field and will also display the shortest path.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class GameBoardPanel extends BoardPanel {
    /**
     * The game object as reference to the model holding the game situation.
     */
    private Game game = null;

    /**
     * The token which is selected and about being moved.
     */
    protected AtomToken token2Move = null;

    // /////////// VIEW SECTION /////////////

    /**
     * Creates a panel with the given game as model.
     *
     * @param game a game object
     */
    public GameBoardPanel(Game game) {
        super(game.getSituation());
        this.game = game;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geolud.atomica.ui.util.board.BoardPanel#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        paintShortestPath(g2);
    }

    /**
     * Paints field highlights if a token on the board is selected to be moved.
     * A highlight will be painted on the field of the selected token as well as
     * on the field of the current mouse position.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#paintFieldHighLight(com.geolud.atomica.objects.Field,
     * java.awt.Graphics2D)
     */
    @Override
    protected void paintFieldHighLight(Field field, Graphics2D g2) {
        super.paintFieldHighLight(field, g2);

        Rectangle fieldBounds = getBounds(field);

        if (field.getToken() == token2Move && field.getToken() != null) {
            g2.setPaint(new Color(0, 255, 255, 100));
            g2
                    .fillRect((int) fieldBounds.getX(), (int) fieldBounds
                                    .getY(), (int) fieldBounds.getWidth(),
                            (int) fieldBounds.getHeight());
        } else if (cursorPositionField == field) {
            g2.setPaint(new Color(0, 255, 0, 50));
            g2
                    .fillRect((int) fieldBounds.getX(), (int) fieldBounds
                                    .getY(), (int) fieldBounds.getWidth(),
                            (int) fieldBounds.getHeight());
        }
    }

    /**
     * Paints the shortest path between the selected atom and the destination
     * field.
     *
     * @param g2 the Graphic2D object
     */
    void paintShortestPath(Graphics2D g2) {
        if (token2Move == null)
            return;

        Field activeField = token2Move.getField();
        if (activeField == null || cursorPositionField == null
                || cursorPositionField == activeField)
            return;

        Path path = game.findShortestPath(activeField, cursorPositionField);
        if (path == null || path.size() < 2) {
            return;
        }

        g2.setColor(Color.BLACK);
        float dash[] =
                {10.0f};
        BasicStroke stroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        g2.setStroke(stroke);

        for (int i = 0; i < path.size() - 1; i++) {
            Field field1 = path.get(i);
            Field field2 = path.get(i + 1);

            Rectangle bounds1 = getBounds(field1);
            Rectangle bounds2 = getBounds(field2);

            int x1 = (int) bounds1.getCenterX();
            int y1 = (int) bounds1.getCenterY();
            int x2 = (int) bounds2.getCenterX();
            int y2 = (int) bounds2.getCenterY();

            g2.drawLine(x1, y1, x2, y2);
        }
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Sets the current cursorPostionField so that this field is highlighted
     * after a repaint. It also indicates the destination field when painting
     * the shortest path. If the game is over this method wont set that field so
     * it wont be highlighted.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#handleMouseMoved(int, int)
     */
    @Override
    protected void handleMouseMoved(int x, int y) {
        if (game.getIsGameOver())
            return;

        cursorPositionField = game.getField(getColFromPixel(x),
                getRowFromPixel(y));

        repaint();
    }

    /**
     * Releasing the mouse indicates that either a new atom was selected to
     * being moved or an already selected atom shall be placed on a destination
     * field. According to that this method sets the new token or calls the
     * model (Game) about moving the token.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#handleMouseReleased(int, int)
     */
    @Override
    protected void handleMouseReleased(int x, int y) {
        if (game.getIsGameOver())
            return;

        int col = getColFromPixel(x);
        int row = getRowFromPixel(y);

        AtomToken atom = game.queryAtomToken(col, row);

        // if an atom on position (x,y) exists, it becomes active
        if (atom != null) {
            token2Move = atom;
        } else if (token2Move != null) {
            // no atom on position (x,y)
            Field field = game.getField(col, row);
            if (field != null) {
                game.moveAtomToken(token2Move, field);
            }

            token2Move = null;
        }

    }

}
