// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.util;

import java.util.UUID;

public class APIUtil {
    public static String stripDashes(UUID inputUuid) {
        String input = inputUuid.toString();
        return input.substring(0, 8) + input.substring(9, 13) + input.substring(14, 18) + input.substring(19, 23) + input.substring(24, 36);
    }
}
