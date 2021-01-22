package net.swifthq.swiftapi.callbacks.lifecycle;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.ServerPlayerEntity;
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
