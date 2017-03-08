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
 * KeyPressedEvent Created by Alexander on 07.03.2017.
 * Description:
 **/
public class KeyPressedEvent extends Event<String> {

    public KeyPressedEvent(String data) {
        super(EnumDefaultEvents.KEY_PRESSED.get(), data);
    }
}
