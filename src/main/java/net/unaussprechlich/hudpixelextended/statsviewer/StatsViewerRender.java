/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package net.unaussprechlich.hudpixelextended.statsviewer;

import eladkay.hudpixel.util.GameType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.statsviewer.msc.IGameStatsViewer;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.client.renderer.Tessellator.getInstance;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR;
import static net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import static net.unaussprechlich.hudpixelextended.statsviewer.msc.StatsCache.getPlayerByName;
import static org.lwjgl.opengl.GL11.glNormal3f;

@SideOnly(Side.CLIENT)
public class StatsViewerRender {

    private static final int DURATION = 10000;

    private IGameStatsViewer iGameStatsViewer;
    private long expireTimestamp;

    StatsViewerRender(GameType gameType, UUID playerUUID) {
        this.iGameStatsViewer = getPlayerByName(playerUUID, gameType);
        this.expireTimestamp = currentTimeMillis() + DURATION;
    }

    /**
     * renders a string above a player copied from the original mc namerenderer
     *
     * @param renderer the renderer
     * @param str      the string to render
     * @param entityIn the entity to render above
     * @param x        x-cord
     * @param y        y-cord
     * @param z        z-cord
     */
    private static void renderName(RenderPlayer renderer, String str, EntityPlayer entityIn, double x, double y, double z) {
        FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        pushMatrix();
        translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
        glNormal3f(0.0F, 1.0F, 0.0F);
        rotate(-renderer.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        rotate(renderer.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        scale(-f1, -f1, f1);
        disableLighting();
        depthMask(false);
        disableDepth();
        enableBlend();

        tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        disableTexture2D();
        worldrenderer.begin(7, POSITION_COLOR);
        worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        enableDepth();
        depthMask(true);
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
        enableLighting();
        disableBlend();
        color(1.0F, 1.0F, 1.0F, 1.0F);
        popMatrix();
    }

    long getExpireTimestamp() {
        return expireTimestamp;
    }

    /**
     * Renders the stats above the player
     *
     * @param event RenderPlayerEvent
     */
    void onRenderPlayer(Pre event) {

        double offset = 0.3;
        int i = 1;

        if (this.iGameStatsViewer.getRenderList() != null) {
            for (String s : this.iGameStatsViewer.getRenderList()) {
                renderName(event.getRenderer(), s, event.getEntityPlayer(), event.getX(), event.getY() + (offset * i), event.getZ());
                i++;
            }
        } else {
            renderName(event.getRenderer(), "Loading stats ....", event.getEntityPlayer(), event.getX(), event.getY() + offset, event.getZ());
        }
    }
}
