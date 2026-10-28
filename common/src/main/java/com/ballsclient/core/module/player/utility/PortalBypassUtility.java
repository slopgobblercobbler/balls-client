package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * PortalBypassUtility — Enables chat input and inventory navigation while standing inside portal blocks.
 */
public class PortalBypassUtility extends Module {
    private final BooleanSetting allowChat = new BooleanSetting("AllowChat", "Allow opening and typing in chat GUI inside portals", true);
    private final BooleanSetting allowInventory = new BooleanSetting("AllowInventory", "Allow opening inventory and crafting inside portals", true);

    public PortalBypassUtility() {
        super("portal-bypass-utility", "Portal Bypass Utility", "Enables chat input and inventory navigation while standing inside portal blocks", Category.PLAYER);
        addSettings(allowChat, allowInventory);
    }

    public BooleanSetting getAllowChat() {
        return allowChat;
    }

    public BooleanSetting getAllowInventory() {
        return allowInventory;
    }

    public static boolean shouldAllowGuiInPortal(boolean inPortal, boolean allowChat, boolean allowInventory) {
        if (!inPortal) return true;
        return allowChat || allowInventory;
    }
}
