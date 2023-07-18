package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.mixin.special.MixinPlayerMoveC2SPacket;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("No Fall", "Prevents fall damage", true, Category.MOVEMENT, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend e && e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (mc.player.getVelocity().y > -0.1) return;
            ((MixinPlayerMoveC2SPacket)packet).setOnGround(true);
        }
    }
}
