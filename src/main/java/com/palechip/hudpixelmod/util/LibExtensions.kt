package com.palechip.hudpixelmod.util

import net.minecraft.util.EnumChatFormatting

/**
 * Created by Elad on 10/29/2016.
 */
operator infix fun String.plus(other: EnumChatFormatting) = "$this$other"
operator infix fun EnumChatFormatting.plus(other: String) = "$this$other"