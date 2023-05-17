package com.deckerben.utilities.comtest;

import com.deckerben.component.SimpleImagePainter;
import com.deckerben.utilities.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ComponentTester extends JPanel implements Utility {

    //Felder
    private final Dimension size = new Dimension(200,200);
    private final SimpleImagePainter painter = new SimpleImagePainter((BufferedImage) collageImage(genRandomImage(4,size),size));

    //Konstruktor
    public ComponentTester(){
        setLayout(null);
        painter.setBounds(50,50,size.width,size.height);
        add(painter);
    }

    public static Image[] genRandomImage(int num, Dimension imgSize){
        Image[] images = new BufferedImage[num];
        Graphics g;
        for (int i = 0; i < num; i++){
            images[i] = new BufferedImage(imgSize.width,imgSize.height,BufferedImage.TYPE_INT_RGB);
            g = images[i].getGraphics();
            g.setColor(new Color((int)((Math.random())*16777215)));
            g.fillRect(0,0, imgSize.width, imgSize.height);
            g.dispose();
        }
        return images;
    }

    public static Image collageImage(Image[] inputs, Dimension outputSize){
        Image collage = new BufferedImage(outputSize.width,outputSize.height,BufferedImage.TYPE_INT_RGB);
        Graphics g = collage.getGraphics();
        int rowLength = (int)Math.ceil(Math.sqrt(inputs.length));   //Rastergröße ermitteln, wird immer Quadrat
        Dimension imgageSize = new Dimension((int)Math.ceil((double)outputSize.width/(double)rowLength), (int)Math.ceil((double)outputSize.height/(double)rowLength));
        int activeImage = 0;
        for (int y = 0; y < rowLength && activeImage < inputs.length; y++){
            for (int x = 0; x <rowLength && activeImage < inputs.length; x++){
                g.drawImage(inputs[activeImage],x* imgageSize.width,y* imgageSize.width,imgageSize.width, imgageSize.height, null);
                activeImage++;
            }
        }
        g.dispose();
        return collage;
    }

    //Overrides von Resettable
    @Override
    public boolean canReset() {
        return true;
    }

    @Override
    public void resetCode() {
        painter.setImage((BufferedImage) collageImage(genRandomImage(9,size),size));
        painter.updateUI();
        updateUI();
    }

    //Overrides von Utility
    @Override
    public String getUtilitiyName() {
        return "ComponentTester";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new ComponentTester();
    }
}
