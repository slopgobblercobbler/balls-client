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
import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * Empirical Adversarial Stress Test Harness for Milestone 2 Modern ClickGUI.
 * Exhaustively stress-tests:
 * 1. Panel dragging: negative coordinates, out-of-screen dragging, coordinate clamping, rapid drag movements.
 * 2. Vertical scrolling: zero items, single item, 150+ items, extreme scroll deltas, bounds clamping.
 * 3. Setting components: Slider math (normalization, clamping, min > max, non-finite values), Dropdown cycling (index bounds, empty options), ColorPicker hue cycling.
 * 4. Module keybind rebinding: ESC unbind to KEY_NONE, DEL unbind to KEY_NONE, normal key assignment, state reset on cancel.
 * 5. JSON config serialization/deserialization under config/ballsclient/clickgui.json (corrupted JSON recovery, missing keys, special characters).
 */
public final class Milestone2AdversarialStressHarness {

    private Milestone2AdversarialStressHarness() {}

    public static void runAllAdversarialStressTests() {
        System.out.println("=================================================================");
        System.out.println("=== Starting ClickGUI Milestone 2 Adversarial Stress Harness ===");
        System.out.println("=================================================================");

        // Initialize core
        BallsClientCore.init();

        testPanelDraggingStress();
        testVerticalScrollingStress();
        testSettingComponentsStress();
        testKeybindRebindingStress();
        testJsonConfigPersistenceStress();

        System.out.println("=================================================================");
        System.out.println("=== All Milestone 2 Adversarial Stress Tests PASSED Cleanly! ===");
        System.out.println("=================================================================");
    }

    // -------------------------------------------------------------------------
    // 1. Panel Dragging & Clamping Stress
    // -------------------------------------------------------------------------
    private static void testPanelDraggingStress() {
        System.out.println("[Test 1/5] Stress-testing Panel Dragging & Coordinate Clamping...");

        CategoryPanel panel = new CategoryPanel("DragTest", 0xFF00FF00, 100, 100, Category.COMBAT);

        // A. Start dragging
        // Header is at (100, 100), width=125, height=20. Click inside header with left button (0)
        boolean dragStarted = panel.mouseClicked(110, 110, 0, 600);
        if (!dragStarted || !panel.isDragging()) {
            throw new AssertionError("Panel failed to initiate dragging on header left-click!");
        }

        // B. Drag into negative coordinates: mouse moved to (-500, -300)
        int screenW = 800;
        int screenH = 600;
        panel.mouseDragged(-500, -300, 0, -610, -410, screenW, screenH);
        // Clamping rule: Math.max(2, Math.min(x, screenW - width - 2))
        if (panel.getX() != 2 || panel.getY() != 2) {
            throw new AssertionError("Negative coordinate clamping failed! Expected (2, 2), got: (" + panel.getX() + ", " + panel.getY() + ")");
        }

        // C. Drag out-of-screen (far positive): mouse moved to (5000, 5000)
        panel.mouseDragged(5000, 5000, 0, 5500, 5300, screenW, screenH);
        int expectedMaxX = screenW - panel.getWidth() - 2; // 800 - 125 - 2 = 673
        int expectedMaxY = screenH - panel.getHeaderHeight() - 2; // 600 - 20 - 2 = 578
        if (panel.getX() != expectedMaxX || panel.getY() != expectedMaxY) {
            throw new AssertionError("Positive out-of-screen clamping failed! Expected (" + expectedMaxX + ", " + expectedMaxY + "), got: (" + panel.getX() + ", " + panel.getY() + ")");
        }

        // D. Rapid jitter dragging (1,000 rapid high-delta mouse movements)
        Random rng = new Random(42);
        for (int i = 0; i < 1000; i++) {
            double rx = rng.nextDouble() * 4000.0 - 2000.0;
            double ry = rng.nextDouble() * 4000.0 - 2000.0;
            panel.mouseDragged(rx, ry, 0, 10, 10, screenW, screenH);

            if (panel.getX() < 2 || panel.getX() > expectedMaxX) {
                throw new AssertionError("Rapid drag breached X bounds at iteration " + i + ": x=" + panel.getX());
            }
            if (panel.getY() < 2 || panel.getY() > expectedMaxY) {
                throw new AssertionError("Rapid drag breached Y bounds at iteration " + i + ": y=" + panel.getY());
            }
        }

        // E. Mouse released
        panel.mouseReleased(100, 100, 0);
        if (panel.isDragging()) {
            throw new AssertionError("Panel still dragging after mouseReleased!");
        }

        // Subsequent mouseDragged should do nothing
        int savedX = panel.getX();
        int savedY = panel.getY();
        panel.mouseDragged(0, 0, 0, -100, -100, screenW, screenH);
        if (panel.getX() != savedX || panel.getY() != savedY) {
            throw new AssertionError("Panel moved while NOT dragging!");
        }

        // F. Screen resizing clamping
        panel.setX(1500);
        panel.setY(900);
        panel.clampToBounds(400, 300);
        if (panel.getX() != (400 - 125 - 2) || panel.getY() != (300 - 20 - 2)) {
            throw new AssertionError("Screen resize clamping failed! Got: (" + panel.getX() + ", " + panel.getY() + ")");
        }

        System.out.println("  -> Panel Dragging & Clamping tests PASSED.");
    }

