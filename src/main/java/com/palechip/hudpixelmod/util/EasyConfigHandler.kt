/***********************************************************************************************************************
HudPixelReloaded - License

The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
intended for usage in this kind of application. By default, all rights are reserved.
The original version of the HudPixel Mod is made by palechip and published under the MIT license.
The majority of code left from palechip's creations is the component implementation.The ported version to
Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
(to be changed to the new license as detailed below in the next minor update).

For the rest of the code and for the build the following license applies:

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
#  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
#  Based on a work at HudPixelExtended & HudPixel.                                                  #
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

Restrictions:

The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
cases the authors reserve the right to revoke all rights for usage of the codebase.

1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
code, but only when it is used separately from HudPixel and any license header must indicate that.
2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
two of the authors.
3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
code is merged to the release branch you cannot revoke the given freedoms by this license.
5. If your own project contains a part of the licensed material you have to give the authors full access to all project
related files.
6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
reserve the right to take down any infringing project.
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.util

import com.palechip.hudpixelmod.HudPixelMod
import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.config.HudPixelConfigGui
import com.palechip.hudpixelmod.util.EasyConfigHandler.init
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.io.File
import java.lang.reflect.Field

/**
 * Created by Elad on 10/14/2016.
 * This object contains utilities for the automatic config system. Its [init] method should be invoked at
 * pre-initialization time.
 */
object EasyConfigHandler {
    var config: File? = null
    var configuration: Configuration? = null

    val fieldMapStr: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()
    val fieldMapInt: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()
    val fieldMapBoolean: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()

    fun init(configf: File, asm: ASMDataTable?) {
        if (asm == null) return
        config = configf
        configuration = Configuration(configf)
        findByClass(Any::class.java, asm)
        findByClass(Boolean::class.javaPrimitiveType!!, asm)
        findByClass(Char::class.javaPrimitiveType!!, asm)
        findByClass(Byte::class.javaPrimitiveType!!, asm)
        findByClass(Short::class.javaPrimitiveType!!, asm)
        findByClass(Int::class.javaPrimitiveType!!, asm)
        findByClass(Float::class.javaPrimitiveType!!, asm)
        findByClass(Long::class.javaPrimitiveType!!, asm)

        if (HudPixelMod.IS_DEBUGGING) {
            fieldMapStr.keys.forEach { println("Found string configuration property field ${it.declaringClass.name}.${it.name}") }
            if (fieldMapStr.keys.size == 0) println("No string configuration property fields found!")
        }

        (configuration as Configuration).load()
        fieldMapStr.forEach {
            it.key.isAccessible = true
            if (!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING){
                it.key.set(null, (configuration as Configuration).get(it.value.getCCategory("catagory", CCategory.UNKNOWN).getName(), it.value.getString("id", ""), it.value.getString("def", ""), it.value.getString("comment", "")).string)
                HudPixelConfigGui.addElement(
                        it.value.getCCategory("catagory", CCategory.UNKNOWN),
                        it.value.getString("id", ""),
                        it.value.getString("def", ""),
                        it.value.getString("comment", ""),
                        configuration
                )
            }
        }
        fieldMapInt.forEach {
            it.key.isAccessible = true
            if (!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING){
                it.key.set(null, (configuration as Configuration).get(it.value.getCCategory("catagory", CCategory.UNKNOWN).getName(), it.value.getString("id", ""), it.value.getInt("def", 0), it.value.getString("comment", "")).int)
                HudPixelConfigGui.addElement(
                        it.value.getCCategory("catagory", CCategory.UNKNOWN),
                        it.value.getString("id", ""),
                        it.value.getInt("def", 0),
                        it.value.getString("comment", ""),
                        configuration
                )
            }
        }
        fieldMapBoolean.forEach {
            it.key.isAccessible = true
            if (!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING){
                it.key.set(null, (configuration as Configuration).get(it.value.getCCategory("catagory", CCategory.UNKNOWN).getName(), it.value.getString("id", ""), it.value.getBoolean("def", false), it.value.getString("comment", "")).boolean)
                HudPixelConfigGui.addElement(
                        it.value.getCCategory("catagory", CCategory.UNKNOWN),
                        it.value.getString("id", ""),
                        it.value.getBoolean("def", false),
                        it.value.getString("comment", ""),
                        configuration
                )
            }
        }
        (configuration as Configuration).save()
    }

