package net.swifthq.swiftapi.mixin.callback;

import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.swifthq.swiftapi.callbacks.inventory.InventoryOpenCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "openInventory", at = @At("HEAD"), cancellable = true)
    public void openInventory(Inventory inventory, CallbackInfo ci){
        if(InventoryOpenCallback.EVENT.invoker().onOpenInventory(inventory) == ActionResult.FAIL){
            ci.cancel();
        }
    }

}
