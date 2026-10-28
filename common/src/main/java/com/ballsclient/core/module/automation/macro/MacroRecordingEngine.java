package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * MacroRecordingEngine — Records real-time player keystrokes and mouse clicks into reproducible macro script files.
 */
public class MacroRecordingEngine extends Module {
    public record RecordedEvent(long timestampMs, String eventType, String target) {}

    private final BooleanSetting recordLookAngles = new BooleanSetting("RecordLookAngles", "Captures player pitch and yaw rotations", true);
    private final BooleanSetting recordKeypresses = new BooleanSetting("RecordKeypresses", "Captures keyboard input events", true);

    private final List<RecordedEvent> recordedEvents = new ArrayList<>();
    private boolean recording = false;

    public MacroRecordingEngine() {
        super("macro-recording-engine", "Macro Recording Engine", "Records real-time player keystrokes and mouse clicks into reproducible macro script files", Category.AUTOMATION);
        addSettings(recordLookAngles, recordKeypresses);
    }

    public BooleanSetting getRecordLookAngles() {
        return recordLookAngles;
    }

    public BooleanSetting getRecordKeypresses() {
        return recordKeypresses;
    }

    public synchronized void startRecording() {
        recordedEvents.clear();
        recording = true;
    }

    public synchronized void stopRecording() {
        recording = false;
    }

    public synchronized void recordEvent(long timestamp, String type, String target) {
        if (recording) {
            recordedEvents.add(new RecordedEvent(timestamp, type, target));
        }
    }

    public synchronized List<RecordedEvent> getRecordedEvents() {
        return new ArrayList<>(recordedEvents);
    }

    public synchronized boolean isRecording() {
        return recording;
    }
}
