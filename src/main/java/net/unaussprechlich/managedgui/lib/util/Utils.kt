/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import java.net.URI
import java.net.URL


object Utils {

    fun openWebLink(link : String) {
        try {
            val url = URL(link).toURI()
            val oclass = Class.forName("java.awt.Desktop")
            val `object` = oclass.getMethod("getDesktop", *arrayOfNulls<Class<*>>(0)).invoke(null as Any?, *arrayOfNulls<Any>(0))
            oclass.getMethod("browse", *arrayOf<Class<*>>(URI::class.java)).invoke(`object`, *arrayOf<Any>(url))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            System.out.println("Couldn\'t open link")
        }

    }
}