package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AutoReplyChime — Scans incoming chat whispers for triggers and executes automated reply messages.
 */
public class AutoReplyChime extends Module {
    private static final Pattern WHISPER_PATTERN = Pattern.compile("^([A-Za-z0-9_]+) whispers to you: (.*)$");

    private final NumberSetting cooldownSeconds = new NumberSetting("CooldownSeconds", "Per-player auto-reply rate limit cooldown", 10.0, 1.0, 60.0, 5.0);
    private final BooleanSetting chimeSound = new BooleanSetting("ChimeSound", "Plays alert chime sound on incoming whisper", true);

    private final Map<String, Long> lastReplyTimes = new HashMap<>();

    public AutoReplyChime() {
        super("auto-reply-chime", "Auto Reply Chime", "Scans incoming chat whispers for regex triggers and executes automated reply messages", Category.AUTOMATION);
        addSettings(cooldownSeconds, chimeSound);
    }

    public NumberSetting getCooldownSeconds() {
        return cooldownSeconds;
    }

    public BooleanSetting getChimeSound() {
        return chimeSound;
    }

    public boolean shouldReply(String chatMessage, long currentTime) {
        if (chatMessage == null) return false;
        Matcher m = WHISPER_PATTERN.matcher(chatMessage.trim());
        if (!m.matches()) {
            return false;
        }
        String sender = m.group(1);
        long lastTime = lastReplyTimes.getOrDefault(sender, 0L);
        long cooldownMs = (long) (cooldownSeconds.getValue() * 1000.0);
        if (currentTime - lastTime >= cooldownMs) {
            lastReplyTimes.put(sender, currentTime);
            return true;
        }
        return false;
    }
}
