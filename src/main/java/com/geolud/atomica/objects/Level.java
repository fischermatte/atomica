package com.geolud.atomica.objects;

import java.io.Serializable;

/**
 * Represents the level a game can be in. It is defined by a number of allowed
 * colors, a final score for reaching the next level and a level number.
 *
 * @author Georg Ludewig
 */
public class Level implements Serializable {
    /**
     * The generated id for serialization.
     */
    private static final long serialVersionUID = -3377192073340036601L;

    /**
     * The minimum score the final score be assigned to.
     */
    public static final int MIN_SCORE = 10;

    /**
     * The maximum score the final score be assigned to.
     */
    public static final int MAX_SCORE = 100000;

    /**
     * The minimum number of colors a level can have.
     */
    public static final int MIN_NUMBEROFCOLORS = 3;

    /**
     * The maximum number of colors a level can have.
     */
    public static final int MAX_NUMBEROFCOLORS = 12;

    /**
     * The number of the level (1-10).
     */
    private int levelNumber;

    /**
     * The final score required to reach the next level.
     */
    private int score = 100;

    /**
     * The number of colors which can appear when playing in this level.
     */
    private int numberOfColors = 3;

    /**
     * Creates a level with the given number, the score to reach the next level
     * and the number of colors which can appear when playing this level.
     *
     * @param levelNumber    the level number
     * @param finalScore     the final score required to reach the next level
     * @param numberOfColors the number of colors which can appear when playing
     *                       this level
     */
    public Level(int levelNumber, int finalScore, int numberOfColors) {
        this.levelNumber = levelNumber;
        this.score = finalScore;
        this.numberOfColors = numberOfColors;
    }

    /**
     * Returns the number of the level.
     *
     * @return the number of the level
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Returns the number of colors which can appear when playing this level.
     *
     * @return the number of colors which can appear when playing this level
     */
    public int getNumberOfColors() {
        return numberOfColors;
    }

    /**
     * Returns the final score required to reach the next level.
     *
     * @return the final score required to reach the next level
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the level number of the level.
     *
     * @param levelNumber the level number
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     * Sets the number of colors which can appear when playing this level.
     *
     * @param numberOfColors the number of colors which can appear when playing
     *                       this level
     */
    public void setNumberOfColors(int numberOfColors) {
        this.numberOfColors = numberOfColors;
    }

    /**
     * Sets the final score required to reach the next level.
     *
     * @param score the final score required to reach the next level.
     */
    public void setScore(int score) {
        this.score = score;
    }

}
