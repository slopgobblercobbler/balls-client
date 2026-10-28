package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * LoomPatternPreviewCache — Headlessly previews 6-layer banner pattern compositions and dye applications.
 */
public class LoomPatternPreviewCache extends Module {
    private final NumberSetting maxPreviewLayers = new NumberSetting("MaxPreviewLayers", "Maximum previewable banner pattern layers", 6.0, 1.0, 6.0, 1.0);

    public LoomPatternPreviewCache() {
        super("loom-pattern-preview-cache", "Loom Pattern Preview", "Headlessly previews 6-layer banner pattern compositions", Category.INVENTORY);
        addSettings(maxPreviewLayers);
    }

    public NumberSetting getMaxPreviewLayers() {
        return maxPreviewLayers;
    }

    public static String generateBannerKey(String baseColor, List<String> patterns, List<String> colors) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseColor != null ? baseColor : "white");
        if (patterns != null && colors != null) {
            int len = Math.min(patterns.size(), colors.size());
            for (int i = 0; i < len; i++) {
                sb.append("/").append(patterns.get(i)).append(":").append(colors.get(i));
            }
        }
        return sb.toString();
    }
}
