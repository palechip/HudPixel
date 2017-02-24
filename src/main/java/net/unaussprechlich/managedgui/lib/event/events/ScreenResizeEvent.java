/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event.events;

import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;

/**
 * ScreenResizeEvent Created by Alexander on 24.02.2017.
 * Description:
 **/
public class ScreenResizeEvent extends Event {

    public ScreenResizeEvent() {
        super(EnumDefaultEvents.SCREEN_RESIZE.get(), null);
    }
}
