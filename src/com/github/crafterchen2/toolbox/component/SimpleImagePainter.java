package com.github.crafterchen2.toolbox.component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Verfügbare Modi:
 * 1: Originalgröße
 * 2: Bestimmte Größe
 * 3: Maximalzoom mit Verzerrung
 * 4: Maximalzoom ohne Verzerrung
 * 5: bestimmter Zoom ohne Verzerrung
 * 6: bestimmter Zoom mit Verzerrung
 * */
public class SimpleImagePainter extends JComponent {

    //Felder
    protected BufferedImage img;
    protected Dimension targetSize;
    protected Dimension targetOffset = new Dimension(0,0);
    protected boolean distorting;

    //Konstruktoren
    /**
     * Modus 1: Zeigt das Bild in Originalgröße an.
     * @param img Das Bild.
     */
    @SuppressWarnings("unused")
    public SimpleImagePainter(BufferedImage img) {
        setImage(img);
    }

    /**
     * Modus 2: Zeigt das Bild in einer bestimmten größe an.
     * @param img Das Bild.
     * @param targetSize Die gewünschte Größe.
     */
    @SuppressWarnings("unused")
    public SimpleImagePainter(BufferedImage img, Dimension targetSize) {
        setImage(img, targetSize);
    }

    /**
     * Modus 3 & 4: Zeigt das Bild in der so groß wie möglich an, basierend auf der Größe des Components.
     * @param img Das Bild.
     * @param distorting true: Keine Verzerrung des Bildes (-> Modus 3).
     *                   false: Erlaubt eine Verzerrung des Bildes (-> Modus 4).
     */
    public SimpleImagePainter(BufferedImage img, boolean distorting) {
        setImage(img, distorting);
    }

    /**
     * Modus 5: Zeigt das Bild mit einem bestimmten Zoom an, ohne es zu verzerren.
     * @param img Das Bild.
     * @param zoom Der gewünschte Zoomfaktor.
     */
    @SuppressWarnings("unused")
    public SimpleImagePainter(BufferedImage img, double zoom) {
        setImage(img, zoom);
    }

    /**
     * Modus 6: Zeigt das Bild mit einem bestimmten Zoom für die Höhe und die Breite an.
     * @param img Das Bild.
     * @param widthZoom Der Zoomfaktor für die Breite.
     * @param heightZoom Der Zoomfaktor für die Höhe.
     */
    @SuppressWarnings("unused")
    public SimpleImagePainter(BufferedImage img, double widthZoom, double heightZoom) {
        setImage(img, widthZoom, heightZoom);
    }

    //Bilddarstellungsvorbereitung
    /**
     * Zeigt das Bild in Originalgröße an.
     */
    public void showOriginal(){
        updateImgLayout(null);
    }

    /**
     * Modus 2: Ändert nur Darstellungsmodus. Zeigt das Bild in einer bestimmten größe an.
     * @param targetSize Die gewünschte Größe.
     */
    public void updateImgLayout(Dimension targetSize){
        if (targetSize == null) {
            this.targetSize = new Dimension(this.img.getWidth(),this.img.getHeight());
            this.distorting = false;
        }   else {
            this.targetSize = targetSize;
            setDistorting();
        }
        updateUI();
    }

    /**
     * Modus 3 & 4: Ändert nur Darstellungsmodus. Zeigt das Bild in der so groß wie möglich an, basierend auf der Größe des Components.
     * @param distorting true: Keine Verzerrung des Bildes (-> Modus 3).
     *                   false: Erlaubt eine Verzerrung des Bildes (-> Modus 4).
     */
    public void updateImgLayout(boolean distorting) {
        //Falls das Component noch nicht gemalt werden konnte, Originalgröße verwenden.
        //this.targetSize = (currentComSize != null)? currentComSize : new Dimension(this.img.getWidth(),this.img.getHeight());
        this.targetSize = (super.getSize() != null)? super.getSize() : new Dimension(this.img.getWidth(),this.img.getHeight());
        if (!distorting) {
            double factorWidth = (double) this.targetSize.width / this.img.getWidth();
            double factorHeight = (double) this.targetSize.height / this.img.getHeight();
            double factor = Math.min(factorWidth,factorHeight);
            updateImgLayout(factor); //Ab hier quasi Modus 5
        }   else {
            setDistorting();
            updateUI();
        }
    }

