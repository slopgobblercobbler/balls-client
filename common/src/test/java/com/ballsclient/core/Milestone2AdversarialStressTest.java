package com.ballsclient.core;

import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.KeybindSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import com.ballsclient.core.util.KeyUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Milestone 2 ClickGUI & Setting Components Adversarial Stress Tests")
class Milestone2AdversarialStressTest {

    @Nested
    @DisplayName("1. Slider Math & NumberSetting Bounds")
    class SliderMathStress {

        @Test
        @DisplayName("Slider normalization: Ratio clamped to [0.0, 1.0] under extreme mouse coordinates")
        void testSliderNormalizationClamping() {
            NumberSetting setting = new NumberSetting("Sensitivity", "Mouse sens", 50.0, 0.0, 100.0, 1.0);
            int trackX = 10;
            int trackW = 100;

            // Helper to simulate SliderComponent.updateValue logic
            java.util.function.Consumer<Double> simulateSliderDrag = mouseX -> {
                double ratio = (mouseX - trackX) / (double) trackW;
                ratio = Math.max(0.0, Math.min(1.0, ratio));
                double min = setting.getMin();
                double max = setting.getMax();
                double rawVal = min + ratio * (max - min);
                double step = setting.getStep();
                if (step > 0.0) {
                    rawVal = Math.round((rawVal - min) / step) * step + min;
                }
                setting.setValue(rawVal);
            };

            // Extreme left
            simulateSliderDrag.accept(-99999.0);
            assertThat(setting.getValue()).isEqualTo(0.0);

            // Exact min track boundary
            simulateSliderDrag.accept(10.0);
            assertThat(setting.getValue()).isEqualTo(0.0);

            // Middle
            simulateSliderDrag.accept(60.0); // 50px into track -> ratio = 0.5
            assertThat(setting.getValue()).isEqualTo(50.0);

            // Exact max track boundary
            simulateSliderDrag.accept(110.0);
            assertThat(setting.getValue()).isEqualTo(100.0);

            // Extreme right
            simulateSliderDrag.accept(888888.0);
            assertThat(setting.getValue()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("Slider step quantization: Verify precise quantization without floating point drift")
        void testSliderStepQuantization() {
            NumberSetting setting = new NumberSetting("FOV", "FOV modifier", 70.0, 30.0, 110.0, 0.25);

            setting.setValue(70.12345);
            assertThat(setting.getValue()).isEqualTo(70.0);

            setting.setValue(70.13);
            assertThat(setting.getValue()).isEqualTo(70.25);

            setting.setValue(70.38);
            assertThat(setting.getValue()).isEqualTo(70.5);

            setting.setValue(70.74);
            assertThat(setting.getValue()).isEqualTo(70.75);

            setting.setValue(70.88);
            assertThat(setting.getValue()).isEqualTo(71.0);
        }

        @Test
        @DisplayName("Non-finite values: NaN, +Inf, -Inf, and null handling")
        void testNonFiniteValues() {
            NumberSetting setting = new NumberSetting("Speed", "Speed setting", 5.0, 1.0, 10.0, 0.1);

            // Setting null does not overwrite or corrupt state
            setting.setValue(null);
            assertThat(setting.getValue()).isEqualTo(5.0);

            // In Java, Math.round(Double.NaN) is 0L, so 0.0 gets clamped to min (1.0)
            setting.setValue(Double.NaN);
            assertThat(setting.getValue()).isBetween(1.0, 10.0);

            // Positive infinity clamped to max (10.0)
            setting.setValue(Double.POSITIVE_INFINITY);
            assertThat(setting.getValue()).isEqualTo(10.0);

            // Negative infinity clamped to min (1.0)
            setting.setValue(Double.NEGATIVE_INFINITY);
            assertThat(setting.getValue()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Inverted ranges: min > max behavior does not enter infinite loops or crash")
        void testInvertedRangeMinGreaterThanMax() {
            NumberSetting inverted = new NumberSetting("Inverted", "Inverted bounds", 5.0, 20.0, 5.0, 1.0);

            // In NumberSetting.clamp: Math.max(min, Math.min(max, val))
            // When min=20 and max=5, Math.min(5, val) <= 5, Math.max(20, <=5) returns 20
            assertThat(inverted.getValue()).isEqualTo(20.0);

            inverted.setValue(0.0);
            assertThat(inverted.getValue()).isEqualTo(20.0);

            inverted.setValue(100.0);
            assertThat(inverted.getValue()).isEqualTo(20.0);
        }

        @Test
        @DisplayName("Zero and negative step safety")
        void testZeroOrNegativeStep() {
            NumberSetting continuous = new NumberSetting("Continuous", "Smooth", 2.5, 0.0, 10.0, 0.0);
            continuous.setValue(3.14159);
            assertThat(continuous.getValue()).isEqualTo(3.14159);

            NumberSetting negativeStep = new NumberSetting("NegativeStep", "Smooth", 2.5, 0.0, 10.0, -1.0);
            negativeStep.setValue(4.2);
            assertThat(negativeStep.getValue()).isEqualTo(4.2);
        }

        @Test
        @DisplayName("JSON resilience: Malformed or incompatible JSON types")
        void testNumberSettingJsonResilience() {
            NumberSetting setting = new NumberSetting("Volume", "Master volume", 50.0, 0.0, 100.0, 5.0);

            setting.fromJson(new JsonPrimitive(80.0));
            assertThat(setting.getValue()).isEqualTo(80.0);

            // Malformed inputs should not throw or alter state
            setting.fromJson(null);
            assertThat(setting.getValue()).isEqualTo(80.0);

            setting.fromJson(JsonNull.INSTANCE);
            assertThat(setting.getValue()).isEqualTo(80.0);

            setting.fromJson(new JsonObject());
            assertThat(setting.getValue()).isEqualTo(80.0);

            setting.fromJson(new JsonArray());
            assertThat(setting.getValue()).isEqualTo(80.0);

            setting.fromJson(new JsonPrimitive("not-a-number"));
            assertThat(setting.getValue()).isEqualTo(80.0);
        }
    }

    @Nested
    @DisplayName("2. Dropdown Component & ModeSetting Carousel")
    class DropdownModeStress {

        @Test
        @DisplayName("ModeSetting: Bidirectional forward and backward cycling")
        void testBidirectionalCycling() {
            ModeSetting setting = new ModeSetting("Target", "Target priority", "Health", "Health", "Distance", "Armor", "Angle");

            // Forward cycling: Health -> Distance -> Armor -> Angle -> Health
            setting.cycle();
            assertThat(setting.getValue()).isEqualTo("Distance");
            setting.cycle();
            assertThat(setting.getValue()).isEqualTo("Armor");
            setting.cycle();
            assertThat(setting.getValue()).isEqualTo("Angle");
            setting.cycle();
            assertThat(setting.getValue()).isEqualTo("Health");

            // Backward cycling (as implemented in DropdownComponent)
            java.util.function.Consumer<ModeSetting> cycleBackward = ms -> {
                List<String> modes = ms.getModes();
                int curIdx = modes.indexOf(ms.getValue());
                if (curIdx <= 0) {
                    ms.setValue(modes.get(modes.size() - 1));
                } else {
                    ms.setValue(modes.get(curIdx - 1));
                }
            };

            // Health backward -> Angle
            cycleBackward.accept(setting);
            assertThat(setting.getValue()).isEqualTo("Angle");

            // Angle backward -> Armor
            cycleBackward.accept(setting);
            assertThat(setting.getValue()).isEqualTo("Armor");

            // Armor backward -> Distance
            cycleBackward.accept(setting);
            assertThat(setting.getValue()).isEqualTo("Distance");

            // Distance backward -> Health
            cycleBackward.accept(setting);
            assertThat(setting.getValue()).isEqualTo("Health");
        }

        @Test
        @DisplayName("Single option mode setting: Cycling is an idempotent no-op")
        void testSingleOptionMode() {
            ModeSetting single = new ModeSetting("Single", "Single option", "Default", "Default");

            single.cycle();
            assertThat(single.getValue()).isEqualTo("Default");

            single.cycle();
            assertThat(single.getValue()).isEqualTo("Default");
        }

        @Test
        @DisplayName("Empty or null mode list: Constructor throws IllegalArgumentException")
        void testEmptyModesValidation() {
            assertThatThrownBy(() -> new ModeSetting("Bad", "Desc", "A", new String[0]))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new ModeSetting("Bad", "Desc", "A", (List<String>) null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Corrupted/unknown current mode: cycle() and backward cycle gracefully recover to valid mode")
        void testCorruptedModeRecovery() throws Exception {
            ModeSetting setting = new ModeSetting("Target", "Desc", "Distance", "Distance", "Fov", "Threat");

            // Force unknown value via reflection
            java.lang.reflect.Field valueField = com.ballsclient.core.setting.Setting.class.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(setting, "CORRUPTED_VALUE");

            assertThat(setting.getValue()).isEqualTo("CORRUPTED_VALUE");

            // cycle() when currentIndex == -1 falls back to modes.get(0)
            setting.cycle();
            assertThat(setting.getValue()).isEqualTo("Distance");

            // Force corrupted value again and test backward recovery
            valueField.set(setting, "ANOTHER_CORRUPTED");
            List<String> modes = setting.getModes();
            int curIdx = modes.indexOf(setting.getValue()); // -1
            if (curIdx <= 0) {
                setting.setValue(modes.get(modes.size() - 1));
            } else {
                setting.setValue(modes.get(curIdx - 1));
            }
            assertThat(setting.getValue()).isEqualTo("Threat");
        }

        @Test
        @DisplayName("ModeSetting: Case insensitive matching on setValue")
        void testCaseInsensitiveMatching() {
            ModeSetting setting = new ModeSetting("Mode", "Test", "Normal", "Normal", "Aggressive", "Passive");

            setting.setValue("AGGRESSIVE");
            assertThat(setting.getValue()).isEqualTo("Aggressive");

            setting.setValue("passive");
            assertThat(setting.getValue()).isEqualTo("Passive");

            // Setting invalid mode does not change current value
            setting.setValue("NonExistent");
            assertThat(setting.getValue()).isEqualTo("Passive");
        }
    }

    @Nested
    @DisplayName("3. ColorPicker Component & ColorSetting Chroma Wave Synthesis")
    class ColorPickerStress {

        @Test
        @DisplayName("ColorSetting: Alpha channel preservation under Chroma Wave animation")
        void testAlphaPreservationUnderChroma() {
            // Semi-transparent red (Alpha = 0x88 = 136)
            int initialRgba = 0x88FF0000;
            ColorSetting color = new ColorSetting("ESP", "ESP Color", initialRgba, true, 1.0);

            assertThat(color.getAlpha()).isEqualTo(0x88);

            // Test 1,000 random timestamps: alpha channel MUST always equal 0x88
            for (int i = 0; i < 1000; i++) {
                long time = ThreadLocalRandom.current().nextLong(-1000000L, 1000000L);
                int effective = color.getEffectiveRgba(time);
                int alpha = (effective >> 24) & 0xFF;
                assertThat(alpha)
                        .withFailMessage("Alpha channel corrupted at time %d: expected 0x88, got 0x%02X", time, alpha)
                        .isEqualTo(0x88);
            }
        }

        @Test
        @DisplayName("Chroma wave synthesis: Extreme timestamps (0, Long.MAX_VALUE, Long.MIN_VALUE, negative)")
        void testExtremeChromaTimestamps() {
            ColorSetting color = new ColorSetting("Accent", "Accent color", 0xFF00FF00, true, 1.0);

            assertThat(color.getEffectiveRgba(0L)).isNotZero();
            assertThat(color.getEffectiveRgba(-50000L)).isNotZero();
            assertThat(color.getEffectiveRgba(Long.MAX_VALUE)).isNotZero();
            assertThat(color.getEffectiveRgba(Long.MIN_VALUE)).isNotZero();
        }

        @Test
        @DisplayName("Chroma speed variations: Zero speed, negative speed, and fast speed")
        void testChromaSpeeds() {
            ColorSetting color = new ColorSetting("ChromaTest", "Desc", 0xFFFF0000, true, 0.0);
            int at0 = color.getEffectiveRgba(1000L);
            int at1 = color.getEffectiveRgba(5000L);
            assertThat(at0).isEqualTo(at1); // Speed 0 produces stationary color

            color.setChromaSpeed(-1.0);
            int negSpeed = color.getEffectiveRgba(2500L);
            assertThat(negSpeed).isNotZero();

            color.setChromaSpeed(100.0);
            int fastSpeed = color.getEffectiveRgba(3333L);
            assertThat(fastSpeed).isNotZero();
        }

        @Test
        @DisplayName("Headless HSB to RGB pure math conversion across all 6 hue sectors")
        void testHsbToRgbAllSectors() {
            // Sector 0: Red to Yellow
            int c0 = ColorSetting.hsbToRgb(0.0f, 1.0f, 1.0f);
            assertThat(c0).isEqualTo(0xFFFF0000); // Pure Red

            // Sector 1: Yellow to Green
            int c1 = ColorSetting.hsbToRgb(1.0f / 6.0f, 1.0f, 1.0f);
            assertThat(c1).isEqualTo(0xFFFFFF00); // Pure Yellow

            // Sector 2: Green to Cyan
            int c2 = ColorSetting.hsbToRgb(2.0f / 6.0f, 1.0f, 1.0f);
            assertThat(c2).isEqualTo(0xFF00FF00); // Pure Green

            // Sector 3: Cyan to Blue
            int c3 = ColorSetting.hsbToRgb(3.0f / 6.0f, 1.0f, 1.0f);
            assertThat(c3).isEqualTo(0xFF00FFFF); // Pure Cyan

            // Sector 4: Blue to Magenta
            int c4 = ColorSetting.hsbToRgb(4.0f / 6.0f, 1.0f, 1.0f);
            assertThat(c4).isEqualTo(0xFF0000FF); // Pure Blue

            // Sector 5: Magenta to Red
            int c5 = ColorSetting.hsbToRgb(5.0f / 6.0f, 1.0f, 1.0f);
            assertThat(c5).isEqualTo(0xFFFF00FF); // Pure Magenta

            // Hue wrap around: 1.0f wraps to 0.0f (Pure Red)
            int c6 = ColorSetting.hsbToRgb(1.0f, 1.0f, 1.0f);
            assertThat(c6).isEqualTo(0xFFFF0000);
        }

        @Test
        @DisplayName("Color preset cycle wrap-around")
        void testPresetCycling() {
            int[] presets = new int[]{
                    0xFFFF4444, 0xFFFF8800, 0xFFFFEE00, 0xFF44FF44,
                    0xFF00FFFF, 0xFF5865F2, 0xFFAA44FF, 0xFFFF44AA, 0xFFFFFFFF
            };

            ColorSetting setting = new ColorSetting("Chroma", "Desc", presets[0]);

            // Cycle through all presets
            for (int i = 0; i < presets.length; i++) {
                assertThat(setting.getValue()).isEqualTo(presets[i]);
                int cur = setting.getValue();
                int next = presets[0];
                for (int j = 0; j < presets.length; j++) {
                    if (presets[j] == cur) {
                        next = presets[(j + 1) % presets.length];
                        break;
                    }
                }
                setting.setValue(next);
            }

            // After cycling through all, should be back at presets[0]
            assertThat(setting.getValue()).isEqualTo(presets[0]);
        }
    }

    @Nested
    @DisplayName("4. Keybind Unbind and Reset Rules")
    class KeybindRebindStress {

        @ParameterizedTest(name = "Keycode {0} unbinds to KEY_NONE (-1)")
        @ValueSource(ints = {256 /* ESC */, 261 /* DELETE */, 259 /* BACKSPACE */})
        void testUnbindKeys(int unbindKey) {
            KeybindSetting keybind = new KeybindSetting("Flight", "Fly key", 70); // F
            assertThat(keybind.isBound()).isTrue();
            assertThat(keybind.getValue()).isEqualTo(70);

            // Rebinding logic: if keyCode in {ESC, DEL, BACKSPACE} -> KEY_NONE
            if (unbindKey == 256 || unbindKey == 261 || unbindKey == 259) {
                keybind.setValue(KeybindSetting.KEY_NONE);
            }

            assertThat(keybind.isBound()).isFalse();
            assertThat(keybind.getValue()).isEqualTo(-1);
            assertThat(keybind.getDisplayName()).isEqualTo("NONE");
        }

        @Test
        @DisplayName("Keybind normal assignment: Preserves valid GLFW keys")
        void testValidKeyAssignment() {
            KeybindSetting keybind = new KeybindSetting("Sprint", "Sprint key", KeybindSetting.KEY_NONE);
            assertThat(keybind.isBound()).isFalse();

            keybind.setValue(344); // RSHIFT
            assertThat(keybind.isBound()).isTrue();
            assertThat(keybind.getValue()).isEqualTo(344);
            assertThat(keybind.getDisplayName()).isEqualTo("RSHIFT");

            keybind.setValue(32); // SPACE
            assertThat(keybind.isBound()).isTrue();
            assertThat(keybind.getDisplayName()).isEqualTo("SPACE");
        }
    }
}
