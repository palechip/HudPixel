/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.handler

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.managedgui.lib.ManagedGui

enum class ResourceHandler{

    STEVE_HEAD;

    val res = ResourceLocation(ManagedGui.MODID, name)



}
