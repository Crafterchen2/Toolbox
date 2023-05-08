package com.deckerpw.utilities;

import com.deckerben.utilities.ToolboxFrame;
import com.deckerben.utilities.Utility;
import org.json.JSONObject;

import java.awt.*;

public interface ConfigurableUtility extends Utility {

    JSONObject getConfigObject();
    default void save(){
        ToolboxFrame.CONTENT_PANE.saveConfig(this.getUtilitiyName(),getConfigObject());
    }
    Component createNewInstance(JSONObject config);

    @Override
    default Component createNewInstance(){
        return null;
    }
}
