package com.deckerben.utilities;

import com.deckerben.Resettable;

import java.awt.*;

public interface Utility extends Resettable {

    String getUtilitiyName();

    Component getComponent();

    Component createNewInstance();

}
