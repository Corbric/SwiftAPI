package net.swifthq.swiftapi.mixin;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.BlockPlacementC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;
import net.swifthq.swiftapi.callbacks.container.ClickContainerCallback;
import net.swifthq.swiftapi.callbacks.entity.player.PlayerItemInteractCallback;
import net.swifthq.swiftapi.chat.ChatManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayerNetworkHandlerMixin {

	@Shadow
	public ServerPlayerEntity player;

	@Shadow
	@Final
	private MinecraftServer server;

	@Shadow
	private int messageCooldown;

	@Shadow
	public abstract void disconnect(String reason);

	@Inject(method = "onClickWindow", at = @At("HEAD"), cancellable = true)
	public void clickWindow(ClickWindowC2SPacket packet, CallbackInfo ci) {
		if (!ClickContainerCallback.EVENT.invoker().clickContainer(player, packet)) {
			ci.cancel();
		}
	}

	@Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Z)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	public void handleChat(ChatMessageC2SPacket packet, CallbackInfo ci, String string, Text text) {
		ci.cancel();
		ChatManager.getInstance().handleChat(string, player, this.server);
//        this.server.getPlayerManager().broadcastChatMessage(ChatManager.getInstance().handleChat(string, player, this.server), false);
		this.messageCooldown += 20;
		if (this.messageCooldown > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
			this.disconnect("disconnect.spam");
		}
	}

	@Inject(method = "onPlayerInteractBlock", at = @At("HEAD"), cancellable = true)
	public void handleAirInteractions(BlockPlacementC2SPacket packet, CallbackInfo ci) {
		World world = player.getWorld();
		Block clickedBlock = world.getBlockState(packet.getPos()).getBlock();
		if (packet.getDirectionId() == 255 && clickedBlock == Blocks.AIR && isNotRogueArmswing(player)) {
			if (!world.isClient) {
				if (PlayerItemInteractCallback.EVENT.invoker().interactItem(player, player.getMainHandStack()) == ActionResult.FAIL) {
					ci.cancel();
				}
			}
		}
	}

	private boolean isNotRogueArmswing(ServerPlayerEntity player) {
		Vec3d playerPos = new Vec3d(this.player.x, this.player.y + (double) this.player.getEyeHeight(), this.player.z);

		float pitch = this.player.pitch;
		float yaw = this.player.yaw;
		float f3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
		float f4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
		float f5 = -MathHelper.cos(-pitch * 0.017453292F);
		float f6 = MathHelper.sin(-pitch * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double reachDistance = player.interactionManager.getGameMode() == LevelInfo.GameMode.CREATIVE ? 5.0D : 4.5D;
		Vec3d endRaytracePoint = playerPos.add((double) f7 * reachDistance, (double) f6 * reachDistance, (double) f8 * reachDistance);
		HitResult hitResult = this.player.world.rayTrace(playerPos, endRaytracePoint, false, false, false);
		return hitResult == null || hitResult.type != HitResult.Type.BLOCK;
	}
}
