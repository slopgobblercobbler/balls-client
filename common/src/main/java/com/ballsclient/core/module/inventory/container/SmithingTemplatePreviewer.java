package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SmithingTemplatePreviewer — Validates netherite upgrade templates and armor trim combinations and costs.
 */
public class SmithingTemplatePreviewer extends Module {
    private final BooleanSetting validateRecipe = new BooleanSetting("ValidateRecipe", "Validate smithing template compatibility", true);

    public SmithingTemplatePreviewer() {
        super("smithing-template-previewer", "Smithing Previewer", "Validates netherite upgrade templates and armor trim combinations", Category.INVENTORY);
        addSettings(validateRecipe);
    }

    public BooleanSetting getValidateRecipe() {
        return validateRecipe;
    }

    public static boolean isValidUpgrade(String templateId, String baseId, String additionId) {
        if (templateId == null || baseId == null || additionId == null) return false;
        String t = templateId.toLowerCase();
        String b = baseId.toLowerCase();
        String a = additionId.toLowerCase();

        if (t.contains("netherite_upgrade_smithing_template")) {
            return b.contains("diamond") && a.contains("netherite_ingot");
        }
        if (t.contains("armor_trim_smithing_template")) {
            boolean isArmor = b.contains("helmet") || b.contains("chestplate") || b.contains("leggings") || b.contains("boots");
            boolean isMaterial = a.contains("ingot") || a.contains("diamond") || a.contains("emerald") || a.contains("quartz") || a.contains("amethyst");
            return isArmor && isMaterial;
        }
        return false;
    }
}
