package com.palechip.hudpixelmod.modulargui;

import com.google.common.collect.Lists;
import com.palechip.hudpixelmod.modulargui.components.*;
import eladkay.modulargui.lib.ModularGuiRegistry;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class ModularGuiHelper {
    public static List<IHudPixelModularGuiProviderBase> providers = Lists.newArrayList();
    public static final ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element TIMER = new ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, new TimerModularGuiProvider());
    public static final ModularGuiRegistry.Element BLITZ_STAR_TRACKER = new ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, new BlitzStarTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element DEATHMATCH_TRACKER = new ModularGuiRegistry.Element("simp1", new BlitzDeathmatchNotifierModularGuiProvider());
    public static final ModularGuiRegistry.Element KILLSTREAK_TRACKER = new ModularGuiRegistry.Element("simp2", new KillstreakTrackerModularGuiProvider());
    public static final ModularGuiRegistry.Element TKR_TIMER = new ModularGuiRegistry.Element("simp3", new TkrTimerModularGuiProvider());
    public static final ModularGuiRegistry.Element VZ_BALANCE = new ModularGuiRegistry.Element(VZBalanceModularGuiProvider.TOTAL_COINS_DISPLAY_TEXT, new VZBalanceModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS2_KILLCOUNTER = new ModularGuiRegistry.Element("simp4", new WallsKillCounterModularGuiProvider());
    public static final ModularGuiRegistry.Element WALLS3_KILLCOUNTER = new ModularGuiRegistry.Element("simp5", new MWKillCounterModularGuiProvider());
    public static void init() {
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
