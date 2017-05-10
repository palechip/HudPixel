package eladkay.hudpixel.asm;

import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(ResourcePackRepository.class)
public abstract class MixinRPR {

    @Shadow private File dirServerResourcepacks;
    @Inject(method = "deleteOldServerResourcesPacks", at = @At("HEAD"), cancellable = true)
    private void deleteOldServerResourcesPacksMixin(CallbackInfo info) {
        if(!dirServerResourcepacks.isDirectory()) info.cancel();
    }
}
