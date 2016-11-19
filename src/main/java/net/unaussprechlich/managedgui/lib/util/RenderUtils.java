/* *****************************************************************************
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
package net.unaussprechlich.managedgui.lib.util;

import com.palechip.hudpixelmod.config.GeneralConfigSettings;
import com.palechip.hudpixelmod.extended.util.ImageLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.util.storage.StorageFourSide;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void renderItemStackWithText(int id, int meta, int xStart, int yStart, String overlay){
        GL11.glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.color(0.0F, 0.0F, 32.0F);
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack iStack = new ItemStack(Item.getItemById(id));
        if(meta > 0) iStack.setItemDamage(meta);
        RenderItem renderItem = mc.getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(iStack, xStart, yStart);
        renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj,iStack, xStart, yStart, overlay);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void renderItemStack(ItemStack iStack, int xStart, int yStart){
        GL11.glPushMatrix();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.color(0.0F, 0.0F, 32.0F);
         RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(iStack , (short)(xStart + 2), (short)(yStart + 2));

        RenderHelper.disableStandardItemLighting();

        double dur = 1 - iStack.getItem().getDurabilityForDisplay(iStack);

        if(iStack.getItem().showDurabilityBar(iStack))
            renderBoxWithColor(xStart + 1, yStart + 18, 18 * dur, 1, (float)(1 - dur), (float)dur, 0, 1);

        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void renderItemStackHudBackground(ItemStack iStack, int xStart, int yStart){
        renderBoxWithHudBackground(xStart + 1, yStart + 1, 18, 18);
        renderItemStack(iStack, xStart, yStart);
    }

    public static void renderBorder(short xStart, short yStart, short innerHight, short innerWidth, StorageFourSide border, ColorRGBA color) {
        renderBoxWithColor(xStart, yStart, (short) (innerWidth + border.LEFT + border.RIGHT), border.TOP, color);
        renderBoxWithColor(xStart, (short) (yStart + border.TOP + innerHight), (short) (innerWidth + border.LEFT + border.RIGHT), border.BOTTOM, color);
        renderBoxWithColor(xStart, (short) (yStart + border.TOP), border.LEFT, innerHight, color);
        renderBoxWithColor((short) (xStart + border.LEFT + innerWidth), (short) (yStart + border.TOP), border.RIGHT, innerHight, color);
    }

    public static void renderBoxWithHudBackground(int xStart, int yStart, int width, int height){
        renderBoxWithColor(
                xStart, yStart, width, height,
                (float)(GeneralConfigSettings.getHudRed())   / 255,
                (float)(GeneralConfigSettings.getHudGreen()) / 255,
                (float)(GeneralConfigSettings.getHudBlue())  / 255,
                (float)(GeneralConfigSettings.getHudAlpha()) / 255
        );
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

    public static void renderBoxWithColor(float xStart, float yStart, int width, int height, ColorRGBA color) {

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.pushMatrix();
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
        GlStateManager.popMatrix();

    }

    public static void renderBoxWithColor(double xStart, double yStart, double width, double height,
                                          float red, float green, float blue, float alpha){

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GlStateManager.color(red, green, blue, alpha);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }



    /**
     * Draws a textured rectangle at z = 0. Args: x, y width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(double x, double y, double width, double height, ResourceLocation resourceLocation, Float alpha) {

        GlStateManager.popMatrix();
        if(resourceLocation != null)
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        //this line took me like 3 hours to fine out the color wasn't resetting :D
        GlStateManager.color(1f, 1f, 1f, alpha);

        float f = (float)(1.0F / height);
        float f1 = (float)(1.0F / width);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos( x, (y + height), 0.0D).tex(width * f, height + height * f1).endVertex();
        worldrenderer.pos( (x + width),  (y + height), 0.0D).tex((width + width) * f, (height + height) * f1).endVertex();
        worldrenderer.pos( (x + width), y, 0.0D).tex((width + width) * f, height * f1).endVertex();
        worldrenderer.pos( x,  y, 0.0D).tex(width * f, height * f1).endVertex();
        tessellator.draw();

        GlStateManager.pushMatrix();

    }


    public static void drawModalRectWithCustomSizedTexture(int x, int y, int uX, int uY, int vX, int vY, int width, int height, ResourceLocation resourceLocation, float alpha) {

        GlStateManager.popMatrix();


        if(resourceLocation != null)
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        //this line took me like 3 hours to fine out the color wasn't resetting :D
        GlStateManager.color(1f, 1f, 1f, alpha);

        float f = 1.0F / height;
        float f1 = 1.0F / width;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos( x        , y + height, 0.0D).tex(uX * f1, vY * f).endVertex();
        worldrenderer.pos( x + width, y + height, 0.0D).tex(vX * f1, vY * f).endVertex();
        worldrenderer.pos( x + width, y         , 0.0D).tex(vX * f1, uY * f).endVertex();
        worldrenderer.pos( x        , y         , 0.0D).tex(uX * f1, uY * f).endVertex();

        tessellator.draw();

        GlStateManager.pushMatrix();
    }

    public static void renderPotionIcon(int x, int y, Potion potion) {
        if(potion.hasStatusIcon()) {
            int iconIndex = potion.getStatusIconIndex();

            double uX = iconIndex % 8 * 18;
            double uY = 198 + iconIndex / 8 * 18;
            double vX = uX + 18;
            double vY = uY + 18;

            drawPotionIcon(x, y, 18, 18, uX, uY, vX, vY, 256, 256, ImageLoader.INVENTORY_RES, 1f );
        }
    }


    public static void drawPotionIcon(double x, double y, double width, double height, double uX, double uY, double vX, double vY, int tWidth, int tHeight, ResourceLocation resourceLocation, float alpha) {

        GlStateManager.popMatrix();
        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        GlStateManager.color(1f, 1f, 1f, alpha);

        float f =  1.0F / tWidth;
        float f1 = 1.0F / tHeight ;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos( x        , y + height, 0.0D).tex(uX * f, vY * f1 ).endVertex();
        worldrenderer.pos( x + width, y + height, 0.0D).tex(vX * f, vY * f1).endVertex();
        worldrenderer.pos( x + width, y         , 0.0D).tex(vX * f, uY * f1).endVertex();
        worldrenderer.pos( x        , y         , 0.0D).tex(uX * f, uY * f1).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
    }

    public static void renderBox(int xStart, int yStart, int fieldWidth, int i) {
        renderBox((short) xStart, (short) yStart, (short) fieldWidth, (short) i);
    }
}
