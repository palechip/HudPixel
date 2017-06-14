/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.shader.Framebuffer
import net.minecraftforge.fml.client.FMLClientHandler

/**
 * FrameBufferObj unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 */
class FrameBufferObj(width: Int, height: Int, p_i45078_3_: Boolean) : Framebuffer(width, height, p_i45078_3_) {

    fun createDeleteFramebuffer(width: Int, height: Int) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = width
            this.framebufferHeight = height
        } else {
            if (this.framebufferObject >= 0)
                this.deleteFramebuffer()

            this.createFramebuffer(width, height)
            this.checkFramebufferComplete()
        }
    }

    fun framebufferRenderTexture(x: Int, y: Int, width: Int, height: Int) {
        if (!OpenGlHelper.isFramebufferEnabled()) return

        GlStateManager.pushMatrix()
        GlStateManager.pushAttrib()
        GlStateManager.enableBlend()
        GlStateManager.enableColorMaterial()

        GlStateManager.color(1f, 1f, 1f, 1f)

        this.bindFramebufferTexture()

        val tessellator = Tessellator.getInstance()
        // This is the instance which is named worldrenderer in Tessellator
        val worldrenderer = tessellator.getBuffer()

        val f1 = 1f / width.toFloat()
        val f2 = 1f / height.toFloat()

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(x.toDouble(), (height + y).toDouble(), 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex()
        worldrenderer.pos((width + x).toDouble(), (height + y).toDouble(), 0.0).tex(1.0, 0.0).color(255, 255, 255, 255).endVertex()
        worldrenderer.pos((width + x).toDouble(), y.toDouble(), 0.0).tex(1.0, 1.0).color(255, 255, 255, 255).endVertex()
        worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex(0.0, 1.0).color(255, 255, 255, 255).endVertex()

        tessellator.draw()

        this.unbindFramebufferTexture()
        GlStateManager.disableColorMaterial()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
        GlStateManager.popAttrib()
    }
}
