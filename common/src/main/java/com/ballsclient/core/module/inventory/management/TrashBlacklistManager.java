package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Set;

/**
 * TrashBlacklistManager — Manages disposable item filters with wildcard matching and auto-drop coordination.
 */
public class TrashBlacklistManager extends Module {
    private final BooleanSetting autoPurge = new BooleanSetting("AutoPurge", "Automatically discard blacklisted items upon pickup", false);

    public TrashBlacklistManager() {
        super("trash-blacklist-manager", "Trash Blacklist Manager", "Manages disposable item filters and auto-drop coordination", Category.INVENTORY);
        addSettings(autoPurge);
    }

    public BooleanSetting getAutoPurge() {
        return autoPurge;
    }

    public static boolean isBlacklisted(String itemId, Set<String> blacklistedIds) {
        if (itemId == null || blacklistedIds == null) return false;
        String id = itemId.toLowerCase();
        for (String b : blacklistedIds) {
            if (b != null && id.contains(b.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
