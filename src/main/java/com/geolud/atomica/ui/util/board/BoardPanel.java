package com.geolud.atomica.ui.util.board;

import com.geolud.atomica.objects.Field;
import com.geolud.atomica.objects.GameSituation;
import com.geolud.atomica.objects.Token;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.components.AtomicaPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

/**
 * Abstract base class for the user interface of a game board. Since it is used
 * as well in a game as in the editor there are two corresponding subclasses of
 * it. Some methods are provided for getting overwritten by them.
 * <p/>
 * <br>
 * <p>
 * Considering the current game situation with the arrangement of indicator and
 * atom tokens on the fields, it paints them proportional to the current panel
 * size. It provides an easy access to the screen bounds of certain field
 * object.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public abstract class BoardPanel extends AtomicaPanel {
    /**
     * Factor for scaling the board.
     */
    static private final double SCALE = 0.96;

    /**
     * The game situation containing the board size and the placement of the
     * tokens.
     */
    protected GameSituation gameSituation = null;

    /**
     * A map providing easy access to the screen bounds of certain field.
     */
    private HashMap<Field, Rectangle> fieldsScreenBounds = null;

    /**
     * The size of the painting game board in screen coordinates.
     */
    private Rectangle board = new Rectangle(0, 0, 0, 0);

    /**
     * A field holding the current position of the mouse cursor.
     */
    protected Field cursorPositionField;

    // /////////// VIEW SECTION /////////////

    /**
     * Initializes the board with the given game situation.
     *
     * @param gameSituation a game situation
     */
    public BoardPanel(GameSituation gameSituation) {
        super();
        this.gameSituation = gameSituation;
        this.fieldsScreenBounds = new HashMap<Field, Rectangle>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e.getX(), e.getY());
            }

        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                handleMouseExited();
            }

        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e.getX(), e.getY());
            }
        });
    }

    /**
     * Returns the screen bounds of the given field.
     *
     * @param field the field to get the screen bounds from
     * @return the screen bounds of the given field
     */
    final protected Rectangle getBounds(Field field) {
        return fieldsScreenBounds.get(field);
    }

    /**
     * Returns the field column on the board for the pixel x position.
     *
     * @param x the pixels x position
     * @return the field column on the board for the pixel x position
     */
    protected int getColFromPixel(int x) {
        int col = -1;

        try {
            int fieldWidth = (int) (board.getWidth() / gameSituation.getCols());
            col = (int) ((x - board.getX()) / fieldWidth);
        } catch (ArithmeticException e) {
            col = -1;
        }

        return col;
    }

    /**
     * Returns the field row on the board for the pixel y position.
     *
     * @param y the pixels y position
     * @return the field row on the board for the pixel y position
     */
    protected int getRowFromPixel(int y) {
        int row = -1;
        try {
            int fieldHeight = (int) (board.getHeight() / gameSituation
                    .getRows());
            row = (int) ((y - board.getY()) / fieldHeight);
        } catch (ArithmeticException e) {
            row = -1;
        }

        return row;
    }

    /**
     * Paints all fields and tokens considering the current size of the panel.
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cols = gameSituation.getCols();
        int rows = gameSituation.getRows();

        int height = (int) ((this.getHeight() / rows) * SCALE);
        int width = (int) ((this.getWidth() / cols) * SCALE);

        board.setBounds(10, 5, width * cols, height * rows);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = (int) ((col * width) + board.getX());
                int y = (int) ((row * height) + board.getY());

                Field field = gameSituation.getField(col, row);

                paintField(field, x, y, width, height, g2);
                paintFieldHighLight(field, g2);

                Token token = field.getToken();
                if (token != null) {
                    paintToken(field, token, g2);
                }

                paintCursor(field, g2);
            }
        }
    }

    /**
     * This method is called by paintComponent to allow a subclass painting a
     * cursor at the given field.
     *
     * @param field the field where to paint a cursor image
     * @param g2    the Graphics2D object
     */
    protected void paintCursor(Field field, Graphics2D g2) {

    }

    /**
     * Paints a field at the given position (x,y,width,height). The image of the
     * field is received by using the ImageLoader class.
     *
     * @param field  the field which is about to be displayed
     * @param x      the x position of the field on screen
     * @param y      the y position of the field on screen
     * @param width  the weight of the field on screen
     * @param height the height of the field on screen
     * @param g2     the Graphic2D object to paint to
     */
    private void paintField(Field field, int x, int y, int width, int height,
                            Graphics2D g2) {
        Rectangle rect = fieldsScreenBounds.get(field);
        if (rect == null) {
            rect = new Rectangle();
            fieldsScreenBounds.put(field, rect);
        }
        rect.setBounds(x, y, width, height);

        Image img = ImageLoader.getInstance().getFieldImage();
        if (img != null) {
            g2.drawImage(img, x, y, width, height, null);
        }
    }

    /**
     * This method is called by paintComponent to allow a subclass painting a
     * field highlight on the given field.
     *
     * @param field the field where to paint a highlight
     * @param g2    the Graphic2D object to paint to
     */
    protected void paintFieldHighLight(Field field, Graphics2D g2) {

    }

    /**
     * Paints the image of the given token at the screen position of the given
     * field.
     *
     * @param field the field where to paint the token
     * @param token the token to display
     * @param g2    the Graphic2D object to paint to
     */
    protected void paintToken(Field field, Token token, Graphics2D g2) {
        if (field == null || token == null)
            return;

        int x = (int) (getBounds(field).getX() + (int) ((getBounds(field)
                .getWidth() * (1 - 0.65)) / 2));
        int y = (int) (getBounds(field).getY() + (int) ((getBounds(field)
                .getHeight() * (1 - 0.65)) / 2));
        int width = (int) (getBounds(field).getWidth() * 0.65);
        int height = (int) (getBounds(field).getHeight() * 0.65);

        Image img = ImageLoader.getInstance().getTokenImage(token);
        if (img != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, x, y, width, height, null);
        }
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * This method is invoked when the mouse is dragged. Introduced to get used
     * in subclass.
     *
     * @param x the horizontal position of the mouse cursor
     * @param y the vertical position of the mouse cursor
     */
    protected void handleMouseDragged(int x, int y) {

    }

    /**
     * Releases the current cursorPostionField so that this field is not
     * highlighted after a repaint.
     */
    private void handleMouseExited() {
        cursorPositionField = null;
        repaint();
    }

    /**
     * This method is invoked when the mouse is moved. Introduced to get used in
     * subclass.
     *
     * @param x the horizontal position of the mouse cursor
     * @param y the vertical position of the mouse cursor
     */
    protected void handleMouseMoved(int x, int y) {

    }

    /**
     * This method is invoked when the mouse is released. Introduced to get used
     * in subclass.
     *
     * @param x the horizontal position of the mouse cursor
     * @param y the vertical position of the mouse cursor
     */
    protected void handleMouseReleased(int x, int y) {

    }
}