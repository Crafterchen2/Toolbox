package com.deckerben.utilities.msgpics.component;

import com.deckerben.component.SimpleImagePainter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ConverterImagePainter extends SimpleImagePainter {

    /*
     * Verfügbare Modi:
     * 1: Originalgröße
     * 2: Bestimmte Größe
     * 3: Maximalzoom mit Verzerrung
     * 4: Maximalzoom ohne Verzerrung
     * 5: Bestimmter Zoom ohne Verzerrung
     * 6: Bestimmter Zoom mit Verzerrung
     * */

    //Modus 1
    public ConverterImagePainter(BufferedImage img) {
        super(img);
    }

    //Modus 2
    public ConverterImagePainter(BufferedImage img, Dimension targetSize) {
        super(img, targetSize);
    }

    //Modus 3 & 4
    public ConverterImagePainter(BufferedImage img, boolean distorting) {
        super(img, distorting);
    }

    //Modus 5
    public ConverterImagePainter(BufferedImage img, double zoom) {
        super(img, zoom);
    }

    //Modus 6
    public ConverterImagePainter(BufferedImage img, double widthZoom, double heightZoom) {
        super(img, widthZoom, heightZoom);
    }
}