package com.palechip.hudpixelmod.util;

import com.palechip.hudpixelmod.HudPixelMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public final class ContributorFancinessHandler implements LayerRenderer<EntityPlayer> {

    private volatile static Map<String, ItemStack> stacks = null;
    private volatile static boolean startedLoading = false;

    private static void firstStart() {
        if (!startedLoading) {
            new ThreadContributorListLoader();
            startedLoading = true;
        }
    }

    private static void load(Properties props) {
        stacks = new HashMap<>();
        System.out.println("Fanciness time!");
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            System.out.println("Loading fanciness for " + key + ": " + value);
            try {
                int i = Integer.parseInt(value);
                if (i < 0 || i >= 16)
                    throw new NumberFormatException();
                stacks.put(key.toLowerCase(), new ItemStack(Item.getItemById(i)));
            } catch (NumberFormatException e) {
                System.out.println("Ok, so it's not a number...");
                if (Item.getByNameOrId("minecraft:" + value) == null) {
                    System.out.println("It's a block!");
                    stacks.put(key.toLowerCase(), new ItemStack(Block.getBlockFromName("minecraft:" + value)));
                } else {
                    System.out.println("It's an item! " + Item.getByNameOrId("minecraft:" + value).getUnlocalizedName());
                    stacks.put(key.toLowerCase(), new ItemStack(Item.getByNameOrId("minecraft:" + value)));
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void renderStack(EntityPlayer player, ItemStack item) {
        if (item == null) System.out.println("Item is null? for " + player.getName());
        GlStateManager.pushMatrix();
        translateToHeadLevel(player);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0, -0.85, 0);
        GlStateManager.rotate(-90, 0, 1, 0);
        if (item.getItem() instanceof ItemBlock)
            GlStateManager.scale(1, 1, 1);
        else GlStateManager.scale(0.5, 0.5, 0.5);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    private static void translateToHeadLevel(EntityPlayer player) {
        GlStateManager.translate(0, -player.getDefaultEyeHeight(), 0);
        if (player.isSneaking())
            GlStateManager.translate(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
    }

    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        String name = player.getName();

        float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
        float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.rotate(yawOffset, 0, -1, 0);
        GlStateManager.rotate(yaw - 270, 0, 1, 0);
        GlStateManager.rotate(pitch, 0, 0, 1);

        firstStart();

        if (stacks.keySet().stream().map(String::toLowerCase).collect(Collectors.toList()).contains(name.toLowerCase()))
            renderStack(player, stacks.get(name.toLowerCase()));

        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private static class ThreadContributorListLoader extends Thread {

        public ThreadContributorListLoader() {
            setName("Contributor Fanciness Thread");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Eladkay/static/master/HudPixelSupporters");
                Properties props = new Properties();
                try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                    props.load(reader);
                    load(props);
                }
            } catch (IOException e) {
                HudPixelMod.instance().getLOGGER().info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
            }
        }

    }

}