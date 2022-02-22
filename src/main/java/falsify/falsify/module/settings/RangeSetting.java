package falsify.falsify.module.settings;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RangeSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final DecimalFormat numberFormat;

    public RangeSetting(String name, double value, double min, double max, DecimalFormat format) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.numberFormat = format;
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

    public DecimalFormat getNumberFormat() {
        return numberFormat;
    }
}
