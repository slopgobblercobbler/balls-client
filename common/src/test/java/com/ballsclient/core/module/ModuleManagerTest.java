package com.ballsclient.core.module;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.event.KeyEvent;
import com.ballsclient.core.module.render.hud.CoordinatesHUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ModuleManager Core Registry & Dispatch Test Suite")
class ModuleManagerTest {
    private EventBus eventBus;
    private ModuleManager manager;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
        manager = new ModuleManager(eventBus);
        manager.registerDefaultModules();
    }

    @Test
    @DisplayName("Registry: 105+ Render modules successfully registered into ModuleManager")
    void testDefaultModuleRegistrationCount() {
        Collection<Module> allModules = manager.getModules();
        assertThat(allModules.size())
                .as("Total registered modules must be at least 105 per requirements")
                .isGreaterThanOrEqualTo(105);

        List<Module> renderModules = manager.getModulesByCategory(Category.RENDER);
        assertThat(renderModules.size())
                .as("Category.RENDER modules must be at least 105")
                .isGreaterThanOrEqualTo(105);
    }

    @Test
    @DisplayName("Registry: Zero duplicate IDs, all IDs lowercase, valid names and RENDER category")
    void testNoDuplicateModuleIds() {
        Set<String> uniqueIds = new HashSet<>();
        for (Module m : manager.getModules()) {
            assertThat(m.getId())
                    .as("Module ID cannot be blank")
                    .isNotBlank();
            assertThat(m.getId())
                    .as("Module ID must be lowercase")
                    .isEqualTo(m.getId().toLowerCase());
            assertThat(m.getName())
                    .as("Module name cannot be blank")
                    .isNotBlank();
            assertThat(m.getCategory())
                    .as("Module category must be RENDER for Render suite")
                    .isEqualTo(Category.RENDER);
            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: %s", m.getId())
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Lookup: Module retrieval by ID (case-insensitive) and by class token")
    void testModuleLookupByIdAndClass() {
        Module coordsById = manager.getModule("coordinates-hud");
        assertThat(coordsById).isNotNull();
        assertThat(coordsById).isInstanceOf(CoordinatesHUD.class);

        // Case-insensitive ID lookup
        Module coordsUpper = manager.getModule("COORDINATES-HUD");
        assertThat(coordsUpper).isSameAs(coordsById);

        // Class token lookup
        CoordinatesHUD coordsByClass = manager.getModule(CoordinatesHUD.class);
        assertThat(coordsByClass).isSameAs(coordsById);

        // Non-existent module
        assertThat(manager.getModule("non-existent-module-id")).isNull();
    }

    @Test
    @DisplayName("Category Query: Filtering returns correct categories and empty list on empty domain")
    void testCategoryFiltering() {
        List<Module> renderList = manager.getModulesByCategory(Category.RENDER);
        assertThat(renderList).isNotEmpty();

        List<Module> combatList = manager.getModulesByCategory(Category.COMBAT);
        assertThat(combatList).isEmpty();

        List<Module> nullCategory = manager.getModulesByCategory(null);
        assertThat(nullCategory).isEmpty();
    }

    @Test
    @DisplayName("Keybinds: Key press triggers module toggle, key release does not")
    void testKeybindDispatching() {
        CoordinatesHUD coords = manager.getModule(CoordinatesHUD.class);
        assertThat(coords).isNotNull();
        assertThat(coords.isEnabled()).isFalse();

        // Bind coordinates-hud to GLFW keycode 88 (X)
        coords.getKeybind().setValue(88);

        // Action PRESS: Key 88 pressed -> should toggle to enabled
        eventBus.post(new KeyEvent(88, 0, KeyEvent.ACTION_PRESS, 0));
        assertThat(coords.isEnabled()).isTrue();

        // Action RELEASE: Key 88 released -> should remain enabled
        eventBus.post(new KeyEvent(88, 0, KeyEvent.ACTION_RELEASE, 0));
        assertThat(coords.isEnabled()).isTrue();

        // Action PRESS again -> should toggle back to disabled
        eventBus.post(new KeyEvent(88, 0, KeyEvent.ACTION_PRESS, 0));
        assertThat(coords.isEnabled()).isFalse();

        // Action PRESS with unrelated key 89 (Y) -> should not affect coordinates
        eventBus.post(new KeyEvent(89, 0, KeyEvent.ACTION_PRESS, 0));
        assertThat(coords.isEnabled()).isFalse();
    }
}
