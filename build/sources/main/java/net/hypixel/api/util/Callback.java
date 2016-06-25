// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.util;

public abstract class Callback<T> {
    private final Class<T> clazz;

    public Callback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract void callback(Throwable failCause, T result);

    public final Class<T> getClazz() {
        return clazz;
    }
}
