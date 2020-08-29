package net.swifthq.swiftapi.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.dimension.DimensionRegistry;

public interface DimensionRegisterCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<DimensionRegisterCallback> EVENT = EventFactory.createArrayBacked(DimensionRegisterCallback.class, (listeners) -> (dimensionRegistry) -> {
        for (DimensionRegisterCallback callback : listeners) {
            callback.registerDimension(dimensionRegistry);
        }
    });

    void registerDimension(DimensionRegistry dimensionRegistry);
}
