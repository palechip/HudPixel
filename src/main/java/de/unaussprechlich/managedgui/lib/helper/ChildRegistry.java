package de.unaussprechlich.managedgui.lib.helper;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

/**
 * Created by kecka on 26.08.2016.
 */
public class ChildRegistry {

    private ArrayList<Child> childs = new ArrayList<Child>();

    public void registerChild(Child child){
        childs.add(child);
    }

    public void unregisterChild(Child child){
        childs.remove(child);
    }

    public void clearChilds(){
        childs.clear();
    }

    public int getChildsSize(){
        return childs.size();
    }

    public boolean containsChild(Child child){
        return childs.contains(child);
    }

    public ArrayList<Child> getChilds(){
        return childs;
    }

    public void onClientTick(){
        for(Child child : childs){
            child.onClientTick();
        }
    }

    public void onRender(){
        for(Child child : childs){
            child.onRenderTick();
        }
    }


    public void onChatMessage(ClientChatReceivedEvent e) {
        for(Child child :childs){
            child.onChatMessage(e);
        }
    }

    public void onMouseMove(int mX, int mY){
        for(Child child : childs){
            child.onMouseMove(mX, mY);
        }
    }

    public void onScroll(int i){
        for(Child child : childs){
            child.onScroll(i);
        }
    }

    public void onClick(MouseHandler.ClickType clickType){
        for(Child child : childs){
            child.onClick(clickType);
        }
    }
}
