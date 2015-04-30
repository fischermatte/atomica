package com.geolud.atomica.ui.util.components;

import com.geolud.atomica.util.logging.Logging;

import javax.swing.*;
import java.util.logging.Level;

/**
 * Spinner for Integer Values in Atomica Style.
 * <p/>
 * <p>
 * To simplify maintenance and change requests all Integer TextFields in the
 * game should be implemented by using <code>AtomicaNumberSpinner</code>. It
 * guarantees a common style.
 * </p>
 * <p/>
 * The <code>Class</code> also handles correct input/output values by
 * considering a min and max value.
 *
 * @author Georg Ludewig
 */
@SuppressWarnings("serial")
public class AtomicaNumberSpinner extends JSpinner {
    private int min;
    private int max;

    /**
     * Constructs a Spinner with 1 as Step Size.
     *
     * @param value the value to be displayed
     * @param min   the minimum value
     * @param max   the maximum value
     */
    public AtomicaNumberSpinner(int value, int min, int max) {
        super(new SpinnerNumberModel(value, min, max, 1));

        this.min = min;
        this.max = max;
    }

    /**
     * Constructs a Spinner with the specified value, minimum/maximum bounds,
     * and stepSize.
     *
     * @param value    the value to be displayed
     * @param min      the minimum value
     * @param max      the maximum value
     * @param stepSize the difference between elements of the sequence
     */
    public AtomicaNumberSpinner(int value, int min, int max, int stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));

        this.min = min;
        this.max = max;
    }

    /**
     * Returns the current value as integer.
     *
     * @return the current value
     */
    public int getIntValue() {
        int n = 0;

        try {
            Integer i = (Integer) getValue();
            n = i;
        } catch (Exception e) {
            Logging.getLogger().log(Level.SEVERE, e.getMessage());
            n = 0;
        }
        return n;
    }

    /**
     * Validates current value. Returns true if valid and false if not.
     *
     * @return true if the value is valid, false if not
     */
    public boolean isValidValue() {
        int n = 0;
        try {
            Integer i = (Integer) getValue();
            n = i;
        } catch (Exception e) {
            Logging.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }

        return n <= max && n >= min;

    }

    /**
     * Changes current value.
     *
     * @param number the new value
     */
    public void setIntValue(int number) {
        setValue(number);
    }

}
