package com.github.crafterchen2.toolbox.component;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileNameFilter extends FileFilter {

    //Fields
    private final String desc;
    private final String[] names;

    //Constructor
    public FileNameFilter(String desc, String... names) {
        this.desc = desc;
        this.names = names;
    }

    //Methods

    //Getter

    //Setter

    //Overrides from
    ////FileFilter
    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            for (String name : names) {
                if (f.getName().equals(name)) return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
