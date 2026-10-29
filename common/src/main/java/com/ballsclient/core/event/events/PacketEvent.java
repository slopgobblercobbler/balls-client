package com.ballsclient.core.event.events;

/**
 * Adapter/alias for PacketEvent under events subpackage.
 */
public class PacketEvent extends com.ballsclient.core.event.PacketEvent {
    public PacketEvent(Object packet) {
        super(packet);
    }

    public static class Receive extends com.ballsclient.core.event.PacketEvent.Receive {
        public Receive(Object packet) {
            super(packet);
        }
    }

    public static class Send extends com.ballsclient.core.event.PacketEvent.Send {
        public Send(Object packet) {
            super(packet);
        }
    }
}
