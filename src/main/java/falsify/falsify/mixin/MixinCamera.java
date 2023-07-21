package falsify.falsify.mixin;

import falsify.falsify.module.modules.render.Perspective;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow protected abstract double clipToSpace(double desiredCameraDistance);

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.setRotation(FF)V", ordinal = 0))
    private void setPerspective(Args args) {
        Perspective perspective = Perspective.INSTANCE;
        if(perspective != null && perspective.isEnabled()) {
            args.set(0, perspective.getYaw());
            args.set(1, perspective.getPitch());
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.moveBy(DDD)V", ordinal = 0))
    private void setDistance(Args args) {
        Perspective perspective = Perspective.INSTANCE;
        if(perspective != null && perspective.isEnabled()) {
            if(perspective.shouldClip()) args.set(0, -perspective.getCameraDistance());
            else args.set(0, -clipToSpace(perspective.getCameraDistance()));
        }
    }

}
