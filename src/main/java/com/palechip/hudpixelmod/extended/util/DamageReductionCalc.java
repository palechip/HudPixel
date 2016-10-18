package com.palechip.hudpixelmod.extended.util;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

        private static Minecraft mc = Minecraft.getMinecraft();

        public static double helmet = 0.0;
        public static double chest = 0.0;
        public static double pants = 0.0;
        public static double boots = 0.0;

        public static int helmetProt = 0;
        public static int chestProt = 0;
        public static int pantsProt = 0;
        public static int bootsProt = 0;

        public static int resistance = 0;

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

            if(mc.thePlayer.inventory.armorInventory[3] != null) {
                getHelmet();
                getHelmetProtection();
            } else {
                helmet = 0.0;
                helmetProt = 0;
            }
            if(mc.thePlayer.inventory.armorInventory[2] != null) {
                getChestplate();
                getChestplateProtection();
            } else {
                chest = 0.0;
                chestProt = 0;
            }
            if(mc.thePlayer.inventory.armorInventory[1] != null) {
                getPants();
                getPantsProtection();
            } else {
                pants = 0.0;
                pantsProt = 0;
            }
            if(mc.thePlayer.inventory.armorInventory[0] != null) {
                getBoots();
                getBootsProtection();
            } else {
                boots = 0.0;
                bootsProt = 0;
            }
            if(mc.thePlayer.getActivePotionEffects() != null) {
                getResistance();
            } else {
                resistance = 0;
            }
        }

        private static void getHelmet() {
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).getItem()) == 298) {
                helmet = 0.04;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).getItem()) == 314) {
                helmet = 0.08;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).getItem()) == 302) {
                helmet = 0.08;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).getItem()) == 306) {
                helmet = 0.08;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).getItem()) == 310) {
                helmet = 0.12;
            }
        }

        private static void getChestplate() {
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).getItem()) == 299) {
                chest = 0.12;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).getItem()) == 315) {
                chest = 0.20;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).getItem()) == 303) {
                chest = 0.20;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).getItem()) == 307) {
                chest = 0.24;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).getItem()) == 311) {
                chest = 0.32;
            }
        }

        private static void getPants() {
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).getItem()) == 300) {
                pants = 0.08;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).getItem()) == 316) {
                pants = 0.12;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).getItem()) == 304) {
                pants = 0.16;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).getItem()) == 308) {
                pants = 0.20;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).getItem()) == 312) {
                pants = 0.24;
            }
        }

        private static void getBoots() {
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).getItem()) == 301) {
                boots = 0.04;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).getItem()) == 317) {
                boots = 0.04;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).getItem()) == 305) {
                boots = 0.04;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).getItem()) == 309) {
                boots = 0.08;
            }
            if(Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).getItem()) == 313) {
                boots = 0.12;
            }
        }

        private static void getResistance() {
            if(mc.thePlayer.isPotionActive(Potion.resistance)){
                Collection potionEffects = mc.thePlayer.getActivePotionEffects();
                Iterator it = potionEffects.iterator();
                while (it.hasNext())
                {
                    PotionEffect potionEffect = (PotionEffect)it.next();
                    Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                    if(potion.getName().equalsIgnoreCase(Potion.resistance.getName())) {
                        resistance = potionEffect.getAmplifier() + 1;
                    }
                }
            } else {
                resistance = 0;
            }
        }

        private static void getHelmetProtection() {
            if(helmet != 0) {
                helmetProt = EnchantmentHelper.getEnchantmentLevel(0, mc.thePlayer.inventory.armorItemInSlot(3));
            } else {
                helmetProt = 0;
            }
        }

        private static void getChestplateProtection() {
            if(chest != 0) {
                chestProt = EnchantmentHelper.getEnchantmentLevel(0, mc.thePlayer.inventory.armorItemInSlot(2));
            } else {
                chestProt = 0;
            }
        }

        private static void getPantsProtection() {
            if(pants != 0) {
                pantsProt = EnchantmentHelper.getEnchantmentLevel(0, mc.thePlayer.inventory.armorItemInSlot(1));
            } else {
                pantsProt = 0;
            }
        }

        private static void getBootsProtection() {
            if(boots != 0) {
                bootsProt = EnchantmentHelper.getEnchantmentLevel(0, mc.thePlayer.inventory.armorItemInSlot(0));
            } else {
                bootsProt = 0;
            }
        }

    }
}
