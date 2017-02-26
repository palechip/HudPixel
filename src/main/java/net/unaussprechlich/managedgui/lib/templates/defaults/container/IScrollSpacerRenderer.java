/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

/**
 * IScrollSpacerRenderer Created by Alexander on 26.02.2017.
 * Description:
 **/
public interface IScrollSpacerRenderer {

    void render(int xStart, int yStart, int width);

    int getSpacerHeight();

}
