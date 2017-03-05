/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

import net.unaussprechlich.managedgui.lib.ConstantsMG;
import net.unaussprechlich.managedgui.lib.container.callback.ICallbackUpdateHeight;
import net.unaussprechlich.managedgui.lib.util.FontUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * DefTextListAutoLineBreakContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 **/
public class DefTextListAutoLineBreakContainer extends DefTextListContainer {

    @Nonnull
    private ArrayList<String> textList = new ArrayList<>();

    private ICallbackUpdateHeight callbackHeight;

    public int getListSize(){
        if(textList.isEmpty()) return 0;
        return textList.size();
    }

    public void clearAll(){
        textList.clear();
        updateRenderList();
    }

    public void removeEntry(String s){
        textList.removeAll(textList.stream()
                                   .filter(s1 -> s1.equalsIgnoreCase(s))
                                   .collect(Collectors.toList())
        );
        updateRenderList();
    }

    @Override
    protected boolean doResizeLocal(int width, int height) {
        updateRenderList();
        return super.doResizeLocal(width, height);
    }

    @Override
    protected void updateSize(){
        super.setHeightLocal(super.getListSize() * ConstantsMG.TEXT_Y_OFFSET);
        if(callbackHeight != null)
            callbackHeight.call(getHeight());
    }

    @Override
    public void addEntry(String s) {
        textList.add(s);
        updateRenderList();
    }

    private void updateRenderList(){
        ArrayList<String> renderList = new ArrayList<>();
        textList.forEach(s -> renderList.addAll(FontUtil.INSTANCE.getFontRenderer().listFormattedStringToWidth(s, getWidth())));
        super.setTextList(renderList);
    }

    public DefTextListAutoLineBreakContainer(ArrayList<String> textList, int width) {
        super(new ArrayList<>());
        this.textList = textList;
        super.setWidthLocal(width);
        updateRenderList();
    }

    public DefTextListAutoLineBreakContainer(String text, int width) {
        super(new ArrayList<>());
        this.textList = new ArrayList<>(Collections.singletonList(text));
        super.setWidthLocal(width);
        updateRenderList();
    }

    public DefTextListAutoLineBreakContainer(String text, int width, ICallbackUpdateHeight callback) {
        super(new ArrayList<>());
        this.textList = new ArrayList<>(Collections.singletonList(text));
        super.setWidthLocal(width);
        updateRenderList();
        this.callbackHeight = callback;
    }

    @Override
    public void setWidth(int width){
        super.setWidthLocal(width);
    }
}
