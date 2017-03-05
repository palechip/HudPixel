/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import com.palechip.hudpixelmod.HudPixelMod.Companion.instance


object LoggerHelperMG {

    private val LOGGER = instance().logger!!

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
