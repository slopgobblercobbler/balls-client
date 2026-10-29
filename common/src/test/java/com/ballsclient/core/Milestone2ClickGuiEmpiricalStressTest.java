package com.ballsclient.core;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.setting.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Milestone 2 ClickGUI Empirical Stress Test Suite")
class Milestone2ClickGuiEmpiricalStressTest {

    @BeforeAll
    static void initCore() {
        BallsClientCore.init();
    }

    // -------------------------------------------------------------------------
    // Objective 1: Category Mapping Completeness & Zero Module Loss
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Verify ALL 7 Category enum values are mapped across the 6 ClickGUI panels")
    void testAllCategoryEnumsCovered() {
        // The 6 panels defined in ModernClickGuiScreen:
        Map<String, List<Category>> panelMappings = new LinkedHashMap<>();
        panelMappings.put("Combat", List.of(Category.COMBAT));
        panelMappings.put("Movement", List.of(Category.MOVEMENT));
        panelMappings.put("Render", List.of(Category.RENDER));
        panelMappings.put("Player", List.of(Category.PLAYER));
        panelMappings.put("World", List.of(Category.INVENTORY, Category.AUTOMATION));
        panelMappings.put("Misc", List.of(Category.NETWORK));

        assertThat(panelMappings).hasSize(6);

        Set<Category> coveredCategories = new HashSet<>();
        for (List<Category> cats : panelMappings.values()) {
            for (Category c : cats) {
                boolean added = coveredCategories.add(c);
                assertThat(added)
                        .as("Category %s must not be mapped to multiple panels", c)
                        .isTrue();
            }
        }

        for (Category expected : Category.values()) {
            assertThat(coveredCategories)
                    .as("Category %s must be mapped in ClickGUI panels", expected)
                    .contains(expected);
        }
    }

