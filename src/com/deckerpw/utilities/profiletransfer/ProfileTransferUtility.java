package com.deckerpw.utilities.profiletransfer;

import com.deckerben.utilities.Utility;
import com.deckerpw.profiletransfer.component.ProfileTransferPanel;

import java.awt.*;

public class ProfileTransferUtility extends ProfileTransferPanel implements Utility {

    public ProfileTransferUtility() {
        super("Ursprung der Profile", "Transferziel");
    }

    @Override
    public boolean canReset() {
        return false;
    }

    @Override
    public void resetCode() {
        if (canReset()){
            //TODO Paul anfragen, ob er eine "clearLog():void" Methode schreiben möchte, da sonst kein Reset möglich ist.
        }
    }

    @Override
    public String getUtilitiyName() {
        return "Profile Transfer Tool";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component createNewInstance() {
        return new ProfileTransferUtility();
    }
}
