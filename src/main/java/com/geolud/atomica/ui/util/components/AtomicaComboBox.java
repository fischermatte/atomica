package com.geolud.atomica.ui.util.components;

import javax.swing.*;

/**
 * ComboBox in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all ComboBoxes in the game should
 * be implemented by using <code>AtomicaComboBox</code>. It guarantees a common
 * style.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaComboBox extends JComboBox {

    /**
     * The Default Constructor
     */
    public AtomicaComboBox() {
        super();
        // // setOpaque(false);
        // setForeground(new Color(255, 155, 4));
        // setBackground(Color.BLACK);
        // setBorder(new LineBorder(Color.black, 1, false));
        // setPreferredSize(new Dimension(120, 20));
    }

}
