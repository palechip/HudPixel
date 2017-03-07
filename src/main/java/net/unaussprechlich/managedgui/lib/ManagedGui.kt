/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib

import net.minecraftforge.common.MinecraftForge
import net.unaussprechlich.managedgui.lib.helper.SetupHelper
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG


object ManagedGui {

    var isIsDisabled = true
    var iSetupHelper: SetupHelper? = null
        private set

    fun setup(ISetupHelper: SetupHelper) {
        LoggerHelperMG.logInfo("Setting up ManagedGuiLib!")
        ManagedGui.iSetupHelper = ISetupHelper
        GuiManagerMG
        MinecraftForge.EVENT_BUS.register(GuiManagerMG)

    }

}
