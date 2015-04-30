package com.geolud.atomica.util.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

/**
 * Wrapper class to handle logging. Holds a java.util.logging.Logger instance.
 *
 * @author Georg Ludewig
 */
public class Logging {
    /**
     * The only instance of a logger class.
     */
    private static Logger logger = null;

    /**
     * Returns the only instance of the logger. If no instance exist, a new
     * instance will be created and initialized.
     *
     * @return the only instance of the logger
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("AtomicaLogging");

            // set handler for file logging
            try {
                logger.setUseParentHandlers(false);

                // set handler for console logging
                ConsoleHandler ch = new ConsoleHandler();
                ch.setFormatter(new SingleLineFormatter());
                logger.addHandler(ch);

                // FileHandler fh;
                // fh = new FileHandler( "com.geolud.atomica.log" );
                // fh.setFormatter(new SimpleFormatter());
                // logger.addHandler(fh);
            } catch (Exception e) {
                System.out
                        .println("Initializing ConsoleHandler for Logging failed.");
            }
        }

        return logger;
    }
}
