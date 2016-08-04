package com.palechip.hudpixelmod.games;

import java.util.ArrayList;

public class GameEventObserver {

    ArrayList <IGameEvents> clients = new ArrayList<IGameEvents>();

    public void onGameEnd(){
        for(IGameEvents cm : clients)
            cm.onGameEnd();
    }

    public void onGameStart(){
        for(IGameEvents cm : clients)
            cm.onGameStart();
    }
}
