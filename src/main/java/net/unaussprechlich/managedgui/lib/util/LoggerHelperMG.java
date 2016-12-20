/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util;

import org.apache.logging.log4j.Logger;

import static com.palechip.hudpixelmod.HudPixelMod.instance;

public class LoggerHelperMG {

    private static Logger LOGGER = instance().getLOGGER();

    public static void logInfo(String s) {
        LOGGER.info("[ManagedGui]" + s);
    }

    public static void logWarn(String s) {
        LOGGER.warn("[ManagedGui]" + s);
    }

    public static void logError(String s) {
        LOGGER.error("[ManagedGui]" + s);
    }

    public static void logDebug(String s) {
        LOGGER.debug("[ManagedGui]" + s);
    }

}
