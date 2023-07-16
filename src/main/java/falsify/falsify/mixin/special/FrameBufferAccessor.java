package falsify.falsify.mixin.special;

import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Framebuffer.class)
public interface FrameBufferAccessor {

    @Invoker("bind")
    void bindFramebuffer(boolean viewport);
}
