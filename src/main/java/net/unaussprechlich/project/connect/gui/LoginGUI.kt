package net.unaussprechlich.project.connect.gui

import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.unaussprechlich.hudpixelextended.hypixelapi.ApiKeyHandler
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.handler.ResourceHandlerConnect
import net.unaussprechlich.managedgui.lib.templates.defaults.container.*
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.Utils
import net.unaussprechlich.project.connect.socket.io.SocketConnection
import net.unaussprechlich.project.connect.socket.io.requests.LoginRequest
import org.json.JSONObject
import java.security.MessageDigest




/**
 * ConnectGUI Created by unaussprechlich on 20.12.2016.
 * Description:
 */
object LoginGUI : GUI() {

    private var login = false

    var userExists = false
        set(value) {
            if(value){
                wrapperCon.unregisterChild(tofCheckBoxCon)
                wrapperCon.unregisterChild(signUpButtonCon)
                wrapperCon.registerChild(loginButtonCon)
            }
            field = value
        }

    private var isBlocked = false
    private val buttonClickedListener : (MouseHandler.ClickType, Container) -> Unit = { clickType, container ->
        if(clickType == MouseHandler.ClickType.SINGLE && !isBlocked){
            if(!tofCheckBoxCon.isChecked && !userExists){
                tofCheckBoxCon.textColor = RGBA.RED_MC.get()
            } else {
                isBlocked = true
                LoginRequest({ args: JSONObject, success : Boolean ->
                    try {
                        if(success){
                            SocketConnection.SESSION_TOKEN = args.getString("TOKEN")
                            GuiManagerMG.unbindScreen()
                            GuiManagerMG.removeGUI("LoginGui")
                            isBlocked = false
                        } else {
                            when(args.getInt("errorcode")){
                                6557 -> displayError("Your password is wrong!")
                                else -> displayError("An error occurred - code: ${args.getInt("errorcode")}")
                            }
                            isBlocked = false
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
                        isBlocked = false
                    }
                }).apply {
                    val player = FMLClientHandler.instance().clientPlayerEntity;
                    val uuid = player.gameProfile.id.toString()
                    val md = MessageDigest.getInstance("SHA-256")
                    md.update((uuid + passwordFieldCon.realText).toByteArray())

                    PASSWORD = String.format("%064x", java.math.BigInteger(1, md.digest()))
                    NAME = player.displayNameString
                    UUID = uuid
                    API_KEY = ApiKeyHandler.getApiKey()
                }.send()
            }
        }
    }

    private val backgroundCon           = DefBackgroundContainer(RGBA.P1B1_DEF.get(), 300, 250).apply {
        xOffset = (DisplayUtil.scaledMcWidth - 300) / 2
        yOffset = (DisplayUtil.scaledMcHeight - 250) / 2
    }
    private val wrapperCon              = DefWrapperContainer().apply {
        xOffset = 25
        yOffset = 25
    }

    private val logoCon                 = DefPictureContainer(ResourceHandlerConnect.HUDPIXEL_LOGO.res).apply {
        xOffset = -4
        width = 150
        height = 40
    }
    private val textCon                 = DefTextListAutoLineBreakContainer("", 245).apply {
        yOffset = 40
    }
    private val passwordFieldCon        = DefPasswordFieldContainer("password ...", 120).apply {
        yOffset = 133
    }

    private val signUpButtonCon          = DefButtonStandardContainer("SIGN UP", buttonClickedListener ).apply {
        yOffset = 133
        xOffset = 119
    }

    private val loginButtonCon          = DefButtonStandardContainer("LOGIN", buttonClickedListener).apply {
        yOffset = 133
        xOffset = 119
    }

    private val rememberMeCheckBoxCon   = DefCheckboxContainer("Remember me", RGBA.P1B1_596068.get()).apply {
        yOffset = 170
    }

    private val tofCheckBoxCon          = DefCheckboxContainer("I agree to the terms of service", RGBA.P1B1_596068.get()).apply {
        yOffset = 185
    }

    private val error                   = DefTextContainer("").apply {
        yOffset = 151
        xOffset = 1
        isVisible = false
    }

    private val discordLogo             = DefPictureContainer(47, 15, ResourceHandlerConnect.DISCORD_LOGO_2.res).apply {
        width = 46
        height = 15
        yOffset = 0
        xOffset = 250-46

    }



    fun displayError(msg : String){
        error.text = "" + TextFormatting.DARK_RED + msg
        error.isVisible = true
    }

    init {
        this.registerChild(net.unaussprechlich.project.connect.gui.LoginGUI.backgroundCon)
        backgroundCon.registerChild(wrapperCon)

        wrapperCon.registerChild(logoCon)
        wrapperCon.registerChild(textCon)
        wrapperCon.registerChild(passwordFieldCon)
        wrapperCon.registerChild(signUpButtonCon)
        wrapperCon.registerChild(rememberMeCheckBoxCon)
        wrapperCon.registerChild(tofCheckBoxCon)
        wrapperCon.registerChild(error)
        wrapperCon.registerChild(discordLogo)

        discordLogo.registerClickedListener({ clickType, _ ->
            if(clickType != MouseHandler.ClickType.SINGLE){
                Utils.openWebLink("https://discord.gg/v8bUFYg")
            }
        })

        textCon.addEntry("Hi ${FMLClientHandler.instance().clientPlayerEntity.displayNameString}, ")
        textCon.addEntry("")
        textCon.addEntry("Welcome to HudPixel! HudPixel is a client-side mod especially designed for Hypixel players and officially allowed by the Hypixel Team. You can disable or enable features in the config by pressing <config key>.")
        textCon.addEntry("We hope you enjoy the mod and if you have any feedback, contact us on our discord server!")


    }

    override fun doClientTick(): Boolean {

        if(discordLogo.isHover){
            discordLogo.apply {
                width = 54
                height = 17
                yOffset = -1
                xOffset =  250 - 50
            }
        } else {
            discordLogo.apply {
                width = 46
                height = 15
                yOffset = 0
                xOffset = 250-46

            }
        }

        backgroundCon.apply {
            xOffset = (DisplayUtil.scaledMcWidth - 300) / 2
            yOffset = (DisplayUtil.scaledMcHeight - 200) / 2
        }
        return true
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {

        return true
    }

    override fun doChatMessage(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doMouseMove(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun doScroll(i: Int): Boolean {
        return true
    }

    override fun doClick(clickType: MouseHandler.ClickType): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        if (!(event.id == EnumDefaultEvents.TIME.get() && event.data === EnumTime.SEC_15)) return true
        return true
    }

    override fun doOpenGUI(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResize(): Boolean {
        return true
    }
}