package com.palechip.hudpixelmod.util

/**
 * Created by Elad on 10/29/2016.
 */
data class Option<T, V>(val t: T?, val v: V? = null) {
    operator fun invoke()
            = t ?: v
}