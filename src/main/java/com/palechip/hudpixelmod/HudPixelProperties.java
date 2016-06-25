package com.palechip.hudpixelmod;

import com.palechip.hudpixelmod.uptodate.UpdateChannel;

import java.io.InputStream;
import java.util.Properties;

/**
 * Static class providing properties from the hudpixel.properties file
 * @author palechip
 */
public class HudPixelProperties {
    public static final String VERSION;
    public static final UpdateChannel UPDATE_CHANNEL;
    public static final String SHORT_VERSION = "3.0"; // only to be used for the annotation which requires such a constant.

    private static final String DEFAULT_VERSION = "3.0.1";
    static {
        Properties props = null;
        try {
            // get the file which is included in the artifact
            InputStream input = HudPixelProperties.class.getClassLoader().getResourceAsStream("hudpixel.properties");
            // create a properties object
            props = new Properties();
            // load the file as a properties file
            props.load(input);
        } catch(Exception ignored) {
            // If the loading failed, we will handle this afterwards
        }

        if(props == null) {
            VERSION = "0.0.0.0";
            UPDATE_CHANNEL = UpdateChannel.STABLE;
        } else {
            // read the version
            String version = props.getProperty("version");
            // verify the format, doesn't check if they are actual numbers
            if(version != null && version.matches(".+[.].+[.].+[.].+")) {
                VERSION = version;
            } else {
                VERSION = DEFAULT_VERSION;
            }

            // get the update channel
            String channel = props.getProperty("updatechannel");

            // parse the update channel
            if(channel != null) {
                if(channel.equalsIgnoreCase(UpdateChannel.STABLE.getName())) {
                    UPDATE_CHANNEL = UpdateChannel.STABLE;
                } else if(channel.equalsIgnoreCase(UpdateChannel.DEV.getName())) {
                    UPDATE_CHANNEL = UpdateChannel.DEV;
                } else {
                    UPDATE_CHANNEL = UpdateChannel.NONE;
                }
            } else {
                UPDATE_CHANNEL = UpdateChannel.STABLE;
            }

        }
    }
}
