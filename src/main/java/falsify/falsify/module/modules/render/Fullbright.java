package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Fullbright extends Module {

    private final RangeSetting gamma = new RangeSetting("Gamma", 30, 1, 100, 0.5);

    public Fullbright() {
        super("Fullbright", Category.PLAYER, -1);
        settings.add(gamma);
    }

    public Double getValue() {
        return gamma.getValue()/10d;
    }
}
