package com.github.crafterchen2.toolbox;

import com.github.crafterchen2.toolbox.Resettable;

import java.awt.*;

public interface Utility extends Resettable {

    String getUtilitiyName();

    Component getComponent();

    Component createNewInstance();
    
    default int getListPriority() {
        return 0;
    }

}
