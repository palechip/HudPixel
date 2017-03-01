/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;

/**
 * FrameBufferObj unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 **/
public class FrameBufferObj extends Framebuffer {

    public FrameBufferObj(int width, int height, boolean p_i45078_3_) {
        super(width, height, p_i45078_3_);
    }

    public void createDeleteFramebuffer(int width, int height) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
        } else {
            if (this.framebufferObject >= 0)
                this.deleteFramebuffer();

            this.createFramebuffer(width, height);
            this.checkFramebufferComplete();
        }
    }

    public void framebufferRenderTexture(int x, int y, int width, int height) {
        if (!OpenGlHelper.isFramebufferEnabled()) return;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();

        GlStateManager.color(1f, 1f, 1f, 1f);

        this.bindFramebufferTexture();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        float f1 =  1f / (float) width;
        float f2 =  1f / (float) height;

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x           ,height + y, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width + x,height + y, 0.0D).tex(1f  , 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width + x, y           , 0.0D).tex(1f   , 1f   ).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(x           , y           , 0.0D).tex(0.0D, 1f   ).color(255, 255, 255, 255).endVertex();

        tessellator.draw();

        this.unbindFramebufferTexture();
        GlStateManager.disableColorMaterial();
        GlStateManager.disableBlend();


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
}
