package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
public class OnlineFriend {

    private static final int IMAGESIZE = 20;


    private String username;
    private String gamemode;

    private BufferedImage image;
    private DynamicTexture texture;
    private ResourceLocation resourceLocation;
    private int heigt;
    private int width;
    private boolean imageLoaded = false;

    OnlineFriend(String username, String gamemode){
        this.gamemode = gamemode;
        this.username = username;
        try {
            image = ImageIO.read(new URL("http://skins.minecraft.net/MinecraftSkins/"+ username +".png"));
            image = image.getSubimage(8,8,8,8);
            heigt = image.getHeight();
            width = image.getWidth();
            texture = new DynamicTexture(image);
            resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(username, texture);
            imageLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getUsername() {return username;}
    String getGamemode() {return gamemode;}
    void setGamemode(String gamemode) {this.gamemode = gamemode;}

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    private void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {

        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        //this line took me like 3 hours to fine out the color wasn't resetting :D
        GlStateManager.color(1f, 1f, 1f, 1f);

        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();

        GlStateManager.pushMatrix();

    }

    void renderOnlineFriend(float xStart, float yStart){

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        int i = fontRenderer.getStringWidth(gamemode);
        RenderUtils.renderBoxWithColor(xStart, yStart, 120, 23, 0, 1f, 1f, 1f, 0.15f);

        if(image != null && resourceLocation != null && imageLoaded)
            drawModalRectWithCustomSizedTexture(Math.round(xStart + 2), Math.round(yStart + 2), 0, 0, 20, 20, 20f, 20f );


        //renderBox(xStart, yStart, 20, 20, 0);
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD + username,xStart + 26, yStart +3, 0xffffff);
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GREEN + gamemode,xStart + 26, yStart + 12, 0xffffff);
    }
}
