package com.palechip.hudpixelmod.modulargui;

import com.google.common.collect.Lists;
import com.palechip.hudpixelmod.modulargui.components.BlitzStarTrackerModularGuiProvider;
import com.palechip.hudpixelmod.modulargui.components.CoinCounterModularGuiProvider;
import com.palechip.hudpixelmod.modulargui.components.TimerModularGuiProvider;
import eladkay.modulargui.lib.ModularGuiRegistry;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class ModularGuiHelper {
    public static List<HudPixelModularGuiProvider> providers = Lists.newArrayList();
    public static ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static ModularGuiRegistry.Element TIMER = new ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, new TimerModularGuiProvider());
    public static ModularGuiRegistry.Element BLITZ_STAR_TRACKER = new ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, new BlitzStarTrackerModularGuiProvider());
    public static void init() {
        ModularGuiRegistry.registerElement(COIN_COUNTER);
        providers.add((HudPixelModularGuiProvider)COIN_COUNTER.provider);
        ModularGuiRegistry.registerElement(TIMER);
        providers.add((HudPixelModularGuiProvider)TIMER.provider);
        ModularGuiRegistry.registerElement(BLITZ_STAR_TRACKER);
        providers.add((HudPixelModularGuiProvider)BLITZ_STAR_TRACKER.provider);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent e) {
        for(HudPixelModularGuiProvider provider : providers)
            provider.onChatMessage(e.message.getUnformattedText(), e.message.getFormattedText());
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent e) {
        for(HudPixelModularGuiProvider provider : ModularGuiHelper.providers)
            provider.onTickUpdate();
    }
}