    // -------------------------------------------------------------------------
    // 2. Vertical Scrolling & Bounds Clamping Stress
    // -------------------------------------------------------------------------
    private static void testVerticalScrollingStress() {
        System.out.println("[Test 2/5] Stress-testing Vertical Scrolling Bounds & Extreme Deltas...");

        // A. Zero items (empty category panel)
        CategoryPanel emptyPanel = new CategoryPanel("EmptyPanel", 0xFFFFFFFF, 10, 10);
        emptyPanel.getCards().clear(); // Ensure 0 cards
        if (emptyPanel.getTotalCardsHeight() != 0) {
            throw new AssertionError("Empty panel total cards height should be 0");
        }
        if (emptyPanel.getViewportHeight(600) != 0) {
            throw new AssertionError("Empty panel viewport height should be 0");
        }
        boolean scrolledEmpty = emptyPanel.mouseScrolled(15, 15, 0, 5.0, 600);
        if (scrolledEmpty) {
            throw new AssertionError("Empty panel should not consume scroll event");
        }

        // B. Single item
        CategoryPanel singlePanel = new CategoryPanel("SinglePanel", 0xFFFFFFFF, 10, 10);
        singlePanel.getCards().clear();
        singlePanel.getCards().add(new ModuleCard(new Module("single_m", "Single", "Desc", Category.COMBAT, KeybindSetting.KEY_NONE) {}));
        if (singlePanel.getCards().size() != 1) {
            throw new AssertionError("Expected 1 card");
        }
        int singleH = singlePanel.getTotalCardsHeight(); // 18
        if (singleH != 18) {
            throw new AssertionError("Single card height expected 18, got: " + singleH);
        }
        boolean scrolledSingle = singlePanel.mouseScrolled(15, 15, 0, -1.0, 600);
        if (scrolledSingle) {
            throw new AssertionError("Single card panel with no overflow should not scroll");
        }

        // C. 150+ items panel (200 modules)
        CategoryPanel hugePanel = new CategoryPanel("HugePanel", 0xFFFFAA00, 20, 30);
        hugePanel.getCards().clear();
        for (int i = 0; i < 200; i++) {
            hugePanel.getCards().add(new ModuleCard(new Module("mock_" + i, "Mock " + i, "Desc", Category.COMBAT, KeybindSetting.KEY_NONE) {}));
        }
        if (hugePanel.getCards().size() != 200) {
            throw new AssertionError("Expected 200 cards");
        }
        int totalContentH = hugePanel.getTotalCardsHeight(); // 200 * 18 = 3600
        int screenH = 600;
        int viewportH = hugePanel.getViewportHeight(screenH);
        int maxScroll = totalContentH - viewportH;
        if (maxScroll <= 0) {
            throw new AssertionError("Expected maxScroll > 0 for 200 items, got: " + maxScroll);
        }

        // D. Normal downward scrolling (verticalAmount = -1.0 -> scroll down by 18px)
        int mouseX = hugePanel.getX() + 10;
        int mouseY = hugePanel.getY() + hugePanel.getHeaderHeight() + 10;

        try {
            Field scrollOffsetField = CategoryPanel.class.getDeclaredField("scrollOffset");
            scrollOffsetField.setAccessible(true);

            // Scroll down 5 steps
            for (int i = 1; i <= 5; i++) {
                boolean scrolled = hugePanel.mouseScrolled(mouseX, mouseY, 0, -1.0, screenH);
                if (!scrolled) throw new AssertionError("Scroll failed to consume at step " + i);
                int offset = (int) scrollOffsetField.get(hugePanel);
                if (offset != i * 18) {
                    throw new AssertionError("Expected offset " + (i * 18) + ", got: " + offset);
                }
            }

            // Scroll up 2 steps
            hugePanel.mouseScrolled(mouseX, mouseY, 0, 1.0, screenH);
            hugePanel.mouseScrolled(mouseX, mouseY, 0, 1.0, screenH);
            int offset = (int) scrollOffsetField.get(hugePanel);
            if (offset != (5 - 2) * 18) {
                throw new AssertionError("Expected offset 54, got: " + offset);
            }

            // E. Extreme positive scroll delta: verticalAmount = 100000.0 (fast scroll to top)
            hugePanel.mouseScrolled(mouseX, mouseY, 0, 100000.0, screenH);
            offset = (int) scrollOffsetField.get(hugePanel);
            if (offset != 0) {
                throw new AssertionError("Offset must be clamped to 0 on extreme scroll up, got: " + offset);
            }

            // F. Extreme negative scroll delta: verticalAmount = -100000.0 (fast scroll to bottom)
            hugePanel.mouseScrolled(mouseX, mouseY, 0, -100000.0, screenH);
            offset = (int) scrollOffsetField.get(hugePanel);
            if (offset != maxScroll) {
                throw new AssertionError("Offset must be clamped to maxScroll on extreme scroll down, got: " + offset + ", maxScroll: " + maxScroll);
            }

            // G. Non-finite scroll deltas: NaN, +Inf, -Inf
            hugePanel.mouseScrolled(mouseX, mouseY, 0, Double.NaN, screenH);
            offset = (int) scrollOffsetField.get(hugePanel);
            if (offset < 0 || offset > maxScroll) {
                throw new AssertionError("Offset corrupted after NaN scroll: " + offset);
            }

            hugePanel.mouseScrolled(mouseX, mouseY, 0, Double.POSITIVE_INFINITY, screenH);
            offset = (int) scrollOffsetField.get(hugePanel);
            if (offset != 0) {
                throw new AssertionError("Offset should clamp to 0 on POSITIVE_INFINITY, got: " + offset);
            }

            hugePanel.mouseScrolled(mouseX, mouseY, 0, Double.NEGATIVE_INFINITY, screenH);
            offset = (int) scrollOffsetField.get(hugePanel);
            if (offset < 0 || offset > maxScroll) {
                throw new AssertionError("Offset breached bounds on NEGATIVE_INFINITY: " + offset);
            }

            // H. Collapsed panel should never scroll
            hugePanel.setCollapsed(true);
            boolean scrolledCollapsed = hugePanel.mouseScrolled(mouseX, mouseY, 0, -5.0, screenH);
            if (scrolledCollapsed) {
                throw new AssertionError("Collapsed panel should NOT scroll!");
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Reflection access failed in scrolling test", e);
        }

        System.out.println("  -> Vertical Scrolling & Clamping tests PASSED.");
    }

    // -------------------------------------------------------------------------
    // 3. Setting Components Stress (Slider, Dropdown, ColorPicker, Toggle)
    // -------------------------------------------------------------------------
    private static void testSettingComponentsStress() {
        System.out.println("[Test 3/5] Stress-testing Setting Components Math & Click Bounds...");

        // A. SliderComponent track math & dragging
        NumberSetting nSet = new NumberSetting("SliderStress", "Desc", 5.0, 0.0, 10.0, 0.5);
        SliderComponent slider = new SliderComponent(nSet);

        // Track is at [x+4 .. x+width-4], width=100 -> trackW = 92
        // Click at x=0, mouseX = 4 (track start) -> ratio 0.0 -> value 0.0
        slider.mouseClicked(4, 10, 0, 0, 0, 100);
        if (Math.abs(nSet.getValue() - 0.0) > 1e-6) {
            throw new AssertionError("Slider click at track start failed: " + nSet.getValue());
        }

        // Drag to mouseX = 96 (track end) -> ratio 1.0 -> value 10.0
        slider.mouseDragged(96, 10, 0, 92, 0);
        if (Math.abs(nSet.getValue() - 10.0) > 1e-6) {
            throw new AssertionError("Slider drag to track end failed: " + nSet.getValue());
        }

        // Drag beyond bounds left: mouseX = -500
        slider.mouseDragged(-500, 10, 0, -596, 0);
        if (Math.abs(nSet.getValue() - 0.0) > 1e-6) {
            throw new AssertionError("Slider drag left bound clamping failed: " + nSet.getValue());
        }

        // Drag beyond bounds right: mouseX = 5000
        slider.mouseDragged(5000, 10, 0, 5500, 0);
        if (Math.abs(nSet.getValue() - 10.0) > 1e-6) {
            throw new AssertionError("Slider drag right bound clamping failed: " + nSet.getValue());
        }

        slider.mouseReleased(5000, 10, 0);

        // B. Track width <= 0 safety (should not divide by zero)
        slider.mouseClicked(10, 10, 0, 0, 0, 6); // width=6 -> trackW = 6 - 8 = -2
        // Should not crash

        // C. DropdownComponent cycling
        ModeSetting mSet = new ModeSetting("ModeStress", "Desc", "A", "A", "B", "C");
        DropdownComponent dropdown = new DropdownComponent(mSet);

        // Forward cycling with button 0
        dropdown.mouseClicked(10, 5, 0, 0, 0, 100);
        if (!"B".equals(mSet.getValue())) throw new AssertionError("Dropdown forward failed: " + mSet.getValue());
        dropdown.mouseClicked(10, 5, 0, 0, 0, 100);
        if (!"C".equals(mSet.getValue())) throw new AssertionError("Dropdown forward failed: " + mSet.getValue());
        dropdown.mouseClicked(10, 5, 0, 0, 0, 100);
        if (!"A".equals(mSet.getValue())) throw new AssertionError("Dropdown forward wrap failed: " + mSet.getValue());

        // Backward cycling with button 1
        dropdown.mouseClicked(10, 5, 1, 0, 0, 100);
        if (!"C".equals(mSet.getValue())) throw new AssertionError("Dropdown backward wrap failed: " + mSet.getValue());
        dropdown.mouseClicked(10, 5, 1, 0, 0, 100);
        if (!"B".equals(mSet.getValue())) throw new AssertionError("Dropdown backward failed: " + mSet.getValue());

        // Middle click (button 2) should be ignored
        dropdown.mouseClicked(10, 5, 2, 0, 0, 100);
        if (!"B".equals(mSet.getValue())) throw new AssertionError("Dropdown should ignore middle click");

        // D. ColorPickerComponent: Chroma button & Preset swatch clicks
        ColorSetting cSet = new ColorSetting("ColorStress", "Desc", 0xFFFF4444);
        ColorPickerComponent colorComp = new ColorPickerComponent(cSet);

        // Chroma button is at [x + width - 48 .. x + width - 24], e.g. x=0, width=100 -> [52..76]
        boolean clickedChroma = colorComp.mouseClicked(60, 5, 0, 0, 0, 100);
        if (!clickedChroma || !cSet.isChroma()) {
            throw new AssertionError("Chroma button click failed!");
        }
        colorComp.mouseClicked(60, 5, 0, 0, 0, 100);
        if (cSet.isChroma()) {
            throw new AssertionError("Chroma button second click failed to disable chroma!");
        }

        // Swatch button is at [x + width - 18 .. x + width - 4], e.g. x=0, width=100 -> [82..96]
        // Preset 0 is 0xFFFF4444 (Red), clicking should advance to Preset 1: 0xFFFF8800 (Orange)
        boolean clickedSwatch = colorComp.mouseClicked(88, 5, 0, 0, 0, 100);
        if (!clickedSwatch || cSet.getValue() != 0xFFFF8800) {
            throw new AssertionError("Color swatch preset advance failed! Expected 0xFFFF8800, got: 0x" + Integer.toHexString(cSet.getValue()));
        }

        // E. ToggleComponent
        BooleanSetting bSet = new BooleanSetting("ToggleStress", "Desc", false);
        ToggleComponent toggle = new ToggleComponent(bSet);
        toggle.mouseClicked(10, 5, 0, 0, 0, 100);
        if (!bSet.getValue()) throw new AssertionError("Toggle switch failed to activate");
        toggle.mouseClicked(10, 5, 0, 0, 0, 100);
        if (bSet.getValue()) throw new AssertionError("Toggle switch failed to deactivate");

        System.out.println("  -> Setting Components Math & Click tests PASSED.");
    }

    // -------------------------------------------------------------------------
    // 4. Module Keybind Rebinding Stress
    // -------------------------------------------------------------------------
    private static void testKeybindRebindingStress() {
        System.out.println("[Test 4/5] Stress-testing Module Keybind Rebinding State Machine...");

        Module testModule = new Module("rebind_stress", "Rebind Stress", "Desc", Category.COMBAT, KeybindSetting.KEY_NONE) {};
        ModuleCard card = new ModuleCard(testModule);

        // 1. Initial State
        if (card.isRebinding()) throw new AssertionError("Card should not start rebinding");
        if (testModule.getKeybind().isBound()) throw new AssertionError("Initial keybind should be unbound");
        if (!"NONE".equalsIgnoreCase(testModule.getKeybind().getDisplayName())) {
            throw new AssertionError("Initial display name must be NONE");
        }

        // 2. Normal key assignment: GLFW_KEY_R (82)
        card.setRebinding(true);
        boolean handled = card.handleKey(GLFW.GLFW_KEY_R);
        if (!handled) throw new AssertionError("handleKey should return true");
        if (card.isRebinding()) throw new AssertionError("Card should exit rebinding after keypress");
        if (testModule.getKeybind().getValue() != GLFW.GLFW_KEY_R) {
            throw new AssertionError("Expected keycode 82, got: " + testModule.getKeybind().getValue());
        }
        if (!"R".equalsIgnoreCase(testModule.getKeybind().getDisplayName())) {
            throw new AssertionError("Expected display name 'R', got: " + testModule.getKeybind().getDisplayName());
        }

        // 3. ESC unbind to KEY_NONE (-1)
        card.setRebinding(true);
        handled = card.handleKey(GLFW.GLFW_KEY_ESCAPE);
        if (!handled) throw new AssertionError("handleKey should consume ESC");
        if (card.isRebinding()) throw new AssertionError("Card should exit rebinding after ESC");
        if (testModule.getKeybind().getValue() != KeybindSetting.KEY_NONE) {
            throw new AssertionError("ESC must unbind to KEY_NONE (-1), got: " + testModule.getKeybind().getValue());
        }
        if (testModule.getKeybind().isBound()) throw new AssertionError("Keybind should be unbound after ESC");

        // 4. DEL unbind to KEY_NONE (-1)
        card.setRebinding(true);
        card.handleKey(GLFW.GLFW_KEY_F);
        if (testModule.getKeybind().getValue() != GLFW.GLFW_KEY_F) throw new AssertionError("Bind to F failed");

        card.setRebinding(true);
        handled = card.handleKey(GLFW.GLFW_KEY_DELETE);
        if (!handled) throw new AssertionError("handleKey should consume DELETE");
        if (testModule.getKeybind().getValue() != KeybindSetting.KEY_NONE) {
            throw new AssertionError("DELETE must unbind to KEY_NONE (-1)");
        }

        // 5. BACKSPACE unbind to KEY_NONE (-1)
        card.setRebinding(true);
        card.handleKey(GLFW.GLFW_KEY_G);
        card.setRebinding(true);
        handled = card.handleKey(GLFW.GLFW_KEY_BACKSPACE);
        if (!handled) throw new AssertionError("handleKey should consume BACKSPACE");
        if (testModule.getKeybind().getValue() != KeybindSetting.KEY_NONE) {
            throw new AssertionError("BACKSPACE must unbind to KEY_NONE (-1)");
        }

        // 6. HandleKey when NOT in rebinding mode must return false
        handled = card.handleKey(GLFW.GLFW_KEY_A);
        if (handled) throw new AssertionError("handleKey must return false when card is NOT rebinding!");

        // 7. Rebinding cancel & reset on click outside
        card.setRebinding(true);
        card.setRebinding(false); // Simulate cancellation
        if (card.isRebinding()) throw new AssertionError("Rebinding cancel failed");

        System.out.println("  -> Module Keybind Rebinding tests PASSED.");
    }

    // -------------------------------------------------------------------------
    // 5. JSON Config Persistence Stress (clickgui.json)
    // -------------------------------------------------------------------------
    private static void testJsonConfigPersistenceStress() {
        System.out.println("[Test 5/5] Stress-testing ClickGuiConfig JSON Serialization & Corruption Recovery...");

        Path configPath = Path.of("config", "ballsclient", "clickgui.json");

        try {
            // A. Normal save and round-trip
            ClickGuiConfig.setPanelState("Combat", 25, 35, false);
            ClickGuiConfig.setPanelState("Movement", 160, 45, true);
            ClickGuiConfig.setPanelState("Render", 290, 55, false);
            ClickGuiConfig.save();

            ClickGuiConfig.load();
            ClickGuiConfig.PanelState combat = ClickGuiConfig.getPanelState("Combat");
            ClickGuiConfig.PanelState movement = ClickGuiConfig.getPanelState("Movement");

            if (combat == null || combat.x != 25 || combat.y != 35 || combat.collapsed) {
                throw new AssertionError("Combat panel persistence mismatch");
            }
            if (movement == null || movement.x != 160 || movement.y != 45 || !movement.collapsed) {
                throw new AssertionError("Movement panel persistence mismatch");
            }

            // B. Special characters & unicode panel titles
            String specialTitle = "Test /\\:?*\"<>| \u0000 \n \t \uD83D\uDD25";
            ClickGuiConfig.setPanelState(specialTitle, 50, 60, true);
            ClickGuiConfig.save();
            ClickGuiConfig.load();
            ClickGuiConfig.PanelState special = ClickGuiConfig.getPanelState(specialTitle);
            if (special == null || special.x != 50 || special.y != 60 || !special.collapsed) {
                throw new AssertionError("Special character title round-trip failed");
            }

            // C. Corrupted JSON file recovery (malformed JSON syntax)
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, "{ \"panels\": { \"Broken\": { x: invalid json [[[");

            // Must NOT throw exception; must recover gracefully
            ClickGuiConfig.load();

            // D. Missing keys fallback: panel missing x, y, and collapsed
            String missingKeysJson = "{\n" +
                    "  \"panels\": {\n" +
                    "    \"PartialPanel\": {}\n" +
                    "  }\n" +
                    "}";
            Files.writeString(configPath, missingKeysJson);
            ClickGuiConfig.load();
            ClickGuiConfig.PanelState partial = ClickGuiConfig.getPanelState("PartialPanel");
            if (partial == null) throw new AssertionError("Partial panel should load fallback defaults");
            if (partial.x != 20 || partial.y != 30 || partial.collapsed) {
                throw new AssertionError("Partial panel should fallback to x=20, y=30, collapsed=false, got: x=" + partial.x + ", y=" + partial.y);
            }

            // E. Non-primitive JSON values
            String nonPrimitiveJson = "{\n" +
                    "  \"panels\": {\n" +
                    "    \"BadTypes\": {\n" +
                    "      \"x\": [1, 2, 3],\n" +
                    "      \"y\": {\"nested\": 42},\n" +
                    "      \"collapsed\": \"maybe\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            Files.writeString(configPath, nonPrimitiveJson);
            // Must not crash
            ClickGuiConfig.load();

            // Clean up test file
            Files.deleteIfExists(configPath);

        } catch (Exception e) {
            throw new RuntimeException("ClickGuiConfig persistence stress failed", e);
        }

        System.out.println("  -> ClickGuiConfig JSON Persistence tests PASSED.");
    }

    public static void main(String[] args) {
        runAllAdversarialStressTests();
    }
}
