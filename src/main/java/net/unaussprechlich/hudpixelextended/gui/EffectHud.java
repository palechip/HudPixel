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

package net.unaussprechlich.hudpixelextended.gui;

import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.config.ConfigPropertyInt;
import eladkay.hudpixel.util.DisplayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler;
import net.unaussprechlich.hudpixelextended.util.IEventHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EffectHud implements IEventHandler {

    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "disablePotionHud", comment = "Disables the PotionHud", def = false)
    private static boolean disable_PotionHud = false;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderRightPotionHud", comment = "Renders the PotionHud on the right side", def = true)
    private static boolean renderRight_PotionHud = true;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "playSoundPotionHud", comment = "Plays a sound when a potion effect runs out.", def = true)
    private static boolean playSound_PotionHud = true;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderBottomPotionHud", comment = "Renders the PotionHud on the bottom side", def = true)
    private static boolean renderBottom_PotionHud = true;
    @ConfigPropertyBoolean(category = CCategory.POTION_HUD, id = "renderVerticalPotionHud", comment = "Renders the PotionHud Vertical", def = true)
    private static boolean renderVertical_PotionHud = true;
    @ConfigPropertyInt(category = CCategory.POTION_HUD, id = "xOffsetPotionHud", comment = "x-offset", def = -21)
    private static int xOffset_PotionHud = 22;
    @ConfigPropertyInt(category = CCategory.POTION_HUD, id = "yOffsetPotionHud", comment = "y-offset", def = -1)
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

    public void init() {
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    private EffectHud() {
    }

    public static boolean isPlaySound_PotionHud() {
        return playSound_PotionHud;
    }

    @Override
    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        effects.removeAll(effects
                .stream()
                .filter(e -> e.getDuration() <= 5)
                .collect(Collectors.toCollection(ArrayList::new))
        );

        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
        effects.removeAll(effects
                .stream()
                .filter(effect -> !(player.getActivePotionEffects().contains(effect.getPotionEffect())))
                .collect(Collectors.toCollection(ArrayList::new))
        );

        if (player == null || player.getActivePotionEffects() == null || disable_PotionHud) return;

        effects.addAll(player.getActivePotionEffects()
                .stream()
                .filter(potionEffect -> !isPotionEffectInEffects(potionEffect))
                .map(Effect::new)
                .collect(Collectors.toList())
        );

        int dHeight = DisplayUtil.getScaledMcHeight();
        int dWidth = DisplayUtil.getScaledMcWidth();

        if (renderBottom_PotionHud && renderVertical_PotionHud)
            yStart = dHeight - (effects.size() * size) + yOffset_PotionHud;
        else if (renderBottom_PotionHud)
            yStart = dHeight - size + yOffset_PotionHud;
        else
            yStart = yOffset_PotionHud;


        if (renderRight_PotionHud && !renderVertical_PotionHud)
            xStart = dWidth - (effects.size() * size) + xOffset_PotionHud;
        else if (renderRight_PotionHud)
            xStart = dWidth - size + xOffset_PotionHud;
        else
            xStart = xOffset_PotionHud;
    }


    private boolean isPotionEffectInEffects(PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax() || potionEffect.getIsAmbient())
            return true;

        for (Effect effect : effects) {
            if (potionEffect == effect.getPotionEffect())
                return true;
        }
        return false;
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        if ((mc.gameSettings.showDebugInfo) || (!mc.inGameHasFocus) || ((mc.currentScreen instanceof GuiChat)) || disable_PotionHud)
            return;

        if (renderVertical_PotionHud) {
            int buff = yStart;
            for (Effect effect : effects) {
                effect.onRender(xStart, buff);
                buff += size;
            }
        } else {
            int buff = xStart;
            for (Effect effect : effects) {
                effect.onRender(buff, yStart);
                buff += size;
            }
        }
    }
}