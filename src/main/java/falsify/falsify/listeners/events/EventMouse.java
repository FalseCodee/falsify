package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class EventMouse extends Event<EventMouse> {
    public final long window;
    public final int button;
    public final int action;
    public final int mods;

    public EventMouse(long window, int button, int action, int mods) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
    }

    public static class Scroll extends Event<EventMouse.Scroll> {
        private final long window;
        private final double horizontal;
        private final double vertical;

        public Scroll(long window, double horizontal, double vertical) {
            this.window = window;
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public long getWindow() {
            return window;
        }

        public double getHorizontal() {
            return horizontal;
        }

        public double getVertical() {
            return vertical;
        }
    }

    public static class MoveIngame extends Event<EventMouse.Scroll> {
        private double horizontal;
        private double vertical;

        public MoveIngame(double horizontal, double vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public double getHorizontal() {
            return horizontal;
        }

        public double getVertical() {
            return vertical;
        }

        public void setHorizontal(double horizontal) {
            this.horizontal = horizontal;
        }

        public void setVertical(double vertical) {
            this.vertical = vertical;
        }
    }
}
