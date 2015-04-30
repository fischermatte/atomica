package com.geolud.atomica.ui;

import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.components.AtomicaButton;
import com.geolud.atomica.ui.util.components.AtomicaComboBox;
import com.geolud.atomica.ui.util.components.AtomicaLabel;
import com.geolud.atomica.ui.util.components.AtomicaPanel;
import com.geolud.atomica.ui.util.language.Language;
import com.geolud.atomica.ui.util.language.LanguageItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Dialog which lets the user select a language. Supported languages are at
 * the moment English and German. It receives the list of supported languages
 * from com.geolud.atomica.ui.util.language.Language.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class LanguageDialog extends JDialog {
    /**
     * Action command which indicates that the language has changed.
     */
    public static String ACTION_CMD_LANGUAGECHANGED = "LanguageChanged";

    /**
     * The combobox which shows all supported languages.
     */
    private AtomicaComboBox languagesComboBox;

    private Language language;

    // /////////// VIEW SECTION /////////////

    /**
     * Creates the dialog.
     */
    public LanguageDialog(Language language, Frame owner,
                          final ActionListener al) {
        super(owner, true);

        this.language = language;

        // set the dialog not being resizable
        setResizable(false);

        // set its size
        setSize(400, 150);

        // set the title
        setTitle(Language.getString("selectLanguage"));

        // automatically hide and dispose the dialog after invoking any
        // registered WindowListener objects
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // get and add the background panel with the background image
        AtomicaPanel backgroundPanel = getBackgroundPanel();
        add(backgroundPanel);

        // add the panel with the combobox
        backgroundPanel.add(getSelectionPanel(), BorderLayout.CENTER);

        // add the bottom panel with the apply and cancel button
        backgroundPanel.add(getBottomPanel(al), BorderLayout.SOUTH);
    }

    /**
     * Returns the background panel with the background image.
     *
     * @return the background panel
     */
    private AtomicaPanel getBackgroundPanel() {
        AtomicaPanel panel = new AtomicaPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                g.drawImage(ImageLoader.getInstance().getBackgroundImage(), 0,
                        0, getWidth(), getHeight(), null);
                super.paintComponent(g);
            }
        };

        panel.setLayout(new BorderLayout());

        return panel;
    }

    /**
     * Returns the bottom panel holding an apply and cancel button.
     *
     * @param al a listener to notify when the apply or cancel button was
     *           pressed
     * @return the bottom panel
     */
    private AtomicaPanel getBottomPanel(final ActionListener al) {
        // create the bottom panel
        final AtomicaPanel bottomPanel = new AtomicaPanel();
        final FlowLayout applyCancelFlowLayout = new FlowLayout();
        applyCancelFlowLayout.setVgap(10);
        applyCancelFlowLayout.setHgap(10);
        applyCancelFlowLayout.setAlignment(FlowLayout.RIGHT);
        bottomPanel.setLayout(applyCancelFlowLayout);

        // add the apply button
        final AtomicaButton applyButton = new AtomicaButton();

        // when the apply button was pressed an event with the selected
        // LanuageItem is fired
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                LanguageItem langItem = (LanguageItem) languagesComboBox
                        .getSelectedItem();
                if (!langItem.getLocale().equals(language.getLanguage())) {
                    al.actionPerformed(new ActionEvent(langItem, 0,
                            ACTION_CMD_LANGUAGECHANGED));
                }

                dispose();
            }
        });
        applyButton.setText(Language.getString("apply"));
        bottomPanel.add(applyButton);

        // add the cancel button
        final AtomicaButton cancelButton = new AtomicaButton();
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setText(Language.getString("cancel"));
        bottomPanel.add(cancelButton);
        return bottomPanel;
    }

    /**
     * Returns the selection panel holding the combobox with the supported
     * languages.
     *
     * @return the selection panel holding the combobox with the supported
     * languages
     */
    private AtomicaPanel getSelectionPanel() {
        // create the selection panel
        final AtomicaPanel selectionPanel = new AtomicaPanel();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(20);
        flowLayout.setHgap(20);
        selectionPanel.setLayout(flowLayout);

        // set up a label
        final AtomicaLabel langLabel = new AtomicaLabel();
        langLabel.setText(Language.getString("language") + ": ");
        selectionPanel.add(langLabel);

        // add the combobox with the supported languages
        languagesComboBox = new AtomicaComboBox();
        for (String locale : language.getLanguages()) {
            LanguageItem langItem = new LanguageItem(locale, Language
                    .getString(locale));
            languagesComboBox.addItem(langItem);
            if (locale.equals(language.getLanguage())) {
                languagesComboBox.setSelectedItem(langItem);
            }
        }

        selectionPanel.add(languagesComboBox);
        return selectionPanel;
    }

}
