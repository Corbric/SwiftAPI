package net.swifthq.swiftapi.callbacks.world;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface BlockBreakCallback {

    Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(BlockBreakCallback.class, (listeners) -> (player, pos) -> {
        for (BlockBreakCallback callback : listeners) {
            if(callback.onBlockBroken(player, pos) == ActionResult.FAIL){
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    });

    ActionResult onBlockBroken(ServerPlayerEntity player, BlockPos pos);
}
