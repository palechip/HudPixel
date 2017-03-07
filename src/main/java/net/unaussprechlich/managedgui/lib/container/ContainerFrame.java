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
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.*;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;

/**
 * ContainerFrame Created by unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 **/
public abstract class ContainerFrame extends Container {

    private FrameBufferObj frameBuffer;
    private boolean requireFrameUpdate = true;

    public ContainerFrame(int width, int height, int minWidth, int minHeight, @Nonnull ColorRGBA color){
        this(width, height, color);
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public ContainerFrame(int width, int height, @Nonnull ColorRGBA color){
        setWidth(width);
        setHeight(height);
        setBackgroundRGBA(color);
        frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.INSTANCE.getMcScale(), getHeight() * DisplayUtil.INSTANCE.getMcScale(), false);
        frameBuffer.setFramebufferColor(color.getRedf(), color.getGreenf(), color.getBluef(), color.getAlphaf());
    }


    public void updateFrame(){
        requireFrameUpdate = true;
    }

    private boolean isResize = false;

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        if(clickType.equals(MouseHandler.ClickType.DRAG)){
            if(checkIfMouseOver(getXStart() + getWidth() - 10, getYStart() + getHeight() - 10,
                                getXStart() + getWidth(), getYStart() + getHeight())){
                isResize = true;
            }
        }

        if(clickType.equals(MouseHandler.ClickType.DROP)){
            if(isResize){
                isResize = false;
                frameBuffer.createDeleteFramebuffer(getWidth() * DisplayUtil.INSTANCE.getMcScale(), getHeight() * DisplayUtil.INSTANCE.getMcScale());
                onResize();
            }
        }
        return super.doClick(clickType);
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {
        if(event.getID() == EnumDefaultEvents.SCALE_CHANGED.get()){
            frameBuffer.createFramebuffer(getWidth() * DisplayUtil.INSTANCE.getMcScale(), getHeight() * DisplayUtil.INSTANCE.getMcScale());
        }
        return super.doEventBus(event);
    }

    @Override
    public boolean doClientTick() {
        if(isResize){
            if(MouseHandler.INSTANCE.getMX() - getXStart() + 5 > getMinWidth())
                setWidth(MouseHandler.INSTANCE.getMX() - getXStart() + 5);
            if(MouseHandler.INSTANCE.getMY() - getYStart() + 5 > getMinHeight())
                setHeight(MouseHandler.INSTANCE.getMY() - getYStart() + 5);
        }
        updateFrame();
        return super.doClientTick();
    }

    @Override
    public boolean doRender(int xStart, int yStart) {
        updateXYStart(xStart, yStart);
        if(!isVisible()) return false;

        if(isResize){
            RenderUtils.renderBoxWithColor(getXStart(), getYStart(), getWidth(), getHeight(), getBackgroundRGBA());
            RenderUtils.iconRender_resize(getXStart() + getWidth(), getYStart() + getHeight());
            return false;
        }

        GlStateManager.pushMatrix();
        frameBuffer.framebufferRenderTexture(getXStart(), getYStart(), getWidth() , getHeight());
        GlStateManager.popMatrix();

        if(!requireFrameUpdate) return false;

        final int x = 0;
        int y = DisplayUtil.INSTANCE.getScaledMcHeight() - getHeight() ;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        frameBuffer.framebufferClear();

        //frameBuffer.deleteFramebuffer();
        //frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), false);
        //frameBuffer = new FrameBufferObj(Display.getWidth(), Display.getHeight(), false);

        frameBuffer.bindFramebuffer(false);
        GlStateManager.viewport(0, 0, Display.getWidth(), Display.getHeight() );

        if(this.doRenderTickLocal(x, y, getWidth(), getHeight(), EnumEventState.PRE)){
            for(IChild child : getChilds()){
                child.onRender(x, y);
            }
        }

        this.doRenderTickLocal(x, y,  getWidth() , getHeight(), EnumEventState.POST);

        RenderUtils.iconRender_resize(x + getWidth(), y + getHeight());

        frameBuffer.unbindFramebuffer();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        return false;
    }
}
