package net.swifthq.swiftapi.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public ServerWorld[] worlds;

	@ModifyConstant(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", constant = @Constant(intValue = 3))
	private int modifyWorldsSize(int i) {
		return 64;
	}

	@Inject(method = "getWorld(I)Lnet/minecraft/server/world/ServerWorld;", at = @At("HEAD"), cancellable = true)
	public void getWorldButNotHardCoded(int i, CallbackInfoReturnable<ServerWorld> cir) {
		if (i == -1) {
			cir.setReturnValue(worlds[1]);
		} else if (i == 1) {
			cir.setReturnValue(worlds[2]);
		} else if (i == 0) {
			cir.setReturnValue(worlds[0]);
		} else {
			cir.setReturnValue(worlds[i]);
		}
	}

}
