package falsify.falsify.module.modules.render;

import falsify.falsify.gui.clickgui.primatives.Animation;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.*;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;

public class Zoom extends Module {
    private final RangeSetting zoomAmount = new RangeSetting("Amount", 2, 0.1, 500, 0.1);
    private final BooleanSetting smoothCamera = new BooleanSetting("Smooth Camera", false);
    private final Animation smooth = new Animation(250, Animation.Type.EASE_IN_OUT);

    private double inertia = 0.0;
    private boolean released = false;
    public Zoom() {
        super("Zoom", "Who needs binoculars anyways.", false, Category.RENDER, -1);
        settings.add(zoomAmount);
        settings.add(smoothCamera);
    }

    public double getAmount() {
        return zoomAmount.getValue()*smooth.run() + 1;
    }

    @Override
    public void onEnable() {
        released = false;
    }

    @Override
    public void onDisable() {
        smooth.setProgress(0.0);
        mc.options.smoothCameraEnabled = false;
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventKey e && keybind.getValue() != -1 && e.getKey() == keybind.getValue() && e.getAction() == 0) {
            this.released = true;
        }
        if(event instanceof EventRender e) {
            if(released) {
                smooth.lower();
                if(smooth.getProgress() == 0.0) this.toggle();
            }
            else smooth.rise();
            mc.options.smoothCameraEnabled = smoothCamera.getValue();
            if(Math.abs(inertia) > 0.1) {
                zoomAmount.setValue(zoomAmount.getValue() + inertia);
                inertia *= 0.5;
            }
            smooth.tick();
        }
        if(event instanceof EventMouse.Scroll e) {
            inertia += (zoomAmount.getValue()+1)*e.getVertical()/10;
            e.setCancelled(true);
        }
        if(event instanceof EventMouse.MoveIngame e) {
            e.setHorizontal(e.getHorizontal() / getAmount()*2);
            e.setVertical(e.getVertical() / getAmount()*2);
        }
    }
}