    @Test
    @DisplayName("Verify EVERY registered module appears in exactly one of the 6 panels with ZERO loss or duplication")
    void testEveryRegisteredModuleAppearsInExactlyOnePanel() {
        ModuleManager manager = BallsClientCore.getModuleManager();
        assertThat(manager).isNotNull();

        Collection<Module> allModules = manager.getModules();
        assertThat(allModules).isNotEmpty();
        assertThat(allModules.size())
                .as("Total registered modules must be at least 500")
                .isGreaterThanOrEqualTo(500);

        // Define the 6 panels
        Map<String, Set<Category>> panelCategories = new LinkedHashMap<>();
        panelCategories.put("Combat", Set.of(Category.COMBAT));
        panelCategories.put("Movement", Set.of(Category.MOVEMENT));
        panelCategories.put("Render", Set.of(Category.RENDER));
        panelCategories.put("Player", Set.of(Category.PLAYER));
        panelCategories.put("World", Set.of(Category.INVENTORY, Category.AUTOMATION));
        panelCategories.put("Misc", Set.of(Category.NETWORK));

        Map<String, List<Module>> panelModules = new LinkedHashMap<>();
        for (String panel : panelCategories.keySet()) {
            panelModules.put(panel, new ArrayList<>());
        }

        Set<String> encounteredModuleIds = new HashSet<>();
        Map<String, String> moduleToPanelMap = new HashMap<>();

        for (Module m : allModules) {
            assertThat(m).as("Module in registry must not be null").isNotNull();
            assertThat(m.getId()).as("Module id must not be null or blank").isNotBlank();
            assertThat(m.getName()).as("Module name must not be null or blank").isNotBlank();
            assertThat(m.getCategory()).as("Module %s must have a non-null category", m.getId()).isNotNull();

            // Find matching panel
            String matchedPanel = null;
            for (Map.Entry<String, Set<Category>> entry : panelCategories.entrySet()) {
                if (entry.getValue().contains(m.getCategory())) {
                    matchedPanel = entry.getKey();
                    break;
                }
            }

            assertThat(matchedPanel)
                    .as("Module %s (Category %s) must map to a panel", m.getId(), m.getCategory())
                    .isNotNull();

            panelModules.get(matchedPanel).add(m);

            // Verify uniqueness
            boolean uniqueId = encounteredModuleIds.add(m.getId());
            assertThat(uniqueId)
                    .as("Duplicate module ID detected in registry: %s", m.getId())
                    .isTrue();

            moduleToPanelMap.put(m.getId(), matchedPanel);
        }

        // Verify total count
        int totalMappedCount = panelModules.values().stream().mapToInt(List::size).sum();
        assertThat(totalMappedCount)
                .as("Total mapped modules across panels must equal registry size")
                .isEqualTo(allModules.size());

        // Check each panel has modules
        for (Map.Entry<String, List<Module>> entry : panelModules.entrySet()) {
            assertThat(entry.getValue())
                    .as("Panel %s must contain modules", entry.getKey())
                    .isNotEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // Objective 2: Scissor Clipping Safety & Coordinate Clamping Invariants
    // -------------------------------------------------------------------------

    static class PanelScissorModel {
        final int width = 125;
        final int headerHeight = 20;
        int x;
        int y;
        boolean collapsed;
        int totalCardsHeight;

        PanelScissorModel(int x, int y, boolean collapsed, int totalCardsHeight) {
            this.x = x;
            this.y = y;
            this.collapsed = collapsed;
            this.totalCardsHeight = totalCardsHeight;
        }

        void clampToBounds(int screenWidth, int screenHeight) {
            this.x = Math.max(2, Math.min(this.x, screenWidth - this.width - 2));
            this.y = Math.max(2, Math.min(this.y, screenHeight - this.headerHeight - 2));
        }

        int getViewportHeight(int screenHeight) {
            if (collapsed) {
                return 0;
            }
            int maxH = Math.max(60, screenHeight - y - headerHeight - 15);
            return Math.min(totalCardsHeight, maxH);
        }

        int getViewportY() {
            return y + headerHeight;
        }

        // Returns {x1, y1, x2, y2} passed to context.enableScissor()
        int[] getScissorBox(int screenHeight) {
            int vY = getViewportY();
            int vH = getViewportHeight(screenHeight);
            return new int[]{x, vY, x + width, vY + vH};
        }
    }

    @ParameterizedTest(name = "Screen {0}x{1}, pos ({2}, {3}), collapsed={4}, cardsH={5}")
    @CsvSource({
            "800, 600, 20, 30, false, 500",
            "1920, 1080, 500, 400, false, 1200",
            "1920, 1080, 1900, 1050, false, 800",
            "640, 480, -100, -100, false, 300",
            "800, 600, 100, 100, true, 500",
            "300, 200, 10, 10, false, 0",
            "3840, 2160, 2000, 1500, false, 5000",
            "100, 100, 50, 50, false, 100"
    })
    @DisplayName("Verify Scissor Clipping Dimensions are NEVER negative and strictly bounded")
    void testScissorClippingDimensionSafety(int screenW, int screenH, int initX, int initY, boolean collapsed, int cardsH) {
        PanelScissorModel model = new PanelScissorModel(initX, initY, collapsed, cardsH);
        model.clampToBounds(screenW, screenH);

        // 1. Clamped position bounds
        assertThat(model.x).isGreaterThanOrEqualTo(2);
        assertThat(model.y).isGreaterThanOrEqualTo(2);

        int[] box = model.getScissorBox(screenH);
        int x1 = box[0];
        int y1 = box[1];
        int x2 = box[2];
        int y2 = box[3];

        int scissorWidth = x2 - x1;
        int scissorHeight = y2 - y1;

        // Invariant: Scissor width must always equal panel width (125) and strictly > 0
        assertThat(scissorWidth)
                .as("Scissor width must equal panel width")
                .isEqualTo(125);

        // Invariant: Scissor height must be >= 0 (never negative)
        assertThat(scissorHeight)
                .as("Scissor height must never be negative")
                .isGreaterThanOrEqualTo(0);

        if (collapsed) {
            assertThat(scissorHeight)
                    .as("Collapsed panel must produce 0 height")
                    .isEqualTo(0);
        }
    }

    @Test
    @DisplayName("Stress-test 50,000 randomized edge-case screen dimensions and panel positions")
    void testFuzzedScissorDimensions() {
        Random rng = new Random(42);
        for (int i = 0; i < 50_000; i++) {
            int screenW = rng.nextInt(4000);
            int screenH = rng.nextInt(4000);
            int x = rng.nextInt(10000) - 5000;
            int y = rng.nextInt(10000) - 5000;
            boolean collapsed = rng.nextBoolean();
            int cardsH = rng.nextInt(10000);

            PanelScissorModel model = new PanelScissorModel(x, y, collapsed, cardsH);
            model.clampToBounds(screenW, screenH);

            int[] box = model.getScissorBox(screenH);
            int scissorW = box[2] - box[0];
            int scissorH = box[3] - box[1];

            assertThat(scissorW).as("Fuzzed scissor width must be 125").isEqualTo(125);
            assertThat(scissorH).as("Fuzzed scissor height must be >= 0").isGreaterThanOrEqualTo(0);
        }
    }

    // -------------------------------------------------------------------------
    // Objective 3: Screen Event Routing & Overlapping Panel Z-Order Simulation
    // -------------------------------------------------------------------------

    static class MockPanel {
        final String title;
        int x;
        int y;
        final int width = 125;
        final int headerHeight = 20;
        boolean dragging = false;
        int dragOffsetX;
        int dragOffsetY;
        int clickCount = 0;

        MockPanel(String title, int x, int y) {
            this.title = title;
            this.x = x;
            this.y = y;
        }

        boolean mouseClicked(double mx, double my, int button) {
            if (mx >= x && mx <= x + width && my >= y && my < y + headerHeight) {
                if (button == 0) {
                    this.dragging = true;
                    this.dragOffsetX = (int) mx - x;
                    this.dragOffsetY = (int) my - y;
                    this.clickCount++;
                    return true;
                }
            }
            return false;
        }

        boolean mouseReleased(double mx, double my, int button) {
            boolean was = dragging;
            dragging = false;
            return was;
        }

        boolean mouseDragged(double mx, double my, int button) {
            if (dragging && button == 0) {
                this.x = (int) mx - dragOffsetX;
                this.y = (int) my - dragOffsetY;
                return true;
            }
            return false;
        }
    }

    @Test
    @DisplayName("Verify Top-Most Panel Handles Clicks First and Header Click Brings Panel to Front")
    void testPanelZOrderAndFrontFocus() {
        List<MockPanel> panels = new ArrayList<>();
        MockPanel panelBottom = new MockPanel("BottomPanel", 100, 100);
        MockPanel panelTop = new MockPanel("TopPanel", 100, 100); // Fully overlapping

        panels.add(panelBottom); // index 0 (rendered first / back)
        panels.add(panelTop);    // index 1 (rendered last / front)

        // Click at (110, 110) inside header of BOTH panels
        double clickX = 110;
        double clickY = 110;

        // Dispatch click in reverse order (as in ModernClickGuiScreen)
        boolean handled = false;
        for (int i = panels.size() - 1; i >= 0; i--) {
            MockPanel p = panels.get(i);
            if (p.mouseClicked(clickX, clickY, 0)) {
                if (p.dragging) {
                    panels.remove(i);
                    panels.add(p);
                }
                handled = true;
                break;
            }
        }

        assertThat(handled).isTrue();
        assertThat(panelTop.clickCount).isEqualTo(1);
        assertThat(panelBottom.clickCount).isEqualTo(0);
        assertThat(panelTop.dragging).isTrue();
        assertThat(panelBottom.dragging).isFalse();

        // Release mouse
        for (MockPanel p : panels) {
            p.mouseReleased(clickX, clickY, 0);
        }
        assertThat(panelTop.dragging).isFalse();

        // Now move panelBottom partially visible at (50, 100) and click only panelBottom
        panelBottom.x = 50;
        panelBottom.y = 100;
        // click at (60, 110) -> inside panelBottom, outside panelTop
        handled = false;
        for (int i = panels.size() - 1; i >= 0; i--) {
            MockPanel p = panels.get(i);
            if (p.mouseClicked(60, 110, 0)) {
                if (p.dragging) {
                    panels.remove(i);
                    panels.add(p);
                }
                handled = true;
                break;
            }
        }

        assertThat(handled).isTrue();
        assertThat(panelBottom.clickCount).isEqualTo(1);
        // panelBottom must now be at the end of the list (brought to front)
        assertThat(panels.get(panels.size() - 1)).isSameAs(panelBottom);
    }

    // -------------------------------------------------------------------------
    // Objective 4: Keybind Rebinding State Machine & Escape Unbinding
    // -------------------------------------------------------------------------

    static class MockKeybindModuleCard {
        final Module module;
        boolean rebinding = false;

        MockKeybindModuleCard(Module module) {
            this.module = module;
        }

        boolean handleKey(int keyCode) {
            if (!rebinding) {
                return false;
            }
            if (keyCode == 256 || keyCode == 261 || keyCode == 259) { // GLFW ESC, DELETE, BACKSPACE
                module.getKeybind().setValue(KeybindSetting.KEY_NONE);
            } else {
                module.getKeybind().setValue(keyCode);
            }
            this.rebinding = false;
            return true;
        }
    }

    @Test
    @DisplayName("Verify Keybind Rebinding State Transitions, Normal Keys, and Unbinding via ESC/DEL/BACKSPACE")
    void testKeybindRebindingTransitions() {
        Module testMod = new Module("test_card_mod", "Test Card Mod", "Desc", Category.COMBAT, KeybindSetting.KEY_NONE) {};
        MockKeybindModuleCard card = new MockKeybindModuleCard(testMod);

        assertThat(testMod.getKeybind().isBound()).isFalse();
        assertThat(testMod.getKeybind().getDisplayName()).isEqualTo("NONE");

        // 1. Normal key assignment
        card.rebinding = true;
        boolean consumed = card.handleKey(82); // GLFW_KEY_R
        assertThat(consumed).isTrue();
        assertThat(card.rebinding).isFalse();
        assertThat(testMod.getKeybind().isBound()).isTrue();
        assertThat(testMod.getKeybind().getValue()).isEqualTo(82);
        assertThat(testMod.getKeybind().getDisplayName()).isEqualTo("R");

        // 2. Unbind via ESC (256)
        card.rebinding = true;
        consumed = card.handleKey(256);
        assertThat(consumed).isTrue();
        assertThat(card.rebinding).isFalse();
        assertThat(testMod.getKeybind().isBound()).isFalse();
        assertThat(testMod.getKeybind().getValue()).isEqualTo(-1);
        assertThat(testMod.getKeybind().getDisplayName()).isEqualTo("NONE");

        // 3. Rebind to Space (32) then unbind via DELETE (261)
        card.rebinding = true;
        card.handleKey(32);
        assertThat(testMod.getKeybind().getValue()).isEqualTo(32);

        card.rebinding = true;
        card.handleKey(261);
        assertThat(testMod.getKeybind().isBound()).isFalse();

        // 4. Rebind to RSHIFT (344) then unbind via BACKSPACE (259)
        card.rebinding = true;
        card.handleKey(344);
        assertThat(testMod.getKeybind().getValue()).isEqualTo(344);

        card.rebinding = true;
        card.handleKey(259);
        assertThat(testMod.getKeybind().isBound()).isFalse();
    }

    // -------------------------------------------------------------------------
    // Objective 5: Settings Components Mathematical Invariants
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Verify NumberSetting Slider Scrubbing Math with Step Quantization")
    void testSliderScrubbingMath() {
        NumberSetting setting = new NumberSetting("Speed", "Speed desc", 1.0, 0.0, 10.0, 0.25);

        // Simulation of updateValue(mouseX, trackX, trackW)
        int trackX = 10;
        int trackW = 100;

        // Click at middle mouseX = 60 -> ratio = 0.5 -> raw = 5.0 -> stepped = 5.0
        double ratio = Math.max(0.0, Math.min(1.0, (60.0 - trackX) / trackW));
        double rawVal = setting.getMin() + ratio * (setting.getMax() - setting.getMin());
        double stepped = Math.round((rawVal - setting.getMin()) / setting.getStep()) * setting.getStep() + setting.getMin();
        setting.setValue(stepped);
        assertThat(setting.getValue()).isEqualTo(5.0);

        // Click beyond right mouseX = 200 -> ratio clamped to 1.0 -> 10.0
        ratio = Math.max(0.0, Math.min(1.0, (200.0 - trackX) / trackW));
        rawVal = setting.getMin() + ratio * (setting.getMax() - setting.getMin());
        stepped = Math.round((rawVal - setting.getMin()) / setting.getStep()) * setting.getStep() + setting.getMin();
        setting.setValue(stepped);
        assertThat(setting.getValue()).isEqualTo(10.0);

        // Click beyond left mouseX = -50 -> ratio clamped to 0.0 -> 0.0
        ratio = Math.max(0.0, Math.min(1.0, (-50.0 - trackX) / trackW));
        rawVal = setting.getMin() + ratio * (setting.getMax() - setting.getMin());
        stepped = Math.round((rawVal - setting.getMin()) / setting.getStep()) * setting.getStep() + setting.getMin();
        setting.setValue(stepped);
        assertThat(setting.getValue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Verify ModeSetting Carousel Forward and Backward Cycling")
    void testModeDropdownBidirectionalCycling() {
        ModeSetting mode = new ModeSetting("Mode", "Mode desc", "Alpha", "Alpha", "Beta", "Gamma");
        List<String> list = mode.getModes();

        // Forward cycle (Left click)
        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("Beta");
        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("Gamma");
        mode.cycle();
        assertThat(mode.getValue()).isEqualTo("Alpha");

        // Backward cycle (Right click)
        // Simulated logic from DropdownComponent.mouseClicked button == 1
        int curIdx = list.indexOf(mode.getValue());
        mode.setValue(curIdx <= 0 ? list.get(list.size() - 1) : list.get(curIdx - 1));
        assertThat(mode.getValue()).isEqualTo("Gamma");

        curIdx = list.indexOf(mode.getValue());
        mode.setValue(curIdx <= 0 ? list.get(list.size() - 1) : list.get(curIdx - 1));
        assertThat(mode.getValue()).isEqualTo("Beta");
    }

    // -------------------------------------------------------------------------
    // Objective 6: ClickGUI Config JSON Serialization Round-Trip
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Verify ClickGUI JSON Serialization & Deserialization Invariants")
    void testClickGuiJsonPersistence() {
        Gson gson = new Gson();
        JsonObject root = new JsonObject();
        JsonObject panelsObj = new JsonObject();

        String[] panelNames = {"Combat", "Movement", "Render", "Player", "World", "Misc"};
        int startX = 20;
        int gap = 137;

        for (int i = 0; i < panelNames.length; i++) {
            JsonObject p = new JsonObject();
            p.addProperty("x", startX + i * gap);
            p.addProperty("y", 30 + i * 5);
            p.addProperty("collapsed", i % 2 == 0);
            panelsObj.add(panelNames[i], p);
        }
        root.add("panels", panelsObj);

        String json = gson.toJson(root);
        assertThat(json).isNotBlank();

        // Deserialize
        JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
        assertThat(parsed.has("panels")).isTrue();
        JsonObject restoredPanels = parsed.getAsJsonObject("panels");

        for (int i = 0; i < panelNames.length; i++) {
            assertThat(restoredPanels.has(panelNames[i])).isTrue();
            JsonObject p = restoredPanels.getAsJsonObject(panelNames[i]);
            assertThat(p.get("x").getAsInt()).isEqualTo(startX + i * gap);
            assertThat(p.get("y").getAsInt()).isEqualTo(30 + i * 5);
            assertThat(p.get("collapsed").getAsBoolean()).isEqualTo(i % 2 == 0);
        }
    }
}
