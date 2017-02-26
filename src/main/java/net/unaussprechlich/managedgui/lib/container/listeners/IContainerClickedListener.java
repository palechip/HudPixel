/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.container.listeners;

import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * IContainerClickedListener Created by Alexander on 26.02.2017.
 * Description:
 **/
public interface IContainerClickedListener {

    void onContainerClicked(MouseHandler.ClickType clickType, Container container);

}
