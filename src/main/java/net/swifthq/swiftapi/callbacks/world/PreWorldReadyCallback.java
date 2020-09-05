package net.swifthq.swiftapi.callbacks.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PreWorldReadyCallback {

    Event<PreWorldReadyCallback> EVENT = EventFactory.createArrayBacked(PreWorldReadyCallback.class, (listeners) -> () -> {
        for (PreWorldReadyCallback callback : listeners) {
            callback.worldReady();
        }
    });

    void worldReady();
}
