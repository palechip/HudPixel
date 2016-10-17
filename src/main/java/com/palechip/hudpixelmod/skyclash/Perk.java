package com.palechip.hudpixelmod.skyclash;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;

public class Perk implements INBTSerializable<NBTTagCompound>, Serializable {
    private static final long serialVersionUID = 14145145122L;
    public String name;
    public EnumType type;
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", name);
        tag.setInteger("type", type.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        name = nbt.getString("name");
        type = EnumType.values()[nbt.getInteger("type")];
    }
    public enum EnumType {
        LV1,
        LV2,
        LV3
    }
}
