/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package eladkay.hudpixel.command

import eladkay.hudpixel.HudPixelMod
import eladkay.hudpixel.util.ChatMessageComposer
import eladkay.hudpixel.util.plus
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextFormatting
import org.apache.commons.lang3.StringUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object NameCommand : HpCommandBase() {
    // This function isn't used. Disabled when porting to MC 1.11. Please remove when refactoring.
    /*private fun isOp(sender: ICommandSender): Boolean {
        return MinecraftServer.getServer().isSinglePlayer
                || sender !is EntityPlayerMP
                || MinecraftServer.getServer().configurationManager.canSendCommands(sender.gameProfile)
    }*/

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    fun canSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }

    /**
     * Gets the nm of the command
     */
    override fun getName(): String {
        return "names"
    }

    override fun getUsage(sender: ICommandSender): String {
        return "/names <player>"
    }


    override fun getAliases(): MutableList<String> {
        val aliases = ArrayList<String>()
        aliases.add("names")
        aliases.add("nm")
        aliases.add("grab")
        return aliases
    }

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if ((HudPixelMod.isHypixelNetwork || Minecraft.getMinecraft().currentServerData == null) && sender != null) {
            try {
                val cSender = sender
                if(args == null) return
                if (args.size == 0) {
                    cSender.sendMessage(ChatMessageComposer("Usage: /names <playername>").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                    cSender.sendMessage(ChatMessageComposer("Example: /names Eladkay").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                } else if (args.size == 1) {
                    val playername = args[0].toString()
                    if (playername.length < 3) {
                        cSender.sendMessage(ChatMessageComposer("Usage: /names <playername>").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                        cSender.sendMessage(ChatMessageComposer("Example: /names Eladkay").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                    } else {
                        object : Thread() {
                            override fun run() {
                                try {
                                    val uuidgrabber = URL("https://api.mojang.com/users/profiles/minecraft/" + playername)
                                    val br1 = BufferedReader(InputStreamReader(uuidgrabber.openStream()))
                                    val uuidfromweb: String
                                    uuidfromweb = br1.readLine()
                                    if (uuidfromweb != null) {
                                        val uuid = uuidfromweb.substring(7, 39)
                                        val namegrabber = URL("https://api.mojang.com/user/profiles/$uuid/names")
                                        val br2 = BufferedReader(InputStreamReader(namegrabber.openStream()))
                                        var webnames: String
                                        webnames = br2.readLine()
                                        if (webnames != null) {
                                            if (webnames.substring(10, webnames.length - 3 - playername.length).length <= 0) {
                                                cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                                cSender.sendMessage(ChatMessageComposer(StringUtils.center(playername, 65)).addFormatting(TextFormatting.LIGHT_PURPLE).addFormatting(TextFormatting.BOLD).assembleMessage(false))
                                                cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                                cSender.sendMessage(ChatMessageComposer(StringUtils.center("player has never changed their nm.", 65)).addFormatting(TextFormatting.YELLOW).assembleMessage(false))
                                                cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                            } else {
                                                webnames = webnames.replace("{", "").replace("}", "").replace(",".toRegex(), ".").replace('"', ' ').replace(" ", "").replace("[", "").replace("]", "").replace(".c", "-c").replace(".", ",")
                                                val split = webnames.split(Pattern.quote(",").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                                cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                                for (s in split) {
                                                    if (s.startsWith("nm") && s.contains("changed")) {
                                                        val names = s.split(Pattern.quote("-").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                                        var p1 = ChatMessageComposer("")
                                                        var p2 = ChatMessageComposer("")
                                                        for (d in names) {
                                                            if (d.startsWith("nm")) {
                                                                p1 = ChatMessageComposer("    - ").addFormatting(TextFormatting.GOLD).appendMessage(ChatMessageComposer(d.replace("nm:", "") + " ").addFormatting(TextFormatting.GREEN))
                                                            }
                                                            if (d.startsWith("changedToAt")) {
                                                                val unixSeconds = java.lang.Long.parseLong(d.replace("changedToAt:", ""))
                                                                val date = Date(unixSeconds)
                                                                val sdf = SimpleDateFormat("dd-MM-yyyy")
                                                                sdf.timeZone = TimeZone.getTimeZone("GMT+1")
                                                                val formattedDate = sdf.format(date)
                                                                p2 = ChatMessageComposer("(Changed on " + formattedDate + ")").addFormatting(TextFormatting.GRAY)
                                                            }
                                                        }
                                                        cSender.sendMessage(p1.appendMessage(p2).assembleMessage(false))
                                                    } else if (s.startsWith("nm") && !s.contains("changed")) {
                                                        cSender.sendMessage(ChatMessageComposer(StringUtils.center(s.replace("nm:", ""), 65)).addFormatting(TextFormatting.LIGHT_PURPLE).addFormatting(TextFormatting.BOLD).assembleMessage(false))
                                                        cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                                    }
                                                }
                                                cSender.sendMessage(ChatMessageComposer("").assembleMessage(false))
                                            }
                                        } else {
                                            cSender.sendMessage(ChatMessageComposer("ERROR: Could not find player '" + playername + "'.").addFormatting(TextFormatting.DARK_RED).assembleMessage(true))
                                            cSender.sendMessage(ChatMessageComposer("This person changed their nm or never existed.").addFormatting(TextFormatting.DARK_RED).assembleMessage(true))
                                        }
                                        br2.close()
                                    } else {
                                        cSender.sendMessage(ChatMessageComposer("ERROR: Could not find player '" + playername + "'.").addFormatting(TextFormatting.DARK_RED).assembleMessage(true))
                                        cSender.sendMessage(ChatMessageComposer("This person changed their nm or never existed.").addFormatting(TextFormatting.DARK_RED).assembleMessage(true))
                                    }
                                    br1.close()
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }

                            }
                        }.start()
                    }
                } else if (args.size > 1) {
                    cSender.sendMessage(ChatMessageComposer("Usage: /names <playername>").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                    cSender.sendMessage(ChatMessageComposer("Example: /names Eladkay").addFormatting(TextFormatting.GOLD).assembleMessage(true))
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
    }
}
