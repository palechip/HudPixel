package de.unaussprechlich.managedgui.lib.util.storage;

/**
 * Created by kecka on 05.09.2016.
 */
public class StorageFourSide {

    public short LEFT;
    public short RIGHT;
    public short TOP;
    public short BOTTOM;

    public StorageFourSide() {
        this.LEFT = 0;
        this.RIGHT = 0;
        this.TOP = 0;
        this.BOTTOM = 0;
    }


    public StorageFourSide(short LEFT, short RIGHT, short TOP, short BOTTOM) {
        this.LEFT = LEFT;
        this.RIGHT = RIGHT;
        this.TOP = TOP;
        this.BOTTOM = BOTTOM;
    }
}
