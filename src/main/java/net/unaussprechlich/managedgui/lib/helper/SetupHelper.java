/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.helper;

import net.unaussprechlich.managedgui.lib.GuiManagerMG;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.templates.defaults.gui.DefChatGUI;
import net.unaussprechlich.managedgui.lib.templates.defaults.gui.DefGUI;
import net.unaussprechlich.managedgui.lib.templates.defaults.gui.DefInGameGUI;

/**
 * ISetupHelper Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class SetupHelper {

    public GUI loadDefaultGUI(GuiManagerMG.EnumGUITypes type){
        if(type.equals(GuiManagerMG.EnumGUITypes.CHAT)) return new DefChatGUI();
        if(type.equals(GuiManagerMG.EnumGUITypes.INGAME)) return new DefInGameGUI();
        return new DefGUI();
    }

}
