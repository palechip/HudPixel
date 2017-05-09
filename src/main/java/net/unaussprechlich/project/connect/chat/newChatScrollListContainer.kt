package net.unaussprechlich.project.connect.chat

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.ContainerFrame
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import java.util.*


/**
 * DefScrollableContainer Created by Alexander on 26.02.2017.
 * Description:
 */
open class newChatScrollListContainer(private val spacer: IScrollSpacerRenderer) : ContainerFrame(500, 300, 100, 50, RGBA.P1B1_DEF.get()) {

    //TODO REPLACE WITH REAL _id
    private val chat = Chat(UUID.randomUUID())

    private var indexScroll: Int = 0
    private val spacerHeight: Int = spacer.spacerHeight

    private var pixelSize = 0
    private var pixelPos = 0

    val scrollElements = ArrayList<Container>()
    private val spacerPositions = ArrayList<Int>()

    private val pixelPosFromIndex: Int
        get() = PIXEL_PER_INDEX * indexScroll

    private val indexFromPixelPos: Int
        get() = pixelPos / PIXEL_PER_INDEX

    private val maxScrollIndex: Int
        get() {
            if (PIXEL_PER_INDEX == 0) return 0
            return (pixelSize - height) / PIXEL_PER_INDEX
        }

    private val scrollBarPosY: Int
        get() {
            val scrollHeight = height - 45
            if (maxScrollIndex != 0)
                return yStart + 5 + (scrollHeight - Math.round(scrollHeight * (indexScroll.toFloat() / ((pixelSize - height).toFloat() / PIXEL_PER_INDEX.toFloat()))))
            else
                return yStart + 5 + height - 20
        }

    init {
        isResizeable = false
    }

    fun registerScrollElement(container: Container) {
        if (scrollElements.size + 1 > MAX_STORED)
            unregisterScrollElement(scrollElements[scrollElements.size - 1])

        registerChild(container)
        scrollElements.add(container)
        updateWithoutAnimation()
    }

    fun unregisterScrollElement(container: Container) {
        unregisterChild(container)
        scrollElements.remove(container)
        updateWithoutAnimation()
    }

    override fun doResize(): Boolean {
        return super.doResize()
    }

    override fun doClientTickLocal(): Boolean {
        scrollAnimation()

        if (isScrollByBar) {
            val i = (height - 50).toFloat()
            val mY = MouseHandler.mY - yStart - 25
            if (mY < 0)         pixelPos = pixelSize - height
            else if (mY > i)    pixelPos = 0
            else                pixelPos = Math.round(Math.abs(mY - i) * ((pixelSize - height).toFloat() / i))
            updateWithoutAnimation()
        }

        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {

        if (ees == EnumEventState.PRE) {
            RenderUtils.renderBoxWithColor(xStart - 5, yStart - 5, width + 100, height + 100, backgroundRGBA)
            if (scrollElements.size > 1)
                spacerPositions.forEach { y -> spacer.render(xStart, yStart + y, width) }
        }

        if (ees == EnumEventState.POST) {
            RenderUtils.renderBoxWithColor(xStart, yStart - 1, width, 7, backgroundRGBA)
            RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart + 6, width, 30, backgroundRGBA,
                    ColorRGBA(backgroundRGBA.red, backgroundRGBA.green, backgroundRGBA.blue, 0))

            if (pixelSize < height) return true

            val scroll = Math.round(Math.abs(pixelPos - (pixelSize - height)) * ((height - 50).toFloat() / (pixelSize - height).toFloat()))

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 10, yStart + 10, height - 20, 4, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2)
            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 11, yStart + scroll + 10, 30, 6, RGBA.P1B1_596068.get(), RGBA.NULL.get(), 2)
        }
        return true
    }



    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }

    private var isScrollByBar = false
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DRAG)
            if (this.checkIfMouseOver(xStart + width - 11, scrollBarPosY, 6, 30))
                isScrollByBar = true

        if (clickType == MouseHandler.ClickType.DROP && isScrollByBar)
                isScrollByBar = false

        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        if (!isThisContainer) return true

        if (i != 0) {
            if (i > 0) {
                if ((indexScroll + 1) * PIXEL_PER_INDEX > pixelSize - height) return true
                indexScroll++
                isScrollUP = true
            } else {
                if (indexScroll - 1 < 0) return true
                indexScroll--
                isScrollUP = false
            }
            updateWithAnimation()
        }
        return true
    }


    private fun updateWithAnimation() {
        scrollAnimated = PIXEL_PER_INDEX
    }

    private fun updateWithoutAnimation() {
        spacerPositions.clear()
        pixelSize = scrollElements.map{it.height}.sum() + scrollElements.size * spacerHeight
        indexScroll = indexFromPixelPos

        var offset = 0

        for (con in scrollElements) {
            con.yOffset = pixelPos - offset + height - pixelSize - scrollAnimated
            if (scrollElements.indexOf(con) != scrollElements.size - 1) {
                spacerPositions.add(pixelPos - offset + con.height + height - pixelSize - scrollAnimated)
                offset -= con.height + spacerHeight
            }
        }
    }

    private var isScrollUP = false
    private var scrollAnimated = PIXEL_PER_INDEX
    private fun scrollAnimation() {

        if (scrollAnimated <= 0) return
        scrollAnimated -= 1
        spacerPositions.clear()
        pixelSize = scrollElements.map{it.height}.sum() + scrollElements.size * spacerHeight
        pixelPos = pixelPosFromIndex

        var offset = 0

        if (isScrollUP)
            for (con in scrollElements) {
                con.yOffset = pixelPosFromIndex - offset + height - pixelSize - scrollAnimated
                spacerPositions.add(indexScroll * PIXEL_PER_INDEX - offset + con.height + height - pixelSize - scrollAnimated)
                offset -= con.height + spacerHeight
            }
        else
            for (con in scrollElements) {
                con.yOffset = pixelPosFromIndex - offset + height - pixelSize + scrollAnimated
                spacerPositions.add(indexScroll * PIXEL_PER_INDEX - offset + con.height + height - pixelSize + scrollAnimated)
                offset -= con.height + spacerHeight
            }
    }


    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean { return true }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        scrollElements.forEach { container -> container.width = getWidth() }
        indexScroll = 0
        updateWithoutAnimation()

        return true
    }

    companion object {
        private val PIXEL_PER_INDEX = 15
        private val MAX_STORED = 50
    }
}

