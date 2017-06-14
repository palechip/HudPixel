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

package net.unaussprechlich.hudpixelextended.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DamageReductionCalc {

    public static List<String> armorReduct = new ArrayList<String>();

    public static List<String> getReduction() {
        armorReduct.clear();
        PlayerArmorInfo.getValues();
        double armor = PlayerArmorInfo.helmet + PlayerArmorInfo.chest + PlayerArmorInfo.pants + PlayerArmorInfo.boots;
        double epf = calcArmorEpf();
        double avgdef = addArmorProtResistance(armor, calcProtection(epf), PlayerArmorInfo.resistance);
        double mindef = addArmorProtResistance(armor, Math.ceil(epf / 2.0D), PlayerArmorInfo.resistance);
        double maxdef = addArmorProtResistance(armor, Math.ceil(epf < 20.0D ? epf : 20.0D), PlayerArmorInfo.resistance);
        double min = roundDouble(mindef * 100.0D);
        double max = roundDouble(maxdef * 100.0D);
        double avg = roundDouble(avgdef * 100.0D);
        armorReduct.add(0, min + "");
        armorReduct.add(1, max + "");
        armorReduct.add(2, avg + "");
        return armorReduct;
    }

    private static double addArmorProtResistance(double armor, double prot, int resi) {
        double protTotal = armor + (1.0D - armor) * prot * 0.04D;
        protTotal += (1.0D - protTotal) * resi * 0.2D;
        return protTotal < 1.0D ? protTotal : 1.0D;
    }

    private static double calcProtection(double armorEpf) {
        double protection = 0.0D;
        for (int i = 50; i <= 100; i++) {
            protection += (Math.ceil(armorEpf * i / 100.0D) < 20.0D ? Math.ceil(armorEpf * i / 100.0D) : 20.0D);
        }
        return protection / 51.0D;
    }

    private static double calcArmorEpf() {
        double prot = calcEpf(PlayerArmorInfo.helmetProt) + calcEpf(PlayerArmorInfo.chestProt) + calcEpf(PlayerArmorInfo.pantsProt) + calcEpf(PlayerArmorInfo.bootsProt);
        return prot < 25.0D ? prot : 25.0D;
    }

    private static double calcEpf(int prot) {
        return prot != 0 ? Math.floor((6.0D + Math.pow(prot, 2.0D)) * 0.25D) : 0.0D;
    }

    private static double roundDouble(double number) {
        double x = Math.round(number * 10000.0D);
        return x / 10000.0D;
    }

    public static class PlayerArmorInfo {

        public static double helmet = 0.0;
        public static double chest = 0.0;
        public static double pants = 0.0;
        public static double boots = 0.0;
        public static int helmetProt = 0;
        public static int chestProt = 0;
        public static int pantsProt = 0;
        public static int bootsProt = 0;
        public static int resistance = 0;
        private static EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();

        public static void getValues() {
            helmet = 0.0;
            chest = 0.0;
            pants = 0.0;
            boots = 0.0;
            helmetProt = 0;
            chestProt = 0;
            pantsProt = 0;
            bootsProt = 0;
            resistance = 0;

            if (player.inventory.armorItemInSlot(3) != null) {
                getHelmet();
                getHelmetProtection();
            } else {
                helmet = 0.0;
                helmetProt = 0;
            }
            if (player.inventory.armorItemInSlot(2) != null) {
                getChestplate();
                getChestplateProtection();
            } else {
                chest = 0.0;
                chestProt = 0;
            }
            if (player.inventory.armorItemInSlot(1) != null) {
                getPants();
                getPantsProtection();
            } else {
                pants = 0.0;
                pantsProt = 0;
            }
            if (player.inventory.armorItemInSlot(0) != null) {
                getBoots();
                getBootsProtection();
            } else {
                boots = 0.0;
                bootsProt = 0;
            }
            if (player.getActivePotionEffects() != null) {
                getResistance();
            } else {
                resistance = 0;
            }
        }

        private static void getHelmet() {
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(3).getItem()) == 298) {
                helmet = 0.04;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(3).getItem()) == 314) {
                helmet = 0.08;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(3).getItem()) == 302) {
                helmet = 0.08;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(3).getItem()) == 306) {
                helmet = 0.08;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(3).getItem()) == 310) {
                helmet = 0.12;
            }
        }

        private static void getChestplate() {
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(2).getItem()) == 299) {
                chest = 0.12;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(2).getItem()) == 315) {
                chest = 0.20;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(2).getItem()) == 303) {
                chest = 0.20;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(2).getItem()) == 307) {
                chest = 0.24;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(2).getItem()) == 311) {
                chest = 0.32;
            }
        }

        private static void getPants() {
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(1).getItem()) == 300) {
                pants = 0.08;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(1).getItem()) == 316) {
                pants = 0.12;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(1).getItem()) == 304) {
                pants = 0.16;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(1).getItem()) == 308) {
                pants = 0.20;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(1).getItem()) == 312) {
                pants = 0.24;
            }
        }

        private static void getBoots() {
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(0).getItem()) == 301) {
                boots = 0.04;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(0).getItem()) == 317) {
                boots = 0.04;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(0).getItem()) == 305) {
                boots = 0.04;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(0).getItem()) == 309) {
                boots = 0.08;
            }
            if (Item.getIdFromItem(player.inventory.armorItemInSlot(0).getItem()) == 313) {
                boots = 0.12;
            }
        }

        private static void getResistance() {
            Potion resistancePotion = Potion.REGISTRY.getObject(new ResourceLocation("resistance"));
            if (player.isPotionActive(resistancePotion)) {
                Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
                for (PotionEffect potionEffect : potionEffects) {
                    if (potionEffect.getPotion().getName().equalsIgnoreCase(resistancePotion.getName())) {
                        resistance = potionEffect.getAmplifier() + 1;
                    }
                }
            } else {
                resistance = 0;
            }
        }

        /**
         * Helper Method to get the Protection enchantment which isn't only an ID anymore.
         *
         * @return Returns the Enchantment object for the protection enchantment.
         */
        private static Enchantment getProtectionEnchantment() {
            return Enchantment.REGISTRY.getObject(new ResourceLocation("protection"));
        }

        private static void getHelmetProtection() {
            if (helmet != 0) {
                helmetProt = EnchantmentHelper.getEnchantmentLevel(getProtectionEnchantment(), player.inventory.armorItemInSlot(3));
            } else {
                helmetProt = 0;
            }
        }

        private static void getChestplateProtection() {
            if (chest != 0) {
                chestProt = EnchantmentHelper.getEnchantmentLevel(getProtectionEnchantment(), player.inventory.armorItemInSlot(2));
            } else {
                chestProt = 0;
            }
        }

        private static void getPantsProtection() {
            if (pants != 0) {
                pantsProt = EnchantmentHelper.getEnchantmentLevel(getProtectionEnchantment(), player.inventory.armorItemInSlot(1));
            } else {
                pantsProt = 0;
            }
        }

        private static void getBootsProtection() {
            if (boots != 0) {
                bootsProt = EnchantmentHelper.getEnchantmentLevel(getProtectionEnchantment(), player.inventory.armorItemInSlot(0));
            } else {
                bootsProt = 0;
            }
        }

    }
}
