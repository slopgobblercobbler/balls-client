package com.ballsclient.fabric263.gui.clickgui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.KeybindSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import com.ballsclient.fabric263.gui.clickgui.component.ColorPickerComponent;
import com.ballsclient.fabric263.gui.clickgui.component.DropdownComponent;
import com.ballsclient.fabric263.gui.clickgui.component.SliderComponent;
import com.ballsclient.fabric263.gui.clickgui.component.ToggleComponent;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Headless empirical verification harness for Milestone 2 Modern ClickGUI.
 * Verifies category mappings, keybind rebinding, config persistence, and component physics.
 */
public final class ClickGuiVerificationHarness {
    private ClickGuiVerificationHarness() {}

    public static void runAllVerifications() {
        System.out.println("=== Starting ClickGUI Empirical Verification ===");

        // 1. Initialize client core if not yet initialized
        BallsClientCore.init();

        // 2. Verify Category mapping coverage
        verifyCategoryMapping();

        // 3. Verify Keybind rebinding state machine
        verifyKeybindRebinding();

        // 4. Verify Setting components logic
        verifySettingComponents();

        // 5. Verify ClickGuiConfig JSON persistence
        verifyConfigPersistence();

        System.out.println("=== All ClickGUI Empirical Verifications PASSED ===");
    }

    private static void verifyCategoryMapping() {
        List<CategoryPanel> panels = List.of(
            new CategoryPanel("Combat", 0xFFED4245, 20, 30, Category.COMBAT),
            new CategoryPanel("Movement", 0xFF00E5FF, 150, 30, Category.MOVEMENT),
            new CategoryPanel("Render", 0xFF9B59B6, 280, 30, Category.RENDER),
            new CategoryPanel("Player", 0xFF43B581, 410, 30, Category.PLAYER),
            new CategoryPanel("World", 0xFFFFAA00, 540, 30, Category.INVENTORY, Category.AUTOMATION),
            new CategoryPanel("Misc", 0xFF5865F2, 670, 30, Category.NETWORK)
        );

        if (panels.size() != 6) {
            throw new AssertionError("Expected exactly 6 category panels, got: " + panels.size());
        }

        Set<String> titles = new HashSet<>();
        for (CategoryPanel p : panels) {
            titles.add(p.getTitle());
        }

        String[] expected = {"Combat", "Movement", "Render", "Player", "World", "Misc"};
        for (String exp : expected) {
            if (!titles.contains(exp)) {
                throw new AssertionError("Missing expected panel: " + exp);
            }
        }

        // Count total modules mapped across panels
        int totalMapped = 0;
        Set<String> moduleIds = new HashSet<>();
        for (CategoryPanel p : panels) {
            for (ModuleCard card : p.getCards()) {
                Module m = card.getModule();
                if (!moduleIds.add(m.getId())) {
                    throw new AssertionError("Duplicate module in panels: " + m.getId());
                }
                totalMapped++;
            }
        }

        int coreTotal = BallsClientCore.getModuleManager().getModules().size();
        System.out.println("[Verification] Total core modules: " + coreTotal + ", Total mapped in panels: " + totalMapped);
        if (totalMapped != coreTotal) {
            throw new AssertionError("Module count mismatch! Core: " + coreTotal + ", Mapped: " + totalMapped);
        }
        if (totalMapped < 500) {
            throw new AssertionError("Expected at least 500 modules mapped, found: " + totalMapped);
        }
        System.out.println("[Verification] Category mapping verified successfully: all " + totalMapped + " modules mapped across 6 panels.");
    }

    private static void verifyKeybindRebinding() {
        Module testModule = new Module("test_rebind", "Test Rebind", "Rebinding test module", Category.COMBAT, KeybindSetting.KEY_NONE) {};
        ModuleCard card = new ModuleCard(testModule);

        // Initially not rebinding, key is NONE (-1)
        if (card.isRebinding()) throw new AssertionError("Card should not start in rebinding mode");
        if (testModule.getKeybind().isBound()) throw new AssertionError("Keybind should start unbound");
        if (!"NONE".equalsIgnoreCase(testModule.getKeybind().getDisplayName())) {
            throw new AssertionError("Expected display name NONE, got: " + testModule.getKeybind().getDisplayName());
        }

        // Enter rebinding mode
        card.setRebinding(true);
        if (!card.isRebinding()) throw new AssertionError("Card failed to enter rebinding mode");

        // Rebind to GLFW_KEY_R (82)
        boolean handled = card.handleKey(GLFW.GLFW_KEY_R);
        if (!handled) throw new AssertionError("handleKey should consume key event during rebinding");
        if (card.isRebinding()) throw new AssertionError("Card should exit rebinding mode after key press");
        if (testModule.getKeybind().getValue() != GLFW.GLFW_KEY_R) {
            throw new AssertionError("Expected keycode " + GLFW.GLFW_KEY_R + ", got: " + testModule.getKeybind().getValue());
        }
        if (!"R".equalsIgnoreCase(testModule.getKeybind().getDisplayName())) {
            throw new AssertionError("Expected display name 'R', got: " + testModule.getKeybind().getDisplayName());
        }

        // Test Unbinding via ESC
        card.setRebinding(true);
        handled = card.handleKey(GLFW.GLFW_KEY_ESCAPE);
        if (!handled) throw new AssertionError("handleKey should consume ESC");
        if (card.isRebinding()) throw new AssertionError("Card should exit rebinding mode after ESC");
        if (testModule.getKeybind().isBound()) throw new AssertionError("Keybind should be unbound after ESC");
        if (testModule.getKeybind().getValue() != KeybindSetting.KEY_NONE) {
            throw new AssertionError("Expected KEY_NONE (-1), got: " + testModule.getKeybind().getValue());
        }

        // Test Unbinding via DELETE
        card.setRebinding(true);
        card.handleKey(GLFW.GLFW_KEY_F);
        if (testModule.getKeybind().getValue() != GLFW.GLFW_KEY_F) throw new AssertionError("Bind to F failed");

        card.setRebinding(true);
        handled = card.handleKey(GLFW.GLFW_KEY_DELETE);
        if (!handled) throw new AssertionError("handleKey should consume DELETE");
        if (testModule.getKeybind().isBound()) throw new AssertionError("Keybind should be unbound after DELETE");

        System.out.println("[Verification] Keybind rebinding state machine verified successfully.");
    }

