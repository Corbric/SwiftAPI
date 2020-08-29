package net.swifthq.swiftapi.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;

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
