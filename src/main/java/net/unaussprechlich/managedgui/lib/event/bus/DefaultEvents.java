/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event.bus;

/**
 * DefaultEvents Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public enum DefaultEvents {
    TIME(0);

    private int ID;

    DefaultEvents(int ID) {
        this.ID = ID;
    }

    public int get() {
        return ID;
    }
}
