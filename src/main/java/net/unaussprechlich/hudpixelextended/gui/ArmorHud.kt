/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package net.unaussprechlich.hudpixelextended.gui

import eladkay.hudpixel.config.CCategory
import eladkay.hudpixel.config.ConfigPropertyBoolean
import eladkay.hudpixel.config.ConfigPropertyInt
import eladkay.hudpixel.config.GeneralConfigSettings
import eladkay.hudpixel.util.DisplayUtil
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.client.FMLClientHandler
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler
import net.unaussprechlich.hudpixelextended.util.IEventHandler
import net.unaussprechlich.managedgui.lib.util.RenderUtils

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

object ArmorHud : IEventHandler {

    @ConfigPropertyBoolean(CCategory.ARMOR_HUD, "disableArmorHud", "Disables the ArmorHud", false) @JvmStatic var disable_ArmorHud: Boolean = false
    @ConfigPropertyBoolean(CCategory.ARMOR_HUD, "renderRightArmorHud", "Renders the ArmorHud on the right side", true) @JvmStatic var renderRight_ArmorHud: Boolean = true
    @ConfigPropertyBoolean(CCategory.ARMOR_HUD, "renderBottomArmorHud", "Renders the ArmorHud on the bottom side", true) @JvmStatic var renderBottom_ArmorHud: Boolean = true
    @ConfigPropertyBoolean(CCategory.ARMOR_HUD, "renderVerticalArmorHud", "Renders the ArmorHud Vertical", true) @JvmStatic var renderVertical_ArmorHud: Boolean = true
    @ConfigPropertyInt(CCategory.ARMOR_HUD, "xOffsetArmorHud", "x-offset", -2) @JvmStatic var xOffset_ArmorHud: Int = -2
    @ConfigPropertyInt(CCategory.ARMOR_HUD, "yOffsetArmorHud", "y-offset", -2) @JvmStatic var yOffset_ArmorHud: Int = -2

    private var xStart: Int = 0
    private var yStart: Int = 0

    private val size: Int = 19

    fun init() {
        HudPixelExtendedEventHandler.registerIEvent(this)
    }

    override fun onRender() {
        val player = FMLClientHandler.instance()?.clientPlayerEntity
        if (player == null) return
        if (disable_ArmorHud || !Minecraft.getMinecraft().inGameHasFocus) return

        // TODO: Test and fix
        // The port to MC 1.11 forced me to use this loop instead of a list addressable by index.
        // This should naturally throw off the variable i when wearing less than a complete set.
        var i : Int = 0
        player.armorInventoryList.forEach {
            if (renderVertical_ArmorHud)
                if (GeneralConfigSettings.hudBackground)
                    RenderUtils.renderItemStackHudBackground(it, xStart, yStart + (i * size))
                else
                    RenderUtils.renderItemStack(it, xStart, yStart + (i * size))
            else
                if (GeneralConfigSettings.hudBackground)
                    RenderUtils.renderItemStackHudBackground(it, xStart + (i * size), yStart)
                else
                    RenderUtils.renderItemStack(it, xStart + (i * size), yStart)
            i += 1
        }
    }


    override fun onClientTick() {
        val dHeight = DisplayUtil.getScaledMcHeight()
        val dWidth = DisplayUtil.getScaledMcWidth()


        if (renderBottom_ArmorHud && renderVertical_ArmorHud)
            yStart = dHeight - (size * 4) + yOffset_ArmorHud
        else if (renderBottom_ArmorHud)
            yStart = dHeight - size + yOffset_ArmorHud
        else
            yStart = yOffset_ArmorHud


        if (renderRight_ArmorHud && !renderVertical_ArmorHud)
            xStart = dWidth - (size * 4) + xOffset_ArmorHud
        else if (renderRight_ArmorHud)
            xStart = dWidth - size + xOffset_ArmorHud
        else
            xStart = xOffset_ArmorHud
    }


}
