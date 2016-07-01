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

package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.*;


public class OnlineFriend {

    private static int loadingBar;

    private String username;
    private String gamemode;

    private BufferedImage image;
    private ResourceLocation resourceLocation;
    private boolean imageLoaded = false;
    private boolean imageSetup = false;

    String getUsername() {return username;}
    String getGamemode() {return gamemode;}
    void setGamemode(String gamemode) {this.gamemode = gamemode;}

    /**
     * Constructor ... also loads the playerhead
     * @param username Username
     * @param gamemode current string to render
     */
    OnlineFriend(String username, String gamemode){
        this.gamemode = gamemode;
        this.username = username;

        loadSkinFromURL();
    }

    /**
     * renders the loading animation
     * @param xStart startposition of the friendsdisplay
     * @param yStart startposition of the friendsdisplay
     */
    private void renderLoadingBar(float xStart, float yStart){

        final int a = 2;
        final int b = 1;
        final float alpha = 0.8f;

        switch (loadingBar){
            case 0:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
            case 1:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
            case 2:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                break;
            case 3:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                break;
            default:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, 0.8f);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
        }
    }

    /**
     * process the current loadingbar value
     */
    private static int tickCounter = 0;
    public static void onClientTick(){
        if(tickCounter >= 2){
            if (loadingBar >= 15) loadingBar = 0;
            else loadingBar ++;
            tickCounter = 0;
        } else tickCounter ++;
    }

    /**
     * helper function to extract the player head from the skin and also store it in
     * the dynamic resourcelocation provided by mc.
     */
    private void setupImage(){
        image = image.getSubimage(8,8,8,8);
        DynamicTexture texture = new DynamicTexture(image);
        resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(username, texture);
        imageSetup = true;

        LoggerHelper.logInfo("[OnlineFriends]: Skin setup for " + username);
    }

    /**
     * Performs the rendering for the online friends display
     * @param xStart Startposition given by the manager class
     * @param yStart Startposition given by the manager class
     */
    void renderOnlineFriend(float xStart, float yStart){

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        //makes the background larger, if the string is to long
        if(fontRenderer.getStringWidth(gamemode) > 90)
            RenderUtils.renderBoxWithColor(xStart, yStart, fontRenderer.getStringWidth(gamemode) + 30, 24, 0, 1f, 1f, 1f, 0.15f);
        else
            RenderUtils.renderBoxWithColor(xStart, yStart, 120, 24, 0, 1f, 1f, 1f, 0.15f);

        //drwaws the player head after t is loaded
        if(image != null && imageLoaded){
            if(!imageSetup)
            setupImage();
            else
            RenderUtils.drawModalRectWithCustomSizedTexture(
                    Math.round(xStart + 2), Math.round(yStart + 2), 0, 0,
                    20, 20, 20f, 20f , resourceLocation, 1f);

        //if the players are not loaded yet the loading animation will be displayed
        } else renderLoadingBar(xStart, yStart);


        //draws the strings with the minecraft fontRenderer
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD + username,xStart + 26, yStart +4, 0xffffff);
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GREEN + gamemode,xStart + 26, yStart + 13, 0xffffff);

    }

    /**
     * helper function to load the minecraft skin at "http://skins.minecraft.net/MinecraftSkins/<USERNAME>.png"
     * uses a callback class so the mainthread isn't stopped while loading the image
     * had to move to waiting code into a external thread ... so the mainthread is mot stopped
     * while waiting
     */
    private void loadSkinFromURL(){

        new Thread() {
            @Override
            public void run() {

                final ExecutorService service;
                final Future<BufferedImage> task;

                service = Executors.newSingleThreadExecutor();
                task = service.submit(new callURL());

                try {
                    image = task.get();
                    LoggerHelper.logInfo("[OnlineFriends]: Skin loaded for " + username);
                } catch(final InterruptedException ex) {
                    LoggerHelper.logError("[OnlineFriends]:Something went wrong while loading the skin for" + username);
                    ex.printStackTrace();
                } catch(final ExecutionException ex) {
                    LoggerHelper.logError("[OnlineFriends]:Something went wrong while loading the skin for" + username);
                    ex.printStackTrace();
                }

                imageLoaded = true;

                service.shutdownNow();
            }
        }.start();
    }

    /**
     * Helper class to get the image via url request and filereader
     */
    class callURL implements Callable<BufferedImage> {

        public BufferedImage call() throws Exception {
            LoggerHelper.logInfo("[OnlineFriends]: Going to load the skin for " + username);
            return ImageIO.read(new URL("http://skins.minecraft.net/MinecraftSkins/"+ username +".png"));
        }
    }


}



