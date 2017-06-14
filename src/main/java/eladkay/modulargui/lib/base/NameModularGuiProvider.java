package eladkay.modulargui.lib.base;

import eladkay.modulargui.lib.IModularGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * This class is meant to show a correct implementation of IModularGuiProvider.
 * This is a static implementation: all templates using it will always have the same value.
 *
 * @author Eladkay
 * @since 1.6
 */
public class NameModularGuiProvider implements IModularGuiProvider {
    @Override
    public boolean showElement() {
        return true;
    } //templates using this provider will always be shown.

    @Override
    public String content() {
        if (FMLClientHandler.instance().getClientPlayerEntity() == null)
            return ""; //if the player object is null (in the title screen etc) don't show anything
        return FMLClientHandler.instance().getClientPlayerEntity().getName(); //else just return the player's nm
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }
}
