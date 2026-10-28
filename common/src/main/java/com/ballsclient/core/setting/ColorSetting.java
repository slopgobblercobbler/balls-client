package com.ballsclient.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 32-bit ARGB color setting with dynamic Chroma Wave synthesis.
 * Uses pure math headless HSB conversion.
 */
public class ColorSetting extends Setting<Integer> {
    private boolean chroma;
    private double chromaSpeed;

    public ColorSetting(String name, String description, int defaultRgba) {
        this(name, description, defaultRgba, false, 1.0);
    }

    public ColorSetting(String name, String description, int defaultRgba, boolean chroma, double chromaSpeed) {
        super(name, description, defaultRgba);
        this.chroma = chroma;
        this.chromaSpeed = chromaSpeed;
    }

    public int getRed() {
        return (getValue() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getValue() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getValue() & 0xFF;
    }

    public int getAlpha() {
        return (getValue() >> 24) & 0xFF;
    }

    public float getRedFloat() {
        return getRed() / 255.0f;
    }

    public float getGreenFloat() {
        return getGreen() / 255.0f;
    }

    public float getBlueFloat() {
        return getBlue() / 255.0f;
    }

    public float getAlphaFloat() {
        return getAlpha() / 255.0f;
    }

    public boolean isChroma() {
        return chroma;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
    }

    public double getChromaSpeed() {
        return chromaSpeed;
    }

    public void setChromaSpeed(double chromaSpeed) {
        this.chromaSpeed = chromaSpeed;
    }

    public int getEffectiveRgba(long timeMillis) {
        if (!chroma) {
            return getValue();
        }
        float hue = (float) (((timeMillis * chromaSpeed * 0.001)) % 1.0);
        if (hue < 0.0f) {
            hue += 1.0f;
        }
        int rgb = hsbToRgb(hue, 0.8f, 1.0f);
        return (getAlpha() << 24) | (rgb & 0x00FFFFFF);
    }

    /**
     * Headless pure math HSB to RGB converter.
     */
    public static int hsbToRgb(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 1 -> {
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 2 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                }
                case 3 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                default -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                }
            }
        }
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rgba", getValue());
        obj.addProperty("chroma", chroma);
        obj.addProperty("chromaSpeed", chromaSpeed);
        return obj;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("rgba")) {
                setValue(obj.get("rgba").getAsInt());
            }
            if (obj.has("chroma")) {
                setChroma(obj.get("chroma").getAsBoolean());
            }
            if (obj.has("chromaSpeed")) {
                setChromaSpeed(obj.get("chromaSpeed").getAsDouble());
            }
        } else if (element != null && element.isJsonPrimitive()) {
            try {
                setValue(element.getAsInt());
            } catch (Exception ignored) {
            }
        }
    }
}
