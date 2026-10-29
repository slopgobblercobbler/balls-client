package com.ballsclient.core.event;

/**
 * Event dispatched when network packets are sent or received.
 */
public class PacketEvent extends CancellableEvent {
    private final Object packet;

    public PacketEvent(Object packet) {
        this.packet = packet;
    }

    public Object getPacket() {
        return packet;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPacket(Class<T> clazz) {
        if (clazz != null && clazz.isInstance(packet)) {
            return (T) packet;
        }
        return null;
    }

    /**
     * Inbound packet received from server.
     */
    public static class Receive extends PacketEvent {
        public Receive(Object packet) {
            super(packet);
        }
    }

    /**
     * Outbound packet being sent to server.
     */
    public static class Send extends PacketEvent {
        public Send(Object packet) {
            super(packet);
        }
    }
}
