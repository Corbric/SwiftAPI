package net.swifthq.swiftapi.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerWorldManager;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.swifthq.swiftapi.callbacks.DimensionRegisterCallback;
import net.swifthq.swiftapi.dimension.DimensionRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    public ServerWorld[] worlds;
    @Shadow
    @Final
    public Profiler profiler;

    @Shadow
    public abstract LevelInfo.GameMode getDefaultGameMode();

    DimensionRegistry dimensionRegistry = new DimensionRegistry();

    @ModifyConstant(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", constant = @Constant(intValue = 3))
    private static int replaceDimensionCount(int original) {
        return original + DimensionRegistry.FABRIC_DIMENSIONS.size();
    }

    @Inject(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", at = @At("HEAD"))
    public void addFabricDimensions(String world, String string, long l, LevelGeneratorType generatorType, String string2, CallbackInfo ci) {
        DimensionRegisterCallback.EVENT.invoker().registerDimension(dimensionRegistry);
    }

    @Inject(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorldManager;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/world/ServerWorld;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void fabricDimensionSetup(String world, String string, long l, LevelGeneratorType generatorType, String string2, CallbackInfo ci, SaveHandler saveHandler, LevelInfo levelInfo3, LevelProperties levelProperties, int i) {
        if (i > 2) { //fabric dimensions are id world[2] and above
            this.worlds[i] = (ServerWorld) (new ServerWorld((MinecraftServer) (Object) this, saveHandler, levelProperties, 0, this.profiler)).getWorld();
            this.worlds[i].setPropertiesInitialized(levelInfo3);
            this.worlds[i].method_278(new ServerWorldManager((MinecraftServer) (Object) this, this.worlds[i]));
            this.worlds[i].getLevelProperties().setGamemode(this.getDefaultGameMode());
        }
    }
}
