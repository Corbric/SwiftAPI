package net.swifthq.swiftapi.callbacks.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldReadyCallback {

    Event<WorldReadyCallback> EVENT = EventFactory.createArrayBacked(WorldReadyCallback.class, (listeners) -> () -> {
        for (WorldReadyCallback callback : listeners) {
            callback.worldReady();
        }
    });

    void worldReady();
}
