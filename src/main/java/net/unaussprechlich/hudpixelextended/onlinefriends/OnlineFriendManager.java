/*
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
 */

package net.unaussprechlich.hudpixelextended.onlinefriends;

import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.config.ConfigPropertyInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;
import net.unaussprechlich.hudpixelextended.util.gui.FancyListManager;
import net.unaussprechlich.hudpixelextended.util.gui.FancyListObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class OnlineFriendManager extends FancyListManager implements IUpdater {

    private static final String JOINED_MESSAGE = " joined.";
    private static final String LEFT_MESSAGE = " left.";
    private static final int UPDATE_COOLDOWN_RENDERING = 10 * 1000; // = 10sec
    private static final int UPDATE_COOLDOWN_ONLINE = 2 * 60 * 1000; // = 2min

    @ConfigPropertyInt(category = CCategory.FRIENDS_DISPLAY, id = "xOffsetFriendsDisplay", comment = "X offset for friends display", def = 2)
    public static int xOffsetFriendsDisplay = 2;
    @ConfigPropertyInt(category = CCategory.FRIENDS_DISPLAY, id = "yOffsetFriendsDisplay", comment = "Y offset for friends display", def = 2)
    public static int yOffsetFriendsDisplay = 2;
    @ConfigPropertyInt(category = CCategory.FRIENDS_DISPLAY, id = "friendsShownAtOnce", comment = "Friends shown at once", def = 10)
    public static int friendsShownAtOnce = 2;
    @ConfigPropertyBoolean(category = CCategory.FRIENDS_DISPLAY, id = "shownFriendsDisplayRight", comment = "Show friends display on the right", def = false)
    public static boolean shownFriendsDisplayRight = false;
    @ConfigPropertyBoolean(category = CCategory.FRIENDS_DISPLAY, id = "hideOfflineFriends", comment = "Hide offline friends?", def = true)
    public static boolean hideOfflineFriends = true;
    @ConfigPropertyBoolean(category = CCategory.FRIENDS_DISPLAY, id = "isOnlineFriendsDisplay", comment = "Enable or disable the BoosterDisplay", def = true)
    public static boolean enabled = false;

    private static long lastUpdateRendering = 0;
    private static long lastUpdateOnline = 0;
    private static OnlineFriendManager instance;
    private static ArrayList<FancyListObject> localStorageFCO = new ArrayList<FancyListObject>();

    private OnlineFriendManager() {
        super(5, xOffsetFriendsDisplay, yOffsetFriendsDisplay, shownFriendsDisplayRight);
        this.isButtons = true;
        new OnlineFriendsLoader();
        this.renderRightSide = shownFriendsDisplayRight;
        this.shownObjects = friendsShownAtOnce;
    }

    public static OnlineFriendManager getInstance() {
        return instance == null ? instance = new OnlineFriendManager() : instance;
    }

    @Override
    public int getConfigxStart() {
        return xOffsetFriendsDisplay;
    }

    @Override
    public boolean getConfigRenderRight() {
        return shownFriendsDisplayRight;
    }

    @Override
    public int getConfigyStart() {
        return yOffsetFriendsDisplay;
    }

    void addFriend(FancyListObject fco) {
        localStorageFCO.add(fco);
    }

    private void updateRendering() {
        if ((System.currentTimeMillis() > lastUpdateRendering + UPDATE_COOLDOWN_RENDERING)) {
            lastUpdateRendering = System.currentTimeMillis();

            if (!localStorageFCO.isEmpty())

                localStorageFCO.forEach(FancyListObject::onClientTick);

            //sort the list to display only friends first
            Collections.sort(localStorageFCO, (f1, f2) -> {
                OnlineFriend o1 = (OnlineFriend) f1;
                OnlineFriend o2 = (OnlineFriend) f2;
                return Boolean.valueOf(o2.isOnline()).compareTo(o1.isOnline());
            });

            if (hideOfflineFriends) {
                ArrayList<FancyListObject> buff = new ArrayList<FancyListObject>();
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.isOnline()) buff.add(fco);
                }
                fancyListObjects = buff;
            } else {
                fancyListObjects = localStorageFCO;
            }
        }
    }

    @Override
    public void onClientTick() {
        if ((System.currentTimeMillis() > lastUpdateOnline + UPDATE_COOLDOWN_ONLINE) && !localStorageFCO.isEmpty() && enabled) {
            lastUpdateOnline = System.currentTimeMillis();
            new OnlineFriendsUpdater(this);
        }
        updateRendering();
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        for (String s : OnlineFriendsLoader.getAllreadyStored()) {
            if (e.getMessage().getUnformattedText().equalsIgnoreCase(s + JOINED_MESSAGE))
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.getUsername().equals(s)) {
                        of.setOnline(true);
                        of.setGamemode(TextFormatting.WHITE + "not loaded yet!");
                    }
                }
            else if (e.getMessage().getUnformattedText().equalsIgnoreCase(s + LEFT_MESSAGE))
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.getUsername().equals(s)) {
                        of.setOnline(false);
                        of.setGamemode(TextFormatting.DARK_GRAY + "currently offline");
                    }
                }
        }
    }

    @Override
    public void onRender() {
        if (!enabled) return;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat && lastUpdateRendering != 0 && OnlineFriendsLoader.isApiLoaded() && Minecraft.getMinecraft().displayHeight > 600) {
            this.renderDisplay();
            this.isMouseHander = true;
        } else {
            this.isMouseHander = false;
        }
    }

    @Override
    public void onUpdaterResponse(HashMap<String, String> onlineFriends) {
        if (onlineFriends == null) {
            LoggerHelper.logWarn("[OnlineFriends][Updater]: Something went wrong while calling a update!");
        } else if (!localStorageFCO.isEmpty()) {
            for (FancyListObject fco : localStorageFCO) {
                OnlineFriend of = (OnlineFriend) fco;
                if (onlineFriends.containsKey(of.getUsername())) {
                    of.setGamemode(onlineFriends.get(of.getUsername()));
                    of.setOnline(true);
                } else {
                    of.setGamemode(TextFormatting.DARK_GRAY + "currently offline");
                    of.setOnline(false);
                }
            }
        }
    }
}
