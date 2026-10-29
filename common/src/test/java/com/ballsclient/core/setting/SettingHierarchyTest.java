package com.ballsclient.core.setting;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Setting Hierarchy & Mutation Test Suite")
class SettingHierarchyTest {

    @Test
    @DisplayName("BooleanSetting: Toggle and listener notification")
    void testBooleanSetting() {
        BooleanSetting setting = new BooleanSetting("TestBool", "Description", false);
        assertThat(setting.getValue()).isFalse();

        AtomicBoolean notified = new AtomicBoolean(false);
        setting.onChange(val -> notified.set(val));

        setting.toggle();
        assertThat(setting.getValue()).isTrue();
        assertThat(notified.get()).isTrue();

        JsonElement json = setting.toJson();
        assertThat(json.getAsBoolean()).isTrue();

        setting.fromJson(new com.google.gson.JsonPrimitive(false));
        assertThat(setting.getValue()).isFalse();
    }

    @Test
    @DisplayName("NumberSetting: Min/Max bounds clamping, step quantization and type conversions")
    void testNumberSettingClampingAndStepping() {
        NumberSetting setting = new NumberSetting("Zoom", "Zoom factor", 2.0, 1.0, 10.0, 0.5);

        // Clamp upper bound
        setting.setValue(15.0);
        assertThat(setting.getValue()).isEqualTo(10.0);

        // Clamp lower bound
        setting.setValue(0.2);
        assertThat(setting.getValue()).isEqualTo(1.0);

        // Quantize to step 0.5 (2.33 -> 2.5)
        setting.setValue(2.33);
        assertThat(setting.getValue()).isEqualTo(2.5);

        // Integer and float conversions
        setting.setValue(4.5);
        assertThat(setting.getIntValue()).isEqualTo(5); // Math.round(4.5)
        assertThat(setting.getFloatValue()).isEqualTo(4.5f);

        // Reset to default
        setting.reset();
        assertThat(setting.getValue()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("ModeSetting: Validation, cycling, case-insensitivity, and predicates")
    void testModeSetting() {
        ModeSetting mode = new ModeSetting("Layout", "Display style", "COMPACT", "COMPACT", "DETAILED", "MINIMAL");
        assertThat(mode.getValue()).isEqualTo("COMPACT");
        assertThat(mode.is("compact")).isTrue();
        assertThat(mode.is("DETAILED")).isFalse();

        // Cycle through options
        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("DETAILED");

        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("MINIMAL");

        // Cycle wraps back to beginning
        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("COMPACT");

        // Case-insensitive direct assignment
        mode.setValue("detailed");
        assertThat(mode.getValue()).isEqualTo("DETAILED");

        // Invalid assignment ignored
        mode.setValue("INVALID_MODE");
        assertThat(mode.getValue()).isEqualTo("DETAILED");

        // Constructor throws on missing default
        assertThatThrownBy(() -> new ModeSetting("Bad", "Desc", "MISSING", "A", "B"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("ColorSetting: Channel extraction, float normalization, and Chroma Wave generation")
    void testColorSetting() {
        // ARGB: 0x80FF00AA -> Alpha: 128, Red: 255, Green: 0, Blue: 170
        int rgba = (128 << 24) | (255 << 16) | (0 << 8) | 170;
        ColorSetting color = new ColorSetting("Accent", "Accent color", rgba, false, 1.0);

        assertThat(color.getAlpha()).isEqualTo(128);
        assertThat(color.getRed()).isEqualTo(255);
        assertThat(color.getGreen()).isEqualTo(0);
        assertThat(color.getBlue()).isEqualTo(170);

        assertThat(color.getRedFloat()).isEqualTo(1.0f);
        assertThat(color.getGreenFloat()).isEqualTo(0.0f);
        assertThat(color.getAlphaFloat()).isBetween(0.5f, 0.51f);

        // When chroma is false, effective RGBA equals static value
        assertThat(color.getEffectiveRgba(1000L)).isEqualTo(rgba);

        // Enable chroma wave
        color.setChroma(true);
        color.setChromaSpeed(2.0);
        int chromaRgba = color.getEffectiveRgba(5000L);
        assertThat((chromaRgba >> 24) & 0xFF).isEqualTo(128); // Preserves alpha channel
    }

    @Test
    @DisplayName("KeybindSetting: Matching, unbound checks, getDisplayName and GLFW keycodes")
    void testKeybindSetting() {
        KeybindSetting keybind = new KeybindSetting("Bind", "Key", KeybindSetting.KEY_NONE);
        assertThat(keybind.isBound()).isFalse();
        assertThat(keybind.getDisplayName()).isEqualTo("NONE");
        assertThat(keybind.matches(65)).isFalse();

        keybind.setValue(0);
        assertThat(keybind.isBound()).isFalse();
        assertThat(keybind.getDisplayName()).isEqualTo("NONE");

        keybind.setValue(65); // Key A
        assertThat(keybind.isBound()).isTrue();
        assertThat(keybind.getDisplayName()).isEqualTo("A");
        assertThat(keybind.matches(65)).isTrue();
        assertThat(keybind.matches(66)).isFalse();

        keybind.setValue(344); // RSHIFT
        assertThat(keybind.getDisplayName()).isEqualTo("RSHIFT");

        keybind.setValue(32); // SPACE
        assertThat(keybind.getDisplayName()).isEqualTo("SPACE");

        keybind.setValue(67); // C
        assertThat(keybind.getDisplayName()).isEqualTo("C");

        keybind.setValue(66); // B
        assertThat(keybind.getDisplayName()).isEqualTo("B");

        keybind.setValue(82); // R
        assertThat(keybind.getDisplayName()).isEqualTo("R");
    }

    @Test
    @DisplayName("Visibility: Conditional visibility reacts dynamically to supplier condition")
    void testConditionalVisibility() {
        BooleanSetting masterToggle = new BooleanSetting("Master", "Master switch", false);
        NumberSetting subSetting = new NumberSetting("Sub", "Dependent slider", 5.0, 1.0, 10.0, 1.0);

        subSetting.visibleWhen(masterToggle::getValue);

        assertThat(subSetting.isVisible()).isFalse();
        masterToggle.setValue(true);
        assertThat(subSetting.isVisible()).isTrue();
        masterToggle.setValue(false);
        assertThat(subSetting.isVisible()).isFalse();
    }

    @Test
    @DisplayName("Exception Isolation: Failing setting listener does not crash setValue or other listeners")
    void testListenerExceptionIsolation() {
        BooleanSetting setting = new BooleanSetting("SafeBool", "Safety test", false);
        AtomicInteger executedCount = new AtomicInteger(0);

        // First listener throws
        setting.onChange(val -> {
            throw new RuntimeException("Simulated listener crash");
        });

        // Second listener succeeds
        setting.onChange(val -> executedCount.incrementAndGet());

        // Mutation should proceed safely without exception
        setting.setValue(true);
        assertThat(setting.getValue()).isTrue();
        assertThat(executedCount.get()).isEqualTo(1);
    }
}
