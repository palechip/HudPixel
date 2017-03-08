package net.unaussprechlich.project.connect.container

import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

class ChatTabContainer(tabListElement: TabListElementContainer, container: Container, tabManager: TabManager) : TabContainer(tabListElement, container, tabManager) {

    val textField = ChatTextFieldContainer("", container.width)

    init {
        setClosed()
        registerChild(textField)
    }

    override fun doClientTickLocal(): Boolean {
        textField.apply {
            yOffset =  container.height + tabListElement.height
            setWidthLocal(container.width + 2)
        }
        return super.doClientTickLocal()
    }

    override fun setClosed() {
        super.setClosed()
        textField.isVisible = false
    }

    override fun setOpen() {
        super.setOpen()
        textField.isVisible = true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + container.width, yStart + tabListElement.height, 2, container.height, RGBA.P1B1_DEF.get())
        return super.doRenderTickLocal(xStart, yStart, width, height, ees)
    }

}


