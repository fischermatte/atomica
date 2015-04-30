package com.geolud.atomica.ui.util.components;

import javax.swing.*;

/**
 * ToggleButton in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all ToggleButtons in the game
 * should be implemented by using <code>AtomicaToggleButton</code>. It
 * guarantees a common style.
 * </p>
 * <p/>
 * <p>
 * The Class <code>AtomicaToggleButton</code> also owns a field
 * <code>data</code> for holding additional information.
 * </p>
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaToggleButton extends JToggleButton {
    /**
     * This field can be used to store additional information.
     */
    private Object data;

    /**
     * The Default Constructor.
     */
    public AtomicaToggleButton() {
        super();

        // setBackground(new Color(60, 0, 0));
        // setBorder(new LineBorder(new Color(255, 155, 4), 1, false));
        // setForeground(new Color(255, 155, 4));
        // setFont(new Font("", Font.BOLD, 12));
        // setPreferredSize(new Dimension(100, 25));
        // setOpaque(true);
    }

    /**
     * @return additional information of this component
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets additional information for this component.
     *
     * @param data any additional information
     */
    public void setData(Object data) {
        this.data = data;
    }
}
