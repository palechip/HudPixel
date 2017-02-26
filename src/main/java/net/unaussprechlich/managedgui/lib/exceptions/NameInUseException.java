/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.exceptions;

public class NameInUseException extends ManagedGuiException{

    public NameInUseException(String name, String where){
        super("The nm " + name + " is all ready used in " + where + "!");
    }

}
