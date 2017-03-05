/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

enum class RGBA {

    //######################################################################################################################

    //PIB1########################################
    //http://paletton.com/palette.php?uid=53B0u0kb9ac7%2Bmw9Kh3hY9qnj7t
    //P1B1_GREY_BLUE
    P1B1_DEF(36, 44, 53),
    P1B1_3F4751(63, 71, 81),
    P1B1_596068(89, 96, 104),
    P1B1_19293A(25, 41, 58),
    P1B1_0A1D31(10, 29, 49),
    //P1B2_PURPLE
    P1B2_DEF(41, 39, 56),
    P1B2_64627D(100, 98, 125),
    P1B2_48465E(72, 70, 94),
    P1B2_1D1B34(29, 27, 52),
    P1B2_120F29(18, 15, 41),
    //P1B3_COFFEE1
    P1B3_DEF(81, 75, 53),
    P1B3_B3A987(179, 169, 135),
    P1B3_887F5F(136, 127, 95),
    P1B3_4B4221(75, 66, 33),
    P1B3_3B3210(59, 50, 16),
    //P1B4_COFFEE2
    P1B4_DEF(81, 70, 53),
    P1B4_B3A287(179, 162, 135),
    P1B4_88785F(136, 120, 95),
    P1B4_4B3A21(75, 58, 33),
    P1B4_3B2A10(59, 42, 16),
    //P1B5_GREEN
    P1B5_DEF(35, 54, 48),
    P1B5_59766E(89, 118, 110),
    P1B5_3E5A51(62, 90, 81),
    P1B5_163229(22, 50, 41),
    P1B5_0B271F(11, 39, 31),
    //P1B6_BLUE
    P1B6_DEF(36, 44, 53),
    P1B6_5A6775(90, 103, 117),
    P1B6_404C59(64, 76, 89),
    P1B6_182431(24, 36, 49),
    P1B6_0D1927(13, 25, 39),


    //ANY##########################################
    TRANSPARENT(0, 0, 0, 0),
    GRAY_T(0, 0, 0),

    RED(255, 0, 0),
    GREEN(0, 255, 0),
    WHITE(255, 255, 255),
    BLUE(0, 0, 255),
    BLACK(0, 0, 0, 1),
    BLACK_LIGHT(30, 30, 30),
    BLACK_LIGHT2(50, 50, 60),
    BLACK_LIGHT3(60, 60, 70),

    //MC###########################################
    YELLOW_MC(255, 255, 85),
    BLACK_MC(0, 0, 0, 1),
    GOLD_MC(255, 170, 0),
    GRAY_MC(170, 170, 170),
    BLUE_MC(85, 85, 255),
    GREEN_MC(85, 255, 85),
    AQUA_MC(85, 255, 255),
    RED_MC(255, 85, 85),
    GRAY_DARK_MC(85, 85, 85),
    BLUE_DARK_MC(0, 0, 170),
    GREEN_DARK_MC(0, 170, 0),
    AQUA_DARK_MC(0, 170, 170),
    RED_DARK_MC(170, 0, 0),
    PURPLE_DARK_MC(170, 0, 170),
    PURPLE_LIGHT_MC(255, 85, 255),
    WHITE_MC(255, 255, 255),

    NULL(0, 0, 0, 0);


    //######################################################################################################################

    private val RGBA: ColorRGBA

    constructor(RGBA: ColorRGBA) {
        this.RGBA = RGBA
    }

    constructor(R: Int, G: Int, B: Int, A: Int) {
        this.RGBA = ColorRGBA(R, G, B, A)
    }

    constructor(R: Int, G: Int, B: Int) {
        this.RGBA = ColorRGBA(R, G, B)
    }

    fun get(): ColorRGBA {
        return RGBA
    }


    val alphaf: Float
        get() = RGBA.alphaf
    val bluEf: Float
        get() = RGBA.bluef
    val greef: Float
        get() = RGBA.greenf
    val ref: Float
        get() = RGBA.redf

    val alpha: Int
        get() = RGBA.alpha
    val blue: Int
        get() = RGBA.blue
    val green: Int
        get() = RGBA.green
    val red: Int
        get() = RGBA.red
}
