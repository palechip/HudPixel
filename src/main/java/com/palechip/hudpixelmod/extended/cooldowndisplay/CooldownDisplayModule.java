package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.extended.util.RenderUtils;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CooldownDisplayModule {

    private String coolDown;
    private int id;
    private int meta;
    private int slot;
    private static int size = 20;
    private ItemStack itemStack;
    private boolean isHidden = false;

    public CooldownDisplayModule(int id, int meta, int slot){
        this.id = id;
        this.meta = meta;
        this.slot = slot;
        itemStack = new ItemStack(Item.getItemById(id));
        if(meta > 0) itemStack.setItemDamage(meta);
    }

    public void renderModule(float xStart, float yStart){
        if(isHidden)return;
        if(coolDown != ""){
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, size   , size   , 0, 1f, 0f, 0f, 0.5f);
        } else
            {
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2, size   , size   , 0, 0f, 1f, 0f, 0.5f);
        }
        RenderUtils.renderBoxWithColor(xStart - 1  , yStart - 1, size -2  , size -2  , 0, 0f, 0f, 0f, 0.6f);
        RenderUtils.renderItemStackWithText(id, meta, Math.round(xStart), Math.round(yStart), coolDown);
        if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().equals(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slot))){
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2       , size  , 1  , 0, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 3 + size, size  , 1  , 0, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 3 + size , yStart - 2,       1  , size  , 0, 1f, 1f, 1f, 1f);
            RenderUtils.renderBoxWithColor(xStart - 2, yStart - 2,        1  , size  , 0, 1f, 1f, 1f, 1f);

        }
    }

    public void onClientTick(){
        if(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slot) == null){
            isHidden = true;
            return;
        } else {
            isHidden = false;
        }
        ItemStack iStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slot);
        if(GameDetector.getCurrentGame().getConfiguration().getModID() == GameType.WARLORDS.getModID() && slot == 4 && iStack.getItemDamage() == 15)
            coolDown = ">1m";
        else if(iStack.stackSize > 1)
            coolDown = "" + iStack.stackSize;
        else
            coolDown = "";
    }
}
