package com.geolud.atomica.ui.util.components;

import javax.swing.*;
import java.awt.*;

/**
 * Readonly TextField in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all readonly TextFields in the
 * game should be implemented by using <code>AtomicaDisplay</code>. It
 * guarantees a common style.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaDisplay extends JTextField {
    /**
     * The Default Constructor
     */
    public AtomicaDisplay() {
        super();

        setBackground(Color.WHITE);
        // setBackground(new Color(25, 25, 25));
        // setBorder(new LineBorder(new Color(255, 155, 4), 1, false));
        // setForeground(new Color(255, 155, 4));
        // setFont(new Font("", Font.BOLD, 12));
        setEditable(false);
    }
}
