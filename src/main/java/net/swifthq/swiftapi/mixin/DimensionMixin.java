package net.swifthq.swiftapi.mixin;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Inject(method = "getById", at = @At("HEAD"), cancellable = true)
    private static void getDimensionByDimId(int id, CallbackInfoReturnable<Dimension> cir) {
        if (id > 2) {
            //TODO: make a registry for dimensions
            cir.setReturnValue(new OverworldDimension());
        }
    }
}
