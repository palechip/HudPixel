package eladkay.hudpixel.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Currently not possible, sorry folks
 */
public class EnchantmentStringHandler {
    public static class EnchInfo {
        public final Enchantment ench;
        public final short level;
        public final ItemStack stack;

        public EnchInfo(Enchantment ench, short level, ItemStack stack) {
            this.ench = ench;
            this.level = level;
            this.stack = stack;
        }
    }

    public static class RenderTooltipEvent extends Event {

        public final float y;
        public final float x;
        public final ItemStack stack;

        public RenderTooltipEvent(float y, float x, ItemStack stack) {
            this.y = y;
            this.x = x;
            this.stack = stack;
        }
    }

    public static List<EnchInfo> getEnchants(ItemStack stack) {
        List<EnchInfo> list = Lists.newArrayList();
        for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); i++) {
            NBTTagCompound tag = stack.getEnchantmentTagList().getCompoundTagAt(i);
            short id = tag.getShort("id");
            short lvl = tag.getShort("lvl");
            Enchantment ench = Enchantment.REGISTRY.getObjectById(id);
            list.add(new EnchInfo(ench, lvl, stack));
        }
        return list;
    }

    public static String getEnchantString(EnchInfo info) {
        return info.ench.getTranslatedName(info.level);
    }

    public static List<String> map(List<EnchInfo> list) {
        return list.stream().map(EnchantmentStringHandler::getEnchantString).collect(Collectors.toList());
    }

    public static List<String> map(ItemStack stack) {
        return getEnchants(stack).stream().map(EnchantmentStringHandler::getEnchantString).collect(Collectors.toList());
    }

    static {
        MinecraftForge.EVENT_BUS.register(new EnchantmentStringHandler());
    }

    private static final String TAG_DYE = "Quark:ItemNameDye";

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void makeTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_DYE)) {
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            int len = font.getStringWidth(stack.getDisplayName());
            String spaces = "";
            while (font.getStringWidth(spaces) < len)
                spaces += " ";

            event.getToolTip().set(0, spaces);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderTooltip(RenderTooltipEvent event) {
        ItemStack stack = event.stack;
        if (stack != null && stack.hasTagCompound()) {
            int dye = stack.getTagCompound().hasKey(TAG_DYE) ? stack.getTagCompound().getInteger(TAG_DYE) : -1;
            if (dye != -1) {
                int rgb = ItemDye.DYE_COLORS[Math.min(15, dye)];
                Color color = new Color(rgb);
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(stack.getDisplayName(), event.x, event.y, color.getRGB());
            }
        }
    }


}
