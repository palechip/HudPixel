/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.resolver

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.minecraft.client.Minecraft.getMinecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import net.unaussprechlich.hudpixelextended.util.LoggerHelper.logInfo
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG
import java.awt.image.BufferedImage
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO.read


class PlayerHeadResolver(val username: String, private val callback: (ResourceLocation?) -> Unit){

    private var image: BufferedImage? = null
    private var isLoaded = false



    private val busCallback : (T : Event<*>) -> Unit =  { e  ->
            if(e.id == EnumDefaultEvents.TIME.get() && e.data == EnumTime.SEC_1){
                if(isLoaded) setup()
            }
    }

    init {
        GuiManagerMG.registerEventBusCallback(busCallback)
        loadImage()
    }


    fun loadImage() = launch(CommonPool) {
        try {
            val conn: HttpURLConnection = URL("http://skins.minecraft.net/MinecraftSkins/$username.png").openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            image = read(conn.inputStream)
        } catch (e : Exception){
            LoggerHelperMG.logInfo("[LoadPlayerHead] Cant find skin for $username @ http://skins.minecraft.net/MinecraftSkins/$username.png")
        } finally {
            isLoaded = true
        }
    }

    fun setup(){
        GuiManagerMG.unregisterEventBusCallback(busCallback)
        if (image == null)
            callback.invoke(null)

        else  {
            image = image!!.getSubimage(8, 8, 8, 8)
            val texture = DynamicTexture(image!!)
            callback.invoke(getMinecraft().textureManager.getDynamicTextureLocation(username, texture))
            logInfo("[LoadPlayer]: Loaded skin for $username @ http://skins.minecraft.net/MinecraftSkins/$username.png")
        }
    }
}
