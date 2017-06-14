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

package net.unaussprechlich.hudpixelextended.cooldowndisplay;


import eladkay.hudpixel.GameDetector;
import eladkay.hudpixel.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

import java.util.Objects;

public class CooldownDisplayModule {

    private static short size = 20;
    private String coolDown;
    private int id;
    private int meta;
    private int slot;
    private ItemStack itemStack;
    private boolean isHidden = false;

    public CooldownDisplayModule(int id, int meta, int slot) {
        this.id = id;
        this.meta = meta;
        this.slot = slot;
        itemStack = new ItemStack(Item.getItemById(id));
        if (meta > 0) itemStack.setItemDamage(meta);
    }

    public void renderModule(int xStart, int yStart) {
        if (isHidden) return;
        if (!Objects.equals(coolDown, "")) {
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, size, size, 1f, 0f, 0f, 0.5f);
        } else {
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, size, size, 0f, 1f, 0f, 0.5f);
        }
        RenderUtils.renderBoxWithColor(xStart - 1, yStart - 1, size - 2, size - 2, 0f, 0f, 0f, 0.6f);
        RenderUtils.renderItemStackWithText(id, meta, Math.round(xStart), Math.round(yStart), coolDown);

        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
        if (player.getHeldItemMainhand() != null
                && player.getHeldItemMainhand().equals(player.inventory.getStackInSlot(slot))) {
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, size, 1, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 3 + size, size, 1, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 3 + size, yStart - 2, 1, size, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, 1, size, 1f, 1f, 1f, 1f);

        }
    }

    public void onClientTick() {
        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
        if (player == null || player.inventory == null
                || player.inventory.getStackInSlot(slot) == null) {
            isHidden = true;
            return;
        }
        isHidden = false;
        ItemStack iStack = player.inventory.getStackInSlot(slot);
        if (GameDetector.getCurrentGameType() == GameType.WARLORDS && slot == 4 && iStack.getItemDamage() == 15)
            coolDown = ">1m";
        else if (iStack.getCount() > 1)
            coolDown = "" + iStack.getCount();
        else
            coolDown = "";
    }
}
