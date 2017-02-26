/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.unaussprechlich.managedgui.lib.child.IChild;
import net.unaussprechlich.managedgui.lib.util.*;
import org.lwjgl.opengl.Display;

/**
 * ContainerFrame Created by unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 **/
public abstract class ContainerFrame extends Container {

    private FrameBufferObj frameBuffer ;
    private boolean requireFrameUpdate = true;

    public ContainerFrame(int width, int height, ColorRGBA colorRGBA) {
        setWidth(width);
        setHeight(height);
        setBackgroundRGBA(colorRGBA);
        frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), false);
    }

    public void updateFrame(){
        requireFrameUpdate = true;
    }


    @Override
    public boolean doClientTick() {
        updateFrame();
        return doClientTickLocal();
    }

    @Override
    public boolean doRender(int xStart, int yStart) {

        updateXYStart(xStart, yStart);
        if(!isVisible()) return false;

        final int x = getXStart();
        final int y = getYStart();

        int scale = DisplayUtil.getMcScale();

        frameBuffer.framebufferRenderTexture(x, y, getWidth() , getHeight());

        if(!requireFrameUpdate) return false;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        frameBuffer.deleteFramebuffer();

        frameBuffer = new FrameBufferObj(getWidth() * scale, getHeight() *scale, false);

        frameBuffer.bindFramebuffer(false);

        GlStateManager.viewport(0, 0, Display.getWidth(), Display.getHeight() );

        RenderUtils.renderBoxWithColor(x, y, getWidth(), getHeight(), getBackgroundRGBA());

        if(doRenderTickLocal(x, y, getWidth(), getWidth(), EnumEventState.PRE)){
            for(IChild child : getChilds()){
                child.onRender(x, y);
            }
        }

        doRenderTickLocal(x, y, getWidth(), getHeight(), EnumEventState.POST);

        frameBuffer.unbindFramebuffer();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        return false;

    }
}
