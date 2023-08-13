package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventSettingChange;

import java.text.DecimalFormat;

public class RangeSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double increment;
    private final DecimalFormat numberFormat = new DecimalFormat("#.##");

    public RangeSetting(String name, double value, double min, double max, double increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    @Override
    public void setValue(Double value) {
        super.setValue(Math.min(max, Math.max(value, min)));
        EventSettingChange<RangeSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }

    @Override
    public Double getValue() {
        double val = value;
        double offset = (val % increment);

        if(offset - increment/2 > 0) {
            val += increment - offset;
        } else {
            val -= offset;
        }
        return Double.valueOf(numberFormat.format(val));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    public DecimalFormat getNumberFormat() {
        return numberFormat;
    }
}
