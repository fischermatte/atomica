package com.geolud.atomica.ui;

import com.geolud.atomica.logic.Game;
import com.geolud.atomica.ui.util.GameSituationSerializerUI;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.board.GameBoardPanel;
import com.geolud.atomica.ui.util.components.AtomicaButton;
import com.geolud.atomica.ui.util.components.AtomicaDisplay;
import com.geolud.atomica.ui.util.components.AtomicaPanel;
import com.geolud.atomica.ui.util.language.Language;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays the a game with its statistical information like the score and the
 * current level) plus the game board with all tokens taking place on it.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class GamePanel extends AtomicaPanel implements Observer {
    /**
     * Action command which indicates that the game shall closed.
     */
    public final static String ACTION_CMD_QUITGAME = "quitGame";

    /**
     * TextField displaying the current score.
     */
    private AtomicaDisplay scoresTextField;

    /**
     * TextField displaying the current level.
     */
    private AtomicaDisplay levelTextField;

    /**
     * TextField displaying the score required to reach the next level.
     */
    private AtomicaDisplay scoresUntilNextLevelTextField;

    /**
     *
     */
    private AtomicaButton flushButton;

    /**
     * The game object holding the current game situation.
     */
    private Game game = null;

    /**
     * The game board panel responsible for interaction and displaying the game
     * situation with all placed tokens.
     */
    private GameBoardPanel gameBoardPanel = null;

    /**
     * A listener for sending ui notifications to. Used for notifying the quit
     * action to the application frame.
     */
    private ActionListener parentAL = null;

    // /////////// VIEW SECTION /////////////

    /**
     * Creates a game panel by the given game.
     */
    public GamePanel(Game game, ActionListener al) {
        super();

        this.game = game;
        this.parentAL = al;

        // register at the model as observer
        game.addObserver(this);

        setLayout(new BorderLayout());

        // add the game board panel displaying the game situation
        add(getGameBoardPanel(), BorderLayout.CENTER);

        // add the statistic panel displaying the current level and score
        add(getStatsPanel(), BorderLayout.WEST);

        // add the bottom panel with the save and back button
        add(getBottomPanel(), BorderLayout.SOUTH);

    }

    /**
     * Returns the bottom panel containing the the save and quit button.
     *
     * @return the bottom panel
     */
    private AtomicaPanel getBottomPanel() {
        // create a panel with flow layout
        AtomicaPanel bottomPanel = new AtomicaPanel();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);
        flowLayout.setAlignment(FlowLayout.LEFT);
        bottomPanel.setLayout(flowLayout);
        bottomPanel
                .setBorder(new MatteBorder(3, 0, 0, 0, new Color(50, 50, 50)));

        // add a button for quitting the game
        AtomicaButton quitButton = new AtomicaButton();
        quitButton.setText(Language.getString("quitGame"));
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                quitGame();
            }
        });
        bottomPanel.add(quitButton);

        // add a button for saving the game
        AtomicaButton saveButton = new AtomicaButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                saveGame();
            }
        });
        saveButton.setText(Language.getString("saveGame"));
        bottomPanel.add(saveButton);

        return bottomPanel;
    }

    /**
     * Returns the panel displaying the game board.
     *
     * @return the panel displaying the game board
     */
    private Component getGameBoardPanel() {
        if (gameBoardPanel == null) {
            gameBoardPanel = new GameBoardPanel(this.game);
            gameBoardPanel.setPreferredSize(new Dimension(250, 300));
        }

        return gameBoardPanel;
    }

    /**
     * Returns the statistic panel displaying the current level and score.
     *
     * @return the statistic panel displaying the current level and score
     */
    private AtomicaPanel getStatsPanel() {
        // add statistics panel for holding scores, level, etc...
        AtomicaPanel statsPanel = new AtomicaPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setPreferredSize(new Dimension(150, 300));
        statsPanel.setMinimumSize(new Dimension(0, 400));

        // prepare grid layout
        AtomicaPanel gridHolderPanel = new AtomicaPanel();
        statsPanel.add(gridHolderPanel);
        AtomicaPanel gridPanel = new AtomicaPanel();
        gridPanel.setOpaque(false);
        final GridLayout gridLayout = new GridLayout(8, 0);
        gridPanel.setLayout(gridLayout);
        gridHolderPanel.add(gridPanel);

        // add Score Label and TextField
        AtomicaPanel scorePanel = new AtomicaPanel();
        gridPanel.add(scorePanel);
        JLabel scoreLabel = new JLabel();
        scoreLabel.setPreferredSize(new Dimension(50, 20));
        scoreLabel.setText(Language.getString("scores"));
        scorePanel.add(scoreLabel);
        scoresTextField = new AtomicaDisplay();
        scoresTextField.setPreferredSize(new Dimension(50, 20));
        scorePanel.add(scoresTextField);

        // add Level Label and TextField
        AtomicaPanel levelPanel = new AtomicaPanel();
        gridPanel.add(levelPanel);
        JLabel levelLabel = new JLabel();
        levelLabel.setPreferredSize(new Dimension(50, 20));
        levelLabel.setText(Language.getString("level"));
        levelPanel.add(levelLabel);
        levelTextField = new AtomicaDisplay();
        levelTextField.setPreferredSize(new Dimension(50, 20));
        levelPanel.add(levelTextField);

        // add Label and TextField for scores until next Level
        AtomicaPanel scoresUntilNextLevelPanel = new AtomicaPanel();
        gridPanel.add(scoresUntilNextLevelPanel);
        JLabel scoresUntilNextLevelLabel = new JLabel();
        scoresUntilNextLevelLabel.setPreferredSize(new Dimension(50, 20));
        scoresUntilNextLevelLabel.setText(Language
                .getString("scoresUntilNextLevel"));
        scoresUntilNextLevelPanel.add(scoresUntilNextLevelLabel);

        scoresUntilNextLevelTextField = new AtomicaDisplay();
        scoresUntilNextLevelTextField.setPreferredSize(new Dimension(50, 20));
        scoresUntilNextLevelPanel.add(scoresUntilNextLevelTextField);

        // add Kipper Label and Button
        AtomicaPanel flushPanel = new AtomicaPanel();
        gridPanel.add(flushPanel);
        flushButton = new AtomicaButton();
        flushButton.setText(Language.getString("flush"));
        flushButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                game.flushTokens();
            }

        });
        flushPanel.add(flushButton);

        return statsPanel;
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
     * from the model (Game) for updating the view. It updates the text fields
     * for displaying the current level, score and score until next level. It
     * also forces the game board panel to repaint. In case the game is over a
     * message box will be displayed.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        levelTextField.setText(Integer.toString(game.getCurrentLevelNumber()));
        scoresTextField.setText(Integer.toString(game.getScore()));
        scoresUntilNextLevelTextField.setText(Integer.toString(game
                .calcScoresUntilNextLevel()));

        if (game.getMoleculeNumber() >= 3) {
            flushButton.setEnabled(true);
        } else {
            flushButton.setEnabled(false);
        }

        repaint();

        if (game.getIsGameOver()) {
            JOptionPane.showMessageDialog(this, Language.getString("gameOver"),
                    Language.getString("com/geolud/atomica"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Quits the current game after confirmation an upcoming message box.
     */
    private void quitGame() {
        Object[] options =
                {Language.getString("yes"), Language.getString("no"),
                        Language.getString("cancel")};
        int ret = JOptionPane.showOptionDialog(this, Language
                        .getString("shallQuitGame"), Language.getString("quitGame"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);

        // YES
        if (ret == JOptionPane.YES_OPTION) {
            parentAL.actionPerformed(new ActionEvent(this, 0,
                    ACTION_CMD_QUITGAME));
        }

    }

    /**
     * Opens a JFileChooser for selecting a path. Saves the current game
     * situation at that location in a file.
     */
    private void saveGame() {
        GameSituationSerializerUI serializer = new GameSituationSerializerUI(
                this);
        serializer.save(game.getSituation());
    }
}
