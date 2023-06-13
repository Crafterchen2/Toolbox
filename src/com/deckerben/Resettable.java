package com.deckerben;

public interface Resettable {

    @SuppressWarnings("SameReturnValue")
    boolean canReset(); //Existiert, damit ein ungewollter Reset vermieden wird.

    default void reset(){ //sollte nicht implementiert werden
        if (canReset()) resetCode();
    }

    default void resetCode() { //hier Reset-Code

    }

}
