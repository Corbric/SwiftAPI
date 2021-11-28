package net.swifthq.swiftapi.callbacks.lifecycle;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface ReloadCallback {

	Event<ReloadCallback> EVENT = EventFactory.createArrayBacked(ReloadCallback.class, (listeners) -> () -> {
		boolean failed = false;
		for (ReloadCallback callback : listeners) {
			if (callback.onReload() != 0) {
				failed = true;
			}
		}
		if(failed) {
			return -1;
		}
		return listeners.length;
	});

	int onReload();
}
