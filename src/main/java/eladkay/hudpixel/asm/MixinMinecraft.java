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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;
import java.util.concurrent.FutureTask;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Final
    @Shadow public         Profiler               mcProfiler;
    @Shadow public        WorldClient            theWorld;
    @Shadow private        boolean                isGamePaused;
    @Shadow private        Timer                  timer;
    @Shadow public        Queue< FutureTask<? >> scheduledTasks;
    @Shadow public static Logger                 logger;
    @Shadow private        PlayerUsageSnooper     usageSnooper;
    @Shadow public        EntityPlayerSP         thePlayer;
    @Shadow private        SoundHandler           mcSoundHandler;
    @Shadow private        Framebuffer            framebufferMc;
    @Shadow public        GameSettings           gameSettings;
    @Shadow public        boolean                skipRenderWorld;
    @Shadow public EntityRenderer entityRenderer;
    @Shadow public int fpsCounter;
    @Shadow public long prevFrameTime;
    @Shadow public GuiAchievement guiAchievement;
    @Shadow public int displayWidth;
    @Shadow public int displayHeight;
    @Shadow public long debugUpdateTime;
    @Shadow private IStream stream;
    @Shadow public String debug;
    @Shadow public GuiScreen currentScreen;
    @Shadow private IntegratedServer theIntegratedServer;
    @Shadow public FrameTimer frameTimer;
    @Shadow public long startNanoTime;
    @Shadow private static int debugFPS;

    @Shadow
    public abstract boolean isSingleplayer();

    @Shadow
    public abstract void updateDisplay();

    @Shadow
    protected abstract void displayDebugInfo(long i1);

    @Shadow
    protected abstract void checkGLError(String s);

    @Shadow
    public abstract boolean isFramerateLimitBelowMax();

    @Shadow abstract int getLimitFramerate();

    @Shadow abstract void runTick();

    @Shadow abstract void shutdown();
}