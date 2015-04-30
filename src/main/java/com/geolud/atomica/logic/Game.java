package com.geolud.atomica.logic;

import com.geolud.atomica.logic.pathfinding.Path;
import com.geolud.atomica.logic.pathfinding.PathFinder;
import com.geolud.atomica.objects.*;
import com.geolud.atomica.util.logging.Logging;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * The class represents a running Game. A Game object can be instantiated as a
 * new game or by loading a serialized GameSituation.
 *
 * @author Georg Ludewig
 */
public class Game extends Observable {
    /**
     * Holds the state of the game
     */
    private GameSituation gameSituation = null;

    /**
     * Stores all detected Molecules of one round.
     */
    private ArrayList<Molecule> moleculesInCurrentRound = null;

    /**
     * Stores all detected Molecules of one round.
     */
    private ArrayList<Molecule> moleculesInGame = null;

    /**
     * A PathFinder instance for finding shortest ways
     */
    private PathFinder pathFinder = null;

    /**
     * Flag which indicates if the game is over
     */
    private boolean isGameOver;

    /**
     * Default constructor for initialization. It doesn't create a valid game so
     * it is privat.
     */
    private Game() {
        super();

        this.pathFinder = new PathFinder();
        this.moleculesInCurrentRound = new ArrayList<Molecule>();
        this.moleculesInGame = new ArrayList<Molecule>();
        isGameOver = false;
    }

    /**
     * Creates a new Game with random initial Tokens using the given game
     * settings.
     *
     * @param gameSettings the GameSettings which define the game
     */
    public Game(GameSettings gameSettings) {
        this();
        gameSituation = new GameSituation(gameSettings);
    }

    /**
     * Creates a Game with the given initial GameSituation.
     *
     * @param gameSituation the initial GameSituation
     */
    public Game(GameSituation gameSituation) {
        this();
        this.gameSituation = gameSituation;
    }

    /**
     * Calculates the score for the constructed Molecules and adds them to the
     * current GameSituation.
     *
     * @param molecules the constructed Molecules
     */
    private void addMoleculeScore(ArrayList<Molecule> molecules) {
        for (Molecule m : molecules) {
            moleculesInCurrentRound.add(m);
            int score = calcMoleculeScore(m);
            // Consider "Combos": the more molecules found in one round the
            // higher factor
            if (moleculesInCurrentRound.size() > 1) {
                score = score * moleculesInCurrentRound.size();
                Logging.getLogger().log(java.util.logging.Level.INFO,
                        moleculesInCurrentRound.size() + " Combo");

            }
            gameSituation.addScore(score);
            Logging.getLogger().log(
                    java.util.logging.Level.INFO,
                    score + " scores added for molecule with " + m.size()
                            + " atoms");
        }
    }

    /**
     * Indicates if there are Atoms in current GameSituation.
     *
     * @return true when no Atoms in current GameSituation
     */
    private boolean allAtomsRemoved() {
        ArrayList<AtomToken> atoms = gameSituation.getAtoms();
        if (atoms == null || atoms.size() == 0)
            return true;

        return false;
    }

    /**
     * Adds additional IndicatorTokens to an initial GameSituation if it has
     * less than 3.
     */
    private void assureFirstRoundHasIndicators() {
        ArrayList<IndicatorToken> indicators = gameSituation.getIndicators();
        if (indicators.size() == GameSituation.INDICATORS_IN_ROUND)
            return;

        ArrayList<Integer> colors2Exlcude = new ArrayList<Integer>();
        // for (IndicatorToken indicator : indicators)
        // {
        // if (!colors2Exlcude.contains(indicator.getColorIndex()))
        // {
        // colors2Exlcude.add(indicator.getColorIndex());
        // }
        // }

        ArrayList<Integer> colorsIndicator = getRandomColors(colors2Exlcude,
                GameSituation.INDICATORS_IN_ROUND - indicators.size(),
                gameSituation.getCurrentLevel());

        for (Integer color : colorsIndicator) {
            Token indicator = new IndicatorToken(color);
            Field indicatorField = findRandomEmptyField();
            if (indicatorField != null) {
                indicatorField.placeToken(indicator);
            }
        }
    }

