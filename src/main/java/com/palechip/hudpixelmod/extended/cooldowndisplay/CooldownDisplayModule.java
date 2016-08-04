package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CooldownDisplayModule {

    private ResourceLocation resourceLocation;
    private double coolDown;
    private int slotNumber;
    private static int size = 20;

    public CooldownDisplayModule(ResourceLocation resourceLocation, int slotNumber){
        this.resourceLocation = resourceLocation;
        this.slotNumber = slotNumber;
    }

    public void renderModule(float xStart, float yStart){
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        if(coolDown > 0)
            RenderUtils.renderBoxWithColor(xStart, yStart, size +2 , size +2, 0, 1f, 0f, 0f, 1f);
        else
            RenderUtils.renderBoxWithColor(xStart, yStart, size +2 , size +2, 0, 0f, 1f, 0f, 1f);
        RenderUtils.drawModalRectWithCustomSizedTexture(xStart + 1, yStart +1, size, size, size, size, size, size, resourceLocation, 1F);
        fontRenderer.drawString(coolDown + "", Math.round(xStart), Math.round(yStart + size/2),0xffffff); //draws the string over the image
    }

    public void onClientTick(){
        ItemStack iStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slotNumber);
        if(iStack.stackSize > 1)
            coolDown = iStack.stackSize;
        else
            coolDown = 0;
    }
}
