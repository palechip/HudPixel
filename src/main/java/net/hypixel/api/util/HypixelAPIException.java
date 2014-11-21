// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.util;

@SuppressWarnings("unused")
public class HypixelAPIException extends RuntimeException {

    public HypixelAPIException() {
    }

    public HypixelAPIException(String message) {
        super(message);
    }

    public HypixelAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public HypixelAPIException(Throwable cause) {
        super(cause);
    }

    public HypixelAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
