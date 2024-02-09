package com.github.crafterchen2.toolbox;

public interface Resettable {

    boolean canReset(); //Existiert, damit ein ungewollter Reset vermieden wird.

    default void reset(){ //sollte nicht implementiert werden
        if (canReset()) resetCode();
    }

    void resetCode();

}
