package com.geolud.atomica.objects;

/**
 * Simple class holding the resource information for help files considering the
 * language.
 *
 * @author Georg Ludewig
 */
public class Help {
    /**
     * The supported languages of the game.
     *
     * @author Georg Ludewig
     */
    public enum HelpLanguage {
        /**
         * German language.
         */
        DE,
        /**
         * English language.
         */
        EN
    }

    ;

    /**
     * The url to the German help file.
     */
    private String url_de;

    /**
     * The url to the English help file.
     */
    private String url_en;

    /**
     * Alternative help information if help file could not be found.
     */
    private String info;

    /**
     * The singleton instance of the help.
     */
    private static Help instance = null;

    /**
     * Returns the only available instance used by the game.
     *
     * @return the only instance used by the game
     */
    public static Help getInstance() {
        if (instance == null) {
            instance = new Help();
        }

        return instance;
    }

    /**
     * Creates a help object. Due to singleton pattern this constructor is
     * private.
     */
    private Help() {
        url_de = "/help/de/index.html";
        url_en = "/help/en/index.html";
        info = "Atomica V 1.0 by Georg Ludewig. The help file could not be found.";
    }

    /**
     * Returns the text for alternative help information if the help file
     * resource could not be found.
     *
     * @return the text for alternative help information
     */
    public String getInfo() {
        return info;
    }

    /**
     * Returns the resource url for the requested language.
     *
     * @param lang the language of the help file
     * @return the url of the help in the requested language
     */
    public String getUrl(HelpLanguage lang) {
        String url = null;
        if (lang == HelpLanguage.DE) {
            url = url_de;
        } else if (lang == HelpLanguage.EN) {
            url = url_en;
        } else {
            url = url_en;
        }

        return url;
    }

}
