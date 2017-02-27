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
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.FrameBufferObj;
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
        frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), true);
    }

    public void updateFrame(){
        requireFrameUpdate = true;
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {

        if(event.getID() == EnumDefaultEvents.SCALE_CHANGED.get()){
            frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), true);
        }

        return super.doEventBus(event);

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

        GlStateManager.pushMatrix();

        frameBuffer.framebufferRenderTexture(x - 5, y, getWidth() , getHeight());

        GlStateManager.popMatrix();

        if(!requireFrameUpdate) return false;

        GlStateManager.pushMatrix();

        //frameBuffer.framebufferClear();

        frameBuffer.framebufferClear();
        frameBuffer.deleteFramebuffer();

        frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), true);

        frameBuffer.bindFramebuffer(false);

        GlStateManager.viewport(0, 0, Display.getWidth(), Display.getHeight() );

        if(this.doRenderTickLocal(x, y, getWidth(), getHeight(), EnumEventState.PRE)){
            for(IChild child : getChilds()){
                child.onRender(x, y);
            }
        }

        this.doRenderTickLocal(x, y, getWidth(), getHeight(), EnumEventState.POST);

        frameBuffer.unbindFramebuffer();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        GlStateManager.popMatrix();

        return false;

    }
}
