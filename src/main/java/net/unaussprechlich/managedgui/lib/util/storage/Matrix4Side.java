/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.storage;

/**
 * Created by kecka on 05.09.2016.
 */
public class Matrix4Side {


    public int LEFT;
    public int RIGHT;
    public int TOP;
    public int BOTTOM;

    public Matrix4Side() {
        this.LEFT = 0;
        this.RIGHT = 0;
        this.TOP = 0;
        this.BOTTOM = 0;
    }

    public Matrix4Side(int LEFT, int RIGHT, int TOP, int BOTTOM) {
        this.LEFT = LEFT;
        this.RIGHT = RIGHT;
        this.TOP = TOP;
        this.BOTTOM = BOTTOM;
    }
}
