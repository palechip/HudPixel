package com.palechip.hudpixelmod.modulargui;

import com.google.common.collect.Lists;
import com.palechip.hudpixelmod.modulargui.components.CoinCounterModularGuiProvider;
import eladkay.modulargui.lib.ModularGuiRegistry;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ModularGuiHelper {
    public static List<HudPixelModularGuiProvider> providers = Lists.newArrayList();
    public static ModularGuiRegistry.Element COIN_COUNTER = new ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, new CoinCounterModularGuiProvider());
    public static void init() {
        ModularGuiRegistry.registerElement(COIN_COUNTER);
        providers.add((HudPixelModularGuiProvider)COIN_COUNTER.provider);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent e) {
        for(HudPixelModularGuiProvider provider : providers)
            provider.onChatMessage(e.message.getUnformattedText(), e.message.getFormattedText());
    }
}
