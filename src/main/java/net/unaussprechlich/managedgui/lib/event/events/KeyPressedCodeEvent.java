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
 * SpecialKeyPressedEvent Created by Alexander on 08.03.2017.
 * Description:
 **/
public class KeyPressedCodeEvent extends Event<Integer>{

    public KeyPressedCodeEvent(int data) {
        super(EnumDefaultEvents.KEY_PRESSED_CODE.get(), data);
    }
}
