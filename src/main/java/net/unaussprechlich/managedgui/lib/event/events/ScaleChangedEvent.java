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
 * ScaleChangedEvent Created by Alexander on 27.02.2017.
 * Description:
 **/
public class ScaleChangedEvent extends Event{

    public ScaleChangedEvent(int scale) {
        super(EnumDefaultEvents.SCREEN_RESIZE.get(), scale);
    }
}
