package net.swifthq.swiftapi.callbacks.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;
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
