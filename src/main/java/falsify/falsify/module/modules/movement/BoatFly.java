package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;

public class BoatFly extends Module {
    private final RangeSetting speed = new RangeSetting("Speed", 1, 0.1, 20, 0.1);
    private final RangeSetting lerp = new RangeSetting("Lerp", 0.5, 0.01, 1, 0.01);
    private final RangeSetting vd = new RangeSetting("Vert Div", 2, 0.1, 20, 0.1);
    public BoatFly() {
        super("BoatFly", "Fly with boats!", true, Category.MOVEMENT, -1);
        settings.add(speed);
        settings.add(lerp);
        settings.add(vd);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate && mc.player.hasVehicle()){
            double[] xzVel = MathUtils.directionSpeed(speed.getValue()/10.0);
            double yVel = MathUtils.getVerticalMov() * (speed.getValue()/10.0/vd.getValue());
            mc.player.getVehicle().setVelocity(xzVel[0], MathUtils.lerp(mc.player.getVelocity().y, yVel, lerp.getValue()), xzVel[1]);
        }
    }
}
