package eladkay.modulargui.lib.base;

import eladkay.modulargui.lib.IModularGuiProvider;
import net.minecraft.client.Minecraft;

/*
    This class is meant to show a correct implementation of IModularGuiProvider.
    This is a static implementation: all elements using it will always have the same value.

    @author Eladkay
    @since 1.6

 */
public class NameModularGuiProvider implements IModularGuiProvider {
    @Override
    public boolean showElement() {
        return true;
    } //elements using this provider will always be shown.

    @Override
    public String content() {
        if(Minecraft.getMinecraft().thePlayer == null) return ""; //if the player object is null (in the title screen etc) don't show anything
        return Minecraft.getMinecraft().thePlayer.getName(); //else just return the player's name
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }
}
