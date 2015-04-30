package com.geolud.atomica.objects;

import java.io.Serializable;

/**
 * Defines a game board with a certain number of rows and columns.
 *
 * @author Georg Ludewig
 */
public class Board implements Serializable {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = 7214836735126503766L;

    /**
     * The minimum number of rows a board can have.
     */
    public static final int MIN_ROWS = 3;

    /**
     * The minimum number of columns a board can have.
     */
    public static final int MIN_COLS = 3;

    /**
     * The maximum number of rows a board can have.
     */
    public static final int MAX_ROWS = 30;

    /**
     * The maximum number of columns a board can have.
     */
    public static final int MAX_COLS = 30;

    /**
     * The default number of rows.
     */
    public static final int DEF_ROWS = 10;

    /**
     * The default number of columns.
     */
    public static final int DEF_COLS = 10;

    /**
     * The number of columns of the board.
     */
    private int cols = 10;

    /**
     * The number of rows of the board.
     */
    private int rows = 10;

    /**
     * Creates a board with the default number of rows and columns.
     */
    public Board() {
        super();

        this.rows = DEF_ROWS;
        this.cols = DEF_COLS;
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the number of rows.
     *
     * @return the number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of columns.
     *
     * @param cols the new number of columns
     */
    public void setCols(int cols) {
        assert (cols > MAX_COLS && cols < MIN_COLS);
        this.cols = cols;
    }

    /**
     * Sets the default number of rows and columns.
     */
    public void setDefaultValues() {
        this.cols = DEF_COLS;
        this.rows = DEF_ROWS;
    }

    /**
     * Sets the number of rows.
     *
     * @param rows the new number of rows
     */
    public void setRows(int rows) {
        assert (rows > MAX_ROWS && rows < MIN_ROWS);
        this.rows = rows;
    }

}
