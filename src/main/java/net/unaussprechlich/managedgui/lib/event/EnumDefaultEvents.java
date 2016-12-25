/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event;

/**
 * EnumDefaultEvents Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public enum EnumDefaultEvents {
    TIME(0), CLICK(1);

    private int ID;

    EnumDefaultEvents(int ID) {
        this.ID = ID;
    }

    public int get() {
        return ID;
    }
}
