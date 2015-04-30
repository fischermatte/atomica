package com.geolud.atomica.ui.util.language;

/**
 * Simple class which represents a language with it's caption and locale. The
 * toSting() method is overwritten so it will display the caption when using in
 * components.
 *
 * @author Georg Ludewig
 */
public class LanguageItem {
    /**
     * The locale for the language (for example "de" for German).
     */
    private String locale;

    /**
     * The caption of the language (for example "German" or "Deutsch").
     */
    private String caption;

    /**
     * Creates an Item with the given locale and caption.
     *
     * @param locale  the locale of the language
     * @param caption the caption of the language
     */
    public LanguageItem(String locale, String caption) {
        super();
        this.locale = locale;
        this.caption = caption;
    }

    /**
     * Returns the caption of the language item.
     *
     * @return the caption of the language item
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Returns the locale of the language item.
     *
     * @return the locale of the language item
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the caption of the language item.
     *
     * @param caption the caption of the language item
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Sets the locale of the language item.
     *
     * @param locale the locale of the language item
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Returns the caption.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return caption;
    }
}
