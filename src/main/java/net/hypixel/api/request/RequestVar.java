package net.hypixel.api.request;

public enum RequestVar{
    NAME("name"),
    BY_NAME("byName"),
    BY_UUID("byUuid"),
    ID("if"),
    KEY("key"),
    PLAYER("player"),
    UUID("uuid");

    private final String varType;

    RequestVar(String s){
        this.varType = s;
    }

    public String getValue(){
        return  varType;
    }

}
