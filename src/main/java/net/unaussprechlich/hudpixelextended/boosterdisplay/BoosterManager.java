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

package net.unaussprechlich.hudpixelextended.boosterdisplay;

import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.config.ConfigPropertyInt;
import eladkay.hudpixel.config.GeneralConfigSettings;
import eladkay.hudpixel.util.GameType;
import net.hypixel.api.reply.BoostersReply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.hypixelapi.ApiQueueEntryBuilder;
import net.unaussprechlich.hudpixelextended.hypixelapi.callbacks.BoosterResponseCallback;
import net.unaussprechlich.hudpixelextended.hypixelapi.representations.Booster;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;
import net.unaussprechlich.hudpixelextended.util.gui.FancyListManager;
import net.unaussprechlich.hudpixelextended.util.gui.FancyListObject;

import java.util.List;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class BoosterManager extends FancyListManager implements BoosterResponseCallback {

//######################################################################################################################

    private static final int REQUEST_COOLDOWN = 10 * 60 * 1000; // = 10min
    /**
     * Enter a  new gamemode with booster here, the system will add the booster then!
     * Also please upload the gameicon to the resource folder and link it in util.ImageLoader
     * Also please add the new gamemode with the right ID and right nm (put there the nm it says
     * when tipping somebody in this gamemode) to the GameType enum class. Also add the right tipname in the
     * GameType enum!
     **/
    private final static GameType[] gamesWithBooster = new GameType[]{
            GameType.SPEED_UHC,
            GameType.SMASH_HEROES,
            GameType.CRAZY_WALLS,
            GameType.SKYWARS,
            GameType.TURBO_KART_RACERS,
            GameType.WARLORDS,
            GameType.MEGA_WALLS,
            GameType.UHC,
            GameType.BLITZ,
            GameType.COPS_AND_CRIMS,
            GameType.THE_WALLS,
            GameType.ARCADE_GAMES,
            GameType.ARENA,
            GameType.PAINTBALL,
            GameType.TNT_GAMES,
            GameType.VAMPIREZ,
            GameType.QUAKECRAFT
    };
    @ConfigPropertyInt(category = CCategory.BOOSTER_DISPLAY, id = "xOffsetBoosterDisplay", comment = "X offset of Booster display", def = 2)
    public static int xOffsetBoosterDisplay = 2;
    @ConfigPropertyInt(category = CCategory.BOOSTER_DISPLAY, id = "yOffsetBoosterDisplay", comment = "Y offset of Booster display", def = 2)
    public static int yOffsetBoosterDisplay = 2;
    @ConfigPropertyBoolean(category = CCategory.BOOSTER_DISPLAY, id = "shownBooosterDisplayRight", comment = "Show booster display on right", def = true)
    public static boolean shownBooosterDisplayRight = true;
    @ConfigPropertyInt(category = CCategory.BOOSTER_DISPLAY, id = "boostersShownAtOnce", comment = "Boosters Shown at Once", def = 5)
    public static int boostersShownAtOnce = 5;
    @ConfigPropertyBoolean(category = CCategory.BOOSTER_DISPLAY, id = "isBoosterDisplay", comment = "Enable or disable the BoosterDisplay", def = true)
    public static boolean enabled = false;

//######################################################################################################################
    /**
     * do some things while the gametick ... you should also send the tip
     * to each FancyListObject
     */
    int count = 0;
    private long lastRequest;

    /**
     * sets the settings for the fancyListManager and also generates all boosters
     * in the gamesWithBooster array.
     */
    public BoosterManager() {
        super(5, xOffsetBoosterDisplay, yOffsetBoosterDisplay, shownBooosterDisplayRight); //this sets how many boosters are displayed at once you can change that
        this.isButtons = true;
        for (GameType g : gamesWithBooster) {
            this.fancyListObjects.add(new BoosterExtended(g));
        }
        this.shownObjects = boostersShownAtOnce;
        this.yStart = yOffsetBoosterDisplay;
        this.xStart = xOffsetBoosterDisplay;
        this.renderRightSide = shownBooosterDisplayRight;
    }


    @Override
    public int getConfigxStart() {
        return xOffsetBoosterDisplay;
    }

    @Override
    public boolean getConfigRenderRight() {
        return shownBooosterDisplayRight;
    }

    @Override
    public int getConfigyStart() {
        return yOffsetBoosterDisplay;
    }

    /**
     * Well you can do some stuff here befor rendering the display
     * You still have to call the renderDisplay() method ... otherwise there
     * will be nothing shown.
     */
    @Override
    public void onRender() {
        if (!enabled) return;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat && Minecraft.getMinecraft().displayHeight > 600) {
            this.renderDisplay();
            this.isMouseHander = true;
        } else {
            this.isMouseHander = false;
        }
    }

    @Override
    public void onClientTick() {
        requestBoosters(false);
        fancyListObjects.forEach(FancyListObject::onClientTick);
    }

    /**
     * Filters out the tipped message and notifies the BoosterExtended that is had been tipped.
     *
     * @param e The chatEvent
     */
    @Override
    public void onChatReceived(ClientChatReceivedEvent e) {
        String chat = e.getMessage().getUnformattedText();
        if (!chat.contains("You tipped ") || chat.contains(":")) return;

        String[] split = chat.split(" ");
        String player = split[2];
        String gamemode = split[4];

        for (int i = 5; i < split.length; i++)
            gamemode += (" " + split[i]);

        GameType gameType = GameType.Companion.getTypeByName(gamemode);

        for (FancyListObject f : fancyListObjects) {
            BoosterExtended b = (BoosterExtended) f;
            if (b.getGameType() == gameType)
                b.setGameModeTipped(player);
        }
    }

    /**
     * This requests the boosters via the api hypixelapi
     *
     * @param forceRequest Set this to true if you want to force the request
     */
    public void requestBoosters(Boolean forceRequest) {
        if (GeneralConfigSettings.getUseAPI() && enabled) {
            // isHypixelNetwork if enough time has past
            if ((System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN)) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                ApiQueueEntryBuilder.newInstance().boosterRequest().setCallback(this).create();
            }
        }
    }

    public void checkBooster(Booster b) {
        for (FancyListObject fco : fancyListObjects) {
            BoosterExtended be = (BoosterExtended) fco;
            if (be.getGameType() == b.getGameType()) {
                if (be.getBooster() != null && Objects.equals(be.getBooster().getOwner(), b.getOwner())) {
                    return;
                } else {
                    be.setCurrentBooster(b);
                    LoggerHelper.logInfo("[BoosterDisplay]: stored booster with ID " + b.getGameType().getNm() + "[" + b.getGameType().getDatabaseID() + "]"
                            + " and owner " + b.getOwner() + " in the boosterdisplay!");
                    return;
                }
            }
        }

        LoggerHelper.logWarn("[BoosterDisplay]: No display found for booster with ID "
                + b.getGameType().getDatabaseID() + " and owner " + b.getOwner() + "!");
    }

    /**
     * This method gets called when there is a booster response
     * Sorry for this messy if-for but somehow it works :P
     *
     * @param boosters the boosters parsed by the callback
     */
    @Override
    public void onBoosterResponse(List<BoostersReply.Booster> boosters) {

        // we aren't loading anymore
        if (boosters != null) {
            for (BoostersReply.Booster b : boosters) {
                if (b.getLength() < b.getOriginalLength()) {
                    new Booster(b.getPurchaserUuid().toString(), GameType.Companion.getTypeByDatabaseID(b.getGameType().getId()));
                }
            }
        } else LoggerHelper.logWarn("[BoosterDisplay]: The buuster response was NULL!");
    }
}
