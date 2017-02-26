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
import org.lwjgl.opengl.GL11;

/**
 * FrameBufferObj unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 **/
public class FrameBufferObj extends Framebuffer {

    public FrameBufferObj(int width, int height, boolean p_i45078_3_) {
        super(width, height, p_i45078_3_);
    }

    @Override
    public void setFramebufferFilter(int p_147607_1_) {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            this.framebufferFilter = p_147607_1_;
            GlStateManager.bindTexture(this.framebufferTexture);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, (float)p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
            GlStateManager.bindTexture(0);
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
