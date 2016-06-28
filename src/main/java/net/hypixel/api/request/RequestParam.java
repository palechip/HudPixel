package net.hypixel.api.request;

import net.hypixel.api.util.APIUtil;

import java.util.UUID;


@SuppressWarnings("unused")
public enum RequestParam {

    KEY(RequestType.KEY, RequestVar.KEY),

    PLAYER_BY_NAME      (RequestType.PLAYER,     RequestVar.NAME),
    PLAYER_BY_UUID      (RequestType.PLAYER,     RequestVar.UUID),

    GUILD_BY_NAME       (RequestType.FIND_GUILD, RequestVar.BY_NAME),
    GUILD_BY_PLAYER_UUID(RequestType.FIND_GUILD, RequestVar.BY_UUID),
    GUILD_BY_ID         (RequestType.GUILD,      RequestVar.ID),

    FRIENDS_BY_NAME     (RequestType.FRIENDS,    RequestVar.PLAYER),
    FRIENDS_BY_UUID     (RequestType.FRIENDS,    RequestVar.UUID),

    SESSION_BY_NAME     (RequestType.SESSION,    RequestVar.PLAYER),
    SESSION_BY_UUID     (RequestType.SESSION,    RequestVar.UUID);

    private static final RequestParam[] v = values();

    private final RequestType requestType;
    private final RequestVar requestVar;

    RequestParam(RequestType requestType, RequestVar requestVar) {
        this.requestType = requestType;
        this.requestVar = requestVar;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public RequestVar getRequestVar() {
        return requestVar;
    }

    public Class getValueClass(){
        return requestVar == RequestVar.UUID ? UUID.class : String.class;
    }

    public String serialize(Object value) {
        return requestVar == RequestVar.UUID ? APIUtil.stripDashes((UUID) value) : (String) value;
    }

}
