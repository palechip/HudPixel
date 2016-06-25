// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.reply;

@SuppressWarnings("unused")
public abstract class AbstractReply {

    protected boolean throttle;
    protected boolean success;
    protected String cause;

    public boolean isThrottle() {
        return throttle;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return "AbstractReply{" +
                "throttle=" + throttle +
                ",success=" + success +
                ", cause='" + cause + '\'' +
                '}';
    }
}
