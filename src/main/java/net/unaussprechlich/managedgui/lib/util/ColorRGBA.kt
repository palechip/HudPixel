/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util


class ColorRGBA {

    val red: Int
    val green: Int
    val blue: Int
    val alpha: Int

    val redf: Float
    val greenf: Float
    val bluef: Float
    val alphaf: Float

    constructor(RED: Int, GREEN: Int, BLUE: Int, ALPHA: Int) {
        this.red = RED
        this.green = GREEN
        this.blue = BLUE
        this.alpha = ALPHA

        this.redf = RED.toFloat() / 255
        this.greenf = GREEN.toFloat() / 255
        this.bluef = BLUE.toFloat() / 255
        this.alphaf = ALPHA.toFloat() / 255
    }

    constructor(RED: Int, GREEN: Int, BLUE: Int) {
        val ALPHA = 255
        this.red = RED
        this.green = GREEN
        this.blue = BLUE
        this.alpha = ALPHA

        this.redf = RED.toFloat() / 255
        this.greenf = GREEN.toFloat() / 255
        this.bluef = BLUE.toFloat() / 255
        this.alphaf = ALPHA.toFloat() / 255
    }

    constructor(RED: Float, GREEN: Float, BLUE: Float, ALPHA: Float) {
        this.red = Math.round(RED * 255f)
        this.green = Math.round(GREEN * 255f)
        this.blue = Math.round(BLUE * 255f)
        this.alpha = Math.round(ALPHA * 255f)

        this.redf = RED
        this.greenf = GREEN
        this.bluef = BLUE
        this.alphaf = ALPHA
    }

    constructor(RED: Float, GREEN: Float, BLUE: Float) {
        val ALPHA = 1f
        this.red = Math.round(RED * 255f)
        this.green = Math.round(GREEN * 255f)
        this.blue = Math.round(BLUE * 255f)
        this.alpha = Math.round(ALPHA * 255f)

        this.redf = RED
        this.greenf = GREEN
        this.bluef = BLUE
        this.alphaf = ALPHA
    }

    companion object {

        fun interpolateColor(rgba1: Int, rgba2: Int, percent: Float): Int {
            val r1 = rgba1 and 0xFF
            val g1 = rgba1 shr 8 and 0xFF
            val b1 = rgba1 shr 16 and 0xFF
            val a1 = rgba1 shr 24 and 0xFF
            val r2 = rgba2 and 0xFF
            val g2 = rgba2 shr 8 and 0xFF
            val b2 = rgba2 shr 16 and 0xFF
            val a2 = rgba2 shr 24 and 0xFF

            val r = (if (r1 < r2) r1 + (r2 - r1) * percent else r2 + (r1 - r2) * percent).toInt()
            val g = (if (g1 < g2) g1 + (g2 - g1) * percent else g2 + (g1 - g2) * percent).toInt()
            val b = (if (b1 < b2) b1 + (b2 - b1) * percent else b2 + (b1 - b2) * percent).toInt()
            val a = (if (a1 < a2) a1 + (a2 - a1) * percent else a2 + (a1 - a2) * percent).toInt()

            return r or (g shl 8) or (b shl 16) or (a shl 24)
        }

        fun toRGBA(c: ColorRGBA): Int {
            return c.red or (c.green shl 8) or (c.blue shl 16) or (c.alpha shl 24)
        }

        fun toColor(rgba: Int): ColorRGBA {
            val r = rgba and 0xFF
            val g = rgba shr 8 and 0xFF
            val b = rgba shr 16 and 0xFF
            val a = rgba shr 24 and 0xFF
            return ColorRGBA(r, g, b, a)
        }

        fun getColorFromFormatting(c: Char): ColorRGBA {
            var color = RGBA.WHITE_MC.get()
            when (c) {
                '0' -> color = RGBA.BLACK_MC.get()
                '1' -> color = RGBA.BLUE_DARK_MC.get()
                '2' -> color = RGBA.GREEN_DARK_MC.get()
                '3' -> color = RGBA.AQUA_DARK_MC.get()
                '4' -> color = RGBA.RED_DARK_MC.get()
                '5' -> color = RGBA.PURPLE_DARK_MC.get()
                '6' -> color = RGBA.GOLD_MC.get()
                '7' -> color = RGBA.GRAY_MC.get()
                '8' -> color = RGBA.GRAY_DARK_MC.get()
                '9' -> color = RGBA.BLUE_MC.get()
                'a' -> color = RGBA.GREEN_MC.get()
                'b' -> color = RGBA.AQUA_MC.get()
                'c' -> color = RGBA.RED_MC.get()
                'd' -> color = RGBA.PURPLE_LIGHT_MC.get()
                'e' -> color = RGBA.YELLOW_MC.get()
                'f' -> color = RGBA.WHITE_MC.get()
            }
            return color
        }
    }
}
