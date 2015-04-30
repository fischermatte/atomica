package com.geolud.atomica.ui.util.board;

import com.geolud.atomica.logic.Editor;
import com.geolud.atomica.logic.Editor.EditorMode;
import com.geolud.atomica.objects.Field;
import com.geolud.atomica.objects.Token;
import com.geolud.atomica.ui.util.ImageLoader;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays an editable game board which also allows user interaction by adding
 * new tokens or moving/deleting existing ones.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class EditorBoardPanel extends BoardPanel implements Observer {
    /**
     * The editor object as reference to the model holding the game situation.
     */
    private Editor editor = null;

    /**
     * Creates a panel with the given editor as model.
     *
     * @param editor an editor object
     */
    public EditorBoardPanel(Editor editor) {
        super(editor.getSituation());
        this.editor = editor;
        this.editor.addObserver(this);
    }

    /**
     * Depending on the current editor mode (ADD, MOVE, DELETE) a corresponding
     * mouse cursor is painted. While the editor is in MOVE or ADD mode, the
     * token which is about being moved or added will be painted. When the
     * editor is in DELETE mode a delete image will be shown on cursor position.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#paintCursor(com.geolud.atomica.objects.Field,
     * java.awt.Graphics2D)
     */
    @Override
    protected void paintCursor(Field field, Graphics2D g2) {
        super.paintCursor(field, g2);

        if (cursorPositionField != field) {
            return;
        }

        if (editor.getMode() == EditorMode.DELETE) {
            paintDeleteCursor(field, g2);
        } else if (editor.getMode() == EditorMode.ADD) {
            if (field.getToken() == null) {
                Token token2Add = editor.getToken2Add();
                paintToken(field, token2Add, g2);
            }
        } else if (editor.getMode() == EditorMode.MOVE) {
            if (field.getToken() == null) {
                Token token2Move = editor.getToken2Move();
                paintToken(field, token2Move, g2);
            }
        }
    }

    /**
     * Paints a delete cursor at the given field position.
     *
     * @param field the field where to paint the delete cursor
     * @param g2    the Graphic2D object
     */
    private void paintDeleteCursor(Field field, Graphics2D g2) {
        if (field == null) {
            return;
        }

        Rectangle fieldBounds = getBounds(field);

        int x = (int) (fieldBounds.getX() + (int) ((fieldBounds.getWidth() * (1 - 0.65)) / 2));
        int y = (int) (fieldBounds.getY() + (int) ((fieldBounds.getHeight() * (1 - 0.65)) / 2));
        int width = (int) (fieldBounds.getWidth() * 0.65);
        int height = (int) (fieldBounds.getHeight() * 0.65);

        Image img = ImageLoader.getInstance().getDeleteCursorImage();
        if (img != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, x, y, width, height, null);
        }
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

        // 1. paint a highlight on the field of the selected token
        if (field.getToken() == editor.getToken2Move()
                && field.getToken() != null) {
            g2.setPaint(new Color(0, 255, 255, 100));
            g2
                    .fillRect((int) fieldBounds.getX(), (int) fieldBounds
                                    .getY(), (int) fieldBounds.getWidth(),
                            (int) fieldBounds.getHeight());
        }
        // 2. paint a highlight on the field of the current mouse position
        else if (cursorPositionField == field) {
            g2.setPaint(new Color(0, 255, 0, 50));
            g2
                    .fillRect((int) fieldBounds.getX(), (int) fieldBounds
                                    .getY(), (int) fieldBounds.getWidth(),
                            (int) fieldBounds.getHeight());
        }
    }

    /**
     * Due to observer pattern this method receives notification from the model
     * (Editor). It takes the current game situation from the editor and
     * repaints it.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        this.gameSituation = editor.getSituation();
        repaint();
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Due to supporting also the adding/deleting of tokens while dragging, this
     * method simply forwards to handleMouseMoved and handleMouseReleased.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#handleMouseDragged(int, int)
     */
    @Override
    protected void handleMouseDragged(int x, int y) {
        super.handleMouseDragged(x, y);

        handleMouseMoved(x, y);

        if (editor.getMode() != EditorMode.MOVE) {
            handleMouseReleased(x, y);
        }
    }

    /**
     * Sets the current cursorPostionField so that this field is highlighted
     * after a repaint.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#handleMouseMoved(int, int)
     */
    @Override
    protected void handleMouseMoved(int x, int y) {
        cursorPositionField = editor.getField(getColFromPixel(x),
                getRowFromPixel(y));
        repaint();
    }

    /**
     * Releasing the mouse indicates that a token is either being about to be
     * deleted, moved or added. This information is here delegated to the model
     * (Editor). Due to the observer pattern, the editor will notify about
     * changes.
     *
     * @see com.geolud.atomica.ui.util.board.BoardPanel#handleMouseReleased(int, int)
     */
    @Override
    protected void handleMouseReleased(int x, int y) {
        int col = getColFromPixel(x);
        int row = getRowFromPixel(y);

        Field field = editor.getField(col, row);
        if (field == null)
            return;

        if (editor.getMode() == EditorMode.DELETE) {
            // remove current token at given field
            editor.removeToken(col, row);
        } else if (editor.getMode() == EditorMode.MOVE) {
            Token token2Move = editor.getToken2Move();
            if (token2Move != null && token2Move != field.getToken()) {
                if (editor.placeToken(token2Move, col, row)) {
                    editor.setToken2Move(null);
                }
            } else if (token2Move != null && token2Move == field.getToken()) {
                editor.setToken2Move(null);
            } else {
                Token token = editor.queryToken(col, row);
                if (token != null) {
                    editor.setToken2Move(token);
                }
            }
        } else if (editor.getMode() == EditorMode.ADD) {
            Token token2Add = editor.getToken2Add();
            if (token2Add != null) {
                editor.placeToken(token2Add.getCopy(), col, row);
            }
        }
    }

}
