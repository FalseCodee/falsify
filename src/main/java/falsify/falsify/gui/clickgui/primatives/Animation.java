package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;

import java.awt.*;

public class Animation {
    private long duration;
    private Type type;
    private State state;
    private double progress;

    private Timer timer;


    public Animation(long duration, Type type) {
        this.duration = duration;
        this.type = type;
        this.state = State.INACTIVE;
        this.timer = new Timer();

    }

    public void tick() {
        if(state == State.ACTIVE || state == State.INACTIVE) return;
        if(state == State.RISING) {
            progress += timer.timeElapsed(true)/1000.0/(duration/1000.0);
            if(progress < 1.0) return;

            state = State.ACTIVE;
            progress = 1.0;
        }
        else if(state == State.LOWERING) {
            progress -= timer.timeElapsed(true)/1000.0/(duration/1000.0);
            if(progress > 0) return;

            state = State.INACTIVE;
            progress = 0;
        }
    }

    public double run() {
        double t = 0.0;
        switch (type) {
            case LINEAR -> t = progress;
            case EASE_IN -> t = progress * progress;
            case EASE_OUT -> t = -1.0 * progress * progress + 2 * progress;
            case EASE_IN_OUT -> t = progress * progress * (3.0f - 2.0f * progress);
        }
        return t;
    }

    public double interpolate(double from, double to) {
        return MathUtils.lerp(from, to, run());
    }

    public Color color(Color from, Color to) {
        return new Color(
                (int) interpolate(from.getRed(), to.getRed()),
                (int) interpolate(from.getGreen(), to.getGreen()),
                (int) interpolate(from.getBlue(), to.getBlue()),
                (int) interpolate(from.getAlpha(), to.getAlpha())
        );
    }

    public void rise() {
        if(progress >= 1.0) state = State.ACTIVE;
        else state = State.RISING;
    }

    public void lower() {
        if(progress <= 0.0) state = State.INACTIVE;
        else state = State.LOWERING;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public enum Type {
        LINEAR,
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT
    }

    public enum State {
        INACTIVE,
        ACTIVE,
        RISING,
        LOWERING
    }
}


