/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib;

import net.minecraftforge.common.MinecraftForge;
import net.unaussprechlich.managedgui.lib.helper.ISetupHelper;


public class ManagedGui {

    private static ManagedGui INSTANCE;
    private static boolean isDisabled = true;
    private static ISetupHelper ISetupHelper;

    public static ManagedGui getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new ManagedGui();
        return INSTANCE;
    }

    private ManagedGui(){}

    public static boolean isIsDisabled() {return isDisabled;}
    public static void setIsDisabled(boolean isDisabled) {ManagedGui.isDisabled = isDisabled;}

    public static ISetupHelper getISetupHelper() {return ISetupHelper;}

    private void setup(ISetupHelper ISetupHelper) {
        ManagedGui.ISetupHelper = ISetupHelper;
        GuiManagerMG.setup();
        MinecraftForge.EVENT_BUS.register(GuiManagerMG.getINSTANCE());

    }



}
