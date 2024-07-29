package falsify.falsify.gui.utils;

import falsify.falsify.Falsify;
import falsify.falsify.utils.MathUtils;

import java.awt.*;

public class Animation {

    /**
     * The duration of the animation in milliseconds.
     */
    private long duration;

    /**
     * The type of easing applied to the animation.
     */
    private Type type;

    /**
     * The current state of the animation.
     */
    private State state;

    /**
     * The progress of the animation (0.0 to 1.0).
     */
    private double progress;

    /**
     * Constructs a new Animation with the specified duration and type.
     *
     * @param duration The duration of the animation in milliseconds.
     * @param type     The type of easing to apply.
     */
    public Animation(long duration, Type type) {
        this.duration = duration;
        this.type = type;
        this.state = State.INACTIVE;
    }

    /**
     * Updates the animation state based on the current frame time.
     */
    public void tick() {
        if (state == State.ACTIVE || state == State.INACTIVE) {
            return;
        }

        if (state == State.RISING) {
            progress += Falsify.mc.getRenderTickCounter().getLastFrameDuration() / 20.0 / (duration / 1000.0);
            if (progress < 1.0) {
                return;
            }
            state = State.ACTIVE;
            progress = 1.0;
        } else if (state == State.LOWERING) {
            progress -= Falsify.mc.getRenderTickCounter().getLastFrameDuration() / 20.0 / (duration / 1000.0);
            if (progress > 0) {
                return;
            }
            state = State.INACTIVE;
            progress = 0;
        }
    }

    /**
     * Calculates the current animation progress based on the chosen easing type.
     *
     * @return The animation progress value between 0.0 and 1.0.
     */
    public double run() {
        return switch (type) {
            case LINEAR -> progress;
            case EASE_IN -> progress * progress;
            case EASE_OUT -> -1.0 * progress * progress + 2 * progress;
            case EASE_IN_OUT -> progress * progress * (3.0f - 2.0f * progress);
        };
    }

    /**
     * Interpolates between two values based on the current animation progress.
     *
     * @param from The starting value.
     * @param to   The ending value.
     * @return The interpolated value between `from` and `to`.
     */
    public double interpolate(double from, double to) {
        return MathUtils.lerp(from, to, run());
    }

    /**
     * Interpolates between two colors based on the current animation progress.
     *
     * @param from The starting color.
     * @param to   The ending color.
     * @return The interpolated color between `from` and `to`.
     */
    public Color color(Color from, Color to) {
        return new Color(
                (int) interpolate(from.getRed(), to.getRed()),
                (int) interpolate(from.getGreen(), to.getGreen()),
                (int) interpolate(from.getBlue(), to.getBlue()),
                (int) interpolate(from.getAlpha(), to.getAlpha())
        );
    }

    /**
     * Starts the animation by setting its state to RISING.
     */
    public void rise() {
        if (progress >= 1.0) {
            state = State.ACTIVE;
        } else {
            state = State.RISING;
        }
    }

    /**
     * Ends the animation by setting its state to LOWERING.
     */
    public void lower() {
        if (progress <= 0.0) {
            state = State.INACTIVE;
        } else {
            state = State.LOWERING;
        }
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


