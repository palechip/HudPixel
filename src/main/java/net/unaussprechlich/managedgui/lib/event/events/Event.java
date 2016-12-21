/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event.events;

/**
 * Event Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class Event<T>{

    private final int ID;
    private final T data;

    public Event(int ID, T data) {
        this.ID = ID;
        this.data = data;
    }

    public int getID() {
        return ID;
    }

    public T getData(){
        return data;
    }
}
