/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.storage;

/**
 * ContainerSide Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class ContainerSide {

    private MatrixC1R4<Integer> sides = new MatrixC1R4<>(0, 0, 0, 0);

    public boolean isEmpty(){
        return sides.t0 == 0 && sides.t1 == 0 && sides.t2 == 0 && sides.t3 == 0;
    }

    public ContainerSide TOP(int value){
        sides.t0 = value;
        return this;
    }

    public int TOP(){
        return sides.t0;
    }

    public ContainerSide BOTTOM(int value){
        sides.t2 = value;
        return this;
    }

    public int BOTTOM(){
        return sides.t2;
    }

    public ContainerSide LEFT(int value){
        sides.t3 = value;
        return this;
    }

    public int LEFT(){
        return sides.t3;
    }

    public ContainerSide RIGHT(int value){
        sides.t1 = value;
        return this;
    }

    public int RIGHT(){
        return sides.t1;
    }


}
