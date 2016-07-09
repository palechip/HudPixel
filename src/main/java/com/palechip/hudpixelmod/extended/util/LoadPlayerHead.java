package com.palechip.hudpixelmod.extended.util;

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
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
public class LoadPlayerHead implements IEventHandler {

    private BufferedImage image;
    private ResourceLocation resourceLocation;
    private boolean imageLoaded = false;
    private boolean imageSetup = false;
    private boolean failed = false;
    private String username;
    private ILoadPlayerHeadCallback callback;

    public LoadPlayerHead(String username, ILoadPlayerHeadCallback callback){
        HudPixelExtendedEventHandler.registerIEvent(this);
        this.callback = callback;
        this.username = username;
        loadSkinFromURL();
    }

    private void setupImage(){
        imageSetup = true;
        if(image == null || failed) {
            callback.onLoadPlayerHeadResponse(null);
            HudPixelExtendedEventHandler.unregisterIEvent(this);
            return;
        }
        image = image.getSubimage(8,8,8,8);
        DynamicTexture texture = new DynamicTexture(image);
        resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(username, texture);
        LoggerHelper.logInfo("[LoadPlayer]: Loaded skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/"+ username +".png");
        callback.onLoadPlayerHeadResponse(this.resourceLocation);
        HudPixelExtendedEventHandler.unregisterIEvent(this);
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
                try {
                    image = ImageIO.read(new URL("http://skins.minecraft.net/MinecraftSkins/"+ username +".png"));
                    imageLoaded = true;
                } catch (MalformedURLException e) {
                    failed = true;
                    LoggerHelper.logWarn("[LoadPlayer]: Couldn't load skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/"+ username +".png");
                } catch (IOException e) {
                    failed = true;
                    LoggerHelper.logWarn("[LoadPlayer]: Couldn't read skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/"+ username +".png");
                }
            }
        }.start();
    }

    @Override
    public void onClientTick() {
        if(imageLoaded && !imageSetup) setupImage();
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) {

    }

    @Override
    public void onRender() {

    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {

    }

    @Override
    public void onMouseClick(int mX, int mY) {

    }



}
