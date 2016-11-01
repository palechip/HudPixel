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
package com.palechip.hudpixelmod;

import com.palechip.hudpixelmod.config.CCategory;
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.modulargui.IHudPixelModularGuiProviderBase;
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper;
import com.palechip.hudpixelmod.modulargui.components.TimerModularGuiProvider;
import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.util.ScoreboardReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.regex.Pattern;

public class GameDetector {
    public static final Pattern LOBBY_MATCHER = Pattern.compile("\\w*lobby\\d+");
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
    @ConfigPropertyBoolean(category = CCategory.HUDPIXEL, id = "gameDetector", def = true, comment = "Disable game detector (Risky!)")
    public static boolean enabled = true;
    private static GameType currentGameType = GameType.UNKNOWN;
    private static boolean isLobby = false;
    private static boolean gameHasntBegan = true;

    static {
        MinecraftForge.EVENT_BUS.register(new GameDetector());
    }

    private int cooldown = 0;
    private boolean schedule = false;
    private int scheduleWhereami = -1;

    public static boolean doesGameTypeMatchWithCurrent(GameType type) {
        switch (type) {
            case UNKNOWN:
                return currentGameType == GameType.UNKNOWN;
            case ALL_GAMES:
                return true;

            case QUAKECRAFT:
                return currentGameType == GameType.QUAKECRAFT;

            case THE_WALLS:
                return currentGameType == GameType.THE_WALLS;

            case PAINTBALL:
                return currentGameType == GameType.PAINTBALL;

            case BLITZ:
                return currentGameType == GameType.BLITZ;

            case TNT_GAMES:
            case BOW_SPLEEF:
            case TNT_RUN:
            case TNT_WIZARDS:
            case TNT_TAG:
            case ANY_TNT:
                return currentGameType == GameType.ANY_TNT;

            case VAMPIREZ:
                return currentGameType == GameType.VAMPIREZ;

            case MEGA_WALLS:
                return currentGameType == GameType.MEGA_WALLS;

            case ARENA:
                return currentGameType == GameType.ARENA;

            case UHC:
                return currentGameType == GameType.UHC;

            case COPS_AND_CRIMS:
                return currentGameType == GameType.COPS_AND_CRIMS;

            case WARLORDS:
                return currentGameType == GameType.WARLORDS;

            case ARCADE_GAMES:
            case BLOCKING_DEAD:
            case BOUNTY_HUNTERS:
            case BUILD_BATTLE:
            case CREEPER_ATTACK:
            case DRAGON_WARS:
            case ENDER_SPLEEF:
            case FARM_HUNT:
            case GALAXY_WARS:
            case PARTY_GAMES_1:
            case PARTY_GAMES_2:
            case TRHOW_OUT:
            case TURBO_KART_RACERS:
            case ANY_ARCADE:
            case FOOTBALL:
                return currentGameType == GameType.ANY_ARCADE;

            case SPEED_UHC:
                return currentGameType == GameType.SPEED_UHC;

            case CRAZY_WALLS:
                return currentGameType == GameType.CRAZY_WALLS;

            case SMASH_HEROES:
            case SMASH_HEROES_WOSPACE:
                return currentGameType == GameType.SMASH_HEROES || currentGameType == GameType.SMASH_HEROES_WOSPACE;

            case SKYWARS:
                return currentGameType == GameType.SKYWARS;

            default:
                return false;
        }
    }

    public static boolean isLobby() {
        return isLobby || gameHasntBegan;
    }

    public static boolean shouldProcessAfterstats() {
        return isLobby;
    }

    public static GameType getCurrentGameType() {
        return currentGameType;
    }

    public static String stripColor(String input) {
        if (input == null)
            return null;
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    @SubscribeEvent
    public void onServerChange(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayerSP) || !enabled) return;
        EntityPlayerSP player = (EntityPlayerSP) event.entity;
        player.sendChatMessage("/whereami");
        cooldown = 5;
    }


    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        scheduleWhereami = 10;
    }

    public void update(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "hypixel":
                currentGameType = GameType.UNKNOWN; //main lobby
                break;
            case "hypixel.net":
                ScoreboardReader.resetCache();
                schedule = true;
                break;
            case "":
            case " ":
                schedule = true;
                break;
            case " smash heroes":
            case "smash heroes":
                currentGameType = GameType.SMASH_HEROES;
            default:
                schedule = false;

                GameType game = currentGameType;
                for (GameType type : GameType.values())
                    if (s.toLowerCase().replace(" ", "").contains(type.scoreboardName.toLowerCase().replace(" ", ""))) {
                        currentGameType = type;
                        isLobby = false;
                        ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::setupNewGame);
                        ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameStart);
                    }
                if (game != currentGameType && Minecraft.getMinecraft().thePlayer != null) {
                    //success!
                    if (HudPixelMod.IS_DEBUGGING)
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Changed server! Game is now " + currentGameType));
                } else {
                    currentGameType = GameType.UNKNOWN;
                    schedule = true;
                }

                cooldown = -1;
                break;
        }
    }

    @SubscribeEvent
    public void tickly(TickEvent.ClientTickEvent event) {
        if (!enabled) return;
        String title = ScoreboardReader.getScoreboardTitle();
        title = stripColor(title).toLowerCase();
        cooldown--;
        scheduleWhereami--;
        if (schedule || cooldown == 0) update(title);
        if (scheduleWhereami == 0 && Minecraft.getMinecraft().thePlayer != null) {
            scheduleWhereami = -1;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/whereami");
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!enabled) return;
        String message = event.message.getUnformattedText();
        if (message.equalsIgnoreCase("The game starts in 1 second!")) {
            HudPixelExtendedEventHandler.onGameStart();
            gameHasntBegan = false;
            TimerModularGuiProvider.instance.onGameStart();
        }
        if (message.equalsIgnoreCase("                            Reward Summary")) {
            HudPixelExtendedEventHandler.onGameEnd();
            gameHasntBegan = true;
        }
        if (message.toLowerCase().contains("currently on server".toLowerCase())) {
            if (LOBBY_MATCHER.asPredicate().test(message)) { //lobby
                isLobby = true;
                gameHasntBegan = true;
                ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameEnd);
            }
            event.setCanceled(true);
        }
    }
}
