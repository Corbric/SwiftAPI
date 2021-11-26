package net.swifthq.swiftapi.callbacks.world;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

public interface PreWorldReadyCallback {

    Event<PreWorldReadyCallback> EVENT = EventFactory.createArrayBacked(PreWorldReadyCallback.class, (listeners) -> () -> {
        for (PreWorldReadyCallback callback : listeners) {
            callback.worldReady();
        }
    });

    void worldReady();
}
