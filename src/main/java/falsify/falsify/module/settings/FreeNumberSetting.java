package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventSettingChange;

import java.text.DecimalFormat;

public class FreeNumberSetting extends Setting<Double> {

    private final double increment;
    private final DecimalFormat numberFormat = new DecimalFormat("#.##");

    public FreeNumberSetting(String name, double value, double increment) {
        super(name);
        this.value = value;

        this.increment = increment;
    }

    @Override
    public void setValue(Double value) {
        super.setValue(value);
        EventSettingChange<FreeNumberSetting> event = new EventSettingChange<>(this);
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

    public double getIncrement() {
        return increment;
    }

    public DecimalFormat getNumberFormat() {
        return numberFormat;
    }
}
