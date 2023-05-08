package com.deckerben.utilities.comtest;

import com.deckerben.component.SimpleImagePainter;
import com.deckerpw.utilities.ConfigurableUtility;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ComponentTester extends JPanel implements ConfigurableUtility {

    JSONObject config;
    private Dimension size = new Dimension(200,200);
    private SimpleImagePainter painter = new SimpleImagePainter((BufferedImage) collageImage(genRandomImage(4,size),size));

    //Konstruktor
    public ComponentTester(){
        setLayout(null);
        painter.setBounds(50,50,size.width,size.height);
        add(painter);

        //bingCode();
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

    private static void bingCode() {
        //Code von Bing START
        try {
            // Get the path of the jar file
            String pathToJar = "C:/Users/decke/IdeaProjects/StackomatV2/out/artifacts/StackomatV2_jar/StackomatExec.jar";
            // Create a URL for the jar file
            URL jarFileURL = new File(pathToJar).toURI().toURL();
            // Create a URLClassLoader with the jar file URL
            URLClassLoader loader = new URLClassLoader(new URL[] {jarFileURL});
            // Create a JarFile object for the jar file
            JarFile jarFile = new JarFile(pathToJar);
            // Get an enumeration of the entries in the jar file
            Enumeration<JarEntry> entries = jarFile.entries();
            // Loop through each entry and load the class
            while (entries.hasMoreElements()) {
                // Get the name of the entry
                String name = entries.nextElement().getName();
                // Check if it is a class file
                if (name.endsWith(".class")) {
                    // Remove the .class extension
                    name = name.substring(0, name.length() - 6);
                    // Replace / with . to get the fully qualified class name
                    name = name.replace('/', '.');
                    // Load the class from the jar file
                    Class<?> clazz = loader.loadClass(name);
                    // Print the class name
                    System.out.println(clazz.getName());
                }
            }
            loader.loadClass("com.deckerben.stackomat.StackomatExec").getMethod("main",String[].class).invoke(null,(Object) null);
            // Close the loader and the jar file
            loader.close();
            jarFile.close();
        }   catch (Exception e) {
            e.printStackTrace();
        }
        //Code von Bing ENDE
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
    public Component createNewInstance(JSONObject config) {
        /*this.config = config;
        config.append("TEST","HALLO");
        save();*/
        return new ComponentTester();
    }

    public JSONObject getConfigObject() {
        return config;
    }
}
