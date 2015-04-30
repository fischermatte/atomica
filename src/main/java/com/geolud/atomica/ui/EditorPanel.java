package com.geolud.atomica.ui;

import com.geolud.atomica.logic.Editor;
import com.geolud.atomica.logic.Editor.EditorMode;
import com.geolud.atomica.objects.*;
import com.geolud.atomica.ui.util.GameSituationSerializerUI;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.board.BoardPanel;
import com.geolud.atomica.ui.util.board.EditorBoardPanel;
import com.geolud.atomica.ui.util.components.*;
import com.geolud.atomica.ui.util.language.Language;
import com.geolud.atomica.util.logging.Logging;

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
 * Allows the user to create an initial game situation. Editable settings are
 * the board size and the number of colors which are allowed when playing that
 * game. Depending on this number the user can select tokens (atoms and
 * indicators) from a tool box and place them on the game board. Due to observer
 * pattern it implements the <code>Observer</code> interface.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class EditorPanel extends AtomicaPanel implements Observer {
    /**
     * Action command which indicates that the editor shall be closed.
     */
    public static final String ACTION_CMD_CLOSE = "editorClose";

    /**
     * The editor object holding the editing game situation.
     */
    private Editor editor = null;

    /**
     * The text field holding the number of columns of the game board.
     */
    private AtomicaNumberSpinner colsTextField;

    /**
     * The text field holding the number of rows of the game board.
     */
    private AtomicaNumberSpinner rowsTextField;

    /**
     * The board panel displaying the game situation.
     */
    private BoardPanel boardPanel;

    /**
     * List with all buttons which can be displayed for selecting a token. Is
     * initialized only ones.
     */
    private ArrayList<AbstractButton> tokenButtons = new ArrayList<AbstractButton>();

    /**
     * The panel holding the buttons for selecting a token.
     */
    private AtomicaPanel toolButtonsPanel = null;

    /**
     * The panel holding the configuration options like the board size or the
     * number of colors.
     */
    private AtomicaPanel configurationPanel = null;

    /**
     * The bottom panel holding the load, save and back button.
     */
    private AtomicaPanel bottomPanel = null;

    /**
     * A button group since the only one of the token tool buttons can be
     * active.
     */
    private ButtonGroup tokenButtonsGroup = null;

    /**
     * The combobox for setting the number of colors.
     */
    private AtomicaComboBox colorsComboBox = null;

    /**
     * Button which allows the removal of tokens in the current game situation.
     */
    private AtomicaToggleButton clearButton = null;

    /**
     * Button which allows the removal of all tokens in the current game
     * situation.
     */
    private AtomicaButton clearAllButton = null;

    // /////////// VIEW SECTION /////////////

    /**
     * Creates an editor panel with the settings of the given editor object.
     *
     * @param editor an editor instance
     * @param al     a listener where to send ui notifications to
     */
    public EditorPanel(Editor editor, ActionListener al) {
        super();

        this.editor = editor;
        editor.addObserver(this);

        setLayout(new BorderLayout());

        // add tools panel for holding scores, level, etc...
        add(getConfigurationPanel(), BorderLayout.WEST);

        // add bottom panel
        add(getBottomPanel(al), BorderLayout.SOUTH);

        // add board panel
        add(getBoardPanel(), BorderLayout.CENTER);
    }

    /**
     * Removes all tokens from the game situation.
     */
    private void clearAll() {
        editor.clearSituation();
    }

    /**
     * Returns the board panel displaying the game situation. It initializes it
     * at the first call.
     *
     * @return the board panel displaying the game situation
     */
    private AtomicaPanel getBoardPanel() {
        if (boardPanel == null) {
            boardPanel = new EditorBoardPanel(editor);
            boardPanel.setPreferredSize(new Dimension(250, 300));
        }

        return boardPanel;
    }

    /**
     * Returns the bottom panel holding the load, save and back button.
     * Initializes at first call.
     *
     * @param al a listener to send ui notifications to
     * @return the bottom panel holding the load, save and back button
     */
    private AtomicaPanel getBottomPanel(ActionListener al) {
        if (bottomPanel == null) {
            bottomPanel = new AtomicaPanel();

            // set the layout
            final FlowLayout flowLayout = new FlowLayout();
            flowLayout.setVgap(10);
            flowLayout.setHgap(10);
            flowLayout.setAlignment(FlowLayout.LEFT);
            bottomPanel.setLayout(flowLayout);
            bottomPanel.setBorder(new MatteBorder(3, 0, 0, 0, new Color(50, 50,
                    50)));

            // Back Button
            final AtomicaButton backButton = new AtomicaButton();
            backButton.addActionListener(al);
            backButton.setActionCommand(ACTION_CMD_CLOSE);
            backButton.setText(Language.getString("back"));
            bottomPanel.add(backButton);

            // Load Button
            final AtomicaButton loadButton = new AtomicaButton();
            loadButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    loadGame();
                }
            });
            loadButton.setText(Language.getString("loadGame"));
            bottomPanel.add(loadButton);

            // Save Button
            final AtomicaButton saveButton = new AtomicaButton();
            saveButton.setText(Language.getString("saveGame"));
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    saveGame();
                }
            });
            bottomPanel.add(saveButton);
        }

        return bottomPanel;
    }

    /**
     * Returns the panel holding the buttons for token placement and removal. It
     * initializes it at the first call.
     *
     * @return the panel for holding the buttons for token placement and removal
     */
    private AtomicaPanel getToolButtonsPanel() {
        if (toolButtonsPanel == null) {
            toolButtonsPanel = new AtomicaPanel();
            final FlowLayout flowLayout = new FlowLayout();
            flowLayout.setVgap(3);
            flowLayout.setHgap(3);
            toolButtonsPanel.setLayout(flowLayout);

            // group the toggle buttons.
            tokenButtonsGroup = new ButtonGroup();

            // for each color two buttons will be initialized. one for the atom
            // and one for the indicator.
            for (int i = 0; i < Level.MAX_NUMBEROFCOLORS; i++) {
                // first the atom button
                AtomToken atom = new AtomToken(i);

                final AtomicaToggleButton atomButton = new AtomicaToggleButton();
                atomButton.setIcon(new ImageIcon(ImageLoader.getInstance()
                        .getTokenImage(atom).getScaledInstance(10, 10,
                                Image.SCALE_SMOOTH)));
                atomButton.setData(atom);
                tokenButtons.add(atomButton);
                tokenButtonsGroup.add(atomButton);

                // listen to the click event
                atomButton.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        selectedTokenChanged((Token) atomButton.getData());
                    }
                });

                // second the indicator button
                IndicatorToken indicator = new IndicatorToken(i);

                final AtomicaToggleButton indicatorButton = new AtomicaToggleButton();
                indicatorButton.setIcon(new ImageIcon(ImageLoader.getInstance()
                        .getTokenImage(indicator).getScaledInstance(10, 10,
                                Image.SCALE_SMOOTH)));
                indicatorButton.setData(indicator);
                tokenButtons.add(indicatorButton);
                tokenButtonsGroup.add(indicatorButton);

                // listen to the click event
                indicatorButton.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        selectedTokenChanged((Token) indicatorButton.getData());
                    }
                });
            }

            // add the clear button for individual removal of tokens
            clearButton = new AtomicaToggleButton();
            // listen to the click event
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    selectedTokenChanged(null);
                }
            });
            clearButton.setIcon(new ImageIcon(ImageLoader.getInstance()
                    .getDeleteCursorImage().getScaledInstance(10, 10,
                            Image.SCALE_SMOOTH)));
            tokenButtonsGroup.add(clearButton);

            // add the button for the removal of all tokens
            clearAllButton = new AtomicaButton();
            clearAllButton.setPreferredSize(new Dimension(100, 20));
            clearAllButton.setFont(new Font("", Font.BOLD, 10));
            clearAllButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    clearAll();
                }
            });
            clearAllButton.setText(Language.getString("resetAll"));

            // depending the current number of colors the
            // buttons will also be added to the panel.
            updateToolButtonsPanel();
        }

        return toolButtonsPanel;
    }

    /**
     * Returns the configuration panel holding the configuration options like
     * the board size or the number of colors. Initializes it at first call.
     *
     * @return the configuration panel
     */
    private AtomicaPanel getConfigurationPanel() {
        if (configurationPanel == null) {
            configurationPanel = new AtomicaPanel();

            configurationPanel.setLayout(new BorderLayout());
            configurationPanel.setPreferredSize(new Dimension(120, 300));
            configurationPanel.setMinimumSize(new Dimension(0, 400));

            // set up the north panel holding the panel with the board size
            final AtomicaPanel northConfPanel = new AtomicaPanel();
            northConfPanel.setLayout(new GridLayout(2, 0));
            northConfPanel.setPreferredSize(new Dimension(200, 140));
            configurationPanel.add(northConfPanel, BorderLayout.NORTH);

            // set up the panel holding the board size options
            final AtomicaPanel boardSizePanel = new AtomicaPanel();
            northConfPanel.add(boardSizePanel);

            // set up the label for board size
            final AtomicaLabel boardSizeLabel = new AtomicaLabel();
            boardSizeLabel.setText(Language.getString("gameBoardSize"));
            boardSizePanel.add(boardSizeLabel);

            // set up the text field holding the number of rows of the board
            rowsTextField = new AtomicaNumberSpinner(editor.getRows(),
                    Board.MIN_ROWS, Board.MAX_ROWS);
            rowsTextField.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    editor.changeRows(rowsTextField.getIntValue());
                }
            });
            boardSizePanel.add(rowsTextField);

            // set up the label for the x between the rows and columns text
            // field
            final AtomicaLabel xLabel = new AtomicaLabel();
            xLabel.setText("X");
            boardSizePanel.add(xLabel);

            // set up the text field holding the number of columns of the board
            colsTextField = new AtomicaNumberSpinner(editor.getCols(),
                    Board.MIN_COLS, Board.MAX_COLS);
            colsTextField.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    editor.changeCols(colsTextField.getIntValue());
                }
            });
            boardSizePanel.add(colsTextField);

            // set up the panel holding the combobox for selecting the number of
            // colors.
            final AtomicaPanel colorsPanel = new AtomicaPanel();
            northConfPanel.add(colorsPanel);
            final JLabel colorsLabel = new JLabel();
            colorsLabel.setText(Language.getString("numberOfColors"));
            colorsPanel.add(colorsLabel);

            colorsComboBox = new AtomicaComboBox();

            for (int i = Level.MIN_NUMBEROFCOLORS; i <= Level.MAX_NUMBEROFCOLORS; i++) {
                Integer n = i;
                colorsComboBox.addItem(n);
                if (n.equals(editor.getCurrentNumberOfColors())) {

                    colorsComboBox.setSelectedItem(n);
                }
            }

            // since the number of visible buttons change when changing the
            // number of colors an event hast to be fired.
            colorsComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    Integer newNumberOfColors = (Integer) colorsComboBox
                            .getSelectedItem();
                    numberOfColorsChanged(newNumberOfColors);
                }
            });

            colorsPanel.add(colorsComboBox);

            // adds all buttons for selecting atoms and indicators considering
            // the number of colors
            configurationPanel.add(getToolButtonsPanel(), BorderLayout.CENTER);
        }

        return configurationPanel;
    }

    /**
     * Overwritten for displaying a background image.
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        // displays the background image
        g.drawImage(ImageLoader.getInstance().getBackgroundImage(), 0, 0,
                getWidth(), getHeight(), null);
        super.paintComponent(g);
    }

    /**
     * Due to observer pattern this method receives notification from the model
     * (Editor). It updates the controls for displaying the number of columns
     * and rows, the number of colors and the tool buttons allowing to place a
     * token. Also the game situation is being updated. It also forces the
     * editor board panel to repaint.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        updateToolButtonsPanel();

        if (rowsTextField.getIntValue() != editor.getRows()) {
            rowsTextField.setIntValue(editor.getRows());
        }

        if (colsTextField.getIntValue() != editor.getCols()) {
            colsTextField.setIntValue(editor.getCols());
        }

        int selectedIndex = editor.getCurrentNumberOfColors()
                - Level.MIN_NUMBEROFCOLORS;
        if (colorsComboBox.getSelectedIndex() != selectedIndex) {
            colorsComboBox.setSelectedIndex(selectedIndex);
        }

        repaint();
    }

    /**
     * Update the tool buttons allowing to place a token. It depends on the
     * number of colors.
     */
    private void updateToolButtonsPanel() {
        if (toolButtonsPanel != null) {
            toolButtonsPanel.removeAll();

            for (int i = 0; i < editor.getCurrentNumberOfColors(); i++) {
                toolButtonsPanel.add(tokenButtons.get(i * 2));
                toolButtonsPanel.add(tokenButtons.get((i * 2) + 1));
            }

            toolButtonsPanel.add(clearButton);
            toolButtonsPanel.add(clearAllButton);
        }

        toolButtonsPanel.validate();
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Opens a JFileChooser for selecting a file containing a serialized game
     * situation and loads that situation into the editor.
     */
    private void loadGame() {
        GameSituationSerializerUI serializer = new GameSituationSerializerUI(
                this);
        editor.setSituation(serializer.load());
    }

    /**
     * Sets the given number of colors.
     *
     * @param newNumberOfColors the new number of colors to be set
     */
    private void numberOfColorsChanged(int newNumberOfColors) {
        editor.setNumberOfColors(newNumberOfColors);
        tokenButtonsGroup.clearSelection();
        Logging.getLogger().log(java.util.logging.Level.INFO,
                "Available number of colors changed to " + newNumberOfColors);
    }

    /**
     * Opens a JFileChooser for saving the current game situation into a file.
     */
    private void saveGame() {
        GameSituationSerializerUI serializer = new GameSituationSerializerUI(
                this);
        serializer.save(editor.getSituation());
    }

    /**
     * When a new token of the tools button group is selected this method is
     * called with the selected token as parameter. If the token is null the
     * editor turns into DELETE mode. If the editor is currently in ADD mode,
     * the given token is the new active token ("token2Add").
     *
     * @param token
     */
    private void selectedTokenChanged(Token token) {
        // button delete was pressed if the given token is null, so switch
        // editor to DELETE mode
        if (token == null) {
            editor.setMode(EditorMode.DELETE);
        } else {
            // already checked button was pressed - uncheck
            if (token == editor.getToken2Add()) {
                // switch the current mode to MOVE
                editor.setMode(EditorMode.MOVE);
                tokenButtonsGroup.clearSelection();
            } else {
                // switch the current mode to ADD, since a new token was
                // selected
                editor.setMode(EditorMode.ADD);
                editor.setToken2Add(token);
            }
        }
    }
}
