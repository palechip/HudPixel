/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

/**
 * IScrollSpacerRenderer Created by Alexander on 26.02.2017.
 * Description:
 */
interface IScrollSpacerRenderer {
    fun render(xStart: Int, yStart: Int, width: Int)
    val spacerHeight: Int
}
