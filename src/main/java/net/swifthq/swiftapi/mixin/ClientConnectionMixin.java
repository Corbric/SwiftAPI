package net.swifthq.swiftapi.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.swifthq.swiftapi.network.PacketEncoderException;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin extends SimpleChannelInboundHandler<Packet<?>> {

	@Shadow @Final private static Logger LOGGER;

	@Shadow public abstract void disconnect(Text disconnectReason);

	@Shadow public abstract void disableAutoRead();

	@Shadow private Channel channel;

	@Shadow public abstract void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, GenericFutureListener<? extends Future<? super Void>>... genericFutureListeners);

	private boolean errored;

	@Override
	public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable exception) {
		if (exception instanceof PacketEncoderException) {
			LOGGER.debug("Skipping packet due to errors", exception.getCause());
		} else {
			boolean firstError = !this.errored;
			this.errored = true;
			if (this.channel.isOpen()) {
				if (exception instanceof TimeoutException) {
					LOGGER.debug("Timeout", exception);
					this.disconnect(new TranslatableText("disconnect.timeout"));
				} else {
					Text text = new TranslatableText("disconnect.genericReason", "Internal Server Exception: " + exception);
					if (firstError) {
						LOGGER.error("Failed to send packet", exception);
						this.send(new DisconnectS2CPacket(text), (future) -> this.disconnect(text));
						this.disableAutoRead();
					} else {
						LOGGER.error("Double fault", exception);
						this.disconnect(text);
					}
				}
			}
		}
	}
}
