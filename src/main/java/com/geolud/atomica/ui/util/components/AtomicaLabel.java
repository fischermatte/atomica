package com.geolud.atomica.ui.util.components;

//import java.awt.Color;

import javax.swing.*;

/**
 * Label in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all Labels in the game should be
 * implemented by using <code>AtomicaLabel</code>. It guarantees a common style.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaLabel extends JLabel {

    /**
     * The Default Constructor
     */
    public AtomicaLabel() {
        super();
        // setOpaque(false);
        // setForeground(new Color(255, 155, 4));
    }

}
