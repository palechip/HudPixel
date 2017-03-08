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
    TIME(-1), CLICK(-2), SCREEN_RESIZE(-3), SCALE_CHANGED(-4), KEY_PRESSED(-5), KEY_PRESSED_CODE(-6);

    private int ID;

    EnumDefaultEvents(int ID) {
        this.ID = ID;
    }

    public int get() {
        return ID;
    }
}
