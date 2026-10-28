package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.ModeSetting;

/**
 * SchematicRotateFlip — Mathematical 90/180/270-degree coordinate yaw rotation and horizontal/vertical mirroring.
 */
public class SchematicRotateFlip extends Module {
    private final ModeSetting rotation = new ModeSetting("Rotation", "Coordinate yaw rotation", "NONE", "NONE", "CLOCKWISE_90", "CLOCKWISE_180", "COUNTERCLOCKWISE_90");
    private final ModeSetting mirror = new ModeSetting("Mirror", "Coordinate mirror reflection", "NONE", "NONE", "FRONT_BACK", "LEFT_RIGHT");

    public SchematicRotateFlip() {
        super("schematic-rotate-flip", "Schematic Rotate & Flip", "Mathematical 90/180/270-degree coordinate yaw rotation and horizontal/vertical mirroring", Category.AUTOMATION);
        addSettings(rotation, mirror);
    }

    public ModeSetting getRotation() {
        return rotation;
    }

    public ModeSetting getMirror() {
        return mirror;
    }

    public static SchematicVolume applyTransform(SchematicVolume volume, String rot, String mirr) {
        if (volume == null) return null;
        SchematicVolume transformed = volume;

        if ("CLOCKWISE_90".equalsIgnoreCase(rot)) {
            transformed = transformed.rotate90();
        } else if ("CLOCKWISE_180".equalsIgnoreCase(rot)) {
            transformed = transformed.rotate180();
        } else if ("COUNTERCLOCKWISE_90".equalsIgnoreCase(rot)) {
            transformed = transformed.rotate270();
        }

        if ("FRONT_BACK".equalsIgnoreCase(mirr)) {
            transformed = transformed.mirror(false, true);
        } else if ("LEFT_RIGHT".equalsIgnoreCase(mirr)) {
            transformed = transformed.mirror(true, false);
        }

        return transformed;
    }
}
