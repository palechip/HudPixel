/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib;

import net.minecraftforge.common.MinecraftForge;
import net.unaussprechlich.managedgui.lib.helper.SetupHelper;
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG;


public class ManagedGui {

    private static ManagedGui INSTANCE;
    private static boolean isDisabled = true;
    private static SetupHelper SetupHelper;

    public static ManagedGui getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new ManagedGui();
        return INSTANCE;
    }

    private ManagedGui(){}

    public static boolean isIsDisabled() {return isDisabled;}
    public static void setIsDisabled(boolean isDisabled) {ManagedGui.isDisabled = isDisabled;}

    public static SetupHelper getISetupHelper() {return SetupHelper;}

    public static void setup(SetupHelper ISetupHelper) {
        LoggerHelperMG.logInfo("Setting up ManagedGuiLib!");
        ManagedGui.SetupHelper = ISetupHelper;
        GuiManagerMG.setup();
        MinecraftForge.EVENT_BUS.register(GuiManagerMG.getINSTANCE());

    }



}
