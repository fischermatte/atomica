package com.geolud.atomica.ui;

import com.geolud.atomica.objects.Board;
import com.geolud.atomica.objects.GameSettings;
import com.geolud.atomica.objects.Level;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.components.AtomicaButton;
import com.geolud.atomica.ui.util.components.AtomicaLabel;
import com.geolud.atomica.ui.util.components.AtomicaNumberSpinner;
import com.geolud.atomica.ui.util.components.AtomicaPanel;
import com.geolud.atomica.ui.util.language.Language;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * User Interface for the configuration of the game settings. This includes the
 * base factor, the board size and the levels.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class GameSettingsPanel extends AtomicaPanel implements Observer {
    /**
     * Action command which indicates that the game settings panel shall be
     * closed.
     */
    public static String ACTION_CMD_CLOSE = "closeGameSettings";

    /**
     * TextField holding the base factor.
     */
    private AtomicaNumberSpinner baseFactorTextField;

    /**
     * TextField holding the number of the columns of the board.
     */
    private AtomicaNumberSpinner colsTextField;

    /**
     * TextField holding the number of the rows of the board.
     */
    private AtomicaNumberSpinner rowsTextField;

    /**
     * List of TextField for all levels, each takes the score of one level.
     */
    private ArrayList<AtomicaNumberSpinner> levelTextFields = new ArrayList<AtomicaNumberSpinner>();

    /**
     * The game settings to work on during configuration. A copy of the given
     * original settings.
     */
    private GameSettings gameSettings = null;

    /**
     * The original game settings. After applying the changes this object will
     * to be updated.
     */
    private GameSettings gameSettingsOriginal = null;

    /**
     * A listener for sending ui notifications to. Used for notifying the
     * "close" action to the application frame.
     */
    private ActionListener parentAL = null;

    /**
     * The Button for applying the changes.
     */
    private AtomicaButton applyButton;

    /**
     * Flag which indicates if the game settings are changed by the user.
     */
    private boolean changed = false;

    // /////////// VIEW SECTION /////////////

    /**
     * Create the panel and displays the values of the given settings.
     *
     * @param gameSettings the settings to configure
     * @param parentAL     a listener to fire ui notifications to
     */
    public GameSettingsPanel(GameSettings gameSettings, ActionListener parentAL) {
        super();

        this.gameSettingsOriginal = gameSettings;
        this.gameSettings = gameSettings.deepClone();
        this.parentAL = parentAL;

        // adds the main panel containing all settings
        add(getMainPanel(), BorderLayout.NORTH);

        // adds the bottom panel containing the back, default and apply button
        add(getBottomPanel(), BorderLayout.SOUTH);

        gameSettings.addObserver(this);

    }

    /**
     * Validates and applies the current settings to the model.
     *
     * @return true when the current settings could be applied
     */
    private boolean apply() {
        // validate first all input values
        if (!isValidInput()) {
            return false;
        }

        // validation was successful so go on and apply the values to the model
        gameSettings.setRows(rowsTextField.getIntValue());
        gameSettings.setCols(colsTextField.getIntValue());

        for (int n = 0; n < levelTextFields.size(); n++) {
            AtomicaNumberSpinner textField = levelTextFields.get(n);
            gameSettings.getLevels().get(n).setScore(textField.getIntValue());
        }

        GameSettings.setBaseFactor(baseFactorTextField.getIntValue());

        gameSettingsOriginal.applyValues(gameSettings);

        setChanged(false);

        return true;
    }

    /**
     * Brings a message box asking for applying the changes and fires the
     * closing action command.
     */
    private void cancel() {
        if (changed) {
            Object[] options =
                    {Language.getString("yes"), Language.getString("no"),
                            Language.getString("cancel")};
            int ret = JOptionPane.showOptionDialog(this, Language
                            .getString("shallSaveSettings"), Language
                            .getString("saveSettings"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            // yes - save settings
            if (ret == JOptionPane.YES_OPTION) {
                if (!apply())
                    return;
            }
            // Cancel
            else if (ret == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        parentAL.actionPerformed(new ActionEvent(this, 0, ACTION_CMD_CLOSE));
    }

    /**
     * Returns the bottom panel containing the back, apply and default button.
     *
     * @return the bottom panel
     */
    private AtomicaPanel getBottomPanel() {
        // bottom panel
        final AtomicaPanel bottomPanel = new AtomicaPanel();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);
        flowLayout.setAlignment(FlowLayout.LEFT);
        bottomPanel.setLayout(flowLayout);
        bottomPanel
                .setBorder(new MatteBorder(3, 0, 0, 0, new Color(50, 50, 50)));

        // back button
        final AtomicaButton backButton = new AtomicaButton();
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                cancel();
            }
        });
        backButton.setText(Language.getString("back"));
        bottomPanel.add(backButton);

        // apply button
        applyButton = new AtomicaButton();
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                apply();
            }
        });
        applyButton.setText(Language.getString("apply"));
        applyButton.setEnabled(false);
        bottomPanel.add(applyButton);

        // default button
        final AtomicaButton defaultButton = new AtomicaButton();
        defaultButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                setDefault();
            }

        });
        defaultButton.setText(Language.getString("setDefault"));
        bottomPanel.add(defaultButton);

        return bottomPanel;
    }

    /**
     * Returns the main panel containing all the settings.
     *
     * @return the main panel
     */
    private AtomicaPanel getMainPanel() {
        setLayout(new BorderLayout());

        // create the main panel
        AtomicaPanel mainPanel = new AtomicaPanel();
        final FlowLayout mainFlowLayout = new FlowLayout();
        mainFlowLayout.setHgap(20);
        mainFlowLayout.setVgap(0);
        mainFlowLayout.setAlignment(FlowLayout.LEFT);
        mainPanel.setLayout(mainFlowLayout);

        // center panel with the game settings
        final AtomicaPanel gridBagPanel = new AtomicaPanel();
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagPanel.setLayout(gridBagLayout);
        mainPanel.add(gridBagPanel);

        final Component component = Box.createVerticalStrut(20);
        final GridBagConstraints gridBagConstraintsStrut = new GridBagConstraints();
        gridBagConstraintsStrut.gridy = 1;
        gridBagConstraintsStrut.gridx = 7;
        gridBagPanel.add(component, gridBagConstraintsStrut);

        // BASE FACTOR - LABEL
        final AtomicaLabel baseFactorLabel = new AtomicaLabel();
        baseFactorLabel.setText(Language.getString("baseFactor") + ":");
        final GridBagConstraints gridBagConstraintsBaseFactorLabel = new GridBagConstraints();
        gridBagConstraintsBaseFactorLabel.insets = new Insets(0, 0, 5, 10);
        gridBagConstraintsBaseFactorLabel.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsBaseFactorLabel.gridwidth = 6;
        gridBagConstraintsBaseFactorLabel.gridy = 2;
        gridBagConstraintsBaseFactorLabel.gridx = 0;
        gridBagPanel.add(baseFactorLabel, gridBagConstraintsBaseFactorLabel);

        // BOARD SIZE - LABEL
        final AtomicaLabel boardSizeLabel = new AtomicaLabel();
        boardSizeLabel.setText(Language.getString("gameBoardSize") + ":");
        final GridBagConstraints gridBagConstraintsBoardSizeLabel = new GridBagConstraints();
        gridBagConstraintsBoardSizeLabel.insets = new Insets(15, 0, 5, 10);
        gridBagConstraintsBoardSizeLabel.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsBoardSizeLabel.gridwidth = 6;
        gridBagConstraintsBoardSizeLabel.gridy = 8;
        gridBagConstraintsBoardSizeLabel.gridx = 0;
        gridBagPanel.add(boardSizeLabel, gridBagConstraintsBoardSizeLabel);

        // LEVEL SCORES - LABEL
        final AtomicaLabel levelLabel = new AtomicaLabel();
        levelLabel.setText(Language.getString("levelScores") + ":");
        final GridBagConstraints gridBagConstraintsLevelLabel = new GridBagConstraints();
        gridBagConstraintsLevelLabel.insets = new Insets(15, 0, 5, 10);
        gridBagConstraintsLevelLabel.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsLevelLabel.gridwidth = 6;
        gridBagConstraintsLevelLabel.gridy = 21;
        gridBagConstraintsLevelLabel.gridx = 0;
        gridBagPanel.add(levelLabel, gridBagConstraintsLevelLabel);

        // BASE FACTOR - TEXTFIELD
        baseFactorTextField = new AtomicaNumberSpinner(GameSettings
                .getBaseFactor(), GameSettings.MIN_BASEFACTOR,
                GameSettings.MAX_BASEFACTOR);
        baseFactorTextField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setChanged(true);
            }
        });
        final GridBagConstraints gridBagConstraintsBaseFactorTextField = new GridBagConstraints();
        gridBagConstraintsBaseFactorTextField.gridwidth = 3;
        gridBagConstraintsBaseFactorTextField.insets = new Insets(0, 5, 5, 5);
        gridBagConstraintsBaseFactorTextField.gridy = 2;
        gridBagConstraintsBaseFactorTextField.gridx = 6;
        gridBagPanel.add(baseFactorTextField,
                gridBagConstraintsBaseFactorTextField);

        // BOARD SIZE ROWS - TEXTFIELD
        rowsTextField = new AtomicaNumberSpinner(gameSettings.getRows(),
                Board.MIN_ROWS, Board.MAX_ROWS);
        rowsTextField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setChanged(true);
            }
        });
        final GridBagConstraints gridBagConstraintsRows = new GridBagConstraints();
        gridBagConstraintsRows.insets = new Insets(15, 5, 5, 5);
        gridBagConstraintsRows.gridwidth = 3;
        gridBagConstraintsRows.gridy = 8;
        gridBagConstraintsRows.gridx = 6;
        gridBagPanel.add(rowsTextField, gridBagConstraintsRows);

        // BOARD SIZE COLUMNS - TEXTFIELD
        final AtomicaLabel xLabel = new AtomicaLabel();
        xLabel.setText("X");
        final GridBagConstraints gridBagConstraintsXLabel = new GridBagConstraints();
        gridBagConstraintsXLabel.insets = new Insets(15, 5, 5, 5);
        gridBagConstraintsXLabel.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsXLabel.gridwidth = 1;
        gridBagConstraintsXLabel.gridy = 8;
        gridBagConstraintsXLabel.gridx = 10;
        gridBagPanel.add(xLabel, gridBagConstraintsXLabel);

        colsTextField = new AtomicaNumberSpinner(gameSettings.getCols(),
                Board.MIN_COLS, Board.MAX_COLS);
        colsTextField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setChanged(true);
            }
        });
        final GridBagConstraints gridBagConstraintsCols = new GridBagConstraints();
        gridBagConstraintsCols.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsCols.insets = new Insets(15, 5, 5, 5);
        gridBagConstraintsCols.gridy = 8;
        gridBagConstraintsCols.gridx = 11;
        gridBagPanel.add(colsTextField, gridBagConstraintsCols);

        // LEVEL SCORES - TEXTFIELDs
        final GridBagConstraints gridBagConstraintsLevels = new GridBagConstraints();
        gridBagConstraintsLevels.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraintsLevels.gridy = 21;
        gridBagConstraintsLevels.gridx = 6;
        gridBagConstraintsLevels.gridwidth = 1;

        // for each level create a label and a textfield (except last level)
        for (Level level : gameSettings.getLevels()) {
            // since the last level doesn't have a final score skip it
            if (level == gameSettings.getLastLevel()) {
                break;
            }

            // set the grid position considering the level number
            if (level.getLevelNumber() > 1) {
                gridBagConstraintsLevels.gridy++;
                gridBagConstraintsLevels.insets = new Insets(2, 5, 5, 10);
            } else {
                gridBagConstraintsLevels.insets = new Insets(15, 5, 5, 10);
            }

            // add the level number label
            final AtomicaLabel levelNrLabel = new AtomicaLabel();
            levelNrLabel.setText(Language.getString("level") + " "
                    + level.getLevelNumber() + ":");
            gridBagConstraintsLevels.gridx = 6;
            gridBagConstraintsLevels.gridwidth = 3;
            gridBagPanel.add(levelNrLabel, gridBagConstraintsLevels);

            // add the text field for score input
            AtomicaNumberSpinner scoreTextField = new AtomicaNumberSpinner(
                    level.getScore(), Level.MIN_SCORE, Level.MAX_SCORE, 100);
            scoreTextField.setIntValue(level.getScore());
            scoreTextField.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setChanged(true);
                }
            });

            gridBagConstraintsLevels.gridx = 9;
            gridBagConstraintsLevels.gridwidth = 3;
            gridBagPanel.add(scoreTextField, gridBagConstraintsLevels);
            levelTextFields.add(scoreTextField);
        }

        return mainPanel;

    }

    /**
     * Overwritten for displaying the background image.
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(ImageLoader.getInstance().getBackgroundImage(), 0, 0,
                getWidth(), getHeight(), null);
        super.paintComponent(g);
    }

    /**
     * Due to the observer pattern this method is used to receive notifications
     * from the model (GameSettings) for updating the view.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (baseFactorTextField != null) {
            baseFactorTextField.setIntValue(GameSettings.getBaseFactor());
        }

        if (rowsTextField != null) {
            rowsTextField.setIntValue(gameSettings.getRows());
        }

        if (colsTextField != null) {
            colsTextField.setIntValue(gameSettings.getCols());
        }

        ArrayList<Level> levels = gameSettings.getLevels();
        for (int i = 0; i < levelTextFields.size(); i++) {
            AtomicaNumberSpinner ans = levelTextFields.get(i);
            if (ans != null) {
                Level l = levels.get(i);
                ans.setIntValue(l.getScore());
            }
        }
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Returns whether the input values are valid (within the allowed maximum
     * and minimum values).
     *
     * @return true if all values are valid
     */
    private boolean isValidInput() {
        // check the base factor
        if (!baseFactorTextField.isValidValue()) {
            String validValues = " (min:" + GameSettings.MIN_BASEFACTOR
                    + ", max: " + GameSettings.MAX_BASEFACTOR + ")";
            JOptionPane.showMessageDialog(this, Language
                            .getString("invalidBaseFactor")
                            + validValues, Language.getString("com/geolud/atomica"),
                    JOptionPane.OK_OPTION);
            return false;
        }

        // check number of rows
        if (!rowsTextField.isValidValue()) {
            String validValues = " (min:" + Board.MIN_ROWS + ", max: "
                    + Board.MAX_ROWS + ")";
            JOptionPane.showMessageDialog(this, Language
                            .getString("invalidRowNumber")
                            + validValues, Language.getString("com/geolud/atomica"),
                    JOptionPane.OK_OPTION);
            return false;
        }

        // check number of columns
        if (!colsTextField.isValidValue()) {
            String validValues = " (min:" + Board.MIN_COLS + ", max: "
                    + Board.MAX_COLS + ")";
            JOptionPane.showMessageDialog(this, Language
                            .getString("invalidColNumber")
                            + validValues, Language.getString("com/geolud/atomica"),
                    JOptionPane.OK_OPTION);
            return false;
        }

        // check if the score of every level is bigger than the previous
        int previusScore = -1;
        for (int n = 0; n < levelTextFields.size(); n++) {

            String validValues = " (min:" + Level.MIN_SCORE + ", max: "
                    + Level.MAX_SCORE + ")";

            AtomicaNumberSpinner textField = levelTextFields.get(n);
            if (!textField.isValidValue()) {
                JOptionPane.showMessageDialog(this,
                        Language.getString("level") + " " + (n + 1) + ": "
                                + Language.getString("invalidLevelScore")
                                + validValues, Language.getString("com/geolud/atomica"),
                        JOptionPane.OK_OPTION);
                return false;
            }

            int curScore = textField.getIntValue();
            if (n > 0 && curScore <= previusScore) {
                JOptionPane.showMessageDialog(this, Language
                                .getString("currentLevelScoreNotGreaterPrevious"),
                        Language.getString("com/geolud/atomica"), JOptionPane.OK_OPTION);
                return false;
            }
            previusScore = curScore;
        }

        return true;
    }

    /**
     * Sets whether the user made changes.
     *
     * @param changed flag if user made changes
     */
    private void setChanged(boolean changed) {
        this.changed = changed;
        applyButton.setEnabled(changed);
    }

    /**
     * Sets all component's values back to the default values.
     */
    private void setDefault() {
        setChanged(true);
        gameSettings.setDefaultValues();
    }
}
