package falsify.falsify.module.settings;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
        if(increment/2 - value > 0) {
            value = value - (value % increment);
        } else {
            value = value + increment - (value % increment);
        }

        super.setValue(value);
    }

    @Override
    public Double getValue() {
        return Double.valueOf(numberFormat.format(super.getValue()));
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
