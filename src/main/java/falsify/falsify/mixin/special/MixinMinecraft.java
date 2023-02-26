package falsify.falsify.mixin.special;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MixinMinecraft {

    @Accessor("session")
    @Mutable
    void setSession(Session session);

    @Accessor("itemUseCooldown")
    @Mutable
    void setCoolDown(int coolDown);

//    @Invoker("doAttack")
//    boolean doAttack();
//
//    @Invoker("doItemUse")
//    void doItemUse();

    @Accessor("currentFps")
    int getCurrentFps();
}
