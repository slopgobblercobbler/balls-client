package com.ballsclient.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Double-precision numerical setting with min/max bounds and step quantization.
 */
public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
        super.setValue(clamp(roundToStep(defaultValue)));
    }

    @Override
    public void setValue(Double newValue) {
        if (newValue != null) {
            super.setValue(clamp(roundToStep(newValue)));
        }
    }

    private double clamp(double val) {
        return Math.max(min, Math.min(max, val));
    }

    private double roundToStep(double val) {
        if (step <= 0) {
            return val;
        }
        double rounded = Math.round(val / step) * step;
        return Math.round(rounded * 10000.0) / 10000.0;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public int getIntValue() {
        return (int) Math.round(getValue());
    }

    public float getFloatValue() {
        return getValue().floatValue();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            try {
                setValue(element.getAsDouble());
            } catch (Exception ignored) {
            }
        }
    }
}