    /**
     * Calculates the score for a given Molecule.
     *
     * @param m the Molecule which score shall be calculated
     * @return the calculated score for the Molecule
     */
    private int calcMoleculeScore(Molecule m) {
        int size = m.size();

        // Level currentLevel = gameSituation.getCurrentLevel();
        // int f = currentLevel.getNumberOfColors();
        // int b = GameSettings.getBaseFactor();
        //
        int score = 0;
        //
        // // calculate scores for molecules with 4 atoms
        // if (size == 4)
        // {
        // score = (f - 2) * b;
        // }
        // // calculate score for molecules with more then 4 atoms
        // else if (size > 4)
        // {
        // double factor = (double) size / 3;
        // score = (int) (factor * ((f - 2) * b));
        // }

        score = size * size;

        return score;
    }

    /**
     * Considering the current score, this method returns the necessary score to
     * reach the next Level.
     *
     * @return the necessary score to reach the next Level
     */
    public int calcScoresUntilNextLevel() {
        Level level = gameSituation.getCurrentLevel();
        int score = level.getScore() - gameSituation.getCurrentScore();

        if (level == gameSituation.getLastLevel())
            score = 0;

        return score;
    }

    /**
     * Checks if current score require to change the Level. The Level will not
     * be changed if it is the last Level.
     *
     * @return true if the Level was changed
     */
    private boolean checkLevelChange() {
        Level currentLevel = gameSituation.getCurrentLevel();

        // if current level is the last level, change to a higher
        // level is not possible
        if (currentLevel == gameSituation.getLastLevel())
            return false;

        // check if the necessary scores for changing
        // to a higher level are reached
        int score = gameSituation.getCurrentScore();
        if (score < currentLevel.getScore())
            return false;

        Level nextLevel = gameSituation
                .getLevel(currentLevel.getLevelNumber() + 1);
        while (score >= nextLevel.getScore()
                && nextLevel != gameSituation.getLastLevel()) {
            nextLevel = gameSituation.getLevel(nextLevel.getLevelNumber() + 1);
            if (!(score >= nextLevel.getScore())) {
                break;
            }
        }

        if (nextLevel == currentLevel)
            return false;

        gameSituation.setCurrentLevel(nextLevel);
        Logging.getLogger().log(java.util.logging.Level.INFO,
                "Level changed to " + nextLevel.getLevelNumber());

        return true;
    }

    /**
     * This Method detects all Molecules, adds their score and removes them from
     * the board.
     *
     * @return true if Molecules were detected and removed
     */
    private boolean checkNewMolecules() {
        boolean moleculesFound = false;

        MoleculeDetector moleculeDetector = new MoleculeDetector(gameSituation);
        ArrayList<Molecule> molecules = moleculeDetector.detectMolecules();

        if (molecules != null && molecules.size() > 0) {
            Logging.getLogger().log(java.util.logging.Level.INFO,
                    molecules.size() + " molecules found");

            // calculate score and add it to current scores
            addMoleculeScore(molecules);

            for (Molecule m : molecules) {
                addMoleculeInGame(m);
            }

            // remove molecules from board
            removeMoleculesFromBoard(molecules);

            // if the necessary score of the current level is reached,
            // it might change to a higher level
            checkLevelChange();

            moleculesFound = true;
        }

        return moleculesFound;
    }

    private void addMoleculeInGame(Molecule m) {
        moleculesInGame.add(m);
    }

    /**
     * Finds a random empty Field in current GameSituation.
     *
     * @return an empty field
     */
    public Field findRandomEmptyField() {
        boolean allowIndicators = false;
        ArrayList<Field> emptyFields = gameSituation
                .getEmptyFields(allowIndicators);

        int size = emptyFields.size();
        if (size == 0)
            return null;

        Random random = new Random();

        int n = random.nextInt(size);

        Field field = emptyFields.get(n);

        return field;
    }

    /**
     * Finds the shortest Path between the two Fields considering blocked Fields
     * by Atoms.
     *
     * @param from the Field where to start
     * @param to   the destination Field
     * @return the shortest Path
     */
    public Path findShortestPath(Field from, Field to) {
        return pathFinder.findShortestPath(gameSituation, from, to);
    }

    /**
     * Places randomly atoms and indicators if current GameSituation does not
     * contain any token. It also assures that an initial GameSituations has the
     * required number of indicators.
     */
    private void firstRound() {
        // if board is empty put new tokens
        if (gameSituation.getTokens().size() == 0) {
            // considering the current level and set new tokens
            // on random fields
            placeNewIndicators();

            // check current game situation for new
            // molecules and remove them from the board
            placeNewAtoms();
        }
        // in order to play also initial situations
        else {
            assureFirstRoundHasIndicators();
        }
    }

    /**
     * Returns the number of columns in current GameSituation.
     *
     * @return the number of columns
     */
    public int getCols() {
        return gameSituation.getCols();
    }

