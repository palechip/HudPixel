/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static javax.imageio.ImageIO.read;

@SideOnly(Side.CLIENT)
public class LoadPlayerHead{

    private BufferedImage image;
    private ResourceLocation resourceLocation;

    private String                        username;
    private ILoadPlayerSkinLoadedCallback callback;

    public LoadPlayerHead(String username, ILoadPlayerSkinLoadedCallback callback) {
        this.callback = callback;
        this.username = username;
        loadSkinFromURL();
    }

    /**
     * helper function to load the minecraft skin at "http://skins.minecraft.net/MinecraftSkins/<USERNAME>.png"
     * uses a callback class so the mainthread isn't stopped while loading the image
     * had to move to waiting code into a external thread ... so the mainthread is mot stopped
     * while waiting
     */
    private void loadSkinFromURL() {
        new Thread(() -> {
            final ExecutorService service;
            final Future<BufferedImage> task;

            service = newSingleThreadExecutor();
            task = service.submit(new callURL());

            try {
                callback.onLoaded(task.get(), username);
                LoggerHelperMG.INSTANCE.logInfo("[LoadPlayerSkin]: Skin loaded for " + username);
            } catch (InterruptedException | ExecutionException e) {
                LoggerHelperMG.INSTANCE.logWarn("[LoadPlayerSkin]: Couldn't load skin for " + username + " @ " + "http://skins.minecraft.net/MinecraftSkins/" + username + ".png");
                e.printStackTrace();
            } finally {
                service.shutdownNow();
            }

        }).start();
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
