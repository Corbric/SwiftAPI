package net.swifthq.swiftapi.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelGeneratorType;
import net.swifthq.swiftapi.callbacks.DimensionRegisterCallback;
import net.swifthq.swiftapi.dimension.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    DimensionRegistry dimensionRegistry = new DimensionRegistry();

    @ModifyConstant(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", constant = @Constant(intValue = 3))
    private static int replaceDimensionCount(int original) {
        return original + DimensionRegistry.FABRIC_DIMENSIONS.size();
    }

    @Inject(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", at = @At("HEAD"))
    public void addFabricDimensions(String world, String string, long l, LevelGeneratorType generatorType, String string2, CallbackInfo ci) {
        DimensionRegisterCallback.EVENT.invoker().registerDimension(dimensionRegistry);
    }
}
