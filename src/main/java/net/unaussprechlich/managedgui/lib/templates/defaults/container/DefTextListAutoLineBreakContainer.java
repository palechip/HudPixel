/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

import net.unaussprechlich.managedgui.lib.CONSTANTS;
import net.unaussprechlich.managedgui.lib.util.FontHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * DefTextListAutoLineBreakContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 **/
public class DefTextListAutoLineBreakContainer extends DefTextListContainer {

    @Nonnull
    private ArrayList<String> textList = new ArrayList<>();

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
    protected void updateSize(){
        super.setHeightLocal(super.getListSize() * CONSTANTS.TEXT_Y_OFFSET);
    }

    @Override
    public void addEntry(String s) {
        textList.add(s);
        updateRenderList();
    }

    private void updateRenderList(){
        ArrayList<String> renderList = new ArrayList<>();
        textList.forEach(s -> renderList.addAll(FontHelper.getFrontRenderer().listFormattedStringToWidth(s, getWidth())));
        super.setTextList(renderList);
    }

    public DefTextListAutoLineBreakContainer(ArrayList<String> textList, int width) {
        super(new ArrayList<>());
        this.textList = textList;
        super.setWidthLocal(width);
        updateRenderList();
    }

    @Override
    public void setWidth(int width){
        super.setWidthLocal(width);
        updateRenderList();
    }
}
