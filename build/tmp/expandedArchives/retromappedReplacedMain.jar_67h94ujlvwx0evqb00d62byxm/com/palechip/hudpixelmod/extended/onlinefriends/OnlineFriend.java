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

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;


class OnlineFriend {

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
        loadSkinsFromURL();
    }

    String getUsername() {return username;}
    String getGamemode() {return gamemode;}
    void setGamemode(String gamemode) {this.gamemode = gamemode;}

    private void setupImage(){
        image = image.getSubimage(8,8,8,8);
        heigt = image.getHeight();
        width = image.getWidth();
        texture = new DynamicTexture(image);
        resourceLocation = Minecraft.func_71410_x().func_110434_K().func_110578_a(username, texture);
        imageLoaded = true;
    }

    private void loadSkinsFromURL() {
        final ExecutorService service;
        final Future<BufferedImage> task;

        service = Executors.newFixedThreadPool(1);
        task    = service.submit(new callURL(username));

        try {
            image = task.get();
            setupImage();
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();
        }

        service.shutdownNow();
    }

    void renderOnlineFriend(float xStart, float yStart){

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().field_71466_p;

        RenderUtils.renderBoxWithColor(xStart, yStart, 120, 23, 0, 1f, 1f, 1f, 0.15f);

        if(image != null && resourceLocation != null && imageLoaded)
            RenderUtils.drawModalRectWithCustomSizedTexture(
                    Math.round(xStart + 2), Math.round(yStart + 2), 0, 0,
                    20, 20, 20f, 20f , resourceLocation);

        fontRenderer.func_175063_a(EnumChatFormatting.GOLD + username,xStart + 26, yStart +3, 0xffffff);
        fontRenderer.func_175063_a(EnumChatFormatting.GREEN + gamemode,xStart + 26, yStart + 12, 0xffffff);
    }
}

class callURL implements Callable<BufferedImage> {
    private String username;

    callURL(String username){
        this.username = username;
    }
    public BufferedImage call() {
        try {
            BufferedImage image = ImageIO.read(new URL("http://skins.minecraft.net/MinecraftSkins/"+ username +".png"));
            return image;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
