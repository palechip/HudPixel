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
    public static ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static ModularGuiRegistry.Element TIMER = new ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, new TimerModularGuiProvider());
    public static ModularGuiRegistry.Element BLITZ_STAR_TRACKER = new ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, new BlitzStarTrackerModularGuiProvider());
    public static ModularGuiRegistry.Element DEATHMATCH_TRACKER = new ModularGuiRegistry.Element("", new BlitzDeathmatchNotifierModularGuiProvider());
    public static ModularGuiRegistry.Element KILLSTREAK_TRACKER = new ModularGuiRegistry.Element(KillstreakTrackerModularGuiProvider.CURRENT_KILLSTREAK_DISPLAY_TEXT, new KillstreakTrackerModularGuiProvider());
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