    /**
     * Returns the Number of the current Level.
     *
     * @return the number of the current Level
     */
    public int getCurrentLevelNumber() {
        return gameSituation.getCurrentLevel().getLevelNumber();
    }

    /**
     * Returns the Field instance at the given Position.
     *
     * @param col the column of the field
     * @param row the row of the field
     * @return the field at the given position
     */
    public Field getField(int col, int row) {
        return gameSituation.getField(col, row);
    }

    /**
     * Checks if a Game is finished.
     *
     * @return true if the Game is over
     */
    public boolean getIsGameOver() {
        return isGameOver;
    }

    /**
     * Creates random color indices considering the allowed colors in the given
     * Level. As an initial GameSituation may not have the required number of
     * IndicatorTokens, this method is used to get the colors of the additional
     * IndicatorTokens.
     *
     * @param colors2Exclude excluding colors which are not allowed to create
     * @param number         the number of colors that shall be created
     * @param level          the Level for which random colors shall be created
     * @return an ArrayList with random color indices
     */
    public ArrayList<Integer> getRandomColors(
            ArrayList<Integer> colors2Exclude, int number, Level level) {
        if (colors2Exclude == null)
            return null;

        ArrayList<Integer> randomColors = new ArrayList<Integer>();

        Random random = new Random();

        while (randomColors.size() < number) {
            int randomColor = random.nextInt(level.getNumberOfColors());
            if (!randomColors.contains(randomColor)
                    && randomColor != level.getNumberOfColors()) {
                if (!colors2Exclude.contains(randomColor)) {
                    randomColors.add(randomColor);
                }
            }
        }

        return randomColors;
    }

    /**
     * Creates random color indices considering the allowed colors in the given
     * Level.
     *
     * @param level the Level for which random colors shall be created
     * @return an ArrayList with random color indices
     */
    public ArrayList<Integer> getRandomColors(Level level) {
        ArrayList<Integer> randomColors = new ArrayList<Integer>();

        Random random = new Random();

        while (randomColors.size() != GameSituation.INDICATORS_IN_ROUND) {
            int randomColor = random.nextInt(level.getNumberOfColors());
            if (/*
                 * !randomColors.contains(randomColor) &&
                 */randomColor != level.getNumberOfColors()) {
                randomColors.add(randomColor);
            }
        }

        return randomColors;
    }

    /**
     * Returns the number of rows in current GameSituation.
     *
     * @return the number of rows
     */
    public int getRows() {
        return gameSituation.getRows();
    }

    /**
     * Returns the score of current GameSituation.
     *
     * @return the score of current GameSituation
     */
    public int getScore() {
        return gameSituation.getCurrentScore();
    }

    /**
     * Returns the current GameSituation.
     *
     * @return the current GameSituation
     */
    public GameSituation getSituation() {
        return gameSituation;
    }

    /**
     * Moves an AtomToken to the given destination Field. If there is a path to
     * the Field and the AtomToken could be placed, this Method returns true.
     * <p/>
     * <p>
     * If by moving the AtomToken no Molecule was detected or all Atoms are
     * removed a new Round starts. If that leads to a GameSituation without
     * empty Fields, the Game will be set over.
     * </p>
     *
     * @param atom             the AtomToken to be moved
     * @param destinationField the destination Field
     * @return true if the AtomToken could be moved to the destination Field
     */
    public boolean moveAtomToken(AtomToken atom, Field destinationField) {
        if (destinationField.isBlocked())
            return false;

        Field from = atom.getField();

        // check if there is a possible path to the target
        Path path = pathFinder.findShortestPath(gameSituation, from,
                destinationField);
        if (path == null)
            return false;

        from.removeToken();
        destinationField.placeToken(atom);

        // start next round when ...
        // 1. standard: no new molecule could be detected
        // 2. special treatment: in case all atoms are removed
        if (!checkNewMolecules() || allAtomsRemoved()) {
            nextRound();
        }

        // if there are now no atoms on board start a new round. it will turn
        // current indicators to atoms, so the game keeps playable.
        if (gameSituation.getAtoms().size() == 0) {
            nextRound();
        }

        ArrayList<Field> emptyFields = gameSituation.getEmptyFields(true);
        if (emptyFields.size() <= 1) {
            isGameOver = true;
        }

        setChanged();
        notifyObservers();

        return true;
    }

    /**
     * Starts a new round by transforming current IndicatorToken to AtomToken,
     * checking the situation for new Molecules and placing new Indicators.
     */
    private void nextRound() {
        moleculesInCurrentRound.clear();

        // transform all current indicator to atoms
        transformIndicators();

        // check current game situation for new
        // molecules and remove them from the board
        checkNewMolecules();

        // considering the current level and set new tokens
        // on random fields
        placeNewIndicators();
    }

