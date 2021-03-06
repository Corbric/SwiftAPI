package net.swifthq.swiftapi.callbacks.entity.player.block;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface PlayerBlockBreakCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerBlockBreakCallback> EVENT = EventFactory.createArrayBacked(PlayerBlockBreakCallback.class, (listeners) -> (blockpos) -> {
        for (PlayerBlockBreakCallback callback : listeners) {
            if(callback.breakBlock(blockpos) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult breakBlock(BlockPos blockPos);
}
