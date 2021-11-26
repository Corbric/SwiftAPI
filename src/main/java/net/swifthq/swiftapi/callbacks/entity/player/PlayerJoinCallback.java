package net.swifthq.swiftapi.callbacks.entity.player;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerJoinCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class, (listeners) -> (player) -> {
        for (PlayerJoinCallback callback : listeners) {
            callback.playerJoin(player);
        }
    });

    void playerJoin(ServerPlayerEntity player);
}
