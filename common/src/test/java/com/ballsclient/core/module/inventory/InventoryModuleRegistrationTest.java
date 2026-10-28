package com.ballsclient.core.module.inventory;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.setting.Setting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Inventory Suite Registration & Invariant Test")
class InventoryModuleRegistrationTest {

    @Test
    @DisplayName("Inventory Suite: Exactly 90 concrete modules instantiate with unique IDs and Category.INVENTORY")
    void testAllInventoryModulesInstantiate() {
        List<Module> allInventoryModules = InventoryModuleCatalog.createAllInstances();
        assertThat(allInventoryModules.size())
                .as("Inventory module catalog must contain exactly 90 modules")
                .isEqualTo(90);

        Set<String> uniqueIds = new HashSet<>();
        Map<String, Integer> subpackageCounts = new HashMap<>();

        for (Module m : allInventoryModules) {
            assertThat(m.getId())
                    .as("Module ID cannot be blank")
                    .isNotBlank();
            assertThat(m.getId())
                    .as("Module ID must be lowercase kebab-case: %s", m.getId())
                    .isEqualTo(m.getId().toLowerCase());
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
                    .as("Module category must be Category.INVENTORY for %s", m.getId())
                    .isEqualTo(Category.INVENTORY);
            assertThat(m.getKeybind())
                    .as("Module keybind cannot be null for %s", m.getId())
                    .isNotNull();
            assertThat(m.getSettings())
                    .as("Module settings cannot be empty for %s", m.getId())
                    .isNotEmpty();

            // Verify unique setting names within module
            Set<String> settingNames = new HashSet<>();
            for (Setting<?> s : m.getSettings()) {
                assertThat(s.getName()).isNotBlank();
                assertThat(settingNames.add(s.getName().toLowerCase()))
                        .as("Duplicate setting '%s' in module '%s'", s.getName(), m.getId())
                        .isTrue();
            }

            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: %s", m.getId())
                    .isTrue();

            // Tally subpackages
            String pkg = m.getClass().getPackage().getName();
            String subpkg = pkg.substring(pkg.lastIndexOf('.') + 1);
            subpackageCounts.put(subpkg, subpackageCounts.getOrDefault(subpkg, 0) + 1);
        }

        // Verify symmetrical 18-module partition
        assertThat(subpackageCounts.get("sorting"))
                .as("sorting subpackage must contain exactly 18 modules")
                .isEqualTo(18);
        assertThat(subpackageCounts.get("container"))
                .as("container subpackage must contain exactly 18 modules")
                .isEqualTo(18);
        assertThat(subpackageCounts.get("transfer"))
                .as("transfer subpackage must contain exactly 18 modules")
                .isEqualTo(18);
        assertThat(subpackageCounts.get("vacuum"))
                .as("vacuum subpackage must contain exactly 18 modules")
                .isEqualTo(18);
        assertThat(subpackageCounts.get("management"))
                .as("management subpackage must contain exactly 18 modules")
                .isEqualTo(18);
    }

    @Test
    @DisplayName("Inventory Suite: ModuleManager registers all 90 catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        InventoryModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isEqualTo(90);
        assertThat(manager.getModulesByCategory(Category.INVENTORY).size()).isEqualTo(90);
        assertThat(InventoryModuleCatalog.getTotalModuleCount()).isEqualTo(90);
    }
}
