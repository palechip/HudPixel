package com.palechip.hudpixelmod.util;

import com.google.common.base.Throwables;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.invoke.MethodHandles.publicLookup;

public class HudPixelMethodHandles {

    @Nonnull
    private static final MethodHandle bookPages;

    static {
        try {
            Field bookP = ReflectionHelper.findField(GuiScreenBook.class, "field_146483_y", "bookPages", "y");
            bookPages = publicLookup().unreflectGetter(bookP);
        } catch (Throwable t) {
            Logger.getLogger("HudPixel").log(Level.SEVERE, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static NBTTagList getBookPages(GuiScreenBook book) {
        try {
            return (NBTTagList) bookPages.invokeExact(book);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        Logger.getLogger("HudPixel").log(Level.SEVERE, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }

}