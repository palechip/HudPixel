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

package eladkay.hudpixel.modulargui;

import com.google.common.collect.Lists;
import eladkay.hudpixel.modulargui.components.*;
import eladkay.hudpixel.modulargui.modules.PlayGameModularGuiProvider;
import eladkay.hudpixel.util.ChatMessageComposer;
import eladkay.modulargui.lib.ModularGuiRegistry;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.hudpixelextended.update.UpdateNotifier;
import net.unaussprechlich.hudpixelextended.util.McColorHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModularGuiHelper implements McColorHelper {
    public static final ModularGuiRegistry.Element TITLE = new ModularGuiRegistry.Element("simp0", new SimpleTitleModularGuiProvider());
    //ik i have these backwards
    public static final ModularGuiRegistry.Element FPS = new ModularGuiRegistry.Element("Ping", new PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.FPS));
    public static final ModularGuiRegistry.Element PING = new ModularGuiRegistry.Element("FPS", new PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.PING));
    public static final ModularGuiRegistry.Element AVGPROTECTION = new ModularGuiRegistry.Element("Average Protection", ArmorProtectionModularGuiProvider.INSTANCE);
    public static final ModularGuiRegistry.Element COORDS = new ModularGuiRegistry.Element("possimp", CoordsDisplayModularGuiProvider.INSTANCE);
    public static final ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element TIMER = new ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, TimerModularGuiProvider.instance);
    public static final ModularGuiRegistry.Element BLITZ_STAR_TRACKER = new ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, new BlitzStarTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element DEATHMATCH_TRACKER = new ModularGuiRegistry.Element("simp1", new BlitzDeathmatchNotifierModularGuiProvider());
    public static final ModularGuiRegistry.Element KILLSTREAK_TRACKER = new ModularGuiRegistry.Element("simp2", new KillstreakTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element TKR_TIMER = new ModularGuiRegistry.Element("simp3", new TkrTimerModularGuiProvider());
    public static final ModularGuiRegistry.Element VZ_BALANCE = new ModularGuiRegistry.Element(VZBalanceModularGuiProvider.TOTAL_COINS_DISPLAY_TEXT, new VZBalanceModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS2_KILLCOUNTER = new ModularGuiRegistry.Element("simp4", new WallsKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS3_KILLCOUNTER = new ModularGuiRegistry.Element("simp5", new MWKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element PB_KILLSTREAK_TRACKER = new ModularGuiRegistry.Element("simp6", new PaintballKillstreakTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element WARLORDS_DAMAGE_TRACKER = new ModularGuiRegistry.Element(TextFormatting.RED + "Damage", new WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Damage));
    public static final ModularGuiRegistry.Element WARLORDS_HEALING_TRACKER = new ModularGuiRegistry.Element(TextFormatting.GREEN + "Healing", new WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Healing));
    public static final ModularGuiRegistry.Element PLAY_GAME_MODULE = new ModularGuiRegistry.Element(TextFormatting.DARK_RED + "Game", new PlayGameModularGuiProvider());

    public static final ModularGuiRegistry.Element WALLS3_AKILLCOUNTER = new ModularGuiRegistry.Element("simp7", new MWAssistCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS3_AFKILLCOUNTER = new ModularGuiRegistry.Element("simp8", new MWFinalAssistCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS3_FKILLCOUNTER = new ModularGuiRegistry.Element("simp9", new MWFinalKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element DEATH = new ModularGuiRegistry.Element(DeathCounterModularGuiProvider.DEATH_TEXT, new DeathCounterModularGuiProvider());
    public static List<IHudPixelModularGuiProviderBase> providers = Lists.newArrayList();

    public static void init() {
        //order matters
        ModularGuiRegistry.registerElement(TITLE);
        providers.add((IHudPixelModularGuiProviderBase) TITLE.provider);
        ModularGuiRegistry.registerElement(FPS);
        providers.add((IHudPixelModularGuiProviderBase) FPS.provider);
        ModularGuiRegistry.registerElement(PING);
        providers.add((IHudPixelModularGuiProviderBase) PING.provider);
        ModularGuiRegistry.registerElement(AVGPROTECTION);
        providers.add((IHudPixelModularGuiProviderBase) AVGPROTECTION.provider);
        ModularGuiRegistry.registerElement(COORDS);
        providers.add((IHudPixelModularGuiProviderBase) COORDS.provider);
        //order no longer matters
        ModularGuiRegistry.registerElement(COIN_COUNTER);
        providers.add((IHudPixelModularGuiProviderBase) COIN_COUNTER.provider);
        ModularGuiRegistry.registerElement(TIMER);
        providers.add((IHudPixelModularGuiProviderBase) TIMER.provider);
        ModularGuiRegistry.registerElement(BLITZ_STAR_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) BLITZ_STAR_TRACKER.provider);
        ModularGuiRegistry.registerElement(DEATHMATCH_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) DEATHMATCH_TRACKER.provider);
        ModularGuiRegistry.registerElement(KILLSTREAK_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) KILLSTREAK_TRACKER.provider);
        ModularGuiRegistry.registerElement(TKR_TIMER);
        providers.add((IHudPixelModularGuiProviderBase) TKR_TIMER.provider);
        ModularGuiRegistry.registerElement(VZ_BALANCE);
        providers.add((IHudPixelModularGuiProviderBase) VZ_BALANCE.provider);
        ModularGuiRegistry.registerElement(WALLS2_KILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase) WALLS2_KILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(WALLS3_KILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase) WALLS3_KILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(PB_KILLSTREAK_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) PB_KILLSTREAK_TRACKER.provider);
        ModularGuiRegistry.registerElement(WARLORDS_DAMAGE_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) WARLORDS_DAMAGE_TRACKER.provider);
        ModularGuiRegistry.registerElement(WARLORDS_HEALING_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase) WARLORDS_HEALING_TRACKER.provider);
        ModularGuiRegistry.registerElement(WALLS3_AKILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase) WALLS3_AKILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(WALLS3_AFKILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase) WALLS3_AFKILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(WALLS3_FKILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase) WALLS3_FKILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(DEATH);
        providers.add((IHudPixelModularGuiProviderBase) DEATH.provider);

        ModularGuiRegistry.registerElement(PLAY_GAME_MODULE);
        providers.add((IHudPixelModularGuiProviderBase) PLAY_GAME_MODULE.provider);
    }

    private static ArrayList<String> processAfterstats() {
        ArrayList<String> renderList = new ArrayList<String>();

        //if (!GameDetector.shouldProcessAfterstats()) return renderList;

        /*
         * bitte was schönes hinmachen :D
         */

        renderList.add(" ");
        renderList.add(BLUE + UpdateNotifier.SEPARATION_MESSAGE);


        //collects all data
        for (ModularGuiRegistry.Element element : ModularGuiRegistry.allElements) {
            if (!element.provider.showElement() || element.provider.getAfterstats() == null || element.provider.getAfterstats().isEmpty())
                continue; //if you shouldn't show it, skip it.
            renderList.add(element.provider.getAfterstats());
        }

        /*
         * bitte was schönes hinmachen :D
         */

        renderList.add(BLUE + UpdateNotifier.SEPARATION_MESSAGE);
        renderList.add(" ");

        return renderList;
    }

    public static void onGameEnd() {
        ArrayList<String> renderList = processAfterstats();
        Collections.reverse(renderList);
        for (String s : renderList) {
            printMessage(s);
        }
    }

    /**
     * prints the message to the clientchat
     *
     * @param message the message
     **/
    private static void printMessage(String message) {
        new ChatMessageComposer(message).send();
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent e) {
        for (IHudPixelModularGuiProviderBase provider : providers)
            provider.onChatMessage(e.getMessage().getUnformattedText(), e.getMessage().getFormattedText());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent e) {
        ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onTickUpdate);
    }

}
