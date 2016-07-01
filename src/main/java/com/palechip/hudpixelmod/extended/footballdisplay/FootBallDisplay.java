package com.palechip.hudpixelmod.extended.footballdisplay;

import com.palechip.hudpixelmod.extended.HudPixelExtended;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEvent;
import com.palechip.hudpixelmod.extended.util.ImageLoader;
import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.HashMap;

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
public class FootBallDisplay implements IEvent {

    HashMap<String, FootBallPlayer> footBallPlayers = new HashMap<String, FootBallPlayer>();
    private boolean hasStarted;
    final static int size = 162; //offset
    final static int oX = 10; //offset
    final static int oY = 50; //offset

    private boolean flip;
    private Entity football;
    private ResourceLocation resourceLocation;

    public FootBallDisplay() {
        HudPixelExtendedEventHandler.registerIEvent(this);
        String playerNamer = Minecraft.getMinecraft().thePlayer.getName();
        footBallPlayers.put(playerNamer, new FootBallPlayer(playerNamer, Minecraft.getMinecraft().thePlayer));
        footBallPlayers.get(playerNamer).setOwner();
        resourceLocation = ImageLoader.soccerBallLocation();
    }

    private int count = 0;

    @Override
    public void onClientTick() {
        if (count > 200) {
            for (Entity e : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
                if (e instanceof EntityArmorStand) {
                    EntityArmorStand eas = (EntityArmorStand) e;
                    if (eas.getCurrentArmor(3) != null)
                        if (eas.getCurrentArmor(3).getDisplayName().equals("Oak Sapling")) {
                            football = e;
                        }
                }
                count = 0;
            }
        } else {
            count++;
        }

    }

    private void setupPlayers() {

        for (Entity e : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (e instanceof EntityItem) {
                EntityItem ei = (EntityItem) e;
                System.out.println(ei.getDisplayName());
                System.out.println(ei.getEntityItem().getDisplayName());
                System.out.println(ei.getEntityItem().getUnlocalizedName());
                System.out.println(ei.getEntityItem().getItem().getUnlocalizedName());
            }
            if (e != null && e instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) e;
                if (!footBallPlayers.containsKey(p.getName())) {
                    footBallPlayers.put(p.getName(), new FootBallPlayer(p.getName(), p));
                    if (p.getDisplayName().getUnformattedText().startsWith("§9[§9BLUE§9]")) {
                        footBallPlayers.get(p.getName()).setBlue();
                    } else if (p.getDisplayName().getUnformattedText().startsWith("§c[§cRED§c]")) {
                        footBallPlayers.get(p.getName()).setRed();
                    }
                }
            }
        }
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) {
        if (e.message.getUnformattedText().equals("                                  Football")) {
            setupPlayers();
            hasStarted = true;
        } else if (e.message.getUnformattedText().endsWith(Minecraft.getMinecraft().thePlayer.getName() + " joined the lobby!")
                && HudPixelExtended.footBallDisplay != null) {
            try {
                HudPixelExtendedEventHandler.unregisterIEvent(this);
                HudPixelExtended.footBallDisplay = null;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else if (e.message.getUnformattedText().equalsIgnoreCase("You have been placed on the BLUE team.")) {
            flip = true;
        }
    }

    @Override
    public void onRender() {
        final int b = 1; //border
        final int w = size + (2 * b); //width
        final int h = size + (3 * b); //height
        final int gX = 40; //goalX
        final int gY = 7; //goalY
        final int c = 20;

        if (!hasStarted) return;

        RenderUtils.renderBox(oX, oY, w, h, 0);

        //outline
        RenderUtils.renderBoxWithColor(oX, oY, w, b, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX, oY, b, h, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX + w - b, oY, b, h, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX, oY + h - b, w, b, 0, 1f, 1f, 1f, 1f);


        RenderUtils.renderBoxWithColor(oX, oY + ((h / 2)), (w / 2) - (c / 2), b, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX + (w / 2) + (c / 2), oY + ((h / 2)), (w / 2) - (c / 2), b, 0, 1f, 1f, 1f, 1f);

        RenderUtils.renderBoxWithColor(oX + (w / 2) - (c / 2), oY + ((h / 2)) - (c / 2), c, b, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX + (w / 2) - (c / 2), oY + ((h / 2)) - (c / 2), b, c, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX + (w / 2) - (c / 2), oY + ((h / 2)) + (c / 2), c, b, 0, 1f, 1f, 1f, 1f);
        RenderUtils.renderBoxWithColor(oX + (w / 2) + (c / 2), oY + ((h / 2)) - (c / 2), b, c + b, 0, 1f, 1f, 1f, 1f);

        if (flip) {
            //goals
            RenderUtils.renderBoxWithColor(oX + ((w / 2) - (gX / 2)), oY - gY + b, gX, gY, 0, 1f, 0f, 0f, 1f);
            RenderUtils.renderBoxWithColor(oX + ((w / 2) - (gX / 2)), oY + h - b, gX, gY, 0, 0f, 0f, 1f, 1f);


            for (FootBallPlayer fbp : footBallPlayers.values()) {
                fbp.onRender(h / 80, true);
            }

            final int hs = 15;

            RenderUtils.drawModalRectWithCustomSizedTexture(
                    oX + size / 2 + football.getPosition().getZ() * h / 80 - hs / 2,
                    oY + size / 2 - football.getPosition().getX() * h / 80 - hs / 2,
                    hs, hs, hs, hs, hs, hs, resourceLocation, 1f);
        } else {
            //goals
            RenderUtils.renderBoxWithColor(oX + ((w / 2) - (gX / 2)), oY - gY + b, gX, gY, 0, 0f, 0f, 1f, 1f);
            RenderUtils.renderBoxWithColor(oX + ((w / 2) - (gX / 2)), oY + h - b, gX, gY, 0, 1f, 0f, 0f, 1f);


            for (FootBallPlayer fbp : footBallPlayers.values()) {
                fbp.onRender(h / 80, false);
            }

            final int hs = 15;

            RenderUtils.drawModalRectWithCustomSizedTexture(
                    oX + size / 2 - football.getPosition().getZ() * h / 80 - hs / 2,
                    oY + size / 2 + football.getPosition().getX() * h / 80 - hs / 2,
                    hs, hs, hs, hs, hs, hs, resourceLocation, 1f);
        }
    }



    @Override
    public void handleScrollInput(int i) {

    }
}
