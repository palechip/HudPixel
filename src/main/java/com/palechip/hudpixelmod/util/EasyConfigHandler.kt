package com.palechip.hudpixelmod.util

import com.palechip.hudpixelmod.HudPixelMod
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
    val fieldMapStr: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()
    val fieldMapInt: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()
    val fieldMapBoolean: MutableMap<Field, AnnotationHelper.AnnotationInfo> = mutableMapOf()

    fun init(configf: File, asm: ASMDataTable?) {
        if(asm == null) return
        config = configf
        val config = Configuration(configf)
        findByClass(Any::class.java, asm)
        findByClass(Boolean::class.javaPrimitiveType!!, asm)
        findByClass(Char::class.javaPrimitiveType!!, asm)
        findByClass(Byte::class.javaPrimitiveType!!, asm)
        findByClass(Short::class.javaPrimitiveType!!, asm)
        findByClass(Int::class.javaPrimitiveType!!, asm)
        findByClass(Float::class.javaPrimitiveType!!, asm)
        findByClass(Long::class.javaPrimitiveType!!, asm)
        if (HudPixelMod.IS_DEBUGGING) {
            fieldMapStr.keys.forEach { println("Found string config property field ${it.declaringClass.name}.${it.name}") }
            if (fieldMapStr.keys.size == 0) println("No string config property fields found!")
        }

        config.load()
        fieldMapStr.forEach {
            it.key.isAccessible = true
            if(!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING)
                it.key.set(null, config.get(it.value.getString("catagory", ""), it.value.getString("id", ""), it.value.getString("def", ""), it.value.getString("comment", "")).string)
        }
        fieldMapInt.forEach {
            it.key.isAccessible = true
            if(!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING)
                it.key.set(null, config.get(it.value.getString("catagory", ""), it.value.getString("id", ""), it.value.getInt("def", 0), it.value.getString("comment", "")).int)
        }
        fieldMapBoolean.forEach {
            it.key.isAccessible = true
            if(!it.value.getBoolean("devOnly", false) || HudPixelMod.IS_DEBUGGING)
                it.key.set(null, config.get(it.value.getString("catagory", ""), it.value.getString("id", ""), it.value.getBoolean("def", false), it.value.getString("comment", "")).boolean)
        }
        config.save()

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
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyString(val catagory: String, val id: String, val comment: String, val def: String, val devOnly: Boolean = false)
/**
 * This annotation should be applied to non-final, static (if in Kotlin, [JvmStatic]) fields of type [Int] (or in Kotlin Int?]
 * that you wish to use as a config property. Use [catagory] to indicate the config catagory in the config file,
 * [id] will indicate the name of the property, [comment] will be the comment above the entry in the config file,
 * [def] is the default value, and if [devOnly] (optional) is set to true, this config property will only be set in a
 * development environment.
 */
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyInt(val catagory: String, val id: String, val comment: String, val def: Int, val devOnly: Boolean = false)
/**
 * This annotation should be applied to non-final, static (if in Kotlin, [JvmStatic]) fields of type [Boolean] (or in Kotlin Boolean?]
 * that you wish to use as a config property. Use [catagory] to indicate the config catagory in the config file,
 * [id] will indicate the name of the property, [comment] will be the comment above the entry in the config file,
 * [def] is the default value, and if [devOnly] (optional) is set to true, this config property will only be set in a
 * development environment.
 */
@Target(AnnotationTarget.FIELD) annotation class ConfigPropertyBoolean(val catagory: String, val id: String, val comment: String, val def: Boolean, val devOnly: Boolean = false)

object GeneralConfigSettings {
    @ConfigPropertyBoolean("hudpixel", "useAPI", "Should use the API?", true) @JvmStatic var useAPI: Boolean = true
    @ConfigPropertyBoolean("hudpixel", "hudBackground", "Should the HUD have a background?", true) @JvmStatic var hudBackground: Boolean = true

    @ConfigPropertyInt("hudpixel", "displayXOffset", "HUD offset (X)", 5) @JvmStatic var displayXOffset: Int = 5
    @ConfigPropertyInt("hudpixel", "displayYOffset", "HUD offset (Y)", 5) @JvmStatic var displayYOffset: Int = 5

    @ConfigPropertyInt("hudpixel", "hudRed", "HUD Red", 0) @JvmStatic var hudRed: Int = 0
    @ConfigPropertyInt("hudpixel", "hudGreen", "HUD green", 0) @JvmStatic var hudGreen: Int = 0
    @ConfigPropertyInt("hudpixel", "hudBlue", "HUD blue", 0) @JvmStatic var hudBlue: Int = 0
    @ConfigPropertyInt("hudpixel", "hudAlpha", "HUD alpha", 255) @JvmStatic var hudAlpha: Int = 255
}