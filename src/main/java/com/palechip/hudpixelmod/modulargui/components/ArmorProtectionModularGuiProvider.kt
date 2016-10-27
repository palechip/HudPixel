/***********************************************************************************************************************
HudPixelReloaded - License

The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
intended for usage in this kind of application. By default, all rights are reserved.
The original version of the HudPixel Mod is made by palechip and published under the MIT license.
The majority of code left from palechip's creations is the component implementation.The ported version to
Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
(to be changed to the new license as detailed below in the next minor update).

For the rest of the code and for the build the following license applies:

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
#  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
#  Based on a work at HudPixelExtended & HudPixel.                                                  #
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

Restrictions:

The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
cases the authors reserve the right to revoke all rights for usage of the codebase.

1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
code, but only when it is used separately from HudPixel and any license header must indicate that.
2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
two of the authors.
3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
code is merged to the release branch you cannot revoke the given freedoms by this license.
5. If your own project contains a part of the licensed material you have to give the authors full access to all project
related files.
6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
reserve the right to take down any infringing project.
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.modulargui.components

import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.extended.util.DamageReductionCalc
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean
import net.minecraft.client.Minecraft

/**
 * Created by Elad on 10/18/2016.
 */
object ArmorProtectionModularGuiProvider : HudPixelModularGuiProvider() {
    @ConfigPropertyBoolean(CCategory.HUDPIXEL, "armorprotectiongui", "Enable the armor protection gui?", true)
    @JvmStatic var enabled: Boolean = true

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
        if (Minecraft.getMinecraft().thePlayer != null)
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