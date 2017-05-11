package eladkay.hudpixel.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.Timer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;
import java.util.concurrent.FutureTask;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow private Profiler mcProfiler;
    @Shadow private WorldClient theWorld;
    @Shadow private boolean isGamePaused;
    @Shadow private Timer timer;
    @Shadow private Queue< FutureTask<? >> scheduledTasks;
    @Shadow private static Logger logger;
    @Shadow private PlayerUsageSnooper usageSnooper;
    @Shadow private EntityPlayerSP thePlayer;
    @Shadow private SoundHandler mcSoundHandler;
    @Shadow private Framebuffer framebufferMc;
    @Shadow private GameSettings gameSettings;
    @Shadow private boolean skipRenderWorld;
    @Shadow private EntityRenderer entityRenderer;
    @Shadow private int fpsCounter;
    @Shadow private long prevFrameTime;
    @Shadow private GuiAchievement guiAchievement;
    @Shadow private int displayWidth;
    @Shadow private int displayHeight;
    @Shadow private long debugUpdateTime;
    @Shadow private IStream stream;
    @Shadow private String debug;
    @Shadow private GuiScreen currentScreen;
    @Shadow private IntegratedServer theIntegratedServer;
    @Shadow private FrameTimer frameTimer;
    @Shadow private long startNanoTime;
    @Shadow private static int debugFPS;

    @Shadow abstract boolean isSingleplayer();

    @Shadow abstract void updateDisplay();

    @Shadow abstract void displayDebugInfo(long i1);

    @Shadow abstract void checkGLError(String s);

    @Shadow abstract boolean isFramerateLimitBelowMax();

    @Shadow abstract int getLimitFramerate();

    @Shadow abstract void runTick();

    @Shadow abstract void shutdown();
}