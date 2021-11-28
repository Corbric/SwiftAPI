package net.swifthq.swiftapi.mixin;

import net.swifthq.swiftapi.util.ActionResult;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.swifthq.swiftapi.callbacks.container.ClickContainerCallback;
import net.swifthq.swiftapi.callbacks.entity.player.ItemInteractCallback;
import net.swifthq.swiftapi.chat.ChatManager;
import net.swifthq.swiftapi.player.SwPlayer;
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
		HitResult result = ((SwPlayer) player).getLookingAt();
		if (packet.getDirectionId() == 255 && clickedBlock == Blocks.AIR && isNotRogueArmswing(result)) {
			if (!world.isClient) {
				BlockPos pos = result == null ? null : result.getBlockPos();
				if (ItemInteractCallback.EVENT.invoker().interactItem(player, pos, player.getMainHandStack()) == ActionResult.FAIL) {
					ci.cancel();
				}
			}
		}
	}

	private boolean isNotRogueArmswing(HitResult hitResult) {
		return hitResult == null || hitResult.type != HitResult.Type.BLOCK;
	}
}
