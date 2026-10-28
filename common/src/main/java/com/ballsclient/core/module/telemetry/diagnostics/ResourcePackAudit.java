package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ResourcePackAudit — Validates server-pushed resource pack URLs, SHA-1 checksum integrity,
 * and file sizes to prevent malicious pack exploits.
 */
public class ResourcePackAudit extends Module {
    private final BooleanSetting verifySha1 = new BooleanSetting("VerifySha1", "Verifies downloaded resource pack SHA-1 hash against server header", true);
    private final NumberSetting maxSizeMb = new NumberSetting("MaxSizeMb", "Maximum permitted server resource pack size in megabytes", 100.0, 10.0, 500.0, 10.0);
    private final BooleanSetting blockPromptUrlMismatch = new BooleanSetting("BlockPromptUrlMismatch", "Blocks packs with deceptive or non-matching prompt URLs", true);

    public ResourcePackAudit() {
        super("resource-pack-audit", "Resource Pack Audit", "Validates server-pushed resource pack URLs, SHA-1 checksum integrity, and file sizes to prevent malicious pack exploits.", Category.NETWORK);
        addSettings(verifySha1, maxSizeMb, blockPromptUrlMismatch);
    }

    public BooleanSetting getVerifySha1() {
        return verifySha1;
    }

    public NumberSetting getMaxSizeMb() {
        return maxSizeMb;
    }

    public BooleanSetting getBlockPromptUrlMismatch() {
        return blockPromptUrlMismatch;
    }
}