    /**
     * Places new AtomTokens considering the colors of the current Level.
     */
    private void placeNewAtoms() {
        Level currentLevel = gameSituation.getCurrentLevel();

        ArrayList<Integer> colorsAtoms = getRandomColors(currentLevel);

        for (Integer color : colorsAtoms) {
            Token atom = new AtomToken(color);
            Field atomField = findRandomEmptyField();
            if (atomField == null) {
                // game over
                return;
            }
            atomField.placeToken(atom);
        }
    }

    /**
     * Places new IndicatorTokens considering the colors of the current Level.
     */
    private void placeNewIndicators() {
        Level currentLevel = gameSituation.getCurrentLevel();

        ArrayList<Integer> colorsIndicator = getRandomColors(currentLevel);

        for (Integer color : colorsIndicator) {
            Token indicator = new IndicatorToken(color);
            Field indicatorField = findRandomEmptyField();
            if (indicatorField != null) {
                indicatorField.placeToken(indicator);
            }
        }
    }

    /**
     * Checks if there is an AtomToken on the given Position and returns it.
     *
     * @param col the column of the Field
     * @param row the row of the Field
     * @return the AtomToken at the given Position
     */
    public AtomToken queryAtomToken(int col, int row) {
        return gameSituation.queryAtom(col, row);
    }

    /**
     * Checks if there is a Token on the given Position and returns it.
     *
     * @param col the column of the Field
     * @param row the row of the Field
     * @return the Token at the given Position
     */
    public Token queryToken(int col, int row) {
        Token token = null;
        if (gameSituation != null) {
            token = gameSituation.queryToken(col, row);
        }

        return token;
    }

    /**
     * Removes all AtomTokens of the given Molecule form the current Situation.
     *
     * @param molecules
     */
    private void removeMoleculesFromBoard(ArrayList<Molecule> molecules) {
        if (molecules == null)
            return;

        for (Molecule m : molecules) {
            for (int i = 0; i < m.size(); i++) {
                AtomToken atom = m.getAtom(i);
                Field field = atom.getField();
                if (field != null) {
                    field.removeToken();
                }
            }
        }
    }

    /**
     * Starts a Game by initializing the first round. In case an initial
     * GameSituation without AtomTokens was loaded, it will perform a second
     * round. That will transform current IndicatorTokens to AtomTokens.
     */
    public void start() {
        // set tokens for the first round
        firstRound();

        // in case an initial situation without atoms was loaded,
        // directly go to next round
        if (gameSituation.getAtoms().size() == 0) {
            nextRound();
        }

        setChanged();
        notifyObservers();
        // 2. special treatment: in case all atoms are removed
    }

    /**
     * Transforms all IndicatorTokens of the current Situation to AtomTokens.
     */
    private void transformIndicators() {
        ArrayList<IndicatorToken> indicators = gameSituation.getIndicators();
        for (IndicatorToken indicator : indicators) {
            Field f = indicator.getField();
            f.placeToken(new AtomToken(indicator.getColorIndex()));
        }
    }

    public int getMoleculeNumber() {
        return moleculesInGame.size();
    }

    /**
     * This method lets all tokens in the current game situation "fall" down.
     * New molecules which might be created will be removed.
     * <p/>
     * This was an adaption from the ProPra Seminar 07.09.2008.
     */
    public void flushTokens() {
        int rows = getRows();
        int cols = getCols();

        // start in the lower left in the second row
        for (int r = rows - 2; r >= 0; r--) {
            for (int c = 0; c < cols; c++) {
                Token token = queryToken(c, r);

                // if there already a token continue since it can't "fall" down
                // anymore
                if (token == null)
                    continue;

                // now find emtpy fields below this one so it can fall down to
                // the lowest emtpy field
                int nextRow = r + 1;
                Field emptyNextField = null;
                Field nextField = gameSituation.getField(c, nextRow);
                while (nextField != null && nextField.getToken() == null) {
                    emptyNextField = nextField;
                    nextField = gameSituation.getField(c, nextRow);
                    nextRow += 1;
                }

                if (emptyNextField != null) {
                    emptyNextField.placeToken(token);
                }
            }
        }

        moleculesInGame.clear();

        // start next round when ...
        // 1. standard: no new molecule could be detected
        checkNewMolecules();

        setChanged();
        notifyObservers();
    }
}
