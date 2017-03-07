/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.util.EnumEventState

/**
 * ICustomRenderer Created by Alexander on 07.03.2017.
 * Description:
 */
interface ICustomRenderer {

    fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean

}
