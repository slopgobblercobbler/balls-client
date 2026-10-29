package com.ballsclient.fabric263.mixin;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.event.PacketEvent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onChannelRead0(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        try {
            if (BallsClientCore.getEventBus() != null && packet != null) {
                PacketEvent.Receive event = new PacketEvent.Receive(packet);
                BallsClientCore.getEventBus().post(event);
                if (event.isCancelled()) {
                    ci.cancel();
                }
            }
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error dispatching PacketEvent.Receive in ConnectionMixin: " + t.getMessage(), t);
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
        try {
            if (BallsClientCore.getEventBus() != null && packet != null) {
                PacketEvent.Send event = new PacketEvent.Send(packet);
                BallsClientCore.getEventBus().post(event);
                if (event.isCancelled()) {
                    ci.cancel();
                }
            }
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error dispatching PacketEvent.Send in ConnectionMixin: " + t.getMessage(), t);
        }
    }
}
