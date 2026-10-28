package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DropKeySpamProtector — Protects equipped weapons and rare tools against accidental Q drop key spam.
 */
public class DropKeySpamProtector extends Module {
    private final BooleanSetting protectEnchanted = new BooleanSetting("ProtectEnchanted", "Require double-tap to drop enchanted gear", true);

    public DropKeySpamProtector() {
        super("drop-key-spam-protector", "Drop Key Spam Protector", "Protects equipped gear against accidental Q drop spam", Category.INVENTORY);
        addSettings(protectEnchanted);
    }

    public BooleanSetting getProtectEnchanted() {
        return protectEnchanted;
    }
}
