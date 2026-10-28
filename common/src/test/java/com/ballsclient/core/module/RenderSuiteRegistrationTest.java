package com.ballsclient.core.module;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.render.RenderModuleCatalog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Render Suite Complete Catalog Verification Test")
class RenderSuiteRegistrationTest {

    @Test
    @DisplayName("Render Suite: All 105+ concrete modules instantiate, have unique IDs, and valid settings")
    void testAllRenderModulesInstantiate() {
        List<Module> allRenderModules = RenderModuleCatalog.createAllInstances();
        assertThat(allRenderModules.size())
                .as("Render module catalog must contain at least 105 modules")
                .isGreaterThanOrEqualTo(105);

        Set<String> uniqueIds = new HashSet<>();
        for (Module m : allRenderModules) {
            assertThat(m.getId()).isNotBlank();
            assertThat(m.getName()).isNotBlank();
            assertThat(m.getDescription()).isNotNull();
            assertThat(m.getCategory()).isEqualTo(Category.RENDER);
            assertThat(m.getKeybind()).isNotNull();
            assertThat(m.getSettings()).isNotEmpty(); // Must at least have Keybind
            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: " + m.getId())
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Render Suite: ModuleManager registers all catalog modules and query by category works")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        RenderModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isGreaterThanOrEqualTo(105);
        assertThat(manager.getModulesByCategory(Category.RENDER).size()).isGreaterThanOrEqualTo(105);
    }
}