    private static void verifySettingComponents() {
        // 1. Boolean / Toggle
        BooleanSetting bSet = new BooleanSetting("TestBool", "Test boolean setting", false);
        ToggleComponent toggle = new ToggleComponent(bSet);
        if (toggle.getHeight() != 16) throw new AssertionError("Unexpected toggle height");
        toggle.mouseClicked(10, 10, 0, 0, 0, 100);
        if (!bSet.getValue()) throw new AssertionError("Toggle click failed to invert value");
        toggle.mouseClicked(10, 10, 0, 0, 0, 100);
        if (bSet.getValue()) throw new AssertionError("Toggle second click failed to invert value");

        // 2. Number / Slider
        NumberSetting nSet = new NumberSetting("TestNum", "Test number setting", 5.0, 0.0, 10.0, 0.5);
        SliderComponent slider = new SliderComponent(nSet);
        if (slider.getHeight() != 22) throw new AssertionError("Unexpected slider height");
        // Simulate click at middle (x = 0, width = 100, mouseX = 50 -> track is [4..96], width=92, middle ratio ~0.5 -> ~5.0)
        slider.mouseClicked(50, 10, 0, 0, 0, 100);
        if (nSet.getValue() < 4.0 || nSet.getValue() > 6.0) {
            throw new AssertionError("Slider value out of expected middle range: " + nSet.getValue());
        }
        slider.mouseReleased(50, 10, 0);

        // 3. Mode / Dropdown
        ModeSetting mSet = new ModeSetting("TestMode", "Test mode setting", "ModeA", "ModeA", "ModeB", "ModeC");
        DropdownComponent dropdown = new DropdownComponent(mSet);
        if (dropdown.getHeight() != 18) throw new AssertionError("Unexpected dropdown height");
        dropdown.mouseClicked(10, 5, 0, 0, 0, 100); // Left click forward
        if (!"ModeB".equals(mSet.getValue())) throw new AssertionError("Dropdown forward cycle failed: " + mSet.getValue());
        dropdown.mouseClicked(10, 5, 1, 0, 0, 100); // Right click backward
        if (!"ModeA".equals(mSet.getValue())) throw new AssertionError("Dropdown backward cycle failed: " + mSet.getValue());

        // 4. Color / Picker
        ColorSetting cSet = new ColorSetting("TestColor", "Test color setting", 0xFFFF0000);
        ColorPickerComponent colorComp = new ColorPickerComponent(cSet);
        if (cSet.isChroma()) throw new AssertionError("Chroma should start false");
        // Click chroma button (x + width - 48 to x + width - 24, e.g. x = 0, width = 100 -> [52..76])
        colorComp.mouseClicked(60, 5, 0, 0, 0, 100);
        if (!cSet.isChroma()) throw new AssertionError("Chroma toggle failed");

        System.out.println("[Verification] Setting components logic verified successfully.");
    }

    private static void verifyConfigPersistence() {
        ClickGuiConfig.setPanelState("TestPanel", 123, 456, true);
        ClickGuiConfig.save();

        ClickGuiConfig.PanelState state = ClickGuiConfig.getPanelState("TestPanel");
        if (state == null) throw new AssertionError("Saved state was null");
        if (state.x != 123 || state.y != 456 || !state.collapsed) {
            throw new AssertionError("Saved state values mismatch: x=" + state.x + ", y=" + state.y + ", collapsed=" + state.collapsed);
        }

        // Reload from disk
        ClickGuiConfig.load();
        ClickGuiConfig.PanelState reloaded = ClickGuiConfig.getPanelState("TestPanel");
        if (reloaded == null || reloaded.x != 123 || reloaded.y != 456 || !reloaded.collapsed) {
            throw new AssertionError("Reloaded state values mismatch");
        }

        System.out.println("[Verification] ClickGuiConfig JSON persistence verified successfully.");
    }

    public static void main(String[] args) {
        runAllVerifications();
    }
}
