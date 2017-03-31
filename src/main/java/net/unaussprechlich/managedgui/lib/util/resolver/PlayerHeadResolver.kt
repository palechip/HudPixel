/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.resolver

import net.minecraft.client.Minecraft.getMinecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import net.unaussprechlich.hudpixelextended.util.LoggerHelper.logInfo
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO.read

class PlayerHeadResolver(val username: String, private val callback: (ResourceLocation?) -> Unit) : Thread() {

    private var image: BufferedImage? = null

    init {
        image = loadSkinFromURL()

        if (image == null)
           callback.invoke(null)

        else  {
            image = image!!.getSubimage(8, 8, 8, 8)
            val texture = DynamicTexture(image!!)
            callback.invoke(getMinecraft().textureManager.getDynamicTextureLocation(username, texture))
            logInfo("[LoadPlayer]: Loaded skin for $username @ http://skins.minecraft.net/MinecraftSkins/$username.png")
        }
    }

    private fun loadSkinFromURL() : BufferedImage? {
        return read(URL("http://skins.minecraft.net/MinecraftSkins/$username.png"))
    }







}
