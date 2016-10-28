package com.palechip.hudpixelmod.extended.util;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler.registerIEvent;
import static com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler.unregisterIEvent;
import static com.palechip.hudpixelmod.extended.util.LoggerHelper.logInfo;
import static com.palechip.hudpixelmod.extended.util.LoggerHelper.logWarn;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static javax.imageio.ImageIO.read;
import static net.minecraft.client.Minecraft.getMinecraft;

/* **********************************************************************************************************************
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 **********************************************************************************************************************/
public class LoadPlayerHead implements IEventHandler {

    private BufferedImage image;
    private ResourceLocation resourceLocation;
    private boolean imageLoaded = false;
    private boolean imageSetup = false;
    private String username;
    private ILoadPlayerHeadCallback callback;

    public LoadPlayerHead(String username, ILoadPlayerHeadCallback callback) {
        registerIEvent(this);
        this.callback = callback;
        this.username = username;
        loadSkinFromURL();
    }

    private void setupImage() {
        imageSetup = true;
        if (image == null) {
            callback.onLoadPlayerHeadResponse(null);
            unregisterIEvent(this);
            return;
        }
        image = image.getSubimage(8, 8, 8, 8);
        DynamicTexture texture = new DynamicTexture(image);
        resourceLocation = getMinecraft().getTextureManager().getDynamicTextureLocation(username, texture);
        logInfo("[LoadPlayer]: Loaded skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/" + username + ".png");
        callback.onLoadPlayerHeadResponse(this.resourceLocation);
        unregisterIEvent(this);
    }

    /**
     * helper function to load the minecraft skin at "http://skins.minecraft.net/MinecraftSkins/<USERNAME>.png"
     * uses a callback class so the mainthread isn't stopped while loading the image
     * had to move to waiting code into a external thread ... so the mainthread is mot stopped
     * while waiting
     */
    private void loadSkinFromURL() {

        new Thread() {
            @Override
            public void run() {

                final ExecutorService service;
                final Future<BufferedImage> task;

                service = newSingleThreadExecutor();
                task = service.submit(new callURL());
                boolean failed;
                try {
                    image = task.get();
                    logInfo("[LoadPlayer]: Skin loaded for " + username);
                } catch (final InterruptedException ex) {
                    logWarn("[LoadPlayer]:Something went wrong while loading the skin for" + username);
                    ex.printStackTrace();
                } catch (final ExecutionException ex) {
                    logWarn("[LoadPlayer]:Something went wrong while loading the skin for" + username);
                    ex.printStackTrace();
                    try {
                        image = read(new URL("http://skins.minecraft.net/MinecraftSkins/" + username + ".png"));
                        imageLoaded = true;
                    } catch (MalformedURLException e) {
                        failed = true;
                        logWarn("[LoadPlayer]: Couldn't load skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/" + username + ".png");
                    } catch (IOException e) {
                        failed = true;
                        logWarn("[LoadPlayer]: Couldn't read skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/" + username + ".png");
                    }
                }

                imageLoaded = true;

                service.shutdownNow();
            }
        }.start();
    }

    @Override
    public void onClientTick() {
        if (imageLoaded && !imageSetup) setupImage();
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

    /**
     * Helper class to get the image via url request and filereader
     */
    class callURL implements Callable<BufferedImage> {

        public BufferedImage call() throws Exception {
            return read(new URL("http://skins.minecraft.net/MinecraftSkins/" + username + ".png"));
        }
    }


}
