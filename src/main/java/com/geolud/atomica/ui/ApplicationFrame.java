package com.geolud.atomica.ui;

import com.geolud.atomica.logic.Editor;
import com.geolud.atomica.logic.Game;
import com.geolud.atomica.objects.Application;
import com.geolud.atomica.objects.GameSituation;
import com.geolud.atomica.objects.Help;
import com.geolud.atomica.ui.util.GameSituationSerializerUI;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.components.AtomicaButton;
import com.geolud.atomica.ui.util.components.AtomicaLabel;
import com.geolud.atomica.ui.util.components.AtomicaPanel;
import com.geolud.atomica.ui.util.language.Language;
import com.geolud.atomica.ui.util.language.LanguageItem;
import com.geolud.atomica.util.logging.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;

/**
 * This class represents the main window of the application. Depending on the
 * selected navigation option (new game, load game, editor,...) it displays the
 * required panel in it's content pane.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class ApplicationFrame extends javax.swing.JFrame implements
        ActionListener {

    /**
     * Holds the application object itself.
     */
    private Application app = null;

    /**
     * The main panel with the options for a new game, editor, help etc.
     */
    private JPanel mainPanel = null;

    /**
     * The panel which is about to displayed (game, editor, options).
     */
    private JPanel activePanel = null;

    /**
     * Action command which indicates that a new game shall be started.
     */
    private static String ACTION_CMD_NEW_GAME = "NewGame";

    /**
     * Action command which indicates that a game shall be loaded.
     */
    private static String ACTION_CMD_LOAD_GAME = "LoadGame";

    /**
     * Action command which indicates that the editor shall be started.
     */
    private static String ACTION_CMD_EDITOR = "Editor";

    /**
     * Action command for editing the game settings.
     */
    private static String ACTION_CMD_GAMESETTINGS = "Options";

    /**
     * Action command which indicates that the help shall be displayed.
     */
    private static String ACTION_CMD_HELP = "Help";

    /**
     * Action command which indicates that a language shall be selected.
     */
    private static String ACTION_CMD_LANGUAGE = "Language";

    /**
     * Action command which indicates that the application shall be closed.
     */
    private static String ACTION_CMD_EXIT = "Exit";

    // /////////// VIEW SECTION /////////////

    /**
     * Creates an application frame with the settings given by <code>app</code>.
     */
    public ApplicationFrame(Application app) {
        super(Language.getString("com/geolud/atomica"));

        this.app = app;

        try {
            final BorderLayout borderLayout = new BorderLayout();
            getContentPane().setLayout(borderLayout);

            // sets application image from resources
            initTitleImage();

            // set startup panel
            setActivePanel(getMainPanel());

            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            pack();

            setSize(650, 580);
            setMinimumSize(new Dimension(650, 580));
        } catch (Exception e) {
            Logging.getLogger().log(Level.SEVERE,
                    "Fehler beim initialisieren der GUI.", this);
            e.printStackTrace();
        }
    }

    /**
     * Returns the main panel containing all navigation options like playing a
     * new game, loading an existing game, editing a game situation, opening the
     * help, change the language and closing the application.
     *
     * @return the main panel with all navigation options
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new AtomicaPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.drawImage(ImageLoader.getInstance().getBackgroundImage(),
                            0, 0, getWidth(), getHeight(), null);
                }
            };

            mainPanel.setPreferredSize(new Dimension(400, 300));
            mainPanel.setLayout(new BorderLayout());

            // the top panel with title image
            AtomicaPanel labelPanel = new AtomicaPanel() {
            };
            labelPanel.setPreferredSize(new Dimension(0, 75));
            final FlowLayout labelFlowLayout = new FlowLayout();
            labelFlowLayout.setVgap(30);
            labelFlowLayout.setHgap(20);
            labelFlowLayout.setAlignment(FlowLayout.CENTER);
            labelPanel.setLayout(labelFlowLayout);
            labelPanel.setPreferredSize(new Dimension(100, 120));
            mainPanel.add(labelPanel, BorderLayout.NORTH);

            // add the title text/image with name of the application
            JLabel titleLabel = null;
            Image img = ImageLoader.getInstance().getTitleHeaderImage();
            if (img != null) {
                Image imgScaled = img.getScaledInstance(250, 55, Image.SCALE_SMOOTH);
                titleLabel = new JLabel(new ImageIcon(imgScaled));
            } else {
                titleLabel = new JLabel(Language.getString("com/geolud/atomica"));
            }
            labelPanel.add(titleLabel);


            // create the panel which holds the navigation options
            AtomicaPanel navigationPanel = new AtomicaPanel();
            final FlowLayout centerFlowLayout = new FlowLayout();
            navigationPanel.setLayout(centerFlowLayout);
            mainPanel.add(navigationPanel, BorderLayout.CENTER);

            // put a grid panel on top of the navigation panel
            AtomicaPanel navigationGridPanel = new AtomicaPanel();
            navigationGridPanel.setLayout(new GridLayout(7, 0));
            navigationPanel.add(navigationGridPanel);

            // NEW GAME: place a button for the new game option
            AtomicaPanel newGamePanel = new AtomicaPanel();
            navigationGridPanel.add(newGamePanel);
            AtomicaButton newGameButton = new AtomicaButton();
            newGameButton.setPreferredSize(new Dimension(150, 30));
            newGameButton.setMaximumSize(new Dimension(0, 0));
            newGameButton.setText(Language.getString("newGame"));
            newGameButton.addActionListener(this);
            newGameButton.setActionCommand(ACTION_CMD_NEW_GAME);
            newGamePanel.add(newGameButton);

            // LOAD GAME: place a button for the load game option
            AtomicaPanel loadGamePanel = new AtomicaPanel();
            navigationGridPanel.add(loadGamePanel);
            AtomicaButton loadGameButton = new AtomicaButton();
            loadGameButton.setPreferredSize(new Dimension(150, 30));
            loadGameButton.setText(Language.getString("loadGame"));
            loadGameButton.addActionListener(this);
            loadGameButton.setActionCommand(ACTION_CMD_LOAD_GAME);
            loadGamePanel.add(loadGameButton);

            // EDITOR: place a button for the editor option
            AtomicaPanel editorPanel = new AtomicaPanel();
            navigationGridPanel.add(editorPanel);
            AtomicaButton editorButton = new AtomicaButton();
            editorButton.setPreferredSize(new Dimension(150, 30));
            editorButton.setText(Language.getString("editor"));
            editorButton.addActionListener(this);
            editorButton.setActionCommand(ACTION_CMD_EDITOR);
            editorPanel.add(editorButton);

            // GAME SETTINGS: place a button for the game settings option
            AtomicaPanel gameSettingsPanel = new AtomicaPanel();
            navigationGridPanel.add(gameSettingsPanel);
            AtomicaButton gameSettingsButton = new AtomicaButton();
            gameSettingsButton.setPreferredSize(new Dimension(150, 30));
            gameSettingsButton.setText(Language.getString("gameSettings"));
            gameSettingsButton.addActionListener(this);
            gameSettingsButton.setActionCommand(ACTION_CMD_GAMESETTINGS);
            gameSettingsPanel.add(gameSettingsButton);

            // HELP: place a button for the help option
            AtomicaPanel helpPanel = new AtomicaPanel();
            helpPanel.setOpaque(false);
            helpPanel.setOpaque(false);
            navigationGridPanel.add(helpPanel);
            AtomicaButton helpButton = new AtomicaButton();
            helpButton.setPreferredSize(new Dimension(150, 30));
            helpButton.setText(Language.getString("help"));
            helpButton.addActionListener(this);
            helpButton.setActionCommand(ACTION_CMD_HELP);
            helpPanel.add(helpButton);

            // LANGUAGE: place a button for the language option
            AtomicaPanel languagePanel = new AtomicaPanel();
            navigationGridPanel.add(languagePanel);
            AtomicaButton languageButton = new AtomicaButton();
            languageButton.setPreferredSize(new Dimension(150, 30));
            languageButton.setText(Language.getString("language"));
            languageButton.addActionListener(this);
            languageButton.setActionCommand(ACTION_CMD_LANGUAGE);
            languagePanel.add(languageButton);

            // EXIT: place a button for the exit option
            AtomicaPanel exitPanel = new AtomicaPanel();
            navigationGridPanel.add(exitPanel);
            AtomicaButton exitButton = new AtomicaButton();
            exitButton.setPreferredSize(new Dimension(150, 30));
            exitButton.setText(Language.getString("exit"));
            exitButton.addActionListener(this);
            exitButton.setActionCommand(ACTION_CMD_EXIT);
            exitPanel.add(exitButton);

            // Exit Button
            AtomicaPanel bottomPanel = new AtomicaPanel();
            bottomPanel.setPreferredSize(new Dimension(50, 50));
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setVgap(15);
            bottomPanel.setLayout(flowLayout);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            AtomicaLabel copyrightLabel = new AtomicaLabel();
            copyrightLabel.setText(Language.getString("copyright"));
            copyrightLabel.setFont(new Font("", Font.PLAIN, 10));
            copyrightLabel.setVerticalAlignment(SwingConstants.CENTER);
            bottomPanel.add(copyrightLabel);
        }

        return mainPanel;
    }

    /**
     * Removes the current panel from the content pane and adds the given panel
     * to be the current visible.
     *
     * @param newPanel the new panel to be displayed in the content pane.
     */
    private void setActivePanel(JPanel newPanel) {
        getContentPane().add(newPanel, BorderLayout.CENTER);
        newPanel.repaint();
        if (activePanel != null) {
            getContentPane().remove(activePanel);
        }
        this.validate();
        this.activePanel = newPanel;
    }

    /**
     * Loads and sets the title icon of the frame.
     */
    private void initTitleImage() {
        Image img = null;
        try {
            URL url;
            url = ApplicationFrame.class.getResource("/images/Atomica.png");
            img = new ImageIcon(url).getImage();
        } catch (RuntimeException e) {
            Logging.getLogger().log(Level.SEVERE, "Failed to load Atomica.png");
        }

        if (img != null) {
            setIconImage(img);
        }
    }

    // /////////// CONTROLLER SECTION /////////////

    /**
     * Starts the application.
     *
     * @param args optional arguments - not considered
     */
    public static void main(String[] args) {
        Application app = new Application();
        ApplicationFrame.startGUI(app);
    }

    /**
     * Creates and displays a new ApplicationFrame instance with the settings
     * given by <code>app</code>.
     *
     * @param app the application settings to start the gui with
     */
    public static void startGUI(final Application app) {
        // start the gui asynchrony
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Language.getInstance().switchLanguage(app.getLanguage());
                ApplicationFrame appFrame = new ApplicationFrame(app);
                appFrame.setLocationRelativeTo(null);
                appFrame.setVisible(true);
            }
        });
    }

    /**
     * Saves the settings of the given frame and disposes it.
     *
     * @param frame the application frame to close
     */
    private static void closeGUI(ApplicationFrame frame) {
        frame.app.savePrefs();
        Logging.getLogger().log(Level.INFO, "Dispose ApplicationFrame");
        frame.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACTION_CMD_LANGUAGE)) {
            onLanguage();
        } else if (e.getActionCommand().equals(ACTION_CMD_NEW_GAME)) {
            onNewGame();
        } else if (e.getActionCommand().equals(ACTION_CMD_LOAD_GAME)) {
            onLoadGame();
        } else if (e.getActionCommand().equals(ACTION_CMD_EDITOR)) {
            onEditor();
        } else if (e.getActionCommand().equals(ACTION_CMD_GAMESETTINGS)) {
            onGameSettings();
        } else if (e.getActionCommand().equals(ACTION_CMD_HELP)) {
            onHelp();
        } else if (e.getActionCommand().equals(GamePanel.ACTION_CMD_QUITGAME)) {
            onQuitGame();
        } else if (e.getActionCommand()
                .equals(GameSettingsPanel.ACTION_CMD_CLOSE)) {
            onCloseGameSettings();
        } else if (e.getActionCommand().equals(EditorPanel.ACTION_CMD_CLOSE)) {
            onCloseEditor();
        } else if (e.getActionCommand().equals(HelpPanel.ACTION_CMD_CLOSE)) {
            onCloseHelp();
        } else if (e.getActionCommand().equals(ACTION_CMD_EXIT)) {
            onExit();
        } else if (e.getActionCommand().equals(
                LanguageDialog.ACTION_CMD_LANGUAGECHANGED)) {
            onLanguageChanged((LanguageItem) e.getSource());
        }
    }

    /**
     * Restarts the gui with the given language.
     *
     * @param langItem the new language the gui shall be displayed in
     */
    private void onLanguageChanged(LanguageItem langItem) {
        Logging.getLogger().log(Level.INFO,
                "Change Language to " + app.getLanguage());
        app.setLanguage(langItem.getLocale());
        closeGUI(this);
        startGUI(this.app);
    }

    /**
     * Disposes the current frame and exits the program.
     */
    private void onExit() {
        setVisible(false);
        closeGUI(this);
    }

    /**
     * Closes the help panel and sets the main panel active.
     */
    private void onCloseHelp() {
        Logging.getLogger().log(Level.INFO, "Back to main panel");
        setTitle(Language.getString("com/geolud/atomica"));
        setActivePanel(getMainPanel());
    }

    /**
     * Closes the editor panel and sets the main panel active.
     */
    private void onCloseEditor() {
        Logging.getLogger().log(Level.INFO, "Back to main panel");
        setTitle(Language.getString("com/geolud/atomica"));
        setActivePanel(getMainPanel());
    }

    /**
     * Closes the game settings panel and sets the main panel active.
     */
    private void onCloseGameSettings() {
        Logging.getLogger().log(Level.INFO, "Back to main panel");
        setTitle(Language.getString("com/geolud/atomica"));
        setActivePanel(getMainPanel());
    }

    /**
     * Quits the current game and sets the main panel active.
     */
    private void onQuitGame() {
        Logging.getLogger().log(Level.INFO, "Quit Game");
        setTitle(Language.getString("com/geolud/atomica"));
        setActivePanel(getMainPanel());
    }

    /**
     * Displays the help panel.
     */
    private void onHelp() {
        Logging.getLogger().log(Level.INFO, "Help");
        setTitle(Language.getString("com/geolud/atomica") + " - "
                + Language.getString("help"));
        HelpPanel helpPanel = new HelpPanel(Help.getInstance(), this);
        setActivePanel(helpPanel);
    }

    /**
     * Displays the game settings panel.
     */
    private void onGameSettings() {
        Logging.getLogger().log(Level.INFO, "GameSettings");
        setTitle(Language.getString("com/geolud/atomica") + " - "
                + Language.getString("gameSettings"));
        GameSettingsPanel gamePrefsPanel = new GameSettingsPanel(app
                .getGameSettings(), this);
        setActivePanel(gamePrefsPanel);
    }

    /**
     * Displays the editor.
     */
    private void onEditor() {
        Logging.getLogger().log(Level.INFO, "Editor");
        setTitle(Language.getString("com/geolud/atomica") + " - "
                + Language.getString("editor"));
        Editor gameEditor = new Editor(app.getEditorGameSettings());
        EditorPanel editorPanel = new EditorPanel(gameEditor, this);
        setActivePanel(editorPanel);
    }

    /**
     * Lets the user select a file of a serialized game situation. Starts the
     * game.
     */
    private void onLoadGame() {
        Logging.getLogger().log(Level.INFO, "Load Game");
        GameSituationSerializerUI serializer = new GameSituationSerializerUI(
                this);
        GameSituation gameSituation = serializer.load();
        if (gameSituation != null) {
            Game game = new Game(gameSituation);
            GamePanel gamePanel = new GamePanel(game, this);
            game.start();
            setActivePanel(gamePanel);
        }
    }

    /**
     * Starts a new game defined by the game settings.
     */
    private void onNewGame() {
        Logging.getLogger().log(Level.INFO, "New Game");
        Game game = new Game(app.getGameSettings());
        GamePanel gamePanel = new GamePanel(game, this);
        game.start();
        setActivePanel(gamePanel);
    }

    /**
     * Lets the user select a language.
     */
    private void onLanguage() {
        Logging.getLogger().log(Level.INFO, "Language");
        LanguageDialog langDialog = new LanguageDialog(Language.getInstance(),
                this, this);
        langDialog.setLocationRelativeTo(this);
        langDialog.setVisible(true);
    }
}
