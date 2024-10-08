package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;

public class Flight extends Module {
    private final RangeSetting speed = new RangeSetting("Speed", 1, 0.1, 20, 0.1);
    private final RangeSetting lerp = new RangeSetting("Lerp", 0.5, 0.01, 1, 0.01);
    private final RangeSetting vd = new RangeSetting("Vert Div", 1, 0.1, 20, 0.1);
    public Flight() {
        super("Flight", "I wonder what this one does...", true, Category.MISC, -1);
        settings.add(speed);
        settings.add(vd);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate){
            double[] xzVel = MathUtils.directionSpeed(speed.getValue()/10.0);
            double yVel = MathUtils.getVerticalMov() * (speed.getValue()/10.0/vd.getValue());
            mc.player.setVelocity(MathUtils.lerp(mc.player.getVelocity().x, xzVel[0], lerp.getValue()),
                    MathUtils.lerp(mc.player.getVelocity().y, yVel, lerp.getValue()),
                    MathUtils.lerp(mc.player.getVelocity().z, xzVel[1], lerp.getValue()));
        }
    }
}
