package net.swifthq.swiftapi.callbacks.world;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

public interface PostWorldReadyCallback {

    Event<PostWorldReadyCallback> EVENT = EventFactory.createArrayBacked(PostWorldReadyCallback.class, (listeners) -> () -> {
        for (PostWorldReadyCallback callback : listeners) {
            callback.worldReady();
        }
    });

    void worldReady();
}
