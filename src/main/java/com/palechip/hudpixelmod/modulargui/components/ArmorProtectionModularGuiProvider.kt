package com.palechip.hudpixelmod.modulargui.components

import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.extended.util.DamageReductionCalc
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean
import net.minecraft.client.Minecraft

/**
 * Created by Elad on 10/18/2016.
 */
class ArmorProtectionModularGuiProvider : HudPixelModularGuiProvider() {
    companion object {
        @ConfigPropertyBoolean("hudpixel", "armorprotectiongui", "Enable the armor protection gui?", true)
        @JvmStatic var enabled: Boolean = true
    }

    override fun setupNewGame() {

    }

    override fun onGameStart() {

    }

    override fun onGameEnd() {

    }

    override fun doesMatchForGame(): Boolean {
        return !GameDetector.isLobby()
    }


    override fun onTickUpdate() {
        if(Minecraft.getMinecraft().thePlayer != null)
            content = DamageReductionCalc.getReduction()[2]
    }

    override fun onChatMessage(textMessage: String?, formattedMessage: String?) {

    }

    override fun showElement(): Boolean {
        return doesMatchForGame() && Minecraft.getMinecraft().thePlayer.inventory.armorInventory.size > 0 && enabled
    }

    var content: String = ""

    override fun content(): String? {
        return content;
    }

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

    override fun getAfterstats(): String? {
        return null
    }
}