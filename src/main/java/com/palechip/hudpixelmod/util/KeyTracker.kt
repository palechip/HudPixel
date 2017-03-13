package com.palechip.hudpixelmod.util

import net.minecraftforge.common.MinecraftForge
import org.lwjgl.input.Keyboard
import java.lang.Thread.sleep

/**
 * Created by Elad on 3/7/2017.
 */
object KeyTracker {
    private val handlers = mutableListOf<(Char)->Unit>()
    fun register(handler: (Char)->Unit) {
        handlers.add(handler)
    }
    private var lastChar: Char? = null
    init {
        MinecraftForge.EVENT_BUS.register(this)
        Thread {
            while(true) {
                if(!Keyboard.isCreated()) continue
                for (i in 0 until Keyboard.getNumKeyboardEvents()) {
                    if (Keyboard.getEventKeyState()) {
                        val char = (if (Keyboard.getEventKey() == 0) Keyboard.getEventCharacter() + 256 else Keyboard.getEventCharacter())
                        if ((char == lastChar && char != 8.toChar()) || !char.isLetterOrDigit()) continue
                        handlers.forEach { it(char) }
                        lastChar = char
                    }
                }
                sleep(10)
            }
        }.start()
    }



}