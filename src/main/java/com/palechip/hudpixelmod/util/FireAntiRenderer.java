package com.palechip.hudpixelmod.util;

import com.palechip.hudpixelmod.config.CCategory;
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FireAntiRenderer {
    @ConfigPropertyBoolean(category = CCategory.HUDPIXEL, id = "fireAntiRenderer", comment = "Do not render on fire if you are resistant", def = true)
    public static boolean enabled = false;
    static {
        MinecraftForge.EVENT_BUS.register(new FireAntiRenderer());
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null || !enabled) return;
        if(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.fireResistance) != null)
            HudPixelMethodHandles.setEntityImmuneToFire(Minecraft.getMinecraft().thePlayer, true);
        else HudPixelMethodHandles.setEntityImmuneToFire(Minecraft.getMinecraft().thePlayer, false);
    }
}
