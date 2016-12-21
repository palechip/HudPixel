/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
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
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide;

public class RenderUtils {

    private static void glPop(){
        //GlStateManager.popMatrix();
        //GlStateManager.popAttrib();
    }

    private static void glPush(){
        //GlStateManager.pushMatrix();
        //GlStateManager.pushAttrib();
    }

    /**
     * Draw a 1 pixel wide vertical line.
     */
    protected void drawVerticalLine(int xStart, int yStart, int width, ColorRGBA color) {
            renderBoxWithColor(xStart, yStart, width, 1, color);
    }

    /**
     * Draw a 1 pixel wide horizontal line.
     */
    protected void drawHorizontalLine(int xStart, int yStart, int height, ColorRGBA color) {
        renderBoxWithColor(xStart, yStart, 1, height, color);
    }


    public static void renderItemStackWithText(int id, int meta, int xStart, int yStart, String overlay){

        glPop();
        GlStateManager.enableBlend();
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
        glPush();
    }

    public static void renderItemStack(ItemStack iStack, int xStart, int yStart){

        glPop();
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.color(0.0F, 0.0F, 32.0F);
         RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(iStack , (short)(xStart + 2), (short)(yStart + 2));

        RenderHelper.disableStandardItemLighting();

        double dur = 1 - iStack.getItem().getDurabilityForDisplay(iStack);

        if(iStack.getItem().showDurabilityBar(iStack))
            renderBoxWithColor(xStart + 1, yStart + 18, 18 * dur, 1, (float)(1 - dur), (float)dur, 0, 1);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        glPush();
    }

    public static void renderItemStackHudBackground(ItemStack iStack, int xStart, int yStart){
        renderBoxWithHudBackground(xStart + 1, yStart + 1, 18, 18);
        renderItemStack(iStack, xStart, yStart);
    }

    public static void renderBorder(int xStart, int yStart, int width, int height, ContainerSide border, ColorRGBA color) {
        //TOP//
        renderBoxWithColor(
                xStart, yStart,
                width,
                border.TOP(),
                color
        );
        //BOTTOM//
        renderBoxWithColor(
                xStart, yStart + height - border.BOTTOM(),
                width,
                border.BOTTOM(),
                color
        );
        //LEFT//
        renderBoxWithColor(
                xStart, yStart + border.TOP(),
                border.LEFT(),
                height - border.TOP() - border.BOTTOM(),
                color
        );
        //RIGHT//
        renderBoxWithColor(
                xStart + width - border.RIGHT(), yStart + border.TOP(),
                border.RIGHT(),
                height - border.TOP() - border.BOTTOM(),
                color
        );
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

    public static void renderBoxWithColor(float xStart, float yStart, int width, int height, ColorRGBA color) {

        glPop();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(color.getREDf(), color.getGREENf(), color.getBLUEf(), color.getALPHAf());
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glPush();

    }

    public static void renderBoxWithColor(double xStart, double yStart, double width, double height,
                                          float red, float green, float blue, float alpha){

        glPop();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

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
        glPush();
    }



    /**
     * Draws a textured rectangle at z = 0. Args: x, y width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(double x, double y, double width, double height, ResourceLocation resourceLocation, Float alpha) {

        glPop();

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

        glPush();

    }


    public static void drawModalRectWithCustomSizedTexture(int x, int y, int uX, int uY, int vX, int vY, int width, int height, ResourceLocation resourceLocation, float alpha) {

        glPop();

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

        glPush();
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

        glPop();
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
        glPush();
    }

    public static void renderBox(int xStart, int yStart, int width, int height) {
        renderBoxWithColor(xStart, yStart, width, height, EnumRGBA.GREY_T.get());
    }
}
