package com.palechip.hudpixelmod.extended.gui;

import com.palechip.hudpixelmod.config.CCategory;
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.config.ConfigPropertyInt;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.util.DisplayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EffectHud implements IEventHandler{

    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "disablePotionHud", comment = "Disables the PotionHud", def = false)
    private static boolean disable_PotionHud = false;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderRightPotionHud", comment = "Renders the PotionHud on the right side", def = true)
    private static boolean renderRight_PotionHud = true;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderBottomPotionHud", comment = "Renders the PotionHud on the bottom side", def =true )
    private static boolean renderBottom_PotionHud = true;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderVerticalPotionHud", comment = "Renders the PotionHud Vertical", def = true)
    private static boolean renderVertical_PotionHud = true;
    @ConfigPropertyInt(category = CCategory.POTION_HUD, id = "xOffsetPotionHud", comment = "x-offset", def = -21)
    private static int xOffset_PotionHud = 22;
    @ConfigPropertyInt(category = CCategory.POTION_HUD, id ="yOffsetPotionHud", comment = "y-offset", def = -1)
    private static int yOffset_PotionHud = -1;

    private static final int size = 19;
    private static EffectHud INSTANCE;
    private static ArrayList<Effect> effects = new ArrayList<>();
    private static int xStart = 0;
    private static int yStart = 0;

    public static EffectHud getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new EffectHud();
        return INSTANCE;
    }

    public void init(){
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    private EffectHud() {}

    @Override
    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        effects.removeAll(effects
                .stream()
                .filter(e -> e.getDuration() <= 5)
                .collect(Collectors.toCollection(ArrayList::new))
        );

        effects.removeAll(effects
                .stream()
                .filter(effect-> !(mc.thePlayer.getActivePotionEffects().contains(effect.getPotionEffect())))
                .collect(Collectors.toCollection(ArrayList::new))
        );

        if(mc.thePlayer == null || mc.thePlayer.getActivePotionEffects() == null || disable_PotionHud) return;

        effects.addAll(mc.thePlayer.getActivePotionEffects()
                .stream()
                .filter(potionEffect -> !isPotionEffectInEffects(potionEffect))
                .map(Effect::new)
                .collect(Collectors.toList())
        );

        int dHeight = DisplayUtil.getScaledMcHeight();
        int dWidth = DisplayUtil.getScaledMcWidth();

        if(renderBottom_PotionHud && renderVertical_PotionHud)
            yStart = dHeight  - (effects.size() * size) + yOffset_PotionHud;
        else if(renderBottom_PotionHud )
            yStart = dHeight - size + yOffset_PotionHud;
        else
            yStart = yOffset_PotionHud;


        if(renderRight_PotionHud && !renderVertical_PotionHud)
            xStart = dWidth - (effects.size() * size) + xOffset_PotionHud;
        else if(renderRight_PotionHud )
            xStart = dWidth - size + xOffset_PotionHud;
        else
            xStart = xOffset_PotionHud;
    }



    private boolean isPotionEffectInEffects(PotionEffect potionEffect){
        if(potionEffect.getIsPotionDurationMax() || potionEffect.getIsAmbient()){
            return true;
        }
        for (Effect effect: effects) {
            if(potionEffect == effect.getPotionEffect())
                return true;
        }
        return  false;
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        if ((mc.gameSettings.showDebugInfo) || (!mc.inGameHasFocus) || ((mc.currentScreen instanceof GuiChat)) || disable_PotionHud) return;

        if(renderVertical_PotionHud){
            int buff = yStart;
            for (Effect effect: effects) {
                effect.onRender(xStart, buff);
                buff += size;
            }
        } else {
            int buff = xStart;
            for (Effect effect: effects) {
                effect.onRender(buff, yStart);
                buff += size;
            }
        }
    }
}