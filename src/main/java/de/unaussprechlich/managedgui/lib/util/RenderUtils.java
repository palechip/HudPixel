/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package de.unaussprechlich.managedgui.lib.util;

import de.unaussprechlich.managedgui.lib.util.storage.StorageFourSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {

    public static void renderBorder(short xStart, short yStart, short innerHight, short innerWidth, StorageFourSide border, ColorRGBA color) {
        renderBoxWithColor(xStart, yStart, (short) (innerWidth + border.LEFT + border.RIGHT), border.TOP, color);
        renderBoxWithColor(xStart, (short) (yStart + border.TOP + innerHight), (short) (innerWidth + border.LEFT + border.RIGHT), border.BOTTOM, color);
        renderBoxWithColor(xStart, (short) (yStart + border.TOP), border.LEFT, innerHight, color);
        renderBoxWithColor((short) (xStart + border.LEFT + innerWidth), (short) (yStart + border.TOP), border.RIGHT, innerHight, color);
    }


    /**
     * helper-method, that draws a box wth semitransparent background. Should be with the onRender
     * event.
     *
     * @param xStart left upper x-cord
     * @param yStart left upper y-cord
     * @param width  with of the box
     * @param height height of the box
     */
    public static void renderBox(short xStart, short yStart, short width, short height) {
        renderBoxWithColor(xStart, yStart, width, height, new ColorRGBA(0, 0, 0, 0.5f));
    }

    public static void renderBoxWithColor(short xStart, short yStart, short width, short height,
                                          ColorRGBA color) {

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(color.RED, color.GREEN, color.BLUE, color.ALPHA);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(short x, short y, short width, short height, ResourceLocation resourceLocation, Float alpha) {

        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        //this line took me like 3 hours to fine out the color wasn't resetting :D
        GlStateManager.color(1f, 1f, 1f, alpha);

        float f = 1.0F / height;
        float f1 = 1.0F / width;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) x, (double) (y + height), 0.0D).tex((double) (width * f), (double) ((height + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((width + width) * f), (double) ((height + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) y, 0.0D).tex((double) ((width + width) * f), (double) (height * f1)).endVertex();
        worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (width * f), (double) (height * f1)).endVertex();
        tessellator.draw();

        GlStateManager.pushMatrix();

    }

    public static void renderBoxWithColor(float v, float v1, int i, int i1, int i2, float v2, float v3, float v4, float alpha) {
        renderBoxWithColor((short) v, (short) v1, (short) i1, (short) i2, new ColorRGBA(v2, v3, v4, alpha));
    }

    public static void drawModalRectWithCustomSizedTexture(int round, int round1, int i, int i1, int i2, int i3, int i4, int i5, ResourceLocation resourceLocation, float v) {
        drawModalRectWithCustomSizedTexture((short) round, (short) round1, (short) i4, (short) i5, resourceLocation, v);
    }

    public static void renderBox(int xStart, int yStart, int fieldWidth, int i) {
        renderBox((short) xStart, (short) yStart, (short) fieldWidth, (short) i);
    }
}
