/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util;

import eladkay.hudpixel.config.GeneralConfigSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.hudpixelextended.util.ImageLoader;
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;

public class RenderUtils {

    private static float partialTicks = 0;

    public static float getPartialTicks() {
        return partialTicks;
    }

    public static  void setPartialTicks(float partialTicks) {
        RenderUtils.partialTicks = partialTicks;
    }

    public static void onClientTick(){
        processLoadingBar();
    }

    public static void drawBorderBox(int xStart, int yStart, int width, int height, ColorRGBA cBorder, ColorRGBA cBackground){
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart, yStart, width, height, cBackground);

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart , yStart , width, 1, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart, yStart + 1 , 1, height - 2, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - 1 , yStart + 1 , 1, height -2, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart, yStart + height - 1, width , 1, cBorder);

    }

    public static void drawBorderInlineShadowBox(int xStart, int yStart, int width, int height, ColorRGBA cBorder, ColorRGBA cBackground){
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart, yStart, width, height, cBackground);

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart , yStart , width, 1, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart, yStart + 1 , 1, height - 2, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - 1 , yStart + 1 , 1, height -2, cBorder);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart, yStart + height - 1, width , 1, cBorder);

        RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + 1, yStart + 1, height -2, width -2, RGBA.BLACK_LIGHT.get(), cBackground, 2);
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
    }

    public static void renderItemStack(ItemStack iStack, int xStart, int yStart){

        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.color(0.0F, 0.0F, 32.0F);
         RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(iStack , (short)(xStart + 2), (short)(yStart + 2));

        RenderHelper.disableStandardItemLighting();

        double dur = 1 - iStack.getItem().getDurabilityForDisplay(iStack);

        if(iStack.getItem().showDurabilityBar(iStack))
            renderBoxWithColor(xStart + 1, yStart + 18, (int) Math.round(18 * dur), 1, (float)(1 - dur), (float)dur, 0, 1);

        GlStateManager.disableBlend();
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

    //##################################################################################################################

    public static void renderBoxWithHudBackground(int xStart, int yStart, int width, int height){
        renderBoxWithColor(
                xStart, yStart, width, height,
                (float)(GeneralConfigSettings.getHudRed())   / 255,
                (float)(GeneralConfigSettings.getHudGreen()) / 255,
                (float)(GeneralConfigSettings.getHudBlue())  / 255,
                (float)(GeneralConfigSettings.getHudAlpha()) / 255
        );
    }

    public static void renderBoxWithColor(int xStart, int yStart, int width, int height, ColorRGBA color) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);

        GlStateManager.color(color.getRedf(), color.getGreenf(), color.getBluef(), color.getAlphaf());
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.color(1f, 1f, 1f, 1f);

        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    public static void renderBoxWithColorBlend(int xStart, int yStart, int width, int height, ColorRGBA color, int sfatorAlpha, int dfactorAlpha){
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, sfatorAlpha, dfactorAlpha);

        GlStateManager.color(color.getRedf(), color.getGreenf(), color.getBluef(), color.getAlphaf());

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);

        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();

        tessellator.draw();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }


    public static void renderBoxWithColorBlend_s1_d0(int xStart, int yStart, int width, int height, ColorRGBA color) {
        renderBoxWithColorBlend(xStart, yStart, width, height, color, 1, 0);
    }

    public static void renderBoxWithColorBlend_s1_d1(int xStart, int yStart, int width, int height, ColorRGBA color) {
        renderBoxWithColorBlend(xStart, yStart, width, height, color, 1, 1);
    }

    public static void renderBoxWithColor(int xStart, int yStart, int width, int height,
                                          float red, float green, float blue, float alpha){
        renderBoxWithColor(xStart, yStart, width, height, new ColorRGBA(red, green, blue, alpha));
    }

    public static void renderBoxWithColor(float xStart, float yStart, float width, float height,
                                          float red, float green, float blue, float alpha){
        renderBoxWithColor(Math.round(xStart), Math.round(yStart), Math.round(width), Math.round(height), new ColorRGBA(red, green, blue, alpha));
    }

    public static void renderBoxWithColor(double xStart, double yStart, double width, double height,
                                          float red, float green, float blue, float alpha){
        renderBoxWithColor((int)Math.round(xStart),(int) Math.round(yStart),(int) Math.round(width),(int) Math.round(height), new ColorRGBA(red, green, blue, alpha));
    }

    //##################################################################################################################

    public static void renderRectWithColorBlendFade(int xStart, int yStart, int width, int height,
                                                    ColorRGBA colorX0, ColorRGBA colorX1, ColorRGBA colorX2, ColorRGBA colorX3,
                                                    int sfactorAlpha, int dfactorAlpha){

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, sfactorAlpha, dfactorAlpha);

        GlStateManager.shadeModel(GL_SMOOTH);
        GlStateManager.color(1f, 1f, 1f, 1f);

        Tessellator   tessellator   = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        worldrenderer.pos(xStart, yStart + height, 0.0D).color(colorX0.getRed(), colorX0.getGreen(), colorX0.getBlue(), colorX0.getAlpha()).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).color(colorX1.getRed(), colorX1.getGreen(), colorX1.getBlue(), colorX1.getAlpha()).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).color(colorX2.getRed(), colorX2.getGreen(), colorX2.getBlue(), colorX2.getAlpha()).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).color(colorX3.getRed(), colorX3.getGreen(), colorX3.getBlue(), colorX3.getAlpha()).endVertex();

        tessellator.draw();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void renderRectWithColorBlendFade_s1_d0(int xStart, int yStart, int width, int height,
                                                           ColorRGBA colorX0, ColorRGBA colorX1, ColorRGBA colorX2, ColorRGBA colorX3){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorX0, colorX1, colorX2, colorX3, 1, 0);
    }

    public static void renderRectWithColorBlendFade_s1_d1(int xStart, int yStart, int width, int height,
                                                          ColorRGBA colorX0, ColorRGBA colorX1, ColorRGBA colorX2, ColorRGBA colorX3){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorX0, colorX1, colorX2, colorX3, 1, 1);
    }

    public static void renderRectWithColorFadeHorizontal_s1_d0(int xStart, int yStart, int width, int height, ColorRGBA colorTop, ColorRGBA colorBottom){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorBottom, colorBottom, colorTop, colorTop, 1, 0);
    }

    public static void renderRectWithColorFadeVertical_s1_d0(int xStart, int yStart, int width, int height, ColorRGBA colorLeft, ColorRGBA colorRight){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorLeft, colorRight, colorRight, colorLeft, 1, 0);
    }

    public static void rect_fade_horizontal_s1_d1(int xStart, int yStart, int width, int height, ColorRGBA colorTop, ColorRGBA colorBottom){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorBottom, colorBottom, colorTop, colorTop, 1, 1);
    }

    public static void renderRectWithColorFadeVertical_s1_d1(int xStart, int yStart, int width, int height, ColorRGBA colorLeft, ColorRGBA colorRight){
        renderRectWithColorBlendFade(xStart, yStart, width, height, colorLeft, colorRight, colorRight, colorLeft, 1, 1);
    }

    public static void renderRectWithInlineShadow_s1_d1(int xStart, int yStart, int height, int width, ColorRGBA shadowColor, ColorRGBA fillColor, int fade){

        int fade2 = fade * 2;

        if(height > fade2){
            RenderUtils.renderRectWithColorFadeVertical_s1_d1(xStart, yStart + fade, fade, height - fade2, shadowColor , fillColor);
            RenderUtils.renderRectWithColorFadeVertical_s1_d1(xStart + width - fade, yStart + fade, fade , height - fade2, fillColor , shadowColor);
        }

        if(width > fade2){
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + fade , yStart, width - fade2, fade, shadowColor , fillColor);
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + fade, yStart + height - fade, width - fade2, fade, fillColor , shadowColor);
        }

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + fade, yStart + fade, width - fade2 , height - fade2, fillColor );

        RenderUtils.renderRectWithColorBlendFade_s1_d1(xStart , yStart , fade, fade, shadowColor , fillColor, shadowColor, shadowColor);
        RenderUtils.renderRectWithColorBlendFade_s1_d1(xStart + width - fade, yStart, fade, fade, fillColor , shadowColor, shadowColor, shadowColor);

        RenderUtils.renderRectWithColorBlendFade_s1_d1(xStart, yStart + height - fade, fade, fade, shadowColor , shadowColor, fillColor, shadowColor);
        RenderUtils.renderRectWithColorBlendFade_s1_d1(xStart + width - fade,  yStart + height - fade, fade, fade, shadowColor , shadowColor, shadowColor, fillColor);
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y width, height, textureWidth, textureHeight
     */
    public static void texture_modularRect(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        texture_modularRect(x, y, 0, 0, width, height, width, height, resourceLocation, 1f);
    }


    public static void texture_modularRect(int x, int y, int uX, int uY, int vX, int vY, int width, int height, ResourceLocation resourceLocation, float alpha) {
        if(resourceLocation != null)
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        GlStateManager.color(1f, 1f, 1f, alpha);

        float f = 1.0F / height;
        float f1 = 1.0F / width;

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos( x        , y + height, 0.0D).tex(uX * f1, vY * f).endVertex();
        worldrenderer.pos( x + width, y + height, 0.0D).tex(vX * f1, vY * f).endVertex();
        worldrenderer.pos( x + width, y         , 0.0D).tex(vX * f1, uY * f).endVertex();
        worldrenderer.pos( x        , y         , 0.0D).tex(uX * f1, uY * f).endVertex();

        tessellator.draw();
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
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(1f, 1f, 1f, alpha);

        float f =  1.0F / tWidth;
        float f1 = 1.0F / tHeight ;

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos( x        , y + height, 0.0D).tex(uX * f, vY * f1 ).endVertex();
        worldrenderer.pos( x + width, y + height, 0.0D).tex(vX * f, vY * f1).endVertex();
        worldrenderer.pos( x + width, y         , 0.0D).tex(vX * f, uY * f1).endVertex();
        worldrenderer.pos( x        , y         , 0.0D).tex(uX * f, uY * f1).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
    }

    public static void renderBox(int xStart, int yStart, int width, int height) {
        renderBoxWithColor(xStart, yStart, width, height, RGBA.GRAY_T.get());
    }

    public static void iconRender_resize(int xStartRight, int yStartBot, ColorRGBA color){
        final int l = 1;
        final int r = 10;
        final int r2 = 4;
        final int s2 = 3;

        RenderUtils.renderRectWithColorBlendFade_s1_d1(xStartRight - r, yStartBot - r, r , r, RGBA.NULL.get(), RGBA.P1B1_596068.get(), RGBA.NULL.get(), RGBA.NULL.get());

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStartRight - r, yStartBot - l, r, l, color);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStartRight - l, yStartBot - r, l, r, color);

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStartRight - r2 - s2, yStartBot - s2 - l, r2, l, color);
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStartRight - s2 - l, yStartBot - s2 - r2, l, r2, color);
    }

    public static void iconRender_loadingBar(int xStart, int yStart) {

        final int a = 2;
        final int b = 1;
        final float alpha = 0.8f;
        int f0 = xStart;
        int f1 = xStart + 4;
        int f2 = xStart + 8;

        switch (loadingBar) {
            case 0:
                RenderUtils.renderBoxWithColor(f0, yStart, 2, 6 + a, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f1, yStart, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f2, yStart ,2, 6, 1f, 1f, 1f, alpha);
                break;
            case 1:
                RenderUtils.renderBoxWithColor(f0, yStart, 2, 6 + b, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f1, yStart, 2, 6 + a, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f2, yStart, 2, 6, 1f, 1f, 1f, alpha);
                break;
            case 2:
                RenderUtils.renderBoxWithColor(f0, yStart, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f1, yStart, 2, 6 + b, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f2, yStart, 2, 6 + a, 1f, 1f, 1f, alpha);
                break;
            case 3:
                RenderUtils.renderBoxWithColor(f0, yStart, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f1, yStart, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f2, yStart, 2, 6 + b, 1f, 1f, 1f, alpha);
                break;
            default:
                RenderUtils.renderBoxWithColor(f0, yStart, 2, 6, 1f, 1f, 1f, 0.8f);
                RenderUtils.renderBoxWithColor(f1, yStart, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(f2, yStart, 2, 6, 1f, 1f, 1f, alpha);
                break;
        }
    }

    private static int loadingBar = 0;
    private static int tickCounter = 0;
    private static void processLoadingBar() {
        if (tickCounter >= 2) {
            if (loadingBar >= 15) loadingBar = 0;
            else loadingBar++;
            tickCounter = 0;
        } else tickCounter++;
    }
}
