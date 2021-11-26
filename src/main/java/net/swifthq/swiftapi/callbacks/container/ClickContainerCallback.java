package net.swifthq.swiftapi.callbacks.container;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ClickContainerCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<ClickContainerCallback> EVENT = EventFactory.createArrayBacked(ClickContainerCallback.class, (listeners) -> (player, packet) -> {
        for (ClickContainerCallback callback : listeners) {
            if (!callback.clickContainer(player, packet)) {
                return false;
            }
        }
        return true;
    });

    boolean clickContainer(ServerPlayerEntity player, ClickWindowC2SPacket packet);
}
