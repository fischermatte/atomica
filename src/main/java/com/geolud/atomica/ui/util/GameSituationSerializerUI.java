package com.geolud.atomica.ui.util;

import com.geolud.atomica.logic.GameSituationSerializer;
import com.geolud.atomica.objects.GameSituation;
import com.geolud.atomica.ui.util.language.Language;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Lets the user load and save a game situation by a JFileChooser.
 *
 * @author Georg Ludewig
 */
public class GameSituationSerializerUI {
    /**
     * The parent component.
     */
    private Component parent = null;

    /**
     * The game situation serializer object.
     */
    private GameSituationSerializer gameSituationSerializer;

    /**
     * Constructs a GameSituationSerializerUI for loading and saving game
     * situations.
     *
     * @param parent the parent component
     */
    public GameSituationSerializerUI(Component parent) {
        this.parent = parent;
        this.gameSituationSerializer = new GameSituationSerializer();
    }

    /**
     * Opens a JFileChooser for selecting a game situation and deserializes it.
     *
     * @return the deserialized game situation
     */
    public GameSituation load() {
        // Create a File Filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter(Language
                .getString("atomicaGameSituation"),
                GameSituationSerializer.ATOMICA_FILEEXTENSION);

        // Create a file chooser
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(Language.getString("loadGameSituation"));
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new File(gameSituationSerializer.getLastPath()));


        GameSituation gameSituation = null;

        // In response to a button click:
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(parent)) {
            gameSituation = gameSituationSerializer.load(fc.getSelectedFile()
                    .getAbsolutePath());
        }

        return gameSituation;
    }

    /**
     * Opens a JFileChooser for selecting a path. Stores the given game
     * situation at that path.
     *
     * @param gameSituation the situation to be serialized
     */
    public void save(GameSituation gameSituation) {
        // Create a File Filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter(Language
                .getString("atomicaGameSituation"),
                GameSituationSerializer.ATOMICA_FILEEXTENSION);

        // Create a file chooser
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(Language.getString("saveGameSituation"));
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new File(gameSituationSerializer.getLastPath()));

        // In response to a button click:
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(parent)) {
            gameSituationSerializer.save(gameSituation, fc.getSelectedFile()
                    .getAbsolutePath());
        }
    }

}
