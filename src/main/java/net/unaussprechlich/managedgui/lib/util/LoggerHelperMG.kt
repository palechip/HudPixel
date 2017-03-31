/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import eladkay.hudpixel.HudPixelMod


object LoggerHelperMG {

    private val LOGGER = HudPixelMod.instance().logger!!

    fun logInfo(s: String) {
        LOGGER.info("[ManagedGui]" + s)
    }

    fun logWarn(s: String) {
        LOGGER.warn("[ManagedGui]" + s)
    }

    fun logError(s: String) {
        LOGGER.error("[ManagedGui]" + s)
    }

    fun logDebug(s: String) {
        LOGGER.debug("[ManagedGui]" + s)
    }

}
