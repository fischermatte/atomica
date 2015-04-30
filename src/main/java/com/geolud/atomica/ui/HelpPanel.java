package com.geolud.atomica.ui;

import com.geolud.atomica.objects.Help;
import com.geolud.atomica.objects.Help.HelpLanguage;
import com.geolud.atomica.ui.util.ImageLoader;
import com.geolud.atomica.ui.util.components.AtomicaButton;
import com.geolud.atomica.ui.util.components.AtomicaPanel;
import com.geolud.atomica.ui.util.language.Language;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

/**
 * Displays the html help considering the language.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class HelpPanel extends AtomicaPanel {
    /**
     * Action command which indicates that the help shall be closed.
     */
    public static final String ACTION_CMD_CLOSE = "closeHelp";

    /**
     * The help object holding the url to the help file.
     */
    private Help help;

    // /////////// VIEW SECTION /////////////

    /**
     * Creates a help panel displaying the help file given by the url holding
     * the <code>help</code> parameter.
     *
     * @param help a help object
     * @param al   a listener to fire ui notifications to
     */
    public HelpPanel(Help help, ActionListener al) {
        super();

        this.help = help;

        setLayout(new BorderLayout());

        // add the help pane which shows the html help file
        add(getHelpPane(), BorderLayout.CENTER);

        // add the bottom panel containing a back button
        add(getBottomPanel(al), BorderLayout.SOUTH);
    }

    /**
     * Returns the bottom panel containing a back button.
     *
     * @param al a listener to fire ui notifications to
     * @return the bottom panel
     */
    private AtomicaPanel getBottomPanel(ActionListener al) {
        // create a panel
        final AtomicaPanel bottomPanel = new AtomicaPanel();

        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);
        flowLayout.setAlignment(FlowLayout.LEFT);
        bottomPanel.setLayout(flowLayout);
        bottomPanel
                .setBorder(new MatteBorder(3, 0, 0, 0, new Color(50, 50, 50)));

        // add the back button to the panel
        final AtomicaButton backButton = new AtomicaButton();
        backButton.addActionListener(al);
        backButton.setActionCommand(ACTION_CMD_CLOSE);
        backButton.setText(Language.getString("back"));
        bottomPanel.add(backButton);

        return bottomPanel;
    }

    /**
     * Returns the text pane showing the html help file.
     *
     * @return the text pane showing the html help file
     */
    private JScrollPane getHelpPane() {
        JTextPane helpTextPane = new JTextPane();
        helpTextPane.setEditable(false);
        helpTextPane.setContentType("text/html");

        HelpLanguage helpLang = HelpLanguage.EN;
        if (Language.getInstance().getLanguage().equals(Language.EN)) {
            helpLang = HelpLanguage.EN;
        } else if (Language.getInstance().getLanguage().equals(Language.DE)) {
            helpLang = HelpLanguage.DE;
        }

        try {
            URL url = this.getClass().getResource(help.getUrl(helpLang));
            helpTextPane.setPage(url);
        } catch (IOException e) {
            helpTextPane.setText(help.getInfo());
            e.printStackTrace();
        }

        JScrollPane helpScrollPane = new JScrollPane();
        helpScrollPane.setViewportView(helpTextPane);

        return helpScrollPane;
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
}
