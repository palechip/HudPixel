/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 *
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 *
 * Copyright (c) 2016 unaussprechlich and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.extended.staff;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.palechip.hudpixelmod.extended.util.McColorHelper.D_RED;

public class StaffManager implements IEventHandler{

    private static ArrayList<String> adminList = new ArrayList<String>();
    private static ArrayList<String> helperList = new ArrayList<String>();
    private static String LINK_TO_STAFFJSON = "http://unaussprechlich.net/HudPixel/files/staffjson.php";

    public StaffManager(){
        getHttpRequest();
    }

    public static void onPlayerName(PlayerEvent.NameFormat e){
        if(adminList.contains(e.username)){
            e.displayname = hudAdminTag() + e.displayname;
        } else if(helperList.contains(e.username)){
            e.displayname =  hudHelperTag() + e.displayname;
        }
    }


    private static String hudHelperTag(){
        return EnumChatFormatting.GOLD + "[Hud" + EnumChatFormatting.YELLOW + "Helper"
                + EnumChatFormatting.GOLD + "] " + EnumChatFormatting.YELLOW;
    }

    private static String hudAdminTag(){
        return  EnumChatFormatting.GOLD + "[Hud" + EnumChatFormatting.RED + "Admin"
                + EnumChatFormatting.GOLD + "] " + EnumChatFormatting.RED;
    }

    private void getHttpRequest(){
        try {
            URL u = new URL(LINK_TO_STAFFJSON);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sBuilder = new StringBuilder();
            String buff = "";
            while(( buff = br.readLine()) != null){
                sBuilder.append(buff);
            }
            String data = sBuilder.toString();
            JsonObject jsonObject= jsonParser(data);
            adminList = getArrayFromJsonEntry(getStringFromJson("admins", jsonObject));
            helperList = getArrayFromJsonEntry(getStringFromJson("helper", jsonObject));
        } catch (MalformedURLException e) {
            LoggerHelper.logError("[StaffLoader]: Something went wrong while loading the URL for the staff file");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerHelper.logError("[StaffLoader]: Something went wrong while reading the update file!");
            e.printStackTrace();
        }
    }

    private JsonObject jsonParser(String data){
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(data).getAsJsonObject();
    }

    private ArrayList<String> getArrayFromJsonEntry(String s){
        s = removeChars(s, "\"");
        s = removeChars(s, "[");
        return new ArrayList<String>(Arrays.asList(s.split(",")));
    }

    private String getStringFromJson(String key, JsonObject jsonObject){
        try{
            if(jsonObject.get(key) != null){
                return jsonObject.get(key).toString();
            } else {
                LoggerHelper.logWarn("[StaffLoader]: Key '" + key + "' not Found in Json!");
                return "";
            }
        } catch (Exception e){
            LoggerHelper.logError("[UpdateNotifier]: Something went wrong while extracting Key '" + key + "' from Json!");
            e.printStackTrace();
            return D_RED +"Something went wrong while extracting Key '" + key + "' from Json!";
        }
    }

    private String removeChars(String s, String r){
        return s.replace(r, "");
    }

    @Override
    public void onClientTick() {

    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        /*
        for(String s : adminList){
            if(e.message.getUnformattedText().contains("] " + s + ":")){
                e.message = hudAdminTag() + e.message.a;
            }
        }*/
    }

    @Override
    public void onRender() {

    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {

    }

    @Override
    public void onMouseClick(int mX, int mY) {

    }
}
