package com.palechip.hudpixelmod.skyclash;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;
import java.util.List;

public class SkyClashClass implements INBTSerializable<NBTTagCompound>, Serializable {
    private static final long serialVersionUID = 14145142L;
    List<Perk> perks = Lists.newArrayList();
    String name;
    SkyClashKit kit;


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(Perk perk : perks)
            list.appendTag(perk.serializeNBT());
        tag.setTag("list", list);
        tag.setString("name", name);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("list", nbt.getTagId("list"));
        List<Perk> arrayList = Lists.newArrayList();
        for(int i = 0; i < list.tagCount(); i++) {
            Perk perk = new Perk();
            perk.deserializeNBT(list.getCompoundTagAt(i));
            arrayList.add(perk);
        }
        perks = arrayList;
        name = nbt.getString("name");


    }
    public static SkyClashClass of() {
        return null;
    }
}

