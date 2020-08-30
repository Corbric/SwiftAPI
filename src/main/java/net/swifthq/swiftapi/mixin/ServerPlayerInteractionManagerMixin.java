package net.swifthq.swiftapi.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;
import net.swifthq.swiftapi.callbacks.player.PlayerBlockBreakCallback;
import net.swifthq.swiftapi.callbacks.player.PlayerItemInteractCallback;
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

    @Inject(method = "method_6094", at = @At("HEAD"), cancellable = true)
    public void onBlockBreak(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode.isCreative() && this.player.method_7107() != null && this.player.method_7107().getItem() instanceof SwordItem) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(PlayerBlockBreakCallback.EVENT.invoker().breakBlock(blockPos));
        }
    }

    @Inject(method = "method_6091", at = @At("HEAD"), cancellable = true)
    public void onInteract(PlayerEntity playerEntity, World world, ItemStack itemStack, BlockPos blockPos, Direction direction, float f, float g, float h, CallbackInfoReturnable<Boolean> cir) {
        // FIXME: in PlayerConnection line 717 has something to detect air right clicks. need to implement ASAP
        cir.setReturnValue(PlayerItemInteractCallback.EVENT.invoker().interactItem(playerEntity, itemStack));
    }
}
