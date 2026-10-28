package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

import java.util.Set;

/**
 * FilterBasedAutoPick — Whitelist/blacklist filter permitting pickup of desired items while ignoring junk.
 */
public class FilterBasedAutoPick extends Module {
    private final ModeSetting filterMode = new ModeSetting("FilterMode", "Filter evaluation mode", "Whitelist", "Whitelist", "Blacklist");

    public FilterBasedAutoPick() {
        super("filter-based-auto-pick", "Filter Auto Pick", "Permits pickup of desired items while ignoring junk", Category.INVENTORY);
        addSettings(filterMode);
    }

    public ModeSetting getFilterMode() {
        return filterMode;
    }

    public static boolean canPickup(String itemId, Set<String> filterSet, boolean isWhitelist) {
        if (itemId == null) return false;
        if (filterSet == null || filterSet.isEmpty()) {
            return !isWhitelist; // If whitelist empty, pick nothing; if blacklist empty, pick everything
        }
        boolean contains = filterSet.contains(itemId.toLowerCase());
        return isWhitelist ? contains : !contains;
    }
}
