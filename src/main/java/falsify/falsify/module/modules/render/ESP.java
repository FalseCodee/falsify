package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.stream.Collectors;

public class ESP extends Module {

    private final ModeSetting type = new ModeSetting("Type", "All", "All", "Players", "Mobs", "Animals");

    private int BOX;

    public ESP() {
        super("ESP", Category.PLAYER, GLFW.GLFW_KEY_DOWN);
        settings.add(type);
    }

    @Override
    public void onEnable() {
        this.BOX = GL11.glGenLists(1);
        GL11.glNewList(this.BOX, 4864);
        RenderUtils.drawOutlinedBox(new Box(-0.5D, 0.0D, -0.5D, 0.5D, 1.0D, 0.5D));
        GL11.glEndList();
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender3d eventRender3d) {
            if(mc.world == null) return;

            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(LivingEntity.class::isInstance).collect(Collectors.toList());

            switch (type.getMode()) {
                case "All": break;
                case "Players": entityList = entityList.stream().filter(PlayerEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
            }

            GL11.glPushAttrib(24581);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0F);
            GL11.glPushMatrix();
            GL11.glTranslated(-mc.getEntityRenderDispatcher().camera.getPos().x,
                    -mc.getEntityRenderDispatcher().camera.getPos().y,
                    -mc.getEntityRenderDispatcher().camera.getPos().z);
            
            RenderUtils.drawESPBoxes(entityList, this.BOX, eventRender3d.getTickDelta());
            GL11.glPopMatrix();
            GL11.glPopAttrib();


            }
        }
}
