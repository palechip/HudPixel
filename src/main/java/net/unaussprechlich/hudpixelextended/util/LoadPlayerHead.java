package net.unaussprechlich.hudpixelextended.util;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static javax.imageio.ImageIO.read;
import static net.minecraft.client.Minecraft.getMinecraft;
import static net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler.registerIEvent;
import static net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler.unregisterIEvent;
import static net.unaussprechlich.hudpixelextended.util.LoggerHelper.logInfo;
import static net.unaussprechlich.hudpixelextended.util.LoggerHelper.logWarn;

@SideOnly(Side.CLIENT)
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
