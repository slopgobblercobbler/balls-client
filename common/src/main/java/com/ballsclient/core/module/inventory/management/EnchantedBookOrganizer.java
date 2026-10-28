package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * EnchantedBookOrganizer — Groups and categorizes enchanted books by enchantment utility category.
 */
public class EnchantedBookOrganizer extends Module {
    private final BooleanSetting groupByArmor = new BooleanSetting("GroupByArmor", "Group armor protection enchantments together", true);

    public EnchantedBookOrganizer() {
        super("enchanted-book-organizer", "Enchanted Book Organizer", "Groups and categorizes enchanted books by utility", Category.INVENTORY);
        addSettings(groupByArmor);
    }

    public BooleanSetting getGroupByArmor() {
        return groupByArmor;
    }
}
