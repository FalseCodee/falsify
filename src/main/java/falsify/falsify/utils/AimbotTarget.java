package falsify.falsify.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class AimbotTarget {
    private Vec3d location;
    private LivingEntity entity;

    public AimbotTarget(Vec3d location) {
        this.location = location;
    }

    public AimbotTarget(LivingEntity entity) {
        this.entity = entity;
        this.location = entity.getPos();
    }

    public Vec3d getLocation() {
        return location;
    }

    public void setLocation(Vec3d location) {
        this.location = location;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }
}
