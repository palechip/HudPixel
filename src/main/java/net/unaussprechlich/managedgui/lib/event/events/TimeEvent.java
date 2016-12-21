/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event.events;

import net.unaussprechlich.managedgui.lib.event.bus.DefaultEvents;

/**
 * TimeEvent Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class TimeEvent extends Event{

    public TimeEvent(EnumTime data) {
        super(DefaultEvents.TIME.get(), data);
    }
}
