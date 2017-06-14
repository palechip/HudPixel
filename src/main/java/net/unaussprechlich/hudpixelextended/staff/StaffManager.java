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

package net.unaussprechlich.hudpixelextended.staff;


import eladkay.hudpixel.HudPixelMod;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler;
import net.unaussprechlich.hudpixelextended.util.IEventHandler;
import net.unaussprechlich.hudpixelextended.util.McColorHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * A small "ego"-class to display the the HudPixel staff with a nice color and tag #abgehoben
 */
public class StaffManager implements IEventHandler, McColorHelper {

    /**
     * constructor -> requests the file
     */
    public StaffManager() {
        HudPixelExtendedEventHandler.registerIEvent(this);
        MinecraftForge.EVENT_BUS.register(this);
        //loades the staff
        new AdminHandler();
    }

    private static String hudHelperTag() {
        return GOLD + "[Hud" + YELLOW + "Helper" + GOLD + "]" + YELLOW + " ";
    }

    private static String hudAdminTag() {
        return GOLD + "[Hud" + RED + "Admin" + GOLD + "]" + RED + " ";
    }

    /**
     * changes the tag above the player, when they join and are part of the hudpixel team
     *
     * @param e
     */
    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e) {
        if (tags.keySet().contains(e.getUsername()) || e.getUsername().contains("PixelPlus")) {
            e.setDisplayname(tags.get(e.getUsername()) + e.getDisplayname() + "§r");
        }
    }

    private volatile static Map<String, String> tags = null;

    private static void load(Properties props) {
        tags = new HashMap<>();
        System.out.println("Fanciness time!");
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            System.out.println("Loading fanciness for " + key + ": " + value);
            tags.put(key, value.contains("admin") ? hudAdminTag() : value.contains("helper") ? hudHelperTag() : value);
        }
    }

    private static class AdminHandler extends Thread {
        public AdminHandler() {
            setName("Admin Fanciness Thread");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Eladkay/static/master/HudPixelAdmins");
                Properties props = new Properties();
                try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                    props.load(reader);
                    load(props);
                }
            } catch (IOException e) {
                HudPixelMod.Companion.instance().getLogger().info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
            }
        }
    }


    /**
     * buts the admin/helper tag infront of a message a admin/helper has written
     *
     * @param e chat event
     * @throws Throwable
     */
    private final String default_tag = "§r§7";
    private final String vip_tag = "§r§a[VIP] ";
    private final String vipplus_tag = "§r§a[VIP§r§6+§r§a] ";
    private final String mvp_tag = "§r§b] ";

    private static final String MVPPLUS_TAG_REGEX = ".*?§r§b\\[MVP§r§[0-9a-f]\\+§r§b].";
    
    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        if (e.getType() != 0) return; //return if it isn't a normal chat message
        if (e.getMessage().getUnformattedText().contains("http://") || e.getMessage().getUnformattedText().contains("https://"))
            return; //return if the message contains a link .... so you can still click it :)

        String text = e.getMessage().getFormattedText();

        for (String s : tags.keySet()) { //for admins
            String s1 = s;
		    if (text.contains(default_tag + s1) || text.contains(vip_tag + s1) || text.contains(vipplus_tag + s1) ||
                    text.contains(mvp_tag + s1) || Pattern.compile(MVPPLUS_TAG_REGEX + s1).matcher(text).matches()) {

		        //TODO: Test and potentially fix.
                e.setMessage(new TextComponentString(e.getMessage().getFormattedText().replaceFirst(s, tags.get(s) + s)));
                return;
            }
        }
    }
}
