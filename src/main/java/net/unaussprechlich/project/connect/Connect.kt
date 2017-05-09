package net.unaussprechlich.project.connect

import eladkay.hudpixel.HudPixelMod
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.helper.SetupHelper
import net.unaussprechlich.project.connect.gui.ConnectGUI
import net.unaussprechlich.project.connect.socket.io.SocketConnection

/**
 * Connect Created by unaussprechlich on 20.12.2016.
 * Description:
 */
object Connect {

    fun setup() {
        LoggerHelper.logInfo("Setting up Connect!")
        ManagedGui.setup(object: SetupHelper {
            override fun getMODID(): String {
                return HudPixelMod.MODID
            }
        })
        //TODO set to false if you want the ManagedGUI lib
        ManagedGui.isIsDisabled = true

        GuiManagerMG.addGUI("ConnectGUI", ConnectGUI)

        SocketConnection
    }

}
