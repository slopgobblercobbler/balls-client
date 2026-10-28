package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * AtomicShiftClickSequencer — Sequences container transfer clicks in strict dependency order to prevent ghost drops.
 */
public class AtomicShiftClickSequencer extends Module {
    private final BooleanSetting verifyServerAck = new BooleanSetting("VerifyServerAck", "Wait for server transaction confirmation before advancing", true);

    public AtomicShiftClickSequencer() {
        super("atomic-shift-click-sequencer", "Atomic Shift-Click Sequencer", "Sequences container transfer clicks in strict dependency order", Category.INVENTORY);
        addSettings(verifyServerAck);
    }

    public BooleanSetting getVerifyServerAck() {
        return verifyServerAck;
    }

    public static Queue<Integer> createTransferQueue(List<Integer> slots) {
        Queue<Integer> queue = new ArrayDeque<>();
        if (slots != null) {
            queue.addAll(slots);
        }
        return queue;
    }
}
