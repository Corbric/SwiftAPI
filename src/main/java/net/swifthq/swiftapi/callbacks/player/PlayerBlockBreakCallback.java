package net.swifthq.swiftapi.callbacks.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public interface PlayerBlockBreakCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerBlockBreakCallback> EVENT = EventFactory.createArrayBacked(PlayerBlockBreakCallback.class, (listeners) -> (blockpos) -> {
        List<Boolean> results = new ArrayList<>();
        for (PlayerBlockBreakCallback callback : listeners) {
            results.add(callback.breakBlock(blockpos));
        }
        return !results.contains(Boolean.FALSE);
    });

    boolean breakBlock(BlockPos blockPos);
}
