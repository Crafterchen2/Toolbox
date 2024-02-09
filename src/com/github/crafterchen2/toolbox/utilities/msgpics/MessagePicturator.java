package com.github.crafterchen2.toolbox.utilities.msgpics;

import com.github.crafterchen2.toolbox.Resettable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MessagePicturator implements Resettable {

    //Feld
    private Dimension preferredRatio;
    private final Dimension resetRatio;

    //Listener

    //Konstruktoren
    public MessagePicturator(){
        this(new Dimension(1,1));
    }

    public MessagePicturator(Dimension newPreferredRatio){
        resetRatio = new Dimension(newPreferredRatio);
        reset();
    }

    //Methoden
    ////Konvertierungslogik
    //////Text zu Bild
    public BufferedImage msgToPicture(String msg){
        double ratio = (double)preferredRatio.width / (double)preferredRatio.height;
        double pixelN = getPixelAmount(msg);
        int picHeight = (int) Math.ceil(Math.sqrt(pixelN/ratio));
        int picWidth = (int) Math.ceil((Math.sqrt(pixelN/ratio))*ratio);
        BufferedImage rv = new BufferedImage(picWidth,picHeight,BufferedImage.TYPE_INT_RGB);
        int n = 0;
        Color rgbColor;
        int[] rgbValues = new int[3];
        for (int y = 0; y < rv.getHeight(); y++) {
            for (int x = 0; x < rv.getWidth(); x++) {
                //Setze den Pixel zurück
                rgbValues[0] = 0;
                rgbValues[1] = 0;
                rgbValues[2] = 0;
                try {
                    for (int v = 0; v < rgbValues.length; v++) {
                        rgbValues[v] = Math.min(msg.charAt(v+n),255);
                    }
                }   catch (Exception ignored){

                }   finally {
                    n += 3;
                }
                rgbColor = new Color(rgbValues[0],rgbValues[1],rgbValues[2]);
                rv.setRGB(x,y,rgbColor.getRGB());
            }
        }
        return rv;
    }

    //////Bild zu Text
    public static String pictureToMsg(BufferedImage img){
        StringBuilder rv = new StringBuilder();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                rv.append(colorToWord(img.getRGB(x,y)));
            }
        }
        return rv.toString();
    }

    //////Farbe zu Wort (3 Character)
    public static String colorToWord(Color color){
        StringBuilder rv = new StringBuilder();
        if(color.getRed() > 0) rv.append((char) color.getRed());
        if(color.getGreen() > 0) rv.append((char) color.getGreen());
        if(color.getBlue() > 0) rv.append((char) color.getBlue());
        return rv.toString();
    }

    public static String colorToWord(int rgb){
        return colorToWord(new Color(rgb));
    }

    //Getter
    public static int getPixelAmount(String msg){
        return Math.max((int) Math.ceil(msg.length()/3.0),1);
    }

    @SuppressWarnings("unused")
    public Dimension getPreferredRatio() {
        return preferredRatio;
    }

    //Setter
    public void setPreferredRatio(Dimension preferredRatio) {
        this.preferredRatio = preferredRatio;
    }

    //Maker

    //Overrides aus
    ////Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode(){
        preferredRatio = resetRatio;
    }

}
