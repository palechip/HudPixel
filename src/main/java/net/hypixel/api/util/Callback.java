package net.hypixel.api.util;

import net.hypixel.api.reply.AbstractReply;

public abstract class Callback<T extends AbstractReply> implements ICallback<T> {
    private final Class<T> clazz;

    public Callback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public final Class<T> getClazz() {
        return clazz;
    }
}