    /**
     * Modus 5: Ändert nur Darstellungsmodus. Zeigt das Bild mit einem bestimmten Zoom an, ohne es zu verzerren.
     * @param zoom Der gewünschte Zoomfaktor.
     */
    public void updateImgLayout(double zoom){
        updateImgLayout(zoom,zoom);
    }

    /**
     * Modus 6: Ändert nur Darstellungsmodus. Zeigt das Bild mit einem bestimmten Zoom für die Höhe und die Breite an.
     * @param widthZoom Der Zoomfaktor für die Breite.
     * @param heightZoom Der Zoomfaktor für die Höhe.
     */
    public void updateImgLayout(double widthZoom, double heightZoom) {
        this.targetSize = new Dimension((int) (widthZoom * this.img.getWidth()),(int) (heightZoom * this.img.getHeight()));
        setDistorting();
        updateUI();
    }

    ////Setter
    /**
     * Modus 1: Zeigt das neue Bild in Originalgröße an.
     * @param img Das neue Bild.
     */
    public void setImage(BufferedImage img) {
        this.img = img;
        showOriginal();
    }

    /**
     * Modus 2: Zeigt das neue Bild in einer bestimmten größe an.
     * @param img Das neue Bild.
     * @param targetSize Die gewünschte Größe.
     */
    public void setImage(BufferedImage img, Dimension targetSize) {
        this.img = img;
        updateImgLayout(targetSize);
    }

    /**
     * Modus 3 & 4: Zeigt das neue Bild in der so groß wie möglich an, basierend auf der Größe des Components.
     * @param img Das neue Bild.
     * @param distorting true: Keine Verzerrung des Bildes (-> Modus 3).
     *                   false: Erlaubt eine Verzerrung des Bildes (-> Modus 4).
     */
    public void setImage(BufferedImage img, boolean distorting) {
        this.img = img;
        updateImgLayout(distorting);
    }

    /**
     * Modus 5: Zeigt das neue Bild mit einem bestimmten Zoom an, ohne es zu verzerren.
     * @param img Das neue Bild.
     * @param zoom Der gewünschte Zoomfaktor.
     */
    public void setImage(BufferedImage img, double zoom){
        setImage(img,zoom,zoom);
    }

    /**
     * Modus 6: Zeigt das neue Bild mit einem bestimmten Zoom für die Höhe und die Breite an.
     * @param img Das neue Bild.
     * @param widthZoom Der Zoomfaktor für die Breite.
     * @param heightZoom Der Zoomfaktor für die Höhe.
     */
    public void setImage(BufferedImage img, double widthZoom, double heightZoom) {
        this.img = img;
        updateImgLayout(widthZoom, heightZoom);
    }

    private void setDistorting(){
        distorting = !((this.targetSize.width == this.img.getWidth())&&(this.targetSize.height == this.img.getHeight()));
    }

    /**
     * Tauscht das Bild aus, ohne den Darstellungsmodus zu ändern.
     * @param newImg Das neue Bild.
     */
    @SuppressWarnings("unused")
    public void exchangeImage(BufferedImage newImg){
        img = newImg;
        updateUI();
    }

    @SuppressWarnings("unused")
    public void setOffset(double xOffset, double yOffset){
        setOffset(new Dimension(
                (int) (getWidth()*xOffset) - (int) (targetSize.width*xOffset),
                (int) (getHeight()*yOffset) - (int) (targetSize.height*yOffset)));
    }

    @SuppressWarnings("unused")
    public void setOffset(int xOffset, int yOffset){
        setOffset(new Dimension(xOffset,yOffset));
    }

    public void setOffset(Dimension offset){
        targetOffset = offset;
    }

    //Getter
    public BufferedImage getImage(){
        return img;
    }

    @SuppressWarnings("unused")
    public boolean isDistorting() {
        return distorting;
    }

    //Overrides von JComponent
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Insets insets = getInsets();
        g.drawImage(img,
                targetOffset.width+insets.left,
                targetOffset.height+insets.top,
                targetSize.width-(insets.right+insets.left),
                targetSize.height-(insets.bottom+insets.top),
                null);
    }

    @Override
    public Dimension getPreferredSize() {
        return targetSize;
    }
}
