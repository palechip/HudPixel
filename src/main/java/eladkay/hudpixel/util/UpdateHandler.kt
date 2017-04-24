package eladkay.hudpixel.util

import eladkay.hudpixel.HudPixelMod
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Loader
import org.apache.commons.io.FileUtils
import java.awt.Font
import java.awt.Graphics
import java.io.File
import java.net.URL
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Created by Elad on 4/24/2017.
 */
fun receiveUpdate(version: String) {
    object : JPanel() {
        init {
            add(object : JFrame("HudPixel Autoupdate") {
                init {
                    defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
                    isResizable = true
                    pack()
                    setLocationRelativeTo(null)
                    isVisible = true
                }
            })
        }

        override fun paint(g: Graphics) {
            g.font = Font.getFont(g.font.name).deriveFont(20f)
            g.drawString("HudPixel is updating!", size.width / 2, size.height / 2)
            g.drawString("New version: $version", size.width / 2, size.height / 2 + 20)
            super.paint(g)
        }
    }.show()
    Thread {
        val file = File("HudPixelReloaded-$version.jar").let { FileUtils.copyURLToFile(URL("$URL_UPDATES$version"), it); it }
        val source = Loader.instance().activeModList.first { it.modId == HudPixelMod.MODID }.source
        file.copyTo(File("${source.parent}/HudPixelReloaded-$version.jar"))
        file.deleteOnExit()
        source.deleteOnExit()
        FMLCommonHandler.instance().exitJava(0, false)

    }
}

const val URL_UPDATES = "<fillinurlhere>"