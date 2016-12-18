/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.exceptions;

public class ManagedGuiException extends RuntimeException {

    public ManagedGuiException() {}

    public ManagedGuiException(String message) {
        super(message);
    }

    public ManagedGuiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagedGuiException(Throwable cause) {
        super(cause);
    }

    public ManagedGuiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}