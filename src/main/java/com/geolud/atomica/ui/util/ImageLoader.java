package com.geolud.atomica.ui.util;

import com.geolud.atomica.objects.AtomToken;
import com.geolud.atomica.objects.IndicatorToken;
import com.geolud.atomica.objects.Token;
import com.geolud.atomica.ui.ApplicationFrame;
import com.geolud.atomica.util.logging.Logging;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Responsible for loading all images of the ui. This includes the token images
 * as well as the field and background image. Implemented as singleton.
 *
 * @author Georg Ludewig
 */
public class ImageLoader {
    /**
     * Returns the only instance of the ImageLoader (due to singleton pattern).
     *
     * @return the only instance
     */
    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }

        return instance;
    }

    /**
     * List of all atom images.
     */
    private ArrayList<Image> atomImages = null;

    /**
     * List of all indicator images.
     */
    private ArrayList<Image> indicatorImages = null;

    /**
     * The image displaying a field.
     */
    private Image fieldImage = null;

    /**
     * The image displaying the background.
     */
    private Image backgroundImage = null;

    /**
     * The image displaying the delete cursor.
     */
    private Image deleteCursorImage = null;

    /**
     * The image displaying the title header in the main menu.
     */
    private Image titleHeaderImage = null;

    /**
     * The only instance.
     */
    private static ImageLoader instance = null;

    /**
     * Due to singleton pattern the privat default constructor.
     */
    private ImageLoader() {

    }

    /**
     * Returns all atom images. At first call all atom images are being loaded
     * into memory.
     *
     * @return all atom images
     */
    private ArrayList<Image> getAtomImages() {
        if (atomImages == null) {
            atomImages = new ArrayList<Image>();
            atomImages.add(loadImage("/images/Atom1.png"));
            atomImages.add(loadImage("/images/Atom2.png"));
            atomImages.add(loadImage("/images/Atom3.png"));
            atomImages.add(loadImage("/images/Atom4.png"));
            atomImages.add(loadImage("/images/Atom5.png"));
            atomImages.add(loadImage("/images/Atom6.png"));
            atomImages.add(loadImage("/images/Atom7.png"));
            atomImages.add(loadImage("/images/Atom8.png"));
            atomImages.add(loadImage("/images/Atom9.png"));
            atomImages.add(loadImage("/images/Atom10.png"));
            atomImages.add(loadImage("/images/Atom11.png"));
            atomImages.add(loadImage("/images/Atom12.png"));
        }
        return atomImages;
    }

    /**
     * Returns the background image. At first call it is loaded into memory.
     *
     * @return the background image
     */
    public Image getBackgroundImage() {
        if (backgroundImage == null) {
            backgroundImage = loadImage("/images/Background.png");
        }

        return backgroundImage;
    }

    /**
     * Returns the image for displaying the delete cursor. At first call it is
     * loaded into memory.
     *
     * @return the delete cursor image
     */
    public Image getDeleteCursorImage() {
        if (deleteCursorImage == null) {
            deleteCursorImage = loadImage("/images/DeleteCursor.png");
        }

        return deleteCursorImage;
    }

    /**
     * Returns the image for displaying a field. At first call it is loaded from
     * the resources.
     *
     * @return the image for displaying a field
     */
    public Image getFieldImage() {
        if (fieldImage == null) {
            fieldImage = loadImage("/images/Field.png");
        }

        return fieldImage;
    }

    /**
     * Returns all indicator images. At first call all indicator images are
     * being loaded from the resources.
     *
     * @return all indicator images
     */
    private ArrayList<Image> getIndicatorImages() {
        if (indicatorImages == null) {
            indicatorImages = new ArrayList<Image>();
            indicatorImages.add(loadImage("/images/Indicator1.png"));
            indicatorImages.add(loadImage("/images/Indicator2.png"));
            indicatorImages.add(loadImage("/images/Indicator3.png"));
            indicatorImages.add(loadImage("/images/Indicator4.png"));
            indicatorImages.add(loadImage("/images/Indicator5.png"));
            indicatorImages.add(loadImage("/images/Indicator6.png"));
            indicatorImages.add(loadImage("/images/Indicator7.png"));
            indicatorImages.add(loadImage("/images/Indicator8.png"));
            indicatorImages.add(loadImage("/images/Indicator9.png"));
            indicatorImages.add(loadImage("/images/Indicator10.png"));
            indicatorImages.add(loadImage("/images/Indicator11.png"));
            indicatorImages.add(loadImage("/images/Indicator12.png"));
        }
        return indicatorImages;
    }

    /**
     * Returns the image for displaying the header in the main menu.
     *
     * @return the image for displaying the header in the main menu
     */
    public Image getTitleHeaderImage() {
        if (titleHeaderImage == null) {
            titleHeaderImage = loadImage("/images/AtomicaTitleHeader.png");
        }

        return titleHeaderImage;
    }

    /**
     * Returns the image for the given token.
     *
     * @param token the token the image is wanted for
     * @return the image for the given token
     */
    public Image getTokenImage(Token token) {
        Image img = null;
        if (token.getClass() == AtomToken.class) {
            img = getAtomImages().get(token.getColorIndex());
        } else if (token.getClass() == IndicatorToken.class) {
            img = getIndicatorImages().get(token.getColorIndex());
        }

        return img;
    }

    /**
     * Loads an image from the given path.
     *
     * @param path the path of the image
     * @return the image at the given path
     */
    private Image loadImage(String path) {
        Image img = null;
        try {
            URL url;
            url = ApplicationFrame.class.getResource(path);
            img = new ImageIcon(url).getImage();
        } catch (RuntimeException e) {
            Logging.getLogger().log(Level.SEVERE, "Failed to load " + path);
        }

        return img;
    }
}
