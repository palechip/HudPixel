package net.unaussprechlich.project.connect.gui;

import com.palechip.hudpixelmod.ChatDetector;
import com.palechip.hudpixelmod.HudPixelMod;
import kotlin.Unit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.hypixel.helper.HypixelRank;
import net.unaussprechlich.managedgui.lib.Constants;
import net.unaussprechlich.managedgui.lib.databases.Player.PlayerModel;
import net.unaussprechlich.managedgui.lib.databases.Player.data.Rank;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.RGBA;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 **/
public class ChatGUI extends GUI{

    private final TabManager tabManager = new TabManager();

    private static final int WIDTH  = 500;
    private static final int HEIGHT = 200;

    private static DefScrollableContainer scrollALL = new DefScrollableContainer(Constants.DEF_BACKGROUND_RGBA, WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
        @Override
        public void render(int xStart, int yStart, int width) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, Constants.DEF_BACKGROUND_RGBA, new ColorRGBA(30, 30, 30, 255));
        }

        @Override
        public int getSpacerHeight() {
            return 2;
        }
    });

    private static DefScrollableContainer partyCon = new DefScrollableContainer(Constants.DEF_BACKGROUND_RGBA, WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
        @Override
        public void render(int xStart, int yStart, int width) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, Constants.DEF_BACKGROUND_RGBA, new ColorRGBA(30, 30, 30, 255));
        }

        @Override
        public int getSpacerHeight() {
            return 2;
        }
    });

    private static DefScrollableContainer guildCon = new DefScrollableContainer(Constants.DEF_BACKGROUND_RGBA, WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
        @Override
        public void render(int xStart, int yStart, int width) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, Constants.DEF_BACKGROUND_RGBA, new ColorRGBA(30, 30, 30, 255));
        }

        @Override
        public int getSpacerHeight() {
            return 2;
        }
    });

    private static DefScrollableContainer privateCon = new DefScrollableContainer(Constants.DEF_BACKGROUND_RGBA, WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
        @Override
        public void render(int xStart, int yStart, int width) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, Constants.DEF_BACKGROUND_RGBA, new ColorRGBA(30, 30, 30, 255));
        }

        @Override
        public int getSpacerHeight() {
            return 2;
        }
    });


    public ChatGUI(){
        registerChild(tabManager);

        tabManager.registerTab(new TabContainer(new TabListElementContainer("ALL", RGBA.WHITE.get(), tabManager), scrollALL, tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("PARTY", RGBA.BLUE.get(), tabManager), partyCon, tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("GUILD", RGBA.GREEN.get(), tabManager), guildCon, tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("PRIVATE", RGBA.PURPLE_DARK_MC.get(), tabManager), privateCon, tabManager));

        updatePosition();

        ChatDetector.INSTANCE.registerEventHandler(ChatDetector.PrivateMessage.INSTANCE, (chatDetector, eventInfo) -> {
            System.out.println("FUCK THIS SHIT!" + eventInfo.getData().get("rank"));
                   addChatMessage(privateCon, eventInfo.getData().get("name"), eventInfo.getData().get("message"), HypixelRank.getRankByName(eventInfo.getData().get("rank")));
                   return Unit.INSTANCE;
            }
        );

        ChatDetector.INSTANCE.registerEventHandler(ChatDetector.GuildChat.INSTANCE, (chatDetector, eventInfo) -> {
            System.out.println("FUCK THIS SHIT!" + eventInfo.getData().get("rank"));
                                                       addChatMessage(guildCon, eventInfo.getData().get("name"), eventInfo.getData().get("message"), HypixelRank.getRankByName(eventInfo.getData().get("rank")));
                                                       return Unit.INSTANCE;
                                                   }
        );

        ChatDetector.INSTANCE.registerEventHandler(ChatDetector.PartyChat.INSTANCE, (chatDetector, eventInfo) -> {
            System.out.println("FUCK THIS SHIT!" + eventInfo.getData().get("rank"));
                                                       addChatMessage(partyCon, eventInfo.getData().get("name"), eventInfo.getData().get("message"), HypixelRank.getRankByName(eventInfo.getData().get("rank")));
                                                       return Unit.INSTANCE;
                                                   }
        );

    }

    public static void addChatMessage(DefScrollableContainer con, String name, String message, Rank rank){
        if(!con.getScrollElements().isEmpty() && con.getScrollElements().get(con.getScrollElements().size() - 1) instanceof DefChatMessageContainer){
            DefChatMessageContainer conMes = (DefChatMessageContainer) con.getScrollElements().get(con.getScrollElements().size() - 1);
            if(conMes.getPlayername().equalsIgnoreCase(name)){
                conMes.addMessage(message);
                return;
            }
        }
        con.registerScrollElement(
                new DefChatMessageContainer(
                        new PlayerModel(name, UUID.randomUUID(), rank, new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                        message,
                        new DefPictureContainer(),
                        WIDTH
                )
        );
    }

    private void updatePosition(){
        tabManager.setXOffset(5);
        tabManager.setYOffset(DisplayUtil.getScaledMcHeight() - HEIGHT - 17 - 30);
    }


    @Override
    public boolean doClientTick() {
        //TODO OFFSET RENDERING IS BROKEN! MAYBE IN CONTAINER_FRAME
        return true;
    }

    @Override
    public boolean doRender(int xStart, int yStart) {
        GL11.glColor3f(1f, 1f, 1f);
        //GuiInventory.drawEntityOnScreen(1000, 500, 100, MouseHandler.getmX(), MouseHandler.getmY(), Minecraft.getMinecraft().thePlayer);


        return true;
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return true;
    }

    @Override
    public boolean doScroll(int i) {
        return true;
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return true;
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {
        if(event.getID() == EnumDefaultEvents.SCREEN_RESIZE.get()){
            updatePosition();
        }
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }

    @Override
    public boolean doResize() {
        return true;
    }
}
