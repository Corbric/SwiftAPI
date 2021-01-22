package net.swifthq.swiftapi.callbacks.lifecycle;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface ReloadCallback {

    Event<ReloadCallback> EVENT = EventFactory.createArrayBacked(ReloadCallback.class, (listeners) -> () -> {
        for (ReloadCallback callback : listeners) {
            callback.onReload();
        }
        return listeners.length;
    });

    int onReload();
}
