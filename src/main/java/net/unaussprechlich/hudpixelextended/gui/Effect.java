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

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.unaussprechlich.hudpixelextended.util.SoundManager;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

public class Effect {

    private int startDuration;
    private PotionEffect potionEffect;
    private boolean soundPlayed = false;
    private static Minecraft mc = Minecraft.getMinecraft();


    public Effect(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
        this.startDuration = potionEffect.getDuration();

    }

    public int getDuration() {
        return potionEffect.getDuration();
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    private static String getAmpifireString(PotionEffect potionEffect) {
        switch (potionEffect.getAmplifier()) {
            case 0:
                return "I";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
            case 5:
                return "VI";
            case 6:
                return "VII";
            case 7:
                return "VIII";
            case 8:
                return "IX";
            case 9:
                return "X";
            default:
                return " " + (potionEffect.getAmplifier() + 1);
        }
    }

    private void renderCooldown(int xStart, int yStart) {
        if (potionEffect.getDuration() <= 5) return;
        double percent = (double) (potionEffect.getDuration()) / (double) (startDuration);
        double t = Math.round(percent * 72);
        double xS = xStart;
        double yS = yStart;

        float r = (float) (1f - percent);
        float g = (float) percent;

        if (t >= 63 && t < 72) {
            RenderUtils.renderBoxWithColor(xS + 9 + 72 - t, yS, 9 - 72 + t, 1, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS + 17, yS, 1, 18, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS + 17, 18, 1, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 1, 18, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 9, 1, r, g, 0, 1f);
        } else if (t >= 45 && t < 63) {
            RenderUtils.renderBoxWithColor(xS + 17, yS + 63 - t, 1, 18 - 63 + t, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS + 17, 18, 1, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 1, 18, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 9, 1, r, g, 0, 1f);
        } else if (t >= 27 && t < 45) {
            RenderUtils.renderBoxWithColor(xS, yS + 17, 18 - 45 + t, 1, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 1, 18, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 9, 1, r, g, 0, 1f);
        } else if (t >= 9 && t < 27) {
            RenderUtils.renderBoxWithColor(xS, yS, 1, 18 - 27 + t, r, g, 0, 1f);
            RenderUtils.renderBoxWithColor(xS, yS, 9, 1, r, g, 0, 1f);
        } else if (t > 0 && t < 9) {
            RenderUtils.renderBoxWithColor(xS + 9 - t, yS, t, 1, r, g, 0, 1f);
        }
    }

    public void onRender(int xStart, int yStart) {
        if (potionEffect.getDuration() > startDuration) {
            startDuration = potionEffect.getDuration() + 5;
        }
        RenderUtils.renderBox(xStart, yStart, 18, 18);
        RenderUtils.renderPotionIcon(xStart, yStart, potionEffect.getPotion());
        mc.fontRendererObj.drawStringWithShadow(getAmpifireString(potionEffect),
                xStart + +9 - (mc.fontRendererObj.getStringWidth(getAmpifireString(potionEffect)) / 2),
                yStart + 6,
                0xffffff);
        renderCooldown(xStart, yStart);
        if (potionEffect.getDuration() == 12 && !soundPlayed && EffectHud.isPlaySound_PotionHud()) {
            soundPlayed = true;
            SoundManager.playSound(SoundManager.Sounds.STAR_SOMETHING);
        }
    }
}
