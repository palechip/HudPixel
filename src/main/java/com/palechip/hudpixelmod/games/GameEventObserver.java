package com.palechip.hudpixelmod.games;

import java.util.ArrayList;

public class GameEventObserver {

    static ArrayList <IGameEvents> clients = new ArrayList<IGameEvents>();

    public static void registerClient(IGameEvents callback){
        clients.add(callback);
    }

    public static void onGameEnd(){
        for(IGameEvents cm : clients)
            cm.onGameEnd();
    }

    public static void onGameStart(){
        for(IGameEvents cm : clients)
            cm.onGameStart();
    }

    public static void onSetupNewGame(){
        for(IGameEvents cm : clients)
            cm.onSetupNewGame();
    }
}
