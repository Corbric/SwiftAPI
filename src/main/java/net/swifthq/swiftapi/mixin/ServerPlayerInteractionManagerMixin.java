package net.swifthq.swiftapi.mixin;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;
import net.swifthq.swiftapi.callbacks.entity.player.PlayerItemInteractCallback;
import net.swifthq.swiftapi.callbacks.world.BlockBreakCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    private LevelInfo.GameMode gameMode;

    @Shadow public World world;

    @Inject(method = "method_6094", at = @At("HEAD"), cancellable = true)
    public void onBlockBreak(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode.isCreative() && this.player.getStackInHand() != null && this.player.getStackInHand().getItem() instanceof SwordItem) {
            this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(this.world, blockPos));
            cir.setReturnValue(true);
        } else {
            if(BlockBreakCallback.EVENT.invoker().onBlockBroken(player, blockPos) == ActionResult.FAIL){
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void onInteract(PlayerEntity playerEntity, World world, ItemStack itemStack, BlockPos blockPos, Direction direction, float x, float y, float z, CallbackInfoReturnable<Boolean> cir) {
        if(PlayerItemInteractCallback.EVENT.invoker().interactItem(playerEntity, itemStack) == ActionResult.FAIL){
            cir.setReturnValue(false);
        }
    }
}
