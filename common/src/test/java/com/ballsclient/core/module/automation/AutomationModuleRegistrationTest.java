package com.ballsclient.core.module.automation;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.setting.Setting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Automation Suite Registration & Invariant Test")
class AutomationModuleRegistrationTest {

    @Test
    @DisplayName("Automation Suite: Exactly 70 concrete modules instantiate with unique IDs and Category.AUTOMATION")
    void testAllAutomationModulesInstantiate() {
        List<Module> allModules = AutomationModuleCatalog.createAllInstances();
        assertThat(allModules.size())
                .as("Automation module catalog must contain exactly 70 modules")
                .isEqualTo(70);

        Set<String> uniqueIds = new HashSet<>();
        Map<String, Integer> subpackageCounts = new HashMap<>();

        for (Module m : allModules) {
            assertThat(m.getId())
                    .as("Module ID cannot be blank")
                    .isNotBlank();
            assertThat(m.getId())
                    .as("Module ID must be lowercase kebab-case: %s", m.getId())
                    .isEqualTo(m.getId().toLowerCase(Locale.ROOT));
            assertThat(m.getId())
                    .as("Module ID must not contain spaces or underscores: %s", m.getId())
                    .doesNotContain(" ", "_");
            assertThat(m.getName())
                    .as("Module name cannot be blank for %s", m.getId())
                    .isNotBlank();
            assertThat(m.getDescription())
                    .as("Module description cannot be null for %s", m.getId())
                    .isNotNull();
            assertThat(m.getCategory())
                    .as("Module category must be Category.AUTOMATION for %s", m.getId())
                    .isEqualTo(Category.AUTOMATION);
            assertThat(m.getKeybind())
                    .as("Module keybind cannot be null for %s", m.getId())
                    .isNotNull();
            assertThat(m.getSettings())
                    .as("Module settings cannot be empty for %s", m.getId())
                    .isNotEmpty();

            Set<String> settingNames = new HashSet<>();
            for (Setting<?> s : m.getSettings()) {
                assertThat(s.getName()).isNotBlank();
                assertThat(settingNames.add(s.getName().toLowerCase(Locale.ROOT)))
                        .as("Duplicate setting '%s' in module '%s'", s.getName(), m.getId())
                        .isTrue();
            }

            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: %s", m.getId())
                    .isTrue();

            String pkg = m.getClass().getPackage().getName();
            String subpkg = pkg.substring(pkg.lastIndexOf('.') + 1);
            subpackageCounts.put(subpkg, subpackageCounts.getOrDefault(subpkg, 0) + 1);
        }

        // Verify symmetrical 14-module partition across 5 subpackages
        assertThat(subpackageCounts.get("schematic"))
                .as("schematic subpackage must contain exactly 14 modules")
                .isEqualTo(14);
        assertThat(subpackageCounts.get("macro"))
                .as("macro subpackage must contain exactly 14 modules")
                .isEqualTo(14);
        assertThat(subpackageCounts.get("building"))
                .as("building subpackage must contain exactly 14 modules")
                .isEqualTo(14);
        assertThat(subpackageCounts.get("farming"))
                .as("farming subpackage must contain exactly 14 modules")
                .isEqualTo(14);
        assertThat(subpackageCounts.get("redstone"))
                .as("redstone subpackage must contain exactly 14 modules")
                .isEqualTo(14);
    }

    @Test
    @DisplayName("Automation Suite: ModuleManager registers all 70 catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        AutomationModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isEqualTo(70);
        assertThat(manager.getModulesByCategory(Category.AUTOMATION).size()).isEqualTo(70);
        assertThat(AutomationModuleCatalog.getTotalModuleCount()).isEqualTo(70);
    }
}