    private fun findByClass(clazz: Class<*>, asm: ASMDataTable) {
        AnnotationHelper.findAnnotatedObjects(asm, clazz, ConfigPropertyString::class.java, { field: Field, info: AnnotationHelper.AnnotationInfo ->
            fieldMapStr.put(field, info)
        })
        AnnotationHelper.findAnnotatedObjects(asm, clazz, ConfigPropertyInt::class.java, { field: Field, info: AnnotationHelper.AnnotationInfo ->
            fieldMapInt.put(field, info)
        })
        AnnotationHelper.findAnnotatedObjects(asm, clazz, ConfigPropertyBoolean::class.java, { field: Field, info: AnnotationHelper.AnnotationInfo ->
            fieldMapBoolean.put(field, info)
        })
    }
}

/**
 * This annotation should be applied to non-final, static (if in Kotlin, [JvmStatic]) fields of type [String] (or in Kotlin String?]
 * that you wish to use as a config property. Use [catagory] to indicate the config catagory in the config file,
 * [id] will indicate the name of the property, [comment] will be the comment above the entry in the config file,
 * [def] is the default value, and if [devOnly] (optional) is set to true, this config property will only be set in a
 * development environment.
 */
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyString(val catagory: CCategory, val id: String, val comment: String, val def: String, val devOnly: Boolean = false)

/**
 * This annotation should be applied to non-final, static (if in Kotlin, [JvmStatic]) fields of type [Int] (or in Kotlin Int?]
 * that you wish to use as a config property. Use [catagory] to indicate the config catagory in the config file,
 * [id] will indicate the name of the property, [comment] will be the comment above the entry in the config file,
 * [def] is the default value, and if [devOnly] (optional) is set to true, this config property will only be set in a
 * development environment.
 */
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyInt(val catagory: CCategory, val id: String, val comment: String, val def: Int, val devOnly: Boolean = false)

/**
 * This annotation should be applied to non-final, static (if in Kotlin, [JvmStatic]) fields of type [Boolean] (or in Kotlin Boolean?]
 * that you wish to use as a config property. Use [catagory] to indicate the config catagory in the config file,
 * [id] will indicate the name of the property, [comment] will be the comment above the entry in the config file,
 * [def] is the default value, and if [devOnly] (optional) is set to true, this config property will only be set in a
 * development environment.
 */
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyBoolean(val catagory: CCategory, val id: String, val comment: String, val def: Boolean, val devOnly: Boolean = false)


object GeneralConfigSettings {
    @ConfigPropertyBoolean(CCategory.HUDPIXEL, "useAPI", "Should use the API?", true) @JvmStatic var useAPI: Boolean = true
    @ConfigPropertyBoolean(CCategory.HUDPIXEL, "hudBackground", "Should the HUD have a background?", true) @JvmStatic var hudBackground: Boolean = true

    @ConfigPropertyInt(CCategory.HUDPIXEL, "displayXOffset", "HUD offset (X)", 5) @JvmStatic var displayXOffset: Int = 5
    @ConfigPropertyInt(CCategory.HUDPIXEL, "displayYOffset", "HUD offset (Y)", 5) @JvmStatic var displayYOffset: Int = 5

    @ConfigPropertyInt(CCategory.HUDPIXEL, "hudRed", "HUD Red", 0) @JvmStatic var hudRed: Int = 0
    @ConfigPropertyInt(CCategory.HUDPIXEL, "hudGreen", "HUD green", 0) @JvmStatic var hudGreen: Int = 0
    @ConfigPropertyInt(CCategory.HUDPIXEL, "hudBlue", "HUD blue", 0) @JvmStatic var hudBlue: Int = 0
    @ConfigPropertyInt(CCategory.HUDPIXEL, "hudAlpha", "HUD alpha", 255) @JvmStatic var hudAlpha: Int = 255
}