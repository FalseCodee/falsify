package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "<init>", at=@At("TAIL"))
    public void init(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey<?> registryRef, RegistryEntry<?> dimensionTypeEntry, int loadDistance, int simulationDistance, Supplier<?> profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
        Falsify.configManager.loadModules();
    }
}
