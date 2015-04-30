package com.geolud.atomica.ui.util.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all Panels in the game should be
 * implemented by using <code>AtomicaPanel</code>. It guarantees a common style.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaPanel extends JPanel {

    /**
     * The Default Constructor
     */
    public AtomicaPanel() {
        super();
        setOpaque(false);
        // setBackground (Color.WHITE);

        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(8);
        flowLayout.setHgap(8);
        setLayout(flowLayout);
    }

}
