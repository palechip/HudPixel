/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;

import static net.minecraft.client.Minecraft.getMinecraft;

public interface ILoadPlayerSkinLoadedCallback {

    default void onLoaded(BufferedImage image, String username){
        onPlayerSkinLoadedResponse(
                getResourceLocationSkin(image, username),
                getResourceLocationHead(image, username),
                username
        );
    }

    void onPlayerSkinLoadedResponse(ResourceLocation skin, ResourceLocation head, String name);

    default ResourceLocation getResourceLocationSkin(BufferedImage image, String username){
        DynamicTexture texture = new DynamicTexture(image);
        return getMinecraft().getTextureManager().getDynamicTextureLocation(username, texture);
    }

    default ResourceLocation getResourceLocationHead(BufferedImage image, String username){
        BufferedImage image2 = image.getSubimage(8, 8, 8, 8);
        DynamicTexture texture = new DynamicTexture(image2);
        return  getMinecraft().getTextureManager().getDynamicTextureLocation(username + "_head", texture);
    }
}
