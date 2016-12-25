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
 * ClickEvent Created by unaussprechlich on 21.12.2016.
 * Description:
 **/
public class ClickEvent extends Event{

    public ClickEvent(String clickID) {
         super(EnumDefaultEvents.CLICK.get(), clickID);
    }
}
