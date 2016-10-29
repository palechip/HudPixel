package com.palechip.hudpixelmod.modulargui.components

import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider
import net.minecraft.client.Minecraft

/**
 * Created by Elad on 10/24/2016.
 */
object CoordsDisplayModularGuiProvider : SimpleHudPixelModularGuiProvider() {
    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

    @ConfigPropertyBoolean(CCategory.HUD, "coordsdisplayhud", "Enable the coords display gui?", true)
    @JvmStatic var enabled: Boolean = true

    override fun onGameEnd() {

    }

    override fun onTickUpdate() {

    }

    override fun onChatMessage(textMessage: String?, formattedMessage: String?) {

    }

    override fun onGameStart() {

    }

    override fun getAfterstats(): String? {
        return null
    }


    override fun content(): String? {
        val pos = Minecraft.getMinecraft().thePlayer.position
        return "X: ${pos.x}, Y: ${pos.y}, Z: ${pos.z}"
    }

    override fun showElement(): Boolean {
        return !GameDetector.isLobby() && enabled
    }

    override fun setupNewGame() {

    }

    override fun doesMatchForGame(): Boolean {
        return true
    }
}