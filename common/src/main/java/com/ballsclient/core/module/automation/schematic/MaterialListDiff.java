package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.util.*;

/**
 * MaterialListDiff — Real-time bill-of-materials auditor comparing required blocks against player inventory.
 */
public class MaterialListDiff extends Module {
    public record MaterialRequirement(int required, int available, int missing, int surplus, int stacksNeeded, int shulkerBoxesNeeded) {}

    private final BooleanSetting includeShulkers = new BooleanSetting("IncludeShulkers", "Scans nested shulker boxes in inventory", true);
    private final ModeSetting sortBy = new ModeSetting("SortBy", "Sorting criteria for diff list", "MissingCount", "MissingCount", "Alphabetical", "StackCount");

    public MaterialListDiff() {
        super("material-list-diff", "Material List Diff", "Real-time bill-of-materials auditor comparing required blocks against player inventory", Category.AUTOMATION);
        addSettings(includeShulkers, sortBy);
    }

    public BooleanSetting getIncludeShulkers() {
        return includeShulkers;
    }

    public ModeSetting getSortBy() {
        return sortBy;
    }

    public static Map<String, MaterialRequirement> computeDiff(SchematicVolume volume, Map<String, Integer> inventoryItems) {
        if (volume == null) {
            return Collections.emptyMap();
        }
        Map<String, Integer> required = volume.calculateMaterialList();
        Map<String, Integer> inventory = inventoryItems != null ? inventoryItems : Collections.emptyMap();
        Set<String> allKeys = new HashSet<>(required.keySet());
        allKeys.addAll(inventory.keySet());

        Map<String, MaterialRequirement> result = new HashMap<>();
        for (String id : allKeys) {
            int req = required.getOrDefault(id, 0);
            int avail = inventory.getOrDefault(id, 0);
            int missing = Math.max(0, req - avail);
            int surplus = Math.max(0, avail - req);
            int stacks = (int) Math.ceil(req / 64.0);
            int shulkers = (int) Math.ceil(stacks / 27.0);
            result.put(id, new MaterialRequirement(req, avail, missing, surplus, stacks, shulkers));
        }
        return Collections.unmodifiableMap(result);
    }
}
