package com.palechip.hudpixelmod.modulargui;

import com.google.common.collect.Lists;
import com.palechip.hudpixelmod.modulargui.components.*;
import com.palechip.hudpixelmod.modulargui.modules.PlayGameModularGuiProvider;
import eladkay.modulargui.lib.ModularGuiRegistry;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class ModularGuiHelper {
    public static List<IHudPixelModularGuiProviderBase> providers = Lists.newArrayList();
    public static final ModularGuiRegistry.Element TITLE = new ModularGuiRegistry.Element("simp0", new SimpleTitleModularGuiProvider());
    //ik i have these backwards
    public static final ModularGuiRegistry.Element FPS = new ModularGuiRegistry.Element("Ping", new PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.FPS));
    public static final ModularGuiRegistry.Element PING = new ModularGuiRegistry.Element("FPS", new PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.PING));

    public static final ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element TIMER = new ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, new TimerModularGuiProvider());
    public static final ModularGuiRegistry.Element BLITZ_STAR_TRACKER = new ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, new BlitzStarTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element DEATHMATCH_TRACKER = new ModularGuiRegistry.Element("simp1", new BlitzDeathmatchNotifierModularGuiProvider());
    public static final ModularGuiRegistry.Element KILLSTREAK_TRACKER = new ModularGuiRegistry.Element("simp2", new KillstreakTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element TKR_TIMER = new ModularGuiRegistry.Element("simp3", new TkrTimerModularGuiProvider());
    public static final ModularGuiRegistry.Element VZ_BALANCE = new ModularGuiRegistry.Element(VZBalanceModularGuiProvider.TOTAL_COINS_DISPLAY_TEXT, new VZBalanceModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS2_KILLCOUNTER = new ModularGuiRegistry.Element("simp4", new WallsKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS3_KILLCOUNTER = new ModularGuiRegistry.Element("simp5", new MWKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element PB_KILLSTREAK_TRACKER = new ModularGuiRegistry.Element("simp6", new PaintballKillstreakTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element WARLORDS_DAMAGE_TRACKER = new ModularGuiRegistry.Element(EnumChatFormatting.RED + "Damage", new WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Damage));
    public static final ModularGuiRegistry.Element WARLORDS_HEALING_TRACKER = new ModularGuiRegistry.Element(EnumChatFormatting.GREEN + "Healing", new WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Healing));

    public static final ModularGuiRegistry.Element PLAY_GAME_MODULE = new ModularGuiRegistry.Element(EnumChatFormatting.DARK_RED + "Game", new PlayGameModularGuiProvider());
    public static void init() {
        //order matters
        ModularGuiRegistry.registerElement(TITLE);
        providers.add((IHudPixelModularGuiProviderBase)TITLE.provider);
        ModularGuiRegistry.registerElement(FPS);
        providers.add((IHudPixelModularGuiProviderBase)FPS.provider);
        ModularGuiRegistry.registerElement(PING);
        providers.add((IHudPixelModularGuiProviderBase)PING.provider);

        //order no longer matters
        ModularGuiRegistry.registerElement(COIN_COUNTER);
        providers.add((IHudPixelModularGuiProviderBase)COIN_COUNTER.provider);
        ModularGuiRegistry.registerElement(TIMER);
        providers.add((IHudPixelModularGuiProviderBase)TIMER.provider);
        ModularGuiRegistry.registerElement(BLITZ_STAR_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)BLITZ_STAR_TRACKER.provider);
        ModularGuiRegistry.registerElement(DEATHMATCH_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)DEATHMATCH_TRACKER.provider);
        ModularGuiRegistry.registerElement(KILLSTREAK_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)KILLSTREAK_TRACKER.provider);
        ModularGuiRegistry.registerElement(TKR_TIMER);
        providers.add((IHudPixelModularGuiProviderBase)TKR_TIMER.provider);
        ModularGuiRegistry.registerElement(VZ_BALANCE);
        providers.add((IHudPixelModularGuiProviderBase)VZ_BALANCE.provider);
        ModularGuiRegistry.registerElement(WALLS2_KILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase)WALLS2_KILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(WALLS3_KILLCOUNTER);
        providers.add((IHudPixelModularGuiProviderBase)WALLS3_KILLCOUNTER.provider);
        ModularGuiRegistry.registerElement(PB_KILLSTREAK_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)PB_KILLSTREAK_TRACKER.provider);
        ModularGuiRegistry.registerElement(WARLORDS_DAMAGE_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)WARLORDS_DAMAGE_TRACKER.provider);
        ModularGuiRegistry.registerElement(WARLORDS_HEALING_TRACKER);
        providers.add((IHudPixelModularGuiProviderBase)WARLORDS_HEALING_TRACKER.provider);

        ModularGuiRegistry.registerElement(PLAY_GAME_MODULE);
        providers.add((IHudPixelModularGuiProviderBase)PLAY_GAME_MODULE.provider);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent e) {
        for(IHudPixelModularGuiProviderBase provider : providers)
            provider.onChatMessage(e.message.getUnformattedText(), e.message.getFormattedText());
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent e) {
        for(IHudPixelModularGuiProviderBase provider : ModularGuiHelper.providers)
            provider.onTickUpdate();
    }
}
